package l1j.server.web;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.AuthIP;
import l1j.server.Config;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.IpTable.BanIpReason;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.utils.FormatterUtil;

/**
 * Web Server pipe open
 * @author LinOffice
 */
public class WebServer {
	private static Logger _log = Logger.getLogger(WebServer.class.getName());
	private static WebServer _instance;
	public static WebServer getInstance(){
		if (_instance == null) {
			_instance = new WebServer();
		}
		return _instance;
	}
	private static final List<String> REALTIME_BLOCK_ADDR = new ArrayList<>();

	private boolean 		_isActive;
	private HashMap<Integer, WebChannelFuture> _openeds;
	private EventLoopGroup	_bossGroup;
	private EventLoopGroup	_workGroup;
	
	private WebServer(){
		_openeds	= new HashMap<Integer, WebChannelFuture>();
		_isActive	= true;
		_bossGroup	= new NioEventLoopGroup(1);
		_workGroup	= new NioEventLoopGroup(2);
		Listener.initialized();
	}
	
	public boolean isActive(){
		return _isActive;
	}
	
	public void initialize(WebChannelPipeline pipe){
		if (!_isActive) {
			System.out.println("The web server could not be started.");
			return;
		}
		if (_openeds.containsKey(pipe.get_port())) {
			throw new RuntimeException(String.format("already bind port %d", pipe.get_port()));
		}
		
		try {
			WebChannelFuture future = new WebChannelFuture();
			future.pipe = pipe;
			ServerBootstrap serverBootstrap = new ServerBootstrap()
				.group(_bossGroup, _workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>(){
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						if (!Config.WEB.WEB_SERVER_ENABLE || !_isActive) {
							ch.close();
							return;
						}
						String address = ch.remoteAddress().getAddress().getHostAddress();
						// 벤 아이피
						if (IpTable.isBannedIp(address)) {
							ch.close();
							return;
						}
						// 실시간 차단된 아이피
						if (Config.WEB.REALTIME_BLOCK && REALTIME_BLOCK_ADDR.contains(address)) {
							ch.close();
							return;
						}
						// VPN 또는 해외 아이피
						if (Config.SERVER.IP_PROTECT && !AuthIP.isWhiteIp(address)) {
							not_white_addr(ch, address);
							return;
						}
						pipe.initialize(ch, ch.pipeline());
					}
				});
			
			future.future = serverBootstrap.bind(pipe.get_port());
			_openeds.put(pipe.get_port(), future);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			close();
		}
	}
	
	/**
	 * VPN 또는 해외 아이피 차단
	 * @param ch
	 * @param addr
	 */
	void not_white_addr(SocketChannel ch, String addr) {
		String message = String.format("[%s] VPN_OR_BLACK_IP_BLOCK : IP(%s)", FormatterUtil.get_formatter_time(), addr);
		System.out.println(String.format("[APPCENTER] %s\r\n", message));
		LoggerInstance.getInstance().addWebserver(message);
		IpTable.getInstance().insert(addr, BanIpReason.WEB_NOT_AUTH_IP);
		ch.close();
	}
	
	/**
	 * 실시간 차단 IP 추가
	 * @param addr
	 */
	public static void addBlockAddr(String addr){
		if (!Config.WEB.REALTIME_BLOCK) {
			return;
		}
		REALTIME_BLOCK_ADDR.add(addr);
	}
	
	public void close(){
		try {
			_isActive = false;
			for (WebChannelFuture future : _openeds.values()) {
				future.closeFuture();
			}
			Listener.destroyed();
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			if (_bossGroup != null) {
				_bossGroup.shutdownGracefully();
				_bossGroup = null;
			}
			if (_workGroup != null) {
				_workGroup.shutdownGracefully();
				_workGroup = null;
			}
		}
	}
	
	private static class WebChannelFuture {
		private ChannelFuture future;
		private WebChannelPipeline pipe;
		
		private void closeFuture() {
			pipe.close();
			future.channel().close().awaitUninterruptibly();
		}
	}
}


package l1j.server.server;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.server.controller.LoginController;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.IpTable.BanIpReason;
import xnetwork.Acceptor;
import xnetwork.AcceptorHandler;
import xnetwork.SelectorThread;

public class LoginServer implements AcceptorHandler {
	private int SELECT_THREAD_COUNT;
	private SelectorThread[] _st;
	private int _ioThreadIndex;
	private ConcurrentHashMap<String, Integer> acceptCount;
	
	private static class newInstance {
		public static final LoginServer INSTANCE = new LoginServer();
	}
	public static LoginServer getInstance(){
		return newInstance.INSTANCE;
	}
	private LoginServer(){}
	
	public void initialize() {
		try {
			acceptCount = new ConcurrentHashMap<String, Integer>();
			SELECT_THREAD_COUNT = Config.SERVER.SELECT_THREAD_COUNT;
			_st = new SelectorThread[SELECT_THREAD_COUNT];
			for (int i = 0; i < SELECT_THREAD_COUNT; ++i) {
				_st[i] = new SelectorThread();
			}
			Acceptor ac = new Acceptor(Config.SERVER.LOGIN_SERVER_PORT, _st[0], this);
			ac.startAccept();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onAccept(Acceptor acceptor, final SocketChannel sc) {
		try {
			Socket connection	= sc.socket();
			if (GameServer.getInstance()._shutdownThread != null) {
				connection.close();
				return;
			}
			String host			= connection.getInetAddress().getHostAddress();
			int port			= connection.getPort();
			if (IpTable.isBannedIp(host)) {
				//print(String.format("IP차단 사용자가 접속을 시도하여 차단했습니다. 차단사유(%s)", IpTable.getBanIp(host).getReason().getReason()), host, port);
				print(String.format("An IP blocked user attempted to connect and was blocked. Reason for blocking (%s)", IpTable.getBanIp(host).getReason().getReason()), host, port);
				connection.close();
				return;
			}
			if (port <= Config.SERVER.WELLKNOWN_PORT) {
				IpTable.getInstance().insert(host, BanIpReason.WELLKNOWN_PORT);
				//print("사용자가 WellKnown-Port로 접속을 시도하여 차단했습니다.", host, port);
				print("The user attempted to connect to WellKnown-Port and was blocked.", host, port);
				connection.close();
				return;
			}
			
			Integer count = acceptCount.get(host);
			if (count == null) {
				acceptCount.put(host, 1);
				Runnable r = () -> {
					monitor(host, port);
				};
				GeneralThreadPool.getInstance().schedule(r, Config.SERVER.ACCEPT_OVER_MONITOR_INTERVAL * 1000);
			} else {
				acceptCount.put(host, ++count);
			}
			
			connection.setSendBufferSize(2048);
			connection.setReceiveBufferSize(2048);

			++_ioThreadIndex;
			final SelectorThread st = _st[_ioThreadIndex % SELECT_THREAD_COUNT];
			st.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						new GameClient(sc, st);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onError(Acceptor acceptor, Exception ex) {
		// do nothing
	}
	
	private void print(String message, String ip, int port){
		System.out.println(String.format("[LOGIN_SERVER] %s\r\nIP(%s), PORT(%d)", message, ip, port));
	}
	
	void monitor(String address, int port){
		try {
			synchronized (acceptCount) {
				Integer count = acceptCount.get(address);
				if (count != null && count >= Config.SERVER.ACCEPT_OVER_LIMIT_COUNT) {
					IpTable.getInstance().insert(address, BanIpReason.CONNECTION_OVER);
					//print(String.format("사용자가 %d초동안 %d번 접속을 시도하여 차단했습니다.", Config.SERVER.ACCEPT_OVER_MONITOR_INTERVAL, count), address, port);
					print(String.format("The user attempted to connect %d times in %d seconds and was blocked.", Config.SERVER.ACCEPT_OVER_MONITOR_INTERVAL, count), address, port);
					for (GameClient client : LoginController.getInstance().getAllAccounts()) {
						if (client.getIp().equalsIgnoreCase(address)) {
							client.close();
						}
					}
				}
				acceptCount.remove(address);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}


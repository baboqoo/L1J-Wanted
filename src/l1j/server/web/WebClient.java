package l1j.server.web;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.IpTable.BanIpReason;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.Dispatcher;
import l1j.server.web.dispatcher.DispatcherLoader;
import l1j.server.web.http.HttpModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpView;

/**
 * WebClient Handler
 * @author LinOffice
 */
public class WebClient extends SimpleChannelInboundHandler<FullHttpMessage> {
	private static Logger _log = Logger.getLogger(WebClient.class.getName());
	private HttpRequestModel request;
	private String uri;
	private String addr;
	
	private static final HttpResponse NOT_FOUND_RESPONSE;
	static {
		NOT_FOUND_RESPONSE	= new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.EMPTY_BUFFER);
		NOT_FOUND_RESPONSE.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
		NOT_FOUND_RESPONSE.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=utf-8");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		try {
			ctx.close();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			LoggerInstance.getInstance().addWebserver(e.getMessage());
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
		try {
 			if (msg instanceof HttpRequest) {
				readRequest(ctx, (HttpRequest) msg);
			}
			if (msg instanceof HttpContent) {
				readContent(ctx, msg);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			LoggerInstance.getInstance().addWebserver(e.getMessage());
		}
	}

	/**
	 * 요청(request)을 읽는다
	 * @param ctx
	 * @param request
	 */
	private void readRequest(ChannelHandlerContext ctx, HttpRequest request) {
		this.request = new HttpRequestModel(request, ctx);
		HttpView.view100ContinueExpected(ctx, this.request);
	}

	/**
	 * 요청(request)에 대해 응답(response)한다.
	 * @param ctx
	 * @param msg
	 */
	private void readContent(ChannelHandlerContext ctx, FullHttpMessage msg) {
		if (msg instanceof LastHttpContent) {
			try {
				uri			= request.get_request_uri();
				addr		= request.get_remote_address_string();
				if (Config.WEB.WEB_LOG_PRINT) {
					printLog(request);
				}
				
				// uri길이 제한
				if (uri.length() > Config.WEB.LIMIT_URI_LENGTH) {
					uriLimitLengthOver(ctx);
					return;
				}
				
				// 허용하지 않는 요청 또는 스크립트 공격 차단
				if (request.is_bad_request()) {
					badRequest(ctx);
					return;
				}
				
				// 브라우저 검증
				if (Config.WEB.BROWSER_INGAME_ONLY && !request.isIngame() && !request.isLauncher() 
						&& !DispatcherLoader.isPassUri(uri) 
						&& !WebAuthorizatior.is_auth_address(request.get_remote_address_string())) {
					notAuthBrowser(ctx);
					return;
				}
				
				HttpModel model = Dispatcher.dispatch(request, msg);
				HttpView.view(ctx, request, model);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				LoggerInstance.getInstance().addWebserver(e.getMessage());
				ctx.close();
			}
		}
	}
	
	/**
	 * 승인되지 않은 브라우저
	 * @param ctx
	 */
	private void notAuthBrowser(ChannelHandlerContext ctx){
		print(ctx, String.format("Attempt to call from web URL [%s]", uri));
		WebServer.addBlockAddr(addr);
		sendNotFoundResponse(ctx);
	}
	
	/**
	 * 부정 요청 호출
	 * @param ctx
	 */
	private void badRequest(ChannelHandlerContext ctx){
		IpTable.getInstance().insert(addr, BanIpReason.WEB_ATTACK_REQUEST);
		if (request.is_bad_user_agent()) {
			print(ctx, String.format("Fraudulent request call attempt URL[%s], USER_AGENT [%s]", uri, request.get_user_agent()));
		} else {
			print(ctx, String.format("Fraudulent request call attempt URL [%s]", uri));
		}
		WebServer.addBlockAddr(addr);
		sendNotFoundResponse(ctx);
	}
	
	/**
	 * 제한 길이가 초과된  URI
	 * @param ctx
	 */
	private void uriLimitLengthOver(ChannelHandlerContext ctx) {
		IpTable.getInstance().insert(addr, BanIpReason.WEB_URI_LENGTH_OVER);
		print(ctx, String.format("URI call attempt with exceeded length URL [%s]", uri));
		WebServer.addBlockAddr(addr);
		sendNotFoundResponse(ctx);
	}
	
	/**
	 * 404 읍답 반환(페이지를 찾을 수 없습니다)
	 * @param ctx
	 */
	private void sendNotFoundResponse(ChannelHandlerContext ctx){
		ctx.write(NOT_FOUND_RESPONSE);
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	
	public static void print(ChannelHandlerContext ctx, String message) {
		InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
		print(inetAddr.getAddress().getHostAddress(), inetAddr.getPort(), message);
	}

	public static void print(String ip, int port, String message) {
		System.out.println(String.format("[APPCENTER][%s][%s:%d] %s\r\n", getLocalTime(), ip, port, message));
		LoggerInstance.getInstance().addWebserver(String.format("[%s:%d] %s", ip, port, message));
	}
	
	private static void printLog(HttpRequestModel request){
		System.out.println(String.format("[APPCENTER][%s]\r\n%s\r\n", getLocalTime(), request.toString()));
	}

	private static final SimpleDateFormat formatter = new SimpleDateFormat(StringUtil.DateFormatStringSeconds);
	public static String getLocalTime() {
		return formatter.format(new GregorianCalendar().getTime());
	}
}


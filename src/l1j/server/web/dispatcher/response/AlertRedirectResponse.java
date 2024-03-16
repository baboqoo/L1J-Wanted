package l1j.server.web.dispatcher.response;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class AlertRedirectResponse extends HttpResponseModel {
	private static final String DOCUMENT = "<html><body><script>alert('%s'); location.href='%s';</script></body></html>";
	String message;
	String location;
	
	public AlertRedirectResponse(HttpRequestModel request, String message, String location) {
		super(request, null);
		this.message	= message;
		this.location	= location;
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String html				= String.format(DOCUMENT, message, location);
		ByteBuf response_buff	= Unpooled.wrappedBuffer(html.getBytes(CharsetUtil.UTF_8));
		HttpResponse response	= new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, response_buff);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response_buff.readableBytes());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, HTML_CONTENT_TYPE);
		return response;
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return null;
	}

}


package l1j.server.web.http;

import l1j.server.server.utils.StringUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

public class HttpView {
	public static void view100ContinueExpected(ChannelHandlerContext ctx, HttpRequestModel request){
		if (HttpUtil.is100ContinueExpected(request)) {
			view100ContinueExpectedInternal(ctx);
		}
	}
	
	public static void view(ChannelHandlerContext ctx, HttpRequestModel request, HttpModel model) throws Exception{
		if (model == null){
			viewEof(ctx);
			return;
		}
		HttpResponse response = model.getResponse();
		if (response != null) {
			viewInternal(ctx, request, response);
		} else {
			viewEof(ctx);
		}
	}

	private static final FullHttpResponse view100ContiinueCached = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE).duplicate();
	private static void view100ContinueExpectedInternal(ChannelHandlerContext ctx) {
		ctx.write(view100ContiinueCached.retainedDuplicate());
	}

	private static void viewInternal(ChannelHandlerContext ctx, HttpRequestModel request, HttpResponse response) {
		boolean keepAlive = request.is_keep_alive();
		if (keepAlive) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			ctx.write(response);
			ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		} else {
			ctx.write(response);
		}
	}

	private static void viewEof(ChannelHandlerContext ctx){
		ctx.write(StringUtil.Eof);
	}
}


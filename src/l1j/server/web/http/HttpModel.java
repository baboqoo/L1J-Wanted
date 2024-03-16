package l1j.server.web.http;

import io.netty.handler.codec.http.HttpResponse;

public interface HttpModel {
	public HttpResponse getResponse() throws Exception;
}


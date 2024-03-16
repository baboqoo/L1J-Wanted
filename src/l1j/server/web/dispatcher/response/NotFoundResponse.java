package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class NotFoundResponse extends HttpResponseModel {
	public NotFoundResponse(HttpRequestModel request) {
		super(request, null);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		//return notFound("페이지를 찾을 수 없습니다.");
		return notFound("Page not found.");
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return null;
	}

}


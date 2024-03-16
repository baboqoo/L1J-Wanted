package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class SendRedirectResponse extends HttpResponseModel {
	private String location;
	public SendRedirectResponse(HttpRequestModel request, String location) {
		super(request, null);
		this.location	= location;
	}

	@Override
	public HttpResponse get_response() throws Exception {
		return sendRedirect(location);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return null;
	}

}


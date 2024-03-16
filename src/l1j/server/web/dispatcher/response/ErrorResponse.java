package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class ErrorResponse extends HttpResponseModel {
	String message;
	int errorCode;
	
	public ErrorResponse() {}
	public ErrorResponse(HttpRequestModel request, DispatcherModel model, String message, int errorCode) {
		super(request, model);
		this.message	= message;
		this.errorCode	= errorCode;
	}

	@Override
	public HttpResponse get_response() throws Exception {
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(SUGGEST_ENABLE_PAIR);
		params.add(get_now_pair());
		params.add(new KeyValuePair<String, String>("{ERROR_MESSAGE}",	message));
		params.add(new KeyValuePair<String, String>("{ERROR_CODE}",		String.valueOf(errorCode)));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return null;
	}
}


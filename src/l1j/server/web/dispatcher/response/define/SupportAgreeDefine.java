package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.SupportResponse;
import l1j.server.web.dispatcher.response.account.AccountDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

public class SupportAgreeDefine extends HttpJsonModel {
	public SupportAgreeDefine() {}
	private SupportAgreeDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String result = FALSE_JSON;
		if (account != null) {
			account.setTermsAgree(true);
			if (AccountDAO.getInstance().termsAgreeUpdate(account)) {
				result = new Gson().toJson(SupportResponse.getMsg(true));
			}
		}
		return create_response_json(HttpResponseStatus.OK, result);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new SupportAgreeDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


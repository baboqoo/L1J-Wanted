package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.web.WebAuthorizatior;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.LoginFactory;
import l1j.server.web.http.HttpCookieSetter;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class LogoutDefine extends HttpJsonModel {
	public LogoutDefine() {}
	private LogoutDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		if (request.isLauncher()) {
			LoginFactory.remove_launcher(account);
		} else {
			LoginFactory.remove_web(account);
		}
		if (account.isGm()) {
			WebAuthorizatior.remove_auth_address(account.getIp());
		}
		// 쿠키 제거
		HttpResponse response		= create_response_json(HttpResponseStatus.OK, TRUE_JSON);
		HttpCookieSetter.remove(response, LoginFactory.COOKIE_AUTH_TOKEN);
		return response;
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new LogoutDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


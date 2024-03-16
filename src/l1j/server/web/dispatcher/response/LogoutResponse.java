package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import l1j.server.web.WebAuthorizatior;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.LoginFactory;
import l1j.server.web.http.HttpCookieSetter;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class LogoutResponse extends HttpResponseModel {
	public LogoutResponse() {}
	private LogoutResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	
	@Override
	public HttpResponse get_response() throws Exception {
		HttpResponse response = sendRedirect("/index");
		if (account == null) {
			return response;
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
		HttpCookieSetter.remove(response, LoginFactory.COOKIE_AUTH_TOKEN);
		return response;
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new LogoutResponse(request, model);
	}
}


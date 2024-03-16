package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountDAO;
import l1j.server.web.dispatcher.response.account.AccountVO;
import l1j.server.web.dispatcher.response.account.LoginFactory;
import l1j.server.web.http.HttpCookieSetter;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class LoginWebDefine extends HttpJsonModel {
	public LoginWebDefine() {}
	private LoginWebDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post	= request.get_post_datas();
		String login				= post.get("loginname");
		String password				= post.get("password");
		if (StringUtil.isNullOrEmpty(login) || StringUtil.isNullOrEmpty(password)) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		AccountVO vo				= AccountDAO.getInstance().getAccount(login, password);
		if (vo == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		vo.setIp(request.get_remote_address_string());
		if (request.isLauncher()) {
			LoginFactory.put_launcher(vo);
		} else {
			LoginFactory.put_web(vo);
		}
		// 쿠키 생성
		HttpResponse response		= create_response_json(HttpResponseStatus.OK, TRUE_JSON);
		HttpCookieSetter.set(response, -1, LoginFactory.COOKIE_AUTH_TOKEN, vo.getAuthToken());
		return response;
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new LoginWebDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


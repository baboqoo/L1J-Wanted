package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import com.google.gson.Gson;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.WebAuthorizatior;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountDAO;
import l1j.server.web.dispatcher.response.account.AccountVO;
import l1j.server.web.dispatcher.response.account.LoginFactory;
import l1j.server.web.http.HttpCookieSetter;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class LoginAdminDefine extends HttpJsonModel {
	public LoginAdminDefine() {}
	private LoginAdminDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	
	static String SUCCESS_JSON, FAILURE_JSON, FAILURE_AUTH_JSON;
	static {
		AuthData data		= new AuthData();
		FAILURE_JSON		= new Gson().toJson(data);
		
		data.result_code	= 2;
		FAILURE_AUTH_JSON	= new Gson().toJson(data);
		
		data.result_code	= 1;
		data.location		= "/gm";
		SUCCESS_JSON		= new Gson().toJson(data);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post	= request.get_post_datas();
		String login				= post.get("loginname");
		String password				= post.get("password");
		if (StringUtil.isNullOrEmpty(login) || StringUtil.isNullOrEmpty(password)) {
			return create_response_json(HttpResponseStatus.OK, FAILURE_JSON);
		}
		AccountVO user				= AccountDAO.getInstance().getAccount(login, password);
		if (user == null) {
			return create_response_json(HttpResponseStatus.OK, FAILURE_JSON);
		}
		if (!user.isGm()) {
			return create_response_json(HttpResponseStatus.OK, FAILURE_AUTH_JSON);
		}
		user.setIp(request.get_remote_address_string());
		LoginFactory.put_web(user);
		WebAuthorizatior.add_auth_address(user.getIp());
		
		// 쿠키 생성
		HttpResponse response		= create_response_json(HttpResponseStatus.OK, SUCCESS_JSON);
		HttpCookieSetter.set(response, -1, LoginFactory.COOKIE_AUTH_TOKEN, user.getAuthToken());
		return response;
	}
	
	public static class AuthData {
		int result_code = 0;
		String location = StringUtil.EmptyString;
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new LoginAdminDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.connector.HttpAccountManager;
import l1j.server.web.http.connector.HttpLoginSession;

public class GameStartDefine extends HttpJsonModel {
	public GameStartDefine() {}
	private GameStartDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		// 런처 검증
		if (!request.isLauncher()) {
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		// 계정 검증
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
		}
		// 개발 환경
		if (Config.SERVER.CONNECT_DEVELOP_LOCK && !account.isGm()) {
			return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
		}
		// 승인 토큰 검증
		String authToken			= request.read_parameters_at_once("token");
		if (StringUtil.isNullOrEmpty(authToken)) {
			return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
		}
		// 로그인 세션 검증
		HttpLoginSession session	= HttpAccountManager.get(authToken);
		if (session == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_6_JSON);
		}
		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GameStartDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


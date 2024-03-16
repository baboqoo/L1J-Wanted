package l1j.server.web.dispatcher.response.define;

import java.util.Map;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountDAO;
import l1j.server.web.dispatcher.response.account.AccountVO;
import l1j.server.web.dispatcher.response.account.LoginFactory;
import l1j.server.web.http.HttpCookieSetter;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

/**
 * 인게임에서 사용될 계정 데이터 생성 
 * 세션 쿠키를 등록한다.
 * @author LinOffice
 */
public class LoginIngameAuthDefine extends HttpJsonModel {
	public LoginIngameAuthDefine() {}
	private LoginIngameAuthDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		try {
			if (!request.isIngame()) {
				return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
			}
			
			Map<String, String> post	= request.get_post_datas();
			String charname				= post.get("charname");
			String auth_cnt				= post.get("auth_cnt");
			if (StringUtil.isNullOrEmpty(charname) || StringUtil.isNullOrEmpty(auth_cnt)) {
				return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
			}
			L1PcInstance pc = L1World.getInstance().getPlayer(charname);
			if (pc == null) {
				return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
			}
			
			// 승인 검증 횟수 초과
			if (Integer.parseInt(auth_cnt) > Config.WEB.INGAME_LOGIN_AUTH_MAX_COUNT) {
				pc.ingame_login_auth_delay = pc.ingame_login_auth = false;
				return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
			}
			
			// 결정 전(재호출)
			if (pc.ingame_login_auth_delay) {
				return create_response_json(HttpResponseStatus.OK, CODE_6_JSON);
			}
			
			// 승인 여부 결정 완료
			boolean auth_flag = pc.ingame_login_auth;
			
			// 승인 거절
			if (!auth_flag) {
				return create_response_json(HttpResponseStatus.OK, CODE_7_JSON);
			}
			
			// 승인 완료
			pc.ingame_login_auth = false;
			GameClient client = pc.getNetConnection();
			AccountVO result = AccountDAO.getInstance().getAccount(client.getAccount(), client.getActiveChar());
			
			result.setIp(request.get_remote_address_string());
			result.setFirstChar(pc.getId());
			
			LoginFactory.put_ingame(result, client);
			
			// 쿠키 생성
			HttpResponse response		= create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
			HttpCookieSetter.set(response, -1, LoginFactory.COOKIE_AUTH_TOKEN, result.getAuthToken());
			return response;
		} catch (Exception e) {
			return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
		}
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new LoginIngameAuthDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


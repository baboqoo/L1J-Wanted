package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
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
 * 인게임에서 최초 승인 검증
 * 검증 완료 시 세션 쿠키를 등록한다.
 * @author LinOffice
 */
public class LoginIngameInitAuthDefine extends HttpJsonModel {
	public LoginIngameInitAuthDefine() {}
	private LoginIngameInitAuthDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		try {
			if (!request.isIngame()) {
				return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
			}
			String UID				= request.read_post("UID");
			if (StringUtil.isNullOrEmpty(UID)) {
				return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
			}
			L1PcInstance pc = null;
			for (L1PcInstance user : L1World.getInstance().getAllPlayers()) {
				if (user.ingame_init_auth_uid == null) {
					continue;
				}
				if (user.ingame_init_auth_uid.equals(UID)) {
					pc = user;
					break;
				}
			}
			if (pc == null) {
				return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
			}
			
			// 승인 완료
			pc.ingame_init_auth_uid = null;// 파기
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
		return new LoginIngameInitAuthDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


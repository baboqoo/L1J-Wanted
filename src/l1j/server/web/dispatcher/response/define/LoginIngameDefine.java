package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

/**
 * 인게임에서 사용될 계정 데이터 검증
 * @author LinOffice
 */
public class LoginIngameDefine extends HttpJsonModel {
	public LoginIngameDefine() {}
	private LoginIngameDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		try {
			// 인게임 검증
			if (!request.isIngame()) {
				return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
			}
			Map<String, String> post	= request.get_post_datas();
			String login				= post.get("loginname");
			String password				= post.get("password");
			String charname				= post.get("charname");
			
			// 파라미터 검증
			if (StringUtil.isNullOrEmpty(login) || StringUtil.isNullOrEmpty(password) || StringUtil.isNullOrEmpty(charname)) {
				return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
			}
			
			// 활성화 클라이언트 검증
			GameClient client = get_client_from_account_name(login, null);
			if (client == null) {
				return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
			}
			
			// 계정 검증
			if (client.getAccount() == null || !client.getAccount().getPassword().equals(password)) {
				return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
			}
			
			// 활성화 캐릭터 검증
			L1PcInstance pc = L1World.getInstance().getPlayer(charname);
			if (pc == null || client.getActiveChar() != pc) {
				return create_response_json(HttpResponseStatus.OK, CODE_6_JSON);
			}
			
			// 이미 승인 진행중인지 검증
			if (pc.ingame_login_auth_delay) {
				return create_response_json(HttpResponseStatus.OK, CODE_7_JSON);
			}

			// 인게임에서 승인 여부를 결정한다.(C_Attr 유저 출력된 메세지 폼 결정 진행)
			S_MessageYN yn = new S_MessageYN(C_Attr.MSGCODE_INGAME_LOGIN_AUTH, C_Attr.YN_MESSAGE_CODE, 
					//String.format("'%s'에서 요청한 앱센터 로그인을 승인하시겠습니까?", request.get_remote_address_string()));
					String.format("Would you like to approve the App Center login requested from '%s'?", request.get_remote_address_string()));
			pc.sendPackets(yn, true);
			pc.ingame_login_auth_delay	= true;
			
			// 최대 검증 시간 후 초기화
			Runnable r = () -> {
				if (pc != null) {
					pc.ingame_login_auth_delay = pc.ingame_login_auth = false;
				}
			};
			GeneralThreadPool.getInstance().schedule(r, (Config.WEB.INGAME_LOGIN_AUTH_MAX_COUNT) * 1000);
			return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
		} catch (Exception e) {
			return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
		}
	}
	

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new LoginIngameDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountVO;
import l1j.server.web.dispatcher.response.account.CharacterVO;
import l1j.server.web.dispatcher.response.account.LoginFactory;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class GMUserDeleteDefine extends HttpJsonModel {
	public GMUserDeleteDefine() {}
	private GMUserDeleteDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	
	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
		}
		
		Map<String, String> post	= request.get_post_datas();
		String account_name			= post.get("acc_name");
		String name					= post.get("user_name");
		
		// 유저가 접속종
		L1PcInstance user			= L1World.getInstance().getPlayer(name);
		if (user != null) {
			user.getNetConnection().kick();// 접속중인 캐릭터 강제종료
		}
		
		AccountVO vo				= get_account_vo(account_name);
		if (vo != null) {
			for (CharacterVO cha : vo.getCharList()) {
				if (cha.getName().equals(name)) {
					vo.getCharList().remove(cha);
					break;
				}
			}
		}
		
		CharacterTable character	= CharacterTable.getInstance();
		character.deleteCharacter(account_name, name);
		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
	}
	
	AccountVO get_account_vo(String account_name) {
		for (AccountVO users : LoginFactory.get_ingame_accounts().values()) {
			if (users.getName().equals(account_name)) {
				return users;
			}
		}
		for (AccountVO users : LoginFactory.get_web_accounts().values()) {
			if (users.getName().equals(account_name)) {
				return users;
			}
		}
		return null;
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GMUserDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


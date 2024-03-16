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
import l1j.server.web.dispatcher.response.account.CharacterInventoryVO;
import l1j.server.web.dispatcher.response.account.CharacterVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class GMItemDeleteDefine extends HttpJsonModel {
	public GMItemDeleteDefine() {}
	private GMItemDeleteDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	
	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		
		Map<String, String> post	= request.get_post_datas();
		String user_name			= post.get("user_name");
		int id						= Integer.parseInt(post.get("item_id"));
		
		// 유저가 접속종
		L1PcInstance user			= L1World.getInstance().getPlayer(user_name);
		if (user != null) {
			boolean result = user.getInventory().removeItem(id) > 0;
			if (result) {
				delete_vo(user_name, user.getAccount().getAccountVO(), id);
			}
			return create_response_json(HttpResponseStatus.OK, result ? TRUE_JSON : FALSE_JSON);
		}
		
		// 유저가 접속중이지 않음
		boolean result = CharacterTable.getInstance().delete_inventory_item(id);
		return create_response_json(HttpResponseStatus.OK, result ? TRUE_JSON : FALSE_JSON);
	}
	
	void delete_vo(String name, AccountVO accountVO, int id) {
		if (accountVO == null) {
			return;
		}
		CharacterInventoryVO del = null;
		for (CharacterVO vo : accountVO.getCharList()) {
			if (!vo.getName().equals(name)) {
				continue;
			}
			for (CharacterInventoryVO inv : vo.getInventory()) {
				if (inv.getId() != id) {
					continue;
				}
				del = inv;
				break;
			}
			vo.getInventory().remove(del);
			return;
		}
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GMItemDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


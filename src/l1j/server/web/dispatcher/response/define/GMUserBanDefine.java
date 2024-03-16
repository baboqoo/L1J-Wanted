package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import l1j.server.server.command.executor.L1SKick;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class GMUserBanDefine extends HttpJsonModel {
	public GMUserBanDefine() {}
	private GMUserBanDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	
	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
		}
		
		Map<String, String> post	= request.get_post_datas();
		String name					= post.get("user_name");
		
		// 유저가 접속종
		L1PcInstance user			= L1World.getInstance().getPlayer(name);
		if (user == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		
		AccountVO vo				= user.getAccount().getAccountVO();
		if (vo != null) {
			if (vo.isBan()) {
				return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
			}
			vo.setBan(true);
		}
		//L1SKick.getInstance().execute(null, "추방", String.format("%s %d", name, IpTable.BanIpReason.ETC.getCode()));
		L1SKick.getInstance().execute(null, "skick", String.format("%s %d", name, IpTable.BanIpReason.ETC.getCode()));
		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GMUserBanDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.sql.Timestamp;
import java.util.Map;

import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.alim.AlimDAO;
import l1j.server.web.dispatcher.response.alim.AlimVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class CashChargeDefine extends HttpJsonModel {
	public CashChargeDefine() {}
	private CashChargeDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (!request.isIngame()) {
			return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
		}
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
		}
		L1PcInstance pc		= getIngamePlayer();
		if (pc == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
		}
		Account	acc			= pc.getAccount();
		Map<String, String> post = request.get_post_datas();
		String imp_uid		= post.get("imp_uid");
		String merchant_uid	= post.get("merchant_uid");
		String card_auth_id	= post.get("card_auth_id");
		String price		= post.get("price");
		int count			= Integer.parseInt(post.get("count"));
		int afterCoin		= acc.getNcoin() + count;
		acc.setNcoin(afterCoin);
		acc.updateNcoin();

		Timestamp ts = new Timestamp(System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		sb.append("N Coin ").append(count);
		sb.append("<br/>Purchase completed<span class='hidden'><br/>Unique ID:").append(imp_uid);
		sb.append("<br/>Store Transaction ID:").append(merchant_uid);
		sb.append("<br/>Card Approval Nº:").append(card_auth_id);
		sb.append("<br/>Payment Ammount:").append(price);
		sb.append("<br/>Quantity:").append(count);
		sb.append("<br/>Purchase Time").append(ts);
		sb.append("</span>");
		AlimVO alim = new AlimVO(acc.getName(), sb.toString(), 2, ts, false);
		AlimDAO.getInstance().insert(alim);

		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CashChargeDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


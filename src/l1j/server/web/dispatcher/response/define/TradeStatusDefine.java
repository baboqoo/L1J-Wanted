package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeStatus;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class TradeStatusDefine extends HttpJsonModel {
	public TradeStatusDefine() {}
	private TradeStatusDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String result	= CODE_0_JSON;
		Map<String, String> post = request.get_post_datas();
		int rownum		= Integer.parseInt(post.get("rownum"));
		TradeVO vo		= TradeDAO.getTrade(rownum);
		if (vo != null) {
			result = vo.getStatus().equals(TradeStatus.SELL) ? CODE_1_JSON : CODE_2_JSON;
		}
		return create_response_json(HttpResponseStatus.OK, result);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeStatusDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


package l1j.server.web.dispatcher.response.define;

import java.util.List;
import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeStatus;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class TradeDefine extends HttpJsonModel {
	public TradeDefine() {}
	private TradeDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		TradeStatus status	= TradeStatus.getCode(post.get("status"));
		List<TradeVO> list	= status != null ? TradeDAO.getList(status) : TradeDAO.getList();
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(list));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


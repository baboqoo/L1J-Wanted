package l1j.server.web.dispatcher.response.define;

import l1j.server.web.SearchConstruct;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.goods.GoodsDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class GoodsDefine extends HttpJsonModel {
	public GoodsDefine() {}
	private GoodsDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String query = request.read_post("query");
		String jsonString = new Gson().toJson(query.equals(SearchConstruct.SEARCH_ALL) ? GoodsDAO.getGoods().values() : GoodsDAO.getKeywordGoods(query));
		return create_response_json(HttpResponseStatus.OK, jsonString);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GoodsDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


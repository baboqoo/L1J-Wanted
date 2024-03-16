package l1j.server.web.dispatcher.response.define;

import java.util.List;
import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class SearchListDefine extends HttpJsonModel {
	public SearchListDefine() {}
	private SearchListDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String searchCode	= post.get("searchCode");
		String query		= post.get("query");
		List<?> list = null;
		switch(searchCode) {
		case "2":list = BoardDAO.getSearchList(99999, "title,contents", query);break;
		case "3":list = TradeDAO.getSearchList(99999, "title,contents", query);break;
		case "4":list = ContentDAO.getSearchList(99999, "title,contents", query);break;
		}
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(list));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new SearchListDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


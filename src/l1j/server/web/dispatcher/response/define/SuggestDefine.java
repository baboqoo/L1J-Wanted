package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.PowerbookType;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

public class SuggestDefine extends HttpJsonModel {
	public SuggestDefine() {}
	private SuggestDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (!Config.WEB.SUGGEST_ENABLE) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		String query		= request.read_post("query");
		if (StringUtil.isNullOrEmpty(query)) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		int limit			= Config.WEB.SEARCH_SUGGEST_TOTAL_COUNT;
		String limited		= request.read_post("limit");
		if (!StringUtil.isNullOrEmpty(limited)) {
			limit			= Integer.parseInt(limited);
		}
		PowerbookType type	= PowerbookType.ALL;
		String typed		= request.read_post("type");
		if (!StringUtil.isNullOrEmpty(typed)) {
			type			= PowerbookType.fromString(typed);
		}
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(L1InfoDAO.getSuggestQuery(query.trim(), limit, type.toInt())));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new SuggestDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


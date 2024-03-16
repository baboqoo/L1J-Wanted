package l1j.server.web.dispatcher.response.define;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.guide.GuideDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class GuideBossDefine extends HttpJsonModel {
	private static ConcurrentHashMap<Integer, String> BOSS_LIST_JSON = new ConcurrentHashMap<Integer, String>();
	
	public GuideBossDefine() {}
	private GuideBossDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		int loc = Integer.parseInt(request.read_post("loc"));
		String list_json = BOSS_LIST_JSON.get(loc);
		if (list_json == null) {
			list_json = new Gson().toJson(GuideDAO.getInstance().getBossList(loc));
			BOSS_LIST_JSON.put(loc, list_json);
		}
		return create_response_json(HttpResponseStatus.OK, list_json);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GuideBossDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


package l1j.server.web.dispatcher.response.define;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.pitch.PitchDAO;
import l1j.server.web.dispatcher.response.pitch.PitchVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class PitchDefine extends HttpJsonModel {
	public PitchDefine() {}
	private PitchDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		ArrayList<KeyValuePair<String, Object>> params = new ArrayList<KeyValuePair<String, Object>>();
		List<PitchVO> list	= null;
		Map<String, String> post = request.get_post_datas();
		int size			= Integer.parseInt(post.get("size"));
		int last			= 4;
		String lastSize		= post.get("last");
		if (!StringUtil.isNullOrEmpty(lastSize)) {
			last			= Integer.parseInt(lastSize);
		}
		String searchType	= post.get("search_type");
		String searchText	= post.get("search_text");
		if (!StringUtil.isNullOrEmpty(searchType) && !StringUtil.isNullOrEmpty(searchText)) {
			list			= PitchDAO.getSearchList(last, searchType, searchText);
		} else {
			list			= PitchDAO.getList(size + 1, last);
		}
		
		params.add(new KeyValuePair<String, Object>("PITCH",			list));
		params.add(new KeyValuePair<String, Object>("TOP_PITCH",		PitchDAO._contentNotice));
		
		String jsonString = new Gson().toJson(params);
		params.clear();
		return create_response_json(HttpResponseStatus.OK, jsonString);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PitchDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


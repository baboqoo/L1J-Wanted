package l1j.server.web.dispatcher.response.define;

import java.util.List;
import java.util.Map;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.dispatcher.response.content.ContentVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class ContentMoreDefine extends HttpJsonModel {
	public ContentMoreDefine() {}
	private ContentMoreDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		List<ContentVO> list	= null;
		Map<String, String> post = request.get_post_datas();
		int size				= Integer.parseInt(post.get("size"));
		int last				= 4;
		String lastSize			= post.get("last");
		if (!StringUtil.isNullOrEmpty(lastSize)) {
			last				= Integer.parseInt(lastSize);
		}
		String searchType		= post.get("search_type");
		String searchText		= post.get("search_text");
		if (!StringUtil.isNullOrEmpty(searchType) && !StringUtil.isNullOrEmpty(searchText)) {
			list				= ContentDAO.getSearchList(last, searchType, searchText);
		} else {
			list				= ContentDAO.getList(size + 1, last);
		}
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(list));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ContentMoreDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.content.ContentCommentVO;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class ContentCommentDeleteDefine extends HttpJsonModel {
	public ContentCommentDeleteDefine() {}
	private ContentCommentDeleteDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		Map<String, String> post = request.get_post_datas();
		int num				= Integer.parseInt(post.get("id"));
		ContentDAO dao		= ContentDAO.getInstance();
		ContentCommentVO vo	= ContentDAO.getBoardAnser(num);
		dao.deleteAnswer(vo);
		return create_response_json(HttpResponseStatus.OK, TRUE_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ContentCommentDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


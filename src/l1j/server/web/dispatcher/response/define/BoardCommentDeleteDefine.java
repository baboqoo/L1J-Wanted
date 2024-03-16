package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.board.BoardCommentVO;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class BoardCommentDeleteDefine extends HttpJsonModel {
	public BoardCommentDeleteDefine() {}
	private BoardCommentDeleteDefine(HttpRequestModel request, DispatcherModel model) {
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
		int num				= Integer.parseInt(request.read_post("id"));
		BoardDAO dao		= BoardDAO.getInstance();
		BoardCommentVO vo	= BoardDAO.getBoardAnser(num);
		dao.deleteAnswer(vo);
		return create_response_json(HttpResponseStatus.OK, TRUE_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new BoardCommentDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


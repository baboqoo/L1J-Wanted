package l1j.server.web.dispatcher.response.define;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.board.BoardCommentVO;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class BoardCommentInsertDefine extends HttpJsonModel {
	public BoardCommentInsertDefine() {}
	private BoardCommentInsertDefine(HttpRequestModel request, DispatcherModel model) {
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
		BoardDAO dao		= BoardDAO.getInstance();
		int boardId			= Integer.parseInt(post.get("boardId"));
		String content		= post.get("content");
		BoardCommentVO vo	= new BoardCommentVO(BoardDAO.getNextAnswerNum(), boardId, player.getName(), player.getType(), player.getGender(), new Timestamp(System.currentTimeMillis()), content, new ArrayList<String>());
		dao.insertAnswer(vo);
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(vo));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new BoardCommentInsertDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


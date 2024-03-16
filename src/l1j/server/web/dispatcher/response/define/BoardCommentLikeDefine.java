package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.board.BoardCommentVO;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.board.BoardVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class BoardCommentLikeDefine extends HttpJsonModel {
	public BoardCommentLikeDefine() {}
	private BoardCommentLikeDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String result		= CODE_0_JSON;
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, result);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, result);
		}
		Map<String, String> post = request.get_post_datas();
		BoardDAO dao	= BoardDAO.getInstance();
		int commentId	= Integer.parseInt(post.get("id"));
		int boardRownum	= Integer.parseInt(post.get("boardnum"));
		BoardVO board	= BoardDAO.getBoard(boardRownum);
		if (board != null) {
			ArrayList<BoardCommentVO> commentList = board.getAnswerList();
			if (commentList != null) {
				BoardCommentVO comment = null;
				for (BoardCommentVO temp : commentList) {
					if (temp.getId() == commentId) {
						comment = temp;
						break;
					}
				}
				if (comment != null) {
					ArrayList<String> likeList = comment.getLikenames();
					if (likeList != null) {
						if (likeList.contains(player.getName())) {
							likeList.remove(player.getName());
							dao.commentLikeUpdate(comment);
							result = CODE_MINUS_JSON;
						} else {
							likeList.add(player.getName());
							dao.commentLikeUpdate(comment);
							result = CODE_1_JSON;
						}
					}
				}
			}
		}
		return create_response_json(HttpResponseStatus.OK, result);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new BoardCommentLikeDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


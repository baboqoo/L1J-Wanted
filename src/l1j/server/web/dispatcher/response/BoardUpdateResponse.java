package l1j.server.web.dispatcher.response;

import java.util.Map;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.board.BoardVO;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.editor.FileExcutor.TempStatus;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class BoardUpdateResponse extends HttpResponseModel {
	public BoardUpdateResponse() {}
	private BoardUpdateResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		int rownum		= Integer.parseInt(post.get("rownum"));
		String title	= post.get("title");
		String content	= post.get("content");
		String mainImg	= post.get("mainImg");
		String tempList	= post.get("tempList");
		if (!StringUtil.isNullOrEmpty(tempList)) {
			FileExcutor.excute(tempList, content, TempStatus.UPDATE);
		}
		BoardDAO dao	= BoardDAO.getInstance();
		BoardVO vo		= BoardDAO.getBoard(rownum);
		vo.setTitle(title);
		vo.setContent(content);
		vo.setMainImg(StringUtil.isNullOrEmpty(mainImg) ? null : mainImg);
		dao.update(vo);
		return sendRedirect("/board");
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new BoardUpdateResponse(request, model);
	}

}


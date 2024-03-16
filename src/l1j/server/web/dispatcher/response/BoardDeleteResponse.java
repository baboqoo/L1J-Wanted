package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.board.BoardVO;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class BoardDeleteResponse extends HttpResponseModel {
	public BoardDeleteResponse() {}
	private BoardDeleteResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		int num				= Integer.parseInt(request.read_post("num"));
		String deleteImg	= request.read_parameters_at_once("deleteImgList");
		if (!StringUtil.isNullOrEmpty(deleteImg)) {
			FileExcutor.delete(deleteImg);
		}
		BoardDAO dao		= BoardDAO.getInstance();
		BoardVO vo			= BoardDAO.getBoard(num);
		dao.delete(vo);
		return sendRedirect("/board");
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new BoardDeleteResponse(request, model);
	}

}


package l1j.server.web.dispatcher.response;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.dispatcher.response.content.ContentVO;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class ContentDeleteResponse extends HttpResponseModel {
	public ContentDeleteResponse() {}
	private ContentDeleteResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		int num				= Integer.parseInt(request.read_post("num"));
		String deleteImg	= request.read_parameters_at_once("deleteImgList");
		if (!StringUtil.isNullOrEmpty(deleteImg)) {
			FileExcutor.delete(deleteImg);
		}
		ContentDAO dao		= ContentDAO.getInstance();
		ContentVO vo		= ContentDAO.getBoard(num);
		dao.delete(vo);
		return sendRedirect("/contents");
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ContentDeleteResponse(request, model);
	}

}


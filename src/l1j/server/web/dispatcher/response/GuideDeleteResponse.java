package l1j.server.web.dispatcher.response;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.guide.GuideDAO;
import l1j.server.web.dispatcher.response.guide.GuideVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class GuideDeleteResponse extends HttpResponseModel {
	public GuideDeleteResponse() {}
	private GuideDeleteResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		int id				= Integer.parseInt(request.read_post("num"));
		String deleteImg	= request.read_parameters_at_once("deleteImgList");
		if (!StringUtil.isNullOrEmpty(deleteImg)) {
			FileExcutor.delete(deleteImg);
		}
		GuideDAO dao		= GuideDAO.getInstance();
		GuideVO vo			= dao.getGuide(id);
		if (vo != null) {
			dao.delete(vo);
		}
		return sendRedirect("/guide");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GuideDeleteResponse(request, model);
	}
}


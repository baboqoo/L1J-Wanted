package l1j.server.web.dispatcher.response;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.pitch.PitchDAO;
import l1j.server.web.dispatcher.response.pitch.PitchVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class PitchDeleteResponse extends HttpResponseModel {
	public PitchDeleteResponse() {}
	private PitchDeleteResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		int num				= Integer.parseInt(request.read_post("num"));
		String deleteImg	= request.read_parameters_at_once("deleteImgList");
		if (!StringUtil.isNullOrEmpty(deleteImg)) {
			FileExcutor.delete(deleteImg);
		}
		PitchDAO dao		= PitchDAO.getInstance();
		PitchVO vo			= PitchDAO.getBoard(num);
		dao.delete(vo);
		return sendRedirect("/pitch");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PitchDeleteResponse(request, model);
	}
}


package l1j.server.web.dispatcher.response;

import java.util.Map;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.notice.NoticeDAO;
import l1j.server.web.dispatcher.response.notice.NoticeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class NoticeDeleteResponse extends HttpResponseModel {
	public NoticeDeleteResponse() {}
	private NoticeDeleteResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		int num				= Integer.parseInt(post.get("num"));
		int type			= Integer.parseInt(post.get("type"));
		String deleteImg	= request.read_parameters_at_once("deleteImgList");
		if (!StringUtil.isNullOrEmpty(deleteImg)) {
			FileExcutor.delete(deleteImg);
		}
		NoticeDAO dao		= NoticeDAO.getInstance();
		NoticeVO vo			= NoticeDAO.getNotice(num, type);
		dao.delete(vo);
		return sendRedirect("/notice");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new NoticeDeleteResponse(request, model);
	}
}


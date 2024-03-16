package l1j.server.web.dispatcher.response;

import java.util.Map;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.editor.FileExcutor.TempStatus;
import l1j.server.web.dispatcher.response.guide.GuideDAO;
import l1j.server.web.dispatcher.response.guide.GuideVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class GuideUpdateResponse extends HttpResponseModel {
	public GuideUpdateResponse() {}
	private GuideUpdateResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		int id			= Integer.parseInt(post.get("id"));
		String title	= post.get("title");
		String content	= post.get("content");
		String tempList	= post.get("tempList");
		if (!StringUtil.isNullOrEmpty(tempList)) {
			FileExcutor.excute(tempList, content, TempStatus.UPDATE);
		}
		GuideDAO dao	= GuideDAO.getInstance();
		GuideVO vo		= dao.getGuide(id);
		if (vo != null) {
			vo.setTitle(title);
			vo.setContent(content);
			dao.update(vo);
		}
		return sendRedirect("/guide");
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GuideUpdateResponse(request, model);
	}
}


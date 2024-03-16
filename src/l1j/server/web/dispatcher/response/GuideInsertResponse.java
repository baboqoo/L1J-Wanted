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

public class GuideInsertResponse extends HttpResponseModel {
	public GuideInsertResponse() {}
	private GuideInsertResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String title	= post.get("title");
		String content	= post.get("content");
		String tempList	= post.get("tempList");
		if (!StringUtil.isNullOrEmpty(tempList)) {
			FileExcutor.excute(tempList, content, TempStatus.INSERT);
		}
		GuideDAO dao	= GuideDAO.getInstance();
		GuideVO vo		= new GuideVO(dao.nextId(), title, content);
		dao.insert(vo);
		return sendRedirect("/guide");
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GuideInsertResponse(request, model);
	}

}


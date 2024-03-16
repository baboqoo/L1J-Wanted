package l1j.server.web.dispatcher.response;

import java.util.Map;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.editor.FileExcutor.TempStatus;
import l1j.server.web.dispatcher.response.notice.NoticeDAO;
import l1j.server.web.dispatcher.response.notice.NoticeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class NoticeUpdateResponse extends HttpResponseModel {
	public NoticeUpdateResponse() {}
	private NoticeUpdateResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		int rownum		= Integer.parseInt(post.get("rownum"));
		int type		= Integer.parseInt(post.get("noti_type"));
		String title	= post.get("title");
		String content	= post.get("content");
		String mainImg	= post.get("mainImg");
		String tempList	= post.get("tempList");
		if (!StringUtil.isNullOrEmpty(tempList)) {
			FileExcutor.excute(tempList, content, TempStatus.UPDATE);
		}
		NoticeDAO dao	= NoticeDAO.getInstance();
		NoticeVO vo		= NoticeDAO.getNotice(rownum, type);
		vo.setTitle(title);
		vo.setContent(content);
		vo.setType(type);
		vo.setMainImg(StringUtil.isNullOrEmpty(mainImg) ? null : mainImg);
		dao.update(vo);
		return sendRedirect("/notice");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new NoticeUpdateResponse(request, model);
	}
}


package l1j.server.web.dispatcher.response;

import java.sql.Timestamp;
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

public class NoticeInsertResponse extends HttpResponseModel {
	public NoticeInsertResponse() {}
	private NoticeInsertResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		int type		= Integer.parseInt(post.get("noti_type"));
		String name		= post.get("name");
		String title	= post.get("title");
		String content	= post.get("content");
		String mainImg	= post.get("mainImg");
		String tempList	= post.get("tempList");
		if (!StringUtil.isNullOrEmpty(tempList)) {
			FileExcutor.excute(tempList, content, TempStatus.INSERT);
		}
		NoticeDAO dao	= NoticeDAO.getInstance();
		NoticeVO vo		= new NoticeVO(dao.getNextNum(), name, title, content, new Timestamp(System.currentTimeMillis()), 0, type, 1, false, StringUtil.isNullOrEmpty(mainImg) ? null : mainImg);
		dao.insert(vo);
		return sendRedirect("/notice");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new NoticeInsertResponse(request, model);
	}
}


package l1j.server.web.dispatcher.response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import l1j.server.common.data.Gender;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.editor.FileExcutor.TempStatus;
import l1j.server.web.dispatcher.response.pitch.PitchDAO;
import l1j.server.web.dispatcher.response.pitch.PitchVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class PitchInsertResponse extends HttpResponseModel {
	public PitchInsertResponse() {}
	private PitchInsertResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String name			= post.get("name");
		String title		= post.get("title");
		int chatype			= Integer.parseInt(post.get("chatype"));
		Gender chaGender	= Gender.fromInt(Integer.parseInt(post.get("chasex")));
		String content		= post.get("content");
		String mainImg		= post.get("mainImg");
		String tempList		= post.get("tempList");
		if (!StringUtil.isNullOrEmpty(tempList)) {
			FileExcutor.excute(tempList, content, TempStatus.INSERT);
		}
		PitchDAO dao		= PitchDAO.getInstance();
		PitchVO vo			= new PitchVO();
		vo.setId(dao.getNextNum());
		vo.setName(name);
		vo.setTitle(title);
		vo.setContent(content);
		vo.setChatype(chatype);
		vo.setChaGender(chaGender);
		vo.setMainImg(StringUtil.isNullOrEmpty(mainImg) ? null : mainImg);
		vo.setDate(new Timestamp(System.currentTimeMillis()));
		vo.setLikenames(new ArrayList<String>());
		vo.setRownum(1);
		dao.insert(vo);
		return sendRedirect("/pitch");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PitchInsertResponse(request, model);
	}
}


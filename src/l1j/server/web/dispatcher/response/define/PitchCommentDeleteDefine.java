package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.pitch.PitchCommentVO;
import l1j.server.web.dispatcher.response.pitch.PitchDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class PitchCommentDeleteDefine extends HttpJsonModel {
	public PitchCommentDeleteDefine() {}
	private PitchCommentDeleteDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		int num				= Integer.parseInt(request.read_post("id"));
		PitchDAO dao		= PitchDAO.getInstance();
		PitchCommentVO vo	= PitchDAO.getBoardAnser(num);
		dao.deleteAnswer(vo);
		return create_response_json(HttpResponseStatus.OK, TRUE_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PitchCommentDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


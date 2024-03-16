package l1j.server.web.dispatcher.response.define;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.board.CharacterBoardVO;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.dispatcher.response.pitch.PitchDAO;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class MypageBoardDefine extends HttpJsonModel {
	public MypageBoardDefine() {}
	private MypageBoardDefine(HttpRequestModel request, DispatcherModel model) {
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
		String boardType = request.read_post("boardType");
		if (StringUtil.isNullOrEmpty(boardType)) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		List<CharacterBoardVO> list = new ArrayList<CharacterBoardVO>();
		BoardDAO.getList(player.getName(), list, boardType);
		ContentDAO.getList(player.getName(), list, boardType);
		PitchDAO.getList(player.getName(), list, boardType);
		TradeDAO.getList(player.getName(), list, boardType);
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(list));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new MypageBoardDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class BoardWriteResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	public BoardWriteResponse() {}
	private BoardWriteResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (player == null) {
			return sendRedirect(request.isIngame() ? 
					String.format("/login_ingame?urlType=%s", request.get_request_uri()) 
					: String.format("/login?urlType=%s", request.get_request_uri()));
		}
		// TODO 인게임 캐릭터 검증
		if (request.isIngame()) {
			L1PcInstance pc = getIngamePlayer();
			if (pc == null) {
				//return new AlertRedirectResponse(request, "인게임 내 캐릭터를 찾을 수 없습니다.", "/index").get_response();
				return new AlertRedirectResponse(request, "Cannot find in-game character.", "/index").get_response();
			}
		}
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(SUGGEST_ENABLE_PAIR);
		params.add(get_cnb_pair());
		params.add(get_user_data_pair());
		params.add(get_now_pair());
		if (PAGE_CNB_TYPE_PAIR == null) {
			PAGE_CNB_TYPE_PAIR		= new KeyValuePair<String, String>(PAGE_CNB_TYPE_KEY,		dispatcher.getCnbType());
		}
		if (PAGE_CNB_SUB_TYPE_PAIR == null) {
			PAGE_CNB_SUB_TYPE_PAIR	= new KeyValuePair<String, String>(PAGE_CNB_SUB_TYPE_KEY,	dispatcher.getCnbSubType());
		}
		params.add(PAGE_CNB_TYPE_PAIR);
		params.add(PAGE_CNB_SUB_TYPE_PAIR);
		
		params.add(new KeyValuePair<String, String>("{CHARACTER_NAME}",		player.getName()));
		params.add(new KeyValuePair<String, String>("{CHARACTER_TYPE}",		String.valueOf(player.getType())));
		params.add(new KeyValuePair<String, String>("{CHARACTER_SEX}",		String.valueOf(player.getGender().toInt())));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new BoardWriteResponse(request, model);
	}

}


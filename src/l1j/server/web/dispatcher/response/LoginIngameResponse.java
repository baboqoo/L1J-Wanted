package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.Config;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

/**
 * 인게임 로그인 페이지 응답
 * 인게임 브라우저에서 계정 또는 캐릭터를 특정짓지 못할 경우 사용
 * @author LinOffice
 */
public class LoginIngameResponse extends HttpResponseModel {
	private String _callback;
	
	public LoginIngameResponse() {}
	private LoginIngameResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	public LoginIngameResponse(HttpRequestModel request, DispatcherModel model, String callback) {
		super(request, model);
		_callback = callback;
	}
	
	private static final KeyValuePair<String, String> CALLBACK_URL_PAIR		= new KeyValuePair<String, String>("{CALLBACK_URL}", null);
	private static final KeyValuePair<String, String> LOGIN_AUTH_COUNT_PAIR	= new KeyValuePair<String, String>("{LOGIN_AUTH_COUNT}", Integer.toString(Config.WEB.INGAME_LOGIN_AUTH_MAX_COUNT));

	@Override
	public HttpResponse get_response() throws Exception {
		if (!request.isIngame()) {
			return sendRedirect("/index");
		}
		
		String urlType = StringUtil.EmptyString;
		Map<String, String> post = request.get_post_datas();
		if (post.containsKey("urlType")) {
			urlType = post.get("urlType");
		}
		if (StringUtil.isNullOrEmpty(urlType)) {
			String get = request.read_parameters_at_once("urlType");
			if (!StringUtil.isNullOrEmpty(get)) {
				urlType = get;
			}
		}
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(get_now_pair());
		
		CALLBACK_URL_PAIR.value = _callback != null ? _callback : urlType;
		params.add(CALLBACK_URL_PAIR);
		params.add(LOGIN_AUTH_COUNT_PAIR);
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new LoginIngameResponse(request, model);
	}
}


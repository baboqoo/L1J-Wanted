package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class GMResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	public GMResponse() {}
	private GMResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return sendRedirect(request.isIngame() ? 
					String.format("/login_ingame?urlType=%s", request.get_request_uri()) 
					: String.format("/login?urlType=%s", request.get_request_uri()));
		}
		/*if (!account.isGm()) {
			return new AlertRedirectResponse(request, "관리자 전용 페이지입니다.", "/index").get_response();
		}
		if (player == null) {
			return new AlertRedirectResponse(request, "대표 캐릭터를 설정하십시오.", "/index").get_response();
		}*/
		if (!account.isGm()) {
			return new AlertRedirectResponse(request, "This is an administrator-only page.", "/index").get_response();
		}
		if (player == null) {
			return new AlertRedirectResponse(request, "Please set the main character.", "/index").get_response();
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
		
		String tab = StringUtil.EmptyString;
		Map<String, String> post = request.get_post_datas();
		if (post.containsKey("type")) {
			tab = post.get("type");
		}
		params.add(new KeyValuePair<String, String>("{TAB}",				tab));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GMResponse(request, model);
	}
}


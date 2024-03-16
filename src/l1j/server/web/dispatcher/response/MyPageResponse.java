package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.Config;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.CharacterVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class MyPageResponse extends HttpResponseModel {
	private static final String CHAR_LIST_HEAD		= "<div class=\"items\"><div class=\"thumb\"><img src=\"";
	private static final String CHAR_LIST_CONTENT_1	= "\" alt=\"\"></div><div class=\"info\"><input type=\"hidden\" name=\"select_charter_choice\" id=\"select_charter_choice\" value=\"";
	private static final String CHAR_LIST_CONTENT_2	= "\"/><span class=\"name\">";
	private static final String CHAR_LIST_CONTENT_3	= "</span><span class=\"server\">";
	private static final String CHAR_LIST_CONTENT_4	= "</span><span class=\"level\">";
	private static final String CHAR_LIST_TAIL		= "Lv</span></div></div>";
	
	public MyPageResponse() {}
	private MyPageResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return sendRedirect(request.isIngame() ? 
					String.format("/login_ingame?urlType=%s", request.get_request_uri()) 
					: String.format("/login?urlType=%s", request.get_request_uri()));
		}
		if (player == null) {
			//return new AlertRedirectResponse(request, "대표 캐릭터를 설정하십시오.", "/index").get_response();
			return new AlertRedirectResponse(request, "Please set the main character.", "/index").get_response();

		}
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(SUGGEST_ENABLE_PAIR);
		params.add(get_cnb_pair());
		params.add(get_user_data_pair());
		params.add(get_now_pair());
		
		params.add(new KeyValuePair<String, String>("{CHARACTER_PROFILE}",	player.getProfileUrl()));
		params.add(new KeyValuePair<String, String>("{CHARACTER_NAME}",		player.getName()));
		params.add(new KeyValuePair<String, String>("{CHARACTER_LEVEL}",	String.valueOf(player.getLevel())));
		
		StringBuilder sb = new StringBuilder();
		if (account.getCharList() != null && !account.getCharList().isEmpty()) {
			for (CharacterVO each : account.getCharList()) {
				sb.append(CHAR_LIST_HEAD);
				sb.append(each.getProfileUrl());
				sb.append(CHAR_LIST_CONTENT_1);
				sb.append(each.getObjId());
				sb.append(CHAR_LIST_CONTENT_2);
				sb.append(each.getName());
				sb.append(CHAR_LIST_CONTENT_3);
				sb.append(Config.WEB.WEB_SERVER_NAME);
				sb.append(CHAR_LIST_CONTENT_4);
				sb.append(each.getLevel());
				sb.append(CHAR_LIST_TAIL);
			}
		}
		params.add(new KeyValuePair<String, String>("{CHARACTER_LIST}",		sb.toString()));
		sb.setLength(0);
		
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
		return new MyPageResponse(request, model);
	}
}


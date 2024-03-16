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

public class CustomerResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	
	private static final String HELLOW_EMPTY_MENT	= "<span>Welcome to <strong class=\"ng-binding\">" + Config.WEB.WEB_SERVER_NAME + "</strong></span>";
	private static final String HELLOW_MENT_HEAD	= "<span><strong class=\"ng-binding\">";
	private static final String HELLOW_MENT_TAIL	= "</strong>, hello</span>";
	private static final String TELEGRAM_URL		= "<a href=\"https://t.me/" + Config.WEB.TELEGRAM_ID.substring(1) + "\" target=\"_blank\">Inquiry by Telegram</a>";
	private static final String TELEGRAM_URL_INGAME	= "<a href=\"javascript:;\">Inquiry by Telegram</a>";
	
	public CustomerResponse() {}
	private CustomerResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String cnbSubType	= StringUtil.OneString, customType = StringUtil.ZeroString;
		String type 		= post.get("type");
		String num			= post.get("num");
		if (!StringUtil.isNullOrEmpty(type)) {
			cnbSubType = type;
		}
		if (!StringUtil.isNullOrEmpty(num)) {
			customType = num;
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
		params.add(PAGE_CNB_TYPE_PAIR);
		params.add(new KeyValuePair<String, String>(PAGE_CNB_SUB_TYPE_KEY,	cnbSubType));
		
		params.add(new KeyValuePair<String, String>("{HELLOW_MENT}",		account == null ? HELLOW_EMPTY_MENT : HELLOW_MENT_HEAD + account.getName() + HELLOW_MENT_TAIL));
		params.add(new KeyValuePair<String, String>("{TELEGRAM_URL}",		!request.isIngame() && Config.WEB.TELEGRAM_ACTIVE ? TELEGRAM_URL : TELEGRAM_URL_INGAME));
		params.add(new KeyValuePair<String, String>("{CUSTOM_TYPE}",		customType));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CustomerResponse(request, model);
	}

}


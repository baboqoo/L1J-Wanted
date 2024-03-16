package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class ProtectResponse extends HttpResponseModel {
	private static final String TELEGRAM_URL		= "https://t.me/" + Config.WEB.TELEGRAM_ID.substring(1);
	private static final String TELEGRAM_INGAME		= "javascript:;";
	
	private String title;
	private String content;
	
	public ProtectResponse() {}
	public ProtectResponse(HttpRequestModel request, DispatcherModel model) {
		//this(request, model, "해당 계정은 이용하실 수 없습니다.", "이용 제한 계정");
		this(request, model, "This account cannot be used.", "Restricted Account");
	}
	public ProtectResponse(HttpRequestModel request, DispatcherModel model, String title, String content) {
		super(request, model);
		this.title		= title;
		this.content	= content;
	}

	@Override
	public HttpResponse get_response() throws Exception {
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(SUGGEST_ENABLE_PAIR);
		params.add(get_now_pair());
		params.add(new KeyValuePair<String, String>("{TITLE}",			title));
		params.add(new KeyValuePair<String, String>("{CONTENT}",		content));
		params.add(new KeyValuePair<String, String>("{TELEGRAM_URL}",	!request.isIngame() && Config.WEB.TELEGRAM_ACTIVE ? TELEGRAM_URL : TELEGRAM_INGAME));
		params.add(new KeyValuePair<String, String>("{TELEGRAM_ID}",	!request.isIngame() && Config.WEB.TELEGRAM_ACTIVE ? Config.WEB.TELEGRAM_ID : StringUtil.EmptyString));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ProtectResponse(request, model);
	}
}


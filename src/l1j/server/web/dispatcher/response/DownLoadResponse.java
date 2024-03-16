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

public class DownLoadResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	private static KeyValuePair<String, String> CONNECTION_DOWNLOAD_URL_PAIR;
	
	public DownLoadResponse() {}
	private DownLoadResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
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
				
		if (CONNECTION_DOWNLOAD_URL_PAIR == null) {
			boolean is_local = Config.SERVER.LOGIN_SERVER_ADDRESS.equalsIgnoreCase(Config.SERVER.LOCALHOST);
			if (is_local) 
				CONNECTION_DOWNLOAD_URL_PAIR = new KeyValuePair<String, String>("{CONNECT_URL}",	Config.WEB.CONNECTION_DOWNLOAD_URL_LOCAL);
			else
				CONNECTION_DOWNLOAD_URL_PAIR = new KeyValuePair<String, String>("{CONNECT_URL}",	Config.WEB.CONNECTION_DOWNLOAD_URL_REMOTE);
		}
		params.add(CONNECTION_DOWNLOAD_URL_PAIR);
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new DownLoadResponse(request, model);
	}

}


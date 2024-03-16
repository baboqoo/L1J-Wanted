package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.SearchConstruct;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class ShopViewResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	public ShopViewResponse() {}
	private ShopViewResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String searchItemId = request.read_parameters_at_once("itemId");
		if (StringUtil.isNullOrEmpty(searchItemId)) {
			return sendRedirect("/index");
		}
		String tradeType	= request.read_parameters_at_once("tradeType");
		String enchant		= request.read_parameters_at_once("enchant");
		String bless		= request.read_parameters_at_once("bless");
		String attr			= request.read_parameters_at_once("attr");
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
		
		params.add(new KeyValuePair<String, String>("{SEARCH_ITEM_ID}",		searchItemId));
		params.add(new KeyValuePair<String, String>("{TRADE_TYPE}",			StringUtil.isNullOrEmpty(tradeType) ? SearchConstruct.SEARCH_SELL : tradeType));
		params.add(new KeyValuePair<String, String>("{ENCHANT}",			StringUtil.isNullOrEmpty(enchant) ? "-1" : enchant));
		params.add(new KeyValuePair<String, String>("{BLESS}",				StringUtil.isNullOrEmpty(bless) ? SearchConstruct.SEARCH_ALL : bless));
		params.add(new KeyValuePair<String, String>("{ATTR}",				StringUtil.isNullOrEmpty(attr) ? SearchConstruct.SEARCH_ALL : attr));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ShopViewResponse(request, model);
	}
}


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

public class CouponResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	private static KeyValuePair<String, String> GM_INFO_PAIR;
	private static final KeyValuePair<String, String> DEFAILT_INFO_PAIR = new KeyValuePair<String, String>("{GM_HTML}",	StringUtil.EmptyString);
	
	public CouponResponse() {}
	private CouponResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String query	= StringUtil.EmptyString;
		Map<String, String> post = request.get_post_datas();
		if (post.containsKey("query")) {
			query		= post.get("query");
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
		
		params.add(new KeyValuePair<String, String>("{QUERY}",				query));
		StringBuilder sb = new StringBuilder();
		if (account != null) {
			String ncoin = StringUtil.comma(account.getNcoin());
			String npoint = StringUtil.comma(account.getNpoint());
			sb.append(GoodsResponse.ACCOUNT_INFO_HEAD);
			sb.append(ncoin);
			sb.append(GoodsResponse.ACCOUNT_INFO_BODY_1);
			sb.append(npoint);
			sb.append(GoodsResponse.ACCOUNT_INFO_BODY_2);
			sb.append(ncoin);
			sb.append(GoodsResponse.ACCOUNT_INFO_BODY_3);
			sb.append(npoint);
			sb.append(GoodsResponse.ACCOUNT_INFO_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{NSHOP_ACCOUNT_INFO}",	sb.toString()));
		sb.setLength(0);
		
		boolean isGm = account != null && account.isGm();
		params.add(new KeyValuePair<String, String>("{IS_GM}",				isGm ? "true" : "false"));
		
		if (GM_INFO_PAIR == null) {
			GM_INFO_PAIR = new KeyValuePair<String, String>("{GM_HTML}",	load_file_string("./appcenter/goods/coupon_gm.html"));
		}
		params.add(isGm ? GM_INFO_PAIR : DEFAILT_INFO_PAIR);
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CouponResponse(request, model);
	}
}


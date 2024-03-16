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

public class GoodsResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	public static final String ACCOUNT_INFO_HEAD = "<div class=\"user_wrap\"><ul class=\"lst_coin\"><li><a href=\"javascript:;\" class=\"num coin\" id=\"headerNcoin\">";
	public static final String ACCOUNT_INFO_BODY_1 = "</a></li><li><a href=\"javascript:;\" class=\"num point\" id=\"headerNpoint\">";
	public static final String ACCOUNT_INFO_BODY_2 = "</a></li><li><a href=\"/goods/coupon\" class=\"num coupon\">Coupon</a></li></ul></div><button type=\"button\" class=\"myshop btn_menu\"><span>My N Shop</span></button><div class=\"menu_area\" style=\"display: none;\"><div class=\"lst_area\"><em class=\"tit\">My N Shop</em></div><div class=\"lst_area\"><em class=\"tit\">N Coin<span class=\"num\">";
	public static final String ACCOUNT_INFO_BODY_3 = "</span></em><a href=\"javascript:openNCoinGiftPopup();\" class=\"sub\">Gift</a></div><div class=\"lst_area\"><em class=\"tit\">N Points<span class=\"num\">";
	public static final String ACCOUNT_INFO_TAIL = "</span></em></div><div class=\"lst_area\"><em class=\"tit\">Coupon</em><a href=\"/goods/coupon\" class=\"sub\">Register/Use</a></div>";
	
	public GoodsResponse() {}
	private GoodsResponse(HttpRequestModel request, DispatcherModel model) {
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
			sb.append(ACCOUNT_INFO_HEAD);
			sb.append(ncoin);
			sb.append(ACCOUNT_INFO_BODY_1);
			sb.append(npoint);
			sb.append(ACCOUNT_INFO_BODY_2);
			sb.append(ncoin);
			sb.append(ACCOUNT_INFO_BODY_3);
			sb.append(npoint);
			sb.append(ACCOUNT_INFO_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{NSHOP_ACCOUNT_INFO}",	sb.toString()));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GoodsResponse(request, model);
	}

}


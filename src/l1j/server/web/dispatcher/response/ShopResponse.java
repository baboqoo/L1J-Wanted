package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.market.MPSECore;
import l1j.server.web.dispatcher.response.market.MarketSearchRankVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class ShopResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	private static String SHOP_INDEX_HTML;
	private static String SHOP_SEARCH_HTML;
	private static KeyValuePair<String, String> SHOP_SEARCH_RANK_PAIR;
	
	public ShopResponse() {}
	private ShopResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String document;
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
		
		String keyword		= request.read_parameters_at_once("keyword");// 검색한 아이템 명
		if (!StringUtil.isNullOrEmpty(keyword)) {
			if (SHOP_SEARCH_HTML == null) {
				SHOP_SEARCH_HTML = load_file_string("./appcenter/shop/search.html");
			}
			params.add(new KeyValuePair<String, String>("{KEYWORD}",		keyword));
			// parameter define
			document = StringUtil.replace(SHOP_SEARCH_HTML, params);
		} else {
			if (SHOP_INDEX_HTML == null) {
				SHOP_INDEX_HTML = load_file_string("./appcenter/shop/index.html");
			}
			
			if (SHOP_SEARCH_RANK_PAIR == null) {
				StringBuilder sb = new StringBuilder();
				for (LinkedHashMap<Integer, LinkedList<MarketSearchRankVO>> val : MPSECore.getSearchRank().values()) {
					sb.append("<div class=\"slick-slide\">");
					for (Map.Entry<Integer, LinkedList<MarketSearchRankVO>> entry : val.entrySet()) {
						sb.append("<div class=\"searchrank-list searchrank-list--");
						int shop_type = entry.getKey();
						sb.append(shop_type == 1 ? "sell" : "Buy");
						sb.append("\"><h2>");
						sb.append(shop_type == 1 ? "Sell" : "Buy");
						sb.append("</h2><ol>");
						for (MarketSearchRankVO rank : entry.getValue()) {
							sb.append("<li><a href=\"/my/item-search?keyword=");
							int enchant = rank.getEnchant();
							if (enchant != 0) {
								sb.append(enchant > 0 ? "+" : "-").append(enchant).append(" ");
							}
							sb.append(rank.getItem().getName());
							sb.append("\"><img src=\"/img/item/");
							sb.append(rank.getItem().getInvgfx());
							sb.append(".png\" onerror=\"this.src='/img/shop/noimg.gif'\" class=\"thumb\" alt=\"\"><strong class=\"name\">");
							if (enchant != 0) {
								sb.append(enchant > 0 ? "+" : "-").append(enchant).append(" ");
							}
							sb.append(rank.getItem().getName());
							sb.append("</strong></a><span class=\"ui-rank ");
							int ranking = rank.getSearch_rank();
							sb.append(ranking > 0 ? "up" : ranking < 0 ? "down" : "same");
							sb.append("\">");
							sb.append(ranking);
							sb.append("</span></li>");
						}
						sb.append("</ol></div>");
					}
					sb.append("</div>");
				}
				SHOP_SEARCH_RANK_PAIR = new KeyValuePair<String, String>("{RANK_LIST}",	sb.toString());
			}
			params.add(SHOP_SEARCH_RANK_PAIR);
			
			// parameter define
			document = StringUtil.replace(SHOP_INDEX_HTML, params);
		}
		params.clear();
		params = null;
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ShopResponse(request, model);
	}
}


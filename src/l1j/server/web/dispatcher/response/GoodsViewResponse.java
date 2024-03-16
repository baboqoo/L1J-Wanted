package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.goods.GoodsDAO;
import l1j.server.web.dispatcher.response.goods.GoodsVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class GoodsViewResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	//private static final String SAVED_POINT_HEAD = "<dl class=\"row\"><dt>총 적립예정 포인트</dt><dd><span class=\"npoint\" id=\"rewardAmount\">";
	private static final String SAVED_POINT_HEAD = "<dl class=\"row\"><dt>Total Accumulated Points</dt><dd><span class=\"npoint\" id=\"rewardAmount\">";
	private static final String SAVED_POINT_TAIL = "</span></dd></dl>";
	
	public GoodsViewResponse() {}
	private GoodsViewResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String query	= StringUtil.EmptyString;
		Map<String, String> post = request.get_post_datas();
		if (post.containsKey("query")) {
			query		= post.get("query");
		}
		String num		= post.get("num");
		if (StringUtil.isNullOrEmpty(num)) {
			return sendRedirect("/goods");
		}
		int id = Integer.parseInt(num);
		GoodsVO goods = GoodsDAO.getGoodsInfo(id);
		if (goods == null) {
			return sendRedirect("/goods");
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
		params.add(new KeyValuePair<String, String>("{NSHOP_ACCOUNT_INFO}",		sb.toString()));
		sb.setLength(0);
		
		params.add(new KeyValuePair<String, String>("{ID}",						String.valueOf(goods.getId())));
		params.add(new KeyValuePair<String, String>("{ITEM_ID}",				String.valueOf(goods.getItemid())));
		params.add(new KeyValuePair<String, String>("{PRICE}",					String.valueOf(goods.getPrice())));
		params.add(new KeyValuePair<String, String>("{COUNT}",					String.valueOf(goods.getPack() > 1 ? goods.getPack() : 1)));
		params.add(new KeyValuePair<String, String>("{GOODS_FLAG}",				goods.getFlag().getTag_2()));
		params.add(new KeyValuePair<String, String>("{SAVED_POINT}",			String.valueOf(goods.getSavedPoint())));
		params.add(new KeyValuePair<String, String>("{ITEM_FULL_NAME}",			goods.getItemname() + (goods.getPack() > 0 ? "&nbsp;" + goods.getPack() + " units" : StringUtil.EmptyString)));
		params.add(new KeyValuePair<String, String>("{ITEM_GFX}",				String.valueOf(goods.getInvgfx())));
		params.add(new KeyValuePair<String, String>("{PRICE_COMMA}",			StringUtil.comma(goods.getPrice())));
		params.add(new KeyValuePair<String, String>("{DESC}",					goods.getIteminfo()));
		params.add(new KeyValuePair<String, String>("{ENCHANT}",				String.valueOf(goods.getEnchant())));
		params.add(new KeyValuePair<String, String>("{GOODS_LIMIT_COUNT}",		String.valueOf(goods.getLimitCount())));
		params.add(new KeyValuePair<String, String>("{GOODS_PRICE_TYPE}",		goods.getPriceType().name()));
		params.add(new KeyValuePair<String, String>("{GOODS_PRICE_TYPE_LAW}",	goods.getPriceType().name().toLowerCase()));
		
		if (goods.getSavedPoint() > 0) {
			sb.append(SAVED_POINT_HEAD);
			sb.append(StringUtil.comma(goods.getSavedPoint()));
			sb.append(SAVED_POINT_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{GOODS_SAVED_POINT_TAG}",	sb.toString()));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GoodsViewResponse(request, model);
	}

}


package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.board.BoardVO;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.dispatcher.response.content.ContentVO;
import l1j.server.web.dispatcher.response.goods.GoodsDAO;
import l1j.server.web.dispatcher.response.goods.GoodsVO;
import l1j.server.web.dispatcher.response.powerbook.L1Info;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class SearchResponse extends HttpResponseModel {
	private static final String DEFAULT_SEARCH_TYPE = "title,contents";
	
	private static final String NO_DATA = "<div class=\"search-article__nodata\"><div class=\"nodata\"><strong>No results were found for your search.</strong><ol><li>Please check the spelling of the search term.</li><li>If the search term consists of two or more words, please check the spacing.</li></ol></div></div>";
	
	//private static final String NSHOP_HEAD = "<section><header class=\"search-title\"><h2>N샵</h2></header><article class=\"search-article search-article--nshop\"><ul>";
	private static final String NSHOP_HEAD = "<section><header class=\"search-title\"><h2>N Shop</h2></header><article class=\"search-article search-article--nshop\"><ul>";
	private static final String NSHOP_BODY_1 = "<li><a href=\"javascript:urlform('";
	private static final String NSHOP_BODY_2 = "', 'post', '/goods/view');\"><div class=\"search-article__thumb\"><p><img src=\"/img/item/";
	private static final String NSHOP_BODY_3 = ".png\" onerror=\"this.src='/img/shop/noimg.gif';\"></p></div><div class=\"search-article__subject\">";
	private static final String NSHOP_BODY_4 = "</div><div class=\"search-article__price\"><span class=\"search-article__ncoin\"><i class=\"icon__point\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 20 20\"><path fill=\"#F7A800\" d=\"M18.965 12.51l-6.455 6.455a3.56 3.56 0 0 1-5.02 0L1.035 12.51a3.56 3.56 0 0 1 0-5.02L7.49 1.035a3.56 3.56 0 0 1 5.02 0l6.455 6.455a3.56 3.56 0 0 1 0 5.02\"></path><path fill=\"#FFF\" d=\"M11.578 9.723s-.013.963-.375 1.124c0 0-.288.222-.5.25h-.875s-.506.012-.499-.25c.023-.856 0-3.748 0-3.748s.329-.087.5-.125c.25-.055.624 0 .624 0s.572.02.875.5c0 0 .269.205.25 1.5-.02 1.293 0 .749 0 .749M10.953 5.1H9.58c-1.993.067-2.374.624-2.374.624s-.011 8.804 0 9.12c.012.315.25.25.25.25h1.749c.193.001.125-.25.125-.25v-2.249c.18.14 1.749.125 1.749.125 2.526-.288 2.498-2.873 2.498-2.873V7.849c-.169-2.858-2.623-2.748-2.623-2.748\"></path></svg></i>";
	private static final String NSHOP_BODY_5 = "</span></div></a></li>";
	private static final String NSHOP_BODY_6 = "</ul></article><footer class=\"search-more\"><a href=\"javascript:urlQueryform('";
	//private static final String NSHOP_TAIL = "', 'post', '/goods');\">N샵 더보기</a></footer></section>";
	private static final String NSHOP_TAIL = "', 'post', '/goods');\">More from N Shop</a></footer></section>";
	//private static final String NSHOP_PACK_COUNT = "개";
	private static final String NSHOP_PACK_COUNT = " units";
	
	private static final String POWERBOOK_HEAD = "<section><header class=\"search-title\"><h2>Powerbook</h2></header><article class=\"search-article search-article--powerbook\"><ul>";
	private static final String POWERBOOK_BODY_1 = "<li><a href=\"/powerbook/search?searchType=";
	private static final String POWERBOOK_BODY_2 = "&query=";
	private static final String POWERBOOK_BODY_3 = "\">";
	private static final String POWERBOOK_BODY_4 = "<div class=\"search-article__thumb\"><p><img src=\"/img/item/";
	private static final String POWERBOOK_BODY_5 = ".png\" onerror=\"this.src='/img/shop/noimg.gif';\"></p></div>";
	private static final String POWERBOOK_BODY_6 = "<div class=\"search-article__thumb\"><p><img src=\"";
	private static final String POWERBOOK_BODY_7 = "\" onerror=\"this.src='/img/shop/noimg.gif';\"></p></div>";
	private static final String POWERBOOK_BODY_8 = "<div class=\"search-article__subject\"><strong>";
	private static final String POWERBOOK_BODY_9 = "</strong></div><div class=\"search-article__desc\">";
	private static final String POWERBOOK_BODY_10 = "</div></a></li>";
	private static final String POWERBOOK_BODY_11 = "<footer class=\"search-more\"><a href=\"/powerbook/search?query=";
	//private static final String POWERBOOK_BODY_12 = "\">파워북 더보기 (";
	private static final String POWERBOOK_BODY_12 = "\">More from PowerBook ("; 
	
	private static final String BOARD_HEAD = "<section><header class=\"search-title\"><h2>Community</h2></header><article class=\"search-article search-article--community\"><ul>";
	private static final String BOARD_BODY_1 = "<li><a href=\"javascript:urlform('";
	private static final String BOARD_BODY_2 = "', 'post', '/board/view');\"><div class=\"search-article__subject\">";
	private static final String BOARD_BODY_3 = "</div><div class=\"search-article__desc\">";
	private static final String BOARD_BODY_4 = "</div><div class=\"search-article__info\"><span class=\"search-article__writer\">";
	//private static final String BOARD_BODY_5 = "</span><span class=\"search-article__category\">리니지(자유)</span><span class=\"search-article__date\">";
	private static final String BOARD_BODY_5 = "</span><span class=\"search-article__category\">Lineage (Free)</span><span class=\"search-article__date\">";
	private static final String BOARD_BODY_6 = "</span><span class=\"search-article__like\">";
	private static final String BOARD_BODY_7 = "</span><span class=\"search-article__comment\">";
	private static final String BOARD_BODY_8 = "</span></div></a></li>";
	private static final String BOARD_BODY_9 = "<footer class=\"search-more\"><a href=\"/search/community?query=";
	//private static final String BOARD_BODY_10 = "\">게시판 더보기 (";
	private static final String BOARD_BODY_10 = "\">More from the Bulletin Board ("; 
	
	//private static final String TRADE_HEAD = "<section><header class=\"search-title\"><h2>거래소</h2></header><article class=\"search-article search-article--community\"><ul>";
	private static final String TRADE_HEAD = "<section><header class=\"search-title\"><h2>Marketplace</h2></header><article class=\"search-article search-article--community\"><ul>";
	private static final String TRADE_BODY_1 = "<li><a href=\"javascript:urlform('";
	private static final String TRADE_BODY_2 = "', 'post', '/trade/view');\"><span class=\"trade_status\">";
	private static final String TRADE_BODY_3 = "</span><div class=\"search-article__subject\">";
	private static final String TRADE_BODY_4 = "</div><div class=\"search-article__desc\">";
	private static final String TRADE_BODY_5 = "</div><div class=\"search-article__info\"><span class=\"search-article__writer\">";
	//private static final String TRADE_BODY_6 = "</span><span class=\"search-article__category\">거래소</span><span class=\"search-article__date\">";
	private static final String TRADE_BODY_6 = "</span><span class=\"search-article__category\">Marketplace</span><span class=\"search-article__date\">";
	private static final String TRADE_BODY_7 = "</span></div></a></li>";
	private static final String TRADE_BODY_8 = "<footer class=\"search-more\"><a href=\"/search/trade?query=";
	//private static final String TRADE_BODY_9 = "\">거래소 더보기 (";
	private static final String TRADE_BODY_9 = "\">More from the Marketplace ("; 
	
	private static final String CONTENT_HEAD = "<section><header class=\"search-title\"><h2>Contest</h2></header><article class=\"search-article search-article--community\"><ul>";
	private static final String CONTENT_BODY_1 = "<li><a href=\"javascript:urlform('";
	private static final String CONTENT_BODY_2 = "', 'post', '/contents/view');\"><div class=\"search-article__subject\">";
	private static final String CONTENT_BODY_3 = "</div><div class=\"search-article__desc\">";
	private static final String CONTENT_BODY_4 = "</div><div class=\"search-article__info\"><span class=\"search-article__writer\">";
	private static final String CONTENT_BODY_5 = "</span><span class=\"search-article__category\">Contest</span><span class=\"search-article__date\">";
	private static final String CONTENT_BODY_6 = "</span><span class=\"search-article__like\">";
	private static final String CONTENT_BODY_7 = "</span><span class=\"search-article__comment\">";
	private static final String CONTENT_BODY_8 = "</span></div></a></li>";
	private static final String CONTENT_BODY_9 = "<footer class=\"search-more\"><a href=\"/search/content?query=";
	//private static final String CONTENT_BODY_10 = "\">컨텐츠 공모 더보기 (";
	private static final String CONTENT_BODY_10 = "\">More from the Content Contest ("; 
	
	//private static final String SEARCH_COUNT_TAIL = "건)</a></footer>";
	private static final String SEARCH_COUNT_TAIL = " results)</a></footer>";
	private static final String SEARCH_BODY = "</ul></article>";
	private static final String SEARCH_TAIL = "</section>";
			
	public SearchResponse() {}
	private SearchResponse(HttpRequestModel request, DispatcherModel model) {
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
		
		String query = request.read_parameters_at_once("query");
		
		List<GoodsVO> goodsList			= GoodsDAO.getKeywordGoods(query);
		List<L1Info> powerbookList		= L1InfoDAO.getInfoQuery(query);
		List<BoardVO> boardList			= BoardDAO.getSearchList(99999, DEFAULT_SEARCH_TYPE, query);
		List<TradeVO> tradeList			= TradeDAO.getSearchList(99999, DEFAULT_SEARCH_TYPE, query);
		List<ContentVO> contentList		= ContentDAO.getSearchList(99999, DEFAULT_SEARCH_TYPE, query);
		
		params.add(new KeyValuePair<String, String>("{QUERY}",				query));
		params.add(new KeyValuePair<String, String>("{SEARCH_CODE}",		StringUtil.ZeroString));
		
		StringBuilder sb = new StringBuilder();
		if (goodsList != null && !goodsList.isEmpty()) {
			sb.append(NSHOP_HEAD);
			int cnt = 0;
			for (GoodsVO goods : goodsList) {
				if (cnt >= 4) {
					break;
				}
				sb.append(NSHOP_BODY_1);
				sb.append(goods.getId());
				sb.append(NSHOP_BODY_2);
				sb.append(goods.getInvgfx());
				sb.append(NSHOP_BODY_3);
				sb.append(goods.getItemname());
				if (goods.getPack() > 0) {
					sb.append(goods.getPack()).append(NSHOP_PACK_COUNT);
				}
				sb.append(NSHOP_BODY_4);
				sb.append(StringUtil.comma(goods.getPrice()));
				sb.append(NSHOP_BODY_5);
				cnt++;
			}
			sb.append(NSHOP_BODY_6);
			sb.append(query);
			sb.append(NSHOP_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{NSHOP}",				sb.toString()));
		sb.setLength(0);
		
		if (powerbookList != null && !powerbookList.isEmpty()) {
			sb.append(POWERBOOK_HEAD);
			int cnt = 0;
			for (L1Info info : powerbookList) {
				if (cnt >= 6) {
					break;
				}
				int infoType = info.getInfoType();
				sb.append(POWERBOOK_BODY_1);
				sb.append(infoType);
				sb.append(POWERBOOK_BODY_2);
				sb.append(StringUtil.encodeUrl(info.getName()));
				sb.append(POWERBOOK_BODY_3);
				if (infoType == 1 || infoType == 3) {
					sb.append(POWERBOOK_BODY_4);
					sb.append(info.getInfo().get("icon"));
					sb.append(POWERBOOK_BODY_5);
				} else if (infoType == 4) {
					sb.append(POWERBOOK_BODY_6);
					sb.append(info.getInfo().get("mainImg"));
					sb.append(POWERBOOK_BODY_7);
				}
				sb.append(POWERBOOK_BODY_8);
				sb.append(info.getName());
				sb.append(POWERBOOK_BODY_9);
				sb.append(info.getInfoText());
				sb.append(POWERBOOK_BODY_10);
				cnt++;
			}
			sb.append(SEARCH_BODY);
			if (powerbookList.size() > 6) {
				sb.append(POWERBOOK_BODY_11);
				sb.append(StringUtil.encodeUrl(query));
				sb.append(POWERBOOK_BODY_12);
				sb.append(StringUtil.comma(powerbookList.size()));
				sb.append(SEARCH_COUNT_TAIL);
			}
			sb.append(SEARCH_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{POWERBOOK}",			sb.toString()));
		sb.setLength(0);
		
		if (boardList != null && !boardList.isEmpty()) {
			sb.append(BOARD_HEAD);
			int cnt = 0;
			for (BoardVO board : boardList) {
				if (cnt >= 6) {
					break;
				}
				sb.append(BOARD_BODY_1);
				sb.append(board.getRownum());
				sb.append(BOARD_BODY_2);
				sb.append(board.getTitle());
				sb.append(BOARD_BODY_3);
				sb.append(board.getContent());
				sb.append(BOARD_BODY_4);
				sb.append(board.getName());
				sb.append(BOARD_BODY_5);
				sb.append(StringUtil.getFormatDate(board.getDate()));
				sb.append(BOARD_BODY_6);
				sb.append(board.getLikenames() == null ? 0 : board.getLikenames().size());
				sb.append(BOARD_BODY_7);
				sb.append(board.getAnswerList() == null ? 0 : board.getAnswerList().size());
				sb.append(BOARD_BODY_8);
				cnt++;
			}
			sb.append(SEARCH_BODY);
			if (boardList.size() > 6) {
				sb.append(BOARD_BODY_9);
				sb.append(StringUtil.encodeUrl(query));
				sb.append(BOARD_BODY_10);
				sb.append(StringUtil.comma(boardList.size()));
				sb.append(SEARCH_COUNT_TAIL);
			}
			sb.append(SEARCH_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{BOARD}",				sb.toString()));
		sb.setLength(0);
		
		if (tradeList != null && !tradeList.isEmpty()) {
			sb.append(TRADE_HEAD);
			int cnt = 0;
			for (TradeVO trade : tradeList) {
				if (cnt >= 6) {
					break;
				}
				sb.append(TRADE_BODY_1);
				sb.append(trade.getRownum());
				sb.append(TRADE_BODY_2);
				sb.append(trade.getStatus());
				sb.append(TRADE_BODY_3);
				sb.append(trade.getTitle());
				sb.append(TRADE_BODY_4);
				sb.append(trade.getContent());
				sb.append(TRADE_BODY_5);
				sb.append(trade.getSellerCharacter());
				sb.append(TRADE_BODY_6);
				sb.append(StringUtil.getFormatDate(trade.getWriteTime()));
				sb.append(TRADE_BODY_7);
				cnt++;
			}
			sb.append(SEARCH_BODY);
			if (tradeList.size() > 6) {
				sb.append(TRADE_BODY_8);
				sb.append(StringUtil.encodeUrl(query));
				sb.append(TRADE_BODY_9);
				sb.append(StringUtil.comma(tradeList.size()));
				sb.append(SEARCH_COUNT_TAIL);
			}
			sb.append(SEARCH_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{TRADE}",				sb.toString()));
		sb.setLength(0);
		
		if (contentList != null && !contentList.isEmpty()) {
			sb.append(CONTENT_HEAD);
			int cnt = 0;
			for (ContentVO content : contentList) {
				if (cnt >= 6) {
					break;
				}
				sb.append(CONTENT_BODY_1);
				sb.append(content.getRownum());
				sb.append(CONTENT_BODY_2);
				sb.append(content.getTitle());
				sb.append(CONTENT_BODY_3);
				sb.append(content.getContent());
				sb.append(CONTENT_BODY_4);
				sb.append(content.getName());
				sb.append(CONTENT_BODY_5);
				sb.append(StringUtil.getFormatDate(content.getDate()));
				sb.append(CONTENT_BODY_6);
				sb.append(content.getLikenames() == null ? 0 : content.getLikenames().size());
				sb.append(CONTENT_BODY_7);
				sb.append(content.getAnswerList() == null ? 0 : content.getAnswerList().size());
				sb.append(CONTENT_BODY_8);
				cnt++;
			}
			sb.append(SEARCH_BODY);
			if (contentList.size() > 6) {
				sb.append(CONTENT_BODY_9);
				sb.append(StringUtil.encodeUrl(query));
				sb.append(CONTENT_BODY_10);
				sb.append(StringUtil.comma(contentList.size()));
				sb.append(SEARCH_COUNT_TAIL);
			}
			sb.append(SEARCH_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{CONTENT}",			sb.toString()));
		sb.setLength(0);
		
		if (goodsList == null && powerbookList == null && boardList == null && tradeList == null && contentList == null) {
			sb.append(NO_DATA);
		}
		params.add(new KeyValuePair<String, String>("{EMPTY}",				sb.toString()));
		sb.setLength(0);
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new SearchResponse(request, model);
	}
}


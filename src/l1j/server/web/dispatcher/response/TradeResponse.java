package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class TradeResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	private static final String TOP_MSG_HEAD = "<div class=\"ncCommunityNoticeList board-notice\">";
	private static final String TOP_MSG_BODY_1 = "<div class=\"board-notice-item is-new\" onClick=\"urlform(\'";
	private static final String TOP_MSG_BODY_2 = "\', \'post\', \'/trade/view\');\"><span class=\"category\">Notification</span><a href=\"javascript:urlform(\'";
	private static final String TOP_MSG_BODY_3 = "\', \'post\', \'/trade/view\');\">";
	private static final String TOP_MSG_BODY_4 = "</a><i class=\"fe-icon-new\"></i><div class=\"info\"><span class=\"writer\"><img src=\"/img/lineage_writer.png\" alt=\"\"></span></div></div>";
	private static final String TOP_MSG_TAIL = "</div>";
	
	private static final String WRITE_BUTTON_HEAD = "<a href=\"";
	private static final String WRITE_BUTTON_BODY_1 = "/trade/write";
	private static final String WRITE_BUTTON_BODY_2 = "javascript:login();";
	private static final String WRITE_BUTTON_TAIL = "\" class=\"co-btn co-btn-round btn-write board-write-btn\">Add Trade</a>";
	
	public TradeResponse() {}
	private TradeResponse(HttpRequestModel request, DispatcherModel model) {
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
		
		ArrayList<TradeVO> topTrade = TradeDAO._tradeNotice;
		StringBuilder sb = new StringBuilder();
		if (!topTrade.isEmpty()) {
			sb.append(TOP_MSG_HEAD);
			for (TradeVO top : topTrade) {
				sb.append(TOP_MSG_BODY_1);
				sb.append(top.getRownum());
				sb.append(TOP_MSG_BODY_2);
				sb.append(top.getRownum());
				sb.append(TOP_MSG_BODY_3);
				sb.append(top.getTitle());
				sb.append(TOP_MSG_BODY_4);
			}
			sb.append(TOP_MSG_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{TOP_NOTICE}",			sb.toString()));
		params.add(new KeyValuePair<String, String>("{WRITE_BUTTON}",		WRITE_BUTTON_HEAD + (player != null ? WRITE_BUTTON_BODY_1 : WRITE_BUTTON_BODY_2) + WRITE_BUTTON_TAIL));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeResponse(request, model);
	}
}


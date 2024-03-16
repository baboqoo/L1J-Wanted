package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class TradeViewResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	private static final String VIEW_INFO_TOP_HEAD = "<div class=\"view-info\"><span class=\"writer\"><img src=\"/img/lineage_writer.png\" alt=\"\"></span><span class=\"date\">";
	private static final String VIEW_INFO_HEAD = "<div class=\"view-info\"><span class=\"writer\">";
	private static final String VIEW_INFO_BODY = "<span class=\"server\">" + Config.WEB.WEB_SERVER_NAME + "</span></span><span class=\"date\">";
	private static final String VIEW_INFO_TAIL = "</span></div>";
	
	//private static final String STATUS_HEAD = "<div class=\"trade-area\"><div class=\"trade-status\"><span class=\"caption\">《  상태 - ";
	private static final String STATUS_HEAD = "<div class=\"trade-area\"><div class=\"trade-status\"><span class=\"caption\">《 Status - ";
	private static final String STATUS_BODY = " 》</span>";
	private static final String DIV_TAIL = "</div>";
	
	//private static final String SELL_HEAD = "<div class=\"trade-util\"><span class=\"status_txt\">판매중인 물품입니다.</span><a href=\"";
	//private static final String SELL_TAIL = "\" class=\"co-btn co-btn-round co-btn-write\">구매신청</a></div>";
	private static final String SELL_HEAD = "<div class=\"trade-util\"><span class=\"status_txt\">Items for sale.</span><a href=\"";
	private static final String SELL_TAIL = "\" class=\"co-btn co-btn-round co-btn-write\">Purchase</a></div>";	
	private static final String[] SELL_JS = { "javascript:buyPopOpen();", "javascript:login();" }; 
	
	private static final String TRADING_HEAD = "<div class=\"trade-util\">";
	private static final String[] TRADING_BODYS = {
		//"<span class=\"status_txt\">안전한 거래를 위해 거래자와 통화후 진행하십시오.</br>물품을 전달하시면 인계버튼을 눌러주세요.</span>",
		//"<span class=\"status_txt\">거래를 진행하고 있습니다.</span>"
		"<span class=\"status_txt\">For a safe transaction, please communicate with the seller and proceed. </br> Press the transfer button after delivering the item.</span>",
		"<span class=\"status_txt\">In progress with the transaction.</span>"		
	};
	
	//private static final String FINISHED = "<div class=\"trade-util\"><span class=\"status_txt\">거래가 종료된 물품입니다.</span></div>";
	private static final String FINISHED = "<div class=\"trade-util\"><span class=\"status_txt\">The transaction has ended.</span></div>";
	
	/*private static final String TRADE_INFO_1 = "<div class=\"trade-info seller-info\"><div class=\"caption\"><span>판매자 정보</span></div><div class=\"seller-info-con bankName\"><label class=\"fl\">은행명</label><span class=\"fr\">";
	private static final String TRADE_INFO_2 = "</span></div><div class=\"seller-info-con bankNumber\"><label class=\"fl\">계좌번호</label><span class=\"fr\">";
	private static final String TRADE_INFO_3 = "</span></div><div class=\"seller-info-con sellerName\"><label class=\"fl\">계좌주</label><span class=\"fr\">";
	private static final String TRADE_INFO_4 = "</span></div><div class=\"seller-info-con sellerPhone\"><label class=\"fl\">연락처</label><span class=\"fr\">";
	private static final String TRADE_INFO_5 = "</span></div></div><div class=\"trade-info buyer-info\"><div class=\"caption\"><span>구매자 정보</span></div><div class=\"buyer-info-con buyerCharacter\"><label class=\"fl\">케릭터</label><span class=\"fr\">";
	private static final String TRADE_INFO_6 = "</span></div><div class=\"buyer-info-con buyerName\"><label class=\"fl\">구매자</label><span class=\"fr\">";
	private static final String TRADE_INFO_7 = "</span></div><div class=\"buyer-info-con buyerPhone\"><label class=\"fl\">연락처</label><span class=\"fr\">";
	private static final String TRADE_INFO_8 = "</span></div><div class=\"buyer-info-con buyerEmpty\"></div></div>";
	private static final String TRADE_INFO_EMPTY = "-";*/
	private static final String TRADE_INFO_1 = "<div class=\"trade-info seller-info\"><div class=\"caption\"><span>Seller Information</span></div><div class=\"seller-info-con bankName\"><label class=\"fl\">Bank Name</label><span class=\"fr\">";
	private static final String TRADE_INFO_2 = "</span></div><div class=\"seller-info-con bankNumber\"><label class=\"fl\">Account Number</label><span class=\"fr\">";
	private static final String TRADE_INFO_3 = "</span></div><div class=\"seller-info-con sellerName\"><label class=\"fl\">Account Holder</label><span class=\"fr\">";
	private static final String TRADE_INFO_4 = "</span></div><div class=\"seller-info-con sellerPhone\"><label class=\"fl\">Contact Number</label><span class=\"fr\">";
	private static final String TRADE_INFO_5 = "</span></div></div><div class=\"trade-info buyer-info\"><div class=\"caption\"><span>Buyer Information</span></div><div class=\"buyer-info-con buyerCharacter\"><label class=\"fl\">Character</label><span class=\"fr\">";
	private static final String TRADE_INFO_6 = "</span></div><div class=\"buyer-info-con buyerName\"><label class=\"fl\">Buyer</label><span class=\"fr\">";
	private static final String TRADE_INFO_7 = "</span></div><div class=\"buyer-info-con buyerPhone\"><label class=\"fl\">Contact Number</label><span class=\"fr\">";
	private static final String TRADE_INFO_8 = "</span></div><div class=\"buyer-info-con buyerEmpty\"></div></div>";
	private static final String TRADE_INFO_EMPTY = "-";
	
	private static final String BUTTON_1 = "<button class=\"co-btn co-btn-modify\" onClick=\"javascript:modifyTradeAction(\'";
	private static final String BUTTON_2 = "\');\">Edit</button>&nbsp;<button class=\"co-btn co-btn-delete\" onClick=\"javascript:delecteTradeConfirm(\'";
	private static final String BUTTON_3 = "\');\">Delete</button>";
	private static final String BUTTON_4 = "&nbsp;<button class=\"co-btn co-btn-report\" data-id=\"";
	private static final String BUTTON_5 = "\" data-name=\"";
	private static final String BUTTON_6 = "\" data-boardid=\"";
	private static final String BUTTON_7 = "\" onClick=\"reportTradeBtn(this);\">Report</button>";
	
	//private static final String BUY_REQUEST_BUTTON = "<div class=\"btn_area\"><button class=\"btn_create\" onClick=\"javascript:buyRegist();\">신청</button><button class=\"btn_close\" onClick=\"javascript:buyPopClose();\">Close</button></div>";
	private static final String BUY_REQUEST_BUTTON = "<div class=\"btn_area\"><button class=\"btn_create\" onClick=\"javascript:buyRegist();\">Apply</button><button class=\"btn_close\" onClick=\"javascript:buyPopClose();\">Close</button></div>";
	
	public TradeViewResponse() {}
	private TradeViewResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String id = request.read_parameters_at_once("id");
		if (StringUtil.isNullOrEmpty(id)) {
			id = request.read_post("num");
		}
		if (StringUtil.isNullOrEmpty(id)) {
			return sendRedirect("/trade");
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
		
		TradeVO vo			= TradeDAO.getTrade(Integer.parseInt(id));
		
		String viewInfo		= StringUtil.EmptyString;
		StringBuilder sb	= new StringBuilder();
		if (vo.isTop()) {
			viewInfo		= VIEW_INFO_TOP_HEAD + vo.getWriteTime().toString() + VIEW_INFO_TAIL;
		} else {
			viewInfo		= VIEW_INFO_HEAD + vo.getSellerCharacter() + VIEW_INFO_BODY + vo.getWriteTime().toString() + VIEW_INFO_TAIL;
			
			sb.append(STATUS_HEAD);
			sb.append(vo.getStatus());
			sb.append(STATUS_BODY);
			switch(vo.getStatus()) {
			case SELL:
				sb.append(SELL_HEAD);
				sb.append(account != null ? SELL_JS[0] : SELL_JS[1]);
				sb.append(SELL_TAIL);
				break;
			case IN_PROGRESS:
				sb.append(TRADING_HEAD);
				if (account != null && (account.isGm() || (player != null && player.getName().equals(vo.getSellerCharacter())))) {
					sb.append(TRADING_BODYS[0]);
				} else if (account != null && (account.isGm() || (player != null && player.getName().equals(vo.getBuyerCharacter())))) {
					sb.append(TRADING_BODYS[0]);
				} else {
					sb.append(TRADING_BODYS[1]);
				}
				sb.append(DIV_TAIL);
				break;
			case COMPLETED:
				sb.append(FINISHED);
				break;
			default:break;
			}
			sb.append(DIV_TAIL);
			
			if (account != null && (account.isGm() || (player != null && (player.getName().equals(vo.getSellerCharacter()) || player.getName().equals(vo.getBuyerCharacter()))))) {
				sb.append(TRADE_INFO_1);
				sb.append(vo.getBank());
				sb.append(TRADE_INFO_2);
				sb.append(vo.getBankNumber());
				sb.append(TRADE_INFO_3);
				sb.append(vo.getSellerName());
				sb.append(TRADE_INFO_4);
				sb.append(vo.getSellerPhone());
				sb.append(TRADE_INFO_5);
				sb.append(vo.getBuyerCharacter() != null ? vo.getBuyerCharacter() : TRADE_INFO_EMPTY);
				sb.append(TRADE_INFO_6);
				sb.append(vo.getBuyerName() != null ? vo.getBuyerName() : TRADE_INFO_EMPTY);
				sb.append(TRADE_INFO_7);
				sb.append(vo.getBuyerPhone() != null ? vo.getBuyerPhone() : TRADE_INFO_EMPTY);
				sb.append(TRADE_INFO_8);
			}
			sb.append(DIV_TAIL);
		}
		
		params.add(new KeyValuePair<String, String>("{ID}",					String.valueOf(vo.getId())));
		params.add(new KeyValuePair<String, String>("{ROWNUM}",				String.valueOf(vo.getRownum())));
		params.add(new KeyValuePair<String, String>("{TITLE}",				vo.getTitle()));
		params.add(new KeyValuePair<String, String>("{CONTENT}",			vo.getContent()));
		params.add(new KeyValuePair<String, String>("{VIEW_INFO}",			viewInfo));
		params.add(new KeyValuePair<String, String>("{STATUS_INFO}",		sb.toString()));
		
		sb.setLength(0);
		if (account != null && (account.isGm() || (player != null && player.getName().equals(vo.getSellerCharacter())))) {
			sb.append(BUTTON_1);
			sb.append(vo.getRownum());
			sb.append(BUTTON_2);
			sb.append(vo.getRownum());
			sb.append(BUTTON_3);
		}
		if (!vo.isTop()) {
			sb.append(BUTTON_4);
			sb.append(vo.getRownum());
			sb.append(BUTTON_5);
			sb.append(vo.getSellerCharacter());
			sb.append(BUTTON_6);
			sb.append(vo.getId());
			sb.append(BUTTON_7);
		}
		params.add(new KeyValuePair<String, String>("{UTIL_BUTTON}",		sb.toString()));
		params.add(new KeyValuePair<String, String>("{BUY_REQUEST_BUTTON}",	player != null ? BUY_REQUEST_BUTTON : StringUtil.EmptyString));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeViewResponse(request, model);
	}
}


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

public class TradeModifyResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	public TradeModifyResponse() {}
	private TradeModifyResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String id					= request.read_post("num");
		if (StringUtil.isNullOrEmpty(id)) {
			return sendRedirect("/trade");
		}
		TradeVO vo = TradeDAO.getTrade(Integer.parseInt(id));
		if (vo == null) {
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
		
		params.add(new KeyValuePair<String, String>("{ROWNUM}",				String.valueOf(vo.getRownum())));
		params.add(new KeyValuePair<String, String>("{TITLE}",				vo.getTitle()));
		params.add(new KeyValuePair<String, String>("{CONTENT}",			vo.getContent()));
		params.add(new KeyValuePair<String, String>("{BANK}",				vo.getBank()));
		params.add(new KeyValuePair<String, String>("{BANK_NUMBER}",		vo.getBankNumber()));
		params.add(new KeyValuePair<String, String>("{SELLER_NAME}",		vo.getSellerName()));
		params.add(new KeyValuePair<String, String>("{SELLER_PHONE}",		vo.getSellerPhone()));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeModifyResponse(request, model);
	}
}


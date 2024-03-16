package l1j.server.web.dispatcher.response;

import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class TradeUpdateResponse extends HttpResponseModel {
	public TradeUpdateResponse() {}
	private TradeUpdateResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		int rownum			= Integer.parseInt(post.get("rownum"));
		String title		= post.get("title");
		String content		= post.get("content");
		String bank			= post.get("bank");
		String bankNumber	= post.get("bankNumber");
		String sellerName	= post.get("sellerName");
		String sellerPhone	= post.get("sellerPhone");
		TradeDAO dao		= TradeDAO.getInstance();
		TradeVO vo			= TradeDAO.getTrade(rownum);
		vo.setTitle(title);
		vo.setContent(content);
		vo.setBank(bank);
		vo.setBankNumber(bankNumber);
		vo.setSellerName(sellerName);
		vo.setSellerPhone(sellerPhone);
		dao.update(vo);
		return sendRedirect("/trade");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeUpdateResponse(request, model);
	}
}


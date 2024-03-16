package l1j.server.web.dispatcher.response;

import java.sql.Timestamp;
import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeStatus;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class TradeInsertResponse extends HttpResponseModel {
	public TradeInsertResponse() {}
	private TradeInsertResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String title		= post.get("title");
		String content		= post.get("content");
		String bank			= post.get("bank");
		String bankNumber	= post.get("bankNumber");
		String sellerName	= post.get("sellerName");
		String sellerPhone	= post.get("sellerPhone");
		TradeDAO dao		= TradeDAO.getInstance();
		TradeVO vo			= new TradeVO();
		vo.setId(dao.getNextNum());
		vo.setTitle(title);
		vo.setContent(content);
		vo.setBank(bank);
		vo.setBankNumber(bankNumber);
		vo.setStatus(TradeStatus.SELL);
		vo.setSellerName(sellerName);
		vo.setSellerCharacter(player.getName());
		vo.setSellerPhone(sellerPhone);
		vo.setWriteTime(new Timestamp(System.currentTimeMillis()));
		vo.setRownum(1);
		dao.insert(vo);
		return sendRedirect("/trade");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeInsertResponse(request, model);
	}
}


package l1j.server.web.dispatcher.response;

import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeStatus;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class TradeBuyRegistResponse extends HttpResponseModel {
	public TradeBuyRegistResponse() {}
	private TradeBuyRegistResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (player == null) {
			return sendRedirect(request.isIngame() ? "/login_ingame" : "/login");
		}
		Map<String, String> post = request.get_post_datas();
		int rownum				= Integer.parseInt(post.get("rownum"));
		String buyerName		= post.get("buyerName");
		String buyerPhone		= post.get("buyerPhone");
		TradeDAO dao			= TradeDAO.getInstance();
		TradeVO vo				= TradeDAO.getTrade(rownum);
		vo.setStatus(TradeStatus.IN_PROGRESS);
		vo.setBuyerCharacter(player.getName());
		vo.setBuyerName(buyerName);
		vo.setBuyerPhone(buyerPhone);
		dao.buyRegist(vo);
		return sendRedirect("/trade/view?id=" + rownum);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeBuyRegistResponse(request, model);
	}
}


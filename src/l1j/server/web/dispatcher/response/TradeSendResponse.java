package l1j.server.web.dispatcher.response;

import java.sql.Timestamp;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeStatus;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class TradeSendResponse extends HttpResponseModel {
	public TradeSendResponse() {}
	private TradeSendResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (player == null) {
			return sendRedirect(request.isIngame() ? "/login_ingame" : "/login");
		}
		String num			= request.read_parameters_at_once("rownum");
		if (StringUtil.isNullOrEmpty(num)) {
			return sendRedirect("/trade");
		}
		
		int rownum			= Integer.parseInt(num);
		TradeDAO dao		= TradeDAO.getInstance();
		TradeVO vo			= TradeDAO.getTrade(rownum);
		if (!vo.isSend()) {
			vo.setSend(true);
			dao.trading(vo);
			
			// 거래 완료
			if (vo.isSend() && vo.isReceive()) {
				vo.setStatus(TradeStatus.COMPLETED);
				vo.setCompleteTime(new Timestamp(System.currentTimeMillis()));
				dao.complete(vo);
			}
		}
		return sendRedirect("/trade/view?id=" + rownum);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeSendResponse(request, model);
	}
}


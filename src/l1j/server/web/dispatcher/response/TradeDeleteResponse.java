package l1j.server.web.dispatcher.response;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.trade.TradeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class TradeDeleteResponse extends HttpResponseModel {
	public TradeDeleteResponse() {}
	private TradeDeleteResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		int rownum		= Integer.parseInt(request.read_post("num"));
		TradeDAO dao	= TradeDAO.getInstance();
		TradeVO vo		= TradeDAO.getTrade(rownum);
		if (vo != null) {
			dao.delete(vo);
		}
		return sendRedirect("/trade");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new TradeDeleteResponse(request, model);
	}
}


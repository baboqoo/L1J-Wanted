package l1j.server.web.dispatcher.response.define;

import java.util.List;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.coupon.CouponDAO;
import l1j.server.web.dispatcher.response.coupon.CouponVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class CouponDefine extends HttpJsonModel {
	public CouponDefine() {}
	private CouponDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String status	= request.read_post("status");
		CouponDAO dao	= CouponDAO.getInstance();
		List<CouponVO> list = null;
		switch(status) {
		case "0":		list = dao.getList(false);break;
		case "1":		list = dao.getList(true);break;
		case "-1":		list = dao.getList();break;
		default:		list = dao.getList(status);break;
		}
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(list));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CouponDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


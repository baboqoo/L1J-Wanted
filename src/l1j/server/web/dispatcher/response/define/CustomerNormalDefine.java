package l1j.server.web.dispatcher.response.define;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.customer.CustomerDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class CustomerNormalDefine extends HttpJsonModel {
	public CustomerNormalDefine() {}
	private CustomerNormalDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(CustomerDAO.getInstance().getNormalList()));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CustomerNormalDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


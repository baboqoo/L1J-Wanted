package l1j.server.web.dispatcher.response.define;

import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.customer.CustomerDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class CustomerDefine extends HttpJsonModel {
	public CustomerDefine() {}
	private CustomerDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		int type		= Integer.parseInt(post.get("type"));
		String login	= post.get("login");
		if (account != null && account.isGm()) {
			return create_response_json(HttpResponseStatus.OK, new Gson().toJson(CustomerDAO.getInstance().getCustomerMap(type).values()));
		}
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(CustomerDAO.getInstance().getCustomerData(type, login)));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CustomerDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


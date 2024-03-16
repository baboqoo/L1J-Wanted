package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.sql.Timestamp;
import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.customer.CustomerDAO;
import l1j.server.web.dispatcher.response.customer.CustomerVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class CustomerInsertDefine extends HttpJsonModel {
	public CustomerInsertDefine() {}
	private CustomerInsertDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		Map<String, String> post = request.get_post_datas();
		int type			= Integer.parseInt(post.get("customType"));
		String title		= post.get("subject");
		String content		= post.get("content");
		CustomerDAO custom	= CustomerDAO.getInstance();
		// Status. If we want to translate it, we need to modify DB etc.
		CustomerVO vo		= new CustomerVO(custom.getNextNum(), account.getName(), type, title, content, "Submitted", new Timestamp(System.currentTimeMillis()), null, null, 1);
		boolean result		= custom.insert(vo);
		return create_response_json(HttpResponseStatus.OK, result ? TRUE_JSON : FALSE_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CustomerInsertDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


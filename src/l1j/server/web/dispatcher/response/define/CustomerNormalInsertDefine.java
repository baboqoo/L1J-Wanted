package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.customer.CustomerDAO;
import l1j.server.web.dispatcher.response.customer.CustomerNormalVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class CustomerNormalInsertDefine extends HttpJsonModel {
	public CustomerNormalInsertDefine() {}
	private CustomerNormalInsertDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		CustomerDAO dao		= CustomerDAO.getInstance();
		String title		= post.get("faqTitle");
		String content		= post.get("faqContent");
		CustomerNormalVO vo	= new CustomerNormalVO(dao.getNormalNextNum(), title, content);
		boolean result		= dao.insertNormal(vo);
		return create_response_json(HttpResponseStatus.OK, result ? TRUE_JSON : FALSE_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CustomerNormalInsertDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


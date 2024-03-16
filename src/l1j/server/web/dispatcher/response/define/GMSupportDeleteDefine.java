package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.support.SupportBankRequestVO;
import l1j.server.web.dispatcher.response.support.SupportDAO;
import l1j.server.web.dispatcher.response.support.SupportVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class GMSupportDeleteDefine extends HttpJsonModel {
	public GMSupportDeleteDefine() {}
	private GMSupportDeleteDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	
	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
		}
		
		int support_id			= Integer.parseInt(request.read_post("support_id"));
		String type				= request.read_post("type");
		if (type.equals("DEFAULT")) {
			SupportVO vo			= SupportDAO.getSupport(support_id);
			if (vo == null) {
				return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
			}
			
			if (SupportDAO.getInstance().delete(vo)) {
				return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
			}
		} else {
			SupportBankRequestVO vo	= SupportDAO.getSupportRequest(support_id);
			if (vo == null) {
				return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
			}
			
			if (SupportDAO.getInstance().deleteRequest(vo)) {
				return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
			}
		}
		
		return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GMSupportDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


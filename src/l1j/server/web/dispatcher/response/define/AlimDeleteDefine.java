package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.alim.AlimDAO;
import l1j.server.web.dispatcher.response.alim.AlimVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

public class AlimDeleteDefine extends HttpJsonModel {
	public AlimDeleteDefine() {}
	private AlimDeleteDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		String param = request.read_post("id");
		if (StringUtil.isNullOrEmpty(param)) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		
		int id = Integer.parseInt(param);
		AlimVO deleteAlim = null;
		for (AlimVO info : account.getAlimList()) {
			if (info.getId() == id) {
				deleteAlim = info;
				break;
			}
		}
		if (deleteAlim == null){
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		
		if (!AlimDAO.getInstance().delete(deleteAlim)) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(account.getAlimList()));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new AlimDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


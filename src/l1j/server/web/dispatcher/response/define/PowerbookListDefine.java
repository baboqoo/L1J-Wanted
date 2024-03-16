package l1j.server.web.dispatcher.response.define;

import java.util.List;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.powerbook.L1Info;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class PowerbookListDefine extends HttpJsonModel {
	public PowerbookListDefine() {}
	private PowerbookListDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String query = request.read_post("query");
		List<L1Info> infoList = null;
		if (!StringUtil.isNullOrEmpty(query)) {
			infoList = L1InfoDAO.getInfoQuery(query);
		}
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(infoList));
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PowerbookListDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


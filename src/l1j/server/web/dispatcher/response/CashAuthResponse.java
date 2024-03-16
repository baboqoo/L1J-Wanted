package l1j.server.web.dispatcher.response;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class CashAuthResponse extends HttpResponseModel {
	public CashAuthResponse() {}
	private CashAuthResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (player == null) {
			//return new AlertRedirectResponse(request, "대표 캐릭터를 찾을 수 없습니다.", "/index").get_response();
			return new AlertRedirectResponse(request, "Cannot find the main character.", "/index").get_response();
		}
		L1PcInstance pc	= L1World.getInstance().getPlayer(player.getName());
		if (pc == null) {
			//return new AlertRedirectResponse(request, "월드 내 캐릭터가 존재하지 않습니다.", "/index").get_response();
			return new AlertRedirectResponse(request, "Character does not exist in the Aden world.", "/index").get_response();
		}
		Map<String, String> post = request.get_post_datas();
		String price = post.get("price");
		String count = post.get("count");
		if (StringUtil.isNullOrEmpty(price) || StringUtil.isNullOrEmpty(count)) {
			return sendRedirect("/index");
		}
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(new KeyValuePair<String, String>("{PRICE}",	price));
		params.add(new KeyValuePair<String, String>("{COUNT}",	count));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CashAuthResponse(request, model);
	}
}


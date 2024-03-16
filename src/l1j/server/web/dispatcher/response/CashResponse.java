package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class CashResponse extends HttpResponseModel {
	public CashResponse() {}
	private CashResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(SUGGEST_ENABLE_PAIR);
		params.add(get_cnb_pair());
		params.add(get_user_data_pair());
		params.add(get_now_pair());
		
		StringBuilder sb = new StringBuilder();
		if (account != null) {
			String ncoin = StringUtil.comma(account.getNcoin());
			String npoint = StringUtil.comma(account.getNpoint());
			sb.append(GoodsResponse.ACCOUNT_INFO_HEAD);
			sb.append(ncoin);
			sb.append(GoodsResponse.ACCOUNT_INFO_BODY_1);
			sb.append(npoint);
			sb.append(GoodsResponse.ACCOUNT_INFO_BODY_2);
			sb.append(ncoin);
			sb.append(GoodsResponse.ACCOUNT_INFO_BODY_3);
			sb.append(npoint);
			sb.append(GoodsResponse.ACCOUNT_INFO_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{NSHOP_ACCOUNT_INFO}",	sb.toString()));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CashResponse(request, model);
	}
}


package l1j.server.web.dispatcher.response;

import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountDAO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import io.netty.handler.codec.http.HttpResponse;

public class CharacterChangeResponse extends HttpResponseModel {
	public CharacterChangeResponse() {}
	private CharacterChangeResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return sendRedirect("/index");
		}
		Map<String, String> post = request.get_post_datas();
		int change_char		= Integer.parseInt(post.get("num"));
		String url			= post.get("urlType");
		AccountDAO.getInstance().changeCharacter(account, change_char);
		return sendRedirect(url);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CharacterChangeResponse(request, model);
	}

}


package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.powerbook.L1Info;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class PowerbookModifyResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	private static final String IMG_HEAD = "<div class=\"preview\"><span class=\"0th\" style=\"";
	private static final String IMG_BODY = "\"><img src=\"";
	private static final String IMG_TAIL = "\" id=\"0th\"><span onclick=\"deleteFile('0')\"></span></span></div>";
	private static final String STYLE_BLCOK = "display: block;";
	
	public PowerbookModifyResponse() {}
	private PowerbookModifyResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String id = request.read_parameters_at_once("id");
		if (StringUtil.isNullOrEmpty(id)) {
			return sendRedirect("/powerbook");
		}
		L1Info info = L1InfoDAO.getInstance().getInfoGuide(4, Integer.parseInt(id));
		if (info == null) {
			return sendRedirect("/powerbook");
		}
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(SUGGEST_ENABLE_PAIR);
		params.add(get_cnb_pair());
		params.add(get_user_data_pair());
		params.add(get_now_pair());
		if (PAGE_CNB_TYPE_PAIR == null) {
			PAGE_CNB_TYPE_PAIR		= new KeyValuePair<String, String>(PAGE_CNB_TYPE_KEY,		dispatcher.getCnbType());
		}
		if (PAGE_CNB_SUB_TYPE_PAIR == null) {
			PAGE_CNB_SUB_TYPE_PAIR	= new KeyValuePair<String, String>(PAGE_CNB_SUB_TYPE_KEY,	dispatcher.getCnbSubType());
		}
		params.add(PAGE_CNB_TYPE_PAIR);
		params.add(PAGE_CNB_SUB_TYPE_PAIR);
		
		params.add(new KeyValuePair<String, String>("{ID}",					id));
		params.add(new KeyValuePair<String, String>("{TITLE}",				info.getInfo().get("title").toString()));
		params.add(new KeyValuePair<String, String>("{CONTENT}",			info.getInfo().get("content").toString()));
		
		String mainImg	= StringUtil.EmptyString;
		if (info.getInfo().containsKey("mainImg")) {
			mainImg		= info.getInfo().get("mainImg").toString();
		}
		params.add(new KeyValuePair<String, String>("{MAIN_IMG}",			mainImg));
		
		StringBuilder sb = new StringBuilder();
		sb.append(IMG_HEAD).append(!StringUtil.isNullOrEmpty(mainImg) ? STYLE_BLCOK : StringUtil.EmptyString).append(IMG_BODY).append(mainImg).append(IMG_TAIL);
		params.add(new KeyValuePair<String, String>("{IMG_PREVIEW}",		sb.toString()));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PowerbookModifyResponse(request, model);
	}
}


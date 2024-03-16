package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.guide.GuideDAO;
import l1j.server.web.dispatcher.response.guide.GuideVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

import org.apache.commons.lang.StringEscapeUtils;

public class GuideModifyResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	public GuideModifyResponse() {}
	private GuideModifyResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String id				= request.read_parameters_at_once("id");
		if (StringUtil.isNullOrEmpty(id)) {
			return sendRedirect("/guide");
		}
		GuideDAO dao		= GuideDAO.getInstance();
		GuideVO cur			= dao.getGuide(Integer.parseInt(id));
		if (cur == null){
			return sendRedirect("/guide");
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
		params.add(new KeyValuePair<String, String>("{TITLE}",				cur.getTitle()));
		params.add(new KeyValuePair<String, String>("{CONTENT}",			cur.getContent()));
		params.add(new KeyValuePair<String, String>("{ESCAPE_CONTENT}",		StringEscapeUtils.escapeHtml(cur.getContent())));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GuideModifyResponse(request, model);
	}

}


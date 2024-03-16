package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.notice.NoticeDAO;
import l1j.server.web.dispatcher.response.notice.NoticeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

import org.apache.commons.lang.StringEscapeUtils;

public class NoticeModifyResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	
	private static final String[] NOTICE_TYPE_NAMES = {
		//"Notification", "업데이트", "이벤트"
		"Notification", "Update", "Event"
	};
	
	public NoticeModifyResponse() {}
	private NoticeModifyResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String number			= post.get("num");
		if (StringUtil.isNullOrEmpty(number)) {
			return sendRedirect("/notice");
		}
		int num				= Integer.parseInt(number);
		int type			= Integer.parseInt(post.get("type"));
		NoticeVO cur		= NoticeDAO.getNotice(num, type);
		
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(SUGGEST_ENABLE_PAIR);
		params.add(get_cnb_pair());
		params.add(get_user_data_pair());
		params.add(get_now_pair());
		if (PAGE_CNB_TYPE_PAIR == null) {
			PAGE_CNB_TYPE_PAIR		= new KeyValuePair<String, String>(PAGE_CNB_TYPE_KEY,		dispatcher.getCnbType());
		}
		params.add(PAGE_CNB_TYPE_PAIR);
		params.add(new KeyValuePair<String, String>(PAGE_CNB_SUB_TYPE_KEY,	String.valueOf(type+1)));
		
		params.add(new KeyValuePair<String, String>("{TITLE}",				cur.getTitle()));
		params.add(new KeyValuePair<String, String>("{CONTENT}",			cur.getContent()));
		params.add(new KeyValuePair<String, String>("{ESCAPE_CONTENT}",		StringEscapeUtils.escapeHtml(cur.getContent())));
		params.add(new KeyValuePair<String, String>("{ROWNUM}",				String.valueOf(cur.getRownum())));
		params.add(new KeyValuePair<String, String>("{MAIN_IMG}",			!StringUtil.isNullOrEmpty(cur.getMainImg()) ? cur.getMainImg() : StringUtil.EmptyString));
		params.add(new KeyValuePair<String, String>("{NOTICE_TYPE}",		String.valueOf(type)));
		params.add(new KeyValuePair<String, String>("{NOTICE_TYPE_NAME}",	NOTICE_TYPE_NAMES[type]));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new NoticeModifyResponse(request, model);
	}
}


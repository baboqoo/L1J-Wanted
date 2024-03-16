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

public class NoticeViewResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	
	//private static final String PREV_HEAD = "<p class=\"prev\"><span>이전글</span><a href=\"javascript:urlTypeform(\'";
	//private static final String NEXT_HEAD = "<p class=\"prev\"><span>다음글</span><a href=\"javascript:urlTypeform(\'";
	private static final String PREV_HEAD = "<p class=\"prev\"><span>Previous Article</span><a href=\"javascript:urlTypeform(\'";
	private static final String NEXT_HEAD = "<p class=\"prev\"><span>Next Article</span><a href=\"javascript:urlTypeform(\'";	
	private static final String BOTTOM_BODY_1 = "\', \'";
	private static final String BOTTOM_BODY_2 = "\', \'post\', \'/notice/view\');\">";
	private static final String BOTTOM_TAIL = "</a></p>";
	
	private static final String BUTTON_HEAD = "<button class=\"co-btn co-btn-modify\" onClick=\"javascript:urlTypeform(\'";
	private static final String BUTTON_BODY_AND = "\', \'";
	private static final String BUTTON_BODY = "\', \'post\', \'/notice/modify\');\">Edit</button><button class=\"co-btn co-btn-delete\" onClick=\"javascript:delectNoticeConfirm(\'";
	private static final String BUTTON_TAIL = "\');\">Delete</button>";
	
	public NoticeViewResponse() {}
	private NoticeViewResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String number		= post.get("num");
		if (StringUtil.isNullOrEmpty(number)) {
			return sendRedirect("/notice");
		}

		int num				= Integer.parseInt(number);
		int type			= Integer.parseInt(post.get("type"));
		NoticeDAO notice	= NoticeDAO.getInstance();
		notice.readCountAdd(num, type);
		NoticeVO cur		= NoticeDAO.getNotice(num, type);
		NoticeVO pre		= NoticeDAO.getNotice(num-1, type);
		NoticeVO next		= NoticeDAO.getNotice(num+1, type);
		String preString	= StringUtil.EmptyString;
		String nextString	= StringUtil.EmptyString;
		if (pre != null) {
			preString = PREV_HEAD + type + BOTTOM_BODY_1 + pre.getRownum() + BOTTOM_BODY_2 + pre.getTitle() + BOTTOM_TAIL;
		}
		if (next != null) {
			nextString = NEXT_HEAD + type + BOTTOM_BODY_1 + next.getRownum() + BOTTOM_BODY_2 + next.getTitle() + BOTTOM_TAIL;
		}
		String utilString	= StringUtil.EmptyString;
		if (account != null && account.isGm()) {
			utilString = BUTTON_HEAD + type + BUTTON_BODY_AND + cur.getRownum() + BUTTON_BODY + cur.getRownum() + BUTTON_BODY_AND + type + BUTTON_TAIL;
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
		params.add(PAGE_CNB_TYPE_PAIR);
		params.add(new KeyValuePair<String, String>(PAGE_CNB_SUB_TYPE_KEY,	String.valueOf(type+1)));
		
		params.add(new KeyValuePair<String, String>("{TITLE}",				cur.getTitle()));
		params.add(new KeyValuePair<String, String>("{DATE}",				cur.getDate().toString()));
		params.add(new KeyValuePair<String, String>("{READ_COUNT}",			String.valueOf(cur.getReadcount())));
		params.add(new KeyValuePair<String, String>("{CONTENT}",			cur.getContent()));
		params.add(new KeyValuePair<String, String>("{PRE}",				preString));
		params.add(new KeyValuePair<String, String>("{NEXT}",				nextString));
		params.add(new KeyValuePair<String, String>("{UTIL_BUTTON}",		utilString));
		params.add(new KeyValuePair<String, String>("{NOTICE_TYPE}",		String.valueOf(type)));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new NoticeViewResponse(request, model);
	}
}


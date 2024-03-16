package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.notice.NoticeDAO;
import l1j.server.web.dispatcher.response.notice.NoticeVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class NoticeResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	
	private static final String WRITE_BUTTON = "<button class=\"co-btn co-btn-round btn-write\" aria-label=\"Write\" onClick=\"javascript:location.href='/notice/write';\"><svg class=\"fe-svg fe-svg-write\"><use xlink:href=\"#fe-svg-write\"></use></svg><span>Write</span></button>";
	
	public NoticeResponse() {}
	private NoticeResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		int notice_type	= 0;
		String noticeTypeParam = request.get_post_datas().get("type");
		if (!StringUtil.isNullOrEmpty(noticeTypeParam)) {
			notice_type = Integer.parseInt(noticeTypeParam);
		}
		
		int totsize = 0;
		switch(notice_type) {
		case 1:totsize	= NoticeDAO._update.size();break;
		case 2:totsize	= NoticeDAO._event.size();break;
		default:totsize	= NoticeDAO._notice.size();break;
		}
		List<NoticeVO> topList = NoticeDAO.getTopList(notice_type);
		if (topList != null) {
			totsize -= topList.size();
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
		params.add(new KeyValuePair<String, String>(PAGE_CNB_SUB_TYPE_KEY,	String.valueOf(notice_type+1)));
		
		params.add(new KeyValuePair<String, String>("{NOTICE_TYPE}",		String.valueOf(notice_type)));
		params.add(new KeyValuePair<String, String>("{TOTAL_SIZE}",			String.valueOf(totsize)));
		params.add(new KeyValuePair<String, String>("{WRITE_BUTTON}",		account != null && account.isGm() ? WRITE_BUTTON : StringUtil.EmptyString));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new NoticeResponse(request, model);
	}
}


package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.board.BoardVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

import org.apache.commons.lang.StringEscapeUtils;

public class BoardModifyResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	public BoardModifyResponse() {}
	private BoardModifyResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (player == null) {
			return sendRedirect(request.isIngame() ? 
					String.format("/login_ingame?urlType=%s", request.get_request_uri()) 
					: String.format("/login?urlType=%s", request.get_request_uri()));
		}
		
		String number		= request.read_post("num");
		if (StringUtil.isNullOrEmpty(number)) {
			return sendRedirect("/board");
		}
		int num				= Integer.parseInt(number);
		BoardVO cur			= BoardDAO.getBoard(num);
		
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(SUGGEST_ENABLE_PAIR);
		params.add(get_cnb_pair());
		params.add(get_user_data_pair());
		params.add(get_now_pair());
		if (PAGE_CNB_TYPE_PAIR == null) {
			PAGE_CNB_TYPE_PAIR = new KeyValuePair<String, String>(PAGE_CNB_TYPE_KEY,			dispatcher.getCnbType());
		}
		if (PAGE_CNB_SUB_TYPE_PAIR == null) {
			PAGE_CNB_SUB_TYPE_PAIR = new KeyValuePair<String, String>(PAGE_CNB_SUB_TYPE_KEY,	dispatcher.getCnbSubType());
		}
		params.add(PAGE_CNB_TYPE_PAIR);
		params.add(PAGE_CNB_SUB_TYPE_PAIR);
		
		params.add(new KeyValuePair<String, String>("{TITLE}",				cur.getTitle()));
		params.add(new KeyValuePair<String, String>("{CONTENT}",			cur.getContent()));
		params.add(new KeyValuePair<String, String>("{ESCAPE_CONTENT}",		StringEscapeUtils.escapeHtml(cur.getContent())));
		params.add(new KeyValuePair<String, String>("{ROWNUM}",				String.valueOf(cur.getRownum())));
		params.add(new KeyValuePair<String, String>("{MAIN_IMG}",			!StringUtil.isNullOrEmpty(cur.getMainImg()) ? cur.getMainImg() : StringUtil.EmptyString));
		
		// parameter define
		String html = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, html);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new BoardModifyResponse(request, model);
	}

}


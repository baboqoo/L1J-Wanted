package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.construct.L1CharacterInfo.L1Class;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountDAO;
import l1j.server.web.dispatcher.response.board.BoardCommentVO;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.board.BoardVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class BoardViewResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	//private static final String PREV_HEAD = "<p class=\"prev\"><span>이전글</span><a href=\"javascript:urlform(\'";
	//private static final String NEXT_HEAD = "<p class=\"next\"><span>다음글</span><a href=\"javascript:urlform(\'";
	private static final String PREV_HEAD = "<p class=\"prev\"><span>Previous Post</span><a href=\"javascript:urlform(\'";
	private static final String NEXT_HEAD = "<p class=\"next\"><span>Next Post</span><a href=\"javascript:urlform(\'";	
	private static final String BOTTOM_BODY = "\', \'post\', \'/board/view\');\">";
	private static final String BOTTOM_TAIL = "</a></p>";
	
	private static final String BUTTON_MODIFY_HEAD = "<button class=\"co-btn co-btn-modify\" onClick=\"javascript:urlform(\'";
	private static final String BUTTON_MODIFY_TAIL = "\', \'post\', \'/board/modify\');\">Edit</button>";
	private static final String BUTTON_DELETE_HEAD = "<button class=\"co-btn co-btn-delete\" onClick=\"javascript:delectConfirm(\'";
	private static final String BUTTON_DELETE_TAIL = "\');\">Delete</button>";
	
	private static final String COMMENT_HEAD = "<div class=\"comment-form comment-form-contentWrite ";
	private static final String COMMENT_TAIL = "\"\">";
	private static final String COMMENT_TEXTAREA_HEAD = "<div class=\"comment-form-textarea\"><textarea class=\"contentWrite\" name=\"content\" placeholder=\"";
	private static final String COMMENT_TEXTAREA_TAIL = "\" onkeyup=\"commentLengthCheck(this);\"></textarea></div>";
	private static final String COMMENT_TOOLBAR_HEAD = "<div class=\"comment-toolbar\" ";
	private static final String COMMENT_TOOLBAR_TAIL = "\"><div class=\"right\"><span class=\"count-word\"><em>0</em>/300</span><button type=\"button\" class=\"co-btn btn-confirm btn-contentWrite\" aria-label=\"Confirm\" style=\"color: #c69c7c;\">Confirm</button></div></div></div>";
	
	//private static final String[] COMMENT_DEFAULTS = { "댓글을 입력하세요.", "로그인 후 댓글을 입력하실 수 있습니다." };
	private static final String[] COMMENT_DEFAULTS = { "Please enter your comment.", "You can leave a comment after logging in." };
	private static final String COMMENT_STYLE_NONE = "style=\"display:none;\"";
	
	private static final String COMMENT_BODY_1 = "<div class=\"comment-article\" data-commentid=\"";
	private static final String COMMENT_BODY_2 = "\"><div class=\"comment-info\"><span class=\"thumb\"><img src=\"";
	private static final String COMMENT_BODY_3 = "\" alt=\"\"></span><span class=\"writer\">";
	private static final String COMMENT_BODY_4 = "<span class=\"server\">";
	private static final String COMMENT_BODY_5 = "</span></span><span class=\"date\">";
	private static final String COMMENT_BODY_6 = "</span><button class=\"co-btn btn-declare\" data-commentid=\"";
	private static final String COMMENT_BODY_7 = "\" data-boardid=\"";
	private static final String COMMENT_BODY_8 = "\" data-boardnum=\"";
	private static final String COMMENT_BODY_9 = "\" data-name=\"";
	//private static final String COMMENT_BODY_10 = "\" aria-label=\"신고\" onClick=\"commentReportBtn(this);\">&nbsp;신고</button>";
	//private static final String COMMENT_BODY_11 = "&nbsp;<button class=\"co-btn btn-delete\" aria-label=\"삭제\" onClick=\"deleteComment(this, ";
	//private static final String COMMENT_BODY_12 = ");\">&nbsp;삭제</button>";
	private static final String COMMENT_BODY_10 = "\" aria-label=\"Report\" onClick=\"commentReportBtn(this);\">&nbsp;Report</button>";
	private static final String COMMENT_BODY_11 = "&nbsp;<button class=\"co-btn btn-delete\" aria-label=\"Delete\" onClick=\"deleteComment(this, ";
	private static final String COMMENT_BODY_12 = ");\">&nbsp;Delete</button>";	
	private static final String COMMENT_BODY_13 = "</div><div class=\"comment-contents\">";
	private static final String COMMENT_BODY_14 = "</div><div class=\"comment-utils\"><button data-commentid=\"";
	private static final String COMMENT_BODY_16 = "\" data-boardnum=\"";
	//private static final String COMMENT_BODY_17 = "\" class=\"co-btn co-btn-like\" aria-label=\"좋아요수\" onClick=\"commentLikeBtn(this);\"><svg class=\"fe-svg fe-svg-like_s\" style=\"width: 16px; height: 16px; vertical-align: middle; fill: rgba(0,0,0,.45)!important; color: rgba(0,0,0,.45)!important;\"><use xlink:href=\"#fe-svg-like_s\"></use></svg>&nbsp;<em class=\"text\">";
	private static final String COMMENT_BODY_17 = "\" class=\"co-btn co-btn-like\" aria-label=\"Like\" onClick=\"commentLikeBtn(this);\"><svg class=\"fe-svg fe-svg-like_s\" style=\"width: 16px; height: 16px; vertical-align: middle; fill: rgba(0,0,0,.45)!important; color: rgba(0,0,0,.45)!important;\"><use xlink:href=\"#fe-svg-like_s\"></use></svg>&nbsp;<em class=\"text\">";
	private static final String COMMENT_BODY_18 = "</em></button></div></div>";
	
	private static final String ACTIVE_CLASS = "is-active";
	
	public BoardViewResponse() {}
	private BoardViewResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String num			= request.read_post("num");
		if (StringUtil.isNullOrEmpty(num)) {
			return sendRedirect("/board");
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
		
		int boardNum	= Integer.parseInt(num);
		BoardDAO dao	= BoardDAO.getInstance();
		dao.readCountAdd(boardNum);
		BoardVO cur		= BoardDAO.getBoard(boardNum);
		BoardVO pre		= BoardDAO.getBoard(boardNum - 1);
		BoardVO next	= BoardDAO.getBoard(boardNum + 1);
		
		String preString	= StringUtil.EmptyString;
		String nextString	= StringUtil.EmptyString;
		StringBuilder sb = new StringBuilder();
		
		if (pre != null) {
			sb.append(PREV_HEAD).append(pre.getRownum()).append(BOTTOM_BODY).append(pre.getTitle()).append(BOTTOM_TAIL);
			preString = sb.toString();
			sb.setLength(0);
		}
		if (next != null) {
			sb.append(NEXT_HEAD).append(next.getRownum()).append(BOTTOM_BODY).append(next.getTitle()).append(BOTTOM_TAIL);
			nextString = sb.toString();
			sb.setLength(0);
		}
		String utilString	= StringUtil.EmptyString;
		if (account != null && (account.isGm() || (player != null && player.getName().equals(cur.getName())))) {
			sb.append(BUTTON_MODIFY_HEAD).append(cur.getRownum()).append(BUTTON_MODIFY_TAIL);
			sb.append(BUTTON_DELETE_HEAD).append(cur.getRownum()).append(BUTTON_DELETE_TAIL);
			utilString = sb.toString();
			sb.setLength(0);
		}
		String likeStyle	= StringUtil.EmptyString;
		if (account != null && player != null && cur.getLikenames() != null && cur.getLikenames().contains(player.getName())) {
			likeStyle		= ACTIVE_CLASS;
		}
		
		params.add(new KeyValuePair<String, String>("{TITLE}",				cur.getTitle()));
		params.add(new KeyValuePair<String, String>("{DATE}",				cur.getDate().toString()));
		params.add(new KeyValuePair<String, String>("{READ_COUNT}",			String.valueOf(cur.getReadcount())));
		params.add(new KeyValuePair<String, String>("{CONTENT}",			cur.getContent()));
		params.add(new KeyValuePair<String, String>("{ID}",					String.valueOf(cur.getId())));
		params.add(new KeyValuePair<String, String>("{ROWNUM}",				String.valueOf(cur.getRownum())));
		params.add(new KeyValuePair<String, String>("{WRITER_NAME}",		cur.getName()));
		params.add(new KeyValuePair<String, String>("{LIKE_STYLE}",			likeStyle));
		params.add(new KeyValuePair<String, String>("{LIKE_COUNT}",			cur.getLikenames() == null ? StringUtil.ZeroString : String.valueOf(cur.getLikenames().size())));
		params.add(new KeyValuePair<String, String>("{WRITER_POROFILE}",	AccountDAO.getCharacterImg(cur.getChatype(), cur.getChaGender())));
		params.add(new KeyValuePair<String, String>("{CLASS_NAME}",			L1Class.getNameKr(cur.getChatype())));
		params.add(new KeyValuePair<String, String>("{PRE}",				preString));
		params.add(new KeyValuePair<String, String>("{NEXT}",				nextString));
		params.add(new KeyValuePair<String, String>("{UTIL_BUTTON}",		utilString));
		params.add(new KeyValuePair<String, String>("{ANSWER_SIZE}",		cur.getAnswerList() == null ? StringUtil.ZeroString : String.valueOf(cur.getAnswerList().size())));
		
		sb.append(COMMENT_HEAD).append(account != null ? ACTIVE_CLASS : StringUtil.EmptyString).append(COMMENT_TAIL);
		sb.append(COMMENT_TEXTAREA_HEAD).append(account != null ? COMMENT_DEFAULTS[0] : COMMENT_DEFAULTS[1]).append(COMMENT_TEXTAREA_TAIL);
		sb.append(COMMENT_TOOLBAR_HEAD).append(account == null ? COMMENT_STYLE_NONE : StringUtil.EmptyString).append(COMMENT_TOOLBAR_TAIL);
		params.add(new KeyValuePair<String, String>("{COMMENT_WRITE_AREA}",	sb.toString()));
		sb.setLength(0);
		
		if (cur.getAnswerList() != null) {
			for (BoardCommentVO comment : cur.getAnswerList()) {
				sb.append(COMMENT_BODY_1);
				sb.append(comment.getId());
				sb.append(COMMENT_BODY_2);
				sb.append(AccountDAO.getCharacterImg(comment.getChatype(), comment.getChaGender()));
				sb.append(COMMENT_BODY_3);
				sb.append(comment.getName());
				sb.append(COMMENT_BODY_4);
				sb.append(Config.WEB.WEB_SERVER_NAME);
				sb.append(COMMENT_BODY_5);
				sb.append(comment.getDate().toString());
				sb.append(COMMENT_BODY_6);
				sb.append(comment.getId());
				sb.append(COMMENT_BODY_7);
				sb.append(cur.getId());
				sb.append(COMMENT_BODY_8);
				sb.append(cur.getRownum());
				sb.append(COMMENT_BODY_9);
				sb.append(comment.getName());
				sb.append(COMMENT_BODY_10);
				if (account != null && (account.isGm() || (player != null && player.getName().equals(comment.getName())))) {
					sb.append(COMMENT_BODY_11);
					sb.append(comment.getId());
					sb.append(COMMENT_BODY_12);
				}
				sb.append(COMMENT_BODY_13);
				sb.append(comment.getContent());
				sb.append(COMMENT_BODY_14);
				sb.append(comment.getId());
				sb.append(COMMENT_BODY_16);
				sb.append(cur.getRownum());
				sb.append(COMMENT_BODY_17);
				sb.append(comment.getLikenames() == null ? StringUtil.ZeroString : comment.getLikenames().size());
				sb.append(COMMENT_BODY_18);
			}
		}
		params.add(new KeyValuePair<String, String>("{COMMENT_LIST}",		sb.toString()));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new BoardViewResponse(request, model);
	}

}


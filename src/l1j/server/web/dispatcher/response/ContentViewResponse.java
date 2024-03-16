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
import l1j.server.web.dispatcher.response.content.ContentCommentVO;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.dispatcher.response.content.ContentVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class ContentViewResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	public ContentViewResponse() {}
	private ContentViewResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String num			= request.read_post("num");
		if (StringUtil.isNullOrEmpty(num)) {
			return sendRedirect("/content");
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
		ContentDAO dao	= ContentDAO.getInstance();
		dao.readCountAdd(boardNum);
		ContentVO cur	= ContentDAO.getBoard(boardNum);
		ContentVO pre	= ContentDAO.getBoard(boardNum - 1);
		ContentVO next	= ContentDAO.getBoard(boardNum + 1);
		StringBuilder sb = new StringBuilder();
		if (pre != null) {
			//sb.append("<p class=\"prev\"><span>이전글</span><a href=\"javascript:urlform(\'").append(pre.getRownum()).append("\', \'post\', \'/contents/view\');\">").append(pre.getTitle()).append("</a></p>");
			sb.append("<p class=\"prev\"><span>Previous Article</span><a href=\"javascript:urlform(\'").append(pre.getRownum()).append("\', \'post\', \'/contents/view\');\">").append(pre.getTitle()).append("</a></p>");
		}
		params.add(new KeyValuePair<String, String>("{PRE}",				sb.toString()));
		sb.setLength(0);
		
		if (next != null) {
			//sb.append("<p class=\"next\"><span>다음글</span><a href=\"javascript:urlform(\'").append(next.getRownum()).append("\', \'post\', \'/contents/view\');\">").append(next.getTitle()).append("</a></p>");
			sb.append("<p class=\"next\"><span>Next Article</span><a href=\"javascript:urlform(\'").append(next.getRownum()).append("\', \'post\', \'/contents/view\');\">").append(next.getTitle()).append("</a></p>");
		}
		params.add(new KeyValuePair<String, String>("{NEXT}",				sb.toString()));
		sb.setLength(0);
		
		if (account != null && (account.isGm() || (player != null && player.getName().equals(cur.getName())))) {
			sb.append("<button class=\"co-btn co-btn-modify\" onClick=\"javascript:urlform(\'").append(cur.getRownum()).append("\', \'post\', \'/contents/modify\');\">Edit</button>");
			sb.append("<button class=\"co-btn co-btn-delete\" onClick=\"javascript:delectConfirm(\'").append(cur.getRownum()).append("\');\">Delete</button>");
		}
		params.add(new KeyValuePair<String, String>("{UTIL_BUTTON}",		sb.toString()));
		sb.setLength(0);
		
		if (cur.isTop()) {
			sb.append("<span class=\"writer\"><img src=\"/img/lineage_writer.png\" alt=\"\"></span>");
			sb.append("<span class=\"date\">").append(cur.getDate().toString()).append("</span>");
		} else {
			sb.append("<span class=\"writer\">").append(cur.getName()).append("<span class=\"server\">").append(Config.WEB.WEB_SERVER_NAME).append("</span></span>");
			sb.append("<span class=\"date\">").append(cur.getDate().toString()).append("</span>");
			sb.append("<span class=\"hit\">Views<em>").append(cur.getReadcount()).append("</em></span>");
		}
		params.add(new KeyValuePair<String, String>("{WRITER_INFO}",		sb.toString()));
		sb.setLength(0);
		
		if (!cur.isTop()) {
			sb.append("<div class=\"view-signature\">");
			sb.append("<div class=\"thumb\"><img src=\"").append(AccountDAO.getCharacterImg(cur.getChatype(), cur.getChaGender())).append("\" alt=\"\"></div>");
			sb.append("<div class=\"profile-info\"><div class=\"writer\">").append(cur.getName()).append("</div>");
			sb.append("<div class=\"info\"><span class=\"classname\">").append(L1Class.getNameKr(cur.getChatype())).append("</span><span class=\"servername\">").append(Config.WEB.WEB_SERVER_NAME).append("</span></div></div>");
			sb.append("</div>");
		}
		params.add(new KeyValuePair<String, String>("{SIGNATURE}",			sb.toString()));
		sb.setLength(0);
		
		if (!cur.isTop()) {
			String likeStyle	= StringUtil.EmptyString;
			if (player != null && cur.getLikenames() != null && cur.getLikenames().contains(player.getName())) {
				likeStyle		= "is-active";
			}
			sb.append("<div class=\"view-utils\" style=\"margin-bottom: 92px;\">");
			//sb.append("<button class=\"co-btn co-btn-like ").append(likeStyle).append("\" aria-label=\"추천\" data-id=\"").append(cur.getRownum()).append("\" onClick=\"likeBtn(this);\">");
			sb.append("<button class=\"co-btn co-btn-like ").append(likeStyle).append("\" aria-label=\"Like\" data-id=\"").append(cur.getRownum()).append("\" onClick=\"likeBtn(this);\">");
			//sb.append("<svg class=\"fe-svg fe-svg-like\"><use xlink:href=\"#fe-svg-like\"></use></svg><svg class=\"fe-svg fe-svg-like_fill\"><use xlink:href=\"#fe-svg-like_fill\"></use></svg><span>추천</span>");
			sb.append("<svg class=\"fe-svg fe-svg-like\"><use xlink:href=\"#fe-svg-like\"></use></svg><svg class=\"fe-svg fe-svg-like_fill\"><use xlink:href=\"#fe-svg-like_fill\"></use></svg><span>Like</span>");
			sb.append("<em>").append(cur.getLikenames() == null ? 0 : cur.getLikenames().size()).append("</em>");
			sb.append("</button></div>");
		}
		params.add(new KeyValuePair<String, String>("{LIKES}",				sb.toString()));
		sb.setLength(0);
		
		params.add(new KeyValuePair<String, String>("{TITLE}",				cur.getTitle()));
		params.add(new KeyValuePair<String, String>("{CONTENT}",			cur.getContent()));
		params.add(new KeyValuePair<String, String>("{ID}",					String.valueOf(cur.getId())));
		params.add(new KeyValuePair<String, String>("{ROWNUM}",				String.valueOf(cur.getRownum())));
		params.add(new KeyValuePair<String, String>("{ANSWER_SIZE}",		cur.getAnswerList() == null ? "0" : String.valueOf(cur.getAnswerList().size())));
		params.add(new KeyValuePair<String, String>("{WRITER_NAME}",		cur.getName()));
		
		sb.append("<div class=\"comment-form comment-form-contentWrite ").append(account != null ? "is-active" : "").append("\"\">");
		//sb.append("<div class=\"comment-form-textarea\"><textarea class=\"contentWrite\" name=\"content\" placeholder=\"").append(account != null ? "댓글을 입력하세요." : "로그인 후 댓글을 입력하실 수 있습니다.").append("\" onkeyup=\"commentLengthCheck(this);\"></textarea></div>");
		//sb.append("<div class=\"comment-toolbar\" ").append(account == null ? "style=\"display:none;\"" : "").append("\"><div class=\"right\"><span class=\"count-word\"><em>0</em>/300</span><button type=\"button\" class=\"co-btn btn-confirm btn-contentWrite\" aria-label=\"등록\" style=\"color: #c69c7c;\">등록</button></div></div>");
		sb.append("<div class=\"comment-form-textarea\"><textarea class=\"contentWrite\" name=\"content\" placeholder=\"").append(account != null ? "Enter your comment." : "You can leave a comment after logging in.").append("\" onkeyup=\"commentLengthCheck(this);\"></textarea></div>");
		sb.append("<div class=\"comment-toolbar\" ").append(account == null ? "style=\"display:none;\"" : "").append("><div class=\"right\"><span class=\"count-word\"><em>0</em>/300</span><button type=\"button\" class=\"co-btn btn-confirm btn-contentWrite\" aria-label=\"Submit\" style=\"color: #c69c7c;\">Submit</button></div></div>");		
		sb.append("</div>");
		params.add(new KeyValuePair<String, String>("{COMMENT_WRITE_AREA}",	sb.toString()));
		
		sb.setLength(0);
		if (cur.getAnswerList() != null) {
			for (ContentCommentVO comment : cur.getAnswerList()) {
				sb.append("<div class=\"comment-article\" data-commentid=\"").append(comment.getId()).append("\">");
				sb.append("<div class=\"comment-info\">");
				sb.append("<span class=\"thumb\"><img src=\"").append(AccountDAO.getCharacterImg(comment.getChatype(), comment.getChaGender())).append("\" alt=\"\"></span>");
				sb.append("<span class=\"writer\">").append(comment.getName()).append("<span class=\"server\">").append(Config.WEB.WEB_SERVER_NAME).append("</span></span>");
				sb.append("<span class=\"date\">").append(comment.getDate().toString()).append("</span>");
				//sb.append("<button class=\"co-btn btn-declare\" data-commentid=\"").append(comment.getId()).append("\" data-boardid=\"").append(cur.getId()).append("\" data-boardnum=\"").append(cur.getRownum()).append("\" data-name=\"").append(comment.getName()).append("\" aria-label=\"신고\" onClick=\"commentReportBtn(this);\">&nbsp;신고</button>");
				sb.append("<button class=\"co-btn btn-declare\" data-commentid=\"").append(comment.getId()).append("\" data-boardid=\"").append(cur.getId()).append("\" data-boardnum=\"").append(cur.getRownum()).append("\" data-name=\"").append(comment.getName()).append("\" aria-label=\"Report\" onClick=\"commentReportBtn(this);\">&nbsp;Report</button>");
				if (account != null && (account.isGm() || (player != null && player.getName().equals(comment.getName())))) {
					//sb.append("&nbsp;<button class=\"co-btn btn-delete\" aria-label=\"삭제\" onClick=\"deleteComment(this, ").append(comment.getId()).append(");\">&nbsp;삭제</button>");
					sb.append("&nbsp;<button class=\"co-btn btn-delete\" aria-label=\"Delete\" onClick=\"deleteComment(this, ").append(comment.getId()).append(");\">&nbsp;Delete</button>");
				}
				sb.append("</div>");
				sb.append("<div class=\"comment-contents\">").append(comment.getContent()).append("</div>");
				sb.append("<div class=\"comment-utils\">");
				//sb.append("<button data-commentid=\"").append(comment.getId()).append("\" data-boardnum=\"").append(cur.getRownum()).append("\" class=\"co-btn co-btn-like\" aria-label=\"좋아요수\" onClick=\"commentLikeBtn(this);\">");
				sb.append("<button data-commentid=\"").append(comment.getId()).append("\" data-boardnum=\"").append(cur.getRownum()).append("\" class=\"co-btn co-btn-like\" aria-label=\"Like\" onClick=\"commentLikeBtn(this);\">");
				sb.append("<svg class=\"fe-svg fe-svg-like_s\" style=\"width: 16px; height: 16px; vertical-align: middle; fill: rgba(0,0,0,.45)!important; color: rgba(0,0,0,.45)!important;\"><use xlink:href=\"#fe-svg-like_s\"></use></svg>");
				sb.append("&nbsp;<em class=\"text\">").append(comment.getLikenames() == null ? "0" : comment.getLikenames().size()).append("</em>");
				sb.append("</button></div></div>");
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
		return new ContentViewResponse(request, model);
	}

}


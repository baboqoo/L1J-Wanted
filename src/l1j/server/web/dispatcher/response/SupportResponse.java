package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.support.SupportDAO;
import l1j.server.web.dispatcher.response.support.SupportMessageType;
import l1j.server.web.dispatcher.response.support.SupportMessageVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class SupportResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	private static String NOT_PROFIT_HTML;
	private static KeyValuePair<String, String> NOT_PROFIT_HELLOW_MENT;
	
	private static String supportAgreeMsg;
	private static String supportPassMsg;
	
	private static KeyValuePair<String, String> SUPPORT_AGREE_PAIR;
	private static KeyValuePair<String, String> SUPPORT_PASS_PAIR;
	
	private static final String SUPPORT_AGREE_HEAD		= "<span class=\"account-binding\"><strong class=\"ng-binding\">";
	private static final String SUPPORT_AGREE_BODY_1	= "</strong>, hello</span>";
	private static final String SUPPORT_AGREE_BODY_2	= "<span class=\"agree_txt\">&nbsp;[<i class=\"xi-shield-checked-o\"></i>&nbsp;Agree to the terms and conditions]</span>";
	private static final String SUPPORT_AGREE_DEFAULT	= "<span>Welcome to <strong class=\"ng-binding\">" + Config.WEB.WEB_SERVER_NAME + "</strong></span>";
	
	public SupportResponse() {}
	private SupportResponse(HttpRequestModel request, DispatcherModel model) {
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
		if (PAGE_CNB_TYPE_PAIR == null) {
			PAGE_CNB_TYPE_PAIR		= new KeyValuePair<String, String>(PAGE_CNB_TYPE_KEY,		dispatcher.getCnbType());
		}
		if (PAGE_CNB_SUB_TYPE_PAIR == null) {
			PAGE_CNB_SUB_TYPE_PAIR	= new KeyValuePair<String, String>(PAGE_CNB_SUB_TYPE_KEY,	dispatcher.getCnbSubType());
		}
		params.add(PAGE_CNB_TYPE_PAIR);
		params.add(PAGE_CNB_SUB_TYPE_PAIR);
		
		// 비영리 설정 시 처리될 html
		if (!Config.SERVER.PROFIT_SERVER_ACTIVE) {
			if (NOT_PROFIT_HTML == null) {
				NOT_PROFIT_HTML = load_file_string("./appcenter/customer/support_not_profit.html");
			}
			if (NOT_PROFIT_HELLOW_MENT == null) {
				NOT_PROFIT_HELLOW_MENT = new KeyValuePair<String, String>("{HELLOW_MENT}",		SUPPORT_AGREE_DEFAULT);
			}
			params.add(NOT_PROFIT_HELLOW_MENT);
			// parameter define
			String document = StringUtil.replace(NOT_PROFIT_HTML, params);
			params.clear();
			params = null;
			return create_response_html(HttpResponseStatus.OK, document);
		}
		
		StringBuilder sb = new StringBuilder();
		if (account != null) {
			sb.append(SUPPORT_AGREE_HEAD).append(account.getName()).append(SUPPORT_AGREE_BODY_1);
			if (account.isTermsAgree()) {
				sb.append(SUPPORT_AGREE_BODY_2);
			}
		} else {
			sb.append(SUPPORT_AGREE_DEFAULT);
		}
		params.add(new KeyValuePair<String, String>("{HELLOW_MENT}",		sb.toString()));
		params.add(getContent(account != null && account.isTermsAgree()));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	public static KeyValuePair<String, String> getContent(boolean agree) {
		if (!agree) {
			if (SUPPORT_AGREE_PAIR == null) {
				SUPPORT_AGREE_PAIR = new KeyValuePair<String, String>("{SUPPORT_CONTENT}",	getMsg(agree));
			}
			return SUPPORT_AGREE_PAIR;
		}
		if (SUPPORT_PASS_PAIR == null) {
			SUPPORT_PASS_PAIR = new KeyValuePair<String, String>("{SUPPORT_CONTENT}",	getMsg(agree));
		}
		return SUPPORT_PASS_PAIR;
	}
	
	public static String getMsg(boolean agree) {
		if (!agree) {
			if (supportAgreeMsg == null) {
				StringBuilder sb = new StringBuilder();
				sb.append("<div id=\"agree_wrap\">");
				sb.append("<strong class=\"support_agree_tit\">").append("</strong> Before proceeding with Sponsorship, we will guide you through a few precautions and agree to the terms and conditions."); // <strong>&nbsp;[ 최초 1회 진행 ]</strong>
				sb.append("<ul>");
				for (SupportMessageVO msg : SupportDAO.getMessage(SupportMessageType.AGREE)) {
					sb.append("<li>").append(msg.getIndexId()).append(". ").append(msg.getContent()).append("</li>");
				}
				sb.append("</ul>");
				sb.append("<div class=\"support_content\"><div class=\"support_l\"><strong><i class=\"xi-crown\">&nbsp;Sponsorship Reward</i></strong></div><div class=\"support_r\">");
				for (SupportMessageVO msg : SupportDAO.getMessage(SupportMessageType.REWARD)) {
					sb.append("<span class=\"reward_content\">").append(msg.getContent()).append("</span>");
				}
				sb.append("</div></div>");
				sb.append("<div class=\"agree_check_div\"><input type=\"checkbox\" id=\"agree_check\"/><strong id=\"agree_checkbox\">I have read and agree to all of the above.</strong></div>");
				sb.append("<div class=\"agree_btns\"><button onClick=\"agree_progres();\"><strong><i class=\"xi-check\"></i>&nbsp;Go Ahead</strong></button></div>");
				sb.append("</div>");
				supportAgreeMsg = sb.toString();
			}
			return supportAgreeMsg;
		} else {
			if (supportPassMsg == null) {
				StringBuilder sb = new StringBuilder();
				sb.append("<div id=\"support_wrap\">");
				sb.append("<strong class=\"point_color\">").append(Config.WEB.WEB_SERVER_NAME).append("</strong>");
				sb.append(Config.WEB.SUPPORT_BANK_SEND_TYPE ? " Sponsorship Account Information" : " Sponsorship Account Inquiry");
				sb.append("<strong class=\"sub_tit\">※ Please enter the name of the character from your account who will receive the rewards.</strong>");
				sb.append("<div id=\"support_content\">");
				
				if (Config.WEB.SUPPORT_BANK_SEND_TYPE) {
					sb.append("<table class=\"table_list ng-scope\">");
					sb.append("<tbody><tr><th class=\"th_part\">Sponsoring Bank</th><th class=\"th_subject\">Account Nº</th><th>Account Owner</th></tr></tbody>");
					sb.append("<tbody><tr><td>");
					sb.append(Config.WEB.SUPPORT_BANK);// 후원 은행
					sb.append("</td><td>");
					sb.append(Config.WEB.SUPPORT_BANK_NUMBER);// 후원 계좌번호
					sb.append("</td><td>");
					sb.append(Config.WEB.SUPPORT_BANK_NAME);// 후원 계좌주
					sb.append("</td></tr></tbody>");
					sb.append("</table>");
				} else {
					sb.append("<div class=\"support_bank_req\">");
					sb.append("<div class=\"support_bank_req_desc\"><span>If you continue, the full information will be sent to your character as soon as possible.</span></div>");
					sb.append("<div class=\"support_bank_req_btn\"><button onClick=\"javascript:support_bank_request();\"><i class=\"xi-cc-visa\"></i>Sponsored Account Request</button></div>");
					sb.append("</div>");
				}
				
				sb.append("<div class=\"support_content\"><div class=\"support_l\"><strong><i class=\"xi-crown\">&nbsp;Sponsorship Reward</i></strong></div><div class=\"support_r\">");
				for (SupportMessageVO msg : SupportDAO.getMessage(SupportMessageType.REWARD)) {
					sb.append("<span class=\"reward_content\">").append(msg.getContent()).append("</span>");
				}
				sb.append("</div></div>");
				sb.append("<div class=\"support_sub_txt\">");
				sb.append("<ul>");
				for (SupportMessageVO msg : SupportDAO.getMessage(SupportMessageType.PROGRESS)) {
					sb.append("<li>→ ").append(msg.getContent()).append("</li>");
				}
				sb.append("</ul></div>");
				sb.append("<div class=\"gm_send\"><strong>▶&nbsp;Deposit made. Send a Message with the ammount</strong></div>");
				sb.append("<div class=\"gm_send_div\">");
				sb.append("<input type=\"text\" id=\"support_complete_msg\" autocomplete=\"off\" placeholder=\"Deposit amount (required)\" rows=\"1\" maxlength=\"11\" onKeyup=\"inputCheckNumber(this)\">");
				sb.append("<div><button onClick=\"sendGmMessage();\"><i class=\"xi-send\">&nbsp;Send</i></button></div>");
				sb.append("</div></div></div>");
				supportPassMsg = sb.toString();
			}
			return supportPassMsg;
		}
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new SupportResponse(request, model);
	}
}


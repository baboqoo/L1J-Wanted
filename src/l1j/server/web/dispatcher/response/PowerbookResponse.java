package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.Config;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.keyword.KeywordLoader;
import l1j.server.web.dispatcher.response.powerbook.L1Info;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.dispatcher.response.powerbook.PowerbookGuideVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class PowerbookResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	private static KeyValuePair<String, String> GUIDE_LIST_SLICK_1_PAIR;
	private static KeyValuePair<String, String> GUIDE_LIST_SLICK_2_PAIR;
	private static KeyValuePair<String, String> GUIDE_LIST_SLICK_3_PAIR;
	
	//private static final String WRITE_BUTTON		= "<div class=\"powerbookBtn_area\"><a href=\"/powerbook/write\" class=\"powerbookBtn\">등록하기</a></div>";
	private static final String WRITE_BUTTON 		= "<div class=\"powerbookBtn_area\"><a href=\"/powerbook/write\" class=\"powerbookBtn\">Register</a></div>";
	private static final String RECOMMEND_HEAD		= "<div class=\"recommend__bundle slick-item\"><h3 class=\"recommend__bundle-tit\">";
	private static final String RECOMMEND_BODY		= "</h3><ul class=\"recommend__bundle-list\">";
	private static final String RECOMMEND_SLICK_1	= "<li class=\"recommend__item\"><a href=\"/powerbook/search?searchType=4&query=";
	private static final String RECOMMEND_SLICK_2	= "\"><div class=\"pic-box pic-box_theme_full\"><img class=\"pic-box__img\" src=\"";
	private static final String RECOMMEND_SLICK_3	= "\" alt=\"\"><span class=\"pic-box__text\">";
	private static final String RECOMMEND_SLICK_4	= "</span></div><span class=\"recommend__item-desc\">";
	private static final String RECOMMEND_SLICK_5	= "</span></a></li>";
	private static final String RECOMMEND_TAIL		= "</ul></div>";
	
	private static KeyValuePair<String, String> GUIDE_LIST_PAIR;
	
	private static KeyValuePair<String, String> GUIDE_BUTTON_GM_PAIR	= new KeyValuePair<String, String>("{GUIDE_BUTTON}",		WRITE_BUTTON);
	private static KeyValuePair<String, String> GUIDE_BUTTON_EMPTY_PAIR	= new KeyValuePair<String, String>("{GUIDE_BUTTON}",		StringUtil.EmptyString);
	
	public PowerbookResponse() {}
	private PowerbookResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		ArrayList<L1Info> infos	= null;
		L1Info info				= null;
		String query			= request.read_parameters_at_once("query");
		KeywordLoader.putKeyword(query);
		
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
		
		StringBuilder sb = new StringBuilder();
		if (GUIDE_LIST_SLICK_1_PAIR == null) {
			infos	= L1InfoDAO.getInfoGuideMainList();
			
			if (infos.size() >= 3) {
				sb.append(RECOMMEND_HEAD);
				sb.append(Config.WEB.WEB_SERVER_NAME);
				sb.append(RECOMMEND_BODY);
				for (int i=0; i<3; i++) {
					info = infos.get(i);
					sb.append(RECOMMEND_SLICK_1);
					sb.append(info.getName());
					sb.append(RECOMMEND_SLICK_2);
					sb.append(info.getInfo().get("mainImg"));
					sb.append(RECOMMEND_SLICK_3);
					sb.append(info.getName());
					sb.append(RECOMMEND_SLICK_4);
					sb.append(info.getName());
					sb.append(RECOMMEND_SLICK_5);
				}
				sb.append(RECOMMEND_TAIL);
			}
			GUIDE_LIST_SLICK_1_PAIR = new KeyValuePair<String, String>("{GUIDE_LIST_1}",		sb.toString());
			sb.setLength(0);
		}
		params.add(GUIDE_LIST_SLICK_1_PAIR);
		
		if (GUIDE_LIST_SLICK_2_PAIR == null) {
			if (infos.size() >= 6) {
				sb.append(RECOMMEND_HEAD);
				sb.append(Config.WEB.WEB_SERVER_NAME);
				sb.append(RECOMMEND_BODY);
				for (int i=3; i<6; i++) {
					info = infos.get(i);
					sb.append(RECOMMEND_SLICK_1);
					sb.append(info.getName());
					sb.append(RECOMMEND_SLICK_2);
					sb.append(info.getInfo().get("mainImg"));
					sb.append(RECOMMEND_SLICK_3);
					sb.append(info.getName());
					sb.append(RECOMMEND_SLICK_4);
					sb.append(info.getName());
					sb.append(RECOMMEND_SLICK_5);
				}
				sb.append(RECOMMEND_TAIL);
			}
			GUIDE_LIST_SLICK_2_PAIR = new KeyValuePair<String, String>("{GUIDE_LIST_2}",		sb.toString());
			sb.setLength(0);
		}
		params.add(GUIDE_LIST_SLICK_2_PAIR);
		
		if (GUIDE_LIST_SLICK_3_PAIR == null) {
			if (infos.size() >= 9) {
				sb.append(RECOMMEND_HEAD);
				sb.append(Config.WEB.WEB_SERVER_NAME);
				sb.append(RECOMMEND_BODY);
				for (int i=6; i<9; i++) {
					info = infos.get(i);
					sb.append(RECOMMEND_SLICK_1);
					sb.append(info.getName());
					sb.append(RECOMMEND_SLICK_2);
					sb.append(info.getInfo().get("mainImg"));
					sb.append(RECOMMEND_SLICK_3);
					sb.append(info.getName());
					sb.append(RECOMMEND_SLICK_4);
					sb.append(info.getName());
					sb.append(RECOMMEND_SLICK_5);
				}
				sb.append(RECOMMEND_TAIL);
			}
			GUIDE_LIST_SLICK_3_PAIR = new KeyValuePair<String, String>("{GUIDE_LIST_3}",		sb.toString());
			sb.setLength(0);
		}
		params.add(GUIDE_LIST_SLICK_3_PAIR);
		
		params.add(account != null && account.isGm() ? GUIDE_BUTTON_GM_PAIR : GUIDE_BUTTON_EMPTY_PAIR);
		
		if (GUIDE_LIST_PAIR == null) {
			sb.append("<div class=\"guide__list\">");
			for (Map.Entry<String, ArrayList<PowerbookGuideVO>> entry : L1InfoDAO.getGuide().entrySet()) {
				sb.append("<div class=\"guide__item\"><h3 class=\"guide__item-tit\"><a>");
				sb.append(entry.getKey());
				sb.append("</a><span class=\"icon\"></span></h3><ul class=\"guide__item-list\"><li class=\"title\"><a href=\"\">");
				sb.append(entry.getKey());
				sb.append("</a></li>");
				for (PowerbookGuideVO guide : entry.getValue()) {
					sb.append("<li><a href=\"");
					sb.append(guide.getUri());
					sb.append("\">");
					sb.append(guide.getTitle());
					if (guide.isNew()) {
						sb.append("&nbsp;<i class=\"icon-new\"></i>");
					}
					sb.append("</a></li>");
				}
				sb.append("</ul></div>");
			}
			sb.append("</div>");
			GUIDE_LIST_PAIR = new KeyValuePair<String, String>("{GUIDE_LIST}",	sb.toString());
			sb.setLength(0);
		}
		params.add(GUIDE_LIST_PAIR);
		
		params.add(new KeyValuePair<String, String>("{QUERY}",				query));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PowerbookResponse(request, model);
	}
}


package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.controller.action.UserRanking;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.CharacterVO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class RankResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	private static KeyValuePair<String, String> RANK_TOTAL_VALUE_PAIR;
	private static KeyValuePair<String, String> RANK_CLASS_VALUE_PAIR;
	
	public RankResponse() {}
	private RankResponse(HttpRequestModel request, DispatcherModel model) {
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
		
		String loadTime = UserRanking.getLoadTime();
		params.add(new KeyValuePair<String, String>("{LOAD_TIME}",			loadTime));
		
		if (RANK_TOTAL_VALUE_PAIR == null) {
			RANK_TOTAL_VALUE_PAIR = new KeyValuePair<String, String>("{RANK_TOTAL_CALC_RANGE}",	Integer.toString(Config.RANKING.RANKING_TOTAL_CALC_RANGE));
		}
		if (RANK_CLASS_VALUE_PAIR == null) {
			RANK_CLASS_VALUE_PAIR = new KeyValuePair<String, String>("{RANK_CLASS_CALC_RANGE}",	Integer.toString(Config.RANKING.RANKING_CLASS_CALC_RANGE));
		}
		params.add(RANK_TOTAL_VALUE_PAIR);
		params.add(RANK_CLASS_VALUE_PAIR);
		
		String myCharRank = StringUtil.EmptyString;
		if (account != null) {
			myCharRank = getMyCharRank(loadTime);
		}
		params.add(new KeyValuePair<String, String>("{MY_CHAR_RANK}",		myCharRank));
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	String getMyCharRank(String loadTime) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (CharacterVO cha : account.getCharList()) {
			if (cha.getLevel() < 80) {
				continue;
			}
			if (cha.getAllRank() == 0) {
				continue;
			}
			if (count == 0) {
				sb.append("<div class=\"wrap-detail-info\" style=\"display: block;\"><h2><strong>My character</strong><span class=\"date\">");
				sb.append(loadTime).append("</span></h2><div class=\"detail-info\">");
			}
			
			sb.append("<div class=\"detail-info-list\"\"><dl><dt class=\"thumb\"><img src=\"").append(cha.getProfileUrl());
			sb.append("\"></dt><dd><strong class=\"name\">").append(cha.getName());
			sb.append("</strong><span class=\"server\">").append(Config.WEB.WEB_SERVER_NAME);
			sb.append("</span><span class=\"level\">").append(cha.getLevel());
			sb.append("Lv.</span></dd></dl><div class=\"ranking-info\"><ul><li><strong>").append(cha.getAllRank());
			sb.append("</strong><span>Server Ranking</span></li><li><strong>").append(cha.getClassRank());
			sb.append("</strong><span>Class Ranking</span></li><li class=\"grade\"><span class=\"star ").append(getGrade(cha.getAllRank()));
			sb.append("\"></span><span>Rank</span></li></ul></div></div>");
			count++;
		}
		if (count > 0) {
			sb.append("</div><p class=\"notice\">* Ranking is only available for characters level 80 and above.</p></div>");
		}
		return sb.toString();
	}
	
	String getGrade(int rank) {
		if (rank >= 1 && rank <= 10) {
			return "grade4";
		}
   		if (rank >= 11 && rank <= 20) {
   			return "grade3";
   		}
   		if (rank >= 21 && rank <= 60) {
   			return "grade2";
   		}
   		if (rank >= 61 && rank <= 80) {
   			return "grade1";
   		}
   		return "grade0";
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new RankResponse(request, model);
	}
}


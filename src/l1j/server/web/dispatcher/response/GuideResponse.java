package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.LinkedList;

import l1j.server.server.datatables.BossSpawnTable;
import l1j.server.server.datatables.BossSpawnTable.BossTemp;
import l1j.server.server.datatables.BossSpawnTable.SpawnType;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.guide.GuideBossVO;
import l1j.server.web.dispatcher.response.guide.GuideDAO;
import l1j.server.web.dispatcher.response.guide.GuideRecommendVO;
import l1j.server.web.dispatcher.response.guide.GuideVO;
import l1j.server.web.dispatcher.response.item.ItemVO;
import l1j.server.web.dispatcher.response.powerbook.L1Info;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class GuideResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	//private static final String ADMIN_BUTTONS = "<div style=\"text-align: center;\"><button class=\"admin_btn\" onClick=\"javascript:location.href='/guide/write';\" style=\"float: none;\">추가하기</button></div>";
	private static final String ADMIN_BUTTONS = "<div style=\"text-align: center;\"><button class=\"admin_btn\" onClick=\"javascript:location.href='/guide/write';\" style=\"float: none;\">Add</button></div>";
	private static final String GUIDE_HEAD = "<header><h2>";
	private static final String GUIDE_TITLE = " </h2><i class=\"icon-helper on\"></i>";
	private static final String GUIDE_CONTENT = "<div class=\"wrap-helper\"><div class=\"guide_content\">";
	private static final String GUIDE_TAIL = "</div></div></header>";
	private static final String GUIDE_BUTTON_HEAD = "<button class=\"admin_btn\" onClick=\"javascript:location.href='/guide/modify?id=";
	//private static final String GUIDE_BUTTON_TAIL = "';\">관리</button>";
	private static final String GUIDE_BUTTON_TAIL = "';\">Manage</button>";
	
	private KeyValuePair<String, String> GUIDE_BOSS_INFO_PAIR;
	private static final String GUIDE_SLICK_ON_HEAD = "<div class=\"tab-item on\" data-id=\"";
	private static final String GUIDE_SLICK_HEAD = "<div class=\"tab-item\" data-id=\"";
	private static final String GUIDE_SLICK_BODY = "\"><a href=\"javascript:bossChage(";
	private static final String GUIDE_SLICK_BODY_TAIL = ");\">";
	private static final String GUIDE_SLICK_TAIL = "</a></div>";
	
	private KeyValuePair<String, String> GUIDE_BOSS_TIME_PAIR;
	
	private KeyValuePair<String, String> GUIDE_RECOMMEND_PAIR;
	private static final String GUIDE_RECOMMEND_CARD_1 = "<div class=\"recommend-card\"><a href=\"";
	private static final String GUIDE_RECOMMEND_CARD_2 = "\" class=\"self\"><dl><dd class=\"recommend-card__thumb\"><img src=\"";
	private static final String GUIDE_RECOMMEND_CARD_3 = "\"></dd><dt class=\"recommend-card__subject\">";
	private static final String GUIDE_RECOMMEND_CARD_4 = "</dt><dd class=\"recommend-card__desc\">";
	private static final String GUIDE_RECOMMEND_CARD_5 = "</dd></dl></a></div>";
	
	public GuideResponse() {}
	private GuideResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HttpResponse get_response() throws Exception {
		GuideDAO dao		= GuideDAO.getInstance();
		
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
		for (GuideVO guide : dao.getList()) {
			sb.append(GUIDE_HEAD).append(guide.getTitle()).append(GUIDE_TITLE);
			if (account != null && account.isGm()) {
				sb.append(GUIDE_BUTTON_HEAD).append(guide.getId()).append(GUIDE_BUTTON_TAIL);
			}
			sb.append(GUIDE_CONTENT).append(guide.getContent());
			sb.append(GUIDE_TAIL);
		}
		params.add(new KeyValuePair<String, String>("{GUIDE_LIST}",			sb.toString()));
		sb.setLength(0);
		
		if (account != null && account.isGm()) {
			sb.append(ADMIN_BUTTONS);
		}
		params.add(new KeyValuePair<String, String>("{GUIDE_BUTTON}",		sb.toString()));
		sb.setLength(0);
		
		if (GUIDE_BOSS_INFO_PAIR == null) {
			int first = 0;
			for (ArrayList<GuideBossVO> boss : dao.getBoss().values()) {
				if (first == 0) {
					sb.append(GUIDE_SLICK_ON_HEAD).append(boss.get(0).getLoc()).append(GUIDE_SLICK_BODY).append(boss.get(0).getLoc()).append(GUIDE_SLICK_BODY_TAIL).append(boss.get(0).getLocName()).append(GUIDE_SLICK_TAIL);
				} else {
					sb.append(GUIDE_SLICK_HEAD).append(boss.get(0).getLoc()).append(GUIDE_SLICK_BODY).append(boss.get(0).getLoc()).append(GUIDE_SLICK_BODY_TAIL).append(boss.get(0).getLocName()).append(GUIDE_SLICK_TAIL);
				}
				first++;
			}
			GUIDE_BOSS_INFO_PAIR = new KeyValuePair<String, String>("{GUIDE_BOSS}",			sb.toString());
			sb.setLength(0);
		}
		params.add(GUIDE_BOSS_INFO_PAIR);
		
		if (GUIDE_BOSS_TIME_PAIR == null) {
			LinkedList<BossTemp> boss_list = new LinkedList<BossTemp>();
			for (BossTemp boss : BossSpawnTable.getlist()) {
				if (!(boss.spawnType == SpawnType.NORMAL || boss.spawnType == SpawnType.DOMINATION_TOWER || boss.spawnType == SpawnType.DRAGON_RAID)) {
					continue;
				}
				
				boss_list.add(boss);
			}
			//sb.append("<table class=\"table table-size-").append(boss_list.size()).append(" on\"><thead><tr><th class=\"name thead\"><span>몬스터명</span></th>");
			sb.append("<table class=\"table table-size-").append(boss_list.size()).append(" on\"><thead><tr><th class=\"name thead\"><span>Monster Name</span></th>");
			for (BossTemp boss : boss_list) {
				switch (boss.spawnType) {
				case DRAGON_RAID:
					//sb.append("<th class=\"name\">드래곤 레이드</th>");
					sb.append("<th class=\"name\">Dragon Raid</th>");
					break;
				default:
					L1Npc temp = NpcTable.getInstance().getTemplate(boss.npcid);
					//sb.append("<th class=\"name\"><a href=\"/powerbook/search?searchType=2&query=").append(temp.getDescKr()).append("\">");
					//sb.append(temp.getDescKr());
					sb.append("<th class=\"name\"><a href=\"/powerbook/search?searchType=2&query=").append(temp.getDescEn()).append("\">");
					sb.append(temp.getDescEn());
					if (boss.spawnType == SpawnType.DOMINATION_TOWER) {
						//sb.append("(지배)");
						sb.append("(Domination Tower)");
					}
					sb.append("</a></th>");
					break;
				} 
			}
			//sb.append("</tr></thead><tbody><tr><td class=\"time thead\"><span>내용</span></td>");
			sb.append("</tr></thead><tbody><tr><td class=\"time thead\"><span>Content</span></td>");
			for (BossTemp boss : boss_list) {
				//sb.append("<td class=\"time\">- 요일 -</br>");
				sb.append("<td class=\"time\">- Day -</br>");
				if (boss.spawnDay.length == 7) {
					//sb.append("전체");
					sb.append("All");
				} else {
					int dayCnt = 0;
					for (int day : boss.spawnDay) {
						if (dayCnt++ > 0) {
							sb.append("</br>");
						}
						sb.append(CommonUtil.WEEK_DAY_ARRAY[day - 1]);
					}
				}
				//sb.append("</br></br>- 시간 -</br>");
				sb.append("</br></br>- Time -</br>");
				for (int i=0; i<boss.spawnHour.length; i++) {
					if (i > 0) {
						sb.append("</br>");
					}
					//sb.append(boss.spawnHour[i]).append("시 ").append(boss.spawnMinute[i]).append("분");
					sb.append(boss.spawnHour[i]).append(" o'clock ").append(boss.spawnMinute[i]).append(" minutes");
				}
				sb.append("</td>");
			}
			//sb.append("</tr><tr><td class=\"percent thead\"><span>특징</span></td>");
			sb.append("</tr><tr><td class=\"percent thead\"><span>Features</span></td>");
			for (BossTemp boss : boss_list) {
				sb.append("<td class=\"percent\">");
				int featureCnt = 0;
				if (boss.spawnType == SpawnType.DRAGON_RAID) {
					featureCnt++;
					//sb.append("4대용 랜덤 등장(전조현상)");
					sb.append("Random appearance of 4 substitutes (Premonition Phenomenon)");
				}
				if (boss.percent < 100) {
					featureCnt++;
					//sb.append("출현 확률 : ").append(boss.percent).append("%");
					sb.append("Appearance Probability: ").append(boss.percent).append("%");
				}
				if (boss.aliveSecond > 0) {
					if (featureCnt > 0) {
						sb.append("</br>");
					}
					featureCnt++;
					//sb.append("출현 후 삭제 시간 : ").append((int)(boss.aliveSecond / 60)).append("분");
					sb.append("Deletion Time After Appearance: ").append((int)(boss.aliveSecond / 60)).append(" minutes");
				}
				if (boss.rndMinut > 0) {
					if (featureCnt > 0) {
						sb.append("</br>");
					}
					//sb.append("시간 오차 : ").append(boss.rndMinut).append("분");
					sb.append("Time Deviation: ").append(boss.rndMinut).append(" minutes");
				}
				sb.append("</td>");
			}
			//sb.append("</tr><tr><td class=\"reward thead\"><span>보상</span></td>");
			sb.append("</tr><tr><td class=\"reward thead\"><span>Reward</span></td>");
			for (BossTemp boss : boss_list) {
				L1Info info = L1InfoDAO.getInfoNpc(boss.npcid);
				sb.append("<td class=\"reward\">");
				if (info == null) {
					sb.append("-");
				} else {
					ArrayList<ItemVO> dropList = null;
					if (info.getInfo().containsKey("dropList")) {
						dropList = (ArrayList<ItemVO>)info.getInfo().get("dropList");
					}
					if (dropList == null || dropList.isEmpty()) {
						sb.append("-");
					} else {
						int dropCnt = 0;
						for (ItemVO item : dropList) {
							if (dropCnt++ > 0) {
								if (dropCnt > 5) {// 최대 개수
									//sb.append(" 등");
									sb.append(" etc");
									break;
								}
								sb.append(", ");
							}
							sb.append(item.getName());
						}
					}
				}
				sb.append("</td>");
			}
			sb.append("</tr></tbody></table>");
			
			GUIDE_BOSS_TIME_PAIR = new KeyValuePair<String, String>("{GUIDE_BOSS_TIME}",		sb.toString());
			sb.setLength(0);
			boss_list.clear();
			boss_list = null;
		}
		params.add(GUIDE_BOSS_TIME_PAIR);
		
		if (GUIDE_RECOMMEND_PAIR == null) {
			for (GuideRecommendVO recommend : dao.getRecommendList()) {
				sb.append(GUIDE_RECOMMEND_CARD_1);
				sb.append(recommend.getUrl());
				sb.append(GUIDE_RECOMMEND_CARD_2);
				sb.append(recommend.getImg());
				sb.append(GUIDE_RECOMMEND_CARD_3);
				sb.append(recommend.getTitle());
				sb.append(GUIDE_RECOMMEND_CARD_4);
				sb.append(recommend.getContent());
				sb.append(GUIDE_RECOMMEND_CARD_5);
			}
			GUIDE_RECOMMEND_PAIR = new KeyValuePair<String, String>("{GUIDE_RECOMMEND}",	sb.toString());
			sb.setLength(0);
		}
		params.add(GUIDE_RECOMMEND_PAIR);
		
		// parameter define
		String document = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GuideResponse(request, model);
	}

}


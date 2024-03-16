package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;

import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.PowerbookType;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.item.ItemVO;
import l1j.server.web.dispatcher.response.keyword.KeywordLoader;
import l1j.server.web.dispatcher.response.powerbook.L1Info;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class PowerbookSearchResponse extends HttpResponseModel {
	private static KeyValuePair<String, String> PAGE_CNB_TYPE_PAIR;
	private static KeyValuePair<String, String> PAGE_CNB_SUB_TYPE_PAIR;
	
	private static String LIST_HTML;
	private static String ITEM_HTML;
	private static String MONSTER_HTML;
	private static String SKILL_HTML;
	private static String CONTENT_HTML;
	
	//private static final String ITEM_DAMAGE_HEAD = "<tr><th scope=\"col\">공격력 (작은/큰 몬스터)</th><td>";
	private static final String ITEM_DAMAGE_HEAD = "<tr><th scope=\"col\">Damage (small / large monster)</th><td>";
	private static final String ITEM_DAMAGE_BODY = "&nbsp;/&nbsp;";
	private static final String ITEM_DAMAGE_TAIL = "</td></tr>";
	
	//private static final String ITEM_AC_HEAD = "<tr><th scope=\"col\">방어력</th><td>";
	private static final String ITEM_AC_HEAD = "<tr><th scope=\"col\">Defense</th><td>";
	private static final String ITEM_AC_TAIL = "</td></tr>";
	
	private static final String ITEM_USE_CLASS_HEAD = "<a href=\"/powerbook/search?query=";
	private static final String ITEM_USE_CLASS_BODY = "\">";
	private static final String ITEM_USE_CLASS_TAIL = "</a>";
	
	//private static final String ITEM_TWOHAND_HEAD = "<tr><th scope=\"col\">한손/양손</th><td>";
	private static final String ITEM_TWOHAND_HEAD = "<tr><th scope=\"col\">One/Two Handed</th><td>";
	private static final String ITEM_TWOHAND_TAIL = "</td></tr>";
	//private static final String[] ITEM_TWOHANDS = { "양손", "한손" };
	private static final String[] ITEM_TWOHANDS = { "Two Hands", "One Hand" };
	
	/*private static final String ITEM_LONG_DMG = "원거리 대미지+";
	private static final String ITEM_LONG_HIT = "원거리 명중+";
	
	private static final String ITEM_SHORT_DMG = "근거리 대미지+";
	private static final String ITEM_SHORT_HIT = "근거리 명중+";
	
	private static final String ITEM_MR = "마법방어력+";*/

	private static final String ITEM_LONG_DMG = "Long Range Damage+";
	private static final String ITEM_LONG_HIT = "Long Range Hit+";
	
	private static final String ITEM_SHORT_DMG = "Close Range Damage+";
	private static final String ITEM_SHORT_HIT = "Close Range Hit+";
	
	private static final String ITEM_MR = "Magic Defense+";	

	private static final String ITEM_SP = "SP+";
	//private static final String ITEM_MAGIC = "마법 발동&nbsp;:&nbsp;";
	private static final String ITEM_MAGIC = "Magic PROC&nbsp;:&nbsp;";
	
	//private static final String ITEM_CANBEDMG_HEAD = "<tr><th scope=\"col\">손상 여부</th><td>";
	private static final String ITEM_CANBEDMG_HEAD = "<tr><th scope=\"col\">Damageable</th><td>";
	private static final String ITEM_CANBEDMG_TAIL = "</td></tr>";
	//private static final String[] ITEM_CANBEDMGS = { "손상되지 않음", "손상됨" };
	private static final String[] ITEM_CANBEDMGS = { "Not damageable", "Damageable" };
	
	//private static final String ITEM_ENCHANT_HEAD = "<tr><th scope=\"col\">인챈트</th><td>";
	private static final String ITEM_ENCHANT_HEAD = "<tr><th scope=\"col\">Enchant</th><td>";
	private static final String ITEM_ENCHANT_TAIL = "</td></tr>";
	//private static final String[] ITEM_ENCHANTS = { "강화 불가", "0 → +1부터 실패할 가능성 있음", "까지 안전하게 가능" };
	private static final String[] ITEM_ENCHANTS = { "Cannot be enchanted", "Possibility of failure from 0 → +1", "Safely possible up to " };
	
	//private static final String[] ITEM_TRADABLES = { "불가능", "가능" };
	private static final String[] ITEM_TRADABLES = { "Impossible", "Possible" };
	
	private static final String ITEM_SHOP_SEARCH_HEAD = "<a href=\"/my/item-search?keyword=";
	//private static final String ITEM_SHOP_SEARCH_TAIL = "\" class=\"object__price\">시세보기</a>";
	private static final String ITEM_SHOP_SEARCH_TAIL = "\" class=\"object__price\">View price</a>";
	//private static final String ITEM_SHOP_SEARCH_NOT_LOGIN = "<a href=\"javascript:login();\" class=\"object__price\">시세보기</a>";
	private static final String ITEM_SHOP_SEARCH_NOT_LOGIN = "<a href=\"javascript:login();\" class=\"object__price\">View price</a>";
	
	//private static final String DROP_DEFAULT = "<div class=\"wiki__group\"><div class=\"wiki__h2\"><h2>입수법 - 몬스터 드롭</h2></div><div class=\"wiki__body\"><p></p><section class=\"list list-item\"><span>아덴 월드내 몬스터에게서 획득하실 수 있습니다.</span></section><p></p><footer class=\"sub_info\"><p>※ 게임상 내용과 다를 수 있습니다.</p><p>※ 보다 정확한 정보를 확인하시려면 NCSOFT 파워북을 참조하세요.</p></footer></div></div>";
	//private static final String DROP_TABLE_HEAD = "<div class=\"wiki__group\"><div class=\"wiki__h2\"><h2>입수법 - 몬스터 드롭</h2></div><div class=\"wiki__body\"><p></p><section class=\"list list-item\"><table><caption>아이템 목록</caption><thead><tr><th scope=\"col\" class=\"list__header-label\">몬스터명</th><th scope=\"col\" class=\"list__level-label\">레벨</th><th scope=\"col\">HP</th><th scope=\"col\">MP</th><th scope=\"col\">마법방어력</th><th scope=\"col\">크기</th></tr></thead><tbody>";
	//private static final String DROP_TABLE_TAIL = "</tbody></table></section><p></p><footer class=\"sub_info\"><p>※ 게임상 내용과 다를 수 있습니다.</p><p>※ 보다 정확한 정보를 확인하시려면 NCSOFT 파워북을 참조하세요.</p></footer></div></div>";
	private static final String DROP_DEFAULT = "<div class=\"wiki__group\"><div class=\"wiki__h2\"><h2>How to get it - Monster Drop</h2></div><div class=\"wiki__body\"><p>< /p><section class=\"list list-item\"><table><caption>List of items</caption><thead><tr><th scope=\"col\" class=\"list__header-label \">Monster Name</th><th scope=\"col\" class=\"list__level-label\">Level</th><th scope=\"col\">HP</th><th scope=\"col\">MP</th><th scope=\"col\">Magic Defense</th><th scope=\"col\">Size</th></tr></ thead><tbody>";
	private static final String DROP_TABLE_HEAD = "<div class=\"wiki__group\"><div class=\"wiki__h2\"><h2>How to get it - Monster Drop</h2></div><div class=\"wiki__body\"><p></p><section class=\"list list-item\"><table><caption>Item List</caption><thead><tr><th scope=\"col\" class=\"list__header-label\">Monster Name</th><th scope=\"col\" class=\"list__level-label\">Level</th><th scope=\"col\">HP</th><th scope=\"col\">MP</th><th scope=\"col\">Magic Resistance</th><th scope=\"col\">Size</th></tr></thead><tbody>";
	private static final String DROP_TABLE_TAIL = "</tbody></table></section><p></p><footer class=\"sub_info\"><p>※ It may differ from the content in the game.</p><p>※ See Please refer to the NCSOFT PowerBook for more accurate information.</p></footer></div></div>";
	private static final String DROP_TABLE_BODY_1 = "<tr><td class=\"list__header\"><span class=\"list__name\"><a class=\"item-grade--7\" href=\"/powerbook/search?searchType=2&query=";
	private static final String DROP_TABLE_BODY_2 = "\">";
	private static final String DROP_TABLE_BODY_3 = "</a></span></td><td class=\"list__level\">";
	private static final String DROP_TABLE_BODY_4 = "</td><td>";
	private static final String DROP_TABLE_BODY_5 = "</td></tr>";
	
	private static final String MONSTER_SEARCH_HEAD = "<li><a href=\"/powerbook/search?searchType=1&query=";
	private static final String MONSTER_SEARCH_BODY = "\" class=\"drop-item__link\">";
	private static final String MONSTER_SEARCH_TAIL = "</a></li>";
	
	/*private static final String SKILL_PASSIVE_DURATION = "상시";
	private static final String SKILL_PASSIVE_STRING = "패시브";
	private static final String[] SKILL_RANGES = { "칸 내의 PC 혹은 몬스터", "술자", "적 PC 혹은 몬스터" };*/
	private static final String SKILL_PASSIVE_DURATION = "Constant";
	private static final String SKILL_PASSIVE_STRING = "Passive";
	private static final String[] SKILL_RANGES = { "PC/NPC within ", "Caster", "Enemy PC or monster", " cells"};
	private static final String SKILL_LEVEL_SPECIAL = "Special";
	private static final String SKILL_TYPE_BODY_1 = "<a class=\"item-grade--7\" href=\"/powerbook/search?searchType=4&query=";
	private static final String SKILL_TYPE_BODY_2 = "\">";
	
	//private static final String[] ALIGNS = { "라우풀", "카오틱", "뉴트럴" };
	private static final String[] ALIGNS = { "Lawful", "Chaotic", "Neutral" };
	
	private static final String CONTENT_BUTTON_HEAD = "<div class=\"wiki_admin_btn\"><a href=\"/powerbook/modify?id=";
	//private static final String CONTENT_BUTTON_BODY = "\">수정하기</a><a href=\"javascript:powerbookDelete(";
	//private static final String CONTENT_BUTTON_TAIL = ");\">삭제하기</a></div>";
	private static final String CONTENT_BUTTON_BODY = "\">Edit</a><a href=\"javascript:powerbookDelete(";
	private static final String CONTENT_BUTTON_TAIL = ");\">Delete</a></div>";	
	
	public PowerbookSearchResponse() {}
	private PowerbookSearchResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HttpResponse get_response() throws Exception {
		String searchType	= request.read_parameters_at_once("searchType");
		String query		= request.read_parameters_at_once("query");
		PowerbookType search_type		= null;
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
		
		params.add(new KeyValuePair<String, String>("{QUERY}",				query));
		
		L1Info info			= null;
		if (!StringUtil.isNullOrEmpty(searchType) && !StringUtil.isNullOrEmpty(query)) {
			search_type = PowerbookType.fromString(searchType);
			info = L1InfoDAO.getInstance().getInfo(search_type.toInt(), query);
		}
		
		String html			= null;
		if (LIST_HTML == null) {
			LIST_HTML		= load_file_string("./appcenter/powerbook/view_list.html");
		}
		if (ITEM_HTML == null) {
			ITEM_HTML		= load_file_string("./appcenter/powerbook/view_item.html");
		}
		if (MONSTER_HTML == null) {
			MONSTER_HTML	= load_file_string("./appcenter/powerbook/view_monster.html");
		}
		if (SKILL_HTML == null) {
			SKILL_HTML		= load_file_string("./appcenter/powerbook/view_skill.html");
		}
		if (CONTENT_HTML == null) {
			CONTENT_HTML	= load_file_string("./appcenter/powerbook/view_content.html");
		}
		if (info == null) {
			// parameter define
			String document = StringUtil.replace(LIST_HTML, params);
			params.clear();
			params = null;
			return create_response_html(HttpResponseStatus.OK, document);
		}
		
		StringBuilder sb	= new StringBuilder();
		switch(search_type) {
		// item
		case ITEM:
			html = ITEM_HTML;
			params.add(new KeyValuePair<String, String>("{NAME}",				info.getName()));
			params.add(new KeyValuePair<String, String>("{ICON}",				info.getInfo().get("icon").toString()));
			
			int itemType = (Integer)info.getInfo().get("itemType");
			if (itemType == 1) {
				sb.append(ITEM_DAMAGE_HEAD).append(info.getInfo().get("smallDmg")).append(ITEM_DAMAGE_BODY).append(info.getInfo().get("largeDmg")).append(ITEM_DAMAGE_TAIL);
			} else if (itemType == 2) {
				sb.append(ITEM_AC_HEAD).append(info.getInfo().get("ac")).append(ITEM_AC_TAIL);
			}
			params.add(new KeyValuePair<String, String>("{ABLITY}",				sb.toString()));
			sb.setLength(0);
			ArrayList<String> useClassList = (ArrayList<String>)info.getInfo().get("useClass");
			for (String useClass : useClassList) {
				if (sb.length() > 0) {
					sb.append(StringUtil.DivisionString);
				}
				sb.append(ITEM_USE_CLASS_HEAD).append(StringUtil.encodeUrl(useClass)).append(ITEM_USE_CLASS_BODY).append(useClass).append(ITEM_USE_CLASS_TAIL);
			}
			params.add(new KeyValuePair<String, String>("{CLASS}",				sb.toString()));
			sb.setLength(0);
			
			params.add(new KeyValuePair<String, String>("{WEIGHT}",				info.getInfo().get("weight").toString()));
			
			if (itemType == 1) {
				sb.append(ITEM_TWOHAND_HEAD).append((Boolean)info.getInfo().get("twohand") ? ITEM_TWOHANDS[0] : ITEM_TWOHANDS[1]).append(ITEM_TWOHAND_TAIL);
			}
			params.add(new KeyValuePair<String, String>("{WEAPON_TYPE}",		sb.toString()));
			sb.setLength(0);
			
			String useType = info.getInfo().get("useType").toString();
			if (useType.matches("BOW|SINGLE_BOW|GAUNTLET")) {
				int dmg = (Integer)info.getInfo().get("longDmg");
				int hit = (Integer)info.getInfo().get("longHit");
				if (dmg > 0) {
					sb.append(ITEM_LONG_DMG).append(dmg);
				}
				if (hit > 0) {
					if (sb.length() > 0) {
						sb.append(StringUtil.DivisionString);
					}
					sb.append(ITEM_LONG_HIT).append(hit);
				}
			} else {
				int dmg = (Integer)info.getInfo().get("dmg");
				int hit = (Integer)info.getInfo().get("hit");
				if (dmg > 0) {
					sb.append(ITEM_SHORT_DMG).append(dmg);
				}
				if (hit > 0) {
					if (sb.length() > 0) {
						sb.append(StringUtil.DivisionString);
					}
					sb.append(ITEM_SHORT_HIT).append(hit);
				}
			}
			int mr = (Integer)info.getInfo().get("mr");
			if (mr > 0) {
				if (sb.length() > 0) {
					sb.append(StringUtil.DivisionString);
				}
				sb.append(ITEM_MR).append(mr);
			}
			int sp = (Integer)info.getInfo().get("sp");
			if (sp > 0) {
				if (sb.length() > 0) {
					sb.append(StringUtil.DivisionString);
				}
				sb.append(ITEM_SP).append(sp);
			}
			
			if (info.getInfo().containsKey("magicName")) {
				String magicName = (String) info.getInfo().get("magicName");
				if (!StringUtil.isNullOrEmpty(magicName)) {
					if (sb.length() > 0) {
						sb.append(StringUtil.DivisionString);
					}
					sb.append(ITEM_MAGIC).append(info.getInfo().get("magicName"));
				}
			}
			params.add(new KeyValuePair<String, String>("{ABLITY_DETAIL}",		sb.toString()));
			sb.setLength(0);
			
			params.add(new KeyValuePair<String, String>("{MATERIAL}",			info.getInfo().get("material").toString()));
			
			if (itemType == 1 || itemType == 2) {
				sb.append(ITEM_ENCHANT_HEAD);
				int safenchant = (Integer)info.getInfo().get("safenchant");
				switch(safenchant) {
				case -1:
					sb.append(ITEM_ENCHANTS[0]);
					break;
				case 0:
					sb.append(ITEM_ENCHANTS[1]);
					break;
				default:
					sb.append(ITEM_ENCHANTS[2]).append(StringUtil.PlusString).append(safenchant);
					break;
				}
				sb.append(ITEM_ENCHANT_TAIL);
			}
			params.add(new KeyValuePair<String, String>("{ENCHANT_INFO}",		sb.toString()));
			sb.setLength(0);
			
			if (itemType == 1) {
				sb.append(ITEM_CANBEDMG_HEAD).append((Boolean)info.getInfo().get("canbedmg") ? ITEM_CANBEDMGS[0] : ITEM_CANBEDMGS[1]).append(ITEM_CANBEDMG_TAIL);
			}
			params.add(new KeyValuePair<String, String>("{CANBE_DMG}",			sb.toString()));
			sb.setLength(0);
			
			params.add(new KeyValuePair<String, String>("{TRADABLE}",			(Boolean)info.getInfo().get("trade") ? ITEM_TRADABLES[0] : ITEM_TRADABLES[1]));
			
			String shopUrl = account != null ? ITEM_SHOP_SEARCH_HEAD + StringUtil.encodeUrl(info.getName()) + ITEM_SHOP_SEARCH_TAIL : ITEM_SHOP_SEARCH_NOT_LOGIN;
			params.add(new KeyValuePair<String, String>("{SHOP_URL}",			shopUrl));
			
			int itemId = (Integer)info.getInfo().get("itemId");
			if (itemId == 40308 || itemId == 60716 || itemId == 3000028 || itemId == 40074 || itemId == 40087 || itemId == 140074 || itemId == 140087 || itemId == 240074 || itemId == 240087) {
				sb.append(DROP_DEFAULT);
			} else {
				ArrayList<L1Info> dropList = (ArrayList<L1Info>)info.getInfo().get("dropMonster");
				if (dropList != null && !dropList.isEmpty()) {
					sb.append(DROP_TABLE_HEAD);
					for (L1Info drop : dropList) {
						sb.append(DROP_TABLE_BODY_1).append(StringUtil.encodeUrl(drop.getName())).append(DROP_TABLE_BODY_2).append(drop.getName());
						sb.append(DROP_TABLE_BODY_3);
						sb.append(drop.getInfo().get("lvl"));
						sb.append(DROP_TABLE_BODY_4).append(drop.getInfo().get("hp"));
						sb.append(DROP_TABLE_BODY_4).append(drop.getInfo().get("mp"));
						sb.append(DROP_TABLE_BODY_4).append(drop.getInfo().get("mr"));
						sb.append(DROP_TABLE_BODY_4).append(drop.getInfo().get("size"));
						sb.append(DROP_TABLE_BODY_5);
					}
					sb.append(DROP_TABLE_TAIL);
				}
			}
			params.add(new KeyValuePair<String, String>("{DROP_LIST}",			sb.toString()));
			sb.setLength(0);
			break;
		// monster
		case NPC:
			html = MONSTER_HTML;
			params.add(new KeyValuePair<String, String>("{NAME}",				info.getName()));
			params.add(new KeyValuePair<String, String>("{ICON}",				info.getInfo().get("icon").toString()));
			params.add(new KeyValuePair<String, String>("{LEVEL}",				info.getInfo().get("lvl").toString()));
			params.add(new KeyValuePair<String, String>("{AC}",					info.getInfo().get("ac").toString()));
			params.add(new KeyValuePair<String, String>("{HP}",					info.getInfo().get("hp").toString()));
			params.add(new KeyValuePair<String, String>("{MP}",					info.getInfo().get("mp").toString()));
			params.add(new KeyValuePair<String, String>("{MR}",					info.getInfo().get("mr").toString()));
			params.add(new KeyValuePair<String, String>("{WEAK_ATTR}",			info.getInfo().get("weakAttr").toString()));
			params.add(new KeyValuePair<String, String>("{SIZE}",				info.getInfo().get("size").toString()));
			
			ArrayList<ItemVO> dropList = (ArrayList<ItemVO>)info.getInfo().get("dropList");
			if (dropList != null && !dropList.isEmpty()) {
				for (ItemVO item : dropList) {
					sb.append(MONSTER_SEARCH_HEAD);
					sb.append(StringUtil.encodeUrl(item.getName()));
					sb.append(MONSTER_SEARCH_BODY);
					sb.append(item.getBless()).append(item.getName());
					sb.append(MONSTER_SEARCH_TAIL);
				}
			}
			params.add(new KeyValuePair<String, String>("{DROP_LIST}",			sb.toString()));
			sb.setLength(0);
			break;
		// skill
		case SKILL:
			html = SKILL_HTML;
			params.add(new KeyValuePair<String, String>("{NAME}",				info.getName()));
			params.add(new KeyValuePair<String, String>("{ICON}",				info.getInfo().get("icon").toString()));
			//params.add(new KeyValuePair<String, String>("{SKILL_TYPE}",			info.getInfo().get("skillType").toString()));				
			sb.append(SKILL_TYPE_BODY_1).append(StringUtil.encodeUrl(info.getInfo().get("skillType").toString())).append(SKILL_TYPE_BODY_2).append(info.getInfo().get("skillType").toString());
			params.add(new KeyValuePair<String, String>("{SKILL_TYPE}",			sb.toString()));			
			sb.setLength(0);

			// http://localhost:8080/powerbook/search?searchType=4&query=Knight%20Skills	

			int mpConsume = (Integer)info.getInfo().get("mpConsume");
			params.add(new KeyValuePair<String, String>("{MP_CONSUME}",			mpConsume > 0 ? String.valueOf(mpConsume) : /*StringUtil.EmptyString*/"0"));
			int hpConsume = (Integer)info.getInfo().get("hpConsume");
			params.add(new KeyValuePair<String, String>("{HP_CONSUME}",			hpConsume > 0 ? String.valueOf(hpConsume) : /*StringUtil.EmptyString*/"0"));
			String itemname = info.getInfo().get("itemname").toString();
			if (StringUtil.isNullOrEmpty(itemname)) {
				itemname 	= StringUtil.MinusString;
			}
			params.add(new KeyValuePair<String, String>("{ITEM_NAME}",			itemname));
			
			int skillLevel = (Integer)info.getInfo().get("skill_level");
			params.add(new KeyValuePair<String, String>("{SKILL_LEVEL}",		skillLevel > 10 ? SKILL_LEVEL_SPECIAL : skillLevel > 0 ? String.valueOf(skillLevel) : StringUtil.EmptyString));

			params.add(new KeyValuePair<String, String>("{BUFF_DURATION}",		skillLevel == 50 ? SKILL_PASSIVE_DURATION : info.getInfo().get("buffDuration").toString()));
			
			int range = (Integer)info.getInfo().get("ranged");
			if (range > 0) {
				sb.append(SKILL_RANGES[0]).append(range).append(SKILL_RANGES[3]);
			} else {
				String target = info.getInfo().get("target").toString();
				if (target.equals("NONE")) {
					sb.append(SKILL_RANGES[1]);
				} else if (target.equals("ATTACK")) {
					sb.append(SKILL_RANGES[2]);
				} else if (!target.isEmpty())
					sb.append(target);
			}
			params.add(new KeyValuePair<String, String>("{TARGET}",				sb.toString()));
			sb.setLength(0);
			
			int align = (Integer)info.getInfo().get("lawful");
			params.add(new KeyValuePair<String, String>("{LAWFUL}",				align > 0 ? ALIGNS[0] : align < 0 ? ALIGNS[1] : ALIGNS[2]));
			
			if (skillLevel == 50) {
				sb.append(SKILL_PASSIVE_STRING);
			} else {
				String attr = (String)info.getInfo().get("attr");
				if (!StringUtil.isNullOrEmpty(attr)) {
					sb.append(attr);
				}
			}
			params.add(new KeyValuePair<String, String>("{ATTR}",				sb.toString().isEmpty() ? StringUtil.MinusString : sb.toString()));
			sb.setLength(0);

			sb.append(info.getInfo().get("explanation").toString());
			params.add(new KeyValuePair<String, String>("{EXPLANATION}",		sb.toString().isEmpty() ? StringUtil.MinusString : sb.toString()));
			sb.setLength(0);
			
			ArrayList<L1Info> dropMonster = (ArrayList<L1Info>)info.getInfo().get("dropMonster");
			if (dropMonster != null && !dropMonster.isEmpty()) {
				sb.append(DROP_TABLE_HEAD);
				for (L1Info drop : dropMonster) {
					sb.append(DROP_TABLE_BODY_1).append(StringUtil.encodeUrl(drop.getName())).append(DROP_TABLE_BODY_2).append(drop.getName());
					sb.append(DROP_TABLE_BODY_3);
					sb.append(drop.getInfo().get("lvl"));
					sb.append(DROP_TABLE_BODY_4).append(drop.getInfo().get("hp"));
					sb.append(DROP_TABLE_BODY_4).append(drop.getInfo().get("mp"));
					sb.append(DROP_TABLE_BODY_4).append(drop.getInfo().get("mr"));
					sb.append(DROP_TABLE_BODY_4).append(drop.getInfo().get("size"));
					sb.append(DROP_TABLE_BODY_5);
				}
				sb.append(DROP_TABLE_TAIL);
			}
			params.add(new KeyValuePair<String, String>("{DROP_LIST}",			sb.toString()));
			sb.setLength(0);
			break;
		// content
		case GUIDE:
			html = CONTENT_HTML;
			params.add(new KeyValuePair<String, String>("{NAME}",				info.getName()));
			if (account != null && account.isGm()) {
				sb.append(CONTENT_BUTTON_HEAD);
				sb.append(info.getInfo().get("id"));
				sb.append(CONTENT_BUTTON_BODY);
				sb.append(info.getInfo().get("id"));
				sb.append(CONTENT_BUTTON_TAIL);
			}
			params.add(new KeyValuePair<String, String>("{UTIL_BUTTON}",		sb.toString()));
			sb.setLength(0);
			
			String mainImg = StringUtil.EmptyString;
			if (info.getInfo().get("mainImg") != null) {
				mainImg = info.getInfo().get("mainImg").toString();
			}
			
			params.add(new KeyValuePair<String, String>("{MAIN_IMG}",			mainImg));
			params.add(new KeyValuePair<String, String>("{CONTENT}",			info.getInfo().get("content").toString()));
			break;
		// list
		default:
			html = LIST_HTML;
			break;
		}
		
		// parameter define
		String document = StringUtil.replace(html, params);
		params.clear();
		params = null;
		
		return create_response_html(HttpResponseStatus.OK, document);
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PowerbookSearchResponse(request, model);
	}
}


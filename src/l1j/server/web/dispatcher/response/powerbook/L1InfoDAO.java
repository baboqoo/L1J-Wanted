package l1j.server.web.dispatcher.response.powerbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.templates.L1Drop;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.PowerbookType;
import l1j.server.web.dispatcher.response.item.ItemDAO;
import l1j.server.web.dispatcher.response.item.ItemVO;

/**
 * 파워북
 * @author LinOffice
 */
public class L1InfoDAO {
	private static L1InfoDAO _instance;
	public static L1InfoDAO getInstance() {
		if (_instance == null) {
			_instance = new L1InfoDAO();
		}
		return _instance;
	}
	
	private static final ArrayList<L1Info> ALL_LIST			= new ArrayList<>();
	private static final ArrayList<L1Info> MAIN_LIST		= new ArrayList<>();
	private static final ArrayList<L1Info> GUIDE_MAIN_LIST	= new ArrayList<>();
	private static final LinkedHashMap<String, ArrayList<PowerbookGuideVO>> GUIDE_LIST = new LinkedHashMap<>();
	
	public static LinkedHashMap<String, ArrayList<PowerbookGuideVO>> getGuide() {
		return GUIDE_LIST;
	}
	
	private L1InfoDAO() {
		load();
	}
	
	void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		L1Info info = null;
		HashMap<String, Object> data = null;
		ItemVO item = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("SELECT group_type, id, title, uri, is_new FROM app_powerbook_guide ORDER BY group_type, id");
			rs = pstm.executeQuery();
			while (rs.next()) {
				String groupType	= rs.getString("group_type");
				groupType			= groupType.substring(groupType.indexOf(".") + 1);
				int id				= rs.getInt("id");
				String title		= rs.getString("title");
				String uri			= rs.getString("uri");
				boolean isNew		= Boolean.parseBoolean(rs.getString("is_new"));
				ArrayList<PowerbookGuideVO> list = GUIDE_LIST.get(groupType);
				if (list == null) {
					list = new ArrayList<>();
					GUIDE_LIST.put(groupType, list);
				}
				list.add(new PowerbookGuideVO(groupType, id, title, uri, isNew));
			}
			SQLUtil.close(rs, pstm);
			
			// 가이드 정보
			pstm = con.prepareStatement("SELECT id, title, content, mainImg, main, guideMain FROM app_powerbook ORDER BY id ASC");
			rs = pstm.executeQuery();
			while(rs.next()) {
				int id				= rs.getInt("id");
				if (id > _cnt) {
					_cnt = id;
				}
				String title		= rs.getString("title");
				boolean main		= Boolean.parseBoolean(rs.getString("main"));
				boolean guideMain	= Boolean.parseBoolean(rs.getString("guideMain"));
				data = new HashMap<String, Object>();
				data.put("id",		id);
				data.put("title",	title);
				data.put("content",	rs.getString("content"));
				data.put("mainImg",	rs.getString("mainImg"));
				
				info = new L1Info(title, PowerbookType.GUIDE.toInt(), data, settingInfoText(data, PowerbookType.GUIDE));
				ALL_LIST.add(info);
				if (main) {
					MAIN_LIST.add(info);
				}
				if (guideMain) {
					GUIDE_MAIN_LIST.add(info);
				}
			}
			Collections.reverse(MAIN_LIST);
			Collections.reverse(GUIDE_MAIN_LIST);
			
			// 몬스터 정보
			for (L1Npc npc : NpcTable.getInstance().getAllTemplate()) {
				if (!npc.getImpl().matches(L1MonsterInstance.MONSTER_IMPLEMENTS_REGEX)) {
					continue;
				}
				//if (StringUtil.isNullOrEmpty(npc.getDescKr()) || StringUtil.isNullOrEmpty(npc.getDesc())) {
				if (StringUtil.isNullOrEmpty(npc.getDescEn()) || StringUtil.isNullOrEmpty(npc.getDesc())) {
					continue;
				}
				data = new HashMap<String, Object>();
				data.put("npcId",		npc.getNpcId());
				data.put("lvl",			npc.getLevel());
				data.put("hp",			npc.getHp());
				data.put("mp",			npc.getMp());
				data.put("ac",			npc.getAc());
				data.put("mr",			npc.getMr());
				data.put("icon",        npc.getSpriteId());
				data.put("size",		npc.isBig() ? "Big Monster" : "Small Monster");
				data.put("weakAttr",	settingWeakAttr(npc.getWeakAttr()));
				
				ArrayList<L1Drop> drops = DropTable.getInstance().getDropList(npc.getNpcId());
				ArrayList<ItemVO> dropList = null;
				if (drops != null && !drops.isEmpty()) {
					for (L1Drop drop : drops) {
						if (drop == null) {
							continue;
						}
						item = ItemDAO.getItemInfo(drop.getItemid());
						if (item == null) {
							continue;
						}
						if (dropList == null) {
							dropList = new ArrayList<ItemVO>();
						}
						dropList.add(item);
					}
				}
				if (dropList != null && !dropList.isEmpty()) {
					data.put("dropList", dropList);
				}
				//info = new L1Info(npc.getDescKr(), PowerbookType.NPC.toInt(), data, settingInfoText(data, PowerbookType.NPC));
				info = new L1Info(npc.getDescEn(), PowerbookType.NPC.toInt(), data, settingInfoText(data, PowerbookType.NPC));
				ALL_LIST.add(info);
			}
			
			// 액티브 스킬 정보
			for (L1Skills skill : SkillsTable.getAllActiveTemplate()){
				if (skill.getSkillLevel() <= 0 || skill.getInfo() == null) {
					continue;
				}
				
				data = new HashMap<String, Object>();
				data.put("skill_id",	skill.getSkillId());
				data.put("skillType",	settingSkillType(skill.getClassType()));
				//data.put("skillQuery",  settingSkillQuery(skill.getClassType())););
				data.put("skill_level",	skill.getSkillLevel());
				data.put("mpConsume",	skill.getMpConsume());
				data.put("hpConsume",	skill.getHpConsume());
				
				int itemConsumeId = skill.getItemConsumeId();
				String itemConsumName = StringUtil.EmptyString;
				if (itemConsumeId > 0) {
					item = ItemDAO.getItemInfo(itemConsumeId);
					if (item != null) {
						//itemConsumName = String.format("%s %d개", item.getName(), skill.getItemConsumeCount());
						itemConsumName = String.format("%s %d pieces", item.getName(), skill.getItemConsumeCount());

					}
				}
				data.put("itemname", itemConsumName);
				int duration 				= skill.getBuffDuration();
				String durationText 		= skill.getBuffDurationText();
				//data.put("buffDuration",	duration == 0 ? "순간" : duration + "초");
				data.put("buffDuration",  	!durationText.isEmpty() ? durationText : duration == 0 ? "Instant" : duration + " seconds");
				String targetText 			= skill.getTargetToText();
				if (!targetText.isEmpty()) {
					data.put("ranged",			0);
					data.put("target",			targetText);
				} else {
					data.put("target",			skill.getTarget());
					data.put("ranged",			skill.getRanged());	
				}		  
				data.put("attr",			skill.getAttr().getDesc());
				data.put("lawful",			skill.getAlignment());
				data.put("icon",			skill.getInfo().getIcon());
				data.put("msg",				skill.getInfo().getTooltipStrId());
				data.put("explanation",		skill.getEffectText());
				
				item = ItemDAO.getItemInfoSkill(String.format("(%s)", skill.getName()));
				if (item != null && item.getDropMonster() != null && !item.getDropMonster().isEmpty()) {
					data.put("dropMonster", dropMonsterParse(item.getDropMonster()));
				}
				info = new L1Info(skill.getDescEn(), PowerbookType.SKILL.toInt(), data, settingInfoText(data, PowerbookType.SKILL));
				ALL_LIST.add(info);
			}
			
			// 패시브 스킬 정보
			for (L1PassiveSkills passive : SkillsTable.getAllPassiveTemplate()){
				data = new HashMap<String, Object>();
				data.put("passive_id",		passive.getPassiveId());
				data.put("skillType",		settingSkillType(passive.getClassType()));
				data.put("skill_level",		50);
				data.put("mpConsume",		0);
				data.put("hpConsume",		0);
				data.put("itemname", 		StringUtil.EmptyString);
				//data.put("buffDuration",	"상시");
				data.put("buffDuration",	"Permanent");
				data.put("target",			StringUtil.EmptyString);
				data.put("ranged",			0);
				data.put("attr",			0);
				data.put("lawful",			0);
				data.put("icon",			passive.getOnIconId());
				data.put("msg",				passive.getTooltipStrId());
				
				item = ItemDAO.getItemInfoSkill(String.format("(%s)", passive.getName()));
				if (item != null && item.getDropMonster() != null && !item.getDropMonster().isEmpty()) {
					data.put("dropMonster", dropMonsterParse(item.getDropMonster()));
				}
				info = new L1Info(passive.getDescEn(), PowerbookType.SKILL.toInt(), data, settingInfoText(data, PowerbookType.SKILL));
				ALL_LIST.add(info);
			}
			
			// 아이템 정보
			for (ItemVO itemInfo : ItemDAO.getList()) {
				data = new HashMap<String, Object>();
				data.put("itemId",		itemInfo.getItemid());
				data.put("icon",		itemInfo.getInvgfx());
				data.put("itemType",	itemInfo.getItemType());
				data.put("useType",		itemInfo.getUseType());
				data.put("weight",		itemInfo.getWeight() * 0.001);
				data.put("material",	itemInfo.getMaterial());
				data.put("safenchant",	itemInfo.getSafenchant());
				data.put("useClass",	itemInfo.getUseClass());
				data.put("ac",			itemInfo.getAc());
				data.put("smallDmg",	itemInfo.getSmallDmg());
				data.put("largeDmg",	itemInfo.getLargeDmg());
				data.put("hit",			itemInfo.getHit());
				data.put("dmg",			itemInfo.getDmg());
				data.put("longHit",		itemInfo.getLongHit());
				data.put("longDmg",		itemInfo.getLongDmg());
				data.put("mr",			itemInfo.getMr());
				data.put("sp",			itemInfo.getSp());
				data.put("canbedmg",	itemInfo.isCanbedmg());
				data.put("twohand",		itemInfo.isTwohand());
				data.put("magicName",	itemInfo.getMagicName());
				data.put("trade",		itemInfo.isTrade());
				data.put("bless",		itemInfo.getBless());
				
				if (itemInfo.getDropMonster() != null && !itemInfo.getDropMonster().isEmpty()) {
					data.put("dropMonster", dropMonsterParse(itemInfo.getDropMonster()));
				}
				
				info = new L1Info(itemInfo.getName(), PowerbookType.ITEM.toInt(), data, settingInfoText(data, PowerbookType.ITEM));
				ALL_LIST.add(info);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	ArrayList<L1Info> dropMonsterParse(ArrayList<Integer> dropMonsters) {
		L1Info npcInfo = null;
		ArrayList<L1Info> result = null;
		for (int monId : dropMonsters) {
			npcInfo = getInfoNpc(monId);
			if (npcInfo != null) {
				if (result == null) {
					result = new ArrayList<>();
				}
				result.add(npcInfo);
			}
		}
		return result;
	}
	
	/*String settingWeakAttr(L1Attr attr) {
		switch(attr) {
		case EARTH:
			return "땅";
		case WATER:
			return "물";
		case FIRE:
			return "불";
		case WIND:
			return "바람";
		default:
			return StringUtil.MinusString;
		}
	}*/

	String settingWeakAttr(L1Attr attr) {
		switch (attr) {
			case EARTH:
				return "Earth";
			case WATER:
				return "Water";
			case FIRE:
				return "Fire";
			case WIND:
				return "Wind";
			default:
				return StringUtil.MinusString;
		}
	}
	
	
	/*String settingSkillType(int classType) {
		switch(classType) {
		case 0:
			return "군주 마법";
		case 1:
			return "기사 마법";
		case 2:
			return "요정 마법";
		case 3:
			return "마법사 마법";
		case 4:
			return "다크엘프 마법";
		case 5:
			return "용기사 마법";
		case 6:
			return "환술사 마법";
		case 7:
			return "전사 마법";
		case 8:
			return "검사 마법";
		case 9:
			return "창기사 마법";
		default:
			return "일반 마법";
		}
	}*/

	String settingSkillType(int classType) {
		switch (classType) {
			case 0:
				return "Monarch Skills";
			case 1:
				return "Knight Skills";
			case 2:
				return "Elf Skills";
			case 3:
				return "Wizard Skills";
			case 4:
				return "Dark Elf Skills";
			case 5:
				return "Dragon Knight Skills";
			case 6:
				return "Illusionist Skills";
			case 7:
				return "Warrior Skills";
			case 8:
				return "Fencer Skills";
			case 9:
				return "Paladin Skills";
			default:
				return "Common Skills";
		}
	}

	/*String settingSkillQuery(int classType) {
		switch (classType) {
			case 0:
				return "Monarch Skills";
			case 1:
				return "Knight Skills";
			case 2:
				return "Elf Skills";
			case 3:
				return "Wizard Magic";
			case 4:
				return "Dark Elf Magic";
			case 5:
				return "Dragon Knight Magic";
			case 6:
				return "Illusionist Magic";
			case 7:
				return "Warrior Magic";
			case 8:
				return "Fencer Magic";
			case 9:
				return "Paladin Magic";
			default:
				return "Common Magic";
		}
	}*/	
		
	String settingInfoText(HashMap<String, Object> data, PowerbookType type) {
		StringBuilder sb = new StringBuilder();
		switch(type){
		case ITEM:// 아이템
			int itemType = (Integer)data.get("itemType") ;
			if (itemType == 1) {
				//sb.append("공격력: ").append(data.get("smallDmg")).append(StringUtil.SlushString).append(data.get("largeDmg")).append(StringUtil.EmptyOneString);
				sb.append("Attack Power: ").append(data.get("smallDmg")).append(StringUtil.SlushString).append(data.get("largeDmg")).append(StringUtil.EmptyOneString);
				int addDmg = (Integer)data.get("dmg");
				if (addDmg > 0) {
					//sb.append("대미지: ").append(addDmg).append(StringUtil.EmptyOneString);
					sb.append("Additional Damage: ").append(addDmg).append(StringUtil.EmptyOneString);
				}
				int addHit = (Integer)data.get("hit");
				if (addHit > 0) {
					//sb.append("명중: ").append(addHit).append(StringUtil.EmptyOneString);
					sb.append("Hit: ").append(addHit).append(StringUtil.EmptyOneString);
				}
			} else if (itemType == 2) {
				//sb.append("방어력: ").append(data.get("ac")).append(StringUtil.EmptyOneString);
				sb.append("Defense: ").append(data.get("ac")).append(StringUtil.EmptyOneString);
			}
			//sb.append("무게: ").append(data.get("weight")).append(StringUtil.EmptyOneString);
			//sb.append("재질: ").append(data.get("material")).append(StringUtil.EmptyOneString);
			sb.append("Weight: ").append(data.get("weight")).append(StringUtil.EmptyOneString);
			sb.append("Material: ").append(data.get("material")).append(StringUtil.EmptyOneString);
			String magicName = (String) data.get("magicName");
			if (!StringUtil.isNullOrEmpty(magicName)) {
				//sb.append("마법발동: ").append(magicName).append(StringUtil.EmptyOneString);
				sb.append("Magic Activation: ").append(magicName).append(StringUtil.EmptyOneString);
			}
			//sb.append((Boolean)data.get("trade") ? "거래불가" : "거래가능").append(StringUtil.EmptyOneString);
			sb.append((Boolean) data.get("trade") ? "Untradeable" : "Tradeable").append(StringUtil.EmptyOneString);
			if (itemType != 0) {
				@SuppressWarnings("unchecked")
				ArrayList<String> useClass = (ArrayList<String>)data.get("useClass");
				if (useClass != null && !useClass.isEmpty()) {
					//sb.append("클래스: ");
					sb.append("Class: ");
					if (useClass.size() >= 10) {
						//sb.append("전체");
						sb.append("All");
					} else {
						for (String i : useClass) {
							sb.append(i).append(StringUtil.EmptyOneString);
						}
					}
				}
			}
			break;
		/*case NPC:// 몬스터
			sb.append("레벨: ").append(data.get("lvl")).append(StringUtil.EmptyOneString);
			sb.append("방어력: ").append(data.get("ac")).append(StringUtil.EmptyOneString);
			sb.append("HP: ").append(data.get("hp")).append(StringUtil.EmptyOneString);
			sb.append("MP: ").append(data.get("mp")).append(StringUtil.EmptyOneString);
			sb.append("마법방어력: ").append(data.get("mr")).append(StringUtil.EmptyOneString);
			sb.append(data.get("size"));
			break;
		case SKILL:// 스킬
			sb.append("MP소모: ").append(data.get("mpConsume")).append(StringUtil.EmptyOneString);
			sb.append("HP소모: ").append(data.get("hpConsume")).append(StringUtil.EmptyOneString);
			String consumName = (String)data.get("itemname");
			if (!StringUtil.isNullOrEmpty(consumName)) {
				sb.append("재료: ").append(consumName).append(StringUtil.EmptyOneString);
			}
			sb.append("지속시간: ").append(data.get("buffDuration"));
			break;
		case GUIDE:// 가이드
			sb.append("가이드: ").append(data.get("title")).append("<br><br>");
			sb.append("내용: ").append(data.get("content"));
			break;
		default:break;
		}*/

		case NPC:
			sb.append("Level: ").append(data.get("lvl")).append(StringUtil.EmptyOneString);
			sb.append("Defense: ").append(data.get("ac")).append(StringUtil.EmptyOneString);
			sb.append("HP: ").append(data.get("hp")).append(StringUtil.EmptyOneString);
			sb.append("MP: ").append(data.get("mp")).append(StringUtil.EmptyOneString);
			sb.append("Magic Resistance: ").append(data.get("mr")).append(StringUtil.EmptyOneString);
			sb.append(data.get("size"));
			break;
		case SKILL:
			sb.append("MP Consumption: ").append(data.get("mpConsume")).append(StringUtil.EmptyOneString);
			sb.append("HP Consumption: ").append(data.get("hpConsume")).append(StringUtil.EmptyOneString);
			String consumName = (String) data.get("itemname");
			if (!StringUtil.isNullOrEmpty(consumName)) {
				sb.append("Materials: ").append(consumName).append(StringUtil.EmptyOneString);
			}
			sb.append("Duration: ").append(data.get("buffDuration"));
			break;
		case GUIDE:
			sb.append("Guide: ").append(data.get("title")).append("<br><br>");
			sb.append("Content: ").append(data.get("content"));
			break;
		default:
			break;
		}

		return sb.toString();
	}
	
	public static boolean isInfoContains(String keyword) {
		for (L1Info info : ALL_LIST) {
			if (info.getInfoType() != PowerbookType.GUIDE.toInt() && info.getName().equalsIgnoreCase(keyword)) {
				return true;
			}
		}
		return false;
	}
	
	public L1Info getInfo(int infoType, String name) {
		L1Info vo = null;
		for (L1Info info : ALL_LIST) {
			if (info.getInfoType() != infoType) {
				continue;
			}
			if (info.getName().equalsIgnoreCase(name)) {
				vo = info;
				break;
			}
		}
		return vo;
	}
	
	public static L1Info getInfoNpc(int npcId) {
		L1Info vo = null;
		for (L1Info info : ALL_LIST) {
			if (info.getInfoType() != PowerbookType.NPC.toInt()) {
				continue;
			}
			if ((Integer)info.getInfo().get("npcId") == npcId) {
				vo = info;
				break;
			}
		}
		return vo;
	}
	
	public static ArrayList<L1Info> getInfoQuery(String name){
		ArrayList<L1Info> data = new ArrayList<L1Info>();
		for (L1Info info : ALL_LIST) {
			if (info.getName().toLowerCase().contains(name.toLowerCase())) {
				data.add(info);
			} else {
				for (Object obj : info.getInfo().values()) {
					if (obj instanceof String && ((String) obj).toLowerCase().contains(name.toLowerCase())) {
						data.add(info);
					}
				}
			}
		}
		return data;
	}
	
	public static ArrayList<String> getSuggestQuery(String name, int limit, int infoType){
		ArrayList<String> data = new ArrayList<String>();
		int cnt = 0;
		for (L1Info info : ALL_LIST) {
			if (infoType > 0 && info.getInfoType() != infoType) {
				continue;
			}
			if (info.getName().toLowerCase().contains(name.toLowerCase())) {
				data.add(info.getName());
				if (++cnt >= limit) {
					break;
				}
			}
		}
		return data;
	}
	
	public static ArrayList<L1Info> getInfoMainList(){
		return MAIN_LIST;
	}
	
	public static ArrayList<L1Info> getInfoGuideMainList(){
		return GUIDE_MAIN_LIST;
	}
	
	public L1Info getInfoGuide(int infoType, int id) {
		L1Info vo = null;
		for (L1Info info : ALL_LIST) {
			if (info.getInfoType() != infoType) {
				continue;
			}
			if ((Integer)info.getInfo().get("id") == id) {
				vo = info;
				break;
			}
		}
		return vo;
	}
	
	private int _cnt;
	public int guideNextId() {
		return ++_cnt;
	}
	
	public boolean guideInsert(L1Info info) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO app_powerbook SET id=?, title=?, content=?, mainImg=?");
			int i=0;
			pstm.setInt(++i, (Integer)info.getInfo().get("id"));
			pstm.setString(++i, (String)info.getInfo().get("title"));
			pstm.setString(++i, (String)info.getInfo().get("content"));
			pstm.setString(++i, (String)info.getInfo().get("mainImg"));
			if (pstm.executeUpdate() > 0) {
				ALL_LIST.add(info);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean guideUpdate(L1Info info) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE app_powerbook SET title=?, content=?, mainImg=? WHERE id=?");
			int i=0;
			pstm.setString(++i, (String)info.getInfo().get("title"));
			pstm.setString(++i, (String)info.getInfo().get("content"));
			pstm.setString(++i, (String)info.getInfo().get("mainImg"));
			pstm.setInt(++i, (Integer)info.getInfo().get("id"));
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean guideDelete(int id) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM app_powerbook WHERE id=?");
			pstm.setInt(1, id);
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public static void reload() {
		release();
		_instance = new L1InfoDAO();
	}
	
	public static void release() {
		GUIDE_LIST.clear();
		ALL_LIST.clear();
		MAIN_LIST.clear();
		GUIDE_MAIN_LIST.clear();
		_instance = null;
	}
}


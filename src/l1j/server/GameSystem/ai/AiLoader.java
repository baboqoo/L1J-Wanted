package l1j.server.GameSystem.ai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.ai.bean.AiBuffObject;
import l1j.server.GameSystem.ai.bean.AiDropObject;
import l1j.server.GameSystem.ai.bean.AiItemObject;
import l1j.server.GameSystem.ai.bean.AiSkillObject;
import l1j.server.GameSystem.ai.constuct.AiMent;
import l1j.server.GameSystem.ai.constuct.AiPledge;
import l1j.server.GameSystem.ai.constuct.AiType;
import l1j.server.common.data.Gender;
import l1j.server.server.GameServerSetting;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1CharacterInfo.L1Class;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.skill.L1SkillType;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * AI 데이터 로드 클래스
 * @author LinOffice
 */
public class AiLoader {
	private static Logger _log	= Logger.getLogger(AiLoader.class.getName());
	private static AiLoader _instance;
	public static AiLoader getInstance(){
		if (_instance == null) {
			_instance = new AiLoader();
		}
		return _instance;
	}
	
	/**
	 * 혈맹기준 ai 리스트
	 * @param ai_type
	 * @param pledgeName
	 * @return list
	 */
	public FastTable<L1AiUserInstance> getPledgeUsers(AiType ai_type, String pledgeName){
		FastMap<String, FastTable<L1AiUserInstance>> type_map = _users.get(ai_type);
		if (type_map == null || type_map.isEmpty()) {
			return null;
		}
		return type_map.get(pledgeName);
	}
	
	/**
	 * 타입기준 ai 리스트
	 * @param ai_type
	 * @return map
	 */
	public FastMap<String, FastTable<L1AiUserInstance>> getTypeUsers(AiType ai_type){
		FastMap<String, FastTable<L1AiUserInstance>> type_map = _users.get(ai_type);
		if (type_map == null || type_map.isEmpty()) {
			return null;
		}
		return type_map;
	}
	
	public FastTable<String> getMent(AiMent type){
		return _user_ments.get(type);
	}
	
	public int[] getFishLocation() {
		if (_fish_location == null || _fish_location.isEmpty()) {
			return null;
		}
		L1Location loc;
		for (int[] val : _fish_location) {
			loc = new L1Location(val[0], val[1], val[2]);
			if (loc.getMap().isPassable(loc)) {
				return val;
			}
		}
		return null;
	}
	
	private final FastMap<AiType, FastMap<String, FastTable<L1AiUserInstance>>> _users;// ai user data
	private final FastMap<Integer, FastTable<AiItemObject>> _user_items;// ai item data
	private final FastMap<Integer, FastTable<AiDropObject>> _user_drops;// ai droplist data
	private final FastMap<Integer, AiSkillObject> _user_skills;// ai learn skill data
	private final FastTable<AiBuffObject> _user_buffs;// ai buff data
	private final FastMap<AiMent, FastTable<String>> _user_ments;// ai ment data
	private final FastTable<int[]> _fish_location;
	
	/**
	 * 기본 생성자
	 */
	private AiLoader(){
		Connection con		= null;
		try {
			con				= L1DatabaseFactory.getInstance().getConnection();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		_user_items			= loadItem(con);
		_user_drops			= loadDrop(con);
		_user_skills		= loadSkill(con);
		_user_buffs			= loadBuff(con);
		_users				= loadUser(con);
		_user_ments			= loadMent(con);
		_fish_location		= loadFish(con);
		SQLUtil.close(con);
	}
	
	/**
	 * 아이템 로드
	 * @param con
	 * @return FastMap<Integer, FastTable<AiItemObject>>
	 */
	FastMap<Integer, FastTable<AiItemObject>> loadItem(Connection con){
		FastMap<Integer, FastTable<AiItemObject>> user_items = new FastMap<Integer, FastTable<AiItemObject>>();
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		AiItemObject obj		= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM ai_user_item");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				obj = new AiItemObject(
						L1Class.getType(rs.getString("class")),
						rs.getInt("itemId"),
						rs.getInt("count"),
						rs.getInt("enchantLevel"),
						rs.getInt("attrLevel"),
						Boolean.parseBoolean(rs.getString("equip")));
				FastTable<AiItemObject> typeList = user_items.get(obj.getType());
				if (typeList == null) {
					typeList = new FastTable<AiItemObject>();
					user_items.put(obj.getType(), typeList);
				}
				typeList.add(obj);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return user_items;
	}
	
	/**
	 * 드랍리스트 로드
	 * @param con
	 * @return FastMap<Integer, FastTable<AiDropObject>>
	 */
	FastMap<Integer, FastTable<AiDropObject>> loadDrop(Connection con){
		FastMap<Integer, FastTable<AiDropObject>> drop_items = new FastMap<Integer, FastTable<AiDropObject>>();
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		AiDropObject obj		= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM ai_user_drop");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				obj = new AiDropObject(
						L1Class.getType(rs.getString("class")),
						rs.getInt("itemId"),
						rs.getInt("count"),
						rs.getInt("chance"));
				FastTable<AiDropObject> typeList = drop_items.get(obj.getType());
				if (typeList == null) {
					typeList = new FastTable<AiDropObject>();
					drop_items.put(obj.getType(), typeList);
				}
				typeList.add(obj);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return drop_items;
	}
	
	/**
	 * 스킬 로드
	 * @param con
	 * @return FastMap<Integer, AiSkillObject>
	 */
	FastMap<Integer, AiSkillObject> loadSkill(Connection con){
		FastMap<Integer, AiSkillObject> userSkill = new FastMap<Integer, AiSkillObject>();
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		AiSkillObject obj	= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM ai_user_skill");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				obj = new AiSkillObject(
						L1Class.getType(rs.getString("class")),
						parseActiveSkill(rs.getString(L1SkillType.ACTIVE.getName()).replaceAll(StringUtil.LineString, StringUtil.EmptyString).split(StringUtil.CommaString)),
						parsePassiveSkill(rs.getString(L1SkillType.PASSIVE.getName()).replaceAll(StringUtil.LineString, StringUtil.EmptyString).split(StringUtil.CommaString)));
				userSkill.put(obj.getType(), obj);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return userSkill;
	}
	
	/**
	 * 버프 로드
	 * @param con
	 * @return FastTable<AiBuffObject>
	 */
	FastTable<AiBuffObject> loadBuff(Connection con){
		FastTable<AiBuffObject> user_buffs = new FastTable<AiBuffObject>();
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM ai_user_buff");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				user_buffs.add(new AiBuffObject(
						L1Class.getType(rs.getString("class")),
						rs.getInt("elfAttr"),
						parseActiveSkill(rs.getString("buff").replaceAll(StringUtil.LineString, StringUtil.EmptyString).split(StringUtil.CommaString))));
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return user_buffs;
	}
	
	/**
	 * 멘트 로드
	 * @param con
	 * @return FastMap<AiMentType, FastTable<String>>
	 */
	FastMap<AiMent, FastTable<String>> loadMent(Connection con){
		FastMap<AiMent, FastTable<String>> user_ment = new FastMap<AiMent, FastTable<String>>();
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		AiMent type			= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM ai_user_ment");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				String ment = rs.getString("ment");
				type = AiMent.fromString(rs.getString("type"));
				FastTable<String> typeList = user_ment.get(type);
				if (typeList == null) {
					typeList = new FastTable<String>();
					user_ment.put(type, typeList);
				}
				typeList.add(ment);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return user_ment;
	}
	
	FastTable<int[]> loadFish(Connection con){
		FastTable<int[]> location = new FastTable<int[]>();
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM ai_user_fish");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				location.add(new int[] { 
						rs.getInt("loc_x"), rs.getInt("loc_y"), 5490, rs.getInt("heading"), rs.getInt("fish_x"), rs.getInt("fish_y") 
						});
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return location;
	}
	
	L1Skills[] parseActiveSkill(String[] array){
		L1Skills skills	= null;
		ArrayList<L1Skills> list = new ArrayList<L1Skills>();
		for (int i=0; i<array.length; i++) {
			skills = SkillsTable.getTemplate(Integer.parseInt(array[i]));
			if (skills == null || skills.getClassType() == -1) {
				System.out.println(String.format("[AiLoader] NOT_USE_ACTIVE_SKILL : ACTIVE_ID(%s)", array[i]));
				continue;
			}
			list.add(skills);
		}
		L1Skills[] skill		= new L1Skills[list.size()];
		for (int i=0; i<skill.length; i++) {
			skill[i] = list.get(i);
		}
		return skill;
	}
	
	L1PassiveSkills[] parsePassiveSkill(String[] array){
		L1PassiveSkills passives		= null;
		ArrayList<L1PassiveSkills> list = new ArrayList<L1PassiveSkills>();
		for (int i=0; i<array.length; i++) {
			passives = SkillsTable.getPassiveTemplate(Integer.parseInt(array[i]));
			if (passives == null || passives.getClassType() == -1) {
				System.out.println(String.format("[AiLoader] NOT_USE_PASSIVE_SKILL : PASSIVE_ID(%s)", array[i]));
				continue;
			}
			list.add(passives);
		}
		L1PassiveSkills[] passive	= new L1PassiveSkills[list.size()];
		for (int i=0; i<passive.length; i++) {
			passive[i] = list.get(i);
		}
		return passive;
	}
	
	/**
	 * ai 캐릭터 로드
	 * @param con
	 * @return
	 */
	FastMap<AiType, FastMap<String, FastTable<L1AiUserInstance>>> loadUser(Connection con){
		FastMap<AiType, FastMap<String, FastTable<L1AiUserInstance>>> users	= new FastMap<>();
		PreparedStatement pstm				= null;
		ResultSet rs						= null;
		L1AiUserInstance user				= null;
		AiType ai_type						= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM ai_user");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				user = new L1AiUserInstance();
				user.setId(IdFactory.getInstance().nextId());
				user.setAccountName(StringUtil.EmptyString);
				user.setName(rs.getString("name"));
				ai_type = AiType.fromString(rs.getString("ai_type"));
				user.setAiType(ai_type);
				
				int level = rs.getInt("level");
				user.setHighLevel(level);
				user.setLevel(level);
				user.setExp(ExpTable.getExpByLevel(level));
				
				String type = rs.getString("class");
				String genderString = rs.getString("gender");
				genderString = genderString.substring(genderString.indexOf("(") + 1, genderString.indexOf(")"));
				Gender gender = Gender.fromInt(Integer.parseInt(genderString));
				int classId	= L1CharacterInfo.getClassId(type, gender);
				user.setClassId(classId);
				user.setSpriteId(classId);
				user.setGender(gender);
				user.setType(L1Class.getType(type));
				
				statSetting(user, rs);
				user.resetBaseAc();
				user.setAlignment(rs.getInt("alignment"));
				user.getAbility().addShortHitup(rs.getInt("hit"));
				user.getAbility().addLongHitup(rs.getInt("bow_hit"));
				user.getAbility().addShortDmgup(rs.getInt("dmg"));
				user.getAbility().addLongDmgup(rs.getInt("bow_dmg"));
				user.getAbility().addDamageReduction(rs.getInt("reduction"));
				user.getResistance().addHitupSkill(rs.getInt("skill_hit"));
				user.getResistance().addHitupSpirit(rs.getInt("spirit_hit"));
				user.getResistance().addHitupDragon(rs.getInt("dragon_hit"));
				user.getResistance().addHitupFear(rs.getInt("fear_hit"));
				user.getAbility().addMagicHitup(rs.getInt("magic_hit"));
				user.getResistance().addToleranceSkill(rs.getInt("skill_regist"));
				user.getResistance().addToleranceSpirit(rs.getInt("spirit_regist"));
				user.getResistance().addToleranceDragon(rs.getInt("dragon_regist"));
				user.getResistance().addToleranceFear(rs.getInt("fear_regist"));
				user.getAbility().addDg(rs.getInt("dg"));
				user.getAbility().addEr(rs.getInt("er"));
				user.getAbility().addMe(rs.getInt("me"));
				user.getResistance().addMr(rs.getInt("mr"));
				
				int hp = rs.getInt("hp");
				int mp = rs.getInt("mp");
				user.addBaseMaxHp(hp);
				user.setCurrentHp(hp);
				user.addBaseMaxMp(mp);
				user.setCurrentMp(mp);
				
				user.addHpRegen(rs.getInt("hpr"));
				user.addMpRegen(rs.getInt("mpr"));

				user.setTitle(rs.getString("title"));
				user.setClanid(rs.getInt("clanId"));
				user.setClanName(rs.getString("clanname"));
				user.setAiPledge(AiPledge.fromString(user.getClanName()));
				user.setFood(GameServerSetting.MAX_FOOD_VALUE);
				user.setElfAttr(rs.getInt("elfAttr"));
				user.getLight().turnOnOffLight();
				user.setOnlineStatus(1);
				user.setNetConnection(null);
				
				learnSkillSetting(user);// 해당 클래스에 해당하는 스킬을 모두 배운다
				itemSetting(user);
				dropSetting(user);
				buffSetting(user);
				
				FastMap<String, FastTable<L1AiUserInstance>> type_map = users.get(ai_type);
				if (type_map == null) {
					type_map = new FastMap<String, FastTable<L1AiUserInstance>>();
					users.put(ai_type, type_map);
				}
				
				FastTable<L1AiUserInstance> list = type_map.get(user.getClanName());
				if (list == null) {
					list = new FastTable<L1AiUserInstance>();
					type_map.put(user.getClanName(), list);
				}
				list.add(user);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return users;
	}
	
	/**
	 * 스탯 설정
	 * @param user
	 * @param rs
	 * @throws SQLException
	 */
	void statSetting(L1AiUserInstance user, ResultSet rs) throws SQLException {
		// 베이스 스탯 설정
		switch(user.getType()){
		case 0:setBaseStat(user, 20, 11, 9, 11, 9, 11);break;
		case 1:setBaseStat(user, 20, 14, 12, 12, 8, 9);break;
		case 2:setBaseStat(user, 11, 13, 18, 9, 12, 12);break;
		case 3:setBaseStat(user, 8, 16, 7, 8, 18, 18);break;
		case 4:setBaseStat(user, 18, 12, 15, 9, 11, 10);break;
		case 5:setBaseStat(user, 18, 15, 11, 8, 11, 12);break;
		case 6:setBaseStat(user, 11, 17, 10, 8, 17, 12);break;
		case 7:setBaseStat(user, 20, 14, 12, 12, 8, 9);break;
		case 8:setBaseStat(user, 20, 15, 13, 5, 11, 11);break;
		case 9:setBaseStat(user, 20, 16, 12, 6, 9, 12);break;
		}
		// 추가 스탯 설정
		user.getAbility().setStr(rs.getInt("str"));
		user.getAbility().setCon(rs.getInt("con"));
		user.getAbility().setDex(rs.getInt("dex"));
		user.getAbility().setInt(rs.getInt("inti"));
		user.getAbility().setWis(rs.getInt("wis"));
		user.getAbility().setCha(rs.getInt("cha"));
	}
	
	/**
	 * 베이스 스탯 지정
	 * @param user
	 * @param str
	 * @param con
	 * @param dex
	 * @param cha
	 * @param inti
	 * @param wis
	 */
	void setBaseStat(L1AiUserInstance user, int str, int con, int dex, int cha, int inti, int wis){
		user.getAbility().setBaseStr(str);
		user.getAbility().setBaseCon(con);
		user.getAbility().setBaseDex(dex);
		user.getAbility().setBaseCha(cha);
		user.getAbility().setBaseInt(inti);
		user.getAbility().setBaseWis(wis);
	}
	
	/**
	 * 아이템 설정
	 * 슬롯을 모두 개방한다.
	 * @param user
	 */
	void itemSetting(L1AiUserInstance user) {
		// 모든 슬롯 개방
		user.setRingSlotLevel(4);
		user.setEarringSlotLevel(1);
		user.setBadgeSlotLevel(1);
		user.setShoulderSlotLevel(1);
		itemPut(user, _user_items.get(-1));// 전체 지급
		itemPut(user, _user_items.get(user.getType()));// 클래스 지급
	}
	
	/**
	 * 아이템 등록
	 * @param user
	 * @param itemList
	 */
	void itemPut(L1AiUserInstance user, FastTable<AiItemObject> itemList){
		if (itemList.isEmpty()) {
			return;
		}
		L1Item temp			= null;
		for (AiItemObject obj : itemList) {
			temp = ItemTable.getInstance().getTemplate(obj.getItemId());
			if (temp == null) {
				continue;
			}
			if (temp.isMerge()) {
				storeItem(user, temp, obj.getCount(), obj.getEnchantLevel(), obj.getAttrLevel(), obj.isEquip());
			} else {
				for (int i=0; i<obj.getCount(); i++) {
					storeItem(user, temp, 1, obj.getEnchantLevel(), obj.getAttrLevel(), obj.isEquip());
				}
			}
		}
	}
	
	void storeItem(L1AiUserInstance user, L1Item temp, int count, int enchant, int attr, boolean equip){
		L1ItemInstance item = ItemTable.getInstance().createItem(temp);
		item.setCount(count);
		item.setEnchantLevel(enchant);
		item.setAttrEnchantLevel(attr);
		user.getInventory().storeItem(item);

		if (equip) {
			user.getInventory().setEquipped(item, true);// 장착
		}
		
		switch (item.getItemId()) {
		case 140022:// 가벼운 신속 체력 회복제
			user.getAiHealPotion()[0] = item;
			break;
		case 140023:// 가벼운 신속 고급 체력 회복제
			user.getAiHealPotion()[1] = item;
			break;
		case 140024:// 가벼운 신속 강력 체력 회복제
			user.getAiHealPotion()[2] = item;
			break;
		case 40018:// 강화 속도 향상 물약
			user.setAiGreenPotion(item);
			break;
		case 41415:case 40068:case 210036:// 2단 가속 물약
			user.setAiBravePotion(item);
			break;
		default:break;
		}
		if (temp.getItemType() == L1ItemType.NORMAL && temp.get_interaction_type() == L1ItemType.MAGICDOLL.getInteractionType()) {
			user.setDollItem(item);// 인형 아이템
		}
	}
	
	/**
	 * 드랍리스트 등록
	 * @param user
	 */
	void dropSetting(L1AiUserInstance user){
		dropPut(user, _user_drops.get(-1));// 전체
		dropPut(user, _user_drops.get(user.getType()));// 클래스
	}
	
	void dropPut(L1AiUserInstance user, FastTable<AiDropObject> dropList){
		if (dropList.isEmpty()) {
			return;
		}
		for (AiDropObject obj : dropList) {
			if (CommonUtil.random(100) + 1 <= obj.getChance()) {
				user.addDropList(obj);
			}
		}
	}
	
	/**
	 * 스킬을 배운다
	 * @param user
	 */
	void learnSkillSetting(L1AiUserInstance user){
		AiSkillObject obj			= _user_skills.get(user.getType());
		List<Integer> activeList	= new ArrayList<>();
		List<Integer> passiveList	= new ArrayList<>();
		for (L1Skills active : obj.getActive()) {
			activeList.add(active.getSkillId());
		}
		for (L1PassiveSkills passive : obj.getPassive()) {
			passiveList.add(passive.getPassiveId());
			user.getPassiveSkill().set(passive.getPassive());
		}
		if (!activeList.isEmpty()) {
			user.getSkill().addLearnActives(activeList);
		}
		if (!passiveList.isEmpty()) {
			user.getSkill().addLearnPassives(passiveList);
		}
	}
	
	/**
	 * 버프 등록
	 * @param user
	 */
	void buffSetting(L1AiUserInstance user){
		for (AiBuffObject obj : _user_buffs) {
			if (obj.getType() == user.getType()) {
				if (user.isElf()) {
					if (user.getElfAttr() == obj.getElfAttr()) {// 속성별로 세팅
						user.setAiBuffs(obj.getBuffs());
					}
				} else {
					user.setAiBuffs(obj.getBuffs());
				}
			}
		}
	}
	
	public static void reload(){
		release();
		_instance = new AiLoader();
	}
	
	protected static void release(){
		for (FastTable<AiItemObject> list : _instance._user_items.values()) {
			list.clear();
		}
		_instance._user_items.clear();
		for (FastTable<AiDropObject> list : _instance._user_drops.values()) {
			list.clear();
		}
		_instance._user_drops.clear();
		for (AiSkillObject obj : _instance._user_skills.values()) {
			obj.release();
		}
		_instance._user_skills.clear();
		for (AiBuffObject obj : _instance._user_buffs) {
			obj.release();
		}
		_instance._user_buffs.clear();
		for (FastMap<String, FastTable<L1AiUserInstance>> type_map : _instance._users.values()) {
			for (FastTable<L1AiUserInstance> list : type_map.values()) {
				for (L1AiUserInstance ai : list) {
					ai.dispose();
				}
				list.clear();
			}
		}
		_instance._users.clear();
		for (FastTable<String> list : _instance._user_ments.values()) {
			list.clear();
		}
		_instance._user_ments.clear();
		_instance._fish_location.clear();
		_instance = null;
	}
}


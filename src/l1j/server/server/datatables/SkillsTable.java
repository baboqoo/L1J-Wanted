package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.PassiveSpellCommonBinLoader;
import l1j.server.common.bin.SpellCommonBinLoader;
import l1j.server.common.bin.spell.CommonSpellInfo;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.construct.L1Grade;
import l1j.server.server.construct.L1CharacterInfo.L1Class;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class SkillsTable {
	private static Logger _log = Logger.getLogger(SkillsTable.class.getName());

	private static SkillsTable _instance;

	private static final Map<Integer, L1Skills> SKILLS			= new HashMap<>();// KEY: skillId, VALUE: L1Skills
	private static final Map<Integer, L1PassiveSkills> PASSIVES	= new HashMap<>();// KEY: passiveId, VALUE: L1PassiveSkills
	
	private static final Map<Integer, FastTable<L1Skills>> CLASS_ACTIVES			= new HashMap<>();
	private static final Map<Integer, FastTable<L1PassiveSkills>> CLASS_PASSIVES	= new HashMap<>();
	
	private static final Map<L1Grade, ArrayList<L1Skills>> GRADE_ACTIVE_SPELLS			= new HashMap<>();
	private static final Map<L1Grade, ArrayList<L1PassiveSkills>> GRADE_PASSIVE_SPELLS	= new HashMap<>();
	
	/**
	 * 스킬 조사
	 * @param skillId
	 * @return L1Skills
	 */
	public static L1Skills getTemplate(int skillId) {
		return SKILLS.get(skillId);
	}
	
	public static Collection<L1Skills> getAllActiveTemplate() {
		return SKILLS.values();
	}
	
	/**
	 * 패시브 스킬 조사
	 * @param passiveId
	 * @return L1PassiveSkills
	 */
	public static L1PassiveSkills getPassiveTemplate(int passiveId) {
		return PASSIVES.get(passiveId);
	}
	
	public static Collection<L1PassiveSkills> getAllPassiveTemplate() {
		return PASSIVES.values();
	}
	
	/**
	 * 등급에 해당하는 엑티브 스킬리스트 조사
	 * @param grade
	 * @return ArrayList<L1Skills>
	 */
	public static ArrayList<L1Skills> getGradeActiveList(L1Grade grade) {
		return GRADE_ACTIVE_SPELLS.get(grade);
	}
	
	/**
	 * 등급에 해당하는 패시브 스킬리스트 조사
	 * @param grade
	 * @return ArrayList<L1PassiveSkills>
	 */
	public static ArrayList<L1PassiveSkills> getGradePassiveList(L1Grade grade) {
		return GRADE_PASSIVE_SPELLS.get(grade);
	}
	
	/**
	 * 스킬북 아이템 액티브 스킬 검증
	 * 완료시 스킬정보를 반환
	 * @param item
	 * @param classType
	 * @return L1Skills
	 */
	public static L1Skills getActiveToClassType(L1ItemInstance item, int classType) {
		String bookName = item.getItem().getDescKr().replaceAll(ItemTable.COLOR_REPLACE_STR, StringUtil.EmptyString);
		if (CLASS_ACTIVES.containsKey(L1Class.NORMAL.getType())) {
			for (L1Skills skill : CLASS_ACTIVES.get(L1Class.NORMAL.getType())) {
				if (bookName.matches(skill.getBookNameRegex())) {
					return skill;
				}
			}
		}
		if (CLASS_ACTIVES.containsKey(classType)) {
			for (L1Skills skill : CLASS_ACTIVES.get(classType)) {
				if (bookName.matches(skill.getBookNameRegex())) {
					return skill;
				}
			}
		}
		return null;
	}
	
	/**
	 * 스킬북 아이템 패시브 스킬 검증
	 * 완료시 스킬정보를 반환
	 * @param item
	 * @param classType
	 * @return L1PassiveSkills
	 */
	public static L1PassiveSkills getPassiveToClassType(L1ItemInstance item, int classType) {
		String bookName = item.getItem().getDescKr().replaceAll(ItemTable.COLOR_REPLACE_STR, StringUtil.EmptyString);
		//System.out.println("El libro es: " + item.getItem().getDescKr() + " con desc " +  item.getItem().getDesc() + " y con la mascara " + bookName);
		for (L1PassiveSkills passive : CLASS_PASSIVES.get(classType)) {
			//System.out.println("El skill es id: " + passive.getPassiveId() + " con name "  + passive.getName() + " y con mascara " + passive.getBookNameRegex());
			if (bookName.matches(passive.getBookNameRegex())) {
				return passive;
			}
		}
		return null;
	}
	
	/**
	 * 클래스가 배울수 있는 모든 스킬 조사
	 * @param classType
	 * @return List<L1Skills>
	 */
	public static List<L1Skills> getActiveClassTypeList(int classType){
		List<L1Skills> list = new ArrayList<>();
		if (CLASS_ACTIVES.containsKey(classType)) {
			for (L1Skills skill : CLASS_ACTIVES.get(classType)) {
				if (skill.getClassType() == classType) {
					//System.out.println("El skill es: " + skill.getSkillId());
					list.add(skill);
				}
			}
		}
		return list;
	}
	
	/**
	 * 클래스가 배울수 있는 모든 스킬 조사
	 * @param classType
	 * @return List<L1PassiveSkills>
	 */
	public static List<L1PassiveSkills> getPassiveClassTypeList(int classType){
		List<L1PassiveSkills> list = new ArrayList<>();
		if (CLASS_PASSIVES.containsKey(classType)) {
			for (L1PassiveSkills passive : CLASS_PASSIVES.get(classType)) {
				if (passive.getClassType() == classType) {
					list.add(passive);
				}
			}
		}
		return list;
	}

	private final boolean _initialized;

	public static SkillsTable getInstance() {
		if (_instance == null) {
			_instance = new SkillsTable();
		}
		return _instance;
	}

	private SkillsTable() {
		_initialized = true;
		loadSkills();
	}

	private void loadSkills() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM skills");
			rs		= pstm.executeQuery();
			FillActiveSkillsTable(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating skills table", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM skills_passive");
			rs		= pstm.executeQuery();
			FillPassiveSkillsTable(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating skills table", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void FillActiveSkillsTable(ResultSet rs) throws SQLException {
		L1Skills l1skills = null;
		while(rs.next()){
			int skill_id	= rs.getInt("skill_id");
			String name		= rs.getString("name");
			String desc_kr	= rs.getString("desc_kr");
			String desc_en	= rs.getString("desc_en");
			if (StringUtil.isNullOrEmpty(name) || name.equals("none")) {
				continue;
			}
			l1skills = new L1Skills();
			l1skills.setSkillId(skill_id);
			l1skills.setName(name);
			l1skills.setDescEn(desc_en);
			l1skills.setDescKr(desc_kr);
			
			l1skills.setSkillLevel(rs.getInt("skill_level"));
			l1skills.setMpConsume(rs.getInt("mpConsume"));
			l1skills.setHpConsume(rs.getInt("hpConsume"));
			l1skills.setItemConsumeId(rs.getInt("itemConsumeId"));
			l1skills.setItemConsumeCount(rs.getInt("itemConsumeCount"));
			
			l1skills.setReuseDelay(rs.getInt("reuseDelay"));
			l1skills.setDelayGroupId(rs.getInt("delayGroupId"));
			l1skills.setFixDelay(Boolean.valueOf(rs.getString("fixDelay")));
			
			l1skills.setBuffDuration(rs.getInt("buffDuration"));
			l1skills.setBuffDurationText(rs.getString("buffDuration_txt"));
			l1skills.setTarget(L1Skills.SKILL_TARGET.fromString(rs.getString("target")));
			l1skills.setTargetTo(L1Skills.SKILL_TARGET_TO.fromString(rs.getString("target_to")));
			l1skills.setTargetToText(rs.getString("target_to_txt"));
			l1skills.setEffectText(rs.getString("effect_txt"));
			l1skills.setDamageValue(rs.getInt("damage_value"));
			l1skills.setDamageDice(rs.getInt("damage_dice"));
			l1skills.setDamageDiceCount(rs.getInt("damage_dice_count"));
			l1skills.setProbabilityValue(rs.getInt("probability_value"));
			l1skills.setProbabilityDice(rs.getInt("probability_dice"));
			l1skills.setAttr(L1Attr.fromString(rs.getString("attr")));
			l1skills.setType(L1Skills.SKILL_TYPE.fromString(rs.getString("type")));
			l1skills.setAlignment(rs.getInt("alignment"));
			l1skills.setRanged(rs.getInt("ranged"));
			l1skills.setArea(rs.getInt("area"));
			l1skills.setIsThrough(Boolean.parseBoolean(rs.getString("is_through")));
			l1skills.setActionId(rs.getInt("action_id"));
			l1skills.setActionId2(rs.getInt("action_id2"));
			l1skills.setActionId3(rs.getInt("action_id3"));
			l1skills.setCastGfx(rs.getInt("castgfx"));
			l1skills.setCastGfx2(rs.getInt("castgfx2"));
			l1skills.setCastGfx3(rs.getInt("castgfx3"));
			l1skills.setSysmsgIdHappen(rs.getInt("sysmsgID_happen"));
			l1skills.setSysmsgIdStop(rs.getInt("sysmsgID_stop"));
			l1skills.setSysmsgIdFail(rs.getInt("sysmsgID_fail"));
			l1skills.setGrade(L1Grade.fromString(rs.getString("grade")));
			
			ArrayList<L1Skills> grade_list = GRADE_ACTIVE_SPELLS.get(l1skills.getGrade());
			if (grade_list == null) {
				grade_list = new ArrayList<L1Skills>();
				GRADE_ACTIVE_SPELLS.put(l1skills.getGrade(), grade_list);
			}
			grade_list.add(l1skills);
			
			l1skills.setClassType(L1Class.getType(rs.getString("classType")));
			if (l1skills.getClassType() != -1) {
				l1skills.setBookNameRegex(parseBookNameRegex(l1skills));
			}
			
			l1skills.setBin(SpellCommonBinLoader.getData(l1skills.getSkillId()));
			SKILLS.put(new Integer(skill_id), l1skills);
			
			if (l1skills.getClassType() != -1) {
				FastTable<L1Skills> classList = CLASS_ACTIVES.get(l1skills.getClassType());
				if (classList == null) {
					classList = new FastTable<L1Skills>();
					CLASS_ACTIVES.put(l1skills.getClassType(), classList);
				}
				classList.add(l1skills);
			}
		}
		//_log.config("액티브 스킬 " + SKILLS.size() + "건 로드");
		_log.config("Active Skills " + SKILLS.size() + " entries loaded");
	}
	
	private void FillPassiveSkillsTable(ResultSet rs) throws SQLException {
		L1PassiveSkills passiveSkill = null;
		while(rs.next()){
			int passiveId	= rs.getInt("passive_id");
			String name		= rs.getString("name");
			String desc_kr	= rs.getString("desc_kr");
			String desc_en	= rs.getString("desc_en");
			if (StringUtil.isNullOrEmpty(name) || name.equals("none")) {
				continue;
			}
			int duration				= rs.getInt("duration");
			int onIconId				= rs.getInt("on_icon_id");
			int tooltipStrId			= rs.getInt("tooltip_str_id");
			boolean isGood				= Boolean.parseBoolean(rs.getString("is_good"));
			int classType				= L1Class.getType(rs.getString("class_type"));
			int backActiveSkillId		= rs.getInt("back_active_skill_id");
			L1Skills backActive			= SKILLS.get(backActiveSkillId);
			int backPassiveId			= rs.getInt("back_passive_id");
			L1PassiveSkills backPassive	= PASSIVES.get(backPassiveId);
			L1Grade grade 				= L1Grade.fromString(rs.getString("grade"));
			L1PassiveId passive			= L1PassiveId.fromInt(passiveId);
			CommonSpellInfo bin			= PassiveSpellCommonBinLoader.getData(passiveId);
			String bookNameRegex		= null;
			if (classType != -1) {
				//bookNameRegex			= parseBookNameRegex(classType, name);
				bookNameRegex			= parseBookNameRegex(classType, desc_kr);
			}
			passiveSkill = new L1PassiveSkills(
					passiveId, name, desc_en, duration, onIconId, tooltipStrId, isGood, classType, 
					backActive, backPassive, bookNameRegex, passive, grade, bin);
			
			PASSIVES.put(passiveSkill.getPassiveId(), passiveSkill);
			
			if (classType != -1) {
				FastTable<L1PassiveSkills> classList = CLASS_PASSIVES.get(classType);
				if (classList == null) {
					classList = new FastTable<L1PassiveSkills>();
					CLASS_PASSIVES.put(classType, classList);
				}
				classList.add(passiveSkill);
			}
			
			ArrayList<L1PassiveSkills> grade_list = GRADE_PASSIVE_SPELLS.get(passiveSkill.getGrade());
			if (grade_list == null) {
				grade_list = new ArrayList<L1PassiveSkills>();
				GRADE_PASSIVE_SPELLS.put(passiveSkill.getGrade(), grade_list);
			}
			grade_list.add(passiveSkill);
			
			if (backActive != null) {
				backActive.setAfterPassive(passiveSkill);
			}
		}
		//_log.config("패시브 스킬 " + PASSIVES.size() + "건 로드");
		_log.config("Passive Skills " + PASSIVES.size() + "Gun Load");
	}

	public boolean isInitialized() {
		return _initialized;
	}
	
	private static final String[] CLASS_BOOK_STRING = { 
		"마법서 (", "기술서 (", "정령의 수정 (", "마법서 (", "흑정령의 수정 (", "용기사의 서판 (", "기억의 수정 (", "전사의 인장 (", "검사의 서 (", "창술서 (" 
	};
	private static final String BACK_BOOK_STRING = ")";
	private static final String GACKIN_BOOK_STRING = "(각인)";
	private static final String GACKIN2_BOOK_STRING = " (각인)";
	
	private String parseBookNameRegex(L1Skills skill){
		StringBuilder sb	= new StringBuilder();
		String bookDefault	= sb.append(getClassSpellName(skill.getClassType()))
				//.append(skill.getName())
				.append(skill.getDescKr())
				.append(BACK_BOOK_STRING).toString();
		sb.setLength(0);
		String regex		= sb.append(bookDefault)
				.append("|").append(bookDefault).append(GACKIN_BOOK_STRING)
				.append("|").append(bookDefault).append(GACKIN2_BOOK_STRING).toString();
		return regex.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
	}
	
	private String parseBookNameRegex(int classType, String name){
		StringBuilder sb	= new StringBuilder();
		String bookDefault	= sb.append(getClassSpellName(classType))
				.append(name)
				.append(BACK_BOOK_STRING).toString();
		sb.setLength(0);
		String regex		= sb.append(bookDefault)
				.append("|").append(bookDefault).append(GACKIN_BOOK_STRING)
				.append("|").append(bookDefault).append(GACKIN2_BOOK_STRING).toString();
		return regex.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
	}
	
	private String getClassSpellName(int type){
		switch(type){
		case 99:return CLASS_BOOK_STRING[0];
		case 0:	return CLASS_BOOK_STRING[type];
		case 1:	return CLASS_BOOK_STRING[type];
		case 2:	return CLASS_BOOK_STRING[type];
		case 3:	return CLASS_BOOK_STRING[type];
		case 4:	return CLASS_BOOK_STRING[type];
		case 5:	return CLASS_BOOK_STRING[type];
		case 6:	return CLASS_BOOK_STRING[type];
		case 7:	return CLASS_BOOK_STRING[type];
		case 8:	return CLASS_BOOK_STRING[type];
		case 9:	return CLASS_BOOK_STRING[type];
		default:return null;
		}
	}
	
	public static void reload() {
		SKILLS.clear();
		PASSIVES.clear();
		CLASS_ACTIVES.clear();
		CLASS_PASSIVES.clear();
		GRADE_ACTIVE_SPELLS.clear();
		GRADE_PASSIVE_SPELLS.clear();
		_instance.loadSkills();
	}
}


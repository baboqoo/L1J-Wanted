package l1j.server.server.templates;

import l1j.server.common.bin.spell.CommonSpellInfo;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.construct.L1Grade;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class L1Skills {
	
	public static enum SKILL_TARGET {
		NONE,
		ATTACK,
		BUFF,
		;
		public static L1Skills.SKILL_TARGET fromString(String val) {
			switch (val) {
			case "NONE":
				return NONE;
			case "ATTACK":
				return ATTACK;
			case "BUFF":
				return BUFF;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SKILL_TARGET, %s", val));
			}
		}
	}
	
	public static enum SKILL_TARGET_TO {
		ME,
		PC,
		NPC,
		ALL,
		PLEDGE,
		PARTY,
		COMPANIION,
		PLACE,
		;
		public static L1Skills.SKILL_TARGET_TO fromString(String val) {
			switch(val) {
			case "ME":
				return ME;
			case "PC":
				return PC;
			case "NPC":
				return NPC;
			case "ALL":
				return ALL;
			case "PLEDGE":
				return PLEDGE;
			case "PARTY":
				return PARTY;
			case "COMPANIION":
				return COMPANIION;
			case "PLACE":
				return PLACE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SKILL_TARGET_TO, %s", val));
			}
		}
	}
	
	public static enum SKILL_TYPE {
		NONE,
		PROB,
		CHANGE,
		CURSE,
		DEATH,
		HEAL,
		RESTORE,
		ATTACK,
		OTHER,
		;
		public static L1Skills.SKILL_TYPE fromString(String val) {
			switch(val) {
			case "NONE":
				return NONE;
			case "PROB":
				return PROB;
			case "CHANGE":
				return CHANGE;
			case "CURSE":
				return CURSE;
			case "DEATH":
				return DEATH;
			case "HEAL":
				return HEAL;
			case "RESTORE":
				return RESTORE;
			case "ATTACK":
				return ATTACK;
			case "OTHER":
				return OTHER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SKILL_TYPE, %s", val));
			}
		}
	}
	
	private String _name;
	private String _desc_kr;
	private String _desc_en;
	private L1Skills.SKILL_TARGET _target;
	private L1Skills.SKILL_TARGET_TO _targetTo;
	private int _skillId;
	private int _skillLevel;
	private int _damageValue;
	private int _damageDice;
	private int _damageDiceCount;
	private int _probabilityValue;
	private int _probabilityDice;
	private L1Attr _attr;
	private L1Skills.SKILL_TYPE _type;
	private int _ranged;
	private int _area;
	private boolean _isThrough;
	private int _actionId;
	private int _actionId2;
	private int _actionId3;
	private int _castGfx;
	private int _castGfx2;
	private int _castGfx3;
	private int _sysmsgIdHappen;
	private int _sysmsgIdStop;
	private int _sysmsgIdFail;
	private int _alignment;
	private int _mpConsume;
	private int _hpConsume;
	private int _itmeConsumeId;
	private int _itmeConsumeCount;
	private int _reuseDelay;
	private boolean _fixDelay;
	private int _delayGroupId;
	private int _buffDuration;
	private int _classType;// 사용 클래스타입
	private String _bookNameRegex;
	private L1PassiveSkills _afterPassive;
	private L1SkillActionHandler _handler;
	private L1SkillsInfo _info;
	private CommonSpellInfo _bin;
	private L1Grade _grade;
	private String _buffDurationText;
	private String _targetToText;
	private String _effectText;

	public int getSkillId() {
		return _skillId;
	}
	public void setSkillId(int val) {
		_skillId = val;
	}

	public String getName() {
		return _name;
	}
	public void setName(String val) {
		_name = val;
	}

	public String getDescKr() {
		return _desc_kr;
	}
	public void setDescKr(String val) {
		_desc_kr = val;
	}

	public String getDescEn() {
		return _desc_en;
	}
	public void setDescEn(String val) {
		_desc_en = val;
	}

	public int getSkillLevel() {
		return _skillLevel;
	}
	public void setSkillLevel(int val) {
		_skillLevel = val;
	}

	public int getMpConsume() {
		return _mpConsume;
	}
	public void setMpConsume(int val) {
		_mpConsume = val;
	}

	public int getHpConsume() {
		return _hpConsume;
	}
	public void setHpConsume(int val) {
		_hpConsume = val;
	}

	public int getItemConsumeId() {
		return _itmeConsumeId;
	}
	public void setItemConsumeId(int val) {
		_itmeConsumeId = val;
	}

	public int getItemConsumeCount() {
		return _itmeConsumeCount;
	}
	public void setItemConsumeCount(int val) {
		_itmeConsumeCount = val;
	}

	public int getReuseDelay() {
		return _reuseDelay;
	}
	public void setReuseDelay(int val) {
		_reuseDelay = val;
	}
	
	public int getDelayGroupId() {
		return _delayGroupId;
	}
	public void setDelayGroupId(int val) {
		_delayGroupId = val;
	}
	
	public boolean getFixDelay() {
		return _fixDelay;
	}
	public void setFixDelay(boolean val) {
		_fixDelay = val;
	}

	public int getBuffDuration() {
		return _buffDuration;
	}
	public void setBuffDuration(int val) {
		_buffDuration = val;
	}

	public L1Skills.SKILL_TARGET getTarget() {
		return _target;
	}
	public void setTarget(L1Skills.SKILL_TARGET val) {
		_target = val;
	}

	public L1Skills.SKILL_TARGET_TO getTargetTo() {
		return _targetTo;
	}
	public void setTargetTo(L1Skills.SKILL_TARGET_TO val) {
		_targetTo = val;
	}

	public int getDamageValue() {
		return _damageValue;
	}
	public void setDamageValue(int val) {
		_damageValue = val;
	}

	public int getDamageDice() {
		return _damageDice;
	}
	public void setDamageDice(int val) {
		_damageDice = val;
	}

	public int getDamageDiceCount() {
		return _damageDiceCount;
	}
	public void setDamageDiceCount(int val) {
		_damageDiceCount = val;
	}

	public int getProbabilityValue() {
		return _probabilityValue;
	}
	public void setProbabilityValue(int val) {
		_probabilityValue = val;
	}

	public int getProbabilityDice() {
		return _probabilityDice;
	}
	public void setProbabilityDice(int val) {
		_probabilityDice = val;
	}

	public L1Attr getAttr() {
		return _attr;
	}
	public void setAttr(L1Attr val) {
		_attr = val;
	}

	public L1Skills.SKILL_TYPE getType() {
		return _type;
	}
	public void setType(L1Skills.SKILL_TYPE val) {
		_type = val;
	}

	public int getAlignment() {
		return _alignment;
	}
	public void setAlignment(int val) {
		_alignment = val;
	}

	public int getRanged() {
		return _ranged;
	}
	public void setRanged(int val) {
		_ranged = val;
	}

	public int getArea() {
		return _area;
	}
	public void setArea(int val) {
		_area = val;
	}

	public boolean getIsThrough() {
		return _isThrough;
	}
	public void setIsThrough(boolean val) {
		_isThrough = val;
	}

	public int getActionId() {
		return _actionId;
	}
	public void setActionId(int val) {
		_actionId = val;
	}
	public int getActionId2() {
		return _actionId2;
	}
	public void setActionId2(int val) {
		_actionId2 = val;
	}
	public int getActionId3(){
		return _actionId3;
	}
	public void setActionId3(int val) {
		_actionId3 = val;
	}

	public int getCastGfx() {
		return _castGfx;
	}
	public void setCastGfx(int val) {
		_castGfx = val;
	}
	public int getCastGfx2() {
		return _castGfx2;
	}
	public void setCastGfx2(int val) {
		_castGfx2 = val;
	}
	public int getCastGfx3() {
		return _castGfx3;
	}
	public void setCastGfx3(int val) {
		_castGfx3 = val;
	}

	public int getSysmsgIdHappen() {
		return _sysmsgIdHappen;
	}
	public void setSysmsgIdHappen(int val) {
		_sysmsgIdHappen = val;
	}

	public int getSysmsgIdStop() {
		return _sysmsgIdStop;
	}
	public void setSysmsgIdStop(int val) {
		_sysmsgIdStop = val;
	}

	public int getSysmsgIdFail() {
		return _sysmsgIdFail;
	}
	public void setSysmsgIdFail(int val) {
		_sysmsgIdFail = val;
	}
	
	public int getClassType(){
		return _classType;
	}
	public void setClassType(int val){
		_classType = val;
	}
	
	public String getBookNameRegex(){
		return _bookNameRegex;
	}
	public void setBookNameRegex(String val){
		_bookNameRegex = val;
	}
	
	public L1PassiveSkills getAfterPassive() {
		return _afterPassive;
	}
	public void setAfterPassive(L1PassiveSkills val) {
		_afterPassive = val;
	}
	
	public L1SkillActionHandler getHandler() {
		return _handler;
	}
	public void setHandler(L1SkillActionHandler val) {
		this._handler = val;
	}
	
	public L1SkillsInfo getInfo() {
		return _info;
	}
	public void setInfo(L1SkillsInfo val) {
		this._info = val;
	}
	
	public CommonSpellInfo getBin() {
		return _bin;
	}
	public void setBin(CommonSpellInfo val) {
		_bin = val;
	}
	
	public L1Grade getGrade() {
		return _grade;
	}
	public void setGrade(L1Grade val) {
		_grade = val;
	}

	public String getBuffDurationText() {
		return _buffDurationText;
	}
	public void setBuffDurationText(String val) {
		_buffDurationText = val;
	}

	public String getTargetToText() {
		return _targetToText;
	}
	public void setTargetToText(String val) {
		_targetToText = val;
	}

	public String getEffectText() {
		return _effectText;
	}
	public void setEffectText(String val) {
		_effectText = val;
	}

}


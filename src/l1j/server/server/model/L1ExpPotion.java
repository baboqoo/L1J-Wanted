package l1j.server.server.model;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.skill.L1SkillId;

/**
 * 경험치 물약 핸들러
 * @author LinOffice
 */
public class L1ExpPotion {
	protected L1PcInstance _owner;
	protected int _potionId;
	protected int _skillId = -1;
	protected ExpPotionType _type;
	protected boolean _stop;
	
	public L1ExpPotion(L1PcInstance owner) {
		_owner = owner;
	}
	
	public int getPotionId() {
		return _potionId;
	}
	public int getSkillId() {
		return _skillId;
	}
	public boolean isStop() {
		return _stop;
	}
	public void setStop(boolean val) {
		_stop = val;
	}
	
	/**
	 * 보너스 경험치 수치 조사
	 * @return 보너스 수치
	 */
	public int getExpBonusValue() {
		if (_type == null || _stop) {
			return 0;
		}
		switch (_type) {
 		case DRAGON:
 			return L1ExpPlayer.getExpPotion3Value(_owner.getLevel());
 		case DEATH_KNIGHT:
 			return _owner.isPCCafe() ? 93 : 70;
 		case BLESS:
 			return _owner.isPCCafe() ? 100 : 80;
 		case HERO:case EIN:
 			return 30;
 		case NORMAL:case NORMAL_21:
 			return 20;
 		case LEGEND_21:
 			return L1ExpPlayer.getExpPotion6Value(_owner.getLevel());
 		case IMPROVE:
 			return _owner.isPCCafe() ? 100 : 80;
 		default:
 			return 0;
 		}
	}
	
	/**
	 * 아인하사드 상태의 경험치 보너스 조사
	 * @return 보너스 경험치
	 */
	public int getEinhasadExpBonusValue() {
		if (_type == null || _stop) {
			return 0;
		}
		if (_type == ExpPotionType.LEGEND_21) {
			return 20;
		}
		if (_type == ExpPotionType.EIN) {
			return 30;
		}
		return 0;
	}
	
	/**
	 * 경험치 물약 사용시 설정한다.
	 * @param skillId
	 */
	public void init(int skillId) {
		_type			= ExpPotionType.fromSkillId(skillId);
		_potionId		= _type._value;
		_skillId		= skillId;
		_stop			= true;
	}
	
	/**
	 * 경험치 물약 종료시 해제한다.
	 */
	public void reset() {
		_type			= null;
		_potionId		= 0;
		_skillId		= -1;
		_stop			= false;
	}
	
	public static enum ExpPotionType {
		NORMAL(			1),		// 성장의 물약
		BLESS(			6),		// 빛나는 성장의 물약
		DRAGON(			9),		// 드래곤의 성장 물약
		DEATH_KNIGHT(	10),	// 진 데스타이트의 성장 물약
		HERO(			11),	// 영웅의 성장의 물약
		LEGEND_21(		12),	// 21주년 전설 성장 물약
		NORMAL_21(		13),	// 21주년 성장 물약
		EIN(			14),	// 아인하사드의 성장 물약
		IMPROVE(		143),	// 향상된 성장의 물약
		;
		private int _value;
		ExpPotionType(int val) {
			_value		= val;
		}
		public static ExpPotionType fromSkillId(int skillId){
			switch (skillId) {
			case L1SkillId.EXP_POTION:
				return NORMAL;
			case L1SkillId.EXP_POTION1:
				return BLESS;
			case L1SkillId.EXP_POTION3:
				return DRAGON;
			case L1SkillId.EXP_POTION2:
				return DEATH_KNIGHT;
			case L1SkillId.EXP_POTION4:
				return HERO;
			case L1SkillId.EXP_POTION6:
				return LEGEND_21;
			case L1SkillId.EXP_POTION5:
				return NORMAL_21;
			case L1SkillId.EXP_POTION7:
				return EIN;
			case L1SkillId.EXP_POTION8:
				return IMPROVE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ExpPotionType, %d", skillId));
			}
		}
	}
}


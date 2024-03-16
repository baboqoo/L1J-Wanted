package l1j.server.GameSystem.ai.brain.action;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.ArrayList;

import l1j.server.GameSystem.ai.brain.AiBrainHandler;
import l1j.server.GameSystem.ai.constuct.AiBrainStatus;
import l1j.server.GameSystem.astar.World;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.model.sprite.AcceleratorChecker;
import l1j.server.server.templates.L1Skills;

/**
 * AI 공격 담당 핸들러
 * @author LinOffice
 */
public class Attack extends AiBrainHandler {
	public Attack(L1AiUserInstance ai) {
		super(ai);
	}
	
	@Override
	protected void action() throws Exception {
		if (_ai == null) {
			return;
		}
		if (_ai.getTargetList().toTargetArrayList().isEmpty()) {
			_ai.searchTarget();// 타겟 검색
		}
		L1Character target = _ai.getAiTarget();
		if (target == null) {// 타겟 없음
			target = changeTarget(target);
		}
		if (target != null && target.isDead()) {// 타겟 다이
			target = changeTarget(target);
		}
        if (!_ai.isAttack(target)) {// 공격 불가
        	target = changeTarget(target);
        }
        if (target == null) {
        	changeStatus();
        	return;
        }
        onTarget(target);
	}
	
	/**
	 * 타겟 변경
	 * @param target
	 * @return L1Character
	 */
	L1Character changeTarget(L1Character target){
		if (target != null) {
			_ai.removeTargetList(target);
		}
		_ai.setAiTarget(_ai.getTarget());// 타겟 세팅
		return _ai.getAiTarget();
	}
	
	/**
	 * 상태 변경
	 */
	void changeStatus(){
		_ai.searchTarget();// 타겟 검색
    	if (!_ai.getTargetItemList().isEmpty()) {
    		_ai.setAiBrainStatus(AiBrainStatus.PICK_UP);
        	return;
    	}
    	_ai.setAiBrainStatus(AiBrainStatus.MOVE);
	}
	
	/**
	 * 타겟 성공
	 * @param target
	 */
	void onTarget(L1Character target){
		if (!_ai.glanceCheck(_ai.getAttackRange(), target.getX(), target.getY(), false)
				|| !target.glanceCheck(_ai.getAttackRange(),_ai.getX(), _ai.getY(), false)) {
			toMove(target);
			return;
		}
		if (_ai.getCurrentMp() > 30
				&& (int)(Math.random() * 10) + 1 <= 3
				&& toAttackSkill(target)) {
			_ai.setAiSleepTime(_ai.getAcceleratorChecker().getRightInterval(AcceleratorChecker.ACT_TYPE.SPELL_DIR) + 20);
    		return;
		}
        if (_ai.isDistance(_ai.getX(), _ai.getY(), _ai.getMapId(), target.getX(), target.getY(), target.getMapId(), _ai.getAttackRange()) 
        		&& _ai.getLocation().getTileLineDistance(target.getLocation()) <= _ai.getAttackRange() + 1 
        		&& target.getLocation().getTileLineDistance(target.getLocation()) <= 1
        		&& toAttack(target)) {
        	_ai.setAiSleepTime(_ai.getAcceleratorChecker().getRightInterval(AcceleratorChecker.ACT_TYPE.ATTACK) + 10);
        	return;
        }
        toMove(target);
	}
	
	/**
	 * 타겟에게 이동
	 * @param target
	 */
	void toMove(L1Character target){
		int dir			= _ai.getDir(target.getX(), target.getY());
        boolean tail	= World.isThroughObject(_ai.getX(), _ai.getY(), _ai.getMapId(), dir);
        boolean door	= World.isDoorMove(_ai.getX(), _ai.getY(), _ai.getMapId(), dir);
    	if (!tail || door || dir == -1) {
    		_ai.removeTargetList(target);
    		_ai.setAiTarget(null);
    		return;
    	}
    	_ai.setDirectionMove(dir);
	}
	
	/**
	 * 타겟에게 일반 공격
	 * @param target
	 * @return boolean
	 */
	boolean toAttack(L1Character target) {// 일반 공격
    	try {
    		target.onAction(_ai);
    		return true;
    	} catch (Exception e) {
    		_ai.setAiTarget(null);
    		_ai.getTargetList().clear();
    	}
    	return false;
    }
	
	/**
	 * 타겟에게 스킬 공격
	 * @param target
	 * @return boolean
	 */
	boolean toAttackSkill(L1Character target) {// 스킬 공격
		return target instanceof L1PcInstance ? toAttackSkillPVP(target) : toAttackSkillPVE(target);
	}
	
	/**
	 * PVP 스킬 공격
	 * @param target
	 * @return boolean
	 */
	boolean toAttackSkillPVP(L1Character target) {
		if (_ai.isWizard() && isHeal()) {
			return true;
		}
		int skillId	= -1;
		int subChance = (int)(Math.random() * 10) + 1;
		switch (_ai.getType()) {
		case 0:// 군주
			if (subChance <= 2) {
				skillId = TYRANT;
			} else if (subChance <= 4 && !target.isStun()) {
				skillId = EMPIRE;
			}
			break;
		case 1:// 기사
			if (subChance <= 2 && !target.isShockAttack()) {
				skillId = SHOCK_ATTACK;
			} else if (subChance <= 4 && !target.isStun()) {
				skillId = SHOCK_STUN;
			}
			break;
		case 2:// 요정
			if (_ai.getElfAttr() == 8 && subChance <= 2 && !target.getSkill().hasSkillEffect(STRIKER_GALE)) {
				skillId = STRIKER_GALE;
			} else if (_ai.getElfAttr() == 4 && subChance <= 2 && !target.getSkill().hasSkillEffect(POLLUTE_WATER)) {
				skillId = POLLUTE_WATER;
			} else {
				skillId = TRIPLE_ARROW;
			}
			break;
		case 3:// 법사
			if (subChance <= 2 && !target.getSkill().hasSkillEffect(STATUS_ETERNITY)) {
				skillId = ETERNITY;
			} else if (subChance <= 3 && !target.getSkill().hasSkillEffect(STATUS_DISINTEGRATE_NEMESIS)) {
				skillId = DISINTEGRATE;
			} else {
				skillId = ICE_SPIKE;
			}
			break;
		case 4:// 다크엘프
			if (subChance <= 2 && !target.getSkill().hasSkillEffect(ARMOR_BREAK)) {
				skillId = ARMOR_BREAK;
			} else if (subChance <= 4 && !target.isShadowStep()) {
				skillId = SHADOW_STEP;
			}
			break;
		case 5:// 용기사
			if (subChance <= 2 && !target.getSkill().hasSkillEffect(THUNDER_GRAB)) {
				skillId = THUNDER_GRAB;
			} else if (subChance <= 4 && !target.getSkill().hasSkillEffect(DESTROY)) {
				skillId = DESTROY;
			} else {
				skillId = FOU_SLAYER;
			}
			break;
		case 6:// 환술사
			if (subChance <= 2 && !target.getSkill().hasSkillEffect(PANIC)) {
				skillId = PANIC;
			} else if (subChance <= 3 && !target.getSkill().hasSkillEffect(ENSNARE)) {
				skillId = ENSNARE;
			} else if (subChance <= 5 && !target.isStun()) {
				skillId = BONE_BREAK;
			}
			break;
		case 7:// 전사
			if (subChance <= 2 && !target.isDesperado()) {
				skillId = DESPERADO;
			} else if (subChance <= 4 && !target.getSkill().hasSkillEffect(POWER_GRIP)) {
				skillId = POWER_GRIP;
			} else if(subChance <= 5 && !target.getSkill().hasSkillEffect(STATUS_TOMAHAWK_HUNT)) {
				skillId = TOMAHAWK;
			}
			break;
		case 8:// 검사
			if (subChance <= 2 && !target.getSkill().hasSkillEffect(JUDGEMENT)) {
				skillId = JUDGEMENT;
			} else if (subChance <= 3 && !target.isPhantom()) {
				skillId = PHANTOM;
			} else if (subChance <= 5 && !target.isStun()) {
				skillId = PANTERA;
			}
			break;
		case 9:// 창기사
			if (subChance <= 3 
					&& !target.getSkill().hasSkillEffect(PRESSURE) 
					&& !target.getSkill().hasSkillEffect(STATUS_PRESSURE_DEATH_RECAL)) {
				skillId = PRESSURE;
			} else if (subChance <= 5 && !target.isStun()) {
				skillId = CRUEL;
			}
			break;
		default:return false;
		}
		if (skillId != -1) {
			return useSkill(target, skillId);
		}
		return false;
	}
	
	/**
	 * PVE 스킬 공격
	 * @param target
	 * @return boolean
	 */
	boolean toAttackSkillPVE(L1Character target) {
		int skillId	= -1;
		switch(_ai.getType()){
		case 2:skillId = TRIPLE_ARROW;break;
		case 3:
			int hpPercent = _ai.getCurrentHpPercent();
    		int mpPercent = _ai.getCurrentMpPercent();
    		if (hpPercent > 80 && mpPercent < 80 && target.getCurrentMp() > 5) {
    			skillId = MANA_DRAIN;
    		} else if (((L1MonsterInstance) target).getNpcTemplate().isTurnUndead()) {
    			skillId = TURN_UNDEAD;
    		} else if (hpPercent < 80) {
    			skillId = VAMPIRIC_TOUCH;
    		} else {
    			skillId = ERUPTION;
    		}
			break;
		case 5:skillId = FOU_SLAYER;break;
		default:return false;
		}
		if (skillId != -1) {
			return useSkill(target, skillId);
		}
		return false;
    }
	
	/**
	 * 혈맹원 힐
	 * @return boolean
	 */
	boolean isHeal(){
		ArrayList<L1PcInstance> partyMembers = L1World.getInstance().getVisiblePartyPlayer(_ai, 6);
		if (partyMembers == null || partyMembers.isEmpty()) {
			return false;
		}
		L1PcInstance member = null;
		for (L1PcInstance memberPc : partyMembers) {
			if (memberPc == null || memberPc == _ai || !_ai.glanceCheck(6, memberPc.getX(), memberPc.getY(), false)) {
				continue;
			}
			if (((L1AiUserInstance)memberPc).isHelp()) {
				member = memberPc;
				break;
			}
		}
		if (member != null && useSkill(member, FULL_HEAL)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 스킬 시전
	 * @param target
	 * @param skillId
	 * @return boolean
	 */
	boolean useSkill(L1Character target, int skillId){
		L1Skills skill = SkillsTable.getTemplate(skillId);
		if (skill == null || _ai.getSkill().isSkillDelay(skill.getDelayGroupId())) {
			return false;// 스킬 딜레이 체크
		}
		int skillRange = skill.getRanged();
		if (skillRange < 0) {
			skillRange = 15;
		}
		if (_ai.getLocation().getLineDistance(target.getLocation()) > skillRange) {
			return false;// 타겟이 이상한 장소에 있으면 종료
		}
		if (_ai.getLocation().getTileLineDistance(target.getLocation()) > skillRange + 1 
				|| target.getLocation().getTileLineDistance(target.getLocation()) > 1) {
			return false;
		}
		L1SkillUse skilluse = new L1SkillUse(true);
		skilluse.handleCommands(_ai, skillId, target.getId(), target.getX(), target.getY(), 0, L1SkillUseType.NORMAL);
		skilluse = null;
		return true;
	}
}


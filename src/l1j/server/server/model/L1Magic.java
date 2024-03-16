package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import l1j.server.Config;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.construct.L1Undead;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.BalanceTable;
import l1j.server.server.datatables.MapBalanceTable.BalanceType;
import l1j.server.server.datatables.MapBalanceTable.MapBalanceData;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpellProbabilityTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1PassiveSkillHandler.TitanType;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CalcIntelStat;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;

public class L1Magic {
	private static final Random random = new Random(System.nanoTime());
	
	private static final List<Integer> NOT_ENABLE_NPC_IDS = Arrays.asList(new Integer[] {
			7800000, 7800007, 7800064, 7800056, 7800300
    });
	
	private int _calcType;
	private static final int PC_PC		= 1;
	private static final int PC_NPC		= 2;
	private static final int NPC_PC		= 3;
	private static final int NPC_NPC	= 4;

	private L1Character _attacker;
	private L1Character _target;
	private L1PcInstance _pc;
	private L1PcInstance _targetPc;
	private L1NpcInstance _npc;
	private L1NpcInstance _targetNpc;
	
	private int _attackerLevel, _targetLevel;
	
	private int _leverage = 10;
	private boolean _criticalDamage;
	private boolean _damageHit = true;
	
	public boolean isCriticalDamage() {
		return _criticalDamage;
	}

	public void setLeverage(int val) {
		_leverage = val;
	}
	
	public boolean isDamageHit(){
		return _damageHit;
	}

	public L1Magic(L1Character attacker, L1Character target) {
		_attacker		= attacker;
		_target			= target;
		_attackerLevel	= attacker.getLevel();
		_targetLevel	= target.getLevel();
		if (attacker instanceof L1PcInstance) {
			if (target instanceof L1PcInstance) {
				_calcType	= PC_PC;
				_pc			= (L1PcInstance) attacker;
				_targetPc	= (L1PcInstance) target;
			} else {
				_calcType	= PC_NPC;
				_pc			= (L1PcInstance) attacker;
				_targetNpc	= (L1NpcInstance) target;
			}
		} else {
			if (target instanceof L1PcInstance) {
				_calcType	= NPC_PC;
				_npc		= (L1NpcInstance) attacker;
				_targetPc	= (L1PcInstance) target;
			} else {
				_calcType	= NPC_NPC;
				_npc		= (L1NpcInstance) attacker;
				_targetNpc	= (L1NpcInstance) target;
			}
		}
	}

	int getSpellPower() {
		return _calcType == PC_PC || _calcType == PC_NPC ? _pc.getAbility().getSp() : _calcType == NPC_PC || _calcType == NPC_NPC ? _npc.getAbility().getSp() : 0;
	}

	int getMagicLevel() {
		return _calcType == PC_PC || _calcType == PC_NPC ?  _pc.getAbility().getMagicLevel() : _calcType == NPC_PC || _calcType == NPC_NPC ? _npc.getAbility().getMagicLevel() : 0;
	}

	int getMagicBonus() {
		return _calcType == PC_PC || _calcType == PC_NPC ? _pc.getAbility().getMagicBonus() : _calcType == NPC_PC || _calcType == NPC_NPC ? _npc.getAbility().getMagicBonus() : 0;
	}

	int getAlignment() {
		return _calcType == PC_PC || _calcType == PC_NPC ? _pc.getAlignment() : _calcType == NPC_PC || _calcType == NPC_NPC ? _npc.getAlignment() : 0;
	}

	int getTargetMr() {
		return _calcType == PC_PC || _calcType == NPC_PC ? _targetPc.getResistance().getEffectedMrBySkill() : _targetNpc.getResistance().getEffectedMrBySkill();
	}
	
	int getBalanceMagicHit(int attackerType, int targetType){
		return BalanceTable.getMagicHit(attackerType, targetType);
    }
    
    int getBalanceMagicDmg(int attackerType, int targetType){
    	return BalanceTable.getMagicDmg(attackerType, targetType);
    }

	/* ■■■■■■■■■■■■■■ 성공 판정 ■■■■■■■■■■■■■ */
	// ●●●● 확률계 마법의 성공 판정 ●●●●
	// 계산방법
	// 공격측 포인트：LV + ((MagicBonus * 3) * 마법 고유 계수)
	// 방어측 포인트：((LV / 2) + (MR * 3)) / 2
	// 공격 성공율：공격측 포인트 - 방어측 포인트
	/* ■■■■■■■■■■■■■■■ Success judgment ■■■■■■■■■■■■■ */
	// ●●●● Success judgment for probability-based magic ●●●●
	// Calculation method
	// Attacker's points: LV + ((MagicBonus * 3) * Magic unique coefficient)
	// Defender points: ((LV / 2) + (MR * 3)) / 2
	// Attack success rate: Attacker's points - Defender's points	
	public boolean calcProbabilityMagic(int skillId) {
		if (_pc != null && _pc.isGm()) {
			return true;
		}
		if (_calcType == PC_NPC && _targetNpc != null) {
			int npcId	= _targetNpc.getNpcTemplate().getNpcId();
			if (npcId >= 45912 && npcId <= 45915 && !_pc.getSkill().hasSkillEffect(STATUS_HOLY_WATER)) {
				return false;
			}
			if (npcId == 45916 && !_pc.getSkill().hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
				return false;
			}
			if (npcId == 45941 && !_pc.getSkill().hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
				return false;
			}
			if (Config.ALT.KARMA_BUFF_ENABLE) {
				if (L1MonsterInstance.KARMA_BALROG_BOSS_IDS.contains(npcId) && !_pc.getSkill().hasSkillEffect(STATUS_CURSE_BARLOG)) {
					return false;
				}
				if (L1MonsterInstance.KARMA_YAHEE_BOSS_IDS.contains(npcId) && !_pc.getSkill().hasSkillEffect(STATUS_CURSE_YAHEE)) {
					return false;
				}
			}
			if ((npcId >= 5000103 && npcId <= 5000104) && !_pc.getSkill().hasSkillEffect(DETHNIGHT_BUNNO)) {
				return false;
			}
			if (NOT_ENABLE_NPC_IDS.contains(npcId)) {
				return false;
			}
			if (npcId >= 46068 && npcId <= 46091 && _pc.getSpriteId() == 6035) {
				return false;
			}
			if (npcId >= 46092 && npcId <= 46106 && _pc.getSpriteId() == 6034) {
				return false;
			}
			if (npcId != 50087 && _targetNpc.getNpcTemplate().getSpriteId() == 7684 && !_pc.getSkill().hasSkillEffect(PAP_FIVEPEARLBUFF)) {
				return false;
			}
			if (_targetNpc.getNpcTemplate().getSpriteId() == 7805 && !_pc.getSkill().hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
				return false;
			}
			if (_targetNpc.getNpcTemplate().getSpriteId() == 7720) {
				return false;
			}
		}

		if (!checkZone(skillId)) {
			return false;
		}
		
		if ((_calcType == PC_PC || _calcType == NPC_PC) && _targetPc.getSkill().hasSkillEffect(MAGIC_SHIELD)) {// 디버프 방어
			if (random.nextInt(20) < 1) {
	            _targetPc.getSkill().removeSkillEffect(MAGIC_SHIELD);
	            _targetPc.sendPackets(new S_SpellBuffNoti(_targetPc, MAGIC_SHIELD, false, -1), true);
	            _targetPc.send_effect(18946);
	        } else {
	        	_targetPc.send_effect(18945);
	        }
			return false;
		}
		
		if (skillId == SHAPE_CHANGE && _calcType == PC_PC && _pc != null && _targetPc != null) {
			if (_pc.getId() == _targetPc.getId()) {
				return true;
			}
			if (_targetPc.getSkill().hasSkillEffect(SHAPE_CHANGE_DOMINATION) || _targetPc.getSkill().hasSkillEffect(SHAPE_CHANGE_100LEVEL)) {
				_targetPc.send_effect(15846);
				return false;
			}
		}
		
		if (skillId == CANCELLATION) {
			if (_calcType == PC_NPC || _calcType == NPC_PC || _calcType == NPC_NPC) {
				return true;
			}
			if (_calcType == PC_PC && _pc != null && _targetPc != null) {
				if (_pc.getId() == _targetPc.getId()) {
					return true;
				}
				if (_pc.getClanid() > 0 && (_pc.getClanid() == _targetPc.getClanid()) && (_pc.getConfig().getDuelLine() == _targetPc.getConfig().getDuelLine())) {
					return true;
				}
				if (_pc.isInParty() && _pc.getParty().isMember(_targetPc)) {
					return true;
				}
				if (_targetPc.isInvisble() || _targetPc.getTeleport().isTeleport()) {
					return false;
				}
				if (_pc.getRegion() == L1RegionStatus.SAFETY || _targetPc.getRegion() == L1RegionStatus.SAFETY) {
					return false;
				}
			}
		}

		// 80렙 이상 npc 에게 아래 마법 안걸림.
		if (_calcType == PC_NPC && _targetLevel >= 80 && _targetNpc.getNpcTemplate().isCantResurrect()) {
			if (skillId == WEAPON_BREAK || skillId == SLOW || skillId == GREATER_SLOW || skillId == CURSE_PARALYZE
					|| skillId == WEAKNESS || skillId == DISEASE || skillId == DECAY_POTION || skillId == FATAL_POTION
					|| skillId == ERASE_MAGIC || skillId == AREA_OF_SILENCE
					|| skillId == WIND_SHACKLE || skillId == STRIKER_GALE
					|| skillId == FOG_OF_SLEEPING || skillId == ICE_LANCE
					|| skillId == POLLUTE_WATER || skillId == ELEMENTAL_FALL_DOWN || skillId == RETURN_TO_NATURE
					|| skillId == ARMOR_BREAK || skillId == SILENCE
					|| skillId == DARKNESS) {
				return false;
			}
		}

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (_calcType == PC_PC) {
				/*** 신규레벨보호 ***/
				if (Config.ALT.BEGINNER_SAFE_LEVEL > 0) {
					if (_targetLevel < Config.ALT.BEGINNER_SAFE_LEVEL || _attackerLevel < Config.ALT.BEGINNER_SAFE_LEVEL) {
						if (skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL && skillId != NATURES_BLESSING) { // 버프계
							_pc.sendPackets(L1SystemMessage.BEGIN_LEVEL_MAGIC_SAFE);
							_targetPc.sendPackets(L1SystemMessage.BEGIN_LEVEL_MAGIC_SAFE);
							return false;
						}
					}
				}
				
				/** 신규혈맹 공격안되게 **/
				if ((_pc.getClanid() == Config.PLEDGE.BEGINNER_PLEDGE_ID || _targetPc.getClanid() == Config.PLEDGE.BEGINNER_PLEDGE_ID)) {
					if (skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL && skillId != NATURES_BLESSING) { // 버프계
						_pc.sendPackets(L1SystemMessage.BEGIN_CLAN_MAGIC_SAFE);
						_targetPc.sendPackets(L1SystemMessage.BEGIN_CLAN_MAGIC_SAFE);
						return false;
					}
				}
				/*** 신규보호존 ***/
				if ((Config.ALT.BEGINNER_MAP_LIST.contains(_pc.getMapId()) || Config.ALT.BEGINNER_MAP_LIST.contains(_targetPc.getMapId()))) {
					if (skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL && skillId != NATURES_BLESSING) { // 버프계
						_pc.sendPackets(L1SystemMessage.BEGIN_AREA_MAGIC_SAFE);
						_targetPc.sendPackets(L1SystemMessage.BEGIN_AREA_MAGIC_SAFE);
						return false;
					}
				}
				
				if (_targetPc.isStun() && Config.SPELL.STUN_CONTINUE// 연스턴
						&& (skillId == SHOCK_STUN || skillId == BONE_BREAK || skillId == EMPIRE || skillId == PANTERA || skillId == FORCE_STUN)) {// 스턴중에 스턴실패
					return false;
				}
			}
			
			if (_calcType == NPC_PC && _targetPc.getSkill().hasSkillEffect(STATUS_FREEZE) && skillId == STATUS_FREEZE) {
				return false;
			}

			if (_targetPc.isBind()) {
				if (skillId != WEAPON_BREAK && skillId != CANCELLATION // 확률계
						&& skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL
						&& skillId != NATURES_BLESSING && skillId == MANA_DRAIN || skillId == CURSE_PARALYZE || skillId == THUNDER_GRAB
						|| skillId == ERASE_MAGIC || skillId == MOB_ERASE_MAGIC
						|| skillId == SHOCK_STUN || skillId == EMPIRE
						|| skillId == PANTERA || skillId == PHANTOM || skillId == JUDGEMENT 
						|| skillId == FORCE_STUN
						|| skillId == EARTH_BIND || skillId == BONE_BREAK) {// 버프계
					return false;
				}
			}
		} else {
			if (_targetNpc.getSkill().hasSkillEffect(EARTH_BIND) && skillId != WEAPON_BREAK && skillId != CANCELLATION) {
				return false;
			}
		}

		if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			if ((skillId == SILENCE || skillId == AREA_OF_SILENCE)
					&& (_targetNpc.getNpcId() == 45684 || _targetNpc.getNpcId() == 45683 || _targetNpc.getNpcId() == 45681
						|| _targetNpc.getNpcId() == 45682 || _targetNpc.getNpcId() == 900013 || _targetNpc.getNpcId() == 900040 || _targetNpc.getNpcId() == 5100)) {
				return false;
			}
		}

		// 100% 확률을 가지는 스킬
		if (skillId == MIND_BREAK || skillId == IllUSION_AVATAR) {
			return true;
		}

		int probability = 0;		
		if ((_calcType == PC_PC || _calcType == PC_NPC) && SpellProbabilityTable.getInstance().contains_probability(skillId)) {
			L1Character receiver = null;
			switch (_calcType) {
			case PC_PC:
				receiver = _targetPc;
				break;
			case PC_NPC:
				receiver = _targetNpc;
				break;
			case NPC_PC:
				receiver = _targetPc;
				break;
			case NPC_NPC:
				receiver = _targetNpc;
				break;
			}	
			probability = SpellProbabilityTable.getInstance().calc_probability(skillId, _pc, receiver, _pc.getAbility().getTotalInt(), getTargetMr());
			/*if (_pc.isGm()) { 
				_pc.sendPackets(new S_SystemMessage(String.format("%s%s", "Skill probability: " , probability)), true);
			} */  
		} else  {
			probability = calcProbability(skillId);
			// 마법 확률 추가 설정
			switch(_calcType){
			case PC_PC:
				probability += getBalanceMagicHit(_pc.getType(), _targetPc.getType());
				break;
			case PC_NPC:
				probability += getBalanceMagicHit(_pc.getType(), -1);
				break;
			case NPC_PC:
				probability += getBalanceMagicHit(-1, _targetPc.getType());
				probability += _npc.ability.getMagicHitup();
				break;
			case NPC_NPC:
				probability += getBalanceMagicHit(-1, -1);
				break;
			}
		}			
		
		int rnd = 0;
		switch (skillId) {
		case DECAY_POTION:
		case FATAL_POTION:
		case SILENCE:
		case CURSE_PARALYZE:
		case CANCELLATION:
		case SLOW:
		// case DARKNESS:
		case WEAKNESS:
		case CURSE_POISON:
		case CURSE_BLIND:
		case WEAPON_BREAK:
		case MANA_DRAIN:
		case DEATH_HEAL:
		case MOB_DEATH_HEAL:
			if (_calcType == PC_PC) {
				rnd = _targetPc != null ? random.nextInt(_targetPc.getResistance().getEffectedMrBySkill()) + 1 : 1;
			} else if (_calcType == PC_NPC) {
				rnd = _targetNpc.getResistance().getEffectedMrBySkill() < 1 ? 1 : random.nextInt(_targetNpc.getResistance().getEffectedMrBySkill()) + 1;
			} else {
				rnd = random.nextInt(100) + 1;
			}
			break;
		default:
			rnd = random.nextInt(100) + 1;
			if (probability > 90) {
				probability = 90;
			}
			break;
		}
			
		// TODO 성공 여부 계산
		boolean isSuccess	= probability >= rnd;
		if (!isSuccess) {
			if (_calcType == NPC_PC || _calcType == PC_PC) {
				_targetPc.send_effect(13418);// 미스 이팩트
			} else if (_calcType == PC_NPC) {
				_pc.send_effect(_targetNpc.getId(), 13418);// 미스 이팩트
			}
		}
		
		if (_calcType == PC_NPC && !isSuccess & skillId == TURN_UNDEAD && _targetNpc.getNpcTemplate().getUndead() == L1Undead.UNDEAD && random.nextBoolean()) {
			_targetNpc.broadcastPacket(new S_Effect(_targetNpc.getId(), 8987), true);
			if (_targetNpc.getMoveState().getMoveSpeed() != 1) {
				_targetNpc.broadcastPacket(new S_SkillHaste(_targetNpc.getId(), 1, 0), true);
				_targetNpc.getMoveState().setMoveSpeed(1);
			}
		}
		
		if ((_calcType == NPC_PC || _calcType == PC_PC) && isSuccess && _targetPc.getSkill().hasSkillEffect(MAGIC_SHIELD)) {// 디버프 방어
			_targetPc.send_effect(15846);
			if (_targetPc._isMagicShield && random.nextInt(20) < 1) {
				_targetPc.getSkill().removeSkillEffect(MAGIC_SHIELD);
			}
			_targetPc._isMagicShield = true; // 두번째 스킬부터 처리되도록
			return false;
		}
		if (!Config.ALT.ALT_ATKMSG) {
			return isSuccess;
		}
		if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
			return isSuccess;
		}
		if ((_calcType == PC_PC || _calcType == NPC_PC) && !_targetPc.isGm()) {
			return isSuccess;
		}

		String msg0 = StringUtil.EmptyString;
		String msg2 = StringUtil.EmptyString;
		String msg3 = StringUtil.EmptyString;
		String msg4 = StringUtil.EmptyString;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			msg0 = _pc.getName();
		} else if (_calcType == NPC_PC) {
			msg0 = _npc.getName();
		}
		msg2 = S_SystemMessage.getRefText(108) + ":" + probability + "%";
		if (_calcType == NPC_PC || _calcType == PC_PC) {
			msg4 = _targetPc.getName();
		} else if (_calcType == PC_NPC) {
			msg4 = _targetNpc.getName();
		}
		msg3 = isSuccess == true ? S_SystemMessage.getRefText(109) : S_SystemMessage.getRefText(110);
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			_pc.sendPackets(new S_SystemMessage("\\fR[" + msg0 + "->" + msg4 + "] " + msg2 + " / " + msg3), true);
		}
		if (_calcType == NPC_PC || _calcType == PC_PC) {
			_targetPc.sendPackets(new S_SystemMessage("\\fY[" + msg0 + "->" + msg4 + "] " + msg2 + " / " + msg3), true);
		}
		return isSuccess;
	}

	boolean checkZone(int skillId) {
		if (_pc != null && _targetPc != null && (_pc.getRegion() == L1RegionStatus.SAFETY || _targetPc.getRegion() == L1RegionStatus.SAFETY)) {// 마을에서 사용할 수 없는 ... 리턴되는 스킬들
			if (skillId == CURSE_POISON || skillId == CURSE_BLIND || skillId == WEAPON_BREAK
				|| skillId == SLOW || skillId == GREATER_SLOW || skillId == CURSE_PARALYZE
				|| skillId == MANA_DRAIN || skillId == DARKNESS || skillId == WEAKNESS
				|| skillId == DISEASE || skillId == SILENCE || skillId == ICE_LANCE
				|| skillId == FOG_OF_SLEEPING || skillId == DECAY_POTION || skillId == FATAL_POTION || skillId == DEATH_HEAL || skillId == ETERNITY 
				|| skillId == SHOCK_STUN || skillId == FORCE_STUN
				|| skillId == ENTANGLE || skillId == ERASE_MAGIC || skillId == EARTH_BIND || skillId == AREA_OF_SILENCE || skillId == ELEMENTAL_FALL_DOWN
				|| skillId == RETURN_TO_NATURE || skillId == WIND_SHACKLE || skillId == POLLUTE_WATER || skillId == STRIKER_GALE
				|| skillId == DESTROY
				|| skillId == PHANTASM || skillId == CONFUSION || skillId == BONE_BREAK || skillId == PANIC
				|| skillId == SHADOW_SLEEP
				|| skillId == DESPERADO || skillId == POWER_GRIP || skillId == TEMPEST
				|| skillId == PANTERA || skillId == PHANTOM || skillId == JUDGEMENT
				|| skillId == EMPIRE
				|| skillId == PRESSURE || skillId == CRUEL) {
				return false;
			}
		}
		return true;
	}

	int calcProbability(int skillId) {
		L1Skills l1skills	= SkillsTable.getTemplate(skillId);
		double resistance	= 0.0D;
		int probability		= l1skills.getProbabilityValue();
		int attackInt		= _attacker.getAbility().getTotalInt();
		int defenseMr		= _target.getResistance().getEffectedMrBySkill();
		
		// TODO 스킬 확률 세부 설정
		switch (skillId) {
	/************************************************************************************************************************
	************************************************* 군주 기술   ********************************************************************			
	************************************************************************************************************************/
		case EMPIRE:// 엠파이어
			probability = (int) Config.SPELL.SHOCK_STUN_CHANCE + ((_attackerLevel - _targetLevel) * Config.SPELL.STUN_LEVEL_RATE) + (_target.isShockAttack() ? 20 : 0);
			resistance	= _attacker.getResistance().getHitupSkill() - _target.getResistance().getToleranceSkill();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 10, 80);
			break;
		case STATUS_TYRANT_EXCUTION:// 타이런트: 엑스큐션
			probability = (int) Config.SPELL.TYRANT_EXCUTION_PROB + ((_attackerLevel - _targetLevel) << 1);
			resistance	= _attacker.getResistance().getHitupSkill() - _target.getResistance().getToleranceSkill();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 10, 80);
			break;
	/************************************************************************************************************************
	************************************************* 기사 기술   ********************************************************************			
	************************************************************************************************************************/
		case COUNTER_BARRIER:
			probability = Config.SPELL.COUNTER_BARRIER_CHANCE;
			if (_attacker.isPassiveStatus(L1PassiveId.COUNTER_BARRIER_VETERAN)) {
				probability += IntRange.ensure(_attackerLevel - 84, 0, 10);
			}
			break;
		case SHOCK_STUN:// 쇼크스턴
			probability = (int) Config.SPELL.SHOCK_STUN_CHANCE + ((_attackerLevel - _targetLevel) * Config.SPELL.STUN_LEVEL_RATE) + (_target.isShockAttack() ? 20 : 0);
			resistance	= _attacker.getResistance().getHitupSkill() - _target.getResistance().getToleranceSkill();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 10, 80);
			break;
		case FORCE_STUN:// 포스스턴
			probability = (int) Config.SPELL.FORCE_STUN_CHANCE + ((_attackerLevel - _targetLevel) << 1) + (_target.isShockAttack() ? 20 : 0);
			resistance	= _attacker.getResistance().getHitupSkill() - _target.getResistance().getToleranceSkill();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 10, 80);
			break;
		case SHOCK_ATTACK:// 쇼크 어택
			probability = (int) Config.SPELL.SHOCK_ATTACK_CHANCE + ((_attackerLevel - _targetLevel) << 1);
			resistance	= _attacker.getResistance().getHitupSkill() - _target.getResistance().getToleranceSkill();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 10, 80);
			break;
	/************************************************************************************************************************
	************************************************* 요정 정령   ********************************************************************			
	************************************************************************************************************************/
		case INFERNO: // 인페르노
			probability = Config.SPELL.INFERNO_PROB;
			break;
		case ERASE_MAGIC:
		case ELEMENTAL_FALL_DOWN:
			/** 이레이즈매직 엘리멘탈폴다운 **/
			/** 동레벨일경우 40% 레벨 아래당 2% 성공확률 상향 레벨 높을때 2% 성공확률 감소 **/
			if (_attackerLevel >= _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) << 1) + Config.SPELL.ERASE_MAGIC_PROB;
			} else if (_attackerLevel < _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) * 3) + Config.SPELL.ERASE_MAGIC_PROB;
			}
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (probability > 70) {
				probability = 70;
			}
			break;
		case EARTH_BIND:
			/** 동레벨일경우 35% 레벨아래당 2% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
			if (_attackerLevel >= _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) << 1) + Config.SPELL.EARTH_BIND_PROB;
			} else if (_attackerLevel < _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) * 3) + Config.SPELL.EARTH_BIND_PROB;
			}
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (probability > 70) {
				probability = 70;
			}
			break;
		case POLLUTE_WATER:
		case MOB_POLLUTE_WATER:
			/** 동레벨일경우 35% 레벨아래당 2% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
			if (_attackerLevel >= _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) << 1) + Config.SPELL.POLLUTE_WATER_PROB;
			} else if (_attackerLevel < _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) * 3) + Config.SPELL.POLLUTE_WATER_PROB;
			}
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (probability > 70) {
				probability = 70;
			}
			break;	
		case STRIKER_GALE:
			/** 동레벨일경우 35% 레벨아래당 2% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
			if (_attackerLevel >= _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) << 1) + Config.SPELL.STRIKER_GALE_PROB;
			} else if (_attackerLevel < _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) * 3) + Config.SPELL.STRIKER_GALE_PROB;
			}
			if (_attacker.isPassiveStatus(L1PassiveId.STRIKER_GALE_SHOT)) {
				probability += 10;
			}
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (probability > 70) {
				probability = 70;
			}
			break;			
		case AREA_OF_SILENCE:
			/** 동레벨일경우 30% 레벨아래당 2% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
			if (_attackerLevel >= _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) << 1) + Config.SPELL.AREA_OF_SILENCE_PROB;
			} else if (_attackerLevel < _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) * 3) + Config.SPELL.AREA_OF_SILENCE_PROB;
			}
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (probability > 70) {
				probability = 70;
			}
			break;
		case RETURN_TO_NATURE:
			probability = 40;
			break;
		case WIND_SHACKLE:
			/** 동레벨일경우 30% 레벨아래당 2% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
			if (_attackerLevel >= _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) << 1) + Config.SPELL.WIND_SHACKLE_PROB;
			} else if (_attackerLevel < _targetLevel) {
				probability = ((_attackerLevel - _targetLevel) * 3) + Config.SPELL.WIND_SHACKLE_PROB;
			}
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (probability > 70) {
				probability = 70;
			}
			break;
		case ENTANGLE:
			probability = (int) (((l1skills.getProbabilityDice()) / 10D) * (_attackerLevel - _targetLevel)) + l1skills.getProbabilityValue();
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (probability > 70) {
				probability = 70;
			}
			break;
	/************************************************************************************************************************
	************************************************* 다크 엘프 정령    ********************************************************************			
	************************************************************************************************************************/
		case ARMOR_BREAK: // 아머 브레이크		
			probability = (int) (Config.SPELL.ARMOR_BRAKE_PROB + ((_attackerLevel - _targetLevel) * 3));
			if (_attacker.isPassiveStatus(L1PassiveId.ARMOR_BREAK_DESTINY)) {//아머 브레이크 데스니티
				probability += 3;
				if (_attackerLevel > 85) {
					probability += (_attackerLevel + ~0x00000054) * 3;//85레벨 이상일때 1레벨당 3%씩 증가
				}
			}
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 10, 80);
			break;
		case SHADOW_SLEEP:
			probability = (int) 15 + (_attackerLevel - _targetLevel);
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 0, 50);
			break;
		case AVENGER:
			probability = (int) Config.SPELL.AVENGER_PROB + (_attackerLevel - _targetLevel);
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 0, 50);
			break;
		case SHADOW_STEP:
			probability = (int) Config.SPELL.SHADOWSTEP_PROB + ((_attackerLevel - _targetLevel) * 5);
			resistance	= _attacker.getResistance().getHitupSpirit() - _target.getResistance().getToleranceSpirit();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 15, 80);
			break;
	/************************************************************************************************************************
	************************************************* 마법사    ********************************************************************			
	************************************************************************************************************************/
		case MOB_DEATH_HEAL:
			probability = (int) (50 - (defenseMr / 10));
			resistance	= _target.ability.getMe();// 내성
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (defenseMr >= 188 || probability < 0) {
				probability = 0;
			}
			break;
		case DEATH_HEAL:
			probability = Config.SPELL.DEATH_HEAL_PROB + (attackInt - (defenseMr >> 2));
			resistance	= _attacker.ability.getMagicHitup() - _target.ability.getMe();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 1, 80);
			break;
		case CANCELLATION:  //켄슬 본섭 마방100에게 46% - 마방당 -1%
			if (_pc.isWizard()) {
				//if(attackInt > 25)attackInt = 25;
				probability = (int) Config.SPELL.CANCELLATION_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			} else if (_pc.isElf()) {
				//if(attackInt > 45)attackInt = 45;
				probability = (int) Config.SPELL.ELF_CANCELLATION_PROB + ((attackInt << 1) - defenseMr + ~0x0000000E);
			}
			resistance	= _attacker.ability.getMagicHitup() - _target.ability.getMe();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 1, 80);
			break;
		case SHAPE_CHANGE: // 셰이프 본섭 마방140에게 60% - 마방당 -1%
			//if(attackInt > 25)attackInt = 25;
			probability = (int) Config.SPELL.SHAPE_CHANGE_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			resistance	= _attacker.ability.getMagicHitup() - _target.ability.getMe();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 1, 80);
			if ((_calcType == PC_PC || _calcType == NPC_PC) && (_targetPc.getSkill().hasSkillEffect(SHAPE_CHANGE_DOMINATION) || _targetPc.getSkill().hasSkillEffect(SHAPE_CHANGE_100LEVEL))) {
				probability = 0;
			}
			break;
		case CURSE_PARALYZE: //패럴라이즈 마방100에게 15%
			//if(attackInt > 25)attackInt = 25;
			probability = (int) Config.SPELL.CURSE_PARALYZE_PROB + (attackInt * 3 + l1skills.getProbabilityValue() - defenseMr);
			resistance	= _attacker.ability.getMagicHitup() - _target.ability.getMe();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 1, 80);
			break;		
		case SLOW: // 슬로우 본섭 마방100에게 58% - 마방당 -1%
		case DISEASE: // 디지즈 본섭 마방100에게 68% - 마방당 -1%
		case WEAKNESS: // 위크니스 본섭 마방100에게 56% - 마방당 -1%
		case WEAPON_BREAK:// 웨폰브레이크 마방100에게 30%
		case DECAY_POTION: // 디케이포션 마방100에게 23%
		case FATAL_POTION: // 디케이포션 마방100에게 23%
		case ICE_LANCE: // 아이스랜스 마방100에게 30%
		case CURSE_BLIND: // 야매로 15로 설정함
		case CURSE_POISON: // 야매로 50로 설정함
		case SILENCE: // 야매로 35로 설정함
		case DARKNESS: // 야매로 30로 설정함
		case FOG_OF_SLEEPING:// 야매로 25로 설정함
			//if(attackInt > 25)attackInt = 25;
			if (skillId == SLOW) {
				probability = (int) Config.SPELL.SLOW_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			} else if (skillId == DISEASE) {
				probability = (int) Config.SPELL.DISEASE_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			} else if (skillId == WEAKNESS) {
				probability = (int) Config.SPELL.WEAKNESS_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			} else if (skillId == WEAPON_BREAK) {
				probability = (int) Config.SPELL.WEAPON_BREAK_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			} else if (skillId == DECAY_POTION || skillId == FATAL_POTION) {
				probability = (int) Config.SPELL.PATAL_POTION_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			} else if (skillId == CURSE_BLIND) {
				probability = (int) Config.SPELL.CURSE_BLIND_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			} else if (skillId == FOG_OF_SLEEPING) {
				probability = (int) Config.SPELL.FOG_OF_SLEEPING_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			} else if (skillId == DARKNESS) {
				probability = (int) Config.SPELL.DARKNESS_PROB + ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			} else {
				probability = (int) ((attackInt << 2) + l1skills.getProbabilityValue() - defenseMr);
			}
			if (_pc != null && _pc.isElf()) {
				probability += ~0x0000001D;// -30
			}
			resistance	= _attacker.ability.getMagicHitup() - _target.ability.getMe();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 1, 80);
			break;
		case MANA_DRAIN:
			//if(attackInt > 25)attackInt = 25;
			probability = (int) ((attackInt - (defenseMr / 5.95)) * l1skills.getProbabilityValue() + Config.SPELL.MANA_DRAIN_PROB);
			if (_pc != null && _pc.isElf()) {
				probability += ~0x00000020;// -33
			}
			resistance	= _attacker.ability.getMagicHitup() - _target.ability.getMe();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (probability < 0) {
				probability = 0;
			}
			break;
		case TURN_UNDEAD:					
			if (_calcType == PC_PC || _calcType == PC_NPC) {
			    probability = (int) (((attackInt << 1) + (_attackerLevel << 1) + _pc.getBaseMagicHitUp() + Config.SPELL.TURN_UNDEAD_PROB) - (defenseMr + (_targetLevel >> 1)) + ~0x0000005D); //84
			    if (!_pc.isWizard()) {
			    	probability += ~0x0000001D;// -30
			    }
			}
			resistance	= _attacker.ability.getMagicHitup();// 적중
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			if (probability > 60) {
				probability = 60;
			}
			break;
		case ETERNITY:
			probability = (int) Config.SPELL.ETERNITY_PROB + ((_attackerLevel - _targetLevel) << 1);
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				int range = (int) _pc.getLocation().getLineDistance(_target.getLocation());
				if (range > 4) {
					probability -= (range + ~0x00000003) * 5;// 4셀 이상일때(1셀당 3프로)
				}
			}
			resistance	= _attacker.ability.getMagicHitup() - _target.ability.getMe();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 10, 80);
			break;
/************************************************************************************************************************
 ***************************************************** 용기사 용언  *****************************************************************			
 ************************************************************************************************************************/
		case THUNDER_GRAB:
		case DESTROY:
			probability = (int) 35 + ((_attackerLevel - _targetLevel) << 1);
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += _pc.getBaseMagicHitUp() << 1;
			}
			resistance	= _attacker.getResistance().getHitupDragon() - _target.getResistance().getToleranceDragon();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 20, 80);
			break;
		case HALPHAS:
			probability = Config.SPELL.HALPHAS_PROB;
			break;
		case FOU_SLAYER:
			probability = Config.SPELL.FOUSLAYER_STUN_PROB + (_attackerLevel - _targetLevel);
			if (_attacker.isPassiveStatus(L1PassiveId.FOU_SLAYER_FORCE)) {
				probability += Config.SPELL.FOUSLAYER_FORCE_STUN_PROB;
			}
			resistance	= _attacker.getResistance().getHitupDragon() - _target.getResistance().getToleranceDragon();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 5, 60);
			break;
		case CHAIN_REACTION:
			probability = (int) Config.SPELL.CHAIN_REACTION_PROB + (_attackerLevel - _targetLevel);
			resistance	= _attacker.getResistance().getHitupDragon() - _target.getResistance().getToleranceDragon();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 5, 80);
			break;
		case BEHEMOTH:
			probability = (int) Config.SPELL.BEHEMOTH_DEBUFF_PROB + (_attackerLevel - _targetLevel);
			resistance	= _attacker.getResistance().getHitupDragon() - _target.getResistance().getToleranceDragon();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 5, 80);
			break;
	/************************************************************************************************************************
	************************************************* 환술사 용언    ********************************************************************			
	************************************************************************************************************************/
		case CONFUSION:
		case PHANTASM: // 컨퓨젼, 판타즘 본섭 30%
			probability = Config.SPELL.PHANTASM_PROB;
			resistance	= _attacker.getResistance().getHitupDragon() - _target.getResistance().getToleranceDragon();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 20, 90);
			break;
		case BONE_BREAK:
			probability = (int) Config.SPELL.BONE_BREAK_PROB + (_attackerLevel - _targetLevel);
			resistance	= _attacker.getResistance().getHitupDragon() - _target.getResistance().getToleranceDragon();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 20, 90);
			break;
		case ENSNARE:
			probability = (int) Config.SPELL.ENSNARE_CHANCE + (_attackerLevel - _targetLevel);
			resistance	= _attacker.getResistance().getHitupDragon() - _target.getResistance().getToleranceDragon();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 20, 90);
			break;
		case OSIRIS:
			probability = (int) Config.SPELL.OSIRIS_CHANCE + (_attackerLevel - _targetLevel);
			resistance	= _attacker.getResistance().getHitupDragon() - _target.getResistance().getToleranceDragon();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 20, 90);
			break;
	/************************************************************************************************************************
	************************************************* 전사 공포    ********************************************************************			
	************************************************************************************************************************/
		case DESPERADO:
			probability = (int) Config.SPELL.DESPERADO_CHANCE + ((_attackerLevel - _targetLevel) * Config.SPELL.DESPERADO_LEVEL_RATE);
			resistance	= _attacker.getResistance().getHitupFear() - _target.getResistance().getToleranceFear();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 10, 80);
			break;
		case TEMPEST:
			probability = (int) Config.SPELL.TEMPEST + ((_attackerLevel - _targetLevel) << 1);
			resistance	= _attacker.getResistance().getHitupFear() - _target.getResistance().getToleranceFear();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 15, 80);
			break;
		case POWER_GRIP:
			probability = (int) Config.SPELL.POWER_GRIP + ((_attackerLevel - _targetLevel) * 5);
			resistance	= _attacker.getResistance().getHitupFear() - _target.getResistance().getToleranceFear();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 15, 80);
			break;
		case TOMAHAWK:
			probability = (int) l1skills.getProbabilityValue() + (_attackerLevel - _targetLevel);
			resistance	= _attacker.getResistance().getHitupFear() - _target.getResistance().getToleranceFear();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 15, 80);
			break;
	/************************************************************************************************************************
	************************************************* 검사 공포    ********************************************************************			
	************************************************************************************************************************/
		case PANTERA:
			probability = (int) Config.SPELL.PANTERA_PROB + ((_attackerLevel - _targetLevel) << 1);
			resistance	= _attacker.getResistance().getHitupFear() - _target.getResistance().getToleranceFear();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 15, 80);
			break;
		case PHANTOM:
			probability = (int) Config.SPELL.PHANTOM_PROB + ((_attackerLevel - _targetLevel) << 1);
			resistance	= _attacker.getResistance().getHitupFear() - _target.getResistance().getToleranceFear();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 15, 80);
			break;
		case JUDGEMENT:
			probability = (int) Config.SPELL.JUDGEMENT_PROB + ((_attackerLevel - _targetLevel) << 1);
			resistance	= _attacker.getResistance().getHitupFear() - _target.getResistance().getToleranceFear();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 15, 80);
			break;
		/************************************************************************************************************************
		************************************************* 창기사 공포    ********************************************************************			
		************************************************************************************************************************/
		case PRESSURE:
			probability = (int) Config.SPELL.PRESSURE_PROB + ((_attackerLevel - _targetLevel) << 1);
			resistance	= _attacker.getResistance().getHitupFear() - _target.getResistance().getToleranceFear();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 15, 80);
			break;
		case CRUEL:
			probability = (int) Config.SPELL.CRUEL_PROB + ((_attackerLevel - _targetLevel) << 1);
			resistance	= _attacker.getResistance().getHitupFear() - _target.getResistance().getToleranceFear();
			if (resistance != 0) {
				probability += (int)((probability * 0.01D) * resistance);
			}
			probability = IntRange.ensure(probability, 15, 80);
			break;
			
		/************************************************************************************************************************		
		************************************************************************************************************************/
		default:
			int dice = l1skills.getProbabilityDice();
			if (dice > 0) {
				probability += calcProbabilityDice(dice);
			}

			probability = probability * _leverage / 10;
			probability -= getTargetMr();

			if (skillId == TAMING_MONSTER) {
				double probabilityRevision = 1D;
				if ((_targetNpc.getMaxHp() >> 2) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.3D;
				} else if (((_targetNpc.getMaxHp() << 1) >> 2) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.2D;
				} else if (((_targetNpc.getMaxHp() * 3) >> 1) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.1D;
				}
				probability *= probabilityRevision;
			} else if (skillId == MOB_BASILL || skillId == MOB_COCA) {
				if (_calcType == NPC_PC) {
					L1ItemInstance shield = _targetPc.getInventory().getEquippedShield();
					if (shield != null && shield.getItemId() == 20229) {// 반사 방패
						probability += ~0x0000001D;// -30
					}
				}
			}
			break;
		}
		return probability;
	}
	
	int calcProbabilityDice(int dice) {
		int result = 0;
		int diceCount = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			if (_pc.isWizard()) {
				diceCount = getMagicBonus() + getMagicLevel() + 1;
			} else if (_pc.isDragonknight()) {
				diceCount = getMagicBonus() + getMagicLevel();
			} else {
				diceCount = getMagicBonus() + getMagicLevel() + ~0x00000000;
			}
		} else {
			diceCount = getMagicBonus() + getMagicLevel();
		}
		
		if (diceCount < 1) {
			diceCount = 1;
		}
		for (int i = 0; i < diceCount; i++) {
			result += (random.nextInt(dice) + 1);
		}
		return result;
	}

	public int calcMagicDamage(int skillId) {
		if (skillId == COUNTER_DETECTION) {// 인비지 대상에게만 대미지를 준다
			if (!_target.isInvisble() || _target instanceof L1PcInstance == false) {// 인비지 대상에게만 대미지를 준다
				return 0;
			}
			if (_target.getSkill().isBlindHidingAssassin() && _target.getSkill().getSkillEffectTimeSec(BLIND_HIDING) >= 13) {// 16초 - 3초
				return 0;
			}
		}
		int damage = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			damage = calcPcMagicDamage(skillId);
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			damage = calcNpcMagicDamage(skillId);
		}
		
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int tempsp = _pc.getAbility().getSp();
			if (tempsp >= 33) {
				int temp2sp = tempsp / 3;
				damage += damage * (temp2sp * 0.01);
			}

			damage += CalcIntelStat.magicDamage(_pc.getAbility().getTotalInt()) + _pc.ability.getMagicDmgup();
			double balance = CalcIntelStat.magicBonus(_pc.getAbility().getTotalInt()) * 0.02D;
			damage += damage * balance;
		} else if (_calcType == NPC_PC) {
			damage += _npc.ability.getMagicDmgup();
		}

		if (skillId != CONFUSION && skillId != MIND_BREAK && skillId != MAGMA_BREATH) {
			damage = calcMrDefense(damage);
		}
		if ((_calcType == PC_PC || _calcType == PC_NPC) && _pc.getDoll() != null) {
			damage += _pc.getDoll().attackMagicDamage(_pc, _target);
		}
		
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (skillId == ENERGY_BOLT || skillId == CALL_LIGHTNING || skillId == DISINTEGRATE) {
				L1ItemInstance shield = _targetPc.getInventory().getEquippedShield();
				if (shield != null && shield.getItemId() == 20230) {// 붉은 기사의 방패
					int probability = 1;
					if (shield.getEnchantLevel() >= 10) {
						probability = 5;
					} else if (shield.getEnchantLevel() < 6) {
						probability = 1;
					} else {
						probability = shield.getEnchantLevel() + ~0x00000004;// -5
					}
					if (random.nextInt(100) < probability) {
						damage *= 0.8D;
					}
				}
			}
			if (!_targetPc.getSkill().hasSkillEffect(SOUL_BARRIER) && damage > _targetPc.getCurrentHp()) {
				damage = _targetPc.getCurrentHp();
			}
			damage = (int)L1ArmorSkill.getChanceReduction(_targetPc, damage);
			
			if (_targetPc != null) {
				L1ArmorSkill.armorSkillAction(_targetPc);
			}
	        if (_targetPc.getSkill().hasSkillEffect(NATURES_TOUCH)
	        		&& random.nextInt(20) + 1 <= 1) {
        		int gethp = 10 + random.nextInt(10);// 회복률 = 기본50회복+랜덤(1~30) //원래 랜덤수치 30임
        		if (_targetPc.getSkill().hasSkillEffect(POLLUTE_WATER) || _targetPc.getSkill().hasSkillEffect(MOB_POLLUTE_WATER)) {
        			gethp >>= 1;// 플루트워터경우절반 //원래 랜덤수치 30임
        		}
        		if (_targetPc.getSkill().hasSkillEffect(WATER_LIFE)) {
        			gethp <<= 1;// 워터라이프경우두배 //원래 랜덤수치 30임
        		}
        		_targetPc.setCurrentHp(_targetPc.getCurrentHp() + gethp);
        		_targetPc.send_effect(18930);
	        }
			
			// 전사스킬 : 타이탄 매직
			if (_targetPc.isPassiveStatus(L1PassiveId.TITAN_MAGIC) && _targetPc.getPassiveSkill().isTitan(_calcType == 1 ? _pc : _npc, TitanType.MAGIC, _calcType)) {
				_damageHit = false;
				return (int) 0;
			}
		} else { //PC -> NPC
			if (damage > _targetNpc.getCurrentHp()) {
				damage = _targetNpc.getCurrentHp();
			}
		}
		
		if (_calcType == PC_PC) {
			// 마법사 스택 시스템
			if (_pc.isWizard()) {
				long currentTime = System.currentTimeMillis();
				if (_pc._spellStackTargetId != _targetPc.getId()) {// 동일 타켓
					_pc._spellStackTargetId = _targetPc.getId();
					_pc._spellStackTargetCount = 0;
				} else {
					if (currentTime <= _pc._spellStackTargetTime + Config.SPELL.WIZARD_STACK_TIME) {// 2초안에 적중시
						if (_pc._spellStackTargetCount < Config.SPELL.WIZARD_STACK_COUNT) {
							_pc._spellStackTargetCount++;// 스택 카운트 증가
						}
						if (_pc._spellStackTargetCount > 0) {
							damage = (int)(damage * (1 + (_pc._spellStackTargetCount * Config.SPELL.WIZARD_STACK_VALUE)));// 대미지 증폭
						}
					} else {
						_pc._spellStackTargetCount = 0;// 카운팅 초기화
					}
				}
				_pc._spellStackTargetTime = currentTime;// 시간 초기화
			}
						
			/** 로봇시스템 **/
			if (_targetPc.getRobotAi() != null && (_targetPc.noPlayerCK || _targetPc.isGm())) {
				if (_targetPc != null && _targetPc.getClanid() != 0 && !_targetPc.getMap().isSafetyZone(_targetPc.getLocation())) {
					_targetPc.getRobotAi().getAttackList().add(_pc, 0);
				} else if (!_targetPc.getMap().isSafetyZone(_targetPc.getLocation()) && _targetPc.getMap().isTeleportable()) {
					L1Location newLocation = _targetPc.getLocation().randomLocation(200, true);
					_targetPc.getTeleport().start(newLocation, _targetPc.getMoveState().getHeading(), true);
				}
			}
		}
		if (damage <= 0) {
			_damageHit = false;
		}
		return damage;
	}

	public int calcPcFireWallDamage() {
		int dmg = 0;
		L1Skills l1skills = SkillsTable.getTemplate(FIRE_WALL);
		dmg = calcAttrDefence(l1skills.getDamageValue(), L1Attr.FIRE);
		if (_targetPc.getSkill().hasSkillEffect(ABSOLUTE_BARRIER) || _targetPc.isBind()) {
			dmg = 0;
		}
		if (dmg < 0) {
			dmg = 0;
		}
		return dmg;
	}

	public int calcNpcFireWallDamage() {
		int dmg = 0;
		L1Skills l1skills = SkillsTable.getTemplate(FIRE_WALL);
		dmg = calcAttrDefence(l1skills.getDamageValue(), L1Attr.FIRE);
		if (_targetNpc.isBind()) {
			dmg = 0;
		}
		if (dmg < 0) {
			dmg = 0;
		}
		return dmg;
	}

	int calcPcMagicDamage(int skillId) {// PC가 받는 대미지
		if (_targetPc.getTeleport().isTeleport()) {
			return 0;
		}
		int dmg = calcMagicDiceDamage(skillId);
		dmg = (dmg * _leverage) / 10;
		if (_targetPc.ability.getDamageReductionPercent() > 0) {
			dmg -= (int)(((double)dmg * 0.01D) * _targetPc.ability.getDamageReductionPercent());// 대미지 감소 퍼센트
		}
		/** 대미지 감소 **/
		int reduc = _targetPc.ability.getDamageReduction();

		if (_calcType == PC_PC && _targetPc.ability.getPVPMagicDamageReduction() > 0) {
			reduc += _targetPc.ability.getPVPMagicDamageReduction() - _pc.ability.getPVPMagicDamageReductionEgnor();
			if (_targetPc.ability.getAbnormalStatusPVPDamageReduction() > 0 
					&& (_targetPc.isStun() || _targetPc.isHold() || _targetPc.isPressureDeathRecall()
					|| _targetPc.isDesperado() || _targetPc.isOsiris() || _targetPc.isPhantom() || _targetPc.isShockAttackTeleport()
					|| _targetPc.isEternity() || _targetPc.isShadowStepChaser() || _targetPc.isBehemoth())) {
				reduc += _targetPc.ability.getAbnormalStatusPVPDamageReduction();
			}
		}
		if ((_calcType == PC_PC || _calcType == NPC_PC) && _targetPc.ability.getMagicDamageReduction() > 0) {
			reduc += _targetPc.ability.getMagicDamageReduction();
			if (_targetPc.ability.getAbnormalStatusDamageReduction() > 0 
					&& (_targetPc.isStun() || _targetPc.isHold() || _targetPc.isPressureDeathRecall()
					|| _targetPc.isDesperado() || _targetPc.isOsiris() || _targetPc.isPhantom() || _targetPc.isShockAttackTeleport()
					|| _targetPc.isEternity() || _targetPc.isShadowStepChaser() || _targetPc.isBehemoth())) {
				reduc += _targetPc.ability.getAbnormalStatusDamageReduction();
			}
		}
		
		if (_targetPc.isPassiveStatus(L1PassiveId.INFINITI_ARMOR) && _targetLevel >= 45) {
        	int infinitiarmor = 1 + ((_targetLevel + ~0x0000002C) >> 2);
        	if (infinitiarmor > 15) {
        		infinitiarmor = 15;
        	}
        	reduc += infinitiarmor;
        }
		if (_targetPc.getSkill().hasSkillEffect(REDUCTION_ARMOR)) {
			int targetPcLvl = _targetLevel;
			if (targetPcLvl < 50) {
				targetPcLvl = 50;
			}
			reduc += (targetPcLvl + ~0x00000031) / 5 + 1;
		}
		if (_targetPc.getSkill().hasSkillEffect(EARTH_GUARDIAN)) {
			reduc += 2 + (_targetLevel > 80 ? (_targetLevel + ~0x0000004F) >> 2 : 0);
		}
		if (_targetPc.getSkill().hasSkillEffect(PATIENCE)) {
			reduc += _targetLevel >= 80 ? 2 + ((_targetLevel + ~0x0000004F) >> 2) : 2;
		}
		if (_targetPc.isPassiveStatus(L1PassiveId.DRAGON_SKIN)) {
			reduc += _targetLevel >= 80 ? 5 + ((_targetLevel + ~0x0000004F) >> 1) : 5;
		}
		if (_targetPc.isPassiveStatus(L1PassiveId.MAJESTY)) {
			reduc += _targetLevel >= 80 ? 2 + ((_targetLevel + ~0x0000004F) >> 1) : 2;
		}
		
		if (_calcType == PC_PC) {
			reduc -= _pc.ability.getDamageReductionEgnor();// 대미지 무시
		}
		if (reduc < 0) {
			reduc = 0;
		}
		dmg -= reduc;
		
		if (_calcType == PC_PC && _targetPc.ability.getPVPDamageReductionPercent() > 0 && dmg > 0) {
			dmg -= (int)(((double)dmg * 0.01D) * _targetPc.ability.getPVPDamageReductionPercent());// PVP대미지 감소 퍼센트
		}

		if (_targetPc.getSkill().hasSkillEffect(SHADOW_ARMOR) && _targetPc.isPassiveStatus(L1PassiveId.SHADOW_ARMOR_DESTINY)) {
			int add = 5 + (_targetLevel > 85 ? (_targetLevel + ~0x00000054) >> 1 : 0);
			if (add > 10) {
				add = 10;
			}
			dmg -= (dmg / 100) * add;
		}
		
		if (_targetPc.getSkill().hasSkillEffect(IMMUNE_TO_HARM)) {
			dmg -= _calcType == PC_PC && _pc.ability.getEmunEgnor() > 0 ? (dmg * (_targetPc.immunToHarmValue * (1D - ((double) _pc.ability.getEmunEgnor() * 0.01D)))) : (dmg * _targetPc.immunToHarmValue);
		}
		if (_targetPc.getSkill().hasSkillEffect(LUCIFER)) {
			dmg -= (dmg * 0.1);
		}
		if (_calcType == PC_PC) {
			if (_targetPc.getSkill().hasSkillEffect(IMMUNE_TO_HARM) && _targetPc._isImmunToHarmSaint) {
				int saint = 0;
	        	if (_targetLevel >= 80) {
	        		saint += (int) _targetLevel >> 1;
	        		if (saint > 5) {
	        			saint = 5;
	        		}
	        		if (_targetLevel >= 90) {
	        			saint += _targetLevel + ~0x00000059;
	        		}
	        		if (saint > 10) {
	        			saint = 10;
	        		}
	        	}
	        	if (saint > 0) {
	        		dmg -= saint;
	        	}
			}
			if (_targetPc.getSkill().hasSkillEffect(LUCIFER) && _targetPc.isPassiveStatus(L1PassiveId.LUCIFER_DESTINY)) {
				int lucifer = (int) _targetLevel >> 1;
	        	if (lucifer > 10) {
	        		lucifer = 10;
	        	}
	        	if (lucifer > 0) {
	        		dmg -= lucifer;
	        	}
			}
			
			dmg += getBalanceMagicDmg(_pc.getType(), _targetPc.getType());
		}
		// 마안 일정 확률로 마법대미지 50프로 감소
		if ((_calcType == PC_PC || _calcType == NPC_PC)
				&& (_targetPc.getSkill().hasSkillEffect(FAFU_MAAN) 
						|| _targetPc.getSkill().hasSkillEffect(SHAPE_MAAN) 
						|| _targetPc.getSkill().hasSkillEffect(BIRTH_MAAN) 
						|| _targetPc.getSkill().hasSkillEffect(LIFE_MAAN) 
						|| _targetPc.getSkill().hasSkillEffect(ABSOLUTE_MAAN))
				&& (int)(Math.random() * 100) + 1 <= 10) {
			dmg >>= 1;// 확률
		}
		
		/** 마법사 일경우 대미지외부화 적용 */
		if (_calcType == PC_PC && _pc.isWizard()) {
			dmg *= Config.SPELL.WIZARD_MAGIC_DMG_PVP;
		}
		
		if (_calcType == NPC_PC) {
			boolean isPet		= _npc instanceof L1PetInstance;
			boolean isSummon	= _npc instanceof L1SummonInstance;
			if (isPet || isSummon) {
				boolean isNowWar = false;
				int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
				if (castleId > 0) {
					isNowWar = War.getInstance().isNowWar(castleId);
				}
				if (!isNowWar) {
					if (isPet) {
						dmg >>= 3;
					} else if (isSummon && ((L1SummonInstance) _npc).isExsistMaster()) {
						dmg >>= 3;
					}
				}
			}
			
			MapBalanceData mabBalance = _npc.getMap().getBalance();
			if (mabBalance != null) {
				dmg *= mabBalance.getDamageValue(BalanceType.MAGIC);
			}
			dmg += getBalanceMagicDmg(-1, _targetPc.getType());
			if (_npc._statusDistroyHorror) {
				dmg += ~0x00000000;
			}
			
			int dmgModiNpc2Pc = _npc.getMap().getDmgModiNpc2Pc();
	        if (dmgModiNpc2Pc > 0) {
	        	dmg *= dmgModiNpc2Pc * 0.01;
	        }
		}

		if (_calcType == PC_PC) {
			int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
			
			/*** 신규레벨보호 ***/
			if (Config.ALT.BEGINNER_SAFE_LEVEL > 0) {
				if (castle_id == 0 && (_targetLevel < Config.ALT.BEGINNER_SAFE_LEVEL || _attackerLevel < Config.ALT.BEGINNER_SAFE_LEVEL)) {
					dmg >>= 1;
//AUTO SRM: 					S_SystemMessage message = new S_SystemMessage("신규 레벨은 대미지의 50%만 가해집니다."); // CHECKED OK
					S_SystemMessage message = new S_SystemMessage(S_SystemMessage.getRefText(901), true);
					_pc.sendPackets(message, false);
					_targetPc.sendPackets(message, true);
				}
			}
			
			/** 신규혈맹 공격안되게 **/
			if (castle_id == 0 && (_pc.getClanid() == Config.PLEDGE.BEGINNER_PLEDGE_ID || _targetPc.getClanid() == Config.PLEDGE.BEGINNER_PLEDGE_ID)) {
				if (Config.PLEDGE.BEGINNER_PLEDGE_PVP_TYPE) {
					dmg = 0;
//AUTO SRM: 					S_SystemMessage message = new S_SystemMessage("신규보호 혈맹은 상호간에 공격이 되지 않습니다"); // CHECKED OK
					S_SystemMessage message = new S_SystemMessage(S_SystemMessage.getRefText(1102), true);
					_pc.sendPackets(message, false);
					_targetPc.sendPackets(message, true);
				} else {
					dmg >>= 1;
//AUTO SRM: 					S_SystemMessage message = new S_SystemMessage("신규보호혈맹은 대미지를 50%만 가해집니다."); // CHECKED OK
					S_SystemMessage message = new S_SystemMessage(S_SystemMessage.getRefText(1103), true);
					_pc.sendPackets(message, false);
					_targetPc.sendPackets(message, true);
				}
			}
			
			/** 신규보호존 공격안되게 **/
			if (Config.ALT.BEGINNER_MAP_LIST.contains(_pc.getMapId()) || Config.ALT.BEGINNER_MAP_LIST.contains(_targetPc.getMapId())) {
				dmg = 0;
//AUTO SRM: 				S_SystemMessage message = new S_SystemMessage("신규보호존은 상호간에 공격이 되지 않습니다"); // CHECKED OK
				S_SystemMessage message = new S_SystemMessage(S_SystemMessage.getRefText(1104), true);
				_pc.sendPackets(message, false);
				_targetPc.sendPackets(message, true);
			}
			
			if (_pc.getMapId() == 5153 && (_pc.getConfig().getDuelLine() == _targetPc.getConfig().getDuelLine() || _pc.getConfig().getDuelLine() == 0)) {
				dmg = 0;// 배틀존
			}
		}

		if (dmg < 0) {
			dmg = 0;
		}
		return dmg;
	}

	/** 플레이어·NPC 로부터 NPC 에의 대미지 산출 **/
	int calcNpcMagicDamage(int skillId) { //NPC가 받는 대미지
		int dmg = calcMagicDiceDamage(skillId);
		dmg = (dmg * _leverage) / 10;
		
		if (_calcType == PC_NPC) {
			boolean isPet		= _targetNpc instanceof L1PetInstance;
			boolean isSummon	= _targetNpc instanceof L1SummonInstance;
			if (isPet || isSummon) {
				boolean isNowWar = false;
				int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
				if (castleId > 0) {
					isNowWar = War.getInstance().isNowWar(castleId);
				}
				if (!isNowWar) {
					if (isPet) {
						dmg >>= 3;
					} else if (isSummon && ((L1SummonInstance)_targetNpc).isExsistMaster()) {
						dmg >>= 3;
					}
				}
			}
			
			if (_pc.isWizard()) {
				dmg *= Config.SPELL.WIZARD_MAGIC_DMG_PVE;// 마법사 일경우 대미지외부화 적용
			}
			MapBalanceData mabBalance = _targetNpc.getMap().getBalance();
			if (mabBalance != null) {
				dmg = (int) (dmg * mabBalance.getReductionValue(BalanceType.MAGIC));
			}
			
			int dmgModiPc2Npc = _pc.getMap().getDmgModiPc2Npc();
	        if (dmgModiPc2Npc > 0) {
	        	dmg *= dmgModiPc2Npc * 0.01;
	        }
			
			dmg += getBalanceMagicDmg(_pc.getType(), -1);
		} else if (_calcType == NPC_NPC) {
			dmg += getBalanceMagicDmg(-1, -1);
		}

		if (_calcType == PC_NPC && _targetNpc != null) {
			int npcId = _targetNpc.getNpcTemplate().getNpcId();
			int sprite_id = _targetNpc.getNpcTemplate().getSpriteId();
			if (npcId >= 45912 && npcId <= 45915 && !_pc.getSkill().hasSkillEffect(STATUS_HOLY_WATER)) {
				dmg = 0;
			}
			if (npcId == 45916 && !_pc.getSkill().hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
				dmg = 0;
			}
			if (npcId == 45941 && !_pc.getSkill().hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
				dmg = 0;
			}
			if (Config.ALT.KARMA_BUFF_ENABLE) {
			    if (L1MonsterInstance.KARMA_BALROG_BOSS_IDS.contains(npcId) && !_pc.getSkill().hasSkillEffect(STATUS_CURSE_BARLOG)) {
			    	dmg = 0;
			    }
			    if (L1MonsterInstance.KARMA_YAHEE_BOSS_IDS.contains(npcId) && !_pc.getSkill().hasSkillEffect(STATUS_CURSE_YAHEE)) {
			    	dmg = 0;
			    }
			}
			if ((npcId >= 5000103 && npcId <= 5000104) && !_pc.getSkill().hasSkillEffect(DETHNIGHT_BUNNO)) {
				dmg = 0;
			}
			if (NOT_ENABLE_NPC_IDS.contains(npcId)) {
				dmg = 0;
			}
			if (npcId >= 46068 && npcId <= 46091 && _pc.getSpriteId() == 6035) {
				dmg = 0;
			}
			if (npcId >= 46092 && npcId <= 46106 && _pc.getSpriteId() == 6034) {
				dmg = 0;
			}
			if (npcId != 50087 && sprite_id == 7684 && !_pc.getSkill().hasSkillEffect(PAP_FIVEPEARLBUFF)) {
				dmg = 0;
			}
			if (sprite_id == 7805 && !_pc.getSkill().hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
				dmg = 0;
			}
			if ((sprite_id == 7864 || sprite_id == 7869 || sprite_id== 7870)) {
				dmg += dmg >> 1;
			}
		}
		return dmg;
	}

	int calcMagicDiceDamage(int skillId) {
		L1Skills l1skills	= SkillsTable.getTemplate(skillId);
		int dice			= l1skills.getDamageDice();
		int diceCount		= l1skills.getDamageDiceCount();
		int damageValue		= l1skills.getDamageValue();
		int magicDamage		= 0;
		double coefficient	= 0; // PC마법상수
		if (_calcType == PC_PC) {
			dice += getSpellPower();
			for (int i = 0; i < diceCount; i++) {
				magicDamage += (random.nextInt(dice) + 1);
			}
			magicDamage += damageValue * (1 + getSpellPower() / 10);
		} else if ( _calcType == PC_NPC) {
			int PowerSp =	_pc.ability.getSp() - getMagicLevel();
			int PowerInt =	_pc.ability.getTotalInt() - getMagicBonus();
			for (int i = 0; i < diceCount; i++) {
				magicDamage += (random.nextInt(dice) + 1);
			}
			magicDamage += damageValue + ~0x00000004;
			coefficient = (1.0 + (PowerSp + 1) * 0.15 + (PowerInt + ~0x00000008) * 0.2);
			if (coefficient < 1) {
				coefficient = 1;
			}
			magicDamage *= coefficient; // 기본 마법데미지에 마법상수 곱하기
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {  // 이부분이 몹이 유저 또는 몹 몹
			dice += getSpellPower() >> 1;
			for (int i = 0; i < diceCount; i++) {
				magicDamage += (random.nextInt(dice) + 1);
			}
			magicDamage += damageValue * (1 + getSpellPower() / Config.ETC.MONSTER_MAGIC_DMG);
		}
		
		// 치명타 발생
		int rnd = random.nextInt(100) + 1;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int probCritical = CalcIntelStat.magicCritical(_pc.ability.getTotalInt()) + _pc.ability.getMagicCritical();
			switch (skillId) {
			// 6레벨 이하 광역마법 제외한 공격마법
			case ENERGY_BOLT:
			case ICE_DAGGER:
			case WIND_CUTTER:
			case CHILL_TOUCH:
			case SMASH:
			case FIRE_ARROW:
			case STALAC:
			case VAMPIRIC_TOUCH:
			case CONE_OF_COLD:
			case CALL_LIGHTNING:
				probCritical += 10;
				break;
			}
			if (L1ArmorSkill.valakasArmor(_pc, null, true)) {
				probCritical = 100;
			}
			if (criticalOccur(probCritical)) {
				magicDamage += magicDamage >> 1;// 1.5배
				if (_pc.ability.getMagicCriticalDmgAdd() > 0) {// 마법 치명타 대미지 상승
					magicDamage += magicDamage >> 3;// 1.12배
				}
			}
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			double criticalCoefficient = 1.4D;
	        if (_npc instanceof L1PetInstance) {
                L1PetInstance pet = (L1PetInstance)_npc;
                if (pet.isCombo()) {
                    if (pet.getComboTarget() == _target.getId()) {
                        pet.setComboCount(pet.getComboCount() + 1);
                    } else {
                        pet.setComboTarget(_target.getId());
                        pet.setComboCount(1);
                    }
                    magicDamage += (pet.getComboCount() * 10.0D * pet.get_comboDmgMulti() * 0.01D);
                    if (pet.getComboCount() >= 3) {
                        pet.setComboCount(0);
                        _target.broadcastPacket(new S_Effect(_target.getId(), 17326), true);
                    }
                }
                magicDamage += pet.ability.getMagicDmgup();
                if (rnd <= pet.ability.getMagicCritical()) {
                    magicDamage *= criticalCoefficient;
                }
            } else if (rnd <= 15) {
				magicDamage *= criticalCoefficient;
            }
		}

		double attrDeffence = calcAttrResistance(l1skills.getAttr());

		magicDamage -= magicDamage * attrDeffence; // 마방에 의한 데미지 감소후 속성방어에 의한
		// 데미지 감소 처리
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int PowerSp = _pc.getAbility().getSp() - getMagicLevel();
			int PowerInt = _pc.getAbility().getTotalInt() - getMagicBonus();
			magicDamage += _pc.getBaseMagicDmg() * (PowerSp + 1) + PowerInt; // 베이스 스탯 마법 데미지 보너스 추가
			//System.out.println(" 매직 " + magicDamage + " 스펠 " + PowerSp + " 인트 " + PowerInt);
		}
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int weaponAddDmg = 0;
			L1ItemInstance weapon = _pc.getWeapon();
			if (weapon != null) {
				weaponAddDmg = weapon.getItem().getMagicDmgModifier();
			}
			magicDamage += weaponAddDmg; // 무기에 의한 마법 데미지 추가
		//	System.out.println("  무기대한  " + weaponAddDmg);
		}
		return magicDamage;
	}

	/**
	 * 힐계열 스킬 설정
	 * @param skill
	 * @return int
	 */
	public int calcHealing(L1Skills skill) {
		int dice		= skill.getDamageDice();
		int damageValue	= skill.getDamageValue();
		int magicDamage	= 0;
		
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicDamage += (_pc.getAbility().getSp() + _pc.getAbility().getTotalInt()) * 0.1D;
		}
		int magicBonus = getMagicBonus();
		if (magicBonus > 10) {
			magicBonus = 10;
		}

		int diceCount = damageValue + magicBonus;
		for (int i = 0; i < diceCount; i++) {
			magicDamage += (random.nextInt(dice) + 1);
		}

		double alignmentRevision = 1.0D;
		if (getAlignment() > 0) {
			alignmentRevision += (getAlignment() / 32768.0);
		}

		magicDamage *= alignmentRevision;
		return (magicDamage * _leverage) / 10;
	}

	/**
	 * MR에 의한 마법 대미지 감소를 처리 한다
	 * 
	 * @param dmg
	 * @return dmg
	 */
	public int calcMrDefense(int dmg) {
		int magic_resistance = getTargetMr();
		if (magic_resistance < 10) {
			return dmg;
		}
		double rate = 0.0D;
		if (magic_resistance < 20) {
			rate = 0.05D;
		} else if (magic_resistance < 25) {
			rate = 0.06D;
		} else if (magic_resistance < 30) {
			rate = 0.07D;
		} else if (magic_resistance < 35) {
			rate = 0.08D;
		} else if (magic_resistance < 40) {
			rate = 0.1D;
		} else if (magic_resistance < 45) {
			rate = 0.11D;
		} else if (magic_resistance < 50) {
			rate = 0.12D;
		} else if (magic_resistance < 55) {
			rate = 0.14D;
		} else if (magic_resistance < 60) {
			rate = 0.17D;
		} else if (magic_resistance < 65) {
			rate = 0.18D;
		} else if (magic_resistance < 70) {
			rate = 0.20D;
		} else if (magic_resistance < 75) {
			rate = 0.21D;
		} else if (magic_resistance < 80) {
			rate = 0.22D;
		} else if (magic_resistance < 85) {
			rate = 0.23D;
		} else if (magic_resistance < 90) {
			rate = 0.25D;
		} else if (magic_resistance < 95) {
			rate = 0.26D;
		} else if (magic_resistance < 100) {
			rate = 0.27D;
		} else if (magic_resistance < 105) {
			rate = 0.28D;
		} else if (magic_resistance < 110) {
			rate = 0.30D;
		} else if (magic_resistance < 115) {
			rate = 0.31D;
		} else if (magic_resistance < 120) {
			rate = 0.32D;
		} else if (magic_resistance < 125) {
			rate = 0.33D;
		} else if (magic_resistance < 130) {
			rate = 0.34D;
		} else if (magic_resistance < 135) {
			rate = 0.35D;
		} else if (magic_resistance < 140) {
			rate = 0.36D;
		} else if (magic_resistance < 145) {
			rate = 0.37D;
		} else if (magic_resistance < 150) {
			rate = 0.38D;
		} else if (magic_resistance < 155) {
			rate = 0.39D;
		} else if (magic_resistance < 160) {
			rate = 0.40D;
		} else if (magic_resistance < 165) {
			rate = 0.41D;
		} else if (magic_resistance < 170) {
			rate = 0.42D;
		} else if (magic_resistance < 175) {
			rate = 0.43D;
		} else if (magic_resistance < 180) {
			rate = 0.44D;
		} else if (magic_resistance < 185) {
			rate = 0.45D;
		} else if (magic_resistance < 190) {
			rate = 0.46D;
		} else if (magic_resistance < 195) {
			rate = 0.47D;
		} else if (magic_resistance < 200) {
			rate = 0.48D;
		} else if (magic_resistance < 210) {
			rate = 0.49D;
		} else if (magic_resistance < 220) {
			rate = 0.5D;
		} else {
			rate = 0.51D;
		}
		dmg -= dmg * rate;
		return dmg;
	}

	boolean criticalOccur(int prob) {
		if (prob == 0) {
			return false;
		}
		if (random.nextInt(100) + 1 <= prob) {
			return _criticalDamage = true;
		}
		return _criticalDamage;
	}

	double calcAttrResistance(L1Attr attr) {
		int resist = 0, resistFloor = 0;
		switch (attr) {
		case EARTH:	resist = _target.getResistance().getEarth();break;
		case FIRE:	resist = _target.getResistance().getFire();break;
		case WATER:	resist = _target.getResistance().getWater();break;
		case WIND:	resist = _target.getResistance().getWind();break;
		default:break;
		}
		if (resist < 0) {
			resistFloor = (int) (-0.45 * Math.abs(resist));
		} else if (resist < 101) {
			resistFloor = (int) (0.45 * Math.abs(resist));
		} else {
			resistFloor = (int) (45 + 0.09 * Math.abs(resist));// 속성100초과분에 대해0.45의 1/5정도 감소되게 변경
		}
		return resistFloor / 100;
	}

	int calcAttrDefence(int dmg, L1Attr attr) {
		if (dmg < 1) {
			return dmg;
		}
		int resist = 0;
		switch (attr) {
		case EARTH:	resist = _target.getResistance().getEarth();break;
		case FIRE:	resist = _target.getResistance().getFire();break;
		case WATER:	resist = _target.getResistance().getWater();break;
		case WIND:	resist = _target.getResistance().getWind();break;
		default:break;
		}

		dmg -= resist >> 1;
		if (dmg < 1) {
			dmg = 1;
		}
		return dmg;
	}

	public void commit(int damage, int drainMana) {
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			commitPc(damage, drainMana);
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			commitNpc(damage, drainMana);
		}

		if (!Config.ALT.ALT_ATKMSG) {
			return;
		}
		if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
			return;
		}
		if ((_calcType == PC_PC || _calcType == NPC_PC) && !_targetPc.isGm()) {
			return;
		}
		
		String msg0 = StringUtil.EmptyString;
		String msg2 = StringUtil.EmptyString;
		String msg3 = StringUtil.EmptyString;
		String msg4 = StringUtil.EmptyString;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			msg0 = _pc.getName();
		} else if (_calcType == NPC_PC) {
			msg0 = _npc.getName();
		}
		if (_calcType == NPC_PC || _calcType == PC_PC) {
			msg4 = _targetPc.getName();
			msg2 = "HP:" + _targetPc.getCurrentHp();
		} else if (_calcType == PC_NPC) {
			msg4 = _targetNpc.getName();
			msg2 = "HP:" + _targetNpc.getCurrentHp();
		}
		msg3 = "DMG:" + damage;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			_pc.sendPackets(new S_SystemMessage("\\fR[" + msg0 + "->" + msg4 + "] " + msg3 + " / " + msg2, true), true);
		}
		if (_calcType == NPC_PC || _calcType == PC_PC) {
			_targetPc.sendPackets(new S_SystemMessage("\\fY[" + msg0 + "->" + msg4 + "] " + msg3 + " / " + msg2, true), true);
		}
	}

	void commitPc(int damage, int drainMana) {
		if (_targetPc.getSkill().hasSkillEffect(ABSOLUTE_BARRIER) || _targetPc.isBind()) {
			damage = drainMana = 0;
		}

		if (_calcType == PC_PC) {
			if (drainMana > 0 && _targetPc.getCurrentMp() > 0) {
				if (drainMana > _targetPc.getCurrentMp()) {
					drainMana = _targetPc.getCurrentMp();
				}
				int newMp = _pc.getCurrentMp() + drainMana;
				_pc.setCurrentMp(newMp);
			}
			_targetPc.receiveManaDamage(_pc, drainMana);
			if (damage > 0 && _targetPc.isCrown() && _targetPc.getSkill().hasSkillEffect(BRAVE_UNION)) {
				_targetPc.receiveDamageFromBraveUnion(_pc, damage);
			} else {
				_targetPc.receiveDamage(_pc, damage);
			}
		} else if (_calcType == NPC_PC) {
			_targetPc.receiveDamage(_npc, damage);
		}
	}

	void commitNpc(int damage, int drainMana) {
		boolean faile = false, faileOneDmg = false;
		int sprite_id = _targetNpc.getNpcTemplate().getSpriteId();
		if (_targetNpc.isBind()) {
			faile = true;
		}
		int npcId = _targetNpc.getNpcTemplate().getNpcId();
		if (NOT_ENABLE_NPC_IDS.contains(npcId)) {
			faile = true;
		}
		if (npcId >= 45912 && npcId <= 45915 && !_pc.getSkill().hasSkillEffect(STATUS_HOLY_WATER)) {
			faile = true;
		}
		if (npcId == 45916 && !_pc.getSkill().hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
			faile = true;
		}
		if (npcId == 45941 && !_pc.getSkill().hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
			faile = true;
		}
		if (Config.ALT.KARMA_BUFF_ENABLE) {
		    if (L1MonsterInstance.KARMA_BALROG_BOSS_IDS.contains(npcId) && !_pc.getSkill().hasSkillEffect(STATUS_CURSE_BARLOG)) {
		    	faile = true;
		    }
		    if (L1MonsterInstance.KARMA_YAHEE_BOSS_IDS.contains(npcId) && !_pc.getSkill().hasSkillEffect(STATUS_CURSE_YAHEE)) {
		    	faile = true;
		    }
		}
		if (npcId != 50087 && sprite_id == 7684 && !_pc.getSkill().hasSkillEffect(PAP_FIVEPEARLBUFF)) {
			faile = true;
		} else if (npcId != 50087 && sprite_id == 7684 && _pc.getSkill().hasSkillEffect(PAP_FIVEPEARLBUFF)) {
			faileOneDmg = true;
		}
		if (sprite_id == 7805 && !_pc.getSkill().hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
			faile = true;
		} else if (sprite_id == 7805 && _pc.getSkill().hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
			faileOneDmg = true;
		}
		if (sprite_id == 7720) {
			faileOneDmg = true;
		}
		if ((npcId >= 5000103 && npcId <= 5000104) && !_pc.getSkill().hasSkillEffect(DETHNIGHT_BUNNO)) {
			faile = true;
		} else if(npcId >= 5000103 && npcId <= 5000104 && _pc.getSkill().hasSkillEffect(DETHNIGHT_BUNNO)) {
			faileOneDmg = true;
		}
		
		if (_targetNpc instanceof L1MonsterInstance) {
			L1MonsterInstance mon = (L1MonsterInstance) _targetNpc;
			if (mon.kirtasCounterMagic || mon.titanCounterMagic) {
				_pc.receiveDamage(_targetNpc, damage);
				faile = true;
			} else if (mon.kirtasAbsolute || mon._vallacasFly) {
				faile = true;
			} else if ((npcId >= 800550 && npcId <= 800555) && (mon.getMapId() >= 15482 && mon.getMapId() <= 15484) && (_pc.getClanid() == 0 || _pc.getClan().getCastleId() != 0)) {
				faile = true;
			}
		}
		
		if (faile) {
			damage = drainMana = 0;
		}
		if (faileOneDmg) {
			damage = 1;
			drainMana = 0;
		}

		if (_calcType == PC_NPC) {
			if (drainMana > 0) {
				int drainValue = _targetNpc.drainMana(drainMana);
				int newMp = _pc.getCurrentMp() + drainValue;
				_pc.setCurrentMp(newMp);
			}
			_targetNpc.receiveManaDamage(_pc, drainMana);
			_targetNpc.receiveDamage(_pc, damage);
		} else if (_calcType == NPC_NPC) {
			_targetNpc.receiveDamage(_npc, damage);
		}
	}
}


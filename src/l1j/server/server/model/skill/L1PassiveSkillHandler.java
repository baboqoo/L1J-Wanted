package l1j.server.server.model.skill;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.Config;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;
import l1j.server.server.serverpackets.spell.S_SlayerDelay;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

/**
 * PassiveSkill Handler
 * @author LinOffice
 */
public class L1PassiveSkillHandler {
	private static final Random random = new Random(System.nanoTime());
	
	/**
	 * 타이탄계열 설정
	 */
	public static enum TitanType {
    	LOCK(	12555,	Config.SPELL.TITAN_LOCK), // 타이탄 락
    	BLICK(	12557,	Config.SPELL.TITAN_BLICK),// 타이탄 블릿
    	MAGIC(	12559,	Config.SPELL.TITAN_MAGIC),// 타이탄 매직
    	;
    	private final int _effect, _prob;
    	TitanType(int effect, int prob) {
    		_effect	= effect;
    		_prob	= prob;
		}
    }
	
	private boolean _isBerserkForceActive;// 버서크 강화 효과
	private int _advanceSpiritHpValue, _advanceSpiritMpValue;
	private int _giganticHpValue;
	private int _infinitiBloodHpValue, _infinitiBlickErValue, _infinitiDodgeDgValue;
	private int _prideHpValue;
    private int _mortalBodyHpValue;
    private int _tacticalAdvanceBonusValue;
    private int _reductionArmorVeteranPVPReducValue;
    private int _raigingForceHitupSkillValue, _raigingForcePVPReducValue;
    private int _shiningArmorValue;
    
    private long _titanBeastTime;
    
    private final L1PcInstance _owner;
    private final ArrayList<L1PassiveId> _passives;
    
	public L1PassiveSkillHandler(L1PcInstance owner){
		_owner		= owner;
		_passives	= new ArrayList<L1PassiveId>();
	}
	
	/**
	 * 캐릭터의 패시브 스킬 습득 여부
	 * @param L1PassiveId
	 * @return boolean
	 */
	public boolean isStatus(L1PassiveId passive){
		return _passives.contains(passive);
	}
	
	/**
	 * 버서커 액션
	 */
    public void doBerserk(){
    	final boolean force = _owner.isHalfHp();
    	if (force && !_isBerserkForceActive) {
    		setBerserk(true);
    	} else if (!force && _isBerserkForceActive) {
    		setBerserk(false);
    	}
    }
    
    /**
     * 버서커 능력치 설정
     */
    void setBerserk(boolean flag){
    	_isBerserkForceActive = flag;
		_owner.getResistance().addToleranceAll(flag ? 5 : -5);
		_owner.getAbility().addPVPDamage(flag ? 5 : -5);
		_owner.getAbility().addPVPDamageReduction(flag ? 8 : -8);
		_owner.sendPackets(flag ? S_SpellBuffNoti.BERSERK_FORCE_ON : S_SpellBuffNoti.BERSERK_NORMAL_ON);
    }
    
    /**
     * 타이탄 발동을 통재한다.
     * @param attacker
     * @param TitanType
     * @param calcType
     * @return boolean
     */
    public boolean isTitan(L1Character attacker, TitanType titanType, int calcType){
    	boolean is_demolition = isStatus(L1PassiveId.DEMOLITION);
 		if (_owner.isStun() && !is_demolition) {
 			return false;// 데몰리션 없을때 스턴상태 불가
 		}
 		int titan_prob			= titanType._prob + _owner.getAbility().getTitanUp() + (titanType == TitanType.LOCK && _owner.getLevel() >= 92 ? (_owner.getLevel() + ~0x00000059) >> 1 : 0);
 		if (titan_prob > 22) {
 			titan_prob = 22;
 		}
 		final boolean halfHp	= _owner.isHalfHp();
 		if (halfHp && _owner.getSkill().hasSkillEffect(L1SkillId.TITAN_RISING)) {
 			titan_prob += 5;// 50%미만일때 타이탄 확률 상승
 		}
 		if (random.nextInt(100) + 1 <= titan_prob) {
 			if (halfHp && !_owner.getInventory().consumeItem(L1ItemId.GEMSTONE, 5)) {
 				return false;// 50%이하일때 결정체 소모
 			}
 			if (calcType == 1) {// pc to pc
 				L1PcInstance attackPc = (L1PcInstance)attacker;
 				if (titanType == TitanType.LOCK && attackPc.is_reflect_emasculate(is_demolition ? L1PassiveId.DEMOLITION : null)) {
 					_owner.send_effect_self(titanType._effect);
 	        		return false;
 	        	}
 				attackPc.receiveCountingDamage(_owner, calcTitanDamage());
 			} else if (calcType == 3) {// npc to pc
 				((L1NpcInstance)attacker).receiveCountingDamage(_owner, calcTitanDamage());
    		}
 			_owner.send_effect_self(titanType._effect);
 			return true;
 		}
 		return false;
 	}
    
    /**
     * 타이탄 발동 대미지 반환
     * @return damage
     */
    int calcTitanDamage() {
        L1ItemInstance weapon = _owner.getWeapon();
        return weapon == null ? 0 : (int) Math.round((weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgRate()) << 1);
    }
    
    /**
     * 타이탄 비스트 시간 측정
     * @return boolean
     */
    public boolean isTitanBeastTime(){
    	long currentTime = System.currentTimeMillis();
    	if (_titanBeastTime + Config.SPELL.TITAN_BEAST_MILLISECOND > currentTime) {
    		_owner.send_effect_self(20571);
        	return true;
    	}
    	_titanBeastTime = currentTime;
    	return false;
    }
    
    /**
     * 블라인트 하이딩 어쌔신 여부
     * @return boolean
     */
    public boolean isBlindHidingAssassinAttack(){
    	if (!_owner.getSkill().isBlindHidingAssassin()) {
    		return false;
    	}
    	if (!_owner.getInventory().consumeItem(L1ItemId.BLACK_ATTR_STONE, 1)) {
    		return false;
    	}
    	_owner.delBlindHiding();
		return true;
	}
    
    /**
     * 패시브 스킬 능력치 활성화
     * @param L1PassiveId
     */
	public void set(L1PassiveId passive){
		if (passive == null || _passives.contains(passive)) {
			return;
		}
		_passives.add(passive);// 활성화
		
		switch (passive){
		case SLAYER:
			if (_owner.getWeapon() != null && L1ItemWeaponType.isBluntWeapon(_owner.getWeapon().getItem().getWeaponType())) {
				_owner.addAttackSpeedDelayRate(Config.SPELL.SLAYER_ATTACK_SPEED_RATE);
				_owner.sendPackets(S_SlayerDelay.SLAYER_ON);
			}
			_owner.sendPackets(S_SpellBuffNoti.SLAYER_ON);
			break;
		case TITAN_LOCK:
			_owner.sendPackets(S_SpellBuffNoti.TITAN_LOCK_ON);
			break;
		case TITAN_BLICK:
			_owner.sendPackets(S_SpellBuffNoti.TITAN_BLICK_ON);
			break;
		case TITAN_MAGIC:
			_owner.sendPackets(S_SpellBuffNoti.TITAN_MAGIC_ON);
			break;
		case RESIST_ELEMENTAL: 
			_owner.getResistance().addMr(5);
			_owner.getResistance().addAllNaturalResistance(5);
			_owner.sendPackets(new S_OwnCharAttrDef(_owner), true);
			break;
		case INFINITI_BLICK:
			if (_owner.getLevel() >= 75) {
				_infinitiBlickErValue = _owner.getLevel() + ~0x00000049;// -74
				if (_infinitiBlickErValue > 15) {
					_infinitiBlickErValue = 15;
				}
				_owner.getAbility().addEr(_infinitiBlickErValue);
			}
			break;
		case INFINITI_DODGE:
			if (_owner.getLevel() >= 70) {
				_infinitiDodgeDgValue = 5 + ((_owner.getLevel() + ~0x00000045) >> 1);
				if (_infinitiDodgeDgValue > 15) {
					_infinitiDodgeDgValue = 15;
				}
				_owner.getAbility().addDg(_infinitiDodgeDgValue);
			}
			break;
		case PARADOX:
			_owner.getAbility().addReflectEmasculate(Config.SPELL.PARADOX_PROB);
			break;
		case DRESS_EVASION:
			_owner.getAbility().addEr(18);
			break;
		case SOLID_CARRIAGE:
			_owner.getAbility().addEr(10);
			break;
		case INFINITI_BLOOD:
			_infinitiBloodHpValue = 50 + (((_owner.getLevel() - 60) / 3) * 50);
			if (_infinitiBloodHpValue > 650) {
				_infinitiBloodHpValue = 650;
			}
			_owner.addMaxHp(_infinitiBloodHpValue);
			break;
		case AURA:
			if (!_owner.getSkill().isAuraBuff()) {
				_owner.getSkill().setAuraBuff(true);
				_owner.getResistance().addMr(10);
				_owner.getResistance().addToleranceAll(2);
				_owner.getAbility().addAddedInt((byte) 1);
				_owner.getAbility().addAddedDex((byte) 1);
				_owner.getAbility().addAddedStr((byte) 1);
				_owner.sendPackets(new S_OwnCharStatus2(_owner), true);
				_owner.sendPackets(S_PacketBox.AURA_BUFF_ON);
			}
			break;
		case REDUCTION_ARMOR_VETERAN: 
			_owner.getResistance().addToleranceFear(3);
			_reductionArmorVeteranPVPReducValue = (_owner.getLevel() + ~0x0000004F) >> 2;
			if (_reductionArmorVeteranPVPReducValue > 5) {
				_reductionArmorVeteranPVPReducValue = 5;
			}
			_owner.getAbility().addPVPDamageReduction(_reductionArmorVeteranPVPReducValue);
			break;
		case RAIGING_FORCE:
			_raigingForceHitupSkillValue	= 1 + ((_owner.getLevel() + ~0x0000004F) / 3);
			_raigingForcePVPReducValue		= (_owner.getLevel() + ~0x0000004F) >> 2;
			if (_raigingForcePVPReducValue > 5) {
				_raigingForcePVPReducValue = 5;
			}
			_owner.getResistance().addHitupSkill(_raigingForceHitupSkillValue);
			_owner.getAbility().addPVPDamageReduction(_raigingForcePVPReducValue);
			break;
		case ILLUTION_RICH: 
			_owner.getAbility().addSp(4);
			_owner.sendPackets(S_SpellBuffNoti.ILLUTION_RICH_ON);
			break;
		case ILLUTION_GOLEM: 
			_owner.getAC().addAc(-10);
			_owner.sendPackets(S_SpellBuffNoti.ILLUTION_GOLEM_ON);
			break;
		case TACTICAL_ADVANCE:
			_tacticalAdvanceBonusValue = 5 + (_owner.getLevel() >= 90 ? ((_owner.getLevel() + ~0x00000059) >> 1) << 1 : 0);
			if (_tacticalAdvanceBonusValue > 15) {
				_tacticalAdvanceBonusValue = 15;
			}
			_owner.getResistance().addMr(_tacticalAdvanceBonusValue);
			_owner.getAbility().addEr(_tacticalAdvanceBonusValue);
			_owner.getAbility().addDg(_tacticalAdvanceBonusValue);
			_owner.sendPackets(S_SpellBuffNoti.TACTICAL_ADVANCE_ON);
			break;
		case INCREASE_RANGE:
			_owner._statusLancerFormRange = 8;
			break;
		case DRAGON_SKIN:
			_owner.sendPackets(S_SpellBuffNoti.DRAGON_SKIN_ON);
			break;
		case PRIDE:
			_prideHpValue = (int) Math.round(_owner.getBaseMaxHp() * ((_owner.getLevel() >> 2) * 0.01));
			_owner.addMaxHp(_prideHpValue);
			break;
		case ADVANCE_SPIRIT:
			_advanceSpiritHpValue	= _owner.getBaseMaxHp() / 5;
			_advanceSpiritMpValue	= _owner.getBaseMaxMp() / 5;
			_owner.addMaxHp(_advanceSpiritHpValue);
			_owner.addMaxMp(_advanceSpiritMpValue);
			_owner.sendPackets(S_SpellBuffNoti.ADVANCE_SPIRIT_ON);
			break;
		case SHINING_ARMOR:
			_shiningArmorValue = 5 + (_owner.getLevel() > 90 ? (_owner.getLevel() - 90) >> 1 : 0);
			if (_shiningArmorValue > 10) {
				_shiningArmorValue = 10;
			}
			_owner.getAbility().addEr(_shiningArmorValue);
			_owner.getAbility().addDg(_shiningArmorValue);
			_owner.sendPackets(S_SpellBuffNoti.SHINING_ARMOR_ON);
			break;
		case MAJESTY:
			_owner.sendPackets(S_SpellBuffNoti.MAJESTY_ON);
			break;
		case GIGANTIC:
			_giganticHpValue = (int) Math.round(_owner.getBaseMaxHp() * ((_owner.getLevel() >> 1) * 0.01));
			_owner.addMaxHp(_giganticHpValue);
			_owner.sendPackets(S_SpellBuffNoti.GIGANTIC_ON);
			break;
		case RAIGING_WEAPON:
			if (_owner.getWeapon() != null && _owner.getWeapon().getItem().getWeaponType() == L1ItemWeaponType.TOHAND_SWORD) {
				_owner.addAttackSpeedDelayRate(Config.SPELL.RAIGING_WEAPONE_ATTACK_SPEED_RATE);
				_owner.sendPackets(S_SpellBuffNoti.RAIGING_WEAPONE_ON);
			}
			break;
		case DEMOLITION:
			_owner.sendPackets(S_SpellBuffNoti.DEMOLITION_ON);
			break;
		case BERSERK:
			_owner.getResistance().addToleranceAll(5);
			_owner.getAbility().addPVPDamage(5);
			_owner.getAbility().addPVPDamageReduction(8);
			_owner.sendPackets(S_SpellBuffNoti.BERSERK_NORMAL_ON);
			doBerserk();
			break;
		case TITAN_BEAST:
			_owner.sendPackets(S_SpellBuffNoti.TITAN_BEAST_ON);
			break;
		case MORTAL_BODY:
			int mortalHp = 100 + (_owner.getLevel() > 80 ? ((_owner.getLevel() + ~0x0000004F) >> 2) * 100 : 0);
			_mortalBodyHpValue = mortalHp > 600 ? 600 : mortalHp;
			_owner.addMaxHp(_mortalBodyHpValue);
			_owner.sendPackets(S_SpellBuffNoti.MORTAL_BODY_ON);
			break;
		case MOEBIUS:
			_owner.sendPackets(S_SpellBuffNoti.MOEBIUS_ON);
			break;
		case CONQUEROR:
			_owner.sendPackets(S_SpellBuffNoti.CONQUEROR_ON);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 패시브 스킬 능력치 제거
	 * @param L1PassiveId
	 */
	public void remove(L1PassiveId passive){
		if (passive == null || !_passives.contains(passive)) {
			return;
		}
		_passives.remove(passive);// 제거
		
		switch (passive){
		case SLAYER:
			if (_owner.getWeapon() != null && L1ItemWeaponType.isBluntWeapon(_owner.getWeapon().getItem().getWeaponType())) {
				_owner.addAttackSpeedDelayRate(-Config.SPELL.SLAYER_ATTACK_SPEED_RATE);
				_owner.sendPackets(S_SlayerDelay.SLAYER_OFF);
			}
			_owner.sendPackets(S_SpellBuffNoti.SLAYER_OFF);
			break;
		case TITAN_LOCK:
			_owner.sendPackets(S_SpellBuffNoti.TITAN_LOCK_OFF);
			break;
		case TITAN_BLICK:
			_owner.sendPackets(S_SpellBuffNoti.TITAN_BLICK_OFF);
			break;
		case TITAN_MAGIC:
			_owner.sendPackets(S_SpellBuffNoti.TITAN_MAGIC_OFF);
			break;
		case RESIST_ELEMENTAL: 
			_owner.getResistance().addMr(-5);
			_owner.getResistance().addAllNaturalResistance(-5);
			_owner.sendPackets(new S_OwnCharAttrDef(_owner), true);
			break;
		case GLORY_EARTH:
			_owner.setElfAttr(0);
			_owner.sendPackets(S_SkillIconGFX.ATTR_EMPTY);
			break;
		case INFINITI_BLICK:
			_owner.getAbility().addEr(-_infinitiBlickErValue);
			_infinitiBlickErValue = 0;
			break;
		case INFINITI_DODGE:
			_owner.getAbility().addDg(-_infinitiDodgeDgValue);
			_infinitiDodgeDgValue = 0;
			break;
		case PARADOX:
			_owner.getAbility().addReflectEmasculate(-Config.SPELL.PARADOX_PROB);
			break;
		case DRESS_EVASION:
			_owner.getAbility().addEr(-18);
			break;
		case SOLID_CARRIAGE:
			_owner.getAbility().addEr(-10);
			break;
		case INFINITI_BLOOD:
			_owner.addMaxHp(-_infinitiBloodHpValue);
			_infinitiBloodHpValue = 0;
			break;
		case AURA:
			if (_owner.getSkill().isAuraBuff()) {
				_owner.getSkill().setAuraBuff(false);
				_owner.getResistance().addMr(-10);
				_owner.getResistance().addToleranceAll(-2);
				_owner.getAbility().addAddedInt((byte) -1);
				_owner.getAbility().addAddedDex((byte) -1);
				_owner.getAbility().addAddedStr((byte) -1);
				_owner.sendPackets(new S_OwnCharStatus2(_owner), true);
				_owner.sendPackets(S_PacketBox.AURA_BUFF_OFF);
			}
			break;
		case REDUCTION_ARMOR_VETERAN: 
			_owner.getResistance().addToleranceFear(-3);
			_owner.getAbility().addPVPDamageReduction(-_reductionArmorVeteranPVPReducValue);
			_reductionArmorVeteranPVPReducValue = 0;
			break;
		case RAIGING_FORCE: 
			_owner.getResistance().addHitupSkill(-_raigingForceHitupSkillValue);
			_owner.getAbility().addPVPDamageReduction(-_raigingForcePVPReducValue);
			_raigingForceHitupSkillValue = _raigingForcePVPReducValue = 0;
			break;
		case ILLUTION_RICH: 
			_owner.getAbility().addSp(-4);
			_owner.sendPackets(S_SpellBuffNoti.ILLUTION_RICH_OFF);
			break;
		case ILLUTION_GOLEM: 
			_owner.getAC().addAc(10);
			_owner.sendPackets(S_SpellBuffNoti.ILLUTION_GOLEM_OFF);
			break;
		case TACTICAL_ADVANCE:
			_owner.getResistance().addMr(-_tacticalAdvanceBonusValue);
			_owner.getAbility().addEr(-_tacticalAdvanceBonusValue);
			_owner.getAbility().addDg(-_tacticalAdvanceBonusValue);
			_tacticalAdvanceBonusValue = 0;
			_owner.sendPackets(S_SpellBuffNoti.TACTICAL_ADVANCE_OFF);
			break;
		case INCREASE_RANGE:
			_owner._statusLancerFormRange = 4;
			break;
		case DRAGON_SKIN:
			_owner.sendPackets(S_SpellBuffNoti.DRAGON_SKIN_OFF);
			break;
		case PRIDE:
			_owner.addMaxHp(-_prideHpValue);
			_prideHpValue = 0;
			break;
		case ADVANCE_SPIRIT:
			_owner.addMaxHp(-_advanceSpiritHpValue);
			_owner.addMaxMp(-_advanceSpiritMpValue);
			_advanceSpiritHpValue = _advanceSpiritMpValue = 0;
			_owner.sendPackets(S_SpellBuffNoti.ADVANCE_SPIRIT_OFF);
			break;
		case SHINING_ARMOR:
			_owner.getAbility().addEr(-_shiningArmorValue);
			_owner.getAbility().addDg(-_shiningArmorValue);
			_shiningArmorValue = 0;
			_owner.sendPackets(S_SpellBuffNoti.SHINING_ARMOR_OFF);
			break;
		case MAJESTY:
			_owner.sendPackets(S_SpellBuffNoti.MAJESTY_OFF);
			break;
		case GIGANTIC:
			_owner.addMaxHp(-_giganticHpValue);
			_giganticHpValue = 0;
			_owner.sendPackets(S_SpellBuffNoti.GIGANTIC_OFF);
			break;
		case RAIGING_WEAPON:
			if (_owner.getWeapon() != null && _owner.getWeapon().getItem().getWeaponType() == L1ItemWeaponType.TOHAND_SWORD) {
				_owner.addAttackSpeedDelayRate(-Config.SPELL.RAIGING_WEAPONE_ATTACK_SPEED_RATE);
				_owner.sendPackets(S_SpellBuffNoti.RAIGING_WEAPONE_OFF);
			}
			break;
		case DEMOLITION:
			_owner.sendPackets(S_SpellBuffNoti.DEMOLITION_OFF);
			break;
		case BERSERK:
			if (_isBerserkForceActive) {
				_owner.getResistance().addToleranceAll(-5);
				_owner.getAbility().addPVPDamage(-5);
				_owner.getAbility().addPVPDamageReduction(-8);
			}
			_isBerserkForceActive = false;
			_owner.getResistance().addToleranceAll(-5);
			_owner.getAbility().addPVPDamage(-5);
			_owner.getAbility().addPVPDamageReduction(-8);
			_owner.sendPackets(S_SpellBuffNoti.BERSERK_OFF);
			break;
		case TITAN_BEAST:
			_owner.sendPackets(S_SpellBuffNoti.TITAN_BEAST_OFF);
			break;
		case MORTAL_BODY:
			_owner.addMaxHp(-_mortalBodyHpValue);
			_mortalBodyHpValue = 0;
			_owner.sendPackets(S_SpellBuffNoti.MORTAL_BODY_OFF);
			break;
		case MOEBIUS:
			_owner.sendPackets(S_SpellBuffNoti.MOEBIUS_OFF);
			break;
		case CONQUEROR:
			_owner.sendPackets(S_SpellBuffNoti.CONQUEROR_OFF);
			break;
		default:
			break;
		}
	}
	
}


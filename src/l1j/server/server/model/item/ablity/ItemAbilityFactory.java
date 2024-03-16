package l1j.server.server.model.item.ablity;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 아이템의 세부 능력치를 담당하는 클래스를 보관할 Factory
 * enum_key, value, val_size
 * @author LinOffice
 */
public enum ItemAbilityFactory {
	AC_SUB(									2,		ItemAcSub.getInstance(),							1),
	STR(									8,		ItemBonusStrength.getInstance(),					1),
	DEX(									9,		ItemBonusDexterity.getInstance(),					1),
	CON(									10,		ItemBonusConstitution.getInstance(),				1),
	WIS(									11,		ItemBonusWisdom.getInstance(),						1),
	INT(									12,		ItemBonusIntelligence.getInstance(),				1),
	CHA(									13,		ItemBonusCharisma.getInstance(),					1),
	MAX_HP(									14,		ItemMaxHp.getInstance(),							2),
	MAGIC_REGIST(							15,		ItemMagicRegist.getInstance(),						2),
	MP_DRAIN(								16,		ItemMpDrain.getInstance(),							0),
	SPELLPOWER(								17,		ItemSpellpower.getInstance(),						1),
	LONG_HIT(								24,		ItemLongHit.getInstance(),							1),
	ATTR_FIRE(								27,		ItemAttrFire.getInstance(),							1),
	ATTR_WATER(								28,		ItemAttrWater.getInstance(),						1),
	ATTR_WIND(								29,		ItemAttrWind.getInstance(),							1),
	ATTR_EARTH(								30,		ItemAttrEarth.getInstance(),						1),
	MAX_MP(									32,		ItemMaxMp.getInstance(),							2),
	REGIST_FREEZE(							33,		ItemRegistFreeze.getInstance(),						1),
	REGIST_STONE(							33,		ItemRegistStone.getInstance(),						2),
	REGIST_SLEEP(							33,		ItemRegistSleep.getInstance(),						2),
	REGIST_BLIND(							33,		ItemRegistBlind.getInstance(),						2),
	HP_DRAIN(								34,		ItemHpDrain.getInstance(),							0),
	LONG_DAMAGE(							35,		ItemLongDamage.getInstance(),						1),
	EXP_BONUS(								36,		ItemExpBonus.getInstance(),							1),
	HP_REGEN(								37,		ItemHpRegen.getInstance(),							1),
	MP_REGEN(								38,		ItemMpRegen.getInstance(),							1),
	MAGIC_HIT(								40,		ItemMagicHit.getInstance(),							1),
	SHORT_DAMAGE(							47,		ItemShortDamage.getInstance(),						1),
	SHORT_HIT(								48,		ItemShortHit.getInstance(),							1),
	MAGIC_CRITICAL(							50,		ItemMagicCritical.getInstance(),					2),
	EVASION(								51,		ItemEvasion.getInstance(),							1),
	AC_BONUS(								56,		ItemAcBonus.getInstance(),							1),
	PVP_DAMAGE(								59,		ItemPVPDamage.getInstance(),						1),
	PVP_DAMAGE_REDUCTION(					60,		ItemPVPDamageReduction.getInstance(),				1),
	DAMAGE_REDUCTION(						63,		ItemDamageReduction.getInstance(),					1),
	DAMAGE_REDUCTION_CHANCE(				64,		ItemDamageReductionChance.getInstance(),			0),
	POISON_REGIST(							70,		ItemPoisonRegist.getInstance(),						1),
	POLY_DESC(								71,		ItemPolyDescId.getInstance(),						2),
	MAGIC_NAME(								74,		ItemMagicName.getInstance(),						-1),
	HP_ABSOL_REGEN_32_SECOND(				87,		ItemHprAbsol32Second.getInstance(),					1),
	MP_ABSOL_REGEN_64_SECOND(				88,		ItemMprAbsol64Second.getInstance(),					1),
	MAGIC_EVASION(							89,		ItemMagicEvasion.getInstance(),						4),
	CARRY_BONUS(							90,		ItemCarryBonus.getInstance(),						2),
	EVASION_REGIST(							93,		ItemEvasionRegist.getInstance(),					1),
	DAMAGE_CHANCE(							95,		ItemDamageChance.getInstance(),						0),
	POTION_REGIST(							96,		ItemPotionRegist.getInstance(),						1),
	DAMAGE_REDUCTION_EGNOR(					97,		ItemDamageReductionEgnor.getInstance(),				1),
	LONG_CRITICAL(							99,		ItemLongCritical.getInstance(),						1),
	SHORT_CRITICAL(							100,	ItemShortCritical.getInstance(),					1),
	FOW_DAMAGE(								101,	ItemFowSlayerDamage.getInstance(),					1),
	TITAN_UP(								102,	ItemTitanUp.getInstance(),							1),
	DAMAGE_CHANCE_ETC(						103,	ItemDamageChanceEtc.getInstance(),					1),
	MP_ABSOL_REGEN_16_SECOND(				108,	ItemMprAbsol16Second.getInstance(),					1),
	UNDEAD(									114,	ItemUndead.getInstance(),							4),
	DEMON(									115,	ItemDemon.getInstance(),							4),
	REST_EXP_REDUCE_EFFICIENCY(				116,	ItemRestExpReduceEfficiency.getInstance(),			2),
	TOLERANCE_SKILL(						117,	ItemToleranceSkill.getInstance(),					1),
	TOLERANCE_SPIRIT(						118,	ItemToleranceSprit.getInstance(),					1),
	TOLERANCE_DRAGON(						119,	ItemToleranceDragon.getInstance(),					1),
	TOLERANCE_FEAR(							120,	ItemToleranceFear.getInstance(),					1),
	TOLERANCE_ALL(							121,	ItemToleranceAll.getInstance(),						1),
	HITUP_SKILL(							122,	ItemHitupSkill.getInstance(),						1),
	HITUP_SPIRIT(							123,	ItemHitupSprit.getInstance(),						1),
	HITUP_DRAGON(							124,	ItemHitupDragon.getInstance(),						1),
	HITUP_FEAR(								125,	ItemHitupFear.getInstance(),						1),
	HITUP_ALL(								126,	ItemHitupAll.getInstance(),							1),
	PVP_MAGIC_DAMAGE_REDUCTION(				135,	ItemPVPMagicDamageReduction.getInstance(),			1),
	PVP_DAMAGE_REDUCTION_EGNOR(				138,	ItemPVPDamageReductionEgnor.getInstance(),			1),
	PVP_MAGIC_DAMAGE_REDUCTION_EGNOR(		139,	ItemPVPMagicDamageReductionEgnor.getInstance(),		1),
	BUFF_DURATION_SECOND(					140,	ItemBuffDurationSecond.getInstance(),				2),
	ADD_EXP_PERCENT(						142,	ItemAddExpPercent.getInstance(),					2),
	ADD_EXP_PERCENT_PC_CAFE(				143,	ItemAddExpPercentPC.getInstance(),					2),
	FOOD_TYPE(								144,	ItemFoodType.getInstance(),							1),
	BASE_HP_RATE(							149,	ItemBaseHpRate.getInstance(),						1),
	BASE_MP_RATE(							150,	ItemBaseMpRate.getInstance(),						1),
	POTION_PERCENT(							151,	ItemPotionPercent.getInstance(),					1),
	POTION_VALUE(							152,	ItemPotionValue.getInstance(),						1),
	DRAGON_DAMAGE_REDUCTION(				153,	ItemDragonDamageReduction.getInstance(),			1),
	BLESS_EXP(								154,	ItemEinBlessExp.getInstance(),						1),
	ATTR_ALL(								155,	ItemAttrAll.getInstance(),							1),
	THIRD_SPEED(							160,	ItemThirdSpeed.getInstance(),						1),
	FOURTH_SPEED(							165,	ItemFourthGear.getInstance(),						1),
	MAGIC_DAMAGE_REDUCTION(					166,	ItemMagicDamageReduction.getInstance(),				1),
	EMUN_EGNOR(								175,	ItemEmunEgnor.getInstance(),						4),
	STUN_DURATION(							176,	ItemStunDuration.getInstance(),						1),
	DRANIUM(								183,	ItemDranium.getInstance(),							1),
	DAMAGE_REDUCTION_PERCENT(				192,	ItemDamageReductionPercent.getInstance(),			1),
	PVP_DAMAGE_REDUCTION_PERCENT(			193,	ItemPVPDamageReductionPercent.getInstance(),		1),
	MAGIC_DAMAGE(							194,	ItemMagicDamage.getInstance(),						4),
	VANGUARD_TIME(							196,	ItemVanguardTime.getInstance(),						4),
	MAGIC_CRITICAL_DMG_ADD(					197,	ItemMagicCriticalDamageAdd.getInstance(),			2),
	REFLECT_EMASCULATE(						200,	ItemReflectEmasculate.getInstance(),				1),
	// 203 남은 시간 : %d일 %d시간 %d분 %d초
	// 210 적용 상태이상:
	ABNORMAL_STATUS_DAMAGE_REDUCTION(		211,	ItemAbnormalStatusDamageReduction.getInstance(),	1),
	ABNORMAL_STATUS_PVP_DAMAGE_REDUCTION(	212,	ItemAbnormalStatusPVPDamageReduction.getInstance(),	1),
	PVP_DAMAGE_PERCENT(						213,	ItemPVPDamagePercent.getInstance(),					1),
	ATTACK_SPEED_DELAY_RATE(				218,	ItemAttackSpeedDelayRate.getInstance(),				2),
	STRANGE_TIME_INCREASE(					219,	ItemStrangeTimeIncrease.getInstance(),				2),
	RETURNLOCK_DURATION(					220,	ItemReturnLockDuration.getInstance(),				2),
	STRANGE_TIME_DECREASE(					241,	ItemStrangeTimeDecrease.getInstance(),				2),
	HP_POTION_DELAY_DECREASE(				242,	ItemHpPotionDelayDecrease.getInstance(),			2),
	HP_POTION_CRITICAL_PROB(				243,	ItemHpPotionCriticalProb.getInstance(),				2),
	INCREASE_ARMOR_SKILL_PROB(				244,	ItemIncreaseArmorSkillProb.getInstance(),			2),
	MOVE_SPEED_DELAY_RATE(					245,	ItemMoveSpeedDelayRate.getInstance(),				2),
	TRIPLE_ARROW_STUN(						500,	ItemTripleArrowStun.getInstance(),					0),
	ELIXER_BOOSTER(							501,	ItemElixerBooster.getInstance(),					0),
	;
	private int _enum_key;
	private ItemAbility _ability;
	private int _val_size;
	ItemAbilityFactory(int enum_key, ItemAbility ability, int val_size) {
		_enum_key	= enum_key;
		_ability	= ability;
		_val_size	= val_size;
	}
	public int get_enum_key(){
		return _enum_key;
	}
	public ItemAbility get_ability(){
		return _ability;
	}
	public int get_val_size() {
		return _val_size;
	}
	
	private static final ItemAbilityFactory[] ARRAY = ItemAbilityFactory.values();
	private static final ConcurrentHashMap<Integer, ItemAbilityFactory> DATA;
	static {
		DATA = new ConcurrentHashMap<>(ARRAY.length);
		for (ItemAbilityFactory val : ARRAY) {
			DATA.put(val._enum_key, val);
		}
	}
	
	/**
	 * 업무 수행 인스턴스 팩토리 반환
	 * @return ConcurrentHashMap<ItemAblityFactory, ItemAblity>
	 */
	public static ConcurrentHashMap<ItemAbilityFactory, ItemAbility> getAblitys() {
		ConcurrentHashMap<ItemAbilityFactory, ItemAbility> result = new ConcurrentHashMap<>(ARRAY.length);
		for (ItemAbilityFactory val : ARRAY) {
			result.put(val, val._ability.copyInstance());
		}
		return result;
	}
	
	public static ItemAbilityFactory getAbility(int code) {
		return DATA.get(code);
	}
	
	/**
	 * 보너스 옵션 부여
	 * @param owner
	 * @param factory
	 * @param val
	 */
	public static void doBonus(L1PcInstance owner, ItemAbilityFactory factory, int val) {
		switch (factory) {
		case AC_BONUS:
			owner.getAC().addAc(-val);
			break;
		case STR:
			owner.getAbility().addAddedStr((byte) val);
			break;
		case CON:
			owner.getAbility().addAddedCon((byte) val);
			break;
		case DEX:
			owner.getAbility().addAddedDex((byte) val);
			break;
		case INT:
			owner.getAbility().addAddedInt((byte) val);
			break;
		case WIS:
			owner.getAbility().addAddedWis((byte) val);
			break;
		case CHA:
			owner.getAbility().addAddedCha((byte) val);
			break;
		case SHORT_DAMAGE:
			owner.getAbility().addShortDmgup(val);
			break;
		case SHORT_HIT:
			owner.getAbility().addShortHitup(val);
			break;
		case SHORT_CRITICAL:
			owner.getAbility().addShortCritical(val);
			break;
		case LONG_DAMAGE:
			owner.getAbility().addLongDmgup(val);
			break;
		case LONG_HIT:
			owner.getAbility().addLongHitup(val);
			break;
		case LONG_CRITICAL:
			owner.getAbility().addLongCritical(val);
			break;
		case SPELLPOWER:
			owner.getAbility().addSp(val);
			break;
		case MAGIC_HIT:
			owner.getAbility().addMagicHitup(val);
			break;
		case MAGIC_CRITICAL:
			owner.getAbility().addMagicCritical(val);
			break;
		case MAX_HP:
			owner.addMaxHp(val);
			break;
		case MAX_MP:
			owner.addMaxMp(val);
			break;
		case HP_REGEN:
			owner.addHpRegen(val);
			break;
		case MP_REGEN:
			owner.addMpRegen(val);
			break;
		case ATTR_FIRE:
			owner.getResistance().addFire(val);
			break;
		case ATTR_WATER:
			owner.getResistance().addWater(val);
			break;
		case ATTR_WIND:
			owner.getResistance().addWind(val);
			break;
		case ATTR_EARTH:
			owner.getResistance().addEarth(val);
			break;
		case ATTR_ALL:
			owner.getResistance().addAllNaturalResistance(val);
			break;
		case MAGIC_REGIST:
			owner.getResistance().addMr(val);
			break;
		case CARRY_BONUS:
			owner.addCarryBonus(val);
			break;
		case EVASION:
			owner.getAbility().addDg(val);
			break;
		case EVASION_REGIST:
			owner.getAbility().addEr(val);
			break;
		case MAGIC_EVASION:
			owner.getAbility().addMe(val);
			break;
		case DAMAGE_REDUCTION:
			owner.getAbility().addDamageReduction(val);
			break;
		case DAMAGE_REDUCTION_EGNOR:
			owner.getAbility().addDamageReductionEgnor(val);
			break;
		case MAGIC_DAMAGE_REDUCTION:
			owner.getAbility().addMagicDamageReduction(val);
			break;
		case PVP_DAMAGE:
			owner.getAbility().addPVPDamage(val);
			break;
		case PVP_DAMAGE_REDUCTION:
			owner.getAbility().addPVPDamageReduction(val);
			break;
		case PVP_DAMAGE_REDUCTION_EGNOR:
			owner.getAbility().addPVPDamageReductionEgnor(val);
			break;
		case PVP_MAGIC_DAMAGE_REDUCTION:
			owner.getAbility().addPVPMagicDamageReduction(val);
			break;
		case PVP_MAGIC_DAMAGE_REDUCTION_EGNOR:
			owner.getAbility().addPVPMagicDamageReductionEgnor(val);
			break;
		case REST_EXP_REDUCE_EFFICIENCY:
			owner.add_rest_exp_reduce_efficiency(val);
			break;
		case TOLERANCE_SKILL:
			owner.getResistance().addToleranceSkill(val);
			break;
		case TOLERANCE_SPIRIT:
			owner.getResistance().addToleranceSpirit(val);
			break;
		case TOLERANCE_DRAGON:
			owner.getResistance().addToleranceDragon(val);
			break;
		case TOLERANCE_FEAR:
			owner.getResistance().addToleranceFear(val);
			break;
		case TOLERANCE_ALL:
			owner.getResistance().addToleranceAll(val);
			break;
		case HITUP_SKILL:
			owner.getResistance().addHitupSkill(val);
			break;
		case HITUP_SPIRIT:
			owner.getResistance().addHitupSpirit(val);
			break;
		case HITUP_DRAGON:
			owner.getResistance().addHitupDragon(val);
			break;
		case HITUP_FEAR:
			owner.getResistance().addHitupFear(val);
			break;
		case HITUP_ALL:
			owner.getResistance().addHitupAll(val);
			break;
		case EXP_BONUS:
			owner.add_exp_boosting_ratio(val);
			break;
		case EMUN_EGNOR:
			owner.getAbility().addEmunEgnor(val);
			break;
		case STRANGE_TIME_INCREASE:
			owner.getAbility().addStrangeTimeIncrease(val);
			break;
		case STRANGE_TIME_DECREASE:
			owner.getAbility().addStrangeTimeDecrease(val);
			break;
		case ABNORMAL_STATUS_DAMAGE_REDUCTION:
			owner.getAbility().addAbnormalStatusDamageReduction(val);
			break;
		case ABNORMAL_STATUS_PVP_DAMAGE_REDUCTION:
			owner.getAbility().addAbnormalStatusPVPDamageReduction(val);
			break;
		case PVP_DAMAGE_PERCENT:
			owner.getAbility().addPVPDamagePercent(val);
			break;
		default:
			System.out.println(String.format(
					"[ItemAblityFactory] UNDEFINED_ENUM_DESC : KEY(%d), VALUE(%d)", 
					factory._enum_key, val));
			break;
		}
	}
	
	public static void init(){}
}


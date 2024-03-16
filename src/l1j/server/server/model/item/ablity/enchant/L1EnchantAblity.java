package l1j.server.server.model.item.ablity.enchant;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 아이템 인첸트 옵션 오브젝트
 * @author LinOffice
 */
public class L1EnchantAblity {
	private int itemId;
	private int enchant;
	private int ac_bonus, ac_sub;
	private byte str, con, dex, inti, wis, cha;
	private int shortDamage, shortHit, shortCritical;
	private int longDamage, longHit, longCritical;
	private int spellpower, magicHit, magicCritical, magicDamage;
	private int maxHp, hpRegen, baseHpRate;
	private int maxMp, mpRegen, baseMpRate;
	private int attrFire, attrWater, attrWind, attrEarth, attrAll;
	private int mr, carryBonus, dg, er, me;
	private int reduction, reductionEgnor, reductionMagic, reductionPercent;
	private int PVPDamage, PVPReduction, PVPReductionPercent, PVPReductionEgnor, PVPReductionMagic, PVPReductionMagicEgnor, PVPDamagePercent;
	private int abnormalStatusDamageReduction, abnormalStatusPVPDamageReduction;
	private int registBlind, registFreeze, registSleep, registStone;
	private int toleranceSkill, toleranceSpirit, toleranceDragon, toleranceFear, toleranceAll;
	private int hitupSkill, hitupSpirit, hitupDragon, hitupFear, hitupAll;
	private int potionPlusDefens, potionPlusPercent, potionPlusValue;
	private int hprAbsol32Second, mprAbsol64Second, mprAbsol16Second;
	private int imunEgnor;
	private int expBonus, einBlessExp, restExpReduceEfficiency;
	private int fowSlayerDamage;
	private int titanUp;
	private int stunDuration;
	private int tripleArrowStun;
	private int vanguardTime;
	private int strangeTimeIncrease, strangeTimeDecrease;
	private int hpPotionDelayDecrease, hpPotionCriticalProb;
	private int increaseArmorSkillProb;
	private int returnLockDuraion;
	private int attackSpeedDelayRate, moveSpeedDelayRate;
	private String magicName;
	
	public L1EnchantAblity(ResultSet rs) throws SQLException {
		this.itemId								= rs.getInt("itemId");
		this.enchant							= rs.getInt("enchant");
		this.ac_bonus							= rs.getInt("ac_bonus");
		this.ac_sub								= rs.getInt("ac_sub");
		this.str								= rs.getByte("str");
		this.con								= rs.getByte("con");
		this.dex								= rs.getByte("dex");
		this.inti								= rs.getByte("int");
		this.wis								= rs.getByte("wis");
		this.cha								= rs.getByte("cha");
		this.shortDamage						= rs.getInt("shortDamage");
		this.shortHit							= rs.getInt("shortHit");
		this.shortCritical						= rs.getInt("shortCritical");
		this.longDamage							= rs.getInt("longDamage");
		this.longHit							= rs.getInt("longHit");
		this.longCritical						= rs.getInt("longCritical");
		this.spellpower							= rs.getInt("spellpower");
		this.magicHit							= rs.getInt("magicHit");
		this.magicCritical						= rs.getInt("magicCritical");
		this.magicDamage						= rs.getInt("magicDamage");
		this.maxHp								= rs.getInt("maxHp");
		this.hpRegen							= rs.getInt("hpRegen");
		this.baseHpRate							= rs.getInt("baseHpRate");
		this.maxMp								= rs.getInt("maxMp");
		this.mpRegen							= rs.getInt("mpRegen");
		this.baseMpRate							= rs.getInt("baseMpRate");
		this.attrFire							= rs.getInt("attrFire");
		this.attrWater							= rs.getInt("attrWater");
		this.attrWind							= rs.getInt("attrWind");
		this.attrEarth							= rs.getInt("attrEarth");
		this.attrAll							= rs.getInt("attrAll");
		this.mr									= rs.getInt("mr");
		this.carryBonus							= rs.getInt("carryBonus");
		this.dg									= rs.getInt("dg");
		this.er									= rs.getInt("er");
		this.me									= rs.getInt("me");
		this.reduction							= rs.getInt("reduction");
		this.reductionEgnor						= rs.getInt("reductionEgnor");
		this.reductionMagic						= rs.getInt("reductionMagic");
		this.reductionPercent					= rs.getInt("reductionPercent");
		this.PVPDamage							= rs.getInt("PVPDamage");
		this.PVPReduction						= rs.getInt("PVPReduction");
		this.PVPReductionPercent				= rs.getInt("PVPReductionPercent");
		this.PVPReductionEgnor					= rs.getInt("PVPReductionEgnor");
		this.PVPReductionMagic					= rs.getInt("PVPReductionMagic");
		this.PVPReductionMagicEgnor				= rs.getInt("PVPReductionMagicEgnor");
		this.abnormalStatusDamageReduction		= rs.getInt("abnormalStatusDamageReduction");
		this.abnormalStatusPVPDamageReduction	= rs.getInt("abnormalStatusPVPDamageReduction");
		this.PVPDamagePercent					= rs.getInt("PVPDamagePercent");
		this.registBlind						= rs.getInt("registBlind");
		this.registFreeze						= rs.getInt("registFreeze");
		this.registSleep						= rs.getInt("registSleep");
		this.registStone						= rs.getInt("registStone");
		this.toleranceSkill						= rs.getInt("toleranceSkill");
		this.toleranceSpirit					= rs.getInt("toleranceSpirit");
		this.toleranceDragon					= rs.getInt("toleranceDragon");
		this.toleranceFear						= rs.getInt("toleranceFear");
		this.toleranceAll						= rs.getInt("toleranceAll");
		this.hitupSkill							= rs.getInt("hitupSkill");
		this.hitupSpirit						= rs.getInt("hitupSpirit");
		this.hitupDragon						= rs.getInt("hitupDragon");
		this.hitupFear							= rs.getInt("hitupFear");
		this.hitupAll							= rs.getInt("hitupAll");
		this.potionPlusDefens					= rs.getInt("potionPlusDefens");
		this.potionPlusPercent					= rs.getInt("potionPlusPercent");
		this.potionPlusValue					= rs.getInt("potionPlusValue");
		this.hprAbsol32Second					= rs.getInt("hprAbsol32Second");
		this.mprAbsol64Second					= rs.getInt("mprAbsol64Second");
		this.mprAbsol16Second					= rs.getInt("mprAbsol16Second");
		this.imunEgnor							= rs.getInt("imunEgnor");
		this.expBonus							= rs.getInt("expBonus");
		this.einBlessExp						= rs.getInt("einBlessExp");
		this.restExpReduceEfficiency			= rs.getInt("rest_exp_reduce_efficiency");
		this.fowSlayerDamage					= rs.getInt("fowSlayerDamage");
		this.titanUp							= rs.getInt("titanUp");
		this.stunDuration						= rs.getInt("stunDuration");
		this.tripleArrowStun					= rs.getInt("tripleArrowStun");
		this.vanguardTime						= rs.getInt("vanguardTime");
		this.strangeTimeIncrease				= rs.getInt("strangeTimeIncrease");
		this.strangeTimeDecrease				= rs.getInt("strangeTimeDecrease");
		this.hpPotionDelayDecrease				= rs.getInt("hpPotionDelayDecrease");
		this.hpPotionCriticalProb				= rs.getInt("hpPotionCriticalProb");
		this.increaseArmorSkillProb				= rs.getInt("increaseArmorSkillProb");
		this.returnLockDuraion					= rs.getInt("returnLockDuraion");
		this.attackSpeedDelayRate				= rs.getInt("attackSpeedDelayRate");
		this.moveSpeedDelayRate					= rs.getInt("moveSpeedDelayRate");
		this.magicName							= rs.getString("magicName");
	}
	
	public int getItemId() {
		return itemId;
	}
	public int getEnchant() {
		return enchant;
	}
	public int getAcBonus() {
		return ac_bonus;
	}
	public int getAcSub() {
		return ac_sub;
	}
	public byte getStr() {
		return str;
	}
	public byte getCon() {
		return con;
	}
	public byte getDex() {
		return dex;
	}
	public byte getInt() {
		return inti;
	}
	public byte getWis() {
		return wis;
	}
	public byte getCha() {
		return cha;
	}
	public int getShortDamage() {
		return shortDamage;
	}
	public int getShortHit() {
		return shortHit;
	}
	public int getShortCritical() {
		return shortCritical;
	}
	public int getLongDamage() {
		return longDamage;
	}
	public int getLongHit() {
		return longHit;
	}
	public int getLongCritical() {
		return longCritical;
	}
	public int getSpellpower() {
		return spellpower;
	}
	public int getMagicHit() {
		return magicHit;
	}
	public int getMagicCritical() {
		return magicCritical;
	}
	public int getMagicDamage() {
		return magicDamage;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public int getHpRegen() {
		return hpRegen;
	}
	public int getBaseHpRate() {
		return baseHpRate;
	}
	public int getMaxMp() {
		return maxMp;
	}
	public int getMpRegen() {
		return mpRegen;
	}
	public int getBaseMpRate() {
		return baseMpRate;
	}
	public int getAttrFire() {
		return attrFire;
	}
	public int getAttrWater() {
		return attrWater;
	}
	public int getAttrWind() {
		return attrWind;
	}
	public int getAttrEarth() {
		return attrEarth;
	}
	public int getAttrAll() {
		return attrAll;
	}
	public int getMr() {
		return mr;
	}
	public int getCarryBonus() {
		return carryBonus;
	}
	public int getDg() {
		return dg;
	}
	public int getEr() {
		return er;
	}
	public int getMe() {
		return me;
	}
	public int getReduction() {
		return reduction;
	}
	public int getReductionEgnor() {
		return reductionEgnor;
	}
	public int getReductionMagic() {
		return reductionMagic;
	}
	public int getReductionPercent() {
		return reductionPercent;
	}
	public int getPVPDamage() {
		return PVPDamage;
	}
	public int getPVPReduction() {
		return PVPReduction;
	}
	public int getPVPReductionPercent() {
		return PVPReductionPercent;
	}
	public int getPVPReductionEgnor() {
		return PVPReductionEgnor;
	}
	public int getPVPReductionMagic() {
		return PVPReductionMagic;
	}
	public int getPVPReductionMagicEgnor() {
		return PVPReductionMagicEgnor;
	}
	public int getAbnormalStatusDamageReduction() {
		return abnormalStatusDamageReduction;
	}
	public int getAbnormalStatusPVPDamageReduction() {
		return abnormalStatusPVPDamageReduction;
	}
	public int getPVPDamagePercent() {
		return PVPDamagePercent;
	}
	public int getRegistBlind() {
		return registBlind;
	}
	public int getRegistFreeze() {
		return registFreeze;
	}
	public int getRegistSleep() {
		return registSleep;
	}
	public int getRegistStone() {
		return registStone;
	}
	public int getToleranceSkill() {
		return toleranceSkill;
	}
	public int getToleranceSpirit() {
		return toleranceSpirit;
	}
	public int getToleranceDragon() {
		return toleranceDragon;
	}
	public int getToleranceFear() {
		return toleranceFear;
	}
	public int getToleranceAll() {
		return toleranceAll;
	}
	public int getHitupSkill() {
		return hitupSkill;
	}
	public int getHitupSpirit() {
		return hitupSpirit;
	}
	public int getHitupDragon() {
		return hitupDragon;
	}
	public int getHitupFear() {
		return hitupFear;
	}
	public int getHitupAll() {
		return hitupAll;
	}
	public int getPotionPlusDefens() {
		return potionPlusDefens;
	}
	public int getPotionPlusPercent() {
		return potionPlusPercent;
	}
	public int getPotionPlusValue() {
		return potionPlusValue;
	}
	public int getHprAbsol32Second() {
		return hprAbsol32Second;
	}
	public int getMprAbsol64Second() {
		return mprAbsol64Second;
	}
	public int getMprAbsol16Second() {
		return mprAbsol16Second;
	}
	public int getImunEgnor() {
		return imunEgnor;
	}
	public int getExpBonus() {
		return expBonus;
	}
	public int getEinBlessExp() {
		return einBlessExp;
	}
	public int getRestExpReduceEfficiency() {
		return restExpReduceEfficiency;
	}
	public int getFowSlayerDamage() {
		return fowSlayerDamage;
	}
	public int getTitanUp() {
		return titanUp;
	}
	public int getStunDuration() {
		return stunDuration;
	}
	public int getTripleArrowStun() {
		return tripleArrowStun;
	}
	public int getVanguardTime() {
		return vanguardTime;
	}
	public int getStrangeTimeIncrease() {
		return strangeTimeIncrease;
	}
	public int getStrangeTimeDecrease() {
		return strangeTimeDecrease;
	}
	public int getHpPotionDelayDecrease() {
		return hpPotionDelayDecrease;
	}
	public int getHpPotionCriticalProb() {
		return hpPotionCriticalProb;
	}
	public int getIncreaseArmorSkillProb() {
		return increaseArmorSkillProb;
	}
	public int getReturnLockDuraion() {
		return returnLockDuraion;
	}
	public int getAttackSpeedDelayRate() {
		return attackSpeedDelayRate;
	}
	public int getMoveSpeedDelayRate() {
		return moveSpeedDelayRate;
	}
	public String getMagicName() {
		return magicName;
	}
}


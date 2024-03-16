package l1j.server.server.model.item.potential;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.common.bin.PotentialCommonBinLoader;
import l1j.server.common.bin.potential.CommonPotentialInfo;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1Resistance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_FourthGear;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;

/**
 * 인형 잠재력 오브젝트
 * @author LinOffice
 */
public class L1Potential {
	private int bonusId;
	private boolean isUse;
	private CommonPotentialInfo.BonusInfoT info;
	private int ac_bonus;
	private int str, con, dex, inti, wis, cha, allStatus;
	private int shortDamage, shortHit, shortCritical;
	private int longDamage, longHit, longCritical;
	private int spellpower, magicHit, magicCritical;
	private int hp, mp, hpr, mpr, hpStill, mpStill, stillChance, hprAbsol, mprAbsol;
	private int attrFire, attrWater, attrWind, attrEarth, attrAll;
	private int mr, expBonus, carryBonus, dg, er, me;
	private int reduction, reductionEgnor, reductionMagic, reductionPercent;
	private int PVPDamage, PVPReduction, PVPReductionEgnor, PVPReductionMagic, PVPReductionMagicEgnor;
	private int toleranceSkill, toleranceSpirit, toleranceDragon, toleranceFear, toleranceAll;
	private int hitupSkill, hitupSpirit, hitupDragon, hitupFear, hitupAll;
	private int imunEgnor, strangeTimeIncrease;
	private boolean firstSpeed, secondSpeed, thirdSpeed, forthSpeed;
	private int skilId, skillChance;
	
	public L1Potential(ResultSet rs) throws SQLException {
		this.bonusId				= rs.getInt("bonusId");
		this.isUse					= Boolean.parseBoolean(rs.getString("isUse"));
		this.info					= PotentialCommonBinLoader.getBonusInfo(this.bonusId);
		if (this.info == null) {
			System.out.println(String.format("[L1Potential] NOT_FOUND_BONUS_INFO : BONUS_ID(%d)", this.bonusId));
		}
		this.ac_bonus				= rs.getInt("ac_bonus");
		this.str					= rs.getInt("str");
		this.con					= rs.getInt("con");
		this.dex					= rs.getInt("dex");
		this.inti					= rs.getInt("int");
		this.wis					= rs.getInt("wis");
		this.cha					= rs.getInt("cha");
		this.allStatus				= rs.getInt("allStatus");
		this.shortDamage			= rs.getInt("shortDamage");
		this.shortHit				= rs.getInt("shortHit");
		this.shortCritical			= rs.getInt("shortCritical");
		this.longDamage				= rs.getInt("longDamage");
		this.longHit				= rs.getInt("longHit");
		this.longCritical			= rs.getInt("longCritical");
		this.spellpower				= rs.getInt("spellpower");
		this.magicHit				= rs.getInt("magicHit");
		this.magicCritical			= rs.getInt("magicCritical");
		this.hp						= rs.getInt("hp");
		this.mp						= rs.getInt("mp");
		this.hpr					= rs.getInt("hpr");
		this.mpr					= rs.getInt("mpr");
		this.hpStill				= rs.getInt("hpStill");
		this.mpStill				= rs.getInt("mpStill");
		this.stillChance			= rs.getInt("stillChance");
		this.hprAbsol				= rs.getInt("hprAbsol");
		this.mprAbsol				= rs.getInt("mprAbsol");
		this.attrFire				= rs.getInt("attrFire");
		this.attrWater				= rs.getInt("attrWater");
		this.attrWind				= rs.getInt("attrWind");
		this.attrEarth				= rs.getInt("attrEarth");
		this.attrAll				= rs.getInt("attrAll");
		this.mr						= rs.getInt("mr");
		this.expBonus				= rs.getInt("expBonus");
		this.carryBonus				= rs.getInt("carryBonus");
		this.dg						= rs.getInt("dg");
		this.er						= rs.getInt("er");
		this.me						= rs.getInt("me");
		this.reduction				= rs.getInt("reduction");
		this.reductionEgnor			= rs.getInt("reductionEgnor");
		this.reductionMagic			= rs.getInt("reductionMagic");
		this.reductionPercent		= rs.getInt("reductionPercent");
		this.PVPDamage				= rs.getInt("PVPDamage");
		this.PVPReduction			= rs.getInt("PVPReduction");
		this.PVPReductionEgnor		= rs.getInt("PVPReductionEgnor");
		this.PVPReductionMagic		= rs.getInt("PVPReductionMagic");
		this.PVPReductionMagicEgnor	= rs.getInt("PVPReductionMagicEgnor");
		this.toleranceSkill			= rs.getInt("toleranceSkill");
		this.toleranceSpirit		= rs.getInt("toleranceSpirit");
		this.toleranceDragon		= rs.getInt("toleranceDragon");
		this.toleranceFear			= rs.getInt("toleranceFear");
		this.toleranceAll			= rs.getInt("toleranceAll");
		this.hitupSkill				= rs.getInt("hitupSkill");
		this.hitupSpirit			= rs.getInt("hitupSpirit");
		this.hitupDragon			= rs.getInt("hitupDragon");
		this.hitupFear				= rs.getInt("hitupFear");
		this.hitupAll				= rs.getInt("hitupAll");
		this.imunEgnor				= rs.getInt("imunEgnor");
		this.strangeTimeIncrease	= rs.getInt("strangeTimeIncrease");
		this.firstSpeed				= Boolean.parseBoolean(rs.getString("firstSpeed"));
		this.secondSpeed			= Boolean.parseBoolean(rs.getString("secondSpeed"));
		this.thirdSpeed				= Boolean.parseBoolean(rs.getString("thirdSpeed"));
		this.forthSpeed				= Boolean.parseBoolean(rs.getString("forthSpeed"));
		this.skilId					= rs.getInt("skilId");
		this.skillChance			= rs.getInt("skillChance");
	}
	
	public int getBonusId() {
		return bonusId;
	}
	public boolean isUse() {
		return isUse;
	}
	public CommonPotentialInfo.BonusInfoT getInfo() {
		return info;
	}
	public int getHpStill() {
		return hpStill;
	}
	public int getMpStill() {
		return mpStill;
	}
	public int getStillChance() {
		return stillChance;
	}
	public int getSkilId() {
		return skilId;
	}
	public int getSkillChance() {
		return skillChance;
	}
	
	/**
	 * 잠재력 옵션을 소유자에게 설정한다.
	 * @param owner
	 * @param flag
	 */
	public void ablityPotential(L1PcInstance owner, boolean flag){
		int value				= flag ? 1 : -1;
		L1Ability ablity		= owner.getAbility();
		L1Resistance resistance	= owner.getResistance();
		if (ac_bonus != 0) {
			owner.getAC().addAc(ac_bonus * value);
		}
		if (str > 0) {
			ablity.addAddedStr((byte) str * value);
		}
		if (con > 0) {
			ablity.addAddedCon((byte) con * value);
		}
		if (dex > 0) {
			ablity.addAddedDex((byte) dex * value);
		}
		if (inti > 0) {
			ablity.addAddedInt((byte) inti * value);
		}
		if (wis > 0) {
			ablity.addAddedWis((byte) wis * value);
		}
		if (cha > 0) {
			ablity.addAddedCha((byte) cha * value);
		}
		if (allStatus > 0) {
			ablity.addAddedStr((byte) allStatus * value);
			ablity.addAddedDex((byte) allStatus * value);
			ablity.addAddedCon((byte) allStatus * value);
			ablity.addAddedWis((byte) allStatus * value);
			ablity.addAddedInt((byte) allStatus * value);
		}
		if (shortDamage > 0) {
			ablity.addShortDmgup(shortDamage * value);
		}
		if (shortHit > 0) {
			ablity.addShortHitup(shortHit * value);
		}
		if (shortCritical > 0) {
			ablity.addShortCritical(shortCritical * value);
		}
		if (longDamage > 0) {
			ablity.addLongDmgup(longDamage * value);
		}
		if (longHit > 0) {
			ablity.addLongHitup(longHit * value);
		}
		if (longCritical > 0) {
			ablity.addLongCritical(longCritical * value);
		}
		if (spellpower > 0) {
			ablity.addSp(spellpower * value);
		}
		if (magicHit > 0) {
			ablity.addMagicHitup(magicHit * value);
		}
		if (magicCritical > 0) {
			ablity.addMagicHitup(magicCritical * value);
		}
		if (hp > 0) {
			owner.addMaxHp(hp * value);
		}
		if (mp > 0) {
			owner.addMaxMp(mp * value);
		}
		if (hpr > 0) {
			owner.addHpRegen(hpr * value);
		}
		if (mpr > 0) {
			owner.addMpRegen(mpr * value);
		}
		if (hprAbsol > 0) {
			owner.addHpRegen32SecondByItemValue(hprAbsol * value);
		}
		if (mprAbsol > 0) {
			owner.addMpRegen64SecondByItemValue(mprAbsol * value);
		}
		if (attrFire > 0) {
			resistance.addFire(attrFire * value);
		}
		if (attrWater > 0) {
			resistance.addWater(attrWater * value);
		}
		if (attrWind > 0) {
			resistance.addWind(attrWind * value);
		}
		if (attrEarth > 0) {
			resistance.addEarth(attrEarth * value);
		}
		if (attrAll > 0) {
			resistance.addAllNaturalResistance(attrAll * value);
		}
		if (mr > 0) {
			resistance.addMr(mr * value);
		}
		if (expBonus > 0) {
			owner.add_exp_boosting_ratio(expBonus * value);
		}
		if (carryBonus > 0) {
			owner.addCarryBonus(carryBonus * value);
		}
		if (dg > 0) {
			ablity.addDg(dg * value);
		}
		if (er > 0) {
			ablity.addEr(er * value);
		}
		if (me > 0) {
			ablity.addMe(me * value);
		}
		if (reduction > 0) {
			ablity.addDamageReduction(reduction * value);
		}
		if (reductionEgnor > 0) {
			ablity.addDamageReductionEgnor(reductionEgnor * value);
		}
		if (reductionMagic > 0) {
			ablity.addMagicDamageReduction(reductionMagic * value);
		}
		if (reductionPercent > 0) {
			ablity.addDamageReductionPercent(reductionPercent * value);
		}
		if (PVPDamage > 0) {
			ablity.addPVPDamage(PVPDamage * value);
		}
		if (PVPReduction > 0) {
			ablity.addPVPDamageReduction(PVPReduction * value);
		}
		if (PVPReductionEgnor > 0) {
			ablity.addPVPDamageReductionEgnor(PVPReductionEgnor * value);
		}
		if (PVPReductionMagic > 0) {
			ablity.addPVPMagicDamageReduction(PVPReductionMagic * value);
		}
		if (PVPReductionMagicEgnor > 0) {
			ablity.addPVPMagicDamageReductionEgnor(PVPReductionMagicEgnor * value);
		}
		if (toleranceSkill > 0) {
			resistance.addToleranceSkill(toleranceSkill * value);
		}
		if (toleranceSpirit > 0) {
			resistance.addToleranceSpirit(toleranceSpirit * value);
		}
		if (toleranceDragon > 0) {
			resistance.addToleranceDragon(toleranceDragon * value);
		}
		if (toleranceFear > 0) {
			resistance.addToleranceFear(toleranceFear * value);
		}
		if (toleranceAll > 0) {
			resistance.addToleranceAll(toleranceAll * value);
		}
		if (hitupSkill > 0) {
			resistance.addHitupSkill(hitupSkill * value);
		}
		if (hitupSpirit > 0) {
			resistance.addHitupSpirit(hitupSpirit * value);
		}
		if (hitupDragon > 0) {
			resistance.addHitupDragon(hitupDragon * value);
		}
		if (hitupFear > 0) {
			resistance.addHitupFear(hitupFear * value);
		}
		if (hitupAll > 0) {
			resistance.addHitupAll(hitupAll * value);
		}
		if (imunEgnor > 0) {
			ablity.addEmunEgnor(imunEgnor * value);
		}
		if (strangeTimeIncrease > 0) {
			ablity.addStrangeTimeIncrease(strangeTimeIncrease * value);
		}
		if (firstSpeed) {
			if (flag) {
				owner.addHasteItemEquipped(1);
				owner.removeSpeedSkill();
				if (owner.getMoveState().getMoveSpeed() != 1) {
					owner.getMoveState().setMoveSpeed(1);
					owner.sendPackets(new S_SkillHaste(owner.getId(), 1, -1), true);
					owner.broadcastPacket(new S_SkillHaste(owner.getId(), 1, 0), true);
				}
			} else {
				owner.addHasteItemEquipped(-1);
				if (owner.getHasteItemEquipped() == 0) {
					owner.getMoveState().setMoveSpeed(0);
					owner.broadcastPacketWithMe(new S_SkillHaste(owner.getId(), 0, 0), true);
				}
			}
		}
		if (secondSpeed) {
			if (flag) {
				if (owner.getSkill().hasSkillEffect(L1SkillId.STATUS_BRAVE)) {
					owner.getSkill().removeSkillEffect(L1SkillId.STATUS_BRAVE);
				}
				if (owner.getMoveState().getBraveSpeed() == 0) {
					owner.getMoveState().setBraveSpeed(1);
					owner.sendPackets(new S_SkillBrave(owner.getId(), 1, -1), true);
					owner.broadcastPacket(new S_SkillBrave(owner.getId(), 1, 0), true);
				}
			} else {
				owner.getMoveState().setBraveSpeed(0);
				owner.broadcastPacketWithMe(new S_SkillBrave(owner.getId(), 0, 0), true);
			}
		}
		if (thirdSpeed) {
			if (flag) {
				owner.addDrunkenItemEquipped(1);
				if (owner.getSkill().hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)) {
					owner.getSkill().killSkillEffectTimer(L1SkillId.STATUS_DRAGON_PEARL);
					owner.getMoveState().setDrunken(0);
				}
				if (owner.getMoveState().getDrunken() != 8) {
					owner.broadcastPacketWithMe(new S_Liquor(owner.getId(), 8), true);
					owner.sendPackets(S_PacketBox.DRAGON_PEARL_ON);
					owner.getMoveState().setDrunken(8);
				}
			} else {
				owner.addDrunkenItemEquipped(-1);
				if (owner.getDrunkenItemEquipped() == 0) {
					owner.broadcastPacketWithMe(new S_Liquor(owner.getId(), 0), true);
					owner.sendPackets(S_PacketBox.DRAGON_PEARL_OFF);
					owner.getMoveState().setDrunken(0);
				}
			}
		}
		if (forthSpeed) {
			if (flag) {
				owner.addFourthItemEquipped(1);
				if (!owner.getMoveState().isFourthGear()) {
					owner.getMoveState().setFourthGear(true);
					owner.broadcastPacketWithMe(new S_FourthGear(owner.getId(), true), true);
				}
			} else {
				owner.addFourthItemEquipped(-1);
				if (owner.getFourthItemEquipped() == 0) {
					owner.getMoveState().setFourthGear(false);
					owner.broadcastPacketWithMe(new S_FourthGear(owner.getId(), false), true);
				}
			}
		}
	}
	
}


package l1j.server.server.model.item.function;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.NSHOP_BUFF_TYPE;

public class NBuff {
	
	static boolean isValidation(L1PcInstance pc, int skillId, NSHOP_BUFF_TYPE type) {
		if (pc.getSkill().hasSkillEffect(skillId)) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("이미 동일한 버프 상품이 적용중입니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1084), true), true);
			return false;
		}
		pc.getSkill().removeSkillEffect(skillId);
		pc.getSkill().setSkillEffect(skillId, 86400000L * (long) Config.ETC.N_BUFF_RUN_DAY);
		pc.sendPackets(new S_SpellBuffNoti(type, skillId, 86400 * (long) Config.ETC.N_BUFF_RUN_DAY), true);
		return true;
	}
	
	public static void excute(L1PcInstance pc, int itemId, L1ItemInstance useItem) {
		long sysTime = System.currentTimeMillis();
		long nextTime = 86400000 * (long) Config.ETC.N_BUFF_RUN_DAY;
		Timestamp deleteTime = new Timestamp(sysTime + nextTime + 10000);// 7일
		try {
			Account account = pc.getAccount();
			if (itemId == 600213) {
				if (!isValidation(pc, L1SkillId.BUFF_ATTACK, NSHOP_BUFF_TYPE.ATTACK)) {
					return;
				}
				account.setBuff_DMG(deleteTime);
				pc.getAbility().addShortDmgup(1);
				pc.getAbility().addLongDmgup(1);
			} else if (itemId == 600214) {
				if (!isValidation(pc, L1SkillId.BUFF_DEFENSE, NSHOP_BUFF_TYPE.DEFFENSE)) {
					return;
				}
				account.setBuff_REDUC(deleteTime);
				pc.getAbility().addDamageReduction(1); // 데미지 이빠이 올리고 테스트
			} else if (itemId == 600215) {
				if (!isValidation(pc, L1SkillId.BUFF_MAGIC, NSHOP_BUFF_TYPE.MAGIC)) {
					return;
				}
				account.setBuff_MAGIC(deleteTime);
				pc.getAbility().addSp(1); // 옵션
			} else if (itemId == 600216) {
				if (!isValidation(pc, L1SkillId.BUFF_TECHNIQUE, NSHOP_BUFF_TYPE.STUN)) {
					return;
				}
				account.setBuff_STUN(deleteTime);
				pc.getResistance().addToleranceSkill(2);	
			} else if (itemId == 600217) {
				if (!isValidation(pc, L1SkillId.BUFF_SPIRIT, NSHOP_BUFF_TYPE.HOLD)) {
					return;
				}
				account.setBuff_HOLD(deleteTime);
				pc.getResistance().addToleranceSpirit(2);
			} else if (itemId == 600198) {
				if (!isValidation(pc, L1SkillId.BUFF_INTELLIGENCE2, NSHOP_BUFF_TYPE.ATTACK_FIRE)) {
					return;
				}
				account.setBuff_FireATTACK(deleteTime);
			} else if (itemId == 600199) {
				if (!isValidation(pc, L1SkillId.BUFF_CONSTITUTION2, NSHOP_BUFF_TYPE.ATTACK_EARTH)) {
					return;
				}
				account.setBuff_EarthATTACK(deleteTime);
			} else if (itemId == 600200) {
				if (!isValidation(pc, L1SkillId.BUFF_CRAFTSMANSHIP, NSHOP_BUFF_TYPE.ATTACK_WATER)) {
					return;
				}
				account.setBuff_WaterATTACK(deleteTime);
			} else if (itemId == 600201) {
				if (!isValidation(pc, L1SkillId.BUFF_AGILITY2, NSHOP_BUFF_TYPE.ATTACK_AIR)) {
					return;
				}
				account.setBuff_WindATTACK(deleteTime);
			} else if (itemId == 600205) {
				if(pc.getSkill().hasSkillEffect(L1SkillId.BUFF_POWER) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_DEXTERITY) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_KNOWLEDGE) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_INSIGHT)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 스탯 버프 상품이 적용중입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1085), true), true);
					return;
				}
				pc.getSkill().removeSkillEffect(L1SkillId.BUFF_POWER);
				pc.getSkill().setSkillEffect(L1SkillId.BUFF_POWER, nextTime);
				account.setBuff_STR(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.STR, L1SkillId.BUFF_POWER, nextTime), true);
				pc.getAbility().addAddedStr((byte) 1);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			} else if (itemId == 600204) {
				if (pc.getSkill().hasSkillEffect(L1SkillId.BUFF_POWER) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_DEXTERITY) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_KNOWLEDGE) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_INSIGHT)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 스탯 버프 상품이 적용중입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1085), true), true);
					return;
				}
				pc.getSkill().removeSkillEffect(L1SkillId.BUFF_DEXTERITY);
				pc.getSkill().setSkillEffect(L1SkillId.BUFF_DEXTERITY, nextTime);
				account.setBuff_DEX(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.DEX, L1SkillId.BUFF_DEXTERITY, nextTime), true);
				pc.getAbility().addAddedDex((byte) 1);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			} else if (itemId == 600203) {
				if (pc.getSkill().hasSkillEffect(L1SkillId.BUFF_POWER) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_DEXTERITY) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_KNOWLEDGE) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_INSIGHT)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 스탯 버프 상품이 적용중입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1085), true), true);
					return;
				}
				pc.getSkill().removeSkillEffect(L1SkillId.BUFF_KNOWLEDGE);
				pc.getSkill().setSkillEffect(L1SkillId.BUFF_KNOWLEDGE, nextTime);
				account.setBuff_INT(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.INT, L1SkillId.BUFF_KNOWLEDGE, nextTime), true);
				pc.getAbility().addAddedInt((byte) 1);
				pc.sendPackets(new S_SPMR(pc), true);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			} else if (itemId == 600202) {
				if (pc.getSkill().hasSkillEffect(L1SkillId.BUFF_POWER) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_DEXTERITY) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_KNOWLEDGE) || pc.getSkill().hasSkillEffect(L1SkillId.BUFF_INSIGHT)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 스탯 버프 상품이 적용중입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1085), true), true);
					return;
				}
				pc.getSkill().removeSkillEffect(L1SkillId.BUFF_INSIGHT);
				pc.getSkill().setSkillEffect(L1SkillId.BUFF_INSIGHT, nextTime);
				account.setBuff_WIS(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.WIS, L1SkillId.BUFF_INSIGHT, nextTime), true);
				pc.getAbility().addAddedWis((byte) 1);
				pc.sendPackets(new S_SPMR(pc), true);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			} else if (itemId == 600206) {
				if (!isValidation(pc, L1SkillId.BUFF_WISDOM, NSHOP_BUFF_TYPE.DEFFENSE_SPIRIT)) {
					return;
				}
				account.setBuff_SoulDEFENCE(deleteTime);
				pc.getResistance().addFire(5);
				pc.getResistance().addEarth(5);
				pc.getResistance().addWater(5);
				pc.getResistance().addWind(5);
			} else if (itemId == 600207) {
				if (!isValidation(pc, L1SkillId.BUFF_INTELLIGENCE, NSHOP_BUFF_TYPE.DEFFENSE_FIRE)) {
					return;
				}
				account.setBuff_FireDEFENCE(deleteTime);
				pc.getResistance().addFire(5);
			} else if (itemId == 600208) {
				if (!isValidation(pc, L1SkillId.BUFF_CONSTITUTION, NSHOP_BUFF_TYPE.DEFFENSE_EARTH)) {
					return;
				}
				account.setBuff_EarthDEFENCE(deleteTime);
				pc.getResistance().addEarth(5);
			} else if (itemId == 600209) {
				if (!isValidation(pc, L1SkillId.BUFF_AGILITY, NSHOP_BUFF_TYPE.DEFFENSE_AIR)) {
					return;
				}
				account.setBuff_WindDEFENCE(deleteTime);
				pc.getResistance().addWind(5);
			} else if (itemId == 600210) {
				if (!isValidation(pc, L1SkillId.BUFF_STRENGTH, NSHOP_BUFF_TYPE.DEFFENSE_WATER)) {
					return;
				}
				account.setBuff_WaterDEFENCE(deleteTime);
				pc.getResistance().addWater(5);
			}
			account.updateBuff();
			pc.getInventory().removeItem(useItem, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void reset(L1PcInstance pc, int itemId, L1ItemInstance useItem) {
		long sysTime = System.currentTimeMillis();
		Timestamp deleteTime = new Timestamp(sysTime + 1);// 7일
		try {
			Account account = pc.getAccount();
			L1SkillStatus skill = pc.getSkill();	
			if (skill.hasSkillEffect(L1SkillId.BUFF_ATTACK)) {
				skill.removeSkillEffect(L1SkillId.BUFF_ATTACK);
				account.setBuff_DMG(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.ATTACK, L1SkillId.BUFF_ATTACK, (long) 1000), true);
				pc.getAbility().addShortDmgup(-1);
				pc.getAbility().addLongDmgup(-1);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_DEFENSE)) {
				skill.removeSkillEffect(L1SkillId.BUFF_DEFENSE);
				account.setBuff_REDUC(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.DEFFENSE, L1SkillId.BUFF_DEFENSE, (long) 1000), true);
				pc.getAbility().addDamageReduction(-1);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_MAGIC)) {
				skill.removeSkillEffect(L1SkillId.BUFF_MAGIC);
				account.setBuff_MAGIC(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.MAGIC, L1SkillId.BUFF_MAGIC, (long) 1000), true);
				pc.getAbility().addSp(-1);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_TECHNIQUE)) {
				skill.removeSkillEffect(L1SkillId.BUFF_TECHNIQUE);
				account.setBuff_STUN(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.STUN, L1SkillId.BUFF_TECHNIQUE, (long) 1000), true);
				pc.getResistance().addToleranceSkill(-2);	
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_SPIRIT)) {
				skill.removeSkillEffect(L1SkillId.BUFF_SPIRIT);
				account.setBuff_HOLD(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.HOLD, L1SkillId.BUFF_SPIRIT, (long) 1000), true);
				pc.getResistance().addToleranceSpirit(-2);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_INTELLIGENCE2)) {
				skill.removeSkillEffect(L1SkillId.BUFF_INTELLIGENCE2);
				account.setBuff_FireATTACK(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.ATTACK_FIRE, L1SkillId.BUFF_INTELLIGENCE2, (long) 1000), true);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_CONSTITUTION2)) {
				skill.removeSkillEffect(L1SkillId.BUFF_CONSTITUTION2);
				account.setBuff_EarthATTACK(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.ATTACK_EARTH, L1SkillId.BUFF_CONSTITUTION2, (long) 1000), true);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_CRAFTSMANSHIP)) {
				skill.removeSkillEffect(L1SkillId.BUFF_CRAFTSMANSHIP);
				account.setBuff_WaterATTACK(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.ATTACK_WATER, L1SkillId.BUFF_CRAFTSMANSHIP, (long) 1000), true);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_AGILITY2)) {
				skill.removeSkillEffect(L1SkillId.BUFF_AGILITY2);
				account.setBuff_WindATTACK(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.ATTACK_AIR, L1SkillId.BUFF_AGILITY2, (long) 1000), true);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_POWER)) {
				skill.removeSkillEffect(L1SkillId.BUFF_POWER);
				account.setBuff_STR(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.STR, L1SkillId.BUFF_POWER, (long) 1000), true);
				pc.getAbility().addAddedStr((byte) -1);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_DEXTERITY)) {
				skill.removeSkillEffect(L1SkillId.BUFF_DEXTERITY);
				account.setBuff_DEX(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.DEX, L1SkillId.BUFF_DEXTERITY, (long) 1000), true);
				pc.getAbility().addAddedDex((byte) -1);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_KNOWLEDGE)) {
				skill.removeSkillEffect(L1SkillId.BUFF_KNOWLEDGE);
				account.setBuff_INT(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.INT, L1SkillId.BUFF_KNOWLEDGE, (long) 1000), true);
				pc.getAbility().addAddedInt((byte) -1);
				pc.sendPackets(new S_SPMR(pc), true);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_INSIGHT)) {
				skill.removeSkillEffect(L1SkillId.BUFF_INSIGHT);
				account.setBuff_WIS(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.WIS, L1SkillId.BUFF_INSIGHT, (long) 1000), true);
				pc.getAbility().addAddedWis((byte) -1);
				pc.sendPackets(new S_SPMR(pc), true);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_WISDOM)) {
				skill.removeSkillEffect(L1SkillId.BUFF_WISDOM);
				account.setBuff_SoulDEFENCE(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.DEFFENSE_SPIRIT, L1SkillId.BUFF_WISDOM, (long) 1000), true);
				pc.getResistance().addFire(-5);
				pc.getResistance().addEarth(-5);
				pc.getResistance().addWater(-5);
				pc.getResistance().addWind(-5);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_INTELLIGENCE)) {
				skill.removeSkillEffect(L1SkillId.BUFF_INTELLIGENCE);
				account.setBuff_FireDEFENCE(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.DEFFENSE_FIRE, L1SkillId.BUFF_INTELLIGENCE, (long) 1000), true);
				pc.getResistance().addFire(-5);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_CONSTITUTION)) {
				skill.removeSkillEffect(L1SkillId.BUFF_CONSTITUTION);
				account.setBuff_EarthDEFENCE(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.DEFFENSE_EARTH, L1SkillId.BUFF_CONSTITUTION, (long) 1000), true);
				pc.getResistance().addEarth(-5);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_AGILITY)) {
				skill.removeSkillEffect(L1SkillId.BUFF_AGILITY);
				account.setBuff_WindDEFENCE(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.DEFFENSE_AIR, L1SkillId.BUFF_AGILITY, (long) 1000), true);
				pc.getResistance().addWind(-5);
			}
			if (skill.hasSkillEffect(L1SkillId.BUFF_STRENGTH)) {
				skill.removeSkillEffect(L1SkillId.BUFF_STRENGTH);
				account.setBuff_WaterDEFENCE(deleteTime);
				pc.sendPackets(new S_SpellBuffNoti(NSHOP_BUFF_TYPE.DEFFENSE_WATER, L1SkillId.BUFF_STRENGTH, (long) 1000), true);
				pc.getResistance().addWater(-5);
			}
			account.updateBuff();
			pc.getInventory().removeItem(useItem, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



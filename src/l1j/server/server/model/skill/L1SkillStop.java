package l1j.server.server.model.skill;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.GameSystem.arca.L1ArcaGrade;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.controller.CrockController;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.spell.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.spell.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.spell.S_SkillIconWisdomPotion;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.serverpackets.playsupport.S_StartPlaySupport;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconNotiType;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconDurationShowType;

public class L1SkillStop extends L1SkillId {
	private L1SkillStop(){}
	
	public static void stopSkill(L1Character cha, int skillId, L1Skills l1skills) {
		if ((skillId >= L1SkillId.COOKING_BEGIN && skillId <= L1SkillId.COOKING_END) || skillId == L1SkillId.DOGAM_BUFF) {
			if (cha instanceof L1PcInstance) {
				L1Cooking.doBonus((L1PcInstance)cha, skillId, 0, false);
			}
			return;
		}
		switch (skillId) {
		case HALPAS_FIRE_BRESS:
		case HALPAS_POISON_BRESS:
		case HALPAS_WIND_BRESS:
		case BRAVE_MENTAL:
		case COUNTER_BARRIER:
		case ASSASSIN:
		case ABSOLUTE_BLADE:
		case PAP_DEATH_POTION:
		case INFERNO:
		case HALPHAS:
		case MEDITATION:
		case MP_REDUCTION_POTION:
		case TITAN_RISING:
		case BUFF_VISUAL_OF_CAPTAIN:
		case BUFF_PUFFER_FISH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SpellBuffNoti(pc, skillId, false, -1), true);
			}
			break;
			
		case EXPOSE_WEAKNESS:// 약점 노출(공격자)
			cha.getSkill().set_expose_weakness_target(null);
			break;
		case STATUS_EXPOSE_WEAKNESS:// 약점 노출(피격자)
			cha.getAC().addAc(-5);
			cha.getAbility().addDg(10);
			cha.getResistance().addToleranceDragon(10);
			break;
			
		case PLEDGE_EXP_BUFF_I_NORMAL:
		case PLEDGE_EXP_BUFF_I_KING:
		case PLEDGE_EXP_BUFF_I_KNIGHT:
		case PLEDGE_EXP_BUFF_I_ELITE:
		case PLEDGE_EXP_BUFF_II_NORMAL:
		case PLEDGE_EXP_BUFF_II_KING:
		case PLEDGE_EXP_BUFF_II_KNIGHT:
		case PLEDGE_EXP_BUFF_II_ELITE:
		case PLEDGE_BATTLE_BUFF_I_NORMAL:
		case PLEDGE_BATTLE_BUFF_I_ELITE:
		case PLEDGE_BATTLE_BUFF_II_NORMAL:
		case PLEDGE_BATTLE_BUFF_II_ELITE:
		case PLEDGE_DEFENS_BUFF_I_NORMAL:
		case PLEDGE_DEFENS_BUFF_I_ELITE:
		case PLEDGE_DEFENS_BUFF_II_NORMAL:
		case PLEDGE_DEFENS_BUFF_II_ELITE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1BuffUtil.pledge_contribution_buff_set(pc, skillId, -1, false);
			}
			break;
			
		case EXP_POTION:
		case EXP_POTION1:
		case EXP_POTION2:
		case EXP_POTION3:
		case EXP_POTION5:
		case EXP_POTION6:
		case EXP_POTION7:
		case EXP_POTION8:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				cha.getSkill().getExpPotion().reset();
				pc.sendPackets(new S_ExpBoostingInfo(pc), true);
			}
			break;
		case EXP_POTION4:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				cha.getSkill().getExpPotion().reset();
				cha.getAbility().addDamageReduction(-2);
				cha.addMaxHp(-100);
				cha.getAbility().addItemPotionPercent(-10);
				cha.getAbility().addItemPotionValue(-10);
				pc.sendPackets(new S_ExpBoostingInfo(pc), true);
			}
			break;
		case FAITH_OF_HALPAH_PVP:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc._halpasLoyaltyValue > 0) {
					cha.getAbility().addPVPDamage(-pc._halpasLoyaltyValue);
				}
				pc.sendPackets(new S_SpellBuffNoti(pc, skillId, false, -1), true);
			}
			break;
		case ANONYMITY_POLY:
			if (cha instanceof L1PcInstance) {
			    L1PcInstance pc = (L1PcInstance) cha;
			    cha.getResistance().addHitupAll(-5);
			    cha.getAbility().addPVPDamageReduction(-5);
			    pc.sendPackets(new S_SpellBuffNoti(pc, skillId, false, -1), true);
			}
			break;
		case JUDGEMENT_DOLL:
			if (cha instanceof L1PcInstance) {
			    L1PcInstance pc = (L1PcInstance) cha;
			    switch(pc._statusJudgementDoll){
    			case 0:// 군주
    			case 1:// 기사
    				cha.getResistance().addToleranceSkill(15);
    				break;
    			case 3:// 법사
    				cha.getResistance().addMr(15);
    				break;
    			case 2:// 요정
    			case 4:// 다크엘프
    				cha.getResistance().addToleranceSpirit(15);
    				break;
    			case 5:// 용기사
    			case 6:// 환술사
    				cha.getResistance().addToleranceDragon(15);
    				break;
    			case 7:// 전사
    			case 8:// 검사
    			case 9:// 창기사
    				cha.getResistance().addToleranceFear(15);
    				break;
    			}
			    pc._statusJudgementDoll = -1;
			}
			break;
		case HALPAS_ICE_BRESS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(S_Paralysis.BIND_OFF);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillId, false, -1), true);
			}
			break;
			
		case NEW_CASHSCROLL1:
			cha.getAbility().addShortDmgup(-3);
			cha.getAbility().addShortHitup(-5);
			cha.getAbility().addPVPDamageReduction(-3);
			break;
		case NEW_CASHSCROLL2:
			cha.getAbility().addLongDmgup(-3);
			cha.getAbility().addLongHitup(-5);
			cha.getAbility().addPVPDamageReduction(-3);
			break;
		case NEW_CASHSCROLL3:
			cha.getAbility().addSp(-3);
			cha.getAbility().addMagicHitup(-5);
			cha.getAbility().addPVPDamageReduction(-3);
			break;
		case KYULJUN_CASHSCROLL:
			cha.getAC().addAc(5);
			cha.getAbility().addShortHitup(-5);
			cha.getAbility().addLongHitup(-5);
			cha.getAbility().addMagicHitup(-2);
			cha.getAbility().addPVPDamageReduction(-5);
			cha.getAbility().addPVPDamage(-5);
			break;
		case HP_CASHSCROLL: {
			if (!(cha instanceof L1PcInstance)) {
				break;
			}
			L1PcInstance pc = (L1PcInstance) cha;
			cha.addMaxHp(-2000);
			pc.sendPackets(new S_SpellBuffNoti(pc, skillId, false, -1), true);
		}
			break;
		case HERO_FAVOR:
			cha.getAbility().addAddedStr((byte) -1);
			cha.getAbility().addAddedDex((byte) -1);
			cha.getAbility().addAddedInt((byte) -1);
			cha.getAbility().addShortHitup(-3);
			cha.getAbility().addLongHitup(-3);
			cha.getAbility().addMagicHitup(-3);
			cha.getResistance().addHitupAll(-3);
			cha.getAbility().addPVPDamage(-3);
			break;
		case LIFE_FAVOR:
			cha.addMaxHp(-300);
			cha.addMaxMp(-100);
			break;
		case BUFF_ICE_STR:
			cha.getAbility().addShortHitup(-5);
			cha.getAbility().addShortDmgup(-3);
			cha.getAbility().addAddedStr((byte) -1);
			break;
		case BUFF_ICE_DEX:
			cha.getAbility().addLongHitup(-5);
			cha.getAbility().addLongDmgup(-3);
			cha.getAbility().addAddedDex((byte) -1);
			break;
		case BUFF_ICE_INT:
			cha.addMaxMp(-50);
			cha.getAbility().addAddedInt((byte) -1);
			break;
		case LIFE_STREAM:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 59, false), true);
			}
			break;
		case MOB_DEATH_HEAL:
		case PAP_DEATH_HEAL:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				cha.getSkill().removeSkillEffect(skillId);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillId, false, -1), true);
				pc.sendPackets(new S_ServerMessage(4744), true);
			}
			break;

		case LEVELUP_BONUS_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_exp_boosting_ratio(-123);
				pc.sendPackets(new S_PacketBox(0, true, true), true);
			}
			break;
		case DRAGON_PUPLE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(0, 1, true, true), true);
			}
			break;
		case DRAGON_TOPAZ:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(0, 2, true, true), true);
			}
			break;
		case STATUS_BRAVE:
		case STATUS_ELFBRAVE:
		case STATUS_FRUIT:
			cha.getMoveState().setBraveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
			}
			break;
			
		case 800018:// 티칼
			if (!cha.isDead()) {
				CrockController.getInstance().dieCount(0);
				CrockController.getInstance().BossSpawn(800018, 32753, 32870, (short) 784, 1900000);
			}
			break;
		case 800019:// 티칼
			if (!cha.isDead()) {
				CrockController.getInstance().dieCount(0);
				CrockController.getInstance().BossSpawn(800019, 32750, 32859, (short) 784, 1900000);
			}
			break;
		case DELAY:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (!pc.getSkill().hasSkillEffect(DELAY)) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_END), true);
				}
			}
			break;
			
		case TAM_FRUIT_1:
		case TAM_FRUIT_2:
		case TAM_FRUIT_3:
		case TAM_FRUIT_4:
		case TAM_FRUIT_5:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1BuffUtil.arcaBuffDisable(pc, skillId);
				int activeCount = pc.getAccount().getArca().getActiveCount(System.currentTimeMillis());
				if (activeCount > 0) {
					L1ArcaGrade grade = L1ArcaGrade.getGrade(activeCount);
					long activeTime = pc.getAccount().getArca().getActiveTime();
					L1BuffUtil.arcaBuffEnable(pc, grade, activeTime);
				}
			}
			break;

		case STATUS_FRAME:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(S_SpellBuffNoti.FRAME_OFF);
			}
			break;
		case LINDBIOR_SPIRIT_EFFECT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(S_CurseBlind.BLIND_OFF);
			}
			break;
		case DESERT_SKILL1:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.broadcastPacketWithMe(new S_Poison(pc.getId(), 0), true);
				pc.sendPackets(S_Paralysis.PARALYSIS_OFF);
			}
			break;
		case ANTA_MESSAGE_6:
		case ANTA_MESSAGE_7:
		case ANTA_MESSAGE_8:
		case ANTA_SHOCKSTUN:
		case VALLAKAS_PREDICATE2:
		case VALLAKAS_PREDICATE4:
		case VALLAKAS_PREDICATE5:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(S_Paralysis.STURN_OFF);
				pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_STUN, false, 0), true);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		case RINDVIOR_WIND_SHACKLE:
		case RINDVIOR_WIND_SHACKLE_1:
		case DRAKE_WIND_SHACKLE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), 0), true);
				if (!pc.isDead()) {
					pc.broadcastPacket(new S_PCObject(pc), true);
				}
			}
			break;
		case STATUS_IGNITION:
			cha.getResistance().addFire(-30);
			break;
		case STATUS_QUAKE:
			cha.getResistance().addEarth(-30);
			break;
		case STATUS_SHOCK:
			cha.getResistance().addWind(-30);
			break;
		case STATUS_BLUE_POTION:
		case STATUS_BLUE_POTION2:
			break;
		case STATUS_UNDERWATER_BREATH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0), true);
			}
			break;
		case STATUS_WISDOM_POTION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				cha.getAbility().addSp(-2);
				pc.addMpRegen(-2);
				pc.sendPackets(new S_SkillIconWisdomPotion(0), true);
			}
			break;
		case STATUS_CHAT_PROHIBITED:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_ServerMessage(288), true);
			}
			break;
		case STATUS_CASHSCROLL:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				cha.addMaxHp(-50);
				pc.addHpRegen(-4);
			}
			break;
		case STATUS_CASHSCROLL2:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				cha.addMaxMp(-40);
				pc.addMpRegen(-4);
			}
			break;
		case STATUS_CASHSCROLL3:
			cha.getAbility().addShortDmgup(-3);
			cha.getAbility().addShortHitup(-3);
			cha.getAbility().addSp(-3);
			break;
			
		case STATUS_POISON:
		case STATUS_POISON_SILENCE: //침묵독
			cha.curePoison();
			break;
		case STATUS_DRAGON_PEARL: // 드래곤의 진주
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.broadcastPacketWithMe(new S_Liquor(pc.getId(), 0), true);
				pc.sendPackets(L1ServerMessage.sm185);
				pc.getMoveState().setDrunken(0);
			}
			break;
		case PET_BUFF_GROW:
		case PET_BUFF_EIN:
		case PET_BUFF_SKY:
		case PET_BUFF_YEGABAM:
		case PET_BUFF_BLOOD:
			if (cha instanceof L1PetInstance) {
				((L1PetInstance) cha).doBuffBonus(skillId, 0, false);
			}
			break;
		case L1SkillId.STATUS_PSS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(S_StartPlaySupport.TIME_EXPIRE);
				pc.sendPackets(L1SystemMessage.PLAY_SUPPORT_AUTH_ITEM_FAIL);		
				pc.sendPackets(new S_SpellBuffNoti(L1SkillId.STATUS_PSS, SkillIconNotiType.END, pc.getAccount().getPSSTime()*60, SkillIconDurationShowType.TYPE_EFF_MINUTE, 1));
				pc.getAccount().setPSSTime(0);				
				pc.getConfig().finishPlaySupport();
			}
			break;
		default:
			break;
		}

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			sendStopMessage(pc, l1skills);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_OwnCharStatus2(pc), true);
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
		}
	}

	private static void sendStopMessage(L1PcInstance charaPc, L1Skills l1skills) {
		if (l1skills == null || charaPc == null) {
			return;
		}
		int msgID = l1skills.getSysmsgIdStop();
		if (msgID <= 0) {
			return;
		}
		charaPc.sendPackets(getMessage(msgID));
	}
	
	/**
	 * 메세지 패킷 재사용 처리
	 */
	private static final ConcurrentHashMap<Integer, S_ServerMessage> MESSAGES = new ConcurrentHashMap<Integer, S_ServerMessage>();
	private static S_ServerMessage getMessage(int id){
		S_ServerMessage message = MESSAGES.get(id);
		if (message == null) {
			message = new S_ServerMessage(id);
			MESSAGES.put(id, message);
		}
		return message;
	}
	
}


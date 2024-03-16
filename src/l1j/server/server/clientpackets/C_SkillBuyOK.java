package l1j.server.server.clientpackets;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.templates.L1Skills;

public class C_SkillBuyOK extends ClientBasePacket {

	private static final String C_SKILL_BUY_OK = "[C] C_SkillBuyOK";

	public C_SkillBuyOK(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		int count = readH();
		int sid[] = new int[count];
		int price = 0;
		int level1 = 0, level2 = 0, level3 = 0;
		int level1_cost = 0, level2_cost = 0, level3_cost = 0;

		for (int i = 0; i < count; i++) {
			sid[i] = readD();
			// 게랭 버그 관련 추가
			if (sid[i] > 24) {
				pc.denals_disconnect(String.format("[C_SkillBuyOK] SIZE_DENALS : NAME(%s)", pc.getName()));
				return;
			}

			switch (sid[i]) {
			// Lv1 마법
			case 0:
				level1 += 1;
				level1_cost += 100;
				break;
			case 1:
				level1 += 2;
				level1_cost += 100;
				break;
			case 2:
				level1 += 4;
				level1_cost += 100;
				break;
			case 3:
				level1 += 8;
				level1_cost += 100;
				break;
			case 4:
				level1 += 16;
				level1_cost += 100;
				break;
			case 5:
				level1 += 32;
				level1_cost += 100;
				break;
			case 6:
				level1 += 64;
				level1_cost += 100;
				break;
			case 7:
				level1 += 128;
				level1_cost += 100;
				break;

			// Lv2 마법
			case 8:
				level2 += 1;
				level2_cost += 400;
				break;
			case 9:
				level2 += 2;
				level2_cost += 400;
				break;
			case 10:
				level2 += 4;
				level2_cost += 400;
				break;
			case 11:
				level2 += 8;
				level2_cost += 400;
				break;
			case 12:
				level2 += 16;
				level2_cost += 400;
				break;
			case 13:
				level2 += 32;
				level2_cost += 400;
				break;
			case 14:
				level2 += 64;
				level2_cost += 400;
				break;
			case 15:
				level2 += 128;
				level2_cost += 400;
				break;

			// Lv3 마법
			case 16:
				level3 += 1;
				level3_cost += 900;
				break;
			case 17:
				level3 += 2;
				level3_cost += 900;
				break;
			case 18:
				level3 += 4;
				level3_cost += 900;
				break;
			case 19:
				level3 += 8;
				level3_cost += 900;
				break;
			case 20:
				level3 += 16;
				level3_cost += 900;
				break;
			case 21:
				level3 += 32;
				level3_cost += 900;
				break;
			case 22:
				level3 += 64;
				level3_cost += 900;
				break;
			case 23:
				level3 += 128;
				level3_cost += 900;
				break;

			default:
				break;
			}
		}

		switch (pc.getType()) {
		case 0: // 군주
			if (pc.getLevel() < 10) {
				level1 = 0;
				level1_cost = 0;
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 10 && pc.getLevel() <= 19) {
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 20) {
				level3 = 0;
				level3_cost = 0;
			}
			break;

		case 1: // 기사
			if (pc.getLevel() < 50) {
				level1 = 0;
				level1_cost = 0;
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 50) {
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			}
			break;

		case 2: // ELF
			if (pc.getLevel() < 8) {
				level1 = 0;
				level1_cost = 0;
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 8 && pc.getLevel() <= 15) {
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 16 && pc.getLevel() <= 23) {
				level3 = 0;
				level3_cost = 0;
			}
			break;

		case 3: // WIZ
			if (pc.getLevel() < 4) {
				level1 = 0;
				level1_cost = 0;
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 4 && pc.getLevel() <= 7) {
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 8 && pc.getLevel() <= 11) {
				level3 = 0;
				level3_cost = 0;
			}
			break;

		case 4: // DE
			if (pc.getLevel() < 12) {
				level1 = 0;
				level1_cost = 0;
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 12 && pc.getLevel() <= 23) {
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 24) {
				level3 = 0;
				level3_cost = 0;
			}
			break;
		case 7: // 전사
			if (pc.getLevel() < 50) {
				level1 = 0;
				level1_cost = 0;
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			} else if (pc.getLevel() >= 50) {
				level2 = 0;
				level2_cost = 0;
				level3 = 0;
				level3_cost = 0;
			}
			break;
		default:
			break;
		}

		if (level1 == 0 && level2 == 0 && level3 == 0) {
			return;
		}
		price = level1_cost + level2_cost + level3_cost;
		if (pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			pc.getInventory().consumeItem(L1ItemId.ADENA, price);
			pc.send_effect(224);
			
			//pc.sendPackets(new S_AddSkill(level1, level2, level3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), true);

			List<Integer> leanSkills = new ArrayList<>();
			
			if ((level1 & 1) == 1) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.HEAL);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level1 & 2) == 2) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.LIGHT);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level1 & 4) == 4) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.SHIELD);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level1 & 8) == 8) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.ENERGY_BOLT);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level1 & 16) == 16) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.TELEPORT);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level1 & 32) == 32) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.ICE_DAGGER);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level1 & 64) == 64) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.WIND_CUTTER);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level1 & 128) == 128) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.HOLY_WEAPON);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}

			if ((level2 & 1) == 1) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.CURE_POISON);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level2 & 2) == 2) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.CHILL_TOUCH);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level2 & 4) == 4) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.CURSE_POISON);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level2 & 8) == 8) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.ENCHANT_WEAPON);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level2 & 16) == 16) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.DETECTION);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level2 & 32) == 32) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.DECREASE_WEIGHT);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level2 & 64) == 64) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.FIRE_ARROW);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level2 & 128) == 128) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.STALAC);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}

			if ((level3 & 1) == 1) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.LIGHTNING);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level3 & 2) == 2) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.TURN_UNDEAD);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level3 & 4) == 4) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.EXTRA_HEAL);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level3 & 8) == 8) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.CURSE_BLIND);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level3 & 16) == 16) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.BLESSED_ARMOR);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level3 & 32) == 32) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.FROZEN_CLOUD);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if ((level3 & 64) == 64) {
				L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.WEAK_ELEMENTAL);
				pc.getSkill().spellActiveMastery(l1skills);
				leanSkills.add(l1skills.getSkillId());
			}
			if (!leanSkills.isEmpty()) {
				pc.sendPackets(new S_AvailableSpellNoti(leanSkills, true), true);
				leanSkills.clear();
			}
			leanSkills = null;
		} else {
			pc.sendPackets(L1ServerMessage.sm189); // \f1아데나가 부족합니다.
		}
	}

	@Override
	public String getType() {
		return C_SKILL_BUY_OK;
	}

}


package l1j.server.server.command.executor;

import java.util.List;

import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.serverpackets.spell.S_AddSpellPassiveNoti;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1Skills;

public class L1AddSkill implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AddSkill();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1AddSkill() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			pc.send_effect('\343');// 마법 습득의 효과음을 울린다
			// 액티브
			List<L1Skills> learnActiveList	= SkillsTable.getActiveClassTypeList(pc.getType());// 클래스 액티브 전용 스킬
			List<L1Skills> normalList		= SkillsTable.getActiveClassTypeList(99);// 일반 마법 스킬
			int normalLastCnt = 0;
			if (pc.isCrown() || pc.isDarkelf() || pc.isDragonknight() || pc.isFencer() || pc.isLancer()) {
				normalLastCnt = 15;
			} else if (pc.isKnight() || pc.isWarrior()) {
				normalLastCnt = 7;
			} else if (pc.isElf()) {
				normalLastCnt = 47;
			} else if (pc.isWizard()) {
				normalLastCnt = 79;
			}
			if (normalLastCnt >= 0) {
				for (L1Skills skill : normalList) {
					if (skill.getSkillId() >= 0 && skill.getSkillId() <= normalLastCnt) {
						learnActiveList.add(skill);// 클래스별 일반 스킬
					}
				}
			}
			for (L1Skills skill : learnActiveList) {
				//System.out.println("activando el skill: " + skill.getSkillId());
				pc.sendPackets(new S_AvailableSpellNoti(skill.getSkillId(), true), true);
				pc.getSkill().spellActiveMastery(skill); // DB에 등록
			}
			
			// 패시브
			List<L1PassiveSkills> learnPassiveList = SkillsTable.getPassiveClassTypeList(pc.getType());// 클래스 패시브 전용 스킬
			for (L1PassiveSkills passive : learnPassiveList) {
				if (pc.getSkill().isLearnPassive(passive.getPassiveId())) {
					continue;
				}
				pc.sendPackets(new S_AddSpellPassiveNoti(passive.getPassiveId(), true), true);
				pc.getPassiveSkill().set(passive.getPassive());

				pc.getSkill().spellPassiveMastery(passive); // DB에 등록
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 커멘드 에러"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(168), true), true);
			return false;
		}
	}
}



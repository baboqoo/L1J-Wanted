package l1j.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1HpBar implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1HpBar();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1HpBar() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		//if (arg.equalsIgnoreCase("켬")) {
			if (arg.equalsIgnoreCase("on")) {
			pc.getSkill().setSkillEffect(L1SkillId.GMSTATUS_HPBAR, 0);
			return true;
		}
		//if (arg.equalsIgnoreCase("끔")) {
		if (arg.equalsIgnoreCase("off")) {
			pc.getSkill().removeSkillEffect(L1SkillId.GMSTATUS_HPBAR);

			for (L1Object obj : pc.getKnownObjects()) {
				if (isHpBarTarget(obj)) {
					pc.sendPackets(new S_HPMeter(obj.getId(), 0xFF, 0xff), true);
				}
			}
			return true;
		}
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage(cmdName + " [켬,끔] 라고 입력해 주세요. "), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
		return false;
	}

	public static boolean isHpBarTarget(L1Object obj) {
		return obj instanceof L1MonsterInstance || obj instanceof L1PcInstance || obj instanceof L1SummonInstance || obj instanceof L1PetInstance;
	}
}



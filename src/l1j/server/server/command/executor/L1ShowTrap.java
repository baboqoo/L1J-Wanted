package l1j.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TrapInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ShowTrap implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ShowTrap();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ShowTrap() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		if (pc == null) {
			return false;
		}
		//if (arg.equalsIgnoreCase("켬")) {
		if (arg.equalsIgnoreCase("on")) {
			pc.getSkill().setSkillEffect(L1SkillId.GMSTATUS_SHOWTRAPS, 0);
			return true;
		}
		//if (arg.equalsIgnoreCase("끔")) {
		if (arg.equalsIgnoreCase("off")) {
			pc.getSkill().removeSkillEffect(L1SkillId.GMSTATUS_SHOWTRAPS);

			for (L1Object obj : pc.getKnownObjects()) {
				if (obj instanceof L1TrapInstance) {
					pc.removeKnownObject(obj);
					pc.sendPackets(new S_RemoveObject(obj), true);
				}
			}
			return true;
		}
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage(cmdName + " [켬,끔] 라고 입력해 주세요. "), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
		return false;
	}
}



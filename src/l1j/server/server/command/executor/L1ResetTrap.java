package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ResetTrap implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ResetTrap();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ResetTrap() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		L1WorldTraps.getInstance(). resetAllTraps();
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("트랩을 재배치했습니다"), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(638), true), true);
		return true;
	}
}



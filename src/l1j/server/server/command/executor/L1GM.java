package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1GM implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1GM();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1GM() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		if (pc == null) {
			return false;
		}
		pc.setGm(!pc.isGm());
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("GM 세팅 = " + pc.isGm()), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(430) + pc.isGm(), true), true);
		return true;
	}
}



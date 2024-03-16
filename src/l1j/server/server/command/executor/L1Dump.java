package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.ProcessPlayer;

public class L1Dump implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Dump();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Dump() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		ProcessPlayer.dumpLog();
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("덤프 내역을 저장 하였습니다"), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(382), true), true);
		return true;
	}
}



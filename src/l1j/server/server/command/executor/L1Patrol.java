package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
//import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.command.S_BuilderUserList;

public class L1Patrol implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Patrol();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Patrol() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		if (pc == null) {
			return false;
		}
		//pc.sendPackets(new S_PacketBox(S_PacketBox.CALL_SOMETHING), true);
		pc.sendPackets(new S_BuilderUserList(), true);
		return true;
	}
}


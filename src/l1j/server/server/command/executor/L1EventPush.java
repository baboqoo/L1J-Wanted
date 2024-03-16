package l1j.server.server.command.executor;

import l1j.server.GameSystem.eventpush.EventPushManager;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1EventPush implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1EventPush();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1EventPush() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		EventPushManager.getInstance().commands(pc, arg);
		return true;
	}
}


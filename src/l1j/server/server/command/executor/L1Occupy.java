package l1j.server.server.command.executor;

import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1Occupy implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Occupy();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1Occupy() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		OccupyManager.getInstance().command(pc, arg);
		return true;
	}
}


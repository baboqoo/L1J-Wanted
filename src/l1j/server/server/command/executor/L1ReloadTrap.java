package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.trap.L1WorldTraps;

public class L1ReloadTrap implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ReloadTrap();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ReloadTrap() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		L1WorldTraps.reloadTraps();
		return true;
	}
}


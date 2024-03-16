package l1j.server.server.command.executor;

import l1j.server.GameSystem.ai.AiManager;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1CloneWar implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1CloneWar();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1CloneWar() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		AiManager.getInstance().commands(pc, arg);
		return true;
	}
}


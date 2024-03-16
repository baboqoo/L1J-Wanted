package l1j.server.server.command.executor;

import l1j.server.IndunSystem.treasureisland.TreasureIsland;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1TreasureIslandCommand implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1TreasureIslandCommand();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1TreasureIslandCommand() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		TreasureIsland.getInstance().command(pc, arg);
		return true;
	}
}


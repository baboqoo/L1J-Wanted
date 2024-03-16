package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;

public class L1TeleportReset implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1TeleportReset();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1TeleportReset() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null || pc.getNetConnection() == null) {
				return false;
			}
			pc.getTeleport().start(pc.getX(), pc.getY(), pc.getMapId(), pc.getMoveState().getHeading(), false);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}


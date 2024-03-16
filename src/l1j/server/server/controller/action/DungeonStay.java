package l1j.server.server.controller.action;

import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 던전 타이머 컨트롤러
 * @author LinOffice
 */
public class DungeonStay implements ControllerInterface {
	private static class newInstance {
		public static final DungeonStay INSTANCE = new DungeonStay();
	}
	public static DungeonStay getInstance() {
		return newInstance.INSTANCE;
	}
	private DungeonStay(){}

	@Override
	public void execute() {
	}
	
	@Override
	public void execute(L1PcInstance pc) {
		try {
			if (pc == null || pc.getNetConnection() == null || pc.noPlayerCK || pc.getDungoenTimer() == null) {
				return;
			}
			if (!pc.getDungoenTimer().isTimerInfo((int)pc.getMapId())) {
				return;
			}
			pc.getDungoenTimer().stay();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


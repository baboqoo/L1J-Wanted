package l1j.server.server.model.npc.action.html;

import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;

public class HealegateGiranOuterGatefAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new HealegateGiranOuterGatefAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private HealegateGiranOuterGatefAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		castleDoorRepair(pc, 2031, 4);
		castleDoorRepair(pc, 2034, 4);
		castleDoorRepair(pc, 2035, 4);
		return null;
	}
	
	private void castleDoorRepair(L1PcInstance pc, int npcId, int castleId) {
		if (pc.getClan().getCastleId() != castleId) {
			return;
		}
		if (War.getInstance().isNowWar(castleId)) {
			return;
		}
		L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(npcId);
		if (door == null) {
			return;
		}
		door.repairGate();
	}
}


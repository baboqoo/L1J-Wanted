package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;

public class AutorepairoffAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new AutorepairoffAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private AutorepairoffAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		castleDoorAutoRepair(pc, 0);
		return null;
	}
	
	private void castleDoorAutoRepair(L1PcInstance pc, int order) {
		L1Clan clan = pc.getClan();
		if (clan != null) {
			int castleId = clan.getCastleId();
			if (castleId != 0) {
				if (War.getInstance().isNowWar(castleId)) {
					pc.sendPackets(L1ServerMessage.sm991);
					return;
				}
				for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
					if (L1CastleLocation.checkInWarArea(castleId, door)) {
						door.setAutoStatus(order);
					}
				}
				pc.sendPackets(L1ServerMessage.sm990);
			}
		}
	}
}


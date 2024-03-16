package l1j.server.IndunSystem.training;

import java.util.TimerTask;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

public class Training extends TimerTask {
	final int mapId;

	public Training(int Id) {
		mapId = Id;
	}

	@Override
	public void run() {
		L1NpcInstance npc	= null;
		L1PcInstance pc		= null;
		L1ItemInstance item	= null;
		L1World world		= L1World.getInstance();
		for (L1Object obj : world.getVisibleObjects(mapId).values()) {
			if (obj instanceof L1DollInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
				continue;
			}
			if (obj instanceof L1NpcInstance) {
				npc = (L1NpcInstance)obj;
				npc.deleteMe();
				npc = null;
			} else if (obj instanceof L1PcInstance) {
				pc = (L1PcInstance)obj;
				pc.getTeleport().start(34065, 32313, (short)4, 0, true);
			} else if (obj instanceof L1ItemInstance) {
				item = (L1ItemInstance)obj;
				world.getInventory(item.getX(), item.getY(), item.getMapId()).removeItem(item);
			}
		}
		TrainingCreator.getInstance().removeRoom(mapId);
	}
}

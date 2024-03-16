package l1j.server.server.command.executor;

import java.util.ArrayList;

import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1DeleteGroundItem implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1DeleteGroundItem();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1DeleteGroundItem() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		L1ItemInstance l1iteminstance = null;
		ArrayList<L1PcInstance> players = null;
		L1Inventory groundInventory= null;
		LetterTable lettertable = null;
		L1FurnitureInstance furniture = null;
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1ItemInstance) {
				l1iteminstance = (L1ItemInstance) l1object;
				if (l1iteminstance.getX() == 0 && l1iteminstance.getY() == 0) {// 지면상의 아이템은 아니고, 누군가의 소유물
					continue;
				}
				players = L1World.getInstance().getVisiblePlayer(l1iteminstance, 2);
				if (0 == players.size()) {
					groundInventory = L1World.getInstance().getInventory(l1iteminstance.getX(), l1iteminstance.getY(), l1iteminstance.getMapId());
					int itemId = l1iteminstance.getItem(). getItemId();
					if (itemId >= 49016 && itemId <= 49025) { // 편지지
						lettertable = new LetterTable();
						lettertable.deleteLetter(l1iteminstance.getId());
					} else if (itemId >= 41383 && itemId <= 41400 || itemId >= 49072 && itemId <= 49076) { // 가구 // 동상
						if (l1object instanceof L1FurnitureInstance) {
							furniture = (L1FurnitureInstance) l1object;
							if (furniture.getItemObjId() == l1iteminstance.getId()) {// 이미 꺼내고 있는 가구
								FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
							}
						}
					}
					groundInventory.deleteItem(l1iteminstance);
				//	L1World.getInstance().removeVisibleObject(l1iteminstance);
				//	L1World.getInstance().removeObject(l1iteminstance);
				}
			}
		}
		//L1World.getInstance().broadcastServerMessage("월드 맵상의 아이템이 GM에 의해 삭제되었습니다. ", true);
		return true;
	}
}


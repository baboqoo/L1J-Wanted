package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.inventory.S_IdentifyDesc;
import l1j.server.server.serverpackets.inventory.S_ItemStatus;
import l1j.server.server.templates.L1Item;

public class IdentiScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public IdentiScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance targetItem = pc.getInventory().getItem(packet.readD());
			if (targetItem == null) {
				return;
			}
			if (!targetItem.isIdentified()) {
				targetItem.setIdentified(true);
				pc.getInventory().updateItem(targetItem, L1PcInventory.COL_IS_ID);
			}
			int mainid1 = targetItem.getItem().getMainId();
			L1ItemInstance main = null;
			if (mainid1 == targetItem.getItem().getItemId()) {
				main = pc.getInventory().findItemId(mainid1);
				if (main != null) {
					pc.sendPackets(new S_ItemStatus(main, pc, true, main.isEquipped()), true);
				}
			}
			pc.sendPackets(new S_IdentifyDesc(targetItem), true);
			pc.getInventory().removeItem(this, 1);
		}
	}
}


package l1j.server.server.model.item.function;

import javolution.util.FastTable;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.ItemSelectorTable;
import l1j.server.server.datatables.ItemSelectorTable.SelectorWarehouseData;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.inventory.S_ItemsWarehouseSelection;
import l1j.server.server.templates.L1Item;

public class ItemSelectorWarehouse extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ItemSelectorWarehouse(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			FastTable<SelectorWarehouseData> list = ItemSelectorTable.getSelectorWareInfo(this.getItemId());
			if (list == null || list.isEmpty()) {
				return;
			}
			pc.sendPackets(new S_ItemsWarehouseSelection(pc, this.getItemId(), list), true);
		}
	}
}


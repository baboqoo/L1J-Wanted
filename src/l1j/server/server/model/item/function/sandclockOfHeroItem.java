package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class sandclockOfHeroItem extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public sandclockOfHeroItem(L1Item item) {
		super(item);
	}
	
	private L1ItemInstance inputItem;

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int inputId = packet.readD();
			inputItem = pc.getInventory().getItem(inputId);
			if (inputItem == null) {
				return;
			}
			use(pc);
		}
	}
	
	void use(L1PcInstance pc){
		int heroId = inputItem.getItem().getItemId();
		if (heroId >= 52020 && heroId <= 52029) {
			pc.getInventory().SetDeleteTime(inputItem, ItemTable.getTerm(heroId));
			pc.getInventory().updateItem(inputItem, L1PcInventory.COL_REMAINING_TIME);
			pc.getInventory().saveItem(inputItem, L1PcInventory.COL_REMAINING_TIME);
			pc.getInventory().removeItem(this, 1);
		} else {
			pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
		}
	}
	
}


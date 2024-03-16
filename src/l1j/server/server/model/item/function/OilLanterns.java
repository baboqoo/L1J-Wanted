package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.inventory.S_ItemName;
import l1j.server.server.templates.L1Item;

public class OilLanterns extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public OilLanterns(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			for (L1ItemInstance lightItem : pc.getInventory().getItems()) {
				if (lightItem.getItem().getItemId() == 40002) {
					lightItem.setRemainingTime(this.getItem().getLightFuel());
					pc.sendPackets(new S_ItemName(lightItem), true);
					pc.sendPackets(L1ServerMessage.sm230);
					break;
				}
			}
			pc.getInventory().removeItem(this, 1);
		}
	}
}


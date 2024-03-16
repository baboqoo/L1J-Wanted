package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.inventory.S_ItemName;
import l1j.server.server.templates.L1Item;

public class Light extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Light(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			if (getItem().getType() == 2) { // light
				if (getRemainingTime() <= 0 && itemId != 40004 && itemId != 7338 && itemId != 410517) {
					return;
				}
				setNowLighting(!isNowLighting());
				pc.getLight().turnOnOffLight();
				pc.sendPackets(new S_ItemName(this), true);
				if (itemId == 410517) {
					pc.getQuest().questItemUse(itemId);
				}
			}
		}
	}
}


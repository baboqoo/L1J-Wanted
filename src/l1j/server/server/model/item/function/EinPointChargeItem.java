package l1j.server.server.model.item.function;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class EinPointChargeItem extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public EinPointChargeItem(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int value = getItem().getEtcValue();
			if (pc.getEinPoint() + value > Config.EIN.EINHASAD_POINT_LIMIT_CHARGE_VALUE) {
				pc.sendPackets(L1ServerMessage.sm7552);// 아인하사드 포인트를 더 이상  얻을 수 없습니다.
				return;
			}
			pc.addEinPoint(value);
			pc.getInventory().removeItem(this, 1);
		}
	}
}


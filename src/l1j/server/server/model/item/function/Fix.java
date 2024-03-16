package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class Fix extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Fix(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			packet.readD();
			// 무기나 방어용 기구의 경우만
			int durabilityCount = 0;
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (this.getCount() < durabilityCount) {
					break;// 숫돌 개수 체크
				}
				if (item.getItem().getItemType() == L1ItemType.NORMAL) {
					continue;
				}
				if (item.isEquipped() && item.getDurability() > 0) {
					pc.getInventory().recoveryDamage(item);
					pc.sendPackets(new S_ServerMessage(item.getDurability() == 0 ? 464 : 463, item.getLogNameRef()), true); // %0%s는 신품 같은 상태가 되었습니다.
					if (item.getItem().getItemType() == L1ItemType.ARMOR) {
						pc.getAC().addAc(-1);
					}
					durabilityCount++;
				}
			}
			if (durabilityCount > 0) {
				pc.getInventory().removeItem(this, durabilityCount);
			} else {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
			}
		}
	}
}


package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class Arrow extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Arrow(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getWeapon() == null) {
				return;
			}
			if (!L1ItemWeaponType.isBowWeapon(pc.getWeapon().getItem().getWeaponType())) {
				return;
			}
			pc.getInventory().setArrow(this);
			pc.sendPackets(new S_ServerMessage(452, this.getLogNameRef()), true);// %0가 선택되었습니다.	
		}
	}
}


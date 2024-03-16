package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class CharacterWarehouseKey extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public CharacterWarehouseKey(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getSpecialWareHouseSize() >= 60) {
				pc.sendPackets(new S_ServerMessage(3474, "60"), true);// 창고: 확장 불가(최대 확장 {0}칸)
				return;
			}
			pc.setSpecialWareHouseSize(pc.getSpecialWareHouseSize() + 20);
			pc.sendPackets(new S_PacketBox(S_PacketBox.CHARACTER_WHEREHOUSE_SIZE, pc.getSpecialWareHouseSize()), true);
			pc.sendPackets(new S_ServerMessage(1624, Integer.toString(pc.getSpecialWareHouseSize())), true);// 창고: 특수 창고 {0}칸으로 확장 완료
			pc.getInventory().consumeItem(this, 1);
		}
	}
}


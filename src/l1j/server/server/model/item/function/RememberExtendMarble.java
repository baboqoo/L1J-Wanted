package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1Item;

public class RememberExtendMarble extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public RememberExtendMarble(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getBookMarkCount() >= 100) {
				pc.sendPackets(L1ServerMessage.sm2930);
				return;
			}
			int size = pc.getBookMarkCount() + 10;
			pc.setBookMarkCount(size);
			pc.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK_SIZE_PLUS_10, size), true);
			pc.getInventory().removeItem(this, 1);
			try {
				pc.save();
			} catch(Exception e) {}
		}
	}
}


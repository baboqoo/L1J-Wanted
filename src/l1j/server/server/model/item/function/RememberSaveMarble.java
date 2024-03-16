package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.templates.L1Item;

public class RememberSaveMarble extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public RememberSaveMarble(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getBookMarkSize() <= 0) {
				pc.sendPackets(L1ServerMessage.sm2963);
				return;
			}
			pc.sendPackets(S_MessageYN.BOOKMARK_SAVE_YN);
		}
	}
}


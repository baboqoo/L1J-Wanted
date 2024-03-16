package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1Item;

public class RememberAddMarble extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public RememberAddMarble(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch(this.getItemId()){
			case 700023: // 기억의 구슬
				pc.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK_ITEM, this.getId(), this.getCreaterName() != null ? this.getCreaterName() : "$13719", L1BookMark.ShowBookmarkitem(pc, this.getId())), true);
				L1BookMark.Bookmarkitem(pc, this, this.getId(), false);
				break;
			case 700024: // 희미한 기억의 구슬
			case 700025:// 신비한 기억의 구슬
				pc.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK_ITEM, this.getItemId(), "$13719", L1BookMark.ShowBookmarkitem(pc, this.getItemId())), true);
				L1BookMark.Bookmarkitem(pc, this, this.getItemId(), false);
				break; 
				
			}
		}
	}
}


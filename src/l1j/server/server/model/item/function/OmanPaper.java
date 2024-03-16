package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class OmanPaper extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public OmanPaper(L1Item item) {
		super(item);
	}
	
	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isNotTeleport()) {
				return;
			}
			if (pc.getNetConnection().isInterServer() || pc.getMapId() == 5166 || pc.getConfig().getDuelLine() != 0 || !pc.getMap().isEscapable() && !pc.isGm()) {
				pc.sendPackets(L1ServerMessage.sm647);// 이곳에서는 텔레포트를 할 수 없습니다.
				return;
			}
			int tel_x		= getItem().getLocX();
			int tel_y		= getItem().getLocY();
			short tel_map	= getItem().getMapId();
			int item_id		= getItemId();
			if (item_id != 830011 && pc.getMapId() == tel_map) {// 동일층
				pc.getTeleport().randomTeleport(true, 200);
			} else {
				pc.getTeleport().start(tel_x, tel_y, (short) tel_map, pc.getMoveState().getHeading(), true);
			}
			pc.getInventory().removeItem(this, 1);
		}
	}
	
}

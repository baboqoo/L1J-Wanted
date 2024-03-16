package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class OmanAmulet extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public OmanAmulet(L1Item item) {
		super(item);
	}
	
	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if ((pc.getX() >= 33924 && pc.getX() <= 33931 && pc.getY() >= 33342 && pc.getY() <= 33349 && pc.getMapId() == 4) || pc.isGm()) {
				if (pc.isNotTeleport()) {
					return;
				}
				if (!pc.getMap().isEscapable() || pc.getConfig().getDuelLine() != 0) {
					pc.sendPackets(L1ServerMessage.sm647);// 이곳에서는 텔레포트를 할 수 없습니다.
					return;
				}
				int tel_x		= getItem().getLocX();
				int tel_y		= getItem().getLocY();
				short tel_map	= getItem().getMapId();
				pc.getTeleport().start(tel_x, tel_y, tel_map, pc.getMoveState().getHeading(), true);
			} else {
				pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
			}
		}
	}
}


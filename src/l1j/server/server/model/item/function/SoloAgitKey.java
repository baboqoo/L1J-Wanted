package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.templates.L1Item;

public class SoloAgitKey extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public SoloAgitKey(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		if (pc.isNotTeleport()) {
			return;
		}
		if (pc.getConfig().getDuelLine() != 0 || pc.getMapId() == 5166 || pc.getMapId() == 2237) {
			pc.sendPackets(L1ServerMessage.sm563);// 여기에서는 사용할 수 없습니다.
			return;
		}
		if (pc.getMap().getInter() != null) {
			pc.sendPackets(L1ServerMessage.sm647);
			pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			return;
		}
		if (pc.getMap().isEscapable() || pc.isGm()) {
			pc.getTeleport().c_start(32773, 32828, (short) 2237, pc.getMoveState().getHeading(), true);
		} else {
			pc.sendPackets(L1ServerMessage.sm647);
		}	
	}

}


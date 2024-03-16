package l1j.server.server.model.item.function;

import l1j.server.GameSystem.inn.InnHelper;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class InnKey extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public InnKey(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isNotTeleport()) {
				return;
			}
			if (pc.getConfig().getDuelLine() != 0 || pc.getMapId() == 5166) {
				pc.sendPackets(L1ServerMessage.sm563);// 여기에서는 사용할 수 없습니다.
				return;
			}
			if (pc.getMap().getInter() != null) {
				pc.sendPackets(L1ServerMessage.sm647);
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				return;
			}
			innEnter(pc);	
		}
	}
	
//AUTO SRM: 	private static final S_SystemMessage END_KEY_MESSAGE = new S_SystemMessage("기간이 지난 여관 열쇠 입니다."); // CHECKED OK
	private static final S_SystemMessage END_KEY_MESSAGE = new S_SystemMessage(S_SystemMessage.getRefText(1081), true);
	
	private void innEnter(L1PcInstance pc) {
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int keymap = this.getEndTime().getTime() > System.currentTimeMillis() ? this.getKey() : 0;
			if (keymap == 0) {
				pc.sendPackets(END_KEY_MESSAGE);
				return;
			}
			if (keymap == pc.getMapId()) {
				pc.sendPackets(new S_ServerMessage(74, this.getLogNameRef()), true);// %s은 사용할 수 없습니다.
				return;
			}
			int[] loc = InnHelper.getInLoc(keymap);
			if (loc == null) {
				return;
			}
			pc.getTeleport().c_start(loc[0], loc[1], (short) keymap, 5, true);
		} else {
			pc.sendPackets(L1ServerMessage.sm647);
		}
	}
}



package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ChangeItemUse extends ServerBasePacket {
	private static final String S_CHANGE_ITEM_USE = "[S] S_ChangeItemUse";

	public S_ChangeItemUse(int itemObjId) {
		writeC(Opcodes.S_CHANGE_ITEM_USE);
		writeD(itemObjId);
		writeC(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	@Override
	public String getType() {
		return S_CHANGE_ITEM_USE;
	}
}

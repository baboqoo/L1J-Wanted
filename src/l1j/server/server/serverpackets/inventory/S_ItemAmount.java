package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ItemAmount extends ServerBasePacket {
	private static final String S_ITEM_AMOUNT = "[S] S_ItemAmount";

	public S_ItemAmount(L1ItemInstance item) {
		if (item == null) {
			return;
		}
		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		writeC(Opcodes.S_CHANGE_ITEM_DESC_EX);
		writeD(item.getId());
		writeS(item.getViewName());
		writeD(item.getCount());
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_ITEM_AMOUNT;
	}

}


package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DeleteInventoryItem extends ServerBasePacket {
	private static final String S_DELETE_INVENTORY_ITEM = "[S] S_DeleteInventoryItem";

	/**
	 * 목록으로부터 아이템을 삭제한다.
	 * @param item - 삭제하는 아이템
	 */
	public S_DeleteInventoryItem(L1ItemInstance item) {
		if (item == null) {
			return;
		}
		writeC(Opcodes.S_REMOVE_INVENTORY);
		writeD(item.getId());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_DELETE_INVENTORY_ITEM;
	}
}


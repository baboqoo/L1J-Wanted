package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ItemColor extends ServerBasePacket {
	private static final String S_ITEM_COLOR = "[S] S_ItemColor";

	/** 아이템의 색을 변경한다. 축복·저주 상태가 변화했을 때 등에 보낸다 */
	public S_ItemColor(L1ItemInstance item) {
		if (item == null) {
			return;
		}
		buildPacket(item);
	}
	
	public S_ItemColor(L1ItemInstance item, int color) {
		if (item == null) {
			return;
		}
		buildPacket(item, color);
	}

	private void buildPacket(L1ItemInstance item) {
		writeC(Opcodes.S_CHANGE_ITEM_BLESS);
		writeD(item.getId());
		writeC(item.getBless()); // 0:b 1:n 2:c -의 값:아이템이 봉인되어?
	}
	
	private void buildPacket(L1ItemInstance item, int color) {
		writeC(Opcodes.S_CHANGE_ITEM_BLESS);
		writeD(item.getId());
		// 0 : 축복 1: 보통 2: 저주 3: 미확인 128: 축봉인 129: 봉인 130: 저주봉인 131: 미확인봉인
		writeC(color);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_ITEM_COLOR;
	}

}


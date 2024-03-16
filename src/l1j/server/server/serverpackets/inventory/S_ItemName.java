package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ItemName extends ServerBasePacket {
	private static final String S_ITEM_NAME = "[S] S_ItemName";
	//public static final int ON = 0x22;
	/**
	 * 아이템의 이름을 변경한다. 장비나 강화 상태가 바뀌었을 때에 보낸다.
	 */
	public S_ItemName(L1ItemInstance item) {
		if (item == null) {
			return;
		}
		// jump를 보는 한, 이 Opcode는 아이템명을 갱신시키는 목적인 만큼 사용되는 모양(장비 후나 OE 후 전용? )
		// 후에 무엇인가 데이터를 계속해 보내도 모두 무시되어 버린다
		writeC(Opcodes.S_CHANGE_ITEM_DESC);
		writeD(item.getId());
		writeS(item.getViewName());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_ITEM_NAME;
	}
}


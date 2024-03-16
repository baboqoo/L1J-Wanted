package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ItemStatus extends ServerBasePacket {
	private static final String S_ITEM_STATUS = "[S] S_ItemStatus";

	/** 아이템의 이름, 상태, 특성, 중량등의 표시를 변경한다 */
	public S_ItemStatus(L1ItemInstance item, L1PcInstance pc) {
		if (item.getItem().getSetId() != 0) {
			if (item.getItem().getMainId() == item.getItem().getItemId()) {
				if (item.isEquipped()) {
					build(item, pc, true, true);
				} else {
					build(item, pc, true, false);
				}
			} else {
				build(item, pc, false, false);
			}
		} else {
			build(item, pc, false, false);
		}
	}

	public S_ItemStatus(L1ItemInstance item, L1PcInstance pc, boolean dd, boolean check) {
		build(item, pc, dd, check);
	}

	public void build(L1ItemInstance item, L1PcInstance pc, boolean dd, boolean check) {
		writeC(Opcodes.S_CHANGE_ITEM_DESC_EX);
		writeD(item.getId());
		writeS(item.getViewName());
		writeD(item.getCount());
		if (!item.isIdentified()) {
			writeC(0);// 미감정의 경우 스테이터스를 보낼 필요는 없다
		} else {
			writeBytesWithLength(dd ? item.getStatusBytes(pc, check) : item.getStatusBytes(pc));
		}
	}

	@Override
	public byte[] getContent() {
		return _bao.toByteArray();
	}

	@Override
	public String getType() {
		return S_ITEM_STATUS;
	}
}


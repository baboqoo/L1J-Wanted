package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AddInventoryNoti extends ServerBasePacket {
	private static final String S_ADD_INVENTORY_NOTI = "[S] S_AddInventoryNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x024c;
	
	// 인벤토리에 1개의 아이템 추가
	public S_AddInventoryNoti(L1ItemInstance item, L1PcInstance pc) {
		write_init();
		write_item_info(item.getItemInfo(pc));
		writeH(0x00);
	}
	
	// 인벤토리에 복수개의 아이템 추가(로그인)
	public S_AddInventoryNoti(L1PcInstance pc) {
		write_init();
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (item == null) {
				continue;
			}
			write_item_info(item.getItemInfo(pc));
		}
		write_on_start(true);
		writeH(0);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_item_info(byte[] item_info) {
		writeRaw(0x0a);// item_info
		writeBytesWithLength(item_info);
	}
	
	void write_on_start(boolean on_start) {
		writeRaw(0x10);// on_start
		writeB(on_start);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_ADD_INVENTORY_NOTI;
	}
}


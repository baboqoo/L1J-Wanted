package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InventoryInfoNoti extends ServerBasePacket {
	private static final String S_INVENTORY_INFO_NOTI = "[S] S_InventoryInfoNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x025d;

	public S_InventoryInfoNoti(int code, java.util.LinkedList<byte[]> item_infos, int owner_object_id, int total_pages, int cur_pages) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		write_item_infos(item_infos);
		write_owner_object_id(owner_object_id);
		write_total_pages(total_pages);
		write_total_pages(cur_pages);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_item_infos(java.util.LinkedList<byte[]> item_infos) {
		if (item_infos != null && !item_infos.isEmpty()) {
			for (byte[] info : item_infos) {
				writeRaw(0x0a);
				writeBytesWithLength(info);
			}
		}
	}
	
	void write_owner_object_id(int owner_object_id) {
		writeRaw(0x10);
		writeBit(owner_object_id);
	}
	
	void write_total_pages(int total_pages) {
		writeRaw(0x18);
		writeRaw(total_pages);
	}
	
	void write_cur_pages(int cur_pages) {
		writeRaw(0x20);
		writeRaw(cur_pages);
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
		return S_INVENTORY_INFO_NOTI;
	}
}


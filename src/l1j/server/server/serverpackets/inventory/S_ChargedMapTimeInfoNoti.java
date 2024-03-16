package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_ChargedMapTimeInfoNoti extends ServerBasePacket {
	private static final String S_CHARGED_MAP_TIME_INFO_NOTI = "[S] S_ChargedMapTimeInfoNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x09c6;

	public S_ChargedMapTimeInfoNoti(java.util.LinkedList<S_ChargedMapTimeInfoNoti.InfoT> info, int used_item_name_id, int used_item_group_id) {
		write_init();
		write_info(info);
		write_used_item_name_id(used_item_name_id);
		write_used_item_group_id(used_item_group_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_info(java.util.LinkedList<S_ChargedMapTimeInfoNoti.InfoT> info) {
		for (S_ChargedMapTimeInfoNoti.InfoT val : info) {
			writeRaw(0x0a);
			writeBytesWithLength(val.getBytes());
		}
	}
	
	void write_used_item_name_id(int used_item_name_id) {
		writeRaw(0x10);
		writeBit(used_item_name_id);
	}
	
	void write_used_item_group_id(int used_item_group_id) {
		writeRaw(0x18);
		writeRaw(used_item_group_id);
	}
	
	public static class InfoT extends BinaryOutputStream {
		public InfoT(int group, int charged_time, int charged_count, int extra_charged_time) {
			super();
			write_group(group);
			write_charged_time(charged_time);
			write_charged_count(charged_count);
			write_extra_charged_time(extra_charged_time);
		}
		
		void write_group(int group) {
			writeC(0x08);
			writeC(group);
		}
		
		void write_charged_time(int charged_time) {
			writeC(0x10);
			writeBit(charged_time);
		}
		
		void write_charged_count(int charged_count) {
			writeC(0x18);
			writeC(charged_count);
		}
		
		void write_extra_charged_time(int extra_charged_time) {
			writeC(0x20);
			writeBit(extra_charged_time);
		}
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
		return S_CHARGED_MAP_TIME_INFO_NOTI;
	}
}

package l1j.server.server.serverpackets.equip;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ExtendSlotResultNoti extends ServerBasePacket {
	private static final String S_EXTEND_SLOT_RESULT_NOTI = "[S] S_ExtendSlotResultNoti";
	private byte[] _byte = null;
	public static final int RESULT		= 0x0960;
	
	public S_ExtendSlotResultNoti(int slot_position, boolean is_sucess, boolean isDiscount) {
		write_init();
		write_slot_position(slot_position);
		write_is_sucess(is_sucess);
		write_isDiscount(isDiscount);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(RESULT);
	}
	
	void write_slot_position(int slot_position) {
		writeRaw(0x08);// slot_position
		writeRaw(slot_position);
	}
	
	void write_is_sucess(boolean is_sucess) {
		writeRaw(0x10);// is_sucess
		writeB(is_sucess);
	}
	
	void write_isDiscount(boolean isDiscount) {
		writeRaw(0x18);// isDiscount
		writeB(isDiscount);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_EXTEND_SLOT_RESULT_NOTI;
	}
}


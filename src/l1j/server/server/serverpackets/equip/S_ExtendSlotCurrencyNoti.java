package l1j.server.server.serverpackets.equip;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ExtendSlotCurrencyNoti extends ServerBasePacket {
	private static final String S_EXTEND_SLOT_CURRENCY_NOTI = "[S] S_ExtendSlotCurrencyNoti";
	private byte[] _byte = null;
	public static final int CURRENCY	= 0x095d;
	
	public S_ExtendSlotCurrencyNoti(boolean isDiscount, int adena_count, int slot_position) {
		write_init();
		write_isDiscount(isDiscount);
		write_adena_count(adena_count);
		write_slot_position(slot_position);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CURRENCY);
	}
	
	void write_isDiscount(boolean isDiscount) {
		writeRaw(0x08);// isDiscount
		writeB(isDiscount);
	}
	
	void write_adena_count(int adena_count) {
		writeRaw(0x10);// adena_count
		writeBit(adena_count);
	}
	
	void write_slot_position(int slot_position) {
		writeRaw(0x18);// slot_position
		writeRaw(slot_position);
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
		return S_EXTEND_SLOT_CURRENCY_NOTI;
	}
}


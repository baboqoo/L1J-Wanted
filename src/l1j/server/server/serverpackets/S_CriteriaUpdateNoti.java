package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CriteriaUpdateNoti extends ServerBasePacket {
	private static final String S_CRITERIA_UPDATE_NOTI = "[S] S_CriteriaUpdateNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0237;
	
	public S_CriteriaUpdateNoti(int criteria_id, long quantity) {
		write_init();
		write_criteria_id(criteria_id);
		write_quantity(quantity);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_criteria_id(int criteria_id) {
		writeRaw(0x08);// criteria_id
		writeBit(criteria_id);
	}
	
	void write_quantity(long quantity) {
		writeRaw(0x10);// quantity
		writeBit(quantity);
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
		return S_CRITERIA_UPDATE_NOTI;
	}
}


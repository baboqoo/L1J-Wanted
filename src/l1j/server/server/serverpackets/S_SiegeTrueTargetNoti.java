package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SiegeTrueTargetNoti extends ServerBasePacket {
	private static final String S_SIEGE_TRUE_TARGET_NOTI = "[S] S_SiegeTrueTargetNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0069;
	
	public S_SiegeTrueTargetNoti(int targetNumber, S_SiegeTrueTargetNoti.tt_status status, int spritenumber) {
		write_init();
		write_targetNumber(targetNumber);
		write_status(status);
		write_spritenumber(spritenumber);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_targetNumber(int targetNumber) {
		writeRaw(0x08);
		writeBit(targetNumber);
	}
	
	void write_status(S_SiegeTrueTargetNoti.tt_status status) {
		writeRaw(0x10);
		writeRaw(status.value);
	}
	
	void write_spritenumber(int spritenumber) {
		writeRaw(0x18);
		writeBit(spritenumber);
	}
	
	public enum tt_status{
		begin(1),
		keep(2),
		end(3),
		;
		private int value;
		tt_status(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(tt_status v){
			return value == v.value;
		}
		public static tt_status fromInt(int i){
			switch(i){
			case 1:
				return begin;
			case 2:
				return keep;
			case 3:
				return end;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments tt_status, %d", i));
			}
		}
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
		return S_SIEGE_TRUE_TARGET_NOTI;
	}
}


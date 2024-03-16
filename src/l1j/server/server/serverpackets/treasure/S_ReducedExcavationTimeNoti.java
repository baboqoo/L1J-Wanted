package l1j.server.server.serverpackets.treasure;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ReducedExcavationTimeNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_REDUCED_EXCAVATION_TIME_NOTI = "[S] S_ReducedExcavationTimeNoti";
	public static final int NOTI = 0x0a1b;
	
	public static final S_ReducedExcavationTimeNoti DEFAULT		= new S_ReducedExcavationTimeNoti(1);// 기본
	public static final S_ReducedExcavationTimeNoti MULTIPLE	= new S_ReducedExcavationTimeNoti(5);// 5배 단축
	
	public S_ReducedExcavationTimeNoti(int time_multiple) {
		write_init();
		write_time_multiple(time_multiple);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_time_multiple(int time_multiple) {
		writeRaw(0x08);// time_multiple
		writeRaw(time_multiple);
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
		return S_REDUCED_EXCAVATION_TIME_NOTI;
	}
}

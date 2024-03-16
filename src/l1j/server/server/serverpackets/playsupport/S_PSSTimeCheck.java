package l1j.server.server.serverpackets.playsupport;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PSSTimeCheck extends ServerBasePacket {
	private static final String S_PSS_TIME_CHECK = "[S] S_PSSTimeCheck";
	private byte[] _byte = null;
	public static final int PLAY_SUPPORT_TIME_CHECK 	= 0x083b;
	
	public S_PSSTimeCheck(int remain_time){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PLAY_SUPPORT_TIME_CHECK);
		writeC(0x08);// remain_time
		writeBit(remain_time);
		writeH(0x00);
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
		return S_PSS_TIME_CHECK;
	}
}


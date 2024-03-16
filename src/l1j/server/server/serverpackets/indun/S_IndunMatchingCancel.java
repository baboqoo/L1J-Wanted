package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunMatchingCancel extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_MATCHING_CANCEL = "[S] S_IndunMatchingCancel";
	public static final int MATCHING_CANCEL = 0x08c4;
	
	public static final S_IndunMatchingCancel CANCEL	= new S_IndunMatchingCancel(true);// 인던 오토매치 종료
	
	public S_IndunMatchingCancel(boolean result) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MATCHING_CANCEL);
		writeC(0x08);// result
		writeB(result);
		writeH(0x00);
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
		return S_INDUN_MATCHING_CANCEL;
	}
}

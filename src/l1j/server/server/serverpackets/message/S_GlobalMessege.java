package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_GlobalMessege extends ServerBasePacket {
	private static final String S_GLOBAL_MESSEGE = "[S] S_GlobalMessege";
	private byte[] _byte = null;
	
	public S_GlobalMessege(int type, String msg, int iconId) {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(type);// 메세지번호
		writeC(1);// 타입
		writeS(msg);
		writeH(iconId);// 인벤이미지
	}
	
	public S_GlobalMessege(int type, String msg1, String msg2, int iconId) {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(type);// 메세지번호
		writeC(2);// 타입
		writeS(msg1);
		writeS(msg2);
		writeH(iconId);// 인벤이미지
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
		return S_GLOBAL_MESSEGE;
	}
}


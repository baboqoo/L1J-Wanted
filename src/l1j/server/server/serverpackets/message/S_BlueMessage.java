package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BlueMessage extends ServerBasePacket {
	private static final String S_BLUE_MESSAGE = "[S] S_BlueMessage";
	private byte[] _byte = null;

	public S_BlueMessage(int type, String msg1) {
		buildPacket(type, msg1, null, null, 1);
	}

	public S_BlueMessage(int type, String msg1, String msg2) {
		buildPacket(type, msg1, msg2, null, 2);
	}

	public S_BlueMessage(int type, String msg1, String msg2, String msg3) {
		buildPacket(type, msg1, msg2, msg3, 3);
	}

	private void buildPacket(int type, String msg1, String msg2, String msg3, int check) {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(type);
		if (check == 1) {
			if (msg1.length() <= 0) {
				writeC(0);
			} else {
				writeC(1);
				writeS(msg1);
			}
		} else if (check == 2) {
			writeC(2);
			writeS(msg1);
			writeS(msg2);
		} else if (check == 3) {
			writeC(3);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
		}
		writeD(0);// 더미로 넣어야 안팅김
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
		return S_BLUE_MESSAGE;
	}
}


package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.Config;

public class S_ServerMessage extends ServerBasePacket {
	private static final String S_SERVER_MESSAGE = "[S] S_ServerMessage";
	private byte[] _byte = null;

	public S_ServerMessage(int type) {
		buildPacket(type, null, null, null, null, null, 0);
	}

	public S_ServerMessage(int type, String msg1) {
		buildPacket(type, msg1, null, null, null, null, 1);
	}

	public S_ServerMessage(int type, String msg1, String msg2) {
		buildPacket(type, msg1, msg2, null, null, null, 2);
	}

	public S_ServerMessage(int type, String msg1, String msg2, String msg3) {
		buildPacket(type, msg1, msg2, msg3, null, null, 3);
	}

	public S_ServerMessage(int type, String msg1, String msg2, String msg3, String msg4) {
		buildPacket(type, msg1, msg2, msg3, msg4, null, 4);
	}

	public S_ServerMessage(int type, String msg1, String msg2, String msg3, String msg4, String msg5) {
		buildPacket(type, msg1, msg2, msg3, msg4, msg5, 5);
	}

	public static int getStringIdx(Integer index) {		
		return index + Config.ALT.STRING_SERVER_START_LINE;
	}

	private void buildPacket(int type, String msg1, String msg2, String msg3, String msg4, String msg5, int check) {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(type);
		if (check == 0) {
			writeC(0);
		} else if (check == 1) {
			writeC(1);
			writeS(msg1);
		} else if (check == 2) {
			writeC(2);
			writeS(msg1);
			writeS(msg2);
		} else if (check == 3) {
			writeC(3);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
		} else if (check == 4) {
			writeC(4);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
			writeS(msg4);
		} else {
			writeC(5);
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
			writeS(msg4);
			writeS(msg5);
		}
	}
	
	public S_ServerMessage(int code, int value) {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(code);
		writeC(0x01);
		writeH(value); 
	}
	
	public S_ServerMessage(int code, int value1, int value2) {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(code);
		writeC(0x02);
		writeD(value1);
		writeD(value2);
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
		return S_SERVER_MESSAGE;
	}
}
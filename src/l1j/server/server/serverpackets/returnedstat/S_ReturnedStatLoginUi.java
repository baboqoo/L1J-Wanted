package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ReturnedStatLoginUi extends ServerBasePacket {
	private static final String S_RETURNED_STAT_LOGIN_UI = "[S] S_ReturnedStatLoginUi";
	private byte[] _byte = null;

	public static final int UI5				= 0x41;
	public static final int UI4				= 0x44;
	
	public static final S_ReturnedStatLoginUi LOGIN_UI4 = new S_ReturnedStatLoginUi(UI4);
	public static final S_ReturnedStatLoginUi LOGIN_UI5 = new S_ReturnedStatLoginUi(UI5);
	
	public S_ReturnedStatLoginUi(int type) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeRaw(type);
		switch (type) {
		case UI4:
			writeD(1);
			writeRaw(12);
			writeH(2240);
			break;
		case UI5:
			writeRaw(0x00);
			writeRaw(0x93);
			writeRaw(0xf6);
			//writeH(45500);
			break;
		default:
			break;
		}
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_RETURNED_STAT_LOGIN_UI;
	}
}

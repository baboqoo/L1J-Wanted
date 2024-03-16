package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharacterCreate extends ServerBasePacket {
	private static final String S_CHARACTER_CREATE = "[S] S_CharacterCreate";
	
	//public static final int 케릭비번표시 = 0x33;
	//public static final int 케릭비번성공 = 0x16;
	public static final int CHARACTER_PASSWORD_DISPLAY = 0x33;
	public static final int CHARACTER_PASSWORD_SUCCESS = 0x16;
	
	private byte[] _byte = null;

	public S_CharacterCreate() {
		buildPacket();
	}

	private void buildPacket() {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(63);
		writeC(1);
	}

	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_CHARACTER_CREATE;
	}
}

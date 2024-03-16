package l1j.server.server.serverpackets.huntingquest;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_HuntingQuestUserValue extends ServerBasePacket {
	private static final String S_HUNTING_QUEST_USER_VALUE	= "[S] S_HuntingQuestUserValue";
	private byte[] _byte	= null;
	public static final int USER_VALUE	= 0x098e;
	
	public static final S_HuntingQuestUserValue HUNTING_QUEST_USER_VALUE	= new S_HuntingQuestUserValue(100);
	
	public S_HuntingQuestUserValue(int user_value) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(USER_VALUE);
		writeRaw(0x08);// user_value
		writeBit(user_value);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_HUNTING_QUEST_USER_VALUE;
	}
}


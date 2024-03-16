package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

import l1j.server.server.Opcodes;

public class S_LoginUnknown extends ServerBasePacket {
	private static final String S_LOGIN_UNKNOWN = "[S] S_LoginUnknown";
	private byte[] _byte = null;
	
	public static final S_LoginUnknown QUEST_UNKNOWN_1	= new S_LoginUnknown(256);
	public static final S_LoginUnknown QUEST_UNKNOWN_2	= new S_LoginUnknown(2);
	public static final S_LoginUnknown QUEST_UNKNOWN_3	= new S_LoginUnknown(2925);
	public static final S_LoginUnknown QUEST_UNKNOWN_4	= new S_LoginUnknown(71362000, 71362000);
	
	public static final S_LoginUnknown LOGIN_UNKNOWN	= new S_LoginUnknown();
	
	public S_LoginUnknown() {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(0x0f);
		for (int i = 0; i < 62; i++) {
			writeD(0x00);
		}
	}
	
	public S_LoginUnknown(String str) {
		writeC(Opcodes.S_VOICE_CHAT);
		StringTokenizer st = new StringTokenizer(str.toString());
		while (st.hasMoreTokens()) {
			try {
				writeC(Integer.parseInt(st.nextToken(), 16));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public S_LoginUnknown(int value) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x0a53);
		
		writeC(0x08);
		writeBit(value);
		
		writeH(0x00);
	}
	
	public S_LoginUnknown(int value, int value2) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x0090);
		
		writeC(0x08);
		writeBit(value);
		
		writeC(0x10);
		writeBit(value2);
		
		writeH(0x00);
	}
	
	public static void init(){}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_LOGIN_UNKNOWN;
	}
}


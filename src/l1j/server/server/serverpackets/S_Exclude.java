package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.proto.A_Exclude;
import l1j.server.server.clientpackets.proto.A_Exclude.eExcludeMode;

public class S_Exclude extends ServerBasePacket {
	private static final String S_EXCLUDE = "[S] S_Exclude";
	private byte[] _byte = null;
	
	public static final int LIST	= 17;
	public static final int ADD		= 18;
	public static final int DEL		= 19;
	
	public S_Exclude(String[] names, int page) {
		writeC(Opcodes.S_EVENT);
		writeC(LIST);
		writeC(0);
		writeC(page);
		writeC(names.length);
		for (String name : names) {
			writeS(name);
		}
		writeH(0);
	}

	public S_Exclude(A_Exclude.eExcludeMode mode, A_Exclude.eExcludeType type, String name) {
    	writeC(Opcodes.S_EVENT);
    	writeC(mode == eExcludeMode.eEXCLUDE_MODE_ADD ? ADD : DEL);
    	writeS(name);
    	writeC(type.toInt());
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
		return S_EXCLUDE;
	}
}


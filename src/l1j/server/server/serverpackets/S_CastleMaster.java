package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CastleMaster extends ServerBasePacket {
	private static final String S_CASTLE_MASTER = "[S] S_CastleMaster";
	private byte[] _byte = null;

	public S_CastleMaster(int type, int objecId) {
		writeC(Opcodes.S_CASTLE_OWNER);
		writeC(type);
		writeD(objecId);
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
		return S_CASTLE_MASTER;
	}

}


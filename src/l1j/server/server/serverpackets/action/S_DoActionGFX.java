package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DoActionGFX extends ServerBasePacket {
	private static final String S_DOACTION_GFX = "[S] S_DoActionGFX";
	private byte[] _byte = null;

	public S_DoActionGFX(int objectId, int actionId) {
		writeC(Opcodes.S_ACTION);
		writeD(objectId);
		writeC(actionId);
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
		return S_DOACTION_GFX;
	}
}


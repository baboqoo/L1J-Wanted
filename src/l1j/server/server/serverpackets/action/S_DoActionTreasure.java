package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DoActionTreasure extends ServerBasePacket {
	private static final String S_DO_ACTION_TREASURE = "[S] S_DoActionTreasure";
	private byte[] _byte = null;

	public S_DoActionTreasure(int object, int time) {
		writeC(Opcodes.S_ACTION);
		writeD(object);
		writeC(0x08);
		writeH(time);
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
		return S_DO_ACTION_TREASURE;
	}
}


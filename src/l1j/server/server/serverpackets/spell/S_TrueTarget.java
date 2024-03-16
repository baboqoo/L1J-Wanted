package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TrueTarget extends ServerBasePacket {
	private static final String S_TRUE_TARGET = "[S] S_TrueTarget";
	private byte[] _byte = null;

	public S_TrueTarget(int targetId, boolean active) {
		writeC(Opcodes.S_EVENT);
		writeC(0xc2);
		writeD(targetId);
		writeD(13135);// sprite_id
		writeD(active ? 1 : 0);
		writeH(0x00);
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
		return S_TRUE_TARGET;
	}

}


package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Effect extends ServerBasePacket {
	private static final String S_EFFECT = "[S] S_Effect";
	private byte[] _byte = null;

	public S_Effect(int object_id, int sprite_id) {
		writeC(Opcodes.S_EFFECT);
		writeD(object_id);
		writeH(sprite_id);
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
		return S_EFFECT;
	}
}


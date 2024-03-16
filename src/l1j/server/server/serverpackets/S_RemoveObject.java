package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;

public class S_RemoveObject extends ServerBasePacket {
	private static final String S_REMOVE_OBJECT = "[S] S_RemoveObject";
	private byte[] _byte = null;

	public S_RemoveObject(L1Object obj) {
		writeC(Opcodes.S_REMOVE_OBJECT);
		writeD(obj.getId());
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
		return S_REMOVE_OBJECT;
	}
}


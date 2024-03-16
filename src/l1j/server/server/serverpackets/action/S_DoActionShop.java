package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DoActionShop extends ServerBasePacket {
	private static final String S_DO_ACTION_SHOP = "[S] S_DoActionShop";
	private byte[] _byte = null;

	public S_DoActionShop(int object, int gfxid, byte[] message) {
		writeC(Opcodes.S_ACTION);
		writeD(object);
		writeC(gfxid);
		writeByte(message);
	}
	
	public S_DoActionShop(int object, int gfxid, String message) {
		writeC(Opcodes.S_ACTION);
		writeD(object);
		writeC(gfxid);
		writeS(message);
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
		return S_DO_ACTION_SHOP;
	}
}


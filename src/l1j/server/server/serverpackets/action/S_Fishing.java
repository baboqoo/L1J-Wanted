package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_Fishing extends ServerBasePacket {
	private static final String S_FISHING = "[S] S_Fishing";
	private byte[] _byte = null;

	public S_Fishing() {
		buildPacket();
	}

	public S_Fishing(int objectId, int motionNum, int x, int y) {
		buildPacket(objectId, motionNum, x, y);
	}

	void buildPacket() {
		writeC(Opcodes.S_ACTION);
		writeC(0x37);
		writeD(0x76002822);
		writeH(0x8AC3);
	}

	void buildPacket(int objectId, int motionNum, int x, int y) {
		writeC(Opcodes.S_ACTION);
		writeD(objectId);
		writeC(motionNum);
		writeH(x);
		writeH(y);
		writeD(0);
		writeH(0);
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
		return S_FISHING;
	}
}


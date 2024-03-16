package l1j.server.server.serverpackets.polymorph;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ChangeShape extends ServerBasePacket {
	private byte[] _byte = null;
	
	public S_ChangeShape(int objId, int polyId) {
		buildPacket(objId, polyId, false);
	}

	public S_ChangeShape(int objId, int polyId, boolean weaponTakeoff) {
		buildPacket(objId, polyId, weaponTakeoff);
	}

	private void buildPacket(int objId, int polyId, boolean weaponTakeoff) {
		writeC(Opcodes.S_POLYMORPH);
		writeD(objId);
		writeH(polyId);
		// 왜 29인가 불명
		writeC(weaponTakeoff ? 0 : 29);
		writeC(0xff);
		writeC(0xff);
		writeC(0x00);
		writeS("abcd");
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}


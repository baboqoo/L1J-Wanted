package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_MoveCharPacket extends ServerBasePacket {
	private static final String S_MOVE_CHAR_PACKET = "[S] S_MoveCharPacket";
	private byte[] _byte = null;

	public S_MoveCharPacket(L1Character cha) {
		int x = cha.getX();
		int y = cha.getY();

		switch (cha.getMoveState().getHeading()) {
		case 1:x--;y++;break;
		case 2:x--;break;
		case 3:x--;y--;break;
		case 4:y--;break;
		case 5:x++;y--;break;
		case 6:x++;break;
		case 7:x++;y++;break;
		case 0:y++;break;
		}

		writeC(Opcodes.S_MOVE_OBJECT);
		writeD(cha.getId());
		writeH(x);
		writeH(y);
		writeC(cha.getMoveState().getHeading());
		writeC(129);
		writeD(0);
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
		return S_MOVE_CHAR_PACKET;
	}
}

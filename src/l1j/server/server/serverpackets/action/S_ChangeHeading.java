package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ChangeHeading extends ServerBasePacket {
	private static final String S_CHANGE_HEADING = "[S] S_ChangeHeading";
	private byte[] _byte = null;

	public S_ChangeHeading(L1Character cha) {
		writeC(Opcodes.S_CHANGE_DIRECTION);
		writeD(cha.getId());
		writeC(cha.getMoveState().getHeading());
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
		return S_CHANGE_HEADING;
	}
}


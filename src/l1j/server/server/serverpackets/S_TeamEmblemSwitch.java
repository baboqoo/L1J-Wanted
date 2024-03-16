package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_TeamEmblemSwitch extends ServerBasePacket {
	private static final String S_TEAM_EMBLEM_SWITCH = "[S] S_TeamEmblemSwitch";
	private byte[] _byte = null;
	public static final int SWITCH = 0x022e;
	
	public S_TeamEmblemSwitch(boolean is_turn_on){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SWITCH);
		writeRaw(0x08);// is_turn_on
		writeB(is_turn_on);
        writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_TEAM_EMBLEM_SWITCH;
	}
}


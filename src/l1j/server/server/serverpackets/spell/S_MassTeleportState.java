package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_MassTeleportState extends ServerBasePacket {
	private static final String S_MASS_TELEPORT_STATE = "[S] S_MassTeleportState";
	private byte[] _byte = null;
	public static final int STATE	= 0x0a22;
	
	public static final S_MassTeleportState ON	= new S_MassTeleportState(true);
	public static final S_MassTeleportState OFF	= new S_MassTeleportState(false);
	
	private S_MassTeleportState(boolean mass_teleport_state){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(STATE);
		writeRaw(0x08);// mass_teleport_state
		writeB(mass_teleport_state);
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
		return S_MASS_TELEPORT_STATE;
	}
}


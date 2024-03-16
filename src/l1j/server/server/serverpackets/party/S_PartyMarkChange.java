package l1j.server.server.serverpackets.party;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PartyMarkChange extends ServerBasePacket {
	private static final String S_PARTY_MARK_CHANGE = "[S] S_PartyMarkChange";
	private byte[] _byte = null;
	public static final int MARK_CHANGE	= 0x0153;
	
	public S_PartyMarkChange(byte[] flag) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MARK_CHANGE);
		writeByte(flag);
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
		return S_PARTY_MARK_CHANGE;
	}

}


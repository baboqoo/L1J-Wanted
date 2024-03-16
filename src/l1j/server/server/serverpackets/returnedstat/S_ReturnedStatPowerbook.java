package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ReturnedStatPowerbook extends ServerBasePacket {
	private static final String S_RETURNED_STAT_POWERBOOK = "[S] S_ReturnedStatPowerbook";
	private byte[] _byte = null;
	public static final int POWERBOOK_SEARCH	= 0x19;
	
	public S_ReturnedStatPowerbook(String val) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeRaw(POWERBOOK_SEARCH);
		writeRaw(0x00);
		writeD(0x2c24a1a6);
		writeD(0x462c2e40);
		writeD(0x10567981);
		writeD(0x72771a38);
		writeS(val);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_RETURNED_STAT_POWERBOOK;
	}
}

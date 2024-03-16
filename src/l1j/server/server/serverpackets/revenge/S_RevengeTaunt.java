package l1j.server.server.serverpackets.revenge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_RevengeTaunt extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_REVENGE_TAUNT = "[S] S_RevengeTaunt";
	public static final int TAUNT	= 0x041f;
	
	public S_RevengeTaunt(eRevengeResult result) {
		write_init();
		write_result(result);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TAUNT);
	}
	
	void write_result(eRevengeResult result) {
		writeRaw(0x08);
		writeRaw(result.toInt());
	}
	
	void write_anonymity_taunt(boolean anonymity_taunt) {
		writeRaw(0x10);
		writeB(anonymity_taunt);
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
		return S_REVENGE_TAUNT;
	}
}

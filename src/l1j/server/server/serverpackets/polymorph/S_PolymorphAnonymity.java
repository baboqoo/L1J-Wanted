package l1j.server.server.serverpackets.polymorph;

import l1j.server.common.data.ePolymorphAnonymityType;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PolymorphAnonymity extends ServerBasePacket {
	private static final String S_POLYMORPH_ANONYMITY = "[S] S_PolymorphAnonymity";
	private byte[] _byte = null;
	public static final int ANONYMITY	= 0x019b;
	
	public S_PolymorphAnonymity(ePolymorphAnonymityType anonymity_type){
		write_init();
		write_anonymity_type(anonymity_type);
		write_result_type(true);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ANONYMITY);
	}
	
	void write_anonymity_type(ePolymorphAnonymityType anonymity_type) {
		writeRaw(0x08);// anonymity_type
		writeRaw(anonymity_type.toInt());
	}
	
	void write_result_type(boolean result_type) {
		writeRaw(0x10);// result_type
		writeB(result_type);
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
		return S_POLYMORPH_ANONYMITY;
	}
}


package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeContribution extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_CONTRIBUTION = "[S] S_BloodPledgeContribution";
	private byte[] _byte = null;
	public static final int CONTRIBUTION	= 0x096f;
	
	public S_BloodPledgeContribution(long value) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CONTRIBUTION);
		writeC(0x08);
		writeBit(value);
		writeH(0x00);
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
		return S_BLOODPLEDGE_CONTRIBUTION;
	}
}


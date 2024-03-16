package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeJoinLimitInfo extends ServerBasePacket {
	private static final String S_BLOOD_PLEDGE_JOIN_LIMIT_INFO = "[S] S_BloodPledgeJoinLimitInfo";
	private byte[] _byte = null;
	public static final int INFO	= 0x0aad;
	
	public S_BloodPledgeJoinLimitInfo(L1Clan pledge) {
		write_init();
		write_limit_level(pledge.get_limit_level());
		java.util.LinkedList<String> limit_user_names = pledge.get_limit_user_names();
		if (limit_user_names != null && !limit_user_names.isEmpty()) {
			write_limit_user_names(limit_user_names);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_limit_level(int limit_level) {
		writeRaw(0x08);
		writeBit(limit_level);
	}
	
	void write_limit_user_names(java.util.LinkedList<String> limit_user_names) {
		for (String name : limit_user_names) {
			writeRaw(0x12);
			writeStringWithLength(name);
		}
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
		return S_BLOOD_PLEDGE_JOIN_LIMIT_INFO;
	}
}


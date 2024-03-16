package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeStoreAllowInfo extends ServerBasePacket {
	private static final String S_BLOOD_PLEDGE_STORE_ALLOW_INFO = "[S] S_BloodPledgeStoreAllowInfo";
	private byte[] _byte = null;
	public static final int INFO	= 0x0ac8;
	
	public S_BloodPledgeStoreAllowInfo(L1Clan pledge) {
		write_init();
		java.util.LinkedList<String> limit_user_names = pledge.get_store_allow_list();
		if (limit_user_names != null && !limit_user_names.isEmpty()) {
			write_limit_user_names(limit_user_names);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_limit_user_names(java.util.LinkedList<String> limit_user_names) {
		for (String name : limit_user_names) {
			writeRaw(0x0a);
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
		return S_BLOOD_PLEDGE_STORE_ALLOW_INFO;
	}
}


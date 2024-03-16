package l1j.server.server.serverpackets.pledge;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.StringUtil;

public class S_BloodPledgeUserInfo extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_USER_INFO = "[S] S_BloodPledgeUserInfo";
	private byte[] _byte = null;
	public static final int INFO	= 0x0219;
	
	public S_BloodPledgeUserInfo(String clanname, eBloodPledgeRankType rank, boolean store_allow) {
		boolean isFlag = rank != null && !StringUtil.isNullOrEmpty(clanname);
		write_init();
		write_bloodpledge_name(clanname);
		write_rank(isFlag ? rank.toInt() : 11);
		write_store_allow(store_allow);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_bloodpledge_name(String bloodpledge_name) {
		writeC(0x0a);// bloodpledge_name
		writeStringWithLength(bloodpledge_name);
	}
	
	void write_rank(int rank) {
		writeC(0x10);// rank
		writeC(rank);
	}
	
	void write_store_allow(boolean store_allow) {
		writeC(0x18);// store_allow
		writeB(store_allow);
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
		return S_BLOODPLEDGE_USER_INFO;
	}
}


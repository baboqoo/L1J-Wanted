package l1j.server.server.serverpackets.party;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PartyMemberList extends ServerBasePacket {
	private static final String S_PARTY_MEMBER_LIST = "[S] S_PartyMemberList";
	private byte[] _byte = null;
	public static final int LIST	= 0x0337;
	
	public S_PartyMemberList(L1PcInstance pc) {
		write_init();
		write_leader_name(pc.getParty().getLeader().getName());
		for (L1PcInstance member : pc.getParty().getMembersArray()) {
			write_member_info(memberByteInfo(member));
		}
		write_leader_cache_server_no(Config.VERSION.SERVER_NUMBER);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST);
	}
	
	void write_leader_name(String leader_name) {
		writeRaw(0x0a);
		writeS2(leader_name);
	}
	
	void write_member_info(byte[] member_info) {
		writeRaw(0x12);
		writeBytesWithLength(member_info);
	}
	
	void write_leader_cache_server_no(int leader_cache_server_no) {
		writeRaw(0x18);
		writeBit(leader_cache_server_no);
	}
	
	byte[] memberByteInfo(L1PcInstance member) {
		PartyMemberInfo memberInfo = null;
		try {
			memberInfo = new PartyMemberInfo().write_member_info(member);
			return memberInfo.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (memberInfo != null) {
				try {
					memberInfo.close();
					memberInfo = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
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
		return S_PARTY_MEMBER_LIST;
	}

}


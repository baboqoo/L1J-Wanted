package l1j.server.server.serverpackets.party;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PartyMemberListChange extends ServerBasePacket {
	private static final String S_PARTY_MEMBER_LIST_CHANGE = "[S] S_PartyMemberListChange";
	private byte[] _byte = null;
	public static final int CHANGE	= 0x0338;
	
	public static enum PartyMemberChangeType {
		NEW_USER, OUT_USER, CHANGE_LEADER
	}
	
	public S_PartyMemberListChange(PartyMemberChangeType type, L1PcInstance member) {
		write_init();
		switch (type) {
		case NEW_USER:
			write_new_user(memberByteInfo(member));
			break;
		case OUT_USER:
			write_out_user(member.getName());
			break;
		case CHANGE_LEADER:
			write_leader_name(member.getName());
			break;
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHANGE);
	}
	
	void write_new_user(byte[] new_user) {
		writeRaw(0x22);// new_user
		writeBytesWithLength(new_user);
	}
	
	void write_out_user(String out_user) {
		writeRaw(0x12);// out_user
		writeS2(out_user);
	}
	
	void write_leader_name(String leader_name) {
		writeRaw(0x0a);// leader_name
		writeS2(leader_name);
	}

	byte[] memberByteInfo(L1PcInstance new_user) {
		PartyMemberInfo memberInfo = null;
		try {
			memberInfo = new PartyMemberInfo().write_member_info(new_user);
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
		return S_PARTY_MEMBER_LIST_CHANGE;
	}

}


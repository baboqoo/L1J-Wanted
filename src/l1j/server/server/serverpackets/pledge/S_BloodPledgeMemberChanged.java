package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeMemberChanged extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_MEMBER_CHANGED = "[S] S_BloodPledgeMemberChanged";
	private byte[] _byte = null;
	public static final int MEMBER_CHANGED	= 0x025f;
	
	public S_BloodPledgeMemberChanged(MemberChangedReason reason, int objId){
		write_init();
		write_reason(reason);// 0:본인, 1:, 2:접속중, 3:
		write_user_id(objId);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MEMBER_CHANGED);
	}
	
	void write_reason(MemberChangedReason reason) {
		writeRaw(0x08);// reason
		writeRaw(reason.value);
	}
	
	void write_user_id(int user_id) {
		writeRaw(0x10);// user_id
		writeBit(user_id);
	}
	
	public enum MemberChangedReason{
		ADD(0),
		DEL(1),
		ADD_ME(2),
		DEL_ME(3),
		;
		private int value;
		MemberChangedReason(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(MemberChangedReason v){
			return value == v.value;
		}
		public static MemberChangedReason fromInt(int i){
			switch(i){
			case 0:
				return ADD;
			case 1:
				return DEL;
			case 2:
				return ADD_ME;
			case 3:
				return DEL_ME;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments Reason, %d", i));
			}
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
		return S_BLOODPLEDGE_MEMBER_CHANGED;
	}
}


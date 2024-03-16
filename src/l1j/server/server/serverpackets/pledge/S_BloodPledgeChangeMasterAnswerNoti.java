package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeChangeMasterAnswerNoti extends ServerBasePacket {// 위임 요청 대상이 받는 패킷
	private static final String S_BLOOD_PLEDGE_CHANGE_MASTER_ANSWER_NOTI = "[S] S_BloodPledgeChangeMasterAnswerNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0ac6;
	
	public S_BloodPledgeChangeMasterAnswerNoti(String new_master_name, boolean yes) {
		write_init();
		write_new_master_name(new_master_name);
		write_yes(yes);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_new_master_name(String new_master_name) {
		writeRaw(0x0a);
		writeStringWithLength(new_master_name);
	}
	
	void write_yes(boolean yes) {
		writeRaw(0x10);
		writeB(yes);
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
		return S_BLOOD_PLEDGE_CHANGE_MASTER_ANSWER_NOTI;
	}
}


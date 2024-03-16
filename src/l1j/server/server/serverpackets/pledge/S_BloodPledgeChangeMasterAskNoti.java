package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeChangeMasterAskNoti extends ServerBasePacket {// 위임 요청 대상이 받는 패킷
	private static final String S_BLOOD_PLEDGE_CHANGE_MASTER_ASK_NOTI = "[S] S_BloodPledgeChangeMasterAskNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0ac4;
	
	public S_BloodPledgeChangeMasterAskNoti(String old_master_name) {
		write_init();
		write_old_master_name(old_master_name);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_old_master_name(String old_master_name) {
		writeRaw(0x0a);
		writeStringWithLength(old_master_name);
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
		return S_BLOOD_PLEDGE_CHANGE_MASTER_ASK_NOTI;
	}
}


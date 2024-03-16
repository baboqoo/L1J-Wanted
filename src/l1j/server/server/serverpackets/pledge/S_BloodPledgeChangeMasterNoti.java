package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeChangeMasterNoti extends ServerBasePacket {// 혈맹 군주 변경 알림
	private static final String S_BLOOD_PLEDGE_CHANGE_MASTER_NOTI = "[S] S_BloodPledgeChangeMasterNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0ac2;
	
	public S_BloodPledgeChangeMasterNoti(String old_master_name, String new_master_name) {
		write_init();
		write_old_master_name(old_master_name);
		write_new_master_name(new_master_name);
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
	
	void write_new_master_name(String new_master_name) {
		writeRaw(0x12);
		writeStringWithLength(new_master_name);
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
		return S_BLOOD_PLEDGE_CHANGE_MASTER_NOTI;
	}
}


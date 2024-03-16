package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeEnterNoticeNoti extends ServerBasePacket {
	private static final String S_BLOOD_PLEDGE_ENTER_NOTICE_NOTI = "[S] S_BloodPledgeEnterNoticeNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0ab2;
	
	public S_BloodPledgeEnterNoticeNoti(String enter_notice) {
		write_init();
		write_enter_notice(enter_notice);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_enter_notice(String enter_notice) {
		writeC(0x0a);
		writeStringWithLength(enter_notice);
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
		return S_BLOOD_PLEDGE_ENTER_NOTICE_NOTI;
	}
}


package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeStoreAllowInfoNoti extends ServerBasePacket {
	private static final String S_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI = "[S] S_BloodPledgeStoreAllowInfoNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0acc;
	
	public static final S_BloodPledgeStoreAllowInfoNoti ALLOW		= new S_BloodPledgeStoreAllowInfoNoti(true);
	public static final S_BloodPledgeStoreAllowInfoNoti DISALLOW	= new S_BloodPledgeStoreAllowInfoNoti(false);
	
	public S_BloodPledgeStoreAllowInfoNoti(boolean store_allow) {
		write_init();
		write_store_allow(store_allow);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_store_allow(boolean store_allow) {
		writeRaw(0x08);
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
		return S_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI;
	}
}


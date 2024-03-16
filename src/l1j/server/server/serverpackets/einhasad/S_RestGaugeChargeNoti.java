package l1j.server.server.serverpackets.einhasad;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_RestGaugeChargeNoti extends ServerBasePacket {
	private static final String S_REST_GAUGE_CHARGE_NOTI = "[S] S_RestGaugeChargeNoti";
	public static final int CHARGE	= 0x0092;
	private byte[] _byte = null;
	
	public static final S_RestGaugeChargeNoti PLUS_50	= new S_RestGaugeChargeNoti(50);
	public static final S_RestGaugeChargeNoti PLUS_100	= new S_RestGaugeChargeNoti(100);
	public static final S_RestGaugeChargeNoti PLUS_150	= new S_RestGaugeChargeNoti(150);
	public static final S_RestGaugeChargeNoti PLUS_200	= new S_RestGaugeChargeNoti(200);
	public static final S_RestGaugeChargeNoti PLUS_250	= new S_RestGaugeChargeNoti(250);
	public static final S_RestGaugeChargeNoti PLUS_300	= new S_RestGaugeChargeNoti(300);
	
	public S_RestGaugeChargeNoti(int bonus_percent_value) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHARGE);
		writeRaw(0x08);
		writeBit(bonus_percent_value);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_REST_GAUGE_CHARGE_NOTI;
	}
}


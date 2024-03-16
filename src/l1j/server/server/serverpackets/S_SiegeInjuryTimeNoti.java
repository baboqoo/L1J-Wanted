package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.StringUtil;

public class S_SiegeInjuryTimeNoti extends ServerBasePacket {
	private static final String S_SIEGE_INJURY_TIME_NOTI = "[S] S_SiegeInjuryTimeNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x004c;
	
	public static final S_SiegeInjuryTimeNoti CASTLE_WAR_TIME_NONE = new S_SiegeInjuryTimeNoti(S_SiegeInjuryTimeNoti.SIEGE_KIND.SIEGE_DEFFENCE, 0, StringUtil.EmptyString);
	public S_SiegeInjuryTimeNoti(S_SiegeInjuryTimeNoti.SIEGE_KIND siegeKind, int remainSecond, String pledgeName) {
		write_init();
		write_siegeKind(siegeKind);
		if (remainSecond > 0) {
			write_remainSecond(remainSecond << 1);
			write_pledgeName(pledgeName);
		} else {
			write_remainSecond(0);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_siegeKind(S_SiegeInjuryTimeNoti.SIEGE_KIND siegeKind) {
		writeC(0x08);// siegeKind
		writeC(siegeKind.value);
	}
	
	void write_remainSecond(int remainSecond) {
		writeC(0x10);// remainSecond
		writeBit(remainSecond);
	}
	
	void write_pledgeName(String pledgeName) {
		writeC(0x1a);// pledgeName
		writeStringWithLength(pledgeName);
	}
	
	public enum SIEGE_KIND{
		SIEGE_DEFFENCE(1),	// 수성
		SIEGE_ATTACK(2),	// 공성
		;
		private int value;
		SIEGE_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SIEGE_KIND v){
			return value == v.value;
		}
		public static SIEGE_KIND fromInt(int i){
			switch(i){
			case 1:
				return SIEGE_DEFFENCE;
			case 2:
				return SIEGE_ATTACK;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SIEGE_KIND, %d", i));
			}
		}
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
		return S_SIEGE_INJURY_TIME_NOTI;
	}
}


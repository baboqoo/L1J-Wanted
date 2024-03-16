package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SiegeZoneUpdateNoti extends ServerBasePacket {
	private static final String S_SIEGE_ZONE_UPDATE_NOTI = "[S] S_SiegeZoneUpdateNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0042;
	
	public S_SiegeZoneUpdateNoti(S_SiegeZoneUpdateNoti.SIEGE_ZONE_KIND siegeZoneKind) {
		write_init();
		write_siegeZoneKind(siegeZoneKind);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_siegeZoneKind(S_SiegeZoneUpdateNoti.SIEGE_ZONE_KIND siegeZoneKind) {
		writeRaw(0x08);
		writeRaw(siegeZoneKind.value);
	}
	
	public enum SIEGE_ZONE_KIND{
		SIEGE_ZONE_BEGIN(1),
		SIEGE_ZONE_END(2),
		;
		private int value;
		SIEGE_ZONE_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SIEGE_ZONE_KIND v){
			return value == v.value;
		}
		public static SIEGE_ZONE_KIND fromInt(int i){
			switch(i){
			case 1:
				return SIEGE_ZONE_BEGIN;
			case 2:
				return SIEGE_ZONE_END;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SIEGE_ZONE_KIND, %d", i));
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
		return S_SIEGE_ZONE_UPDATE_NOTI;
	}
}


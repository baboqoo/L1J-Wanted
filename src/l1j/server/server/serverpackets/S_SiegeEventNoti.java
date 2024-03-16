package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;

public class S_SiegeEventNoti extends ServerBasePacket {
	private static final String S_SIEGE_EVENT_NOTI = "[S] S_SiegeEventNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0044;
	
	public S_SiegeEventNoti(int castleId, S_SiegeEventNoti.SIEGE_EVENT_KIND eventKind) {
		byte[] ownerNameBytes	= null;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getCastleId() == castleId) {
				ownerNameBytes	= clan.getClanName().getBytes();
				break;
			}
		}
		if (ownerNameBytes == null) {
			ownerNameBytes = L1CastleLocation.DEFAULT_CASTLE_OWNER_BYTES[castleId - 1];
		}
		
		write_init();
		write_eventKind(eventKind);
		write_castles(L1CastleLocation.CASTLE_DESC[castleId - 1], ownerNameBytes);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_eventKind(S_SiegeEventNoti.SIEGE_EVENT_KIND eventKind) {
		writeC(0x08);// eventKind
		writeC(eventKind.value);
	}
	
	void write_castles(int stringNumber, byte[] ownerNameBytes) {
		writeC(0x12);// castles
		writeC(getBitSize(stringNumber) + ownerNameBytes.length + 3);
			
		writeC(0x08);// stringNumber
		writeBit(stringNumber);
			
		writeC(0x12);// ownerName
		writeBytesWithLength(ownerNameBytes);
	}
	
	public enum SIEGE_EVENT_KIND{
		SIEGE_EVENT_KIND_START(1),			// 공성전이 시작되었습니다.
		SIEGE_EVENT_PROGRESSING(2),			// 공성전이 진행중입니다.
		SIEGE_EVENT_KIND_COLLECT_END(3),	// 붉은 기사단이 공성전에서 승리하였습니다.
		SIEGE_EVENT_KIND_END(4),			// 공성전이 종료되어습니다.
		;
		private int value;
		SIEGE_EVENT_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SIEGE_EVENT_KIND v){
			return value == v.value;
		}
		public static SIEGE_EVENT_KIND fromInt(int i){
			switch(i){
			case 1:
				return SIEGE_EVENT_KIND_START;
			case 2:
				return SIEGE_EVENT_PROGRESSING;
			case 3:
				return SIEGE_EVENT_KIND_COLLECT_END;
			case 4:
				return SIEGE_EVENT_KIND_END;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments SIEGE_EVENT_KIND, %d", i));
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
		return S_SIEGE_EVENT_NOTI;
	}

}


package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_FishingNoti extends ServerBasePacket {
	private static final String S_FISHING_NOTI = "[S] S_FishingNoti";
	private byte[] _byte = null;
	private static final int FISHING_NOTI	= 0x003f;
	
	public static final S_FishingNoti FISH_NORMAL_RIL = new S_FishingNoti(240, S_FishingNoti.FISHING_ROD.FISHING_ROD_NORMAL, 0);

	public S_FishingNoti(int startTime, S_FishingNoti.FISHING_ROD rodKind, int rodCount) {
		write_init();
		write_fishingKind(S_FishingNoti.FISHING_NOT_KIND.FISHING_NOT_KIND_START);
		write_startTime(startTime);
		write_rodKind(rodKind);
		write_rodCount(rodKind == S_FishingNoti.FISHING_ROD.FISHING_ROD_SPECIAL ? rodCount : 0);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(FISHING_NOTI);
	}
	
	void write_fishingKind(S_FishingNoti.FISHING_NOT_KIND fishingKind) {
		writeRaw(0x08);// fishingKind
		writeRaw(fishingKind.value);
	}
	
	void write_startTime(int startTime) {
		writeRaw(0x10);// startTime 챔질 시간
		writeBit(startTime);
	}
	
	void write_rodKind(S_FishingNoti.FISHING_ROD rodKind) {
		writeRaw(0x18);// rodKind 1:릴 미장착,  2:릴장착
		writeRaw(rodKind.value);
	}
	
	void write_rodCount(int rodCount) {
		writeRaw(0x20);// rodCount 릴 장착 횟수
		writeBit(rodCount);
	}
	
	public enum FISHING_NOT_KIND{
		FISHING_NOT_KIND_START(1),
		FISHING_NOT_KIND_END_PREPARE(2),
		;
		private int value;
		FISHING_NOT_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(FISHING_NOT_KIND v){
			return value == v.value;
		}
		public static FISHING_NOT_KIND fromInt(int i){
			switch(i){
			case 1:
				return FISHING_NOT_KIND_START;
			case 2:
				return FISHING_NOT_KIND_END_PREPARE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments FISHING_NOT_KIND, %d", i));
			}
		}
	}
	public enum FISHING_ROD{
		FISHING_ROD_NORMAL(1),
		FISHING_ROD_SPECIAL(2),
		;
		private int value;
		FISHING_ROD(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(FISHING_ROD v){
			return value == v.value;
		}
		public static FISHING_ROD fromInt(int i){
			switch(i){
			case 1:
				return FISHING_ROD_NORMAL;
			case 2:
				return FISHING_ROD_SPECIAL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments FISHING_ROD, %d", i));
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
		return S_FISHING_NOTI;
	}
}


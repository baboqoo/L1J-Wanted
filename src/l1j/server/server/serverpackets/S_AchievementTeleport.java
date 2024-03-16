package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_AchievementTeleport extends ServerBasePacket {
	private static final String S_ACHIEVEMENT_TELEPORT = "[S] S_AchievementTeleport";
	private byte[] _byte = null;
	public static final int TELEPORT = 0x0236;
	
	public S_AchievementTeleport(S_AchievementTeleport.eResultCode result, int achievement_id) {
		write_init();
		write_result(result);
		write_achievement_id(achievement_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TELEPORT);
	}
	
	void write_result(S_AchievementTeleport.eResultCode result) {
		writeRaw(0x08);// result
		writeBit(result.value);
	}
	
	void write_achievement_id(int achievement_id) {
		writeRaw(0x10);// achievement_id
		writeBit(achievement_id);
	}
	
	public enum eResultCode{
		TELEPORT_SUCCESS(0),
		TELEPORT_FAIL(1),
		TELEPORT_FAIL_NOT_ENOUGH_ADENA(2),
		TELEPORT_FAIL_WRONG_LOCATION(3),
		;
		private int value;
		eResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResultCode v){
			return value == v.value;
		}
		public static eResultCode fromInt(int i){
			switch(i){
			case 0:
				return TELEPORT_SUCCESS;
			case 1:
				return TELEPORT_FAIL;
			case 2:
				return TELEPORT_FAIL_NOT_ENOUGH_ADENA;
			case 3:
				return TELEPORT_FAIL_WRONG_LOCATION;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResultCode, %d", i));
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
		return S_ACHIEVEMENT_TELEPORT;
	}
}


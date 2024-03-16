package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CompletedAchievementReward extends ServerBasePacket {
	private static final String S_COMPLETED_ACHIEVEMENT_REWARD = "[S] S_CompletedAchievementReward";
	private byte[] _byte = null;
	public static final int REWARD = 0x0234;
	
	public S_CompletedAchievementReward(S_CompletedAchievementReward.eResultCode result, int achievement_id) {
		write_init();
		write_result(result);
		write_achievement_id(achievement_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(REWARD);
	}
	
	void write_result(S_CompletedAchievementReward.eResultCode result) {
		writeRaw(0x08);// result
		writeBit(result.value);
	}
	
	void write_achievement_id(int achievement_id) {
		writeRaw(0x10);// achievement_id
		writeBit(achievement_id);
	}
	
	public enum eResultCode{
		REQUEST_REWARD_SUCCESS(0),
		REQUEST_REWARD_FAIL(1),
		REQUEST_REWARD_FAIL_ALREADY_GET_REWARD(2),
		REQUEST_REWARD_FAIL_NOT_COMPLETED(3),
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
				return REQUEST_REWARD_SUCCESS;
			case 1:
				return REQUEST_REWARD_FAIL;
			case 2:
				return REQUEST_REWARD_FAIL_ALREADY_GET_REWARD;
			case 3:
				return REQUEST_REWARD_FAIL_NOT_COMPLETED;
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
		return S_COMPLETED_ACHIEVEMENT_REWARD;
	}
}


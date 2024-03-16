package l1j.server.server.serverpackets.huntingquest;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_HuntingQuestMapSelectFail extends ServerBasePacket {
	private static final String S_HUNTING_QUEST_MAP_SELECT_FAIL	= "[S] S_HuntingQuestMapSelectFail";
	private byte[] _byte	= null;
	public static final int SELECT_FAILE	= 0x0992;
	
	public static final S_HuntingQuestMapSelectFail HUNTING_QUEST_MAP_SELECT_FAIL	= new S_HuntingQuestMapSelectFail(HuntingQuestFailResult.FAIL_INVALID_LEVEL);// 사냥터도감 등록실패
	
	public S_HuntingQuestMapSelectFail(HuntingQuestFailResult fail_reason) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SELECT_FAILE);
		writeRaw(0x08);// fail_reason
		writeRaw(fail_reason.value);
		writeH(0x00);
	}
	
	public enum HuntingQuestFailResult{
		SUCCESS(0),
		FAIL_INVALID_LEVEL(1),
		FAIL_ALREADY_MAX_QUEST(2),
		FAIL_INVALID_MAP(3),
		FAIL_ALREADY_ADD_QUEST(4),
		FAIL_ALREADY_FINISHED(5),
		FAIL_UNKNOWN_REASON(9999),
		;
		private int value;
		HuntingQuestFailResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(HuntingQuestFailResult v){
			return value == v.value;
		}
		public static HuntingQuestFailResult fromInt(int i){
			switch(i){
			case 0:
				return SUCCESS;
			case 1:
				return FAIL_INVALID_LEVEL;
			case 2:
				return FAIL_ALREADY_MAX_QUEST;
			case 3:
				return FAIL_INVALID_MAP;
			case 4:
				return FAIL_ALREADY_ADD_QUEST;
			case 5:
				return FAIL_ALREADY_FINISHED;
			case 9999:
				return FAIL_UNKNOWN_REASON;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
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

	public String getType() {
		return S_HUNTING_QUEST_MAP_SELECT_FAIL;
	}
}


package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SetToggleInfo extends ServerBasePacket {
	private static final String S_SET_TOGGLE_INFO = "[S] S_SetToggleInfo";
	private byte[] _byte = null;
	public static final int SET = 0x0a05;
	
	public static final S_SetToggleInfo ENABLE_FAITH_OF_HALPAH		= new S_SetToggleInfo(S_SetToggleInfo.eSetToggleResult.SUCCESS, eToggleInfoType.TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE, true, true);
	public static final S_SetToggleInfo DISABLE_FAITH_OF_HALPAH		= new S_SetToggleInfo(S_SetToggleInfo.eSetToggleResult.SUCCESS, eToggleInfoType.TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE, false, true);
	public static final S_SetToggleInfo NOT_FIND_FAITH_OF_HALPAH	= new S_SetToggleInfo(S_SetToggleInfo.eSetToggleResult.CAN_NOT_FIND_TOGGLE_INFO, eToggleInfoType.TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE, false, false);
	public static final S_SetToggleInfo NOT_ENOUGH_FAITH_OF_HALPAH	= new S_SetToggleInfo(S_SetToggleInfo.eSetToggleResult.CAN_NOT_SET_WHEN_FAITH_OF_HALPAH_DEBUF_ENABLE, eToggleInfoType.TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE, false, false);
	
	public S_SetToggleInfo(S_SetToggleInfo.eSetToggleResult result, eToggleInfoType toggle_info_type, boolean is_enable, boolean by_user) {
		write_init();
		write_result(result);
		write_toggle_info_type(toggle_info_type);
		write_is_enable(is_enable);
		write_by_user(by_user);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SET);
	}
	
	void write_result(S_SetToggleInfo.eSetToggleResult result) {
		writeC(0x08);
		writeC(result.value);
	}
	
	void write_toggle_info_type(eToggleInfoType toggle_info_type) {
		writeC(0x10);
		writeC(toggle_info_type.toInt());
	}
	
	void write_is_enable(boolean is_enable) {
		writeC(0x18);// is_enable
		writeB(is_enable);
	}
	
	void write_by_user(boolean by_user) {
		writeC(0x20);// by_user
		writeB(by_user);
	}
	
	public enum eSetToggleResult{
		SUCCESS(0),
		CAN_NOT_SET_TOGGLE_INFO_IS_NOT_LOAD(1),
		CAN_NOT_FIND_TOGGLE_INFO(2),
		CAN_NOT_SET_WHEN_FAITH_OF_HALPAH_DEBUF_ENABLE(3),
		;
		private int value;
		eSetToggleResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eSetToggleResult v){
			return value == v.value;
		}
		public static eSetToggleResult fromInt(int i){
			switch(i){
			case 0:
				return SUCCESS;
			case 1:
				return CAN_NOT_SET_TOGGLE_INFO_IS_NOT_LOAD;
			case 2:
				return CAN_NOT_FIND_TOGGLE_INFO;
			case 3:
				return CAN_NOT_SET_WHEN_FAITH_OF_HALPAH_DEBUF_ENABLE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eSetToggleResult, %d", i));
			}
		}
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
		return S_SET_TOGGLE_INFO;
	}
}


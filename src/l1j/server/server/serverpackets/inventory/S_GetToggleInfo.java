package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_GetToggleInfo extends ServerBasePacket {
	private static final String S_GET_TOGGLE_INFO = "[S] S_GetToggleInfo";
	private byte[] _byte = null;
	public static final int GET = 0x0a07;
	
	public S_GetToggleInfo(S_GetToggleInfo.eGetToggleResult result, eToggleInfoType toggle_info_type, boolean is_enable, boolean by_user) {
		write_init();
		write_result(result);
		write_toggle_info_type(toggle_info_type);
		write_is_enable(is_enable);
		write_by_user(by_user);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(GET);
	}
	
	void write_result(S_GetToggleInfo.eGetToggleResult result) {
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
	
	public enum eGetToggleResult{
		SUCCESS(0),
		CAN_NOT_GET_TOGGLE_INFO_IS_NOT_LOAD(1),
		CAN_NOT_FIND_TOGGLE_INFO(2),
		CAN_NOT_ENOUGH_TOGGLE_INFO(3),
		;
		private int value;
		eGetToggleResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eGetToggleResult v){
			return value == v.value;
		}
		public static eGetToggleResult fromInt(int i){
			switch(i){
			case 0:
				return SUCCESS;
			case 1:
				return CAN_NOT_GET_TOGGLE_INFO_IS_NOT_LOAD;
			case 2:
				return CAN_NOT_FIND_TOGGLE_INFO;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eGetToggleResult, %d", i));
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
		return S_GET_TOGGLE_INFO;
	}
}


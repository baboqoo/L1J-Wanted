package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_AppServerPlayTime extends ServerBasePacket {
	private static final String S_APP_SERVER_PLAY_TIME = "[S] S_AppServerPlayTime";
	private byte[] _byte = null;
	
	public S_AppServerPlayTime(int code, S_AppServerPlayTime.ResultCode resultCode, int playTime) {
		write_init(code);
		write_resultCode(resultCode);
		write_playTime(playTime);
		writeH(0x00);
	}
	
	void write_init(int code) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
	}
	
	void write_resultCode(S_AppServerPlayTime.ResultCode resultCode) {
		writeRaw(0x08);// resultCode
		writeRaw(resultCode.value);
	}
	
	void write_playTime(int playTime) {
		writeRaw(0x10);// playTime
		writeBit(playTime);
	}
	
	public enum ResultCode{
		RC_SUCCESS(0),
		RC_ERROR_PLAYTIME_IS_ZERO(1),
		RC_ERROR_NO_PAYMENT_INFO(2),
		;
		private int value;
		ResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ResultCode v){
			return value == v.value;
		}
		public static ResultCode fromInt(int i){
			switch(i){
			case 0:
				return RC_SUCCESS;
			case 1:
				return RC_ERROR_PLAYTIME_IS_ZERO;
			case 2:
				return RC_ERROR_NO_PAYMENT_INFO;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ResultCode, %d", i));
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
		return S_APP_SERVER_PLAY_TIME;
	}
}


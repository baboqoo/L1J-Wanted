package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_MoveServerAuthError extends ServerBasePacket {
	private static final String S_MOVE_SERVER_AUTH_ERROR = "[S] S_MoveServerAuthError";
	private byte[] _byte = null;
	public static final int ERROR	= 0x033F;
	
	public static final S_MoveServerAuthError INVALID_SERVER = new S_MoveServerAuthError(eErrorType.error_invalid_server);

	public S_MoveServerAuthError(eErrorType error){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ERROR);
		writeRaw(0x08);// code
		writeRaw(error.value);
        writeH(0x00);
	}
	
	public enum eErrorType{
		error_fail(1),
		error_full(2),
		error_notopen(3),
		error_not_register(4),
		error_already_reserve(5),
		error_wait(6),
		error_logout(7),
		error_complete(8),
		error_not_connected(9),
		error_system_error(10),
		error_not_find(11),
		error_not_ingame(12),
		error_invalid_server(13),
		error_invalid_state(14),
		error_not_security(15),
		;
		private int value;
		eErrorType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eErrorType v){
			return value == v.value;
		}
		public static eErrorType fromInt(int i){
			switch(i){
			case 1:
				return error_fail;
			case 2:
				return error_full;
			case 3:
				return error_notopen;
			case 4:
				return error_not_register;
			case 5:
				return error_already_reserve;
			case 6:
				return error_wait;
			case 7:
				return error_logout;
			case 8:
				return error_complete;
			case 9:
				return error_not_connected;
			case 10:
				return error_system_error;
			case 11:
				return error_not_find;
			case 12:
				return error_not_ingame;
			case 13:
				return error_invalid_server;
			case 14:
				return error_invalid_state;
			case 15:
				return error_not_security;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eErrorType, %d", i));
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
		return S_MOVE_SERVER_AUTH_ERROR;
	}
}


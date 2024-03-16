package l1j.server.server.serverpackets.command;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BuilderCommand extends ServerBasePacket {
	private static final String S_BUILDER_COMMAND = "[S] S_BuilderCommand";
	private byte[] _byte = null;
	
	public S_BuilderCommand(int code, String resultMessage){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		
		writeC(0x08);
		writeC(CommandResultCode.SUCCESS.value);
		
		writeC(0x12);
		writeBytesWithLength(resultMessage.getBytes());
		
        writeH(0x00);
	}
	
	public enum CommandResultCode{
		SUCCESS(0),
		FAIL(1),
		INVALID_USER(2),
		;
		private int value;
		CommandResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(CommandResultCode v){
			return value == v.value;
		}
		public static CommandResultCode fromInt(int i){
			switch(i){
			case 0:
				return SUCCESS;
			case 1:
				return FAIL;
			case 2:
				return INVALID_USER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ResultCode, %d", i));
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
		return S_BUILDER_COMMAND;
	}
}


package l1j.server.server.serverpackets.command;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BuilderMessage extends ServerBasePacket {
	private static final String S_BUILDER_MESSAGE = "[S] S_BuilderMessage";
	private byte[] _byte = null;
	public static final int MESSAGE = 0x0328;
	
	public S_BuilderMessage(int code, eTarget target, String targetMessage){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MESSAGE);
		
		writeC(0x08);
		writeC(target.value);
		
		writeC(0x12);
		writeBytesWithLength(targetMessage.getBytes());
		
        writeH(0x00);
	}
	
	public enum eTarget{
		PREFARE_CHAR(0),
		ENTRANCE_USER(1),
		;
		private int value;
		eTarget(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eTarget v){
			return value == v.value;
		}
		public static eTarget fromInt(int i){
			switch(i){
			case 0:
				return PREFARE_CHAR;
			case 1:
				return ENTRANCE_USER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eTarget, %d", i));
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
		return S_BUILDER_MESSAGE;
	}
}


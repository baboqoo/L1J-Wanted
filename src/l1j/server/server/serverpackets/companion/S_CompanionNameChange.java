package l1j.server.server.serverpackets.companion;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_CompanionNameChange extends ServerBasePacket {
	private static final String S_COMPANION_NAME_CHANGE = "[S] S_CompanionNameChange";
	private byte[] _byte = null;
	public static final int NAME = 0x07d2;

	public S_CompanionNameChange(S_CompanionNameChange.eResult result, byte[] new_name) {
		write_init();
	    write_result(result);
	    write_new_name(new_name);
	    writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
	    writeH(NAME);
	}
	
	void write_result(S_CompanionNameChange.eResult result) {
		writeRaw(0x08);
		writeRaw(result.value);
	}
	
	void write_new_name(byte[] new_name) {
		writeRaw(0x12);
	    writeBytesWithLength(new_name);
	}
	
	public enum eResult{
		Success(0),
		SameNameExisted(1),
		BadName(2),
		SystemError(3),
		;
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 0:
				return Success;
			case 1:
				return SameNameExisted;
			case 2:
				return BadName;
			case 3:
				return SystemError;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
			}
		}
	}

	public byte[] getContent() {
	    if (_byte == null) {
	    	_byte = getBytes();
	    }
	    return _byte;
	}
  
    @Override
	public String getType() {
		return S_COMPANION_NAME_CHANGE;
    }
}

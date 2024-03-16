package l1j.server.server.serverpackets.smelting;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SynthesisSmeltingStart extends ServerBasePacket {
	private static final String S_SYNTHESIS_SMELTING_START = "[S] S_SynthesisSmeltingStart";
	private byte[] _byte = null;
    public static final int START	= 0x09c4;
    
    public static final S_SynthesisSmeltingStart SUCCESS		= new S_SynthesisSmeltingStart(S_SynthesisSmeltingStart.eResult.SUCCESS_START);
    public static final S_SynthesisSmeltingStart FAIL_LOC		= new S_SynthesisSmeltingStart(S_SynthesisSmeltingStart.eResult.FAIL_CANT_START_HERE);
    public static final S_SynthesisSmeltingStart FAIL_CONFIG	= new S_SynthesisSmeltingStart(S_SynthesisSmeltingStart.eResult.FAIL_CANT_LOAD_CONFIG);
    public static final S_SynthesisSmeltingStart FAIL_USER		= new S_SynthesisSmeltingStart(S_SynthesisSmeltingStart.eResult.FAIL_CANT_FIND_USER);
    
    private S_SynthesisSmeltingStart(S_SynthesisSmeltingStart.eResult result){
        writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(START);
        writeRaw(0x08);
        writeRaw(result.value);
        writeH(0x00);
    }
    
    public enum eResult{
    	SUCCESS_START(0),
		FAIL_CANT_START_HERE(1),
		FAIL_CANT_LOAD_CONFIG(2),
		FAIL_CANT_FIND_USER(3),
		INVALID_TELEPORT_WORLD(4),
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
				return SUCCESS_START;
			case 1:
				return FAIL_CANT_START_HERE;
			case 2:
				return FAIL_CANT_LOAD_CONFIG;
			case 3:
				return FAIL_CANT_FIND_USER;
			case 4:
				return INVALID_TELEPORT_WORLD;
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

	@Override
	public String getType() {
		return S_SYNTHESIS_SMELTING_START;
	}
}

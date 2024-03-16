package l1j.server.server.serverpackets.alchemy;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PetballContentStart extends ServerBasePacket {
	private static final String S_PETBALL_CONTENT_START = "[S] S_PetballContentStart";
	private byte[] _byte = null;
    public static final int START	= 0x094f;
    
    public static final S_PetballContentStart SUCCESS		= new S_PetballContentStart(S_PetballContentStart.eResult.SUCCESS_START);
    public static final S_PetballContentStart FAIL_LOC		= new S_PetballContentStart(S_PetballContentStart.eResult.FAIL_CANT_START_HERE);
    public static final S_PetballContentStart FAIL_CONFIG	= new S_PetballContentStart(S_PetballContentStart.eResult.FAIL_CANT_LOAD_CONFIG);
    public static final S_PetballContentStart FAIL_USER		= new S_PetballContentStart(S_PetballContentStart.eResult.FAIL_CANT_FIND_USER);
    
    private S_PetballContentStart(S_PetballContentStart.eResult result){
        writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(START);
        writeRaw(0x08);
        writeRaw(result.value);
        writeH(0x00);
    }
    
    private enum eResult{
		SUCCESS_START(0),
		FAIL_CANT_START_HERE(1),
		FAIL_CANT_LOAD_CONFIG(2),
		FAIL_CANT_FIND_USER(3),
		;
		private int value;
		eResult(int val){
			value = val;
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
		return S_PETBALL_CONTENT_START;
	}
}

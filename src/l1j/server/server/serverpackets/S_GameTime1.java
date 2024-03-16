package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_GameTime1 extends ServerBasePacket {
	private static final String S_GAME_TIME_1 = "[S] S_GameTime1";
	private byte[] _byte = null;

	public S_GameTime1(L1PcInstance pc){
		buildPacket(pc);
	}

	//0000 : 7e 41 14 fa 7f 50 80 f9                            ~A..P..

    /*00:00 시간 흘러가는 패킷*/
	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
        writeC(0x41);
        writeC(0); 
        writeC(0); 
        writeC(0); 
        writeC(0); 
        writeC(0); 
        writeC(0); 

 /*  writeC(0x8e); 
    writeC(0xe9); 
    writeC(0x30); 
    writeC(0xb2); 
    writeC(0x60); 
    writeC(0xd4); */
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GAME_TIME_1;
	}
}


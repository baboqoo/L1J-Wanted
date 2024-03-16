package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_GameStart extends ServerBasePacket {
	private static final String S_GAME_START = "[S] S_GameStart";

	private byte[] _byte = null;

	public S_GameStart(L1PcInstance pc){
		buildPacket1(pc);
	}

	//0000 : 7e 40 05 3c 0a ac b7 b6                            ~@.<....

// 40 = 시작, 41 = 시간, 42 = 리스트, 43 = 바퀴수, 44 = 랭킹, 45 = 오버, 45 = 엔드
    /*스타트*/
	private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(0x40);
        writeC(0x05); // 속도
        writeC(0); 
        writeC(0); 
        writeC(0); 
        writeC(0); 
        writeC(0); 

   /*     writeC(0x81); 
        writeC(0xfc); 
        writeC(0x7d); 
        writeC(0x6e); 
        writeC(0x11); */


	 }

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GAME_START;
	}
}


package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_GameOver extends ServerBasePacket {
	private static final String S_GAME_OVER = "[S] S_GameOver";
	private byte[] _byte = null;

	public S_GameOver(L1PcInstance pc){
		buildPacket1(pc);
	}

	//0000 : 7e 45 0a d0 bb 83 09 35                            ~E.....5
    private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(0x45);
        writeC(10);
        writeC(109);
        writeC(85);
        writeC(208);
        writeC(2);
        writeC(220);
    }

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GAME_OVER;
	}
}


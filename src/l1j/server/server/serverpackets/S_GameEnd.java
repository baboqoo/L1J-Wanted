package l1j.server.server.serverpackets;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.Opcodes;

public class S_GameEnd extends ServerBasePacket {
	private static final String S_GameEnd = "[S] S_GameEnd";
	private byte[] _byte = null;

	public S_GameEnd(L1PcInstance pc){
		buildPacket1(pc);
	}

    private void buildPacket1(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(0x46);
        writeC(147);
        writeC(92);
        writeC(151);
        writeC(220);
        writeC(42);
        writeC(74);
    }

	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = getBytes();
		return _byte;
	}

	public String getType() {
		return S_GameEnd;
	}
}


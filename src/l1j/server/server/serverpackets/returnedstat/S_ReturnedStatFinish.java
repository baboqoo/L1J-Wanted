package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ReturnedStatFinish extends ServerBasePacket {
	private static final String S_RETURNED_STAT_FINISH = "[S] S_ReturnedStatFinish";
	private byte[] _byte = null;
	public static final int FINISH	= 0x03;
	
	public S_ReturnedStatFinish(L1PcInstance pc, int value) {
        writeC(Opcodes.S_VOICE_CHAT);
        writeRaw(FINISH);
        writeRaw(value);
    	if (pc.getLevel() > pc.getHighLevel()) {
			pc.getNetConnection().kick();
			pc.getNetConnection().close();
		}
    }
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_RETURNED_STAT_FINISH;
	}
}

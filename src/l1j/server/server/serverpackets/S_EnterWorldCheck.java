package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_EnterWorldCheck extends ServerBasePacket {
	private static final String S_ENTER_WORLD_CHECK = "[S] S_EnterWorldCheck";
	private byte[] _byte = null;
	public static final S_EnterWorldCheck ENTER_WORLD = new S_EnterWorldCheck();
	
	public S_EnterWorldCheck() {
		writeC(Opcodes.S_ENTER_WORLD_CHECK);
		writeC(0x03);
	}
	
	public S_EnterWorldCheck(L1PcInstance pc) {
		writeC(Opcodes.S_ENTER_WORLD_CHECK);
		writeC(0x03);
		if (pc.getClanid() > 0) {
			writeD(pc.getId());
		} else {
			writeC(0x53);
			writeC(0x01);
			writeC(0x00);
			writeC(0x8b);
		}
		//writeC(0x9c);
		//writeC(0x1f);
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
		return S_ENTER_WORLD_CHECK;
	}
}


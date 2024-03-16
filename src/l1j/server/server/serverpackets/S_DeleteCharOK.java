package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_DeleteCharOK extends ServerBasePacket {
	private static final String S_DELETE_CHAR_OK = "[S] S_DeleteCharOK";
	public static final int DELETE_CHAR_NOW			= 0x05;
	public static final int DELETE_CHAR_AFTER_7DAYS	= 0x51;

	public S_DeleteCharOK(int type, L1PcInstance pc) {
		writeC(Opcodes.S_DELETE_CHARACTER_CHECK);
		writeC(type);
		if (type == DELETE_CHAR_AFTER_7DAYS) {
			if (pc.getDeleteTime() != null && (pc.getDeleteTime().getTime() - System.currentTimeMillis()) > 0) {
				long time = (pc.getDeleteTime().getTime() / 1000) - (System.currentTimeMillis() / 1000);
				writeD((int) time);
			} else {
				writeD(0x00);
			}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_DELETE_CHAR_OK;
	}
}


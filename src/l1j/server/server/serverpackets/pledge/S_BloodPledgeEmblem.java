package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeEmblem extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_EMBLEM = "[S] S_BloodPledgeEmblem";
	private byte[] _byte = null;
	public static final int PLEDGE_EMBLEM	= 0x3c;

	public S_BloodPledgeEmblem(int pcObjId, int emblemId) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(PLEDGE_EMBLEM);
		writeD(pcObjId);
		writeD(emblemId);
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_BLOODPLEDGE_EMBLEM;
	}
}

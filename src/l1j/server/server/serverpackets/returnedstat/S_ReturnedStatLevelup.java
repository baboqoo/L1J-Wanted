package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1StatReset;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ReturnedStatLevelup extends ServerBasePacket {
	private static final String S_RETURNED_STAT_LEVELUP = "[S] S_ReturnedStatLevelup";
	private byte[] _byte = null;
	public static final int LEVELUP	= 0x02;
	
	public S_ReturnedStatLevelup(L1StatReset sr) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeRaw(LEVELUP);
		writeRaw(sr.getNowLevel());
		writeRaw(sr.getEndLevel());
		writeH(sr.getMaxHp());
		writeH(sr.getMaxMp());
		writeH(sr.getAC());
		writeRaw(sr.getStr());
		writeRaw(sr.getIntel());
		writeRaw(sr.getWis());
		writeRaw(sr.getDex());
		writeRaw(sr.getCon());
		writeRaw(sr.getCha());
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_RETURNED_STAT_LEVELUP;
	}
}

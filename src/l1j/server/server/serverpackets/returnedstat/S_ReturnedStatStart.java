package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.CalcStat;

public class S_ReturnedStatStart extends ServerBasePacket {
	private static final String S_RETURNED_STAT_START = "[S] S_ReturnedStatStart";
	private byte[] _byte = null;
	public static final int START	= 0x01;
	
	public S_ReturnedStatStart(L1PcInstance pc) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeRaw(START);
		writeH(CalcStat.initHp(pc));
		writeH(CalcStat.initMp(pc));
		writeRaw(10);
		writeRaw(ExpTable.getLevelByExp(pc.getReturnStat_exp()));
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_RETURNED_STAT_START;
	}
}

package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ReturnedStatLogin extends ServerBasePacket {
	private static final String S_RETURNED_STAT_LOGIN = "[S] S_ReturnedStatLogin";
	private byte[] _byte = null;
	public static final int LOGIN	= 0x04;
	
	public S_ReturnedStatLogin(L1PcInstance pc) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeRaw(LOGIN);
		/*
		 * pc.getAblilyty에서 반환되는 최소 스탯값 배열 순서 
		 * 0:힘/1:덱/2:콘/3:위즈/4:카리/5:인트
		 */
		int[] minStat	= pc.getAbility().getMinStat(pc.getClassId());
		int first		= minStat[0] + minStat[5] * 16;
		int second		= minStat[3] + minStat[1] * 16;
		int third		= minStat[2] + minStat[4] * 16;
		writeRaw(first);  //int,str
		writeRaw(second);  //dex,wis
		writeRaw(third);  //cha,con
		writeRaw(0x00);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_RETURNED_STAT_LOGIN;
	}
}

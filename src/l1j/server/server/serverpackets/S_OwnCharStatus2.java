package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_OwnCharStatus2 extends ServerBasePacket {

	public S_OwnCharStatus2(L1PcInstance pc) {
		if(pc == null)return;
		writeC(Opcodes.S_ABILITY_SCORES);
		writeH(pc.getAbility().getTotalStr());
		writeH(pc.getAbility().getTotalInt());
		writeH(pc.getAbility().getTotalWis());
		writeH(pc.getAbility().getTotalDex());
		writeH(pc.getAbility().getTotalCon());
		writeH(pc.getAbility().getTotalCha());
		writeC(pc.getInventory().getWeightPercent());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S_OwnChraStatus2;
	}

	private static final String _S_OwnChraStatus2 = "[C] S_OwnCharStatus2";
}


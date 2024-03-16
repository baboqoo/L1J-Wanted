package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Strup extends ServerBasePacket {

	public S_Strup(L1PcInstance pc, int value, int time) {
		writeC(Opcodes.S_MAGE_STRENGTH);
		writeH(time);
		writeC(pc.getAbility().getTotalStr());
		writeC(0x00);
		writeC(pc.getInventory().getWeightPercent());
		writeC(value);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_STR_UP;
	}

	private static final String S_STR_UP = "[S] S_Strup";
}


package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Dexup extends ServerBasePacket {

	public S_Dexup(L1PcInstance pc, int value, int time) {
		writeC(Opcodes.S_MAGE_DEXTERITY);
		writeH(time);
		writeC(pc.getAbility().getTotalDex());
		writeC(0x00);
		writeC(value);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	@Override
	public String getType() {
		return S_DEX_UP;
	}

	private static final String S_DEX_UP = "[S] S_Dexup";
}


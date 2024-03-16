package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_CharVisualUpdate extends ServerBasePacket {
	private static final String S_CHAR_VISUAL_UPDATE = "[C] S_CharVisualUpdate";

	public S_CharVisualUpdate(L1PcInstance pc) {
		writeC(Opcodes.S_WIELD);
		writeD(pc.getId());
		writeC(pc.getCurrentWeapon());
		writeC(0xff);
		writeC(0xff);
		writeH(0x00);
	}

	public S_CharVisualUpdate(L1Character cha, int status) {
		writeC(Opcodes.S_WIELD);
		writeD(cha.getId());
		writeC(status);
		writeC(0xff);
		writeC(0xff);
		writeH(0x00);
	}

	public S_CharVisualUpdate(L1NpcInstance cha) {
		writeC(Opcodes.S_WIELD);
		writeD(cha.getId());
		writeC(cha.getActionStatus());
		writeC(0xff);
		writeC(0xff);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_CHAR_VISUAL_UPDATE;
	}
}


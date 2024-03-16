package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharacterConfigTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_CharacterConfig extends ClientBasePacket {

	private static final String C_CHARACTER_CONFIG = "[C] C_CharacterConfig";

	public C_CharacterConfig(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		if (client == null || !Config.SERVER.CHARACTER_CONFIG_IN_SERVER_SIDE) {
			return;
		}
		L1PcInstance pc	= client.getActiveChar();
		if (pc == null) {
			return;
		}
		int length	= readD();
		byte data[]	= readByte(length);
		CharacterConfigTable.getInstance().upsertCharacterConfig(pc.getId(), length, data);
	}

	@Override
	public String getType() {
		return C_CHARACTER_CONFIG;
	}
}


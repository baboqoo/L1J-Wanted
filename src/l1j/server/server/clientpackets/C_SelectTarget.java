package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;

public class C_SelectTarget extends ClientBasePacket {

	private static final String C_SELECT_TARGET = "[C] C_SelectTarget";

	public C_SelectTarget(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
	}

	@Override
	public String getType() {
		return C_SELECT_TARGET;
	}
}


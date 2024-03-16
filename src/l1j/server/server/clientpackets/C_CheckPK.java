package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class C_CheckPK extends ClientBasePacket {
	private static final String C_CHECK_PK = "[C] C_CheckPK";

	public C_CheckPK(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		pc.sendPackets(new S_ServerMessage(562, String.valueOf(pc.getPKcount())), true);// 현재의 PK회수는%0입니다.
	}

	@Override
	public String getType() {
		return C_CHECK_PK;
	}

}


package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BoardInstance;

public class C_BoardBack extends ClientBasePacket {

	private static final String C_BOARD_BACK = "[C] C_BoardBack";

	public C_BoardBack(byte abyte0[], GameClient client) {
		super(abyte0);
		int objId = readD();
		int topicNumber = readD();
		L1Object obj = L1World.getInstance().findObject(objId);
		if ((obj == null) || (client.getActiveChar() == null)) {
			return;
		}
		L1BoardInstance board = (L1BoardInstance) obj;
		board.onAction(client.getActiveChar(), topicNumber);
	}

	@Override
	public String getType() {
		return C_BOARD_BACK;
	}

}


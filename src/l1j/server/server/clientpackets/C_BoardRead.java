package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BoardInstance;

public class C_BoardRead extends ClientBasePacket {

	private static final String C_BOARD_READ = "[C] C_BoardRead";

	public C_BoardRead(byte decrypt[], GameClient client) {
		super(decrypt);
		int objId		= readD();
		int topicNumber	= readD();
		L1Object obj = L1World.getInstance().findObject(objId);
		L1BoardInstance board = (L1BoardInstance) obj;
		if (board == null) {
			return;
		}
		board.onActionRead(client.getActiveChar(), topicNumber);
	}

	@Override
	public String getType() {
		return C_BOARD_READ;
	}

}


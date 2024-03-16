package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AuctionBoardInstance;
import l1j.server.server.model.Instance.L1BoardInstance;

public class C_Board extends ClientBasePacket {

	private static final String C_BOARD = "[C] C_Board";

	private boolean isBoardInstance(L1Object obj) {
		return (obj instanceof L1BoardInstance || obj instanceof L1AuctionBoardInstance);
	}

	public C_Board(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		int objectId = readD();
		L1Object obj = L1World.getInstance().findObject(objectId);
		if (!isBoardInstance(obj)) {
			return;
		}
		obj.onAction(clientthread.getActiveChar());
	}

	@Override
	public String getType() {
		return C_BOARD;
	}

}


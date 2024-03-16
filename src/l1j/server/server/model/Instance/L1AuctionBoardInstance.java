package l1j.server.server.model.Instance;

import l1j.server.server.serverpackets.S_AuctionBoard;
import l1j.server.server.templates.L1Npc;

public class L1AuctionBoardInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1AuctionBoardInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		pc.sendPackets(new S_AuctionBoard(this, pc), true);
	}

}


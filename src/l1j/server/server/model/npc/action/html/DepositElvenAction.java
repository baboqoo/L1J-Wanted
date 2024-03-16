package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.warehouse.S_RetrieveDeposit;

public class DepositElvenAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new DepositElvenAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private DepositElvenAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (pc.isElf()) {
			pc.sendPackets(new S_RetrieveDeposit(2, pc), true);
		}
		return null;
	}
}


package l1j.server.server.model.npc.action.html;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.warehouse.S_RetrieveDeposit;

public class DepositPledgeAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new DepositPledgeAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private DepositPledgeAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1Clan pledge = pc.getClan();
		if (pledge == null) {
			return null;
		}
		if (pc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING && !pledge.is_store_allow(pc.getName())) {
			pc.sendPackets(L1ServerMessage.sm728);
			return null;
		}
		pc.sendPackets(new S_RetrieveDeposit(3, pc), true);
		return null;
	}
}


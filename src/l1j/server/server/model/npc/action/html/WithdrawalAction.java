package l1j.server.server.model.npc.action.html;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.S_Drawal;
import l1j.server.server.templates.L1Castle;

public class WithdrawalAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new WithdrawalAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private WithdrawalAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (pc.getId() != pc.getClan().getLeaderId() || pc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING || !pc.isCrown()) {
			return null;
		}
		L1Clan clan = pc.getClan();
		if (clan != null) {
			int castle_id = clan.getCastleId();
			if (castle_id != 0) {
				L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
				if (l1castle.getPublicMoney() <= 0) {
					return null;
				}
				pc.sendPackets(new S_Drawal(pc.getId(), l1castle.getPublicMoney()), true);
			}
		}
		return null;
	}
}


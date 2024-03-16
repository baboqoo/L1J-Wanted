package l1j.server.server.model.npc.action.html;

import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.S_TaxRate;
import l1j.server.server.templates.L1Castle;

public class TaxAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new TaxAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private TaxAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1Clan clan = pc.getClan();
		if (clan == null || clan.getCastleId() == 0) {
			return null;
		}
		
		L1Castle castle = CastleTable.getInstance().getCastleTable(clan.getCastleId());
		if (castle == null) {
			return null;
		}
		pc.sendPackets(new S_TaxRate(pc.getId(), castle.getTaxRate()), true);
		return null;
	}
}


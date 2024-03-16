package l1j.server.server.model.npc.action.html;

import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.utils.StringUtil;

public class InexAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new InexAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private InexAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1Clan clan = pc.getClan();
		if (clan != null) {
			int castle_id = clan.getCastleId();
			if (castle_id != 0) {
				L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
				pc.sendPackets(new S_ServerMessage(309, l1castle.getName(), String.valueOf(l1castle.getPublicMoney())), true);
				return StringUtil.EmptyString;
			}
		}
		return null;
	}
}


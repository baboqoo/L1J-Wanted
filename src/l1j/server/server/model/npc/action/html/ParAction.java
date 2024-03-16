package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.utils.StringUtil;

public class ParAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new ParAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ParAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return ultimateEnter(pc, npcId);
	}
	
	private String ultimateEnter(L1PcInstance pc, int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		if (ub == null || pc == null) {
			return StringUtil.EmptyString;
		}
		if (!ub.isActive() || !ub.canPcEnter(pc)) {
			return "colos2";
		}
		if (ub.isNowUb()) {
			return "colos1";
		}
		if (ub.getMembersCount() >= ub.getMaxPlayer()) {
			return "colos4";
		}
		if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 10000)) {
			pc.sendPackets(L1ServerMessage.sm189);
			return StringUtil.EmptyString;
		}
		ub.addMember(pc);
		L1Location loc = ub.getLocation().randomLocation(10, false);
		pc.getTeleport().start(loc.getX(), loc.getY(), ub.getMapId(), 5, true);
		return StringUtil.EmptyString;
	}
}


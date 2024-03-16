package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class FkInAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new FkInAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private FkInAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return fishEnter(pc, s);
	}
	
	private String fishEnter(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("a")) {
			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
				pc.sendPackets(L1ServerMessage.sm189); // \f1%0이 부족합니다.
				return null;
			}
			pc.removeShapeChange();
			L1PolyMorph.undoPoly(pc);
			pc.getTeleport().start(32765, 32832, (short) 5490, pc.getMoveState().getHeading(), true);
			return StringUtil.EmptyString;
		}
		return null;
	}
}


package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;

public class MarryRingAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MarryRingAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MarryRingAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		marryRingCharge(pc, s);
		return null;
	}
	
	private void marryRingCharge(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("chg")) {
			L1ItemInstance ring = null;
			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
				pc.sendPackets(L1ServerMessage.sm189);
				return;
			}
			if (pc.getPartnerId() != 0) {
				ring = pc.getInventory().findItemId(40906);
				if (ring == null) {
					ring = pc.getInventory().findItemId(40905);
				}
				if (ring == null) {
					ring = pc.getInventory().findItemId(40904);
				}
				if (ring == null) {
					ring = pc.getInventory().findItemId(40903);
				}
				if (ring != null) {
					ring.setChargeCount(ring.getItem().getMaxChargeCount());
					pc.getInventory().updateItem(ring, L1PcInventory.COL_CHARGE_COUNT);
					pc.sendPackets(L1SystemMessage.MARRY_RING_CHARGE_COMPLETE);
				}
			}
			pc.sendPackets(L1SystemMessage.MARRY_RING_CHARGE_MSG);
		}
	}

}


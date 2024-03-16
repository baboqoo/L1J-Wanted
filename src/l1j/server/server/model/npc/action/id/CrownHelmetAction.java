package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class CrownHelmetAction implements L1NpcIdAction {// 기마용 투구 충전
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new CrownHelmetAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CrownHelmetAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return crownHelmetCharge(pc, s);
	}
	
	private String crownHelmetCharge(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("c") && pc.isCrown()) {
			L1ItemInstance item = pc.getInventory().findItemId(20383);// 기마용 투구
			if (item == null || item.getChargeCount() >= 50) {
				return null;
			}
			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 100000)) {
				pc.sendPackets(L1ServerMessage.sm189); // 아데나가 부족합니다.
				return null;
			}
			item.setChargeCount(50);
			pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
			return StringUtil.EmptyString;
		}
		return null;
	}
}


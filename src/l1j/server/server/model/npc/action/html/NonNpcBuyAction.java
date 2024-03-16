package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.NotificationTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.shop.S_PremiumShopSellList;
import l1j.server.server.templates.L1Notification;

public class NonNpcBuyAction implements L1NpcHtmlAction {

	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new NonNpcBuyAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private NonNpcBuyAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1Notification noti = NotificationTable.getNotification(25);
		if (npcId == noti.getEventNpcList().getFirst().getNpcId()) {
			if (pc.getClanid() == 0) {
				return "incence";
			} else {
				if(pc.getInventory().getWeightPercent() >= 99){// 중량 오버
					pc.sendPackets(L1ServerMessage.sm3561);
					return null;
				}
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (isNpcSellOnly(npc)) {
					return null;
				}
				pc.sendPackets(new S_PremiumShopSellList(obj.getId(), pc, ShopTable.getInstance().getShopInfo(npcId)), true);
			}
		}
		return null;
	}
	
	//private static final String ADEN_BUYER = "에덴상단"; // not necesary becouse actually there is NO npc with that string in the description or whatever
	private boolean isNpcSellOnly(L1NpcInstance npc) {
		int npcId = npc.getNpcTemplate().getNpcId();
		if (npcId == 70027 /*|| ADEN_BUYER.equals(npc.getNpcTemplate().getDescKr())*/) {
			return true;
		}
		return false;
	}

}


package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.ShopTable.L1ShopInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.shop.S_PremiumShopSellList;
import l1j.server.server.serverpackets.shop.S_ShopSellList;

public class BuyAction implements L1NpcHtmlAction {
	
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new BuyAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BuyAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (pc.getInventory().getWeightPercent() >= 99) {// 중량 오버
			pc.sendPackets(L1ServerMessage.sm3561);
			return null;
		}
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (isNpcSellOnly(npc)) {
			return null;
		}
		L1ShopInfo info = ShopTable.getInstance().getShopInfo(npcId);
		if (info != null) {
			pc.sendPackets(new S_PremiumShopSellList(obj.getId(), pc, info), true);// 특화 상점
			return null;
		}
		if (npcId >= 6100000 && npcId <= 6100013 || npcId >= 6100042 && npcId <= 6100045 || npcId >= 6100014 && npcId <= 6100027 || npcId >= 6100028 && npcId <= 6100041) {// 패키지
			pc.sendPackets(new S_PremiumShopSellList(obj.getId(), pc, info), true);// 특화 상점
			return null;
		}
		pc.sendPackets(new S_ShopSellList(obj.getId(), pc), true);// 일반 상점
		return null;
	}
	
	//private static final String ADEN_BUYER = "에덴상단"; // not necesary becouse actually there is NO npc with that string in the description or whatever
	private boolean isNpcSellOnly(L1NpcInstance npc) {
		if (npc.getNpcTemplate().getNpcId() == 70027 /*|| ADEN_BUYER.equals(npc.getNpcTemplate().getDescKr())*/) {
			return true;
		}
		return false;
	}
}


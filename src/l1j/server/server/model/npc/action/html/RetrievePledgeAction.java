package l1j.server.server.model.npc.action.html;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;

public class RetrievePledgeAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new RetrievePledgeAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private RetrievePledgeAction(){}
	
	private static final String NONE_VALUE_ACTION = "noitemret";

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid	=	null;
		if (pc.getLevel() >= 5) {
			if (pc.isTwoLogin()) {
				return null;
			}
			L1Clan pledge = pc.getClan();
			if (pledge == null) {
				pc.sendPackets(L1ServerMessage.sm208);// 창고: 혈맹 창고 이용 불가(혈맹 미가입)
				return null;
			}
			if (pc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING && !pledge.is_store_allow(pc.getName())) {
				pc.sendPackets(L1ServerMessage.sm728);
				return null;
			}
			L1NpcInstance npc = (L1NpcInstance) obj;
			S_WareHouseItemListNoti rpl = new S_WareHouseItemListNoti(S_WareHouseItemListNoti.eWarehouseType.TRADE_RETRIEVE_PLEDGE, npc, pc);
			if (rpl.is_pledge_using) {
				return null;
			}
			if (rpl.is_non_value) {
				htmlid = NONE_VALUE_ACTION;
			} else {
				pc.sendPackets(rpl);
			}
			rpl.clear();
			rpl = null;
		}
		return htmlid;
	}
}


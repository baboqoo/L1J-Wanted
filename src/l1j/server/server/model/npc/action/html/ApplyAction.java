package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.S_ApplyAuction;
import l1j.server.server.utils.StringUtil;

public class ApplyAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new ApplyAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ApplyAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		L1Clan clan = pc.getClan();
		if (clan != null) {
			if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
				if (pc.getLevel() >= 15) {
					if (clan.getHouseId() == 0) {							
						pc.sendPackets(new S_ApplyAuction(obj.getId(), s2), true);
					} else {
						pc.sendPackets(L1ServerMessage.sm521);
						htmlid = StringUtil.EmptyString;
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm519);
					htmlid = StringUtil.EmptyString;
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm518);
				htmlid = StringUtil.EmptyString;
			}
		} else {
			pc.sendPackets(L1ServerMessage.sm518);
			htmlid = StringUtil.EmptyString;
		}
		return htmlid;
	}
}


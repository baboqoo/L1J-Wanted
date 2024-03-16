package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Town;
import l1j.server.server.utils.StringUtil;

public class SetAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new SetAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private SetAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (obj instanceof L1NpcInstance) {
			L1Town town = TownTable.getTownFromNpcId(npcId);
			if (town != null && town.get_townid() >= L1TownLocation.TOWNID_TALKING_ISLAND && town.get_townid() <= L1TownLocation.TOWNID_OREN) {
				if (pc.getHomeTownId() == -1) {
					pc.sendPackets(L1ServerMessage.sm759);
				} else if (pc.getHomeTownId() > 0) {
					if (pc.getHomeTownId() != town.get_townid()) {
						pc.sendPackets(new S_ServerMessage(758, town.get_name()), true);
					}
				} else if (pc.getHomeTownId() == 0) {
					if (pc.getLevel() < 10) {
						pc.sendPackets(L1ServerMessage.sm757);
					} else {
						int level = pc.getLevel();
						int cost = level * level * 10;
						if (pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
							pc.setHomeTownId(town.get_townid());
							pc.setContribution(0);
							try{
								pc.save();
							}catch(Exception e){}
						} else {
							pc.sendPackets(L1ServerMessage.sm189);
						}
					}
				}
				htmlid = StringUtil.EmptyString;
			}
		}
		return htmlid;
	}
}


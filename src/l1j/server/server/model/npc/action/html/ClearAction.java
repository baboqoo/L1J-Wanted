package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.templates.L1Town;
import l1j.server.server.utils.StringUtil;

public class ClearAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new ClearAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ClearAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (obj instanceof L1NpcInstance) {
			L1Town town = TownTable.getTownFromNpcId(npcId);
			if (town == null) {
				return null;
			}
			if (town.get_townid() > 0) {
				if (pc.getHomeTownId() > 0) {
					if (pc.getHomeTownId() == town.get_townid()) {
						pc.setHomeTownId(-1);
						pc.setContribution(0);
					} else {
						pc.sendPackets(L1ServerMessage.sm756);
					}
				}
				return StringUtil.EmptyString;
			}
		}
		return null;
	}
}


package l1j.server.server.model.npc.action.html;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class GetAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new GetAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private GetAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (npcId == 70099 || npcId == 70796) {
			L1ItemInstance item = pc.getInventory().storeItem(20081, 1);
			pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
			pc.getQuest().setEnd(L1Quest.QUEST_OILSKINMANT);
			return StringUtil.EmptyString;
		}
		return null;
	}
}


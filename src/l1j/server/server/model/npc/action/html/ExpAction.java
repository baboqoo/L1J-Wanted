package l1j.server.server.model.npc.action.html;

//import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
//import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.model.npc.L1NpcHtmlAction;

public class ExpAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new ExpAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ExpAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		pc.sendPackets(L1SystemMessage.EXP_REFAIR_ANOTHER_WAY);
		return null;
/*		String htmlid = null;
		if (pc.getExpRes() == 1) {
			int level = pc.getLevel();
			int lawful = pc.getLawful();
			int cost = level < 45 ? level * level * 50 : level * level * 150;
			if(lawful >= 0)cost = (int) (cost * 0.7);
			pc.sendPackets(new S_Message_YN(738, String.valueOf(cost)), true);
		} else {
			pc.sendPackets(L1ServerMessage.sm739);
			htmlid = StringUtil.EmptyString;
		}
		return htmlid;*/
	}
}


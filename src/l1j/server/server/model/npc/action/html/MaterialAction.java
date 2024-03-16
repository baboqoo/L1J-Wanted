package l1j.server.server.model.npc.action.html;

//import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
//import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.model.npc.L1NpcHtmlAction;

public class MaterialAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new MaterialAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MaterialAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		pc.sendPackets(L1SystemMessage.EXP_REFAIR_ANOTHER_WAY);
		return null;
		/*String htmlid = null;
		if (pc.getInventory().checkItem(3000049, 1)) {
			if (pc.getExpRes() == 1) {
				pc.sendPackets(S_Message_YN.EXP_REFAIR_PAPER_YN);
			} else {
				pc.sendPackets(L1ServerMessage.sm739);
				htmlid = StringUtil.EmptyString;
			}
		} else
			pc.sendPackets(L1ServerMessage.sm3567);
		return htmlid;*/
	}
}


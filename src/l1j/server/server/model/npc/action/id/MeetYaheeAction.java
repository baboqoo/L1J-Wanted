package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class MeetYaheeAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MeetYaheeAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MeetYaheeAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("1")) {
			htmlid = "meet005";
		} else if (s.equalsIgnoreCase("d")) {
			if (pc.getInventory().checkItem(40921)) {
				htmlid = StringUtil.EmptyString;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("이동 불가능 : 원소의 지배자 보유."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1139), true), true);
			} else {
				pc.getTeleport().start(32674, 32832, (short) 602, 2, true);
			}
		}
		return htmlid;
	}
}



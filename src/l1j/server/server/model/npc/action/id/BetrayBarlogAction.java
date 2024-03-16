package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_Karma;

public class BetrayBarlogAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BetrayBarlogAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BetrayBarlogAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("1")) {
			if (pc.getKarma() <= -10000000) {
				pc.setKarma(1000000);
				pc.sendPackets(L1ServerMessage.sm1078);
				pc.sendPackets(new S_Karma(pc), true);
				return "betray13";
			}
		}
		return null;
	}
}


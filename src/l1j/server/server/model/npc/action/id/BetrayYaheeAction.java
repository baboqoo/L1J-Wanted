package l1j.server.server.model.npc.action.id;

import l1j.server.Config;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_Karma;

public class BetrayYaheeAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BetrayYaheeAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BetrayYaheeAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("1") && pc.getKarma() >= 10000000) {
			karmaDecrease(pc, -1000000);
			return "betray03";
		}
		return null;
	}
	
	private void karmaDecrease(L1PcInstance pc, int value){
		pc.addKarma((int) (value * Config.RATE.RATE_KARMA));
		pc.sendPackets(L1ServerMessage.sm1079);
		pc.sendPackets(new S_Karma(pc), true);
	}
}


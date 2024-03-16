package l1j.server.server.model.npc.action.id;

import l1j.server.Config;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.utils.StringUtil;

public class MeetBarlogAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MeetBarlogAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MeetBarlogAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("1")) {
			htmlid = "meet105";
		} else if (s.equalsIgnoreCase("2")) {
			htmlid = pc.getInventory().checkItem(40718) ? "meet106" : "meet110";
		} else if (s.equalsIgnoreCase("a")) {
			if (pc.getInventory().consumeItem(40718, 1)) {
				karmaDecrease(pc, -100);
				htmlid = "meet107";
			} else {
				htmlid = "meet104";
			}
		} else if (s.equalsIgnoreCase("b")) {
			if (pc.getInventory().consumeItem(40718, 10)) {
				karmaDecrease(pc, -1000);
				htmlid = "meet108";
			} else {
				htmlid = "meet104";
			}
		} else if (s.equalsIgnoreCase("c")) {
			if (pc.getInventory().consumeItem(40718, 100)) {
				karmaDecrease(pc, -10000);
				htmlid = "meet109";
			} else {
				htmlid = "meet104";
			}
		} else if (s.equalsIgnoreCase("d")) {
			if (pc.getInventory().checkItem(40066)) { // 잠시 아무아이템으로 대쳐하자~송편이잇을경우 못만나기~
				// || pc.getInventory().checkItem(40616)) {
				htmlid = StringUtil.EmptyString;
			} else if (pc.getKarmaLevel() < -1) {
				pc.getTeleport().start(32683, 32895, (short) 608, 5, true);
			}
		}
		return htmlid;
	}
	
	private void karmaDecrease(L1PcInstance pc, int value){
		pc.addKarma((int) (value * Config.RATE.RATE_KARMA));
		pc.sendPackets(L1ServerMessage.sm1079);
		pc.sendPackets(new S_Karma(pc), true);
	}
}


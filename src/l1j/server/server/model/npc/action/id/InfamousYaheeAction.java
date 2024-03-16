package l1j.server.server.model.npc.action.id;

import l1j.server.Config;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class InfamousYaheeAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new InfamousYaheeAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private InfamousYaheeAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (pc.getKarma() >= 10000000) {
			karmaYaheeIncrease(pc, npc, s);
		}
		return StringUtil.EmptyString;
	}
	
	private void karmaYaheeIncrease(L1PcInstance pc, L1NpcInstance npc, String s) {
		L1ItemInstance item = null;
		if (s.equalsIgnoreCase("1")) {
			pc.addKarma((int) (-500 * Config.RATE.RATE_KARMA));
			pc.sendPackets(new S_Karma(pc), true);
			item = pc.getInventory().storeItem(40678, 1);
		} else if (s.equalsIgnoreCase("2")) {
			pc.addKarma((int) (-5000 * Config.RATE.RATE_KARMA));
			pc.sendPackets(new S_Karma(pc), true);
			item = pc.getInventory().storeItem(40678, 10);
		} else if (s.equalsIgnoreCase("3")) {
			pc.addKarma((int) (-50000 * Config.RATE.RATE_KARMA));
			pc.sendPackets(new S_Karma(pc), true);
			item = pc.getInventory().storeItem(40678, 100);
		}
		if (item != null) {
			pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getLogNameRef()), true);
			pc.sendPackets(L1ServerMessage.sm1080);
		}
	}
}


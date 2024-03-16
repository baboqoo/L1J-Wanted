package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class HadestyAction implements L1NpcIdAction {// 치료사
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new HadestyAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private HadestyAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return healer(pc, s);
	}
	
	private String healer(L1PcInstance pc, String s){
		if (pc.getLevel() >= 52) {
			pc.sendPackets(L1ServerMessage.sm939);
			return null;
		}
		if (s.matches("0|fullheal")) {
			if (!pc.getInventory().consumeItem(40320, 1)) {// 흑요석
				pc.sendPackets(L1ServerMessage.sm299);
				return null;
			}
			pc.setCurrentHp(pc.getCurrentHp() + CommonUtil.random(21) + 70);
			pc.sendPackets(L1ServerMessage.sm77);
			pc.send_effect_self(830);
			return StringUtil.EmptyString;
		}
		return null;
	}
}


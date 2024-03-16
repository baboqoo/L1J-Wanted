package l1j.server.server.model.npc.action.id;

import l1j.server.IndunSystem.ice.IceRaidCreator;
import l1j.server.IndunSystem.ice.IceRaidType;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class NewbradAction implements L1NpcIdAction {// 브레드
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new NewbradAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private NewbradAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return bread(pc, s);
	}
	
	private String bread(L1PcInstance pc, String s){
		if (pc.getInventory().checkItem(410555, 1)) {
			return "newbrad6";// 얼어붙은 심장
		}
		if (s.equalsIgnoreCase("a") && pc.getInventory().checkItem(700019, 1) && IceRaidCreator.getInstance().create(pc, IceRaidType.NORMAL)) {// 혹한신전 노말
			pc.getInventory().consumeItem(700019, 1);
			return StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("b") && pc.getInventory().checkItem(700019, 5) && IceRaidCreator.getInstance().create(pc, IceRaidType.HARD)) {// 혹한신전 하드
			pc.getInventory().consumeItem(700019, 5);
			return StringUtil.EmptyString;
		}
		return "newbrad3";
	}
}


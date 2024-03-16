package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;

public class HalpasJakenAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new HalpasJakenAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private HalpasJakenAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("a")) {
			boolean halpasArmor = pc.getInventory().checkEquippedOne(L1ItemId.HALPAS_ARMOR_ITEMS);
			L1BuffUtil.skillAction(pc, halpasArmor ? L1SkillId.BUFF_ZAKEN_HALPAS : L1SkillId.BUFF_ZAKEN);
			return "halpas_jaken1";
	    }
		return null;
	}
}


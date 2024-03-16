package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.spell.S_SkillIconBlessOfEva;

public class ShamansaelAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new ShamansaelAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ShamansaelAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equals("a")) {
			L1BuffUtil.skillAction(pc, L1SkillId.BUFF_SAEL);
			if (!pc.getSkill().hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH)) {						
				pc.getSkill().setSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH, 1800 * 1000);
				pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 1800), true);
			}
			return "shamansael2";
		}
		return null;
	}
}


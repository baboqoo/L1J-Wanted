package l1j.server.server.model.npc.action.id;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class IsvalAction implements L1NpcIdAction {// 이스발
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new IsvalAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private IsvalAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return isval(pc, s);
	}
	
	private String isval(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("a")) {// 잊섬입장
			if (!GameServerSetting.FORGOTTEN_ISLAND) {
				return "fg_isval_cl";
			}
			if (pc.getLevel() < 80) {
				return "fg_isval_fl1";
			}
			pc.getTeleport().start(32742 + CommonUtil.random(5), 32782 + CommonUtil.random(5), (short) 1710, pc.getMoveState().getHeading(), true, true);
			pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
			return StringUtil.EmptyString;
		}
		return null;
	}
}


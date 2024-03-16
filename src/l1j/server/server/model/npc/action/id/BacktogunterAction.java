package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;

public class BacktogunterAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BacktogunterAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BacktogunterAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return redKnightZoneIn(pc, s);
	}
	
	private String redKnightZoneIn(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("a")){
			if (pc.getLevel() >= 55)return "secretgunter07";
			int x = 32736, y = 32847;
			if (pc.getMapId() == 7){ // 붉은 기사단 늪지
				x = 32682;	y = 32814;
			} else if (pc.getMapId() == 12146){ // 거미 동굴
				x = 32689;	y = 32845;
			} else if (pc.getMapId() == 12147){ // 죽은자의 성소
				x = 32701;	y = 32842;
			} else if (pc.getMapId() == 8){ // 숨겨진 길
				x = 32724;	y = 32844;
			}
			pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
			pc.getTeleport().start(x, y, (short) 3, pc.getMoveState().getHeading(), true);
		}
		return null;
	}
}


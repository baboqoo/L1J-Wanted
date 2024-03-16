package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;

public class BirthdayAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BirthdayAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BirthdayAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return birthdayBuff(pc, s);
	}
	
	private String birthdayBuff(L1PcInstance pc, String s){
		if (pc.isInvisble()) {
            pc.sendPackets(L1ServerMessage.sm1003);
            return null;
        }
		if (pc.getLevel() < 45) {
        	return "birthday5"; //45레벨 정도는 되야 드립니다.
        }
        if (s.equalsIgnoreCase("a")) {
        	return "birthday6";
       //	 if (pc.getInventory().checkItem(3000046, 1)) {
       //         return "birthday6"; //탄생일 기간에 다시 오세요
       //	 }
       //	pc.getInventory().storeItem(3000046, 1);
       //	return "birthday3";
        }
        if (s.equalsIgnoreCase("b")) {
        	if (pc.getInventory().consumeItem(3000048, 1)) {
        		L1BuffUtil.skillMotionAction(pc, L1SkillId.COMA_B);
        		return "birthday4";
        	}
        	return "birthday2";// 재료가 부족한 경우
        }
        return null;
	}
}


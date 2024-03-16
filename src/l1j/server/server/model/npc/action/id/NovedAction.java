package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.utils.StringUtil;

public class NovedAction implements L1NpcIdAction {// 치료사
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new NovedAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private NovedAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return healer(pc, s);
	}
	
	private String healer(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("0") || s.equalsIgnoreCase("fullheal")) {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime() + 20 > curtime) {
				pc.sendPackets(L1SystemMessage.DELAY_MSG);
				return null;
			}
			L1BuffUtil.skillArrayAction(pc, L1SkillInfo.HIDDEN_VALIGE_BUFF_ARRAY);
			pc.setCurrentHp(pc.getMaxHp());
			pc.setCurrentMp(pc.getMaxMp());
			pc.send_effect_self(830);
			pc.setQuizTime(curtime);
			return StringUtil.EmptyString; 
		}
		return null;
	}
}


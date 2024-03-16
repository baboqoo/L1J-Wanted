package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.utils.StringUtil;

public class BsGtwBuffAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BsGtwBuffAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BsGtwBuffAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if(s.equals("d")){
    		L1BuffUtil.skillArrayAction(pc, L1SkillInfo.TOWN_BUFF_ARRAY);
    		htmlid = StringUtil.EmptyString;
    	}
		return htmlid;
	}
}


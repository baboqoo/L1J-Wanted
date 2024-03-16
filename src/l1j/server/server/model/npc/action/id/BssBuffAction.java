package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.utils.StringUtil;

public class BssBuffAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BssBuffAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BssBuffAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return townBuffSkyGarden(pc, s);
	}
	
	private String townBuffSkyGarden(L1PcInstance pc, String s){
		if (s.equals("b")) {// 고급 마법을 받는다.
	    	if (pc.getInventory().consumeItem(L1ItemId.PIXIE_GOLD_FEATHER, pc.isPCCafe() ? 2 : 3)) {
	    		int[] skill_array = L1SkillInfo.PCCAFE_BUFF_ARRAY;
	    		int time_secs = 7200;
	    		L1SkillUse l1skilluse = new L1SkillUse(true);
	    		for (int i = 0; i < skill_array.length; i++) {
	    			l1skilluse.handleCommands(pc, skill_array[i], pc.getId(), pc.getX(), pc.getY(), time_secs, L1SkillUseType.GMBUFF);
	    		}
	    		l1skilluse = null;
		    	return StringUtil.EmptyString;
			}
			pc.sendPackets(L1ServerMessage.sm337_FEATHER);// \f1%0이 부족합니다.
		}
		return null;
	}
}


package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.utils.StringUtil;

public class BsTerBuffAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BsTerBuffAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BsTerBuffAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return townBuffTalking(pc, s);
	}
	
	private String townBuffTalking(L1PcInstance pc, String s){
		if(s.equals("a") && pc.getLevel() <= 79){
			if(pc.getInventory().consumeItem(L1ItemId.KNIGHT_COIN, 1000)){// 기사단의 주화
				L1BuffUtil.skillArrayAction(pc, L1SkillInfo.TOWN_BUFF_ARRAY);
				return StringUtil.EmptyString;
			}
			pc.sendPackets(L1ServerMessage.sm337_KNIGHT);// \f1%0이 부족합니다.
		}
		return null;
	}
}


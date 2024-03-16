package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class BsTownBuffAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BsTownBuffAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BsTownBuffAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return townBuff(s, pc);
	}
	
	private String townBuff(String s, L1PcInstance pc) {
		if (s.equals("a")) { // 마법을 받는다.
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
				L1BuffUtil.skillArrayAction(pc, L1SkillInfo.TOWN_BUFF_ARRAY);
				return StringUtil.EmptyString;
			}
			pc.sendPackets(L1ServerMessage.sm189);
			return StringUtil.EmptyString;
		} else if (s.equals("z")) { // 흑사의 기운을 받는다
			if (pc.getInventory().consumeItem(65648, 1)) {
				if(pc.getSkill().hasSkillEffect(L1SkillId.BUFF_BLACK_SAND))pc.getSkill().removeSkillEffect(L1SkillId.BUFF_BLACK_SAND);
				L1BuffUtil.skillMotionAction(pc, L1SkillId.BUFF_BLACK_SAND);
				return StringUtil.EmptyString;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("흑사의 코인이 부족합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1129), true), true);
			return "bs_01z";
		} else if (s.equals("0")) { // 되돌아가기
			return "bs_01";
		}
		return null;
	}
}



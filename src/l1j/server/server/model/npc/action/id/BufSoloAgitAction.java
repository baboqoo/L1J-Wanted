package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.utils.StringUtil;

public class BufSoloAgitAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BufSoloAgitAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BufSoloAgitAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		switch (s) {
		case "a":// 아지트 전용 강화 마법을 받는다
			return owner(pc);
		case "b":// 강화 마법을 받는다.
			return normal(pc);
		default:
			return null;
		}
	}
	
	/**
	 * 아지트 전용 강화 마법
	 * @param pc
	 * @return html
	 */
	String owner(L1PcInstance pc) {
		if (!pc.getInventory().checkItem(31235)) {
			return null;
		}
		L1BuffUtil.skillArrayAction(pc, L1SkillInfo.TOWN_BUFF_ARRAY);
		pc.setCurrentHp(pc.getMaxHp());
		return StringUtil.EmptyString;
	}
	
	/**
	 * 강화 마법
	 * @param pc
	 * @return html
	 */
	String normal(L1PcInstance pc) {
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
			L1BuffUtil.skillArrayAction(pc, L1SkillInfo.TOWN_BUFF_ARRAY);
			return StringUtil.EmptyString;
		}
		pc.sendPackets(L1ServerMessage.sm189);
		return StringUtil.EmptyString;
	}
	
}


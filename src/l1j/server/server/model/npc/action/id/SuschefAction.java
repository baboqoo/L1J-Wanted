package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class SuschefAction implements L1NpcIdAction {// 수상한 요리사
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new SuschefAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private SuschefAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return featherCookBuff(pc, s);
	}
	
	private String featherCookBuff(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("0")) {
			if (pc.getInventory().consumeItem(L1ItemId.PIXIE_FEATHER, 45)) {
				pc.getInventory().storeItem(410055, 1);
				pc.sendPackets(new S_ServerMessage(403, "$8538"), true);
			}
			return "suschef5";
		} else if (s.equalsIgnoreCase("1")) {
			if (pc.getInventory().consumeItem(410057, 1)) {// A
				L1BuffUtil.skillAction(pc, L1SkillId.FEATHER_BUFF_A);
				pc.send_effect(7947);
				return StringUtil.EmptyString;
			} else if (pc.getInventory().consumeItem(410058, 1)) {// B
				L1BuffUtil.skillAction(pc, L1SkillId.FEATHER_BUFF_B);
				pc.send_effect(7948);
				return StringUtil.EmptyString;
			} else if (pc.getInventory().consumeItem(410059, 1)) {// C
				L1BuffUtil.skillAction(pc, L1SkillId.FEATHER_BUFF_C);
				pc.send_effect(7949);
				return StringUtil.EmptyString;
			} else if (pc.getInventory().consumeItem(410060, 1)) {// D
				L1BuffUtil.skillAction(pc, L1SkillId.FEATHER_BUFF_D);
				pc.send_effect(7950);
				return StringUtil.EmptyString;
			}
			return "suschef4";
		}
		return null;
	}
}


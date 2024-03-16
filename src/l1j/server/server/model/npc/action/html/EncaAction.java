package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.StringUtil;

public class EncaAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new EncaAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private EncaAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1ItemInstance item = pc.getInventory().getEquippedArmor();
		if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
			if (item != null) {
				L1BuffUtil.skillMotionAction(pc, L1SkillId.BLESSED_ARMOR);
				pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
			} else {
				pc.sendPackets(L1ServerMessage.sm79);
			}
		} else {
			pc.sendPackets(L1ServerMessage.sm189); // \f1%0이 부족합니다.
		}
		return StringUtil.EmptyString;
	}
}


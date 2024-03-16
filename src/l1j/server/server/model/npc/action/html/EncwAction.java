package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.utils.StringUtil;

public class EncwAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new EncwAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private EncwAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (pc.getWeapon() == null) {
			pc.sendPackets(L1ServerMessage.sm79);
		} else {
			if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
				L1SkillUse l1skilluse = null;
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					if (pc.getWeapon().equals(item)) {
						l1skilluse = new L1SkillUse(true);
						l1skilluse.handleCommands(pc, L1SkillId.ENCHANT_WEAPON, item.getId(), 0, 0, 0, L1SkillUseType.SPELLSC);
						l1skilluse = null;
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
						break;
					}
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm189); // \f1%0이 부족합니다.
			}
		}
		return StringUtil.EmptyString;
	}
}


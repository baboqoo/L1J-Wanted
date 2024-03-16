package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1Item;

public class HolysEvaWater extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public HolysEvaWater(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_HOLY_WATER) || pc.getSkill().hasSkillEffect(L1SkillId.STATUS_HOLY_MITHRIL_POWDER)) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			pc.getSkill().setSkillEffect(L1SkillId.STATUS_HOLY_WATER_OF_EVA, 900000);
			pc.send_effect(190);
			pc.sendPackets(L1ServerMessage.sm1140);
			pc.getInventory().removeItem(this, 1);
		}
	}
}


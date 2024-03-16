package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.templates.L1Item;

public class BlindPotion extends L1ItemInstance {
	private static final long serialVersionUID = 1L;

	public BlindPotion(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isPotionPenalty()) {
				pc.sendPackets(L1ServerMessage.sm698); // \f1마력에 의해 아무것도 마실 수가 없습니다.
				return;
			}
			pc.cancelAbsoluteBarrier();
			blind(pc);
			pc.getInventory().removeItem(this, 1);
		}
	}
	
	private void blind(L1PcInstance pc) {
		int time = 480;
		boolean before_floatingEye = pc.isFloatingEye();
		L1SkillStatus skill = pc.getSkill();
		if (skill.hasSkillEffect(L1SkillId.CURSE_BLIND)) {
			skill.killSkillEffectTimer(L1SkillId.CURSE_BLIND);
		} else if (skill.hasSkillEffect(L1SkillId.DARKNESS)) {
			skill.killSkillEffectTimer(L1SkillId.DARKNESS);
		} else if (skill.hasSkillEffect(L1SkillId.LINDBIOR_SPIRIT_EFFECT)) {
			skill.killSkillEffectTimer(L1SkillId.LINDBIOR_SPIRIT_EFFECT);
		}

		pc.sendPackets(skill.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE) ? S_CurseBlind.BLIND_FLOATING_EYE : S_CurseBlind.BLIND_ON);		
		skill.setSkillEffect(L1SkillId.CURSE_BLIND, time * 1000);
		
		if (!before_floatingEye && skill.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE)) {
			pc.showFloatingEyeToInvis();
		}
	}

}


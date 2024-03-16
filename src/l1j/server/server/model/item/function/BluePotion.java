package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;
import l1j.server.server.templates.L1Item;

public class BluePotion extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public BluePotion(L1Item item) {
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
			int itemId = this.getItemId();
			switch (itemId){
			case 60415:// 마나 절감 물약
				mpReductionPotion(pc);
				break;
			default:
				useBluePotion(pc, itemId);
				break;
			}
			pc.getInventory().removeItem(this, 1);
		}
	}
	
	private void useBluePotion(L1PcInstance pc, int itemId) {// 마나 회복 물약
		int time = this.getItem().getEtcValue();
		if (itemId != 41142) {
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_BLUE_POTION2)) {
				pc.getSkill().removeSkillEffect(L1SkillId.STATUS_BLUE_POTION2);
			}
			int skilltime = pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_BLUE_POTION);
			skilltime += time;
			if (skilltime > 3600) {
				skilltime=3600;
			}
			pc.sendPackets(new S_SkillIconGFX(S_SkillIconGFX.MANA_ICON, skilltime, true), true);
			pc.getSkill().setSkillEffect(L1SkillId.STATUS_BLUE_POTION, skilltime * 1000);
		} else {
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_BLUE_POTION)) {
				pc.getSkill().removeSkillEffect(L1SkillId.STATUS_BLUE_POTION);
			}
			pc.sendPackets(new S_SkillIconGFX(S_SkillIconGFX.MANA_ICON, time, true), true);
			pc.getSkill().setSkillEffect(L1SkillId.STATUS_BLUE_POTION2, time * 1000);
		}
		pc.send_effect(190);
		pc.sendPackets(L1ServerMessage.sm1007);// MP 회복 속도가 빨라집니다.
	}
	
	private void mpReductionPotion(L1PcInstance pc){
		if (pc.getSkill().hasSkillEffect(L1SkillId.MP_REDUCTION_POTION)) {
			pc.getSkill().removeSkillEffect(L1SkillId.MP_REDUCTION_POTION);
		}
		L1BuffUtil.skillAction(pc, L1SkillId.MP_REDUCTION_POTION);
	}
	
}


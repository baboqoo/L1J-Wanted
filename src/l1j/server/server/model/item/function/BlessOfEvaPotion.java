package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.spell.S_SkillIconBlessOfEva;
import l1j.server.server.templates.L1Item;

public class BlessOfEvaPotion extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public BlessOfEvaPotion(L1Item item) {
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
			use(pc);
		}
	}
	
	void use(L1PcInstance pc) {
		int time = getItem().getEtcValue();
		if (time <= 0) {
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH)) {
			int timeSec = pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_UNDERWATER_BREATH);
			time += timeSec;
			if (time > 3600) {
				time = 3600;
			}
		}
		pc.cancelAbsoluteBarrier();
		pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), time), true);
		pc.send_effect(190);
		pc.getSkill().setSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH, time * 1000);
		pc.getInventory().removeItem(this, 1);
	}
	
}


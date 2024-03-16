package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1Item;

public class WisdomPotion extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public WisdomPotion(L1Item item) {
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
			if (pc.isWizard() || pc.isIllusionist()) {
				wisdomUse(pc);
			} else {
				pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
			}
			pc.getInventory().removeItem(this, 1);
		}
	}
	
	private void wisdomUse(L1PcInstance pc) {
		int time = this.getItem().getEtcValue(); // 시간은 4의 배수로 하는 것
		if (!pc.getSkill().hasSkillEffect(L1SkillId.STATUS_WISDOM_POTION)) {
			pc.getAbility().addSp(2);
			pc.addMpRegen(2);
		}
		int skilltime = pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_WISDOM_POTION);
		skilltime += time;
		if (skilltime >= 7200) {
			skilltime = 7200;
		}
		pc.sendPackets(new S_SpellBuffNoti(pc, L1SkillId.STATUS_WISDOM_POTION, true, skilltime), true);
		pc.send_effect(750);
		pc.getSkill().setSkillEffect(L1SkillId.STATUS_WISDOM_POTION, skilltime * 1000);
	}
	
}


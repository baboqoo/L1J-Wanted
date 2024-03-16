package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;

public class HealingPotion extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public HealingPotion(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isStop()) {
				return;
			}
			int delay_id = ((L1EtcItem) this.getItem()).getDelayId();
			if (delay_id != 0 && pc.hasItemDelay(delay_id)) {
				return;// 지연 설정 있어
			}
			if (pc.isPotionPenalty() || pc.getSkill().hasSkillEffect(L1SkillId.STATUS_PRESSURE_DEATH_RECAL)) {
				pc.sendPackets(L1ServerMessage.sm698);// 마력에 의해 아무것도 마실 수가 없습니다.
				return;
			}
			L1HealingPotion potion = this.getItem().getHealingPotion();
			if (potion == null) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (potion.use(pc, this)) {
				L1ItemDelay.onItemUse(pc, this);// 아이템 지연 개시
			}
		}
	}

}


package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class DragonPearl extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public DragonPearl(L1Item item) {
		super(item);
	}
	
	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getDrunkenItemEquipped() != 0) {
				pc.sendPackets(L1ServerMessage.sm2887);// 특정 아이템이 있어 아직은 사용할 수 없습니다.
				return;
			}
			if (pc.isPotionPenalty()) {
				pc.sendPackets(L1ServerMessage.sm698); // \f1마력에 의해 아무것도 마실 수가 없습니다.
				return;
			}
			
			pc.cancelAbsoluteBarrier();
			int time = (10 * 60);// 10분 1초
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)) {
				pc.getSkill().killSkillEffectTimer(L1SkillId.STATUS_DRAGON_PEARL);
				pc.sendPackets(new S_Liquor(pc.getId(), 0), true);
				pc.getMoveState().setDrunken(0);
			}
			
			pc.send_effect(197);
			pc.getSkill().setSkillEffect(L1SkillId.STATUS_DRAGON_PEARL, time * 1000);
			pc.broadcastPacketWithMe(new S_Liquor(pc.getId(), 8), true);
			pc.getMoveState().setDrunken(8);
			pc.sendPackets(new S_ServerMessage(1065, time), true);
			pc.getInventory().removeItem(this, 1);
		}
	}
}


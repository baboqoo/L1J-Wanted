package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.templates.L1Item;

public class GreenPotion extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public GreenPotion(L1Item item) {
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
			int itemId = this.getItemId();
			
			// 앱솔루트 배리어의 해제
			pc.cancelAbsoluteBarrier();
			int time = 0;
			if (itemId == L1ItemId.POTION_OF_HASTE_SELF || itemId == 130028 || itemId == 40030 || itemId == 3006) {// 속도 향상 물약
				time = 300;
			} else if (itemId == L1ItemId.B_POTION_OF_HASTE_SELF) {// 축복 속도 향상 물약
				time = 350;
			} else if (itemId == 40039) {// 와인
				time = 600;
			} else if (itemId == 40040) {// 위스키
				time = 900;
			} else if (itemId == 140018 || itemId == 41338) {// 농축 속도의 물약, 포도주
				time = 1200;
			} else if (itemId == 40018 || itemId == 41342 || itemId == 410520) {// 강화 속도 향상 물약, 축복된 와인, 메듀사의 피
				time = 1800;
			} else if (itemId == 41261 || itemId == 41262 || itemId == 41268 || itemId == 41269 || itemId == 41271 || itemId == 41272|| itemId == 41273) {
				time = 30;
			}
			pc.send_effect(191);
			// XXX:헤이스트아이템 장비시, 취한 상태가 해제되는지 불명
			if (pc.getHasteItemEquipped() > 0) {
				return;
			}
			// 취한 상태를 해제
			pc.setDrink(false);

			// 헤이 파업, 그레이터 헤이 파업과는 중복 하지 않는다
			if (pc.getSkill().hasSkillEffect(L1SkillId.HASTE)) {
				pc.getSkill().killSkillEffectTimer(L1SkillId.HASTE);
				pc.broadcastPacketWithMe(new S_SkillHaste(pc.getId(), 0, 0), true);
				pc.getMoveState().setMoveSpeed(0);
			} else if (pc.getSkill().hasSkillEffect(L1SkillId.GREATER_HASTE)) {
				pc.getSkill().killSkillEffectTimer(L1SkillId.GREATER_HASTE);
				pc.broadcastPacketWithMe(new S_SkillHaste(pc.getId(), 0, 0), true);
				pc.getMoveState().setMoveSpeed(0);
			} /*else if(pc.hasSkillEffect(STATUS_HASTE)) {
				pc.killSkillEffectTimer(STATUS_HASTE);
				pc.broadcastPacketWithMe(new S_SkillHaste(pc.getId(), 0, 0), true);
				pc.setMoveSpeed(0);
			}*/

			// 슬로우, 그레이터 슬로우, 엔탕르중은 슬로우 상태를 해제할 뿐
			if (pc.getSkill().hasSkillEffect(L1SkillId.SLOW)) { // 슬로우
				pc.getSkill().killSkillEffectTimer(L1SkillId.SLOW);
				pc.broadcastPacketWithMe(new S_SkillHaste(pc.getId(), 0, 0), true);
			} else if (pc.getSkill().hasSkillEffect(L1SkillId.GREATER_SLOW)) { // 그레이트 슬로우
				pc.getSkill().killSkillEffectTimer(L1SkillId.GREATER_SLOW);
				pc.broadcastPacketWithMe(new S_SkillHaste(pc.getId(), 0, 0), true);
			} else {
				int skilltime = pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_HASTE);
				skilltime += time;
				if (skilltime >= 7200) {
					skilltime = 7200;
				}
				pc.sendPackets(new S_SkillHaste(pc.getId(), 1, skilltime), true);
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0), true);
				pc.getMoveState().setMoveSpeed(1);
				pc.getSkill().setSkillEffect(L1SkillId.STATUS_HASTE, skilltime * 1000);
			}
			if (itemId == 40030) {
				pc.getQuest().questItemUse(itemId);
			}
			pc.getInventory().removeItem(this, 1);
		}
	}
	
}


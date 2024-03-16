package l1j.server.server.model.item.function;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Einhasad;
import l1j.server.server.model.L1ExpPotion;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1Item;

public class ExpBonusPotion extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ExpBonusPotion(L1Item item) {
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
			int item_id = this.getItemId();
			pc.cancelAbsoluteBarrier();
			if (item_id == 600347) {
				expPotionSub(pc);
			} else {
				expPotion(pc, item_id);
			}
		}
	}
	
	private void expPotionSub(L1PcInstance pc){
		if (pc.getSkill().hasSkillEffect(L1SkillId.DRAGON_EXP_POTION)) {
			return;
		}
		L1BuffUtil.skillAction(pc, L1SkillId.DRAGON_EXP_POTION);
		pc.getInventory().removeItem(this, 1);
	}
	
	private void expPotion(L1PcInstance pc, int item_id){
		L1SkillStatus skill = pc.getSkill();
		L1ExpPotion expPotion = skill.getExpPotion();
		
		// 기존 버프 제거
		if (expPotion.getSkillId() > 0 && skill.hasSkillEffect(expPotion.getSkillId())) {
			skill.removeSkillEffect(expPotion.getSkillId());
		}

		int time = this.getItem().getBuffDurationSecond();
		switch (item_id) {
		case 210094:// 성장의 물약
			expPotion.init(L1SkillId.EXP_POTION);
			skill.setSkillEffect(expPotion.getSkillId(), time * 1000);
			pc.send_effect(9279);
			pc.sendPackets(L1ServerMessage.sm1313);
			pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, time, expPotion.getPotionId(), 1), true);
			break;
		case 30105:// 빛나는 성장의 물약
			expPotion.init(L1SkillId.EXP_POTION1);
			skill.setSkillEffect(expPotion.getSkillId(), time * 1000);
		    pc.send_effect(13249);
		    pc.sendPackets(pc.isPCCafe() ? L1ServerMessage.sm2116 : L1ServerMessage.sm1313);
		    region_status_default(pc, skill, expPotion, time);
			break;
		case 520282:// 진 데스나이트의 성장 물약
			expPotion.init(L1SkillId.EXP_POTION2);
			skill.setSkillEffect(expPotion.getSkillId(), time * 1000);
			pc.send_effect(16125);
			region_status_default(pc, skill, expPotion, time);
			break;
		case 600346:// 드래곤의 성장 물약
			expPotion.init(L1SkillId.EXP_POTION3);
			skill.setSkillEffect(expPotion.getSkillId(), time * 1000);
			pc.send_effect(16072);
			region_status_einhasad(pc, skill, expPotion, time);
			break;
		case 30205:// 영웅의 성장의 물약
			expPotion.init(L1SkillId.EXP_POTION4);
			skill.setSkillEffect(expPotion.getSkillId(), time * 1000);
			pc.send_effect(18724);
			pc.getAbility().addDamageReduction(2);
			pc.addMaxHp(100);
			pc.getAbility().addItemPotionPercent(10);
			pc.getAbility().addItemPotionValue(10);
			region_status_default(pc, skill, expPotion, time);
			break;
		case 30207:// 21주년 성장 물약
			expPotion.init(L1SkillId.EXP_POTION5);
			skill.setSkillEffect(expPotion.getSkillId(), time * 1000);
			pc.send_effect(18972);
			region_status_default(pc, skill, expPotion, time);
			break;
		case 30208:// 21주년 전설 성장 물약
			expPotion.init(L1SkillId.EXP_POTION6);
			skill.setSkillEffect(expPotion.getSkillId(), time * 1000);
			pc.send_effect(18971);
			region_status_default(pc, skill, expPotion, time);
			break;
		case 43051:// 아인하사드의 성장 물약
			expPotion.init(L1SkillId.EXP_POTION7);
			skill.setSkillEffect(expPotion.getSkillId(), time * 1000);
			pc.send_effect(18971);
			region_status_default(pc, skill, expPotion, time);
			break;
		case 30209:// 향상된 성장의 물약
			expPotion.init(L1SkillId.EXP_POTION8);
			skill.setSkillEffect(expPotion.getSkillId(), time * 1000);
			pc.send_effect(20110);
			region_status_default(pc, skill, expPotion, time);
			break;
		}

		pc.sendPackets(new S_ExpBoostingInfo(pc), true);
		pc.getInventory().removeItem(this, 1);
	}
	
	void region_status_default(L1PcInstance pc, L1SkillStatus skill, L1ExpPotion expPotion, int time) {
		if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, time, expPotion.getPotionId(), 2), true);// off
			skill.stopSkillEffectTimer(expPotion.getSkillId());// 타이머 중지
		} else {
			pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, time, expPotion.getPotionId(), 1), true);// on
			expPotion.setStop(false);
		}
	}
	
	void region_status_einhasad(L1PcInstance pc, L1SkillStatus skill, L1ExpPotion expPotion, int time) {
		if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, time, expPotion.getPotionId(), 2), true);// off
			skill.stopSkillEffectTimer(expPotion.getSkillId());// 타이머 중지
		} else {
			L1Einhasad ein = pc.getAccount().getEinhasad();
			if (ein.getPoint() >= Config.EIN.REST_EXP_DEFAULT_RATION) {
			    pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, time, expPotion.getPotionId(), 1), true);// on
			    expPotion.setStop(false);
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, time, expPotion.getPotionId(), 2), true);// off
				skill.stopSkillEffectTimer(expPotion.getSkillId());// 타이머 중지
			}
		}
	}
	
}


package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1Item;

public class BattlePaper extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public BattlePaper(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			switch(itemId){
			case 31162:
				kyuljun(pc);
				break;
			case 410010:case 410011:case 410012:case 30063:// 체력 증강의 주문서, 마력 증강의 주문서, 전투 강화의 주문서 , 드래곤의 돌
			case 31159:case 31160:case 31161:// 투사의 전투 강화 주문서, 명궁의 전투 강화 주문서, 현자의 전투 강화 주문서
				if (itemId == 30063 && !(pc.getMapId() >= 1005 && pc.getMapId() <= 1022 || pc.getMapId() >= 1161 && pc.getMapId() <= 1166 || pc.getMapId() > 6000 && pc.getMapId() < 6999)) {// 드래곤의 돌
					pc.sendPackets(L1ServerMessage.sm1891);// 드래곤의 숨결이 깃든 땅에서만 사용할 수 있습니다.
					return;
				}
				buff(pc, itemId);
				break;
			case 31252:// 체력 강화 주문서
				hpScroll(pc);
				break;
			}
		}
	}
	
	private void buff(L1PcInstance pc, int item_id) {
		int time = this.getItem().getBuffDurationSecond(), scroll = 0, eft = 0;;
		if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CASHSCROLL)) {
			pc.getSkill().removeSkillEffect(L1SkillId.STATUS_CASHSCROLL);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CASHSCROLL2)) {
			pc.getSkill().removeSkillEffect(L1SkillId.STATUS_CASHSCROLL2);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CASHSCROLL3)) {
			pc.getSkill().removeSkillEffect(L1SkillId.STATUS_CASHSCROLL3);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.NEW_CASHSCROLL1)) {
			pc.getSkill().removeSkillEffect(L1SkillId.NEW_CASHSCROLL1);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.NEW_CASHSCROLL2)) {
			pc.getSkill().removeSkillEffect(L1SkillId.NEW_CASHSCROLL2);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.NEW_CASHSCROLL3)) {
			pc.getSkill().removeSkillEffect(L1SkillId.NEW_CASHSCROLL3);
		}
		if (item_id == 410010) {
			eft = scroll = 6993;
			pc.addMaxHp(50);
			pc.addHpRegen(4);
		} else if (item_id == 410011) {
			eft = scroll = 6994;
			pc.addMaxMp(40);
			pc.addMpRegen(4);
		} else if (item_id == 410012 || item_id == 30063) {
			eft = scroll = 6995;
			pc.getAbility().addShortDmgup(3);
			pc.getAbility().addShortHitup(3);
			pc.getAbility().addSp(3);
		} else if (item_id == 31159) {
			eft = 16551;
			scroll = L1SkillId.NEW_CASHSCROLL1;
			pc.getAbility().addShortDmgup(3);
			pc.getAbility().addShortHitup(5);
			pc.getAbility().addPVPDamageReduction(3);
		} else if (item_id == 31160) {
			eft = 16552;
			scroll = L1SkillId.NEW_CASHSCROLL2;
			pc.getAbility().addLongDmgup(3);
			pc.getAbility().addLongHitup(5);
			pc.getAbility().addPVPDamageReduction(3);
		} else if (item_id == 31161) {
			eft = 16553;
			scroll = L1SkillId.NEW_CASHSCROLL3;
			pc.getAbility().addSp(3);
			pc.getAbility().addMagicHitup(5);
			pc.getAbility().addPVPDamageReduction(3);
		}
		pc.send_effect(eft);
		pc.getSkill().setSkillEffect(scroll, time * 1000);
		pc.getInventory().removeItem(this, 1);
	}
	
	private void kyuljun(L1PcInstance pc) {
		final int time = this.getItem().getBuffDurationSecond(), scroll = L1SkillId.KYULJUN_CASHSCROLL;
		if (pc.getSkill().hasSkillEffect(scroll)) {
			pc.getSkill().removeSkillEffect(scroll);
		}
		pc.getAC().addAc(-5);
		pc.getAbility().addShortHitup(5);
		pc.getAbility().addLongHitup(5);
		pc.getAbility().addMagicHitup(2);
		pc.getAbility().addPVPDamageReduction(5);
		pc.getAbility().addPVPDamage(5);
		pc.send_effect(18530);
		pc.getSkill().setSkillEffect(scroll, time * 1000);
		pc.sendPackets(new S_SpellBuffNoti(pc, scroll, true, time), true);
		pc.getInventory().removeItem(this, 1);
	}
	
	private void hpScroll(L1PcInstance pc){
		final int time = this.getItem().getBuffDurationSecond(), scroll = L1SkillId.HP_CASHSCROLL;
		if (pc.getSkill().hasSkillEffect(scroll)) {
			pc.getSkill().removeSkillEffect(scroll);
		}
		pc.addMaxHp(2000);
		pc.getSkill().setSkillEffect(scroll, time * 1000);
		pc.sendPackets(new S_SpellBuffNoti(pc, scroll, true, time), true);
		pc.getInventory().removeItem(this, 1);
	}
}


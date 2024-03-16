package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.templates.L1Item;

public class IceStatBuff extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public IceStatBuff(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			iceBuff(pc);// 빙수 버프
		}
	}
	
	private void iceBuff(L1PcInstance pc){
		if (pc.getSkill().hasSkillEffect(L1SkillId.BUFF_ICE_STR)) {
			pc.getSkill().removeSkillEffect(L1SkillId.BUFF_ICE_STR);
		} else if (pc.getSkill().hasSkillEffect(L1SkillId.BUFF_ICE_DEX)) {
			pc.getSkill().removeSkillEffect(L1SkillId.BUFF_ICE_DEX);
		} else if (pc.getSkill().hasSkillEffect(L1SkillId.BUFF_ICE_INT)) {
			pc.getSkill().removeSkillEffect(L1SkillId.BUFF_ICE_INT);
		}
		int item_id = this.getItem().getItemId();
		int effect = 0;
		if (item_id == 31118) {
			pc.getSkill().setSkillEffect(L1SkillId.BUFF_ICE_STR, 900 * 1000);
			pc.getAbility().addShortHitup(5);
			pc.getAbility().addShortDmgup(3);
			pc.getAbility().addAddedStr((byte) 1);
			effect = 7954;
		} else if (item_id == 31119) {
			pc.getSkill().setSkillEffect(L1SkillId.BUFF_ICE_DEX, 900 * 1000);
			pc.getAbility().addLongHitup(5);
			pc.getAbility().addLongDmgup(3);
			pc.getAbility().addAddedDex((byte) 1);
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
			effect = 7952;
		} else if (item_id == 31120) {
			pc.getSkill().setSkillEffect(L1SkillId.BUFF_ICE_INT, 900 * 1000);
			pc.addMaxMp(50);
			pc.getAbility().addAddedInt((byte) 1);
			effect = 7956;
		}
		pc.sendPackets(new S_OwnCharStatus2(pc), true);
		pc.send_effect(effect);
		pc.getInventory().removeItem(this, 1);
	}
}


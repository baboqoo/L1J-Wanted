package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.templates.L1Item;

public class FeatheerLuckyBuff extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public FeatheerLuckyBuff(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch(this.getItemId()){
			case 410057:	buff(pc, L1SkillId.FEATHER_BUFF_A, 7947);break;// 운세쪽지a
			case 410058:	buff(pc, L1SkillId.FEATHER_BUFF_B, 7948);break;// 운세쪽지b
			case 410059:	buff(pc, L1SkillId.FEATHER_BUFF_C, 7949);break;// 운세쪽지c
			case 410060:	buff(pc, L1SkillId.FEATHER_BUFF_D, 7950);break;// 운세쪽지d
			}
		}
	}
	
	private void buff(L1PcInstance pc, int skillid, int gfx){
		if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_A)) {
			pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_A);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_B)) {
			pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_B);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_C)) {
			pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_C);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_D)) {
			pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_D);
		}
		L1SkillUse skill = new L1SkillUse(true);
		skill.handleCommands(pc, skillid, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.SPELLSC);
		skill = null;
		pc.send_effect(gfx);
		pc.getInventory().removeItem(this, 1);
	}
}


package l1j.server.server.model.skill.action;

import l1j.server.Config;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_CharacterFollowEffect;
import l1j.server.server.serverpackets.S_InstanceHP;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class DivineProtection extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc._divineProtectionHp = Config.SPELL.DIVINE_PROTECTION_HP_VALUE;
			pc.sendPackets(new S_InstanceHP(pc._divineProtectionHp, Config.SPELL.DIVINE_PROTECTION_HP_VALUE, true), true);
			pc.sendPackets(new S_CharacterFollowEffect(pc.getId(), true, 20137), true);
			icon(pc, time);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc._divineProtectionHp = 0;
			pc.sendPackets(S_InstanceHP.FINISH);
			pc.sendPackets(new S_CharacterFollowEffect(pc.getId(), false, 20137), true);
			pc.send_effect(20141);// 종료 이팩트
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new DivineProtection().setValue(_skillId, _skill);
	}

}


package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1CurseParalysis;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;

public class CurseParalyze extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha.isBind()) {
			return 0;
		}
		int duration = 3000;
		if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
			duration += attacker.getAbility().getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			duration -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (cha instanceof L1PcInstance) {
			L1CurseParalysis.curse(cha, 7000, duration);
		}else if (cha instanceof L1MonsterInstance) {
			if (!((L1MonsterInstance) cha).getNpcTemplate().isBossMonster()) {
				L1CurseParalysis.curse(cha, 0, duration);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.broadcastPacketWithMe(new S_Poison(pc.getId(), 0), true);
			pc.sendPackets(S_Paralysis.PARALYSIS_OFF);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new CurseParalyze().setValue(_skillId, _skill);
	}

}


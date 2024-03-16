package l1j.server.server.model.skill.action;

import l1j.server.server.construct.L1Undead;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class TurnUndead extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		L1Undead undeadType = null;
		if (cha instanceof L1MonsterInstance) {
			undeadType = ((L1MonsterInstance) cha).getNpcTemplate().getUndead();// 언데드 몬스터 세팅
		}
		if (undeadType == L1Undead.UNDEAD || undeadType == L1Undead.UNDEAD_BOSS) {
			return cha.getCurrentHp();
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		// TODO Auto-generated method stub

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
		return new TurnUndead().setValue(_skillId, _skill);
	}

}


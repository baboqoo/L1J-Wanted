package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class BuffIntAdd extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		bonus(cha, true);
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		bonus(cha, false);
	}
	
	void bonus(L1Character cha, boolean flag) {
		int flag_val = flag ? 1 : -1;
		cha.getAbility().addSp(1 * flag_val);
		cha.getAbility().addMagicHitup(3 * flag_val);
		cha.getAbility().addAddedInt(1 * flag_val);
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new BuffIntAdd().setValue(_skillId, _skill);
	}

}


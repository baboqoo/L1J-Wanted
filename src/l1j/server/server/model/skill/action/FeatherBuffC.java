package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class FeatherBuffC extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		ablity(cha, true);
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		ablity(cha, false);
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}

	private void ablity(L1Character cha, boolean flag){
		cha.addMaxHp(flag ? 50 : -50);
		cha.addMaxMp(flag ? 30 : -30);
		cha.getAC().addAc(flag ? -2 : 2);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new FeatherBuffC().setValue(_skillId, _skill);
	}
}


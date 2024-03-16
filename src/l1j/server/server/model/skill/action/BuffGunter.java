package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class BuffGunter extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		setAblity(cha, true);
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		setAblity(cha, false);
	}
	
	private void setAblity(L1Character cha, boolean flag){
		cha.getAbility().addAddedDex(flag ? (byte) 5 : (byte) -5);
		cha.getAbility().addLongHitup(flag ? 7 : -7);
		cha.getAbility().addLongDmgup(flag ? 5 : -5);
		cha.addMaxHp(flag ? 100 : -100);
		cha.addMaxMp(flag ? 40 : -40);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addHpRegen(flag ? 10 : -10);
			pc.addMpRegen(flag ? 3 : -3);
		}
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
		return new BuffGunter().setValue(_skillId, _skill);
	}

}


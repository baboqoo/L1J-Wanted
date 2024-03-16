package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class RunTeleport extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1NpcInstance) {
			L1NpcInstance mon = (L1NpcInstance) cha;
			L1Location newLocation = mon.getLocation().randomLocation(6, 8, true);
			mon.teleport(newLocation.getX(), newLocation.getY(), mon.getMoveState().getHeading());
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
		// TODO Auto-generated method stub
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new RunTeleport().setValue(_skillId, _skill);
	}

}


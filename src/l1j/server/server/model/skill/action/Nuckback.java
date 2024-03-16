package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.utils.L1SpawnUtil;

public class Nuckback extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(attacker)) {
			if (pc == null) {
				continue;
			}
			pc.getTeleport().randomTeleport(false, 5);
		}
		if (_skillId == JIBAE_IRIS_TELEPORT || _skillId == IRIS_TELEPORT) {
			int Map2 = cha.getMapId();
			int x5 = cha.getX();
			int y5 = cha.getY();
			int x6 = x5 - 5;
			int y6 = y5 - 5;
			L1SpawnUtil.spawn2(x5, y5, (short) Map2, 5, _skillId == JIBAE_IRIS_TELEPORT ? 800168 : 900099, 0, 2700 * 1000, 0); //아이리스 분신
			L1SpawnUtil.spawn2(x6, y6, (short) Map2, 5, _skillId == JIBAE_IRIS_TELEPORT ? 800168 : 900099, 0, 2700 * 1000, 0); //아이리스 분신
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
		return new Nuckback().setValue(_skillId, _skill);
	}

}


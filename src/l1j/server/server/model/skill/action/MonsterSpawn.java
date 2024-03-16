package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class MonsterSpawn extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		int Map = cha.getMapId();
		int x = cha.getX();
		int y = cha.getY();
		if (_skillId == BALLOG_MONSTER) {
			L1SpawnUtil.spawn2(x + CommonUtil.random(5) - CommonUtil.random(3), y + CommonUtil.random(5) - CommonUtil.random(3), (short) Map, 5, 7220075, 0, 2700 * 1000, 0);
			L1SpawnUtil.spawn2(x - CommonUtil.random(5) - CommonUtil.random(3), y - CommonUtil.random(5) - CommonUtil.random(3), (short) Map, 5, 7220075, 0, 2700 * 1000, 0);
			L1SpawnUtil.spawn2(x + CommonUtil.random(5) - CommonUtil.random(3), y + CommonUtil.random(5) - CommonUtil.random(3), (short) Map, 5, 7220074, 0, 2700 * 1000, 0);
			L1SpawnUtil.spawn2(x - CommonUtil.random(5) - CommonUtil.random(3), y - CommonUtil.random(5) - CommonUtil.random(3), (short) Map, 5, 7220074, 0, 2700 * 1000, 0);
		} else if (_skillId == VALA_HALPAS) {
			L1SpawnUtil.spawn2(x + CommonUtil.random(5) - CommonUtil.random(3), y + CommonUtil.random(5) - CommonUtil.random(3), (short) Map, 5, 5000103, 0, 60 * 1000, 0);//60초후 자폭
			L1SpawnUtil.spawn2(x - CommonUtil.random(5) - CommonUtil.random(3), y - CommonUtil.random(5) - CommonUtil.random(3), (short) Map, 5, 5000103, 0, 60 * 1000, 0);
			L1SpawnUtil.spawn2(x + CommonUtil.random(5) - CommonUtil.random(3), y + CommonUtil.random(5) - CommonUtil.random(3), (short) Map, 5, 5000104, 0, 60 * 1000, 0);
			L1SpawnUtil.spawn2(x - CommonUtil.random(5) - CommonUtil.random(3), y - CommonUtil.random(5) - CommonUtil.random(3), (short) Map, 5, 5000104, 0, 60 * 1000, 0);
			for (L1PcInstance c : L1World.getInstance().getAllPlayers()) {//데스나이트의 분노 버프
				if (c.getMap().getId() == Map && CommonUtil.random(100) + 1 < 50) {
					L1SkillUse skill = new L1SkillUse(true);
					skill.handleCommands(c, DETHNIGHT_BUNNO, c.getId(), c.getX(), c.getY(), 60, L1SkillUseType.GMBUFF);
					skill = null;
				}						
			}
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
		return new MonsterSpawn().setValue(_skillId, _skill);
	}

}


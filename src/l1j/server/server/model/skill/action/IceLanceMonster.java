package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.utils.CommonUtil;

public class IceLanceMonster extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1NpcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (CommonUtil.random(10) + 1 < 3) {
				int freezeTime = 5000;
				pc.getSkill().setSkillEffect(ICE_LANCE, freezeTime);
				L1EffectSpawn.getInstance().spawnEffect(81168, freezeTime, cha.getX(), cha.getY(), cha.getMapId());
				pc.broadcastPacketWithMe(new S_Poison(pc.getId(), 2), true);
				pc.sendPackets(S_Paralysis.FREEZE_ON);			 
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
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new IceLanceMonster().setValue(_skillId, _skill);
	}

}


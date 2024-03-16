package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Effect;

public class WeakElemental extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1MonsterInstance == false) {
			return 0;
		}
		switch (((L1MonsterInstance) cha).getNpcTemplate().getWeakAttr()) {
		case EARTH:
			cha.broadcastPacket(new S_Effect(cha.getId(), 2169), true);
			return 0;
		case WATER:
			cha.broadcastPacket(new S_Effect(cha.getId(), 2167), true);
			return 0;
		case FIRE:
			cha.broadcastPacket(new S_Effect(cha.getId(), 2166), true);
			return 0;
		case WIND:
			cha.broadcastPacket(new S_Effect(cha.getId(), 2168), true);
			return 0;
		default:
			cha.broadcastPacket(new S_Effect(cha.getId(), 19319), true);
			return 0;
		}
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
		return new WeakElemental().setValue(_skillId, _skill);
	}

}


package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_Effect;

public class FouSlayerMonster extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1NpcInstance && cha instanceof L1PcInstance) {
			L1NpcInstance npc	= (L1NpcInstance)attacker;
			L1PcInstance target	= (L1PcInstance) cha;
			attacker.broadcastPacket(new S_Effect(attacker.getId(), 6509), true);
			npc.isFouSlayer = true;
			for (int i = 3; i > 0; i--) {
				if (target.isDead() || target.getCurrentHp() <= 0) {
					break;
				}
				target.onAction(npc);
			}
			npc.isFouSlayer = false;
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
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new FouSlayerMonster().setValue(_skillId, _skill);
	}

}


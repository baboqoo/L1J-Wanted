package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_PacketBox;

public class CursePoison extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		int duration = 3000;
		if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
			duration += attacker.getAbility().getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			duration -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (!pc.getMap().isSafetyZone(pc.getLocation())) {
				L1DamagePoison.doInfection(attacker, pc, duration, 5, false);
				pc.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, pc, 1, duration / 1000), true);
			}
		} else if (cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			L1DamagePoison.doInfection(attacker, npc, duration, 5, false);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
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
		return new CursePoison().setValue(_skillId, _skill);
	}

}


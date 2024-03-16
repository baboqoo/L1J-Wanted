package l1j.server.server.model.skill.action;

import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;

public class Destroy extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		cha.getAC().addAc(5);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (attacker.isPassiveStatus(L1PassiveId.DESTROY_FEAR)) {
				pc.getAbility().addDg(-10);
				pc._statusDistroyFear = true;
				pc.send_effect(18959);
			}
			if (attacker.isPassiveStatus(L1PassiveId.DESTROY_HORROR)) {
				pc.getAbility().addAddedStr((byte) -5);
				pc.getAbility().addAddedInt((byte) -5);
				pc._statusDistroyHorror = true;
				pc.send_effect(18961);
			}
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			if (attacker.isPassiveStatus(L1PassiveId.DESTROY_FEAR)) {
				npc._statusDistroyFear = true;
				npc.broadcastPacket(new S_Effect(npc.getId(), 18959), true);
			}
			if (attacker.isPassiveStatus(L1PassiveId.DESTROY_HORROR)) {
				npc._statusDistroyHorror = true;
				npc.broadcastPacket(new S_Effect(npc.getId(), 18961), true);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		cha.getAC().addAc(-5);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc._statusDistroyFear) {
				pc.getAbility().addDg(10);
				pc._statusDistroyFear = false;
			}
			if (pc._statusDistroyHorror) {
				pc.getAbility().addAddedStr((byte) 5);
				pc.getAbility().addAddedInt((byte) 5);
				pc._statusDistroyHorror = false;
			}
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			if (npc._statusDistroyFear) {
				npc._statusDistroyFear = false;
			}
			if (npc._statusDistroyHorror) {
				npc._statusDistroyHorror = false;
			}
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.sendPackets(new S_OwnCharStatus2(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Destroy().setValue(_skillId, _skill);
	}

}


package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class CreateZombie extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (!(attacker instanceof L1PcInstance) || !(cha instanceof L1MonsterInstance)) {
			return 0;
		}
		L1PcInstance pc = (L1PcInstance)attacker;
		int petcost = 0;
		Object[] petlist = attacker.getPetList().values().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
		int charisma = attacker.getAbility().getTotalCha();
		if (pc.isElf()) {
			charisma += 12;
		} else if (pc.isWizard()) {
			charisma += 6;
		}
		charisma -= petcost;
		if (charisma >= 6) {
			new L1SummonInstance((L1MonsterInstance)cha, attacker, true);
		} else {
			pc.sendPackets(L1ServerMessage.sm319);
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
		return new CreateZombie().setValue(_skillId, _skill);
	}

}


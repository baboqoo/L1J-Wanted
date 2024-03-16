package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class TamingMonster extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (!(attacker instanceof L1PcInstance) || !(cha instanceof L1MonsterInstance) || !((L1MonsterInstance) cha).getNpcTemplate().isTamingable()) {
			return 0;
		}
		L1PcInstance attackPc = (L1PcInstance)attacker;
		int petcost = 0;
		Object[] petlist = attacker.getPetList().values().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
		int charisma = attacker.getAbility().getTotalCha();
		if (attackPc.isElf()) {
			charisma += 12;
		} else if (attackPc.isWizard()) {
			charisma += 6;
		}
		charisma -= petcost;
		int petCount = charisma / 6;
		if (petCount <= 0 || petcost > 12) {
			attackPc.sendPackets(L1ServerMessage.sm489); // 물러가려고 하는 애완동물이 너무 많습니다.
			return 0;
		}
		if (charisma >= 6) {
			new L1SummonInstance((L1NpcInstance)cha, attacker, false);
		} else {
			attackPc.sendPackets(L1ServerMessage.sm319);
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
		return new TamingMonster().setValue(_skillId, _skill);
	}

}


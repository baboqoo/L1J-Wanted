package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class Pantera extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha == null || attacker.isHold() || attacker.isDesperado() || attacker.isOsiris() || attacker.isEternity() || attacker.isShadowStepChaser()) {
			return 0;
		}
		if (attacker.getX() == cha.getX() && attacker.getY() == cha.getY()) {// 같은 위치
			return 0;
		}
		int targetX = 0, targetY = 0;
	 	byte heading = (byte)attacker.targetDirection(cha.getX(), cha.getY());
 		switch(heading){
		case 0:targetX = cha.getX();targetY = cha.getY() + 1;break;
		case 1:targetX = cha.getX() - 1;targetY = cha.getY() + 1;break;
		case 2:targetX = cha.getX() - 1;targetY = cha.getY();break;
		case 3:targetX = cha.getX() - 1;targetY = cha.getY() - 1;break;
		case 4:targetX = cha.getX();targetY = cha.getY() - 1;break;
		case 5:targetX = cha.getX() + 1;targetY = cha.getY() - 1;break;
		case 6:targetX = cha.getX() + 1;targetY = cha.getY();break;
		case 7:targetX = cha.getX() + 1;targetY = cha.getY() + 1;break;
		}
		if (!(attacker.getX() == targetX && attacker.getY() == targetY)) {
			if (!attacker.getMap().isInMap(targetX, targetY)) {
				return 0;// 산출된 위치체크
			}
			if (!attacker.getMap().isPassable(targetX, targetY)) {
				return 0;// 통과 체크
			}
		}
	 	((L1PcInstance)attacker).getTeleport().startMoveSkill(cha, targetX, targetY, heading, _skillId, magic);
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(S_Paralysis.STURN_OFF);
			pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_STUN, false, 0), true);
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			npc.setParalyzed(false);
		}
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
		return new Pantera().setValue(_skillId, _skill);
	}

}


package l1j.server.server.model.skill.action;

import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class Cruel extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		int changeBuffDuration = 0;
		if (attacker instanceof L1PcInstance) {
			L1PcInstance attackPc = (L1PcInstance)attacker;
			if (magic.calcProbabilityMagic(_skillId)) {
				if (cha instanceof L1PcInstance) {// 타겟이 pc인 경우만 이동 처리
					L1PcInstance target	= (L1PcInstance) cha;
				 	byte heading		= (byte)attackPc.targetDirection(target.getX(), target.getY());// 타겟방향
				 	byte targetHeading	= attackPc._isLancerForm ? heading : (byte) target.targetDirection(attackPc.getX(), attackPc.getY());// form에 따른 타겟 방향 
				 	L1Location loc		= attackPc._isLancerForm ? getCruelLong(attackPc, target, heading, _skill.getRanged() + (attackPc.isPassiveStatus(L1PassiveId.CRUEL_CONVICTION) ? 2 : 0)) : getCruelShort(attackPc, target, heading);
				 	if (loc != null) {
				 		target.getTeleport().startMoveSkill(null, loc.getX(), loc.getY(), targetHeading, _skillId, magic);
				 	}
				}
				boolean conviction	= attackPc.isPassiveStatus(L1PassiveId.CRUEL_CONVICTION);
				changeBuffDuration	= CommonUtil.randomIntChoice(conviction ? CRUEL_CONVICTION_ARRAY : CRUEL_ARRAY);// 시간 랜덤을 위해
				if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
					changeBuffDuration += attacker.getAbility().getStrangeTimeIncrease();
				}
				if (cha.getAbility().getStrangeTimeDecrease() > 0) {
					changeBuffDuration -= cha.getAbility().getStrangeTimeDecrease();
				}
				if (changeBuffDuration <= 0) {
					return 0;
				}
				L1EffectSpawn.getInstance().spawnEffect(conviction ? 61015 : 61014, changeBuffDuration, cha.getX(), cha.getY(), cha.getMapId());
				cha.getSkill().setSkillEffect(_skillId, changeBuffDuration);
				if (cha instanceof L1PcInstance) {
					L1PcInstance target = (L1PcInstance) cha;
					target.sendPackets(S_Paralysis.STURN_ON);
					target.sendPackets(new S_SpellBuffNoti(target, STATUS_STUN, true, changeBuffDuration / 1000), true);
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
					L1NpcInstance target = (L1NpcInstance) cha;
					target.setParalyzed(true);
				}
			}
		}
		return changeBuffDuration;
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
	
	/**
	 * 원거리 폼: 타겟이 이동할 위치 산출
	 * @param pc
	 * @param target
	 * @param heading
	 * @param range
	 * @return L1Location
	 */
	private L1Location getCruelLong(L1PcInstance pc, L1PcInstance target, byte heading, int range){
		L1Location loc	= null;
		int moveX = 0, moveY = 0;
		L1Map checkMap = pc.getLocation().getMap();
		for (int i=1; i<=range; i++) {// 거리1부터 지정거리까지 체크
			int checkX = pc.getX(), checkY = pc.getY();
			switch(heading){
			case 0:checkY-=i;break;
			case 1:checkX+=i;checkY-=i;break;
			case 2:checkX+=i;break;
			case 3:checkX+=i;checkY+=i;break;
			case 4:checkY+=i;break;
			case 5:checkX-=i;checkY+=i;break;
			case 6:checkX-=i;break;
			case 7:checkX-=i;checkY-=i;break;
			}
			if (!(checkX == target.getX() && checkY == target.getY()) && !(checkMap.isInMap(checkX, checkY) && checkMap.isPassable(checkX, checkY))) {
				break;// 지정된 좌표가 이동가능한 위치인지 체크
			}
			moveX = checkX;
			moveY = checkY;
		}
		if (moveX != 0 && moveY != 0) {
			loc = new L1Location(moveX, moveY, pc.getMapId());
		}
		return loc;
	}
	
	/**
	 * 근거리 폼: 타겟이 이동할 위치 산출
	 * @param pc
	 * @param target
	 * @param heading
	 * @return L1Location
	 */
	private L1Location getCruelShort(L1PcInstance pc, L1PcInstance target, byte heading){
		L1Location loc		= null;
		boolean firstFail	= false;
		int moveX = 0,moveY = 0;
		L1Map checkMap = pc.getLocation().getMap();
		while(true){
			int checkX = pc.getX(), checkY = pc.getY();
			if (heading == -1) {
				break;// 모든 방향 실패시
			}
			switch (heading) {
			case 0:checkY--;break;
			case 1:checkX++;checkY--;break;
			case 2:checkX++;break;
			case 3:checkX++;checkY++;break;
			case 4:checkY++;break;
			case 5:checkX--;checkY++;break;
			case 6:checkX--;break;
			case 7:checkX--;checkY--;break;
			default:break;
			}
			moveX = checkX;
			moveY = checkY;
			if ((checkX == target.getX() && checkY == target.getY()) || (checkMap.isInMap(checkX, checkY) && checkMap.isPassable(checkX, checkY))) {
				break;//지정된 좌표가 이동가능한 위치인지 체크
			}
			if (!firstFail) {
				firstFail = true;
				if (heading <= 6) {
					heading += 2;// 좌표 실패시 근접방향부터 검사
				}
			}
			heading--;
		}
		if (moveX != 0 && moveY != 0) {
			loc = new L1Location(moveX, moveY, pc.getMapId());
		}
		return loc;
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Cruel().setValue(_skillId, _skill);
	}

}


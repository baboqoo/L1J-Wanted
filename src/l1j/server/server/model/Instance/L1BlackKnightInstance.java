package l1j.server.server.model.Instance;

import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.model.L1MobGroupInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;

/**
 * 흑기사 담당 인스턴스
 * @author LinOffice
 */
public class L1BlackKnightInstance extends L1MonsterInstance {
	private static final long serialVersionUID	= 1L;
	
	private boolean isCooker;
	private boolean isCat;
	
	/**
	 * 생성자
	 * 부모 상속 L1MonsterInstance
	 * @param template
	 */
	public L1BlackKnightInstance(L1Npc template) {
		super(template);
		isCooker	= template.getNpcId() == 8513;
		isCat		= template.getNpcId() == 8518;
	}
	
	/**
	 * 몬스터가 인식할 타겟을 강제로 설정한다.
	 * @return boolean
	 */
	@Override
	protected boolean isSearchTargetForce(){
		L1ScarecrowInstance targetCrow	= null;
		
		int x = getX();
		int y = getY();
		if (x == 32663 && y == 33151) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 3)) {
				if (obj instanceof L1ScarecrowInstance) {
					L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
					if (crow != null && crow.getNpcTemplate().getNpcId() == 5215 && crow.getX() == 32663 && crow.getY() == 33149) {
						targetCrow = crow;
						break;
					}
				}
			}
		} else if (x == 32667 && y == 33158) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 3)) {
				if (obj instanceof L1ScarecrowInstance) {
					L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
					if (crow != null && crow.getNpcTemplate().getNpcId() == 5215 && crow.getX() == 32669 && crow.getY() == 33158) {
						targetCrow = crow;
						break;
					}
				}
			}
		} else if (x == 32667 && y == 33161) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 3)) {
				if (obj instanceof L1ScarecrowInstance) {
					L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
					if (crow != null && crow.getNpcTemplate().getNpcId() == 5215 && crow.getX() == 32669 && crow.getY() == 33161) {
						targetCrow = crow;
						break;
					}
				}
			}
		}
		
		if (targetCrow != null) {
			_hateList.add(targetCrow, 0);
			_target = targetCrow;
			return true;
		}
		return false;
	}
	
	@Override
	public void searchTarget() {
		// TODO 몬스터가 인식할 타겟을 설정한다.
		L1PcInstance targetPlayer		= null;
		L1MonsterInstance targetMonster	= null;
		
		if (isSearchTargetForce()) {
			return;
		}
		
		/** @설명글// 추가 
		 *   이후에있을지도모를 1.Monster vs Monster 
		 *                               2.Monster vs Guard
		 *                               3.Monster vs Guardian
		 *                               4.Monster vs Npc
		 *  위와같은 상황을 위해 오브젝트를 불러오도록 추가 현재는 1번만을위한 소스임
		 *  간단하게 오브젝트를 인스턴스of로 선언만해주면되게끔 설정 
		 * 
		 */
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (pc == null || pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm() || pc.isMonitor() || pc.isGhost() || pc.getSkill().isBlindHidingAssassin()) {
					continue;
				}

				if (!isAgro() && !isAgroPoly() && getNpcTemplate().isAgroGfxId1() < 0 && getNpcTemplate().isAgroGfxId2() < 0) {
					if (pc.getAlignment() < -1000) {
						targetPlayer = pc;
						break;
					}
					continue;
				}

				if (!pc.isInvisble() || isAgroInvis()) {
					if (pc.isShapeChange()) {
						if (isAgroPoly()) {
							targetPlayer = pc;
							break;
						}
					} else if (isAgro()) {
						targetPlayer = pc;
						break;
					}

					if (getNpcTemplate().isAgroGfxId1() >= 0 && getNpcTemplate().isAgroGfxId1() <= 4) {
						if (L1CharacterInfo.CLASS_GFX_IDS[getNpcTemplate().isAgroGfxId1()][0] == pc.getSpriteId() || L1CharacterInfo.CLASS_GFX_IDS[getNpcTemplate().isAgroGfxId1()][1] == pc.getSpriteId()) {
							targetPlayer = pc;
							break;
						}
					} else if (pc.getSpriteId() == getNpcTemplate().isAgroGfxId1()) {
						targetPlayer = pc;
						break;
					}

					if (getNpcTemplate().isAgroGfxId2() >= 0 && getNpcTemplate().isAgroGfxId2() <= 4) {
						if (L1CharacterInfo.CLASS_GFX_IDS[getNpcTemplate().isAgroGfxId2()][0] == pc.getSpriteId() || L1CharacterInfo.CLASS_GFX_IDS[getNpcTemplate().isAgroGfxId2()][1] == pc.getSpriteId()) {
							targetPlayer = pc;
							break;
						}
					} else if (pc.getSpriteId() == getNpcTemplate().isAgroGfxId2()) {
						targetPlayer = pc;
						break;
					}
				}
			} else if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getHiddenStatus() != 0 || mon.isDead()) {
					continue;
				}
				int targetNpcId = mon.getNpcTemplate().getNpcId();
				
				// 검은 전함 고양이 TO 쥐
				if (isCat && (targetNpcId == 8516 || targetNpcId == 8517)) {
					targetMonster = mon;
					break;
				}
			}
		}

		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
		if (targetMonster != null) { 
			_hateList.add(targetMonster, 0);
			_target = targetMonster;
		}
	}
	
	@Override
	public void onTarget() {
		try {
			if (_target == null) {
				return;
			}
			int targetX = _target.getX();
			int targetY = _target.getY();
			_actived = true;
			if (getAtkspeed() == 0 && getPassispeed() == 0) {
				return;
			}
			int distance = getLocation().getTileLineDistance(_target.getLocation());
			if (_target.getMapId() != getMapId() || distance > DISTANCE_RANGE_VALUE) {
				tagertClear();
				return;
			}
			if (isBlind() && distance > 1) {
				tagertClear();
				return;
			}
			if (isCooker) {
				return;// 검은 기사단 요리사
			}
			if (mobSkill.isActionDelay() || mobSkill.skillUse(_target)) {
				setSleepTime(calcSleepTime(mobSkill.getSleepTime(), MAGIC_SPEED));
				return;
			}
			
			// 허수아비 공격
			if (getMapId() == 9 && _target.getMapId() == 9 && (targetX == 32663 && targetY == 33149 || targetX == 32669 && targetY == 33158 || targetX == 32669 && targetY == 33161) && distance <= 2) {
				attackTarget();
				return;
			}
			int range = getNpcTemplate().getRanged();
			if (isAttackPosition(_target, range, _target instanceof L1DoorInstance)
					&& _target.isAttackPosition(this, range, false)) {
				attackTarget();
				return;
			}
			
			// TODO 공격범위가 아니므로 타겟에게 이동 처리
			if (getPassispeed() <= 0) {
				tagertClear();
				return;
			}
			if (isHold()) {
				return;
			}
			onMove(targetX, targetY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean noTarget() {
		if (_master != null && _master.getMapId() == getMapId() && getLocation().getTileLineDistance(_master.getLocation()) > 2) {
			return noTargetMaster();
		}
		if (L1World.getInstance().getRecognizePlayer(this).size() == 0) {
			return true;
		}
		int x = getX(), y = getY();
		if (getMapId() == 9
				&& (x == 32658 && y == 33170 
				|| x == 32658 && y == 33150
				|| x == 32660 && y == 33151
				|| x == 32660 && y == 33153)) {// 멍타기
			return true;
		}
		if (getMovementDistance() > 0 && getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > getMovementDistance()) {
			teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
		}
		if (aStar != null && _master == null && getPassispeed() > 0 && !isRest()) {
			L1MobGroupInfo mobGroupInfo = getMobGroupInfo();
			if (mobGroupInfo == null || mobGroupInfo != null && mobGroupInfo.isLeader(this)) {
				noTargetNormal();
			} else {
				return noTargetGroup(mobGroupInfo.getLeader());
			}
		}
		return false;
	}
}


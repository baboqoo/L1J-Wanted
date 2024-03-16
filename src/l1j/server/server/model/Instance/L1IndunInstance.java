package l1j.server.server.model.Instance;

import l1j.server.GameSystem.astar.World;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1MobGroupInfo;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;

/**
 * 인던 몬스터 인스턴스(타겟 이동 처리)
 * @author LinOffice
 */
public class L1IndunInstance extends L1MonsterInstance {
	private static final long serialVersionUID	= 1L;
	
	private boolean isUnicorn;
	private boolean isGludio;
	private boolean isGludioSoul;
	private boolean isCrocodile;
	private boolean isRabbit, statusRabbit;
	
	@Override
	public void onItemUse() {
		if (isRabbit && !statusRabbit && !_actived && _target != null && _target instanceof L1PcInstance) {// 로서스 섬의 안내자
			rabbitDeleteTimerStart();
		}
		super.onItemUse();
	}
	
	/**
	 * 생성자
	 * 부모 상속 L1MonsterInstance
	 * @param template
	 */
	public L1IndunInstance(L1Npc template) {
		super(template);
		int npcId		= template.getNpcId();
		isUnicorn		= (npcId >= 7200008 && npcId <= 7200020) 
				|| npcId == 7200055 
				|| npcId == 7200056 
				|| (npcId >= 7200030 && npcId <= 7200041);
		isGludio		= npcId == 7800007 
				|| npcId == 7800064 
				|| (npcId >= 7800010 && npcId <= 7800014) 
				|| (npcId >= 7800020 && npcId <= 7800021) 
				|| (npcId >= 7800030 && npcId <= 7800031)
				|| (npcId >= 7800040 && npcId <= 7800041) 
				|| (npcId >= 7800050 && npcId <= 7800051) 
				|| (npcId >= 7800054 && npcId <= 7800055) 
				|| (npcId >= 7800060 && npcId <= 7800063);
		isGludioSoul	= npcId == 7800007 || npcId == 7800064;
		isCrocodile		= npcId == 7800101 || npcId == 7800102;
		isRabbit		= npcId == 7800114;
	}
	
	/**
	 * 몬스터가 인식할 타겟을 강제로 설정한다.
	 * @return boolean
	 */
	@Override
	protected boolean isSearchTargetForce(){
		L1MonsterInstance targetMonster	= null;
		if (isUnicorn) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.isDead()) {
						continue;
					}
					if (mon.getNpcTemplate().getNpcId() == 7200003 || mon.getNpcTemplate().getNpcId() == 7200029) {// 유니콘
						targetMonster = mon;
						break;
					}
				}
			}
		} else if (isGludio){
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.isDead()) {
						continue;
					}
					if (mon.getNpcTemplate().getNpcId() == 7800000) {// 흑마법 수정구
						targetMonster = mon;
						break;
					}
				}
			}
		}
		
		if (targetMonster != null) { 
			_hateList.add(targetMonster, 0);
			_target = targetMonster;
			return true;
		}
		return false;
	}
	
	@Override
	public void searchTarget() {
		// TODO 몬스터가 인식할 타겟을 설정한다.
		if (isUnicorn || isGludio) {
			isSearchTargetForce();
			return;
		}
		
		if (isCrocodile) {
			super.searchTarget();
			return;
		}
	}
	
	@Override
	public void setHate(L1Character cha, int hate) {
		if (cha == null || cha.getId() == getId()) {
			return;
		}
		_hateList.add(cha, hate);
		_dropHateList.add(cha, hate);
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
			if (_target.getMapId() != getMapId() || distance > DISTANCE_RANGE_VALUE || _target.getSkill().isBlindHidingAssassin()) {
				tagertClear();
				return;
			}
			if (this.isBlind() && distance > 1) {
				tagertClear();
				return;
			}
			if (getAtkspeed() == 0 && getPassispeed() > 0) {
				if (distance > 15) {
					tagertClear();
					return;
				}
				onlyMoveAstar(targetX, targetY);
				return;
			}
			
			if (isUnicorn && _target instanceof L1MonsterInstance && !(((L1MonsterInstance)_target).getNpcId() == 7200003 || ((L1MonsterInstance)_target).getNpcId() == 7200029)) {
				tagertClear();
				return;
			}
			
			if (mobSkill.isActionDelay() || mobSkill.skillUse(_target)) {
				setSleepTime(calcSleepTime(mobSkill.getSleepTime(), MAGIC_SPEED));
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
				return;
			}
			if (isHold()) {
				return;
			}
			
			// 글루디오 연구실
			if (isGludioSoul && (getX() > 32790 && getX() < 32808 && getY() > 32855 && getY() < 32872)) {
				return;
			}
			
			int dir = moveDirection(_target.getMapId(), targetX, targetY);
			dir = checkObject(getX(), getY(), getMapId(), dir);
			
			if (isGludio && !(getX() > 32790 && getX() < 32808 && getY() > 32855 && getY() < 32872)) {// 글루디오 연구실
				dir = moveDirectionIndun(_target.getMapId(), targetX, targetY);
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
				return;
			}
			
			// 유니콘 사원
			if (dir == -1 && isUnicorn) {
				L1Map m = L1WorldMap.getInstance().getMap(getMapId());
				if (m.getOriginalTile(getX(), getY()) == 12) {
					getMoveState().setHeading(targetDirection(targetX, targetY));
					dir = getMoveState().getHeading();
				}
			}
			if (dir == -1) {
				return;
			}
			boolean door = World.isDoorMove(getX(), getY(), getMapId(), calcheading(this, targetX, targetY));
			boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir);
			
			// 유니콘 사원
			if (isUnicorn) {
				door = false;
				tail = true;
			}
			if (door || !tail) {
				return;
			}
			setDirectionMove(dir);
			setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean noTarget() {
		if (L1World.getInstance().getRecognizePlayer(this).size() == 0) {
			return true;
		}
		if (isCrocodile) {// 악어섬의 비밀 보스
			return true;
		}
		
		if (getMovementDistance() > 0 && getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > getMovementDistance()) {
			teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			return false;
		}

		if (aStar != null && getPassispeed() > 0 && !isRest()) {
			if (isUnicorn && (getX() != getHomeX() || getY() != getHomeY())) {
				return noTargetHomeMove();
			}
			L1MobGroupInfo mobGroupInfo = getMobGroupInfo();
			if (mobGroupInfo == null || mobGroupInfo != null && mobGroupInfo.isLeader(this)) {
				noTargetNormal();
			} else {
				return noTargetGroup(mobGroupInfo.getLeader());
			}
		}
		return false;
	}
	
	/**
	 * 로서스 섬의 안내자 삭제 타이머 가동
	 */
	void rabbitDeleteTimerStart(){
		statusRabbit = _statusEscape = true;
		new L1NpcDeleteTimer(this, 60000).begin();
	}
}


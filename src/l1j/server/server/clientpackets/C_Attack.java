package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.IndunSystem.minigame.L1Gambling3;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.PerformAdapter;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.PolyTable.WeaponeType;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcSpeedSync;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.sprite.AcceleratorChecker.ACT_TYPE;
import l1j.server.server.model.sprite.L1Sprite;
import l1j.server.server.serverpackets.action.S_AttackStatus;
import l1j.server.server.serverpackets.action.S_UseArrowSkill;
//import manager.Manager;  // MANAGER DISABLED

public class C_Attack extends ClientBasePacket {
	private L1PcInstance pc;
	private L1PcSpeedSync sync;
	private L1Object target;
	private long currentTime;
	private L1ItemInstance weapon;
	private L1World world;
	private int targetId;
	private int target_x, target_y;
	private int owner_x, owner_y;
	
	public C_Attack(byte[] decrypt, GameClient client) {
		super(decrypt);
		pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		// 공격 액션을 취할 수 있는 상태 확인
		if (!pc._isWeightNoActionPenalty && pc.getInventory().getWeightPercent() > 82) {// 중량 오버
			pc.sendPackets(L1ServerMessage.sm110);// 아이템이 너무 무거워 전투할 수가 없습니다.
			return;
		}
		sync		= pc.getSpeedSync();
		currentTime	= System.currentTimeMillis();
		if (Config.SPEED.SPEED_CHECKER_ACTIVE 
				&& PerformAdapter.CPU_USAGE < Config.SPEED.SPEED_CHECKER_CPU_VALUE 
				&& isSpeedDenal()) {
			return;
		}
		if (pc.isInvisble()
				&& !(pc.getSkill().hasSkillEffect(L1SkillId.BLIND_HIDING) && pc.getPassiveSkill().isBlindHidingAssassinAttack())) {
			return;
		}
		
		targetId	= readD();
		target_x	= readH();
		target_y	= readH();
		
		owner_x		= pc.getX();
		owner_y		= pc.getY();
		
		world		= L1World.getInstance();
		target		= world.findObject(targetId);
		if (target instanceof L1NpcInstance && !((L1NpcInstance) target).isEnableHiddenStatus()) {
			return;
		}

		weapon = pc.getWeapon();
		if (target instanceof L1Character) {// npc도 일단 characters를 상속하니까 그냥 이거만 쓰면 됨다
			if (target.getMapId() != pc.getMapId()) {
				return;// 타겟이 이상한 장소에 있으면(자) 종료
			}
			if (weapon == null && PolyTable.isPolyWeapon(WeaponeType.BOW, pc.getSpriteId())) {// 무기가 없고 활변신
				return;
			}
			// TODO 공격 가능 거리 체크
			if (!is_range((L1Character)target)) {
				return;
			}
		}
		if (pc.isFishing()) {
			pc.finishFishing();
		}

		// 미니게임
		if (owner_x == 33515 && owner_y == 32851 && pc.getMapId() == 4 
				&& target instanceof L1NpcInstance && ((L1NpcInstance)target).getNpcTemplate().getNpcId() == 300027) {
			gambling();
		}

		// 공격 액션을 취할 수 있는 경우의 처리
		pc.cancelAbsoluteBarrier();
		if (!pc.isPassiveStatus(L1PassiveId.MEDITATION_BEYOND)) {
			pc.getSkill().removeSkillEffect(L1SkillId.MEDITATION);
		}
		pc.setRegenState(L1PcInstance.REGENSTATE_ATTACK);

		// 미리 타입검사
		if (target != null && target instanceof L1Character && !((L1Character) target).isDead()) {
			onTarget();
		} else {
			noTarget();
		}
		sync.setAttackSyncInterval(pc.getAcceleratorChecker().getRightInterval(ACT_TYPE.ATTACK) + currentTime + Config.SPEED.ATTACK_SPEED_SYNCHRONIZED);// 액션 시간
	}
	
	/**
	 * 대상을 공격 가능한지 검증
	 * @param target
	 * @return boolean
	 */
	boolean is_range(L1Character target) {
		// 타겟의 현재 위치가 패킷에서 조사한 위치인지 검증
		if (target.getLocation().getTileLineDistance(target_x, target_y) > 1) {
			return false;
		}
		int attack_range	= pc.getAttackRange();
		L1Sprite sprite		= target.getSprite();
		int distance_x		= Math.abs(owner_x - target_x);
		int distance_y		= Math.abs(owner_y - target_y);
		int width			= 0;
		int height			= 0;
		// 타겟 sprite의 범위
		if (sprite != null) {
			width			= sprite.get_width();
			height			= sprite.get_height();
		}
		if (distance_x > width + attack_range || distance_y > height + attack_range) {
			return false;
		}
		if (!pc.getMap().isUserPassable(owner_x, owner_y, pc.calcheading(owner_x, owner_y, target_x, target_y))) {
			noTarget();
			sync.setAttackSyncInterval(pc.getAcceleratorChecker().getRightInterval(ACT_TYPE.ATTACK) + currentTime + Config.SPEED.ATTACK_SPEED_SYNCHRONIZED);// 액션 시간
			return false;
		}
		return true;
	}
	
	/**
	 * 타겟 공격
	 */
	void onTarget() {
		if (pc.isWizard() && pc.getPetList() != null && !pc.getPetList().isEmpty()) {
			summonTargetChange();
		}
		target.onAction(pc);
	}
	
	/**
	 * 타겟 없이 공격
	 */
	void noTarget() {
		if (pc.getRegion() == L1RegionStatus.NORMAL) {// 노말존
			for (L1PcInstance user : world.getVisiblePlayer(pc)) {
				// 인비지 상태의 유저에게 공격을 실행한다.
				if (user.isInvisble() && user.getRegion() == L1RegionStatus.NORMAL && user.getX() == target_x && user.getY() == target_y) {
					user.onAction(pc);
					return;
				}
			}
		}
		int heading = pc.targetDirection(target_x, target_y);
		if (pc.getMoveState().getHeading() != heading) {
			pc.getMoveState().setHeading(heading);// 방향세트
		}
		if (weapon != null) {
			L1ItemWeaponType weaponType = weapon.getItem().getWeaponType();
			if (L1ItemWeaponType.isBowWeapon(weaponType)) {
				noTargetBow();
				return;
			}
			if (weaponType == L1ItemWeaponType.GAUNTLET) {
				noTargetGauntlet();
				return;
			}
	    }
		pc.broadcastPacketWithMe(new S_AttackStatus(pc, 0, ActionCodes.ACTION_Attack), true);
	}
	
	/**
	 * 스피드 부정 검사
	 * @return boolean
	 */
	boolean isSpeedDenal() {
		long actionInterval = sync.getAttackSyncInterval();
		int overCount = currentTime < actionInterval ? sync.increaseAttackSpeedOverCountAndGet() : sync.decreaseAttackSpeedOverCountAndGet();
		if (overCount >= Config.SPEED.SPEED_CHECKER_OVER_COUNT) {
			//System.out.println(String.format("■■■■■ [%s] 스피드핵(ATTACK) 의심 ■■■■■ 오차(%d)", pc.getName(), actionInterval - currentTime));
			System.out.println(String.format("■■■■■ [%s] Suspected Speed Hack (ATTACK) ■■■■■ Deviation(%d)", pc.getName(), actionInterval - currentTime));
			//Manager.getInstance().TimeSpeed("ATTACK", pc.getName(), pc); // MANAGER DISABLED
			sync.resetAttackSpeedOverCount();
			return true;
		}
		return false;
	}
	
	/**
	 * 서먼 몬스터 타겟팅 변경
	 */
	void summonTargetChange(){
		if (target instanceof L1MonsterInstance == false) {
			return;
		}
		for (L1NpcInstance obj : pc.getPetList().values()) {
			if (obj instanceof L1SummonInstance && obj.getTarget() != null && target != obj.getTarget()) {
				obj.setTarget((L1MonsterInstance)target);// 타겟팅 변경
			}
		}
	}
	
	/**
	 * 화살 출력
	 */
	void noTargetBow() {
		pc.getInventory().searchArrow();
		L1ItemInstance arrow = pc.getInventory().getArrow();
		if (arrow == null) {
			if (weapon.getItemId() == 190 || weapon.getItemId() == 202011) {
				pc.broadcastPacketWithMe(new S_UseArrowSkill(pc, targetId, 2349, target_x, target_y, false), true);
			}
			return;
		}
		if (arrow.getCount() == 1) {
			pc.getInventory().setArrow(null);
		}
		pc.getInventory().removeItem(arrow, 1);
		pc.broadcastPacketWithMe(new S_UseArrowSkill(pc, targetId, pc.getArrowStingSprite(true), target_x, target_y, false), true);
	}
	
	/**
	 * 스팅 출력
	 */
	void noTargetGauntlet() {
		pc.getInventory().searchSting();
		L1ItemInstance sting = pc.getInventory().getSting();
		if (sting == null) {
			return;
		}
		if (sting.getCount() == 1) {
			pc.getInventory().setSting(null);
		}
		pc.getInventory().removeItem(sting, 1);
		pc.broadcastPacketWithMe(new S_UseArrowSkill(pc, targetId, pc.getArrowStingSprite(false), target_x, target_y, false), true);
	}
	
	/**
	 * 미니 게임
	 */
	void gambling() {
		L1Gambling3 gam3 = new L1Gambling3();
		gam3.dealerTrade(pc);
	}
	
}


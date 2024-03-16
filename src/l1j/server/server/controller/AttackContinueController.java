package l1j.server.server.controller;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l1j.server.Config;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.PolyTable.WeaponeType;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.sprite.L1Sprite;
import l1j.server.server.model.sprite.AcceleratorChecker.ACT_TYPE;

/**
 * 자동 공격 컨트롤러
 * @author LinOffice
 */
public class AttackContinueController implements Runnable {
	private static boolean active = false;
	private static final Map<L1PcInstance, L1Character> DATA	= new HashMap<L1PcInstance, L1Character>();
	private static final List<L1PcInstance> REMOVE				= new ArrayList<L1PcInstance>();

	@Override
	public void run() {
		while (active) {
			try {
				Thread.sleep(10L);
				synchronized(DATA) {
					if (DATA.isEmpty()) {
						continue;
					}
					long currentTime = System.currentTimeMillis();
					for (Map.Entry<L1PcInstance, L1Character> entry : DATA.entrySet()) {
						L1PcInstance attacker = entry.getKey();
						if (attacker == null) {// nullpoint check
							REMOVE.add(attacker);
							continue;
						}
						if (currentTime < attacker.getSpeedSync().getAttackSyncInterval()) {
							continue;
						}
						if (attacker.isStop() || attacker.isDesperado() || attacker.isOsiris()) {
							continue;
						}
						L1Character target = entry.getValue();
						if (target == null) {// nullpoint check
							REMOVE.add(attacker);
							continue;
						}
						if (!isValidation(attacker, target)) {// 유효성 검사
							REMOVE.add(attacker);
							continue;
						}
						if (!action(attacker, target, currentTime)) {
							REMOVE.add(attacker);
						}
					}
					if (!REMOVE.isEmpty()) {
						for (L1PcInstance attacker : REMOVE) {
							DATA.remove(attacker);
						}
						REMOVE.clear();
					}
				}
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
	}
	
	boolean isValidation(L1PcInstance attacker, L1Character target){
		if ((!attacker._isWeightNoActionPenalty && attacker.getInventory().getWeightPercent() > 82) 
				|| target.isDead() || attacker.isDead() || attacker.getTeleport().isTeleport() || target.getMapId() != attacker.getMapId()
				|| attacker.isGhost() || attacker.isInvisble() || attacker.isInvisDelay() 
				|| attacker.isParalyzed() || attacker.isSleeped() || attacker.isPoisonParalyzed()) {
			return false;
		}
		if (attacker.getWeapon() == null && PolyTable.isPolyWeapon(WeaponeType.BOW, attacker.getSpriteId())) {// 무기가 없고 활변신이라면 칼질 안되게
			return false;
		}
		if (!is_range(attacker, target)) {// 거리 체크
			return false;
		}
		return true;
	}
	
	/**
	 * 대상을 공격 가능한지 검증
	 * @param attacker
	 * @param target
	 * @return boolean
	 */
	boolean is_range(L1PcInstance attacker, L1Character target) {
		int owner_x			= attacker.getX();
		int owner_y			= attacker.getY();
		int target_x		= target.getX();
		int target_y		= target.getY();
		int attack_range	= attacker.getAttackRange();
		L1Sprite sprite		= target.getSprite();
		int distance_x		= Math.abs(owner_x - target_x);
		int distance_y		= Math.abs(owner_y - target_y);
		int width			= 0;
		int height			= 0;
		// sprite의 범위
		if (sprite != null) {
			width			= sprite.get_width();
			height			= sprite.get_height();
		}
		if (distance_x > width + attack_range || distance_y > height + attack_range) {
			return false;
		}
		return true;
	}

	boolean action(L1PcInstance attacker, L1Character target, long currentTime) {
		attacker.cancelAbsoluteBarrier();
		attacker.setRegenState(REGENSTATE_ATTACK);
		if (target.getCurrentHp() > 0 || target instanceof L1NpcInstance) {
			target.onAction(attacker);// 액션발생
			attacker.getSpeedSync().setAttackSyncInterval(attacker.getAcceleratorChecker().getRightInterval(ACT_TYPE.ATTACK) + currentTime + Config.SPEED.ATTACK_CONTINUE_SPEED_SYNCHRONIZED);// 액션 시간
			return true;
		}
		return false;
	}
	
	/**
	 * 자동 공격 시작 메소드
	 * 쓰레드가 멈춰있다면 생성하여 시작한다.
	 * @param attacker
	 * @param target
	 */
	public static void start(L1PcInstance attacker, L1Character target) {
		synchronized(DATA){
			if (!active) {
				active = true;
				new Thread(new AttackContinueController()).start();
			}
			if (!DATA.containsKey(attacker) || DATA.get(attacker) != target) {
				DATA.put(attacker, target);// 담기
			}
		}
	}

	/**
	 * 자동 공격을 중지한다.
	 * @param pc
	 */
	public static void stop(L1PcInstance pc) {
		synchronized(DATA) {
			if (DATA.containsKey(pc)) {
				DATA.remove(pc);
			}
		}
	}
}

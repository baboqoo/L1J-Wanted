package l1j.server.server.clientpackets;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_MOVE;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.GameSystem.inn.InnTeleport;
import l1j.server.server.GameClient;
import l1j.server.server.PerformAdapter;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.datatables.FreePVPRegionTable;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.DungeonRandom;
import l1j.server.server.model.L1PcSpeedSync;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.sprite.AcceleratorChecker.ACT_TYPE;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.action.S_MoveCharPacket;
//import manager.Manager;  // MANAGER DISABLED

public class C_MoveChar extends ClientBasePacket {
	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	private L1PcInstance pc;
	private L1PcSpeedSync sync;
	private int locX, locY, heading, oriX, oriY;
	private long currentTime;
	
	public C_MoveChar(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		try {
			sync = pc.getSpeedSync();
			currentTime = System.currentTimeMillis();
			if (Config.SPEED.SPEED_CHECKER_ACTIVE && PerformAdapter.CPU_USAGE < Config.SPEED.SPEED_CHECKER_CPU_VALUE && isSpeedDenal()) {
				backStep();
				return;
			}
			
			locX	= readH();
			locY	= readH();
			heading	= readC();
			heading &= 7;// 0 ~ 7 제한
			oriX	= locX;
			oriY	= locY;
			locX	+= HEADING_TABLE_X[heading];
			locY	+= HEADING_TABLE_Y[heading];
			
			if (pc.isFishing()) {// 낚시 중일 경우 취소 안되고 이동 가능시
				pc.finishFishing();
			}
			if (!pc.isPassiveStatus(L1PassiveId.MEDITATION_BEYOND)) {
				pc.getSkill().removeSkillEffect(L1SkillId.MEDITATION);
			}
			if (!pc.getSkill().hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {
				pc.setRegenState(REGENSTATE_MOVE);
			}
			
			if (InnTeleport.checkInn(pc, locX, locY)) {// 여관체크
				return;
			}
			String key = new StringBuilder().append(pc.getMap().getId()).append(locX).append(locY).toString();
			if (!pc._isShipIn && Dungeon.dg(key, pc)) {// 던전 입장
				key = null;
				return;
			}
			if (DungeonRandom.dg(key, pc)) {// 텔레포트처가 랜덤인 텔레포트 지점
				key = null;
				return;
			}
			key = null;
			
			L1World world = L1World.getInstance();
			// 이동할려는 좌표에 오브젝트가 있으면 뒤로 텔
			boolean ck = false;
			ArrayList<L1PcInstance> pcList = world.getVisiblePlayer(pc, 2);
			for (L1PcInstance otherPc : pcList) {
				if (otherPc.isDead() || otherPc.isGmInvis()) {
					continue;
				}
				if (otherPc.getX() == locX && otherPc.getY() == locY) {
					ck = true;
					break;
				}
			}
			pcList.clear();
			if (ck) {
				backStep();
				return;
			}
			pc.getMoveState().setHeading(heading);
			
			if (!pc.getMap().isUserPassable(oriX, oriY, heading) && !pc.isGhost()) {
				pc.getTeleport().start(pc.getLocation(), pc.getMoveState().getHeading(), false);
				return;
			}

			// TODO 좌표 설정
			pc.getMap().setPassable(pc.getLocation(), true);
			pc.getLocation().set(locX, locY);
			pc.broadcastPacket(new S_MoveCharPacket(pc), true);
			pc.getMap().setPassable(pc.getLocation(), false);
			
			pc.siegeRegionCheck(false);
			/*if (L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_PLAYING 
					&& L1HauntedHouse.getInstance().isMember(pc) 
					&& pc.getX() >= 32872 && pc.getX() <= 32875 && pc.getY() >= 32828 && pc.getY() <= 32833) {
				L1HauntedHouse.getInstance().endHauntedHouse(pc);
			}*/
			L1WorldTraps.onPlayerMoved(pc);
			world.onMoveObject(pc);
			sync.setMoveSyncInterval(pc.getAcceleratorChecker().getRightInterval(ACT_TYPE.MOVE) + currentTime + Config.SPEED.MOVE_SPEED_SYNCHRONIZED);// 액션 시간
			if (pc.isMoveDecrease()) {
				pc.send_effect(20460);// 이속 감소 디버프 이팩트
			}
			if (Config.ETC.FREE_PVP_REGION_ENABLE) {
				FreePVPRegionTable.sendPacket(pc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}
	
	/**
	 * 스피드 부정 검사
	 * @return boolean
	 */
	boolean isSpeedDenal() {
		long actionInterval = sync.getMoveSyncInterval();
		int overCount = currentTime < actionInterval ? sync.increaseMoveSpeedOverCountAndGet() : sync.decreaseMoveSpeedOverCountAndGet();
		if (overCount >= Config.SPEED.SPEED_CHECKER_OVER_COUNT) {
			//System.out.println(String.format("■■■■■ [%s] 스피드핵(MOVE) 의심 ■■■■■ 오차(%d)", pc.getName(), actionInterval - currentTime));
			System.out.println(String.format("■■■■■ [%s] Suspected Speed Hack (MOVE) ■■■■■ Deviation(%d)", pc.getName(), actionInterval - currentTime));
			//Manager.getInstance().TimeSpeed("MOVE", pc.getName(), pc); // MANAGER DISABLED
			sync.resetMoveSpeedOverCount();
			return true;
		}
		return false;
	}
	
	/**
	 * 뒤로 이동 시킨다.
	 */
	void backStep(){
		pc.getMoveState().setHeading(heading); 
		pc.sendPackets(new S_PacketBox(S_PacketBox.MOVE_BACK_STEP, pc), true);
		pc.broadcastPacket(new S_ChangeHeading(pc), true); 
	}
	
	/*boolean tebaeMove(){
		int[] loc = CrockController.getInstance().loc();
		// pc 좌표와 시간의 균열의 좌표가 일치하다면
		if (loc[0] == pc.getX() && loc[1] == pc.getY() && loc[2] == pc.getMapId()) {
			if (CrockController.getInstance().crocktype() == 0)	{
				L1Teleport.teleport(pc, 32639, 32876, (short) 780, pc.getHeading(), false);//테베
			} else {
				L1Teleport.teleport(pc, 32794, 32751, (short) 783, pc.getHeading(), false);//티칼
			}
			return true;
		}
		return false;
	}*/

}

package l1j.server.IndunSystem.occupy;

import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerFromOccupyInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.inter.S_InterGuardianTowerWarOccupyTeam;
import l1j.server.server.serverpackets.message.S_NotificationMessage;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.message.S_NotificationMessage.display_position;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.ColorUtil;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 점령전 핸들러(인터서버 전용)
 * @author LinOffice
 */
public abstract class OccupyHandler implements Runnable {
	protected static Logger _log	= Logger.getLogger(OccupyHandler.class.getName());
	protected final OccupyType TYPE;
	protected final int REWARD_ADENA_COUNT, DEFFENS_MISSION_TIME, OFFENS_MISSION_TIME, PLAY_TIME, BOSS_TIME;
	protected final FastMap<OccupyTeamType, OccupyTeam> TEAM_DATA;
	protected final short WAR_MAP, BOSS_MAP;
	
	private static final byte[] DEFAULT_RGB	= ColorUtil.getColorRgbBytes("00 ff 00");
	
	protected static final ServerBasePacket[] TIP_TOP_MESSAGES = {
//AUTO SRM: 		new S_NotificationMessage(display_position.screen_top, "Tip : 각 팀은 서버의 구분 없이 무작위로 배정됩니다. 기사단 문장으로 자신의 소속을 확인하세요.", DEFAULT_RGB, 6), new S_NotificationMessage(display_position.screen_top, "Tip : 점령전 중에는 유효한 점령지역에 있어야만 훈장을 획득할 수 있습니다.", DEFAULT_RGB, 6), new S_NotificationMessage(display_position.screen_top, "Tip : 전장 곳곳에서 상대 기사단원을 죽이면 훈장을 1개씩 빼앗을 수 있습니다.", DEFAULT_RGB, 6), new S_NotificationMessage(display_position.screen_top, "Tip : 점령지역은 중앙 수호탑 주변 빛나는 울타리 안쪽입니다. 버프탑 주변은 포함되지 않습니다.", DEFAULT_RGB, 6), new S_NotificationMessage(display_position.screen_top, "Tip : 버프탑을 점령한 기사단원에게는 강력한 버프 효과가 발휘됩니다.", DEFAULT_RGB, 6), new S_NotificationMessage(display_position.screen_top, "Tip : 최소 1개 이상의 훈장을 갖고 있어야만 승리 후 보스 지역에 입장할 수 있습니다.", DEFAULT_RGB, 6), new S_NotificationMessage(display_position.screen_top, "Tip : 훈장을 많이 갖고 있는 기사단원일수록 세금을 많이 분배받습니다.", DEFAULT_RGB, 6), new S_NotificationMessage(display_position.screen_top, "Tip : 점령전 시작 후 대기장소에 연속으로 오래 체류할 경우, 훈장이 사라지는 패널티를 받습니다.", DEFAULT_RGB, 6), new S_NotificationMessage(display_position.screen_top, "Tip : 버프탑을 마지막으로 쳐서 파괴시킨 캐릭터와 주변의 동료 기사단원들은 훈장을 획득할 수 있습니다.", DEFAULT_RGB, 6) }; // CHECKED OK
		new S_NotificationMessage(display_position.screen_top, S_SystemMessage.getRefText(39), DEFAULT_RGB, 6), 
		new S_NotificationMessage(display_position.screen_top, S_SystemMessage.getRefText(1273), DEFAULT_RGB, 6), 
		new S_NotificationMessage(display_position.screen_top, S_SystemMessage.getRefText(1274), DEFAULT_RGB, 6),
		new S_NotificationMessage(display_position.screen_top, S_SystemMessage.getRefText(1275), DEFAULT_RGB, 6), 
		new S_NotificationMessage(display_position.screen_top, S_SystemMessage.getRefText(1276), DEFAULT_RGB, 6), 
		new S_NotificationMessage(display_position.screen_top, S_SystemMessage.getRefText(1277), DEFAULT_RGB, 6), 
		new S_NotificationMessage(display_position.screen_top, S_SystemMessage.getRefText(1278), DEFAULT_RGB, 6), 
		new S_NotificationMessage(display_position.screen_top, S_SystemMessage.getRefText(1279), DEFAULT_RGB, 6), 
		new S_NotificationMessage(display_position.screen_top, S_SystemMessage.getRefText(1280), DEFAULT_RGB, 6) 
	};
	
	protected static final ServerBasePacket[] TIP_SYS_MESSAGES = {
//AUTO SRM: 		new S_SystemMessage("Tip : 각 팀은 서버의 구분 없이 무작위로 배정됩니다. 기사단 문장으로 자신의 소속을 확인하세요."), new S_SystemMessage("Tip : 점령전 중에는 유효한 점령지역에 있어야만 훈장을 획득할 수 있습니다."), new S_SystemMessage("Tip : 전장 곳곳에서 상대 기사단원을 죽이면 훈장을 1개씩 빼앗을 수 있습니다."), new S_SystemMessage("Tip : 점령지역은 중앙 수호탑 주변 빛나는 울타리 안쪽입니다. 버프탑 주변은 포함되지 않습니다."), new S_SystemMessage("Tip : 버프탑을 점령한 기사단원에게는 강력한 버프 효과가 발휘됩니다."), new S_SystemMessage("Tip : 최소 1개 이상의 훈장을 갖고 있어야만 승리 후 보스 지역에 입장할 수 있습니다."), new S_SystemMessage("Tip : 훈장을 많이 갖고 있는 기사단원일수록 세금을 많이 분배받습니다."), new S_SystemMessage("Tip : 점령전 시작 후 대기장소에 연속으로 오래 체류할 경우, 훈장이 사라지는 패널티를 받습니다."), new S_SystemMessage("Tip : 버프탑을 마지막으로 쳐서 파괴시킨 캐릭터와 주변의 동료 기사단원들은 훈장을 획득할 수 있습니다."), }; // CHECKED OK
		new S_SystemMessage(S_SystemMessage.getRefText(39), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1273), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1274), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1275), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1276), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1277), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1278), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1279), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1280), true) 
	};
	
	protected static final String STANBY_MENT	= S_SystemMessage.getRefText(325);
	protected static final String MEDAL_MENT	= S_SystemMessage.getRefText(274);
	
	protected OccupyHandler(OccupyType type){
		TYPE					= type;// 점령전 타입
		WAR_MAP					= type.getWarMap();// 진행 맵
		BOSS_MAP				= type.getBossMap();// 보스 맵
		REWARD_ADENA_COUNT		= Config.OCCUPY.OCCUPY_ADENA;// 보상 아데나
		DEFFENS_MISSION_TIME	= Config.OCCUPY.OCCUPY_DEFFENS_MISSION_TIME;// 수성 미션 시간
		OFFENS_MISSION_TIME		= Config.OCCUPY.OCCUPY_OFFENS_MISSION_TIME;// 공성 미션 시간
		PLAY_TIME				= Config.OCCUPY.OCCUPY_PLAY_TIME;// 진행시간
		BOSS_TIME				= Config.OCCUPY.OCCUPY_BOSS_TIME;// 보스시간
		TEAM_DATA				= new FastMap<OccupyTeamType, OccupyTeam>(3);// 팀의 정보
	}
	
	protected int WAIT_TIME, LIMIT_TIME;
	protected int _timer, _deffensMissionTimer, _offensMissionTimer, _teamCnt, _tipCnt;
	protected OccupyTeam _winnerTeam, _deffensMissionTeam;
	protected L1NpcInstance _boss, _bossTeleportor, _moveTeleportor;
	protected L1TowerFromOccupyInstance _mainTower, _eastTower, _westTower, _southTower, _northTower;
	protected boolean _winnerExplan;
	
	protected boolean _running;// 가동 여부
	public boolean isRunning(){
		return _running;
	}
	public void setRunning(boolean flag){
		_running = flag;
	}
	
	protected OccupyStage _stage;
	
	public boolean isBossStage(){
		return _stage == OccupyStage.BOSS;
	}

	public boolean isWinnerTeam(OccupyTeamType team){
		return _winnerTeam != null && _winnerTeam.getTeamType() == team;
	}
	
	public boolean isLargeBadge(L1PcInstance pc){
		OccupyTeam team = getTeamInfo(pc._occupyTeamType);
		return team == null ? false : team.getLargeBadgeNameList().contains(pc.getName());
	}
	
	public void compareLargeBadge(L1PcInstance pc, int badgeCount){
		if (pc._occupyTeamType == null) {
			return;
		}
		OccupyTeam team = getTeamInfo(pc._occupyTeamType);
		if (team == null) {
			return;
		}
		FastTable<String> nameList = team.getLargeBadgeNameList();
		if (nameList.contains(pc.getName())) {
			return;
		}
		if (nameList.isEmpty()) {
			nameList.add(pc.getName());
			return;
		}
		L1PcInstance user = null;
		for (String name : nameList) {
			user = L1World.getInstance().getPlayer(name);
			if (user == null) {
				continue;
			}
			Integer count = (Integer)user.getInventory().checkItemCount(team.getBadgeId());
			if (count.compareTo(badgeCount) != -1) {// 개수 비교
				continue;
			}
			nameList.remove(name);
			nameList.add(pc.getName());
			break;
		}
	}
	
	@Override
	public void run() {
		try {
			while(_running){
				try {
					timerCheck();
					procces();
				} catch(Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} finally {
					Thread.sleep(1000L);
				}
			}
			end();
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	protected abstract void procces() throws Exception;
	protected abstract boolean townLocCheck(L1PcInstance pc, OccupyTeamType teamType);
	protected abstract void towerSpawn();
	
	protected abstract OccupyTeam getLargeTeam(L1TowerFromOccupyInstance tower, boolean mainTower);
	public abstract boolean isMainTowerArea(int x, int y);
	
	protected abstract void bossTeleportSpawn();
	protected abstract void bossSpawn();
	
	protected abstract void start(int waiteMinut);
	protected abstract void end();
	
	protected void timerCheck() {
		if (!_running) {
			return;
		}
		if (--_timer <= 0) {
			_running = false;
			return;
		}
		if (_stage != OccupyStage.BOSS && _timer % 50 == 0) {
			sendTipMessage();// tip msg send
		}
		if (_stage == OccupyStage.PLAY && _timer % 60 == 0) {
			mainTowerAreaReward();// badge send
		}
		// 1410 + "25 " + 1411 + TYPE.getDesc() + STANBY_MENT
		if (WAIT_TIME > 1500 && _timer == LIMIT_TIME - 300) {
			//sendMessage("25분 후 " + TYPE.getDesc() + STANBY_MENT); // CHECKED OK
			sendMessage(S_SystemMessage.getRefText(271) + "25 " + S_SystemMessage.getRefText(272) + TYPE.getDesc() + STANBY_MENT);
		} else if (WAIT_TIME > 1200 && _timer == LIMIT_TIME - 600) {
			//sendMessage("20분 후 " + TYPE.getDesc() + STANBY_MENT); // CHECKED OK
			sendMessage(S_SystemMessage.getRefText(271) + "20 " + S_SystemMessage.getRefText(272) + TYPE.getDesc() + STANBY_MENT);
		} else if (WAIT_TIME > 900 && _timer == LIMIT_TIME - 900) {
			//sendMessage("15분 후 " + TYPE.getDesc() + STANBY_MENT); // CHECKED OK
			sendMessage(S_SystemMessage.getRefText(271) + "15 " + S_SystemMessage.getRefText(272) + TYPE.getDesc() + STANBY_MENT);
		} else if (WAIT_TIME > 600 && _timer == LIMIT_TIME - 1200) {
			//sendMessage("10분 후 " + TYPE.getDesc() + STANBY_MENT); // CHECKED OK
			sendMessage(S_SystemMessage.getRefText(271) + "10 " + S_SystemMessage.getRefText(272) + TYPE.getDesc() + STANBY_MENT);
		} else if (WAIT_TIME > 300 && _timer == LIMIT_TIME - 1500) {
			//sendMessage("5분 후 " + TYPE.getDesc() + STANBY_MENT); // CHECKED OK
			sendMessage(S_SystemMessage.getRefText(271) + "5 " + S_SystemMessage.getRefText(272) + TYPE.getDesc() + STANBY_MENT);
		} else if (WAIT_TIME > 60 && _timer == LIMIT_TIME - 1740) {
			//sendMessage("1분 후 " + TYPE.getDesc() + STANBY_MENT); // CHECKED OK
			sendMessage(S_SystemMessage.getRefText(271) + "1 " + S_SystemMessage.getRefText(272) + TYPE.getDesc() + STANBY_MENT);
		} else if (_timer == LIMIT_TIME - 1790) {
			//sendMessage("곧 " + TYPE.getDesc() + STANBY_MENT); // CHECKED OK
			sendMessage(S_SystemMessage.getRefText(232) + TYPE.getDesc() + STANBY_MENT);
		} else if (_timer == PLAY_TIME + BOSS_TIME) {// 대기 시간 종료
			System.out.println(String.format("■■■■■■■■■■ %s Guard Tower Conquest Battle: In Progress ■■■■■■■■■■", TYPE.getDesc()));
			towerSpawn(); 
			sendMessage(String.format("%s " + S_SystemMessage.getRefText(215), TYPE.getDesc())); // CHECKED OK
			_stage = OccupyStage.PLAY;
			resetTeleport();
			playTimer();
			penalty();
		} else if (_timer == BOSS_TIME) {// 40분 제한 시간 종료
			System.out.println(String.format("■■■■■■■■■■ %s Guard Tower Conquest: Boss ■■■■■■■■■■", TYPE.getDesc()));
			winnerCheck();
			bossTeleportSpawn();
			_stage = OccupyStage.BOSS;
		} else if (_timer == BOSS_TIME - 10) {
			sendMessageBoss(String.format(S_SystemMessage.getRefText(217) + "%s " + S_SystemMessage.getRefText(210), TYPE.getDesc())); // CHECKED OK
		} else if (_timer == BOSS_TIME - 40) {
			sendMessageBoss(String.format(S_SystemMessage.getRefText(211) + "%s " + S_SystemMessage.getRefText(210), TYPE.getDesc())); // CHECKED OK
		} else if (_timer == BOSS_TIME - 60) {
			sendMessageBoss(S_SystemMessage.getRefText(212));
		} else if (_timer == BOSS_TIME - 70) {
			bossSpawn();
		}
	}
	
	// 메인 타워 체크
	protected void towerCheck(){
		if (_mainTower != null && _mainTower.isDead()) {
			offens();
			towerSpawn();// 탑 새로 생성
			sendMessage(S_SystemMessage.getRefText(213));
			removeTowerBuff();
			resetTeleport();
			
			_deffensMissionTimer	= DEFFENS_MISSION_TIME;// 수성 미션 시간 세팅
			_offensMissionTimer		= OFFENS_MISSION_TIME;// 공성 미션 시간 세팅
			missionTimer();
			_winnerExplan			= false;
		} else {
			buffTowerCheck();
			deffens();
			if (!_winnerExplan && (_mainTower.getMaxHp() * 3 / 5) > _mainTower.getCurrentHp()) {
				OccupyTeam tempTeam = getLargeTeam(_mainTower, true);
				sendMessage(String.format("%s " + S_SystemMessage.getRefText(208), tempTeam.getTeamType().getDesc())); // CHECKED OK
				_winnerExplan		= true;
			}
		}
	}
	
	// 버프 타워 체크
	protected void buffTowerCheck(){
		if (_eastTower != null && _eastTower.isDead()) {
			buffTowerDestory(_eastTower, L1SkillId.TOWER_BUFF_E_CURSE, S_SystemMessage.getRefText(189), S_SystemMessage.getRefText(190));
			_eastTower = null;
		}
		if (_westTower != null && _westTower.isDead()) {
			buffTowerDestory(_westTower, L1SkillId.TOWER_BUFF_W, S_SystemMessage.getRefText(191), S_SystemMessage.getRefText(166));
			_westTower = null;
		}
		if (_southTower != null && _southTower.isDead()) {
			buffTowerDestory(_southTower, L1SkillId.TOWER_BUFF_S, S_SystemMessage.getRefText(141), null);
			_southTower = null;
		}
		if (_northTower != null && _northTower.isDead()) {
			buffTowerDestory(_northTower, L1SkillId.TOWER_BUFF_N, S_SystemMessage.getRefText(142), S_SystemMessage.getRefText(143));
			_northTower = null;
		}
	}
	
	private void buffTowerDestory(L1TowerFromOccupyInstance tower, int skillId, String msg1, String msg2){
		L1PcInstance lastAttcker = tower.getLastAttackPc();
		if (lastAttcker == null || lastAttcker._occupyTeamType == null) {
			return;
		}
		OccupyTeam buffTeam = getTeamInfo(lastAttcker._occupyTeamType);
		buffTowerReward(buffTeam, skillId);
		if (!StringUtil.isNullOrEmpty(msg1)) {
			sendMessage(buffTeam.getTeamType().getDesc() + msg1);
		}
		if (!StringUtil.isNullOrEmpty(msg2)) {
			sendMessage(buffTeam.getTeamType().getDesc() + msg2);
		}
		L1ItemInstance badge = null;
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(lastAttcker, 5)) {// 타워 주위 팀원에게 훈장 지급
			if (pc == null || pc._occupyTeamType != lastAttcker._occupyTeamType) {
				continue;
			}
			badge = OccupyUtil.reward(pc, buffTeam.getBadgeId(), 3, _timer);
			if (badge.getCount() > 5) {
				compareLargeBadge(pc, badge.getCount());
			}
			buffTeam.addPoint(1);
		}
	}
	
	// 공성
	private void offens(){
		OccupyTeam largeTeam = getLargeTeam(_mainTower, true);
		L1PcInstance pc = null;
		if (_offensMissionTimer > 0 && _deffensMissionTeam != largeTeam) {// 공성 미션 성공 보상 지급(최초 점령시 타이머는 0이기때문에 보상지급 안함)
			for (String name : largeTeam.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.isDead() || pc.getMapId() != WAR_MAP) {
					continue;
				}
				OccupyUtil.reward(pc, 6055, 1, 0);// 찬탈자의 전리품
			}
		}
		_deffensMissionTeam	= largeTeam;// 수성 미션 팀 지정
		sendMessage(String.format("%s " + S_SystemMessage.getRefText(144), _deffensMissionTeam.getTeamType().getDesc())); // CHECKED OK
		for (String name : _deffensMissionTeam.getTeamNameList()) {
			pc = L1World.getInstance().getPlayer(name);
			if (pc == null || pc.isDead() || pc.getMapId() != WAR_MAP) {
				continue;
			}
			OccupyUtil.reward(pc, 6053, 1, 0);// 전장의 전리품
		}
	}
	
	// 수성
	private void deffens(){
		if (_deffensMissionTimer > 0) {
			_deffensMissionTimer--;
			if (_deffensMissionTimer <= 0 && _deffensMissionTeam != null) {// 수성 미션 성공 보상 지급
				L1PcInstance pc = null;
				for (String name : _deffensMissionTeam.getTeamNameList()) {
					pc = L1World.getInstance().getPlayer(name);
					if (pc == null || pc.isDead() || pc.getMapId() != WAR_MAP) {
						continue;
					}
					OccupyUtil.reward(pc, 6054, 1, 0);// 수호자의 전리품
				}
			}
		}
	}
	
	// 미션 타이머
	private void missionTimer(){
		L1PcInstance pc = null;
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == null) {
				continue;
			}
			for (String name : team.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.getMapId() != WAR_MAP) {
					continue;
				}
				missionTimer(pc);
			}
		}
	}
	
	public void missionTimer(L1PcInstance pc){
		if (pc._occupyTeamType == null || _stage != OccupyStage.PLAY) {
			return;
		}
		OccupyTeam team = getTeamInfo(pc._occupyTeamType);
		if (team == null) {
			return;
		}
		if ((team == _deffensMissionTeam && _deffensMissionTimer > 0) || (team != _deffensMissionTeam && _offensMissionTimer > 0)) {
			pc.sendPackets(new S_InterGuardianTowerWarOccupyTeam(_deffensMissionTeam.getTeamType().getTeamId(), _offensMissionTimer, _deffensMissionTimer), true);// 미션 타이머
		}
	}
	
	// 최종 승리팀 체크
	protected void winnerCheck(){
		_deffensMissionTimer = _offensMissionTimer = 0;// 타이머 초기화
		_winnerTeam = _deffensMissionTeam;
		if (_winnerTeam != null) {
			sendMessage(String.format("%s " + S_SystemMessage.getRefText((138)), _winnerTeam.getTeamType().getDesc())); // CHECKED OK
			// 보상 분배
			int percentCount	= (int) (REWARD_ADENA_COUNT / _winnerTeam.getPoint());
			L1ItemInstance item	= null;
			L1PcInstance pc		= null;
			for (String name : _winnerTeam.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.getMapId() != WAR_MAP) {
					continue;
				}
				int badgeCount = pc.getInventory().checkItemCount(_winnerTeam.getBadgeId());// 훈장 개수
				if (badgeCount <= 0) {
					continue;
				}
				int rewardCount = percentCount * badgeCount;
				if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), rewardCount) != L1Inventory.OK) continue;
				item = pc.getInventory().storeItem(L1ItemId.ADENA, rewardCount);
				pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getItem().getDesc(), rewardCount)), true);
				destroyTimer(pc);
			}
			loseTeleport();
		} else {
			_running = false;// 승리팀이 없다면 종료
		}
	}
	
	// 패널티
	public void penalty(){
		if (TEAM_DATA.isEmpty()) {
			return;
		}
		L1PcInstance pc;
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == null) {
				continue;
			}
			for (String name : team.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.getMapId() != WAR_MAP) {
					continue;
				}
				penalty(pc);
			}
		}
	}
	
	public void penalty(L1PcInstance pc){
		if (_stage != OccupyStage.PLAY || pc._occupyPenaltyTimer != null) {
			return;
		}
		pc._occupyPenaltyTimer	= new OccupyPenaltyTimer(pc, true);
		pc.sendPackets(!pc._occupyPenalyFirst ? S_SpellBuffNoti.PENALTY_ON_120 : S_SpellBuffNoti.PENALTY_ON_60);
		GeneralThreadPool.getInstance().schedule(pc._occupyPenaltyTimer, (!pc._occupyPenalyFirst ? 121000L : 61000L));
	}
	
	public class OccupyPenaltyTimer implements Runnable {
		private L1PcInstance _pc;
		private boolean _active;
		@Override
		public void run() {
			try {
				if (_pc == null || !_active) {
					return;
				}
				_pc.getInventory().consumeItem(OccupyUtil.getBadgeId(_pc._occupyTeamType), 1);
				_pc._occupyPenalyFirst		= true;
				GeneralThreadPool.getInstance().schedule(_pc._occupyPenaltyTimer, 61000L);
				_pc.sendPackets(S_SpellBuffNoti.PENALTY_ON_60);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public OccupyPenaltyTimer(L1PcInstance pc, boolean active) {
			_pc		= pc;
			_active	= active;
		}
		
		public void cancel(){
			_active	= false;
		}
	}
	
	// 점령전 진행 시간
	public void playTimer(){
		if (TEAM_DATA.isEmpty()) {
			return;
		}
		L1PcInstance pc = null;
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == null) {
				continue;
			}
			for (String name : team.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.getMapId() != WAR_MAP) {
					continue;
				}
				playTimer(pc);
			}
		}
	}
	
	public void playTimer(L1PcInstance pc){
		if (_stage != OccupyStage.PLAY) {
			return;
		}
		pc.sendPackets(new S_PacketBox(S_PacketBox.TIME_COUNT, _timer - BOSS_TIME), true);
	}
	
	// 버프 탑 보상
	protected void buffTowerReward(OccupyTeam rewardTeam, int buffId){
		if (rewardTeam == null || TEAM_DATA.isEmpty()) {
			return;
		}
		switch(buffId){
		case L1SkillId.TOWER_BUFF_E_CURSE:
			buffTowerAction(rewardTeam, L1SkillId.TOWER_BUFF_E_BLESS);
			for (OccupyTeam team : TEAM_DATA.values()) {
				if (team == null || team == rewardTeam) {
					continue;
				}
				buffTowerAction(team, buffId);
			}
			break;
		case L1SkillId.TOWER_BUFF_W:
			buffTowerAction(rewardTeam, buffId);
			break;
		case L1SkillId.TOWER_BUFF_S:
			buffTowerAction(rewardTeam, buffId);
			for (L1PcInstance pc : getMainTowerAreaPcList()) {
				if (pc == null || pc.getMapId() != WAR_MAP || rewardTeam.getTeamNameList().contains(pc.getName()) || CommonUtil.nextBoolean()) {
					continue;
				}
				int[] loc = Getback.GetBack_Location(pc);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			}
			break;
		case L1SkillId.TOWER_BUFF_N:
			buffTowerAction(rewardTeam, buffId);
			GeneralThreadPool.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					try {
						L1Location loc = null;
						for (int i=0; i<20; i++) {
							loc = _mainTower.getLocation().randomLocation(14, true);
							_mainTower.broadcastPacket(new S_EffectLocation(loc.getX(), loc.getY(), 20294), true);// 미티어 이팩트
							Thread.sleep(200);
						}
						for (L1PcInstance pc : getMainTowerAreaPcList()) {
							if (pc == null || pc.getMapId() != WAR_MAP || rewardTeam.getTeamNameList().contains(pc.getName())) {
								continue;
							}
							pc.receiveDamage(pc, 500, 2);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			break;
		default:break;
		}
	}
	
	private void buffTowerAction(OccupyTeam team, int buffId){
		L1PcInstance pc;
		for (String name : team.getTeamNameList()) {
			pc = L1World.getInstance().getPlayer(name);
			if (pc == null || pc.getMapId() != WAR_MAP || townLocCheck(pc, team.getTeamType())) {
				continue;
			}
			L1BuffUtil.skillAction(pc, buffId);
		}
	}
	
	private void removeTowerBuff(){
		L1PcInstance pc;
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == null) {
				continue;
			}
			for (String name : team.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null) {
					continue;
				}
				removeTowerBuff(pc);
			}
		}
	}
	
	// 보조 타워 버프 제거
	private void removeTowerBuff(L1PcInstance pc){
		if (pc.getSkill().hasSkillEffect(L1SkillId.TOWER_BUFF_E_CURSE)) {
			pc.getSkill().removeSkillEffect(L1SkillId.TOWER_BUFF_E_CURSE);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.TOWER_BUFF_W)) {
			pc.getSkill().removeSkillEffect(L1SkillId.TOWER_BUFF_W);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.TOWER_BUFF_S)) {
			pc.getSkill().removeSkillEffect(L1SkillId.TOWER_BUFF_S);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.TOWER_BUFF_N)) {
			pc.getSkill().removeSkillEffect(L1SkillId.TOWER_BUFF_N);
		}
	}
	
	// 메인 타워 지역에 있는 유저에게 훈장 지급
	protected void mainTowerAreaReward(){
		L1ItemInstance badge			= null;
		FastTable<L1PcInstance> pcList = getMainTowerAreaPcList();
		for (L1PcInstance pc : pcList) {
			if (pc == null) {
				continue;
			}
			badge = OccupyUtil.reward(pc, OccupyUtil.getBadgeId(pc._occupyTeamType), 1, _timer);
			if (badge.getCount() > 5) {
				compareLargeBadge(pc, badge.getCount());
			}
			getTeamInfo(pc._occupyTeamType).addPoint(1);
		}
		pcList.clear();
		sendMessage(MEDAL_MENT);
	}
	
	// 메인 타워 지역에 있는 모든 유저 취득
	protected FastTable<L1PcInstance> getMainTowerAreaPcList(){
		FastTable<L1PcInstance> pcList = new FastTable<L1PcInstance>();
		L1PcInstance pc;
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == null) {
				continue;
			}
			for (String name : team.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.isDead() || pc.getMapId() != WAR_MAP || !isMainTowerArea(pc.getX(), pc.getY())) {
					continue;
				}
				pcList.add(pc);// 메인 타워 지역에 있는 유저
			}
		}
		return pcList;
	}
	
	protected void sendMessage(String msg){
		S_NotificationMessage topMsg	=	new S_NotificationMessage(display_position.screen_top, msg, DEFAULT_RGB, 6);
		S_SystemMessage sysMsg			=	new S_SystemMessage(msg, true);
		L1PcInstance pc;
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == null) {
				continue;
			}
			for (String name : team.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.getMapId() != WAR_MAP) {
					continue;
				}
				pc.sendPackets(topMsg);
				pc.sendPackets(sysMsg);
			}
		}
		topMsg.clear();
		sysMsg.clear();
		topMsg = null;
		sysMsg = null;
	}
	
	protected void sendTipMessage(){
		L1PcInstance pc;
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == null) {
				continue;
			}
			for (String name : team.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.getMapId() != WAR_MAP) {
					continue;
				}
				pc.sendPackets(TIP_TOP_MESSAGES[_tipCnt]);
				pc.sendPackets(TIP_SYS_MESSAGES[_tipCnt]);
			}
		}
		if (++_tipCnt >= TIP_TOP_MESSAGES.length) {
			_tipCnt = 0;
		}
	}
	
	protected void sendMessageBoss(String msg){
		S_NotificationMessage topMsg	=	new S_NotificationMessage(display_position.screen_top, msg, DEFAULT_RGB, 6);
		S_SystemMessage sysMsg			=	new S_SystemMessage(msg, true);
		L1PcInstance pc;
		for (String name : _winnerTeam.getTeamNameList()) {
			pc = L1World.getInstance().getPlayer(name);
			if (pc == null || !(pc.getMapId() == WAR_MAP || pc.getMapId() == BOSS_MAP)) {
				continue;
			}
			pc.sendPackets(topMsg);
			pc.sendPackets(sysMsg);
		}
		topMsg.clear();
		sysMsg.clear();
		topMsg = null;
		sysMsg = null;
	}
	
	// 시작점 이동
	protected void resetTeleport(){
		L1PcInstance pc;
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == _deffensMissionTeam) {
				continue;// 수성팀 제외
			}
			for (String name : team.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.getMapId() != WAR_MAP || townLocCheck(pc, team.getTeamType())) {
					continue;
				}
				int[] loc = Getback.GetBack_Location(pc);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			}
		}
	}
	
	// 패배 팀 텔레포트
	protected void loseTeleport(){
		L1PcInstance pc;
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == _winnerTeam) {
				continue;// 승리팀 제외
			}
			for (String name : team.getTeamNameList()) {
				pc = L1World.getInstance().getPlayer(name);
				if (pc == null || pc.getMapId() != WAR_MAP || townLocCheck(pc, team.getTeamType())) {
					continue;
				}
				int[] loc = Getback.GetBack_Location(pc);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
				destroyTimer(pc);
			}
		}
	}
	
	// 타이머 제거
	protected void destroyTimer(L1PcInstance pc){
		pc.sendPackets(new S_InterGuardianTowerWarOccupyTeam(_winnerTeam.getTeamType().getTeamId(), 0, 0), true);// 미션 타이머
		if (pc._occupyPenaltyTimer != null) {
			pc._occupyPenaltyTimer.cancel();
			pc._occupyPenaltyTimer	= null;
			pc.sendPackets(S_SpellBuffNoti.PENALTY_OFF);// 패널티 타이머
		}
	}
	
	// 모든 타워 제거
	protected void towerDelete(){
		if (_mainTower != null) {
			_mainTower.deleteMe();
			_mainTower = null;
		}
		if (_eastTower != null) {
			_eastTower.deleteMe();
			_eastTower = null;
		}
		if (_westTower != null) {
			_westTower.deleteMe();
			_westTower = null;
		}
		if (_southTower != null) {
			_southTower.deleteMe();
			_southTower = null;
		}
		if (_northTower != null) {
			_northTower.deleteMe();
			_northTower = null;
		}
	}
	
	// 팀 취득
	public OccupyTeam getTeamInfo(OccupyTeamType type){
		return TEAM_DATA.containsKey(type) ? TEAM_DATA.get(type) : null;
	}
		
	// 팀 등록
	public OccupyTeamType registTeam(L1PcInstance pc){
		if (pc._occupyTeamType != null) {
			return pc._occupyTeamType;// 이미 팀이 배정되어있을 경우 
		}
		// 전체 검사 진행
		for (OccupyTeam team : TEAM_DATA.values()) {
			if (team == null) {
				continue;
			}
			for (String name : team.getTeamNameList()) {
				if (name.equals(pc.getName())) {
					return team.getTeamType();
				}
			}
		}
		OccupyTeamType teamType;
		switch(_teamCnt){
		case 0:teamType = OccupyTeamType.RED_KNIGHT;break;
		case 1:teamType = OccupyTeamType.GOLD_KNIGHT;break;
		case 2:teamType = OccupyTeamType.BLACK_KNIGHT;break;
		default:return null;
		}
		OccupyTeam teamInfo = TEAM_DATA.get(teamType);
		if (teamInfo == null) {
			teamInfo = new OccupyTeam(teamType, 0, new FastTable<String>(), new FastTable<String>(3));
			TEAM_DATA.put(teamType, teamInfo);
		}
		teamInfo.getTeamNameList().add(pc.getName());// 등록
		if (++_teamCnt > 2) {
			_teamCnt = 0;
		}
		return teamType;
	}
		
	// 팀 취소
	public void cancelTeam(L1PcInstance pc){
		if (pc == null || pc._occupyTeamType == null) {
			return;
		}
		OccupyTeam teamInfo = TEAM_DATA.get(pc._occupyTeamType);
		if (teamInfo != null && teamInfo.getTeamNameList() != null && teamInfo.getTeamNameList().contains(pc.getName())) {
			teamInfo.getTeamNameList().remove(pc.getName());// 취소
		}
		pc._occupyTeamType		= null;
		if (pc._occupyPenaltyTimer != null) {
			pc._occupyPenaltyTimer.cancel();
			pc._occupyPenaltyTimer	= null;
		}
		removeTowerBuff(pc);
	}
	
	// 메모리 정리
	protected void dispose(){
		if (_boss != null) {
			_boss.deleteMe();
			_boss = null;
		}
		if (_bossTeleportor != null) {
			_bossTeleportor.deleteMe();
			_bossTeleportor = null;
		}
		if (_moveTeleportor != null) {
			_moveTeleportor.deleteMe();
			_moveTeleportor = null;
		}
		towerDelete();
		if (!TEAM_DATA.isEmpty()) {
			for (OccupyTeam info : TEAM_DATA.values()) {
				info.dispose();
				info = null;
			}
			TEAM_DATA.clear();
		}
		_winnerTeam = _deffensMissionTeam = null;
		OccupyManager.getInstance().removeHandler(TYPE);
	}
}




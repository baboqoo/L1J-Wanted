package l1j.server.IndunSystem.occupy.action;

import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyStage;
import l1j.server.IndunSystem.occupy.OccupyTeam;
import l1j.server.IndunSystem.occupy.OccupyTeamType;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1NotificationMessege;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerFromOccupyInstance;
import l1j.server.server.serverpackets.message.S_Notification;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 윈다우드 점령전
 * @author LinOffice
 */
public class Windawood extends OccupyHandler {
	private static final int[][][] MAIN_TOWER_LOC = {
		{ {32742, 32795}, {32757, 32810} },
		{ {32758, 32800}, {32767, 32805} },
		{ {32747, 32811}, {32752, 32820} },
		{ {32732, 32800}, {32741, 32805} },
		{ {32747, 32785}, {32752, 32794} },
	};
	
	private static final int[][][] BUFF_TOWER_LOC = { 
		{ {32772, 32825}, {32781, 32834} },// east
		{ {32718, 32771}, {32727, 32780} },// west
		{ {32718, 32825}, {32727, 32834} },// south
		{ {32772, 32771}, {32781, 32780} }// north
	};
	
	private static final int[][][] TOWN_LOC = {
		{ {32659, 32819}, {32690, 32875} },// 붉은 기사단 진영
		{ {32656, 32735}, {32692, 32788} },// 검은 기사단 진영
		{ {32603, 32789}, {32656, 32822} },// 황금 성단 진영
	};
	
	public Windawood(OccupyType type) {// 생성자 추상클래스 상속
		super(type);
	}

	@Override
	protected void procces() throws Exception {
		switch(_stage){
		case WAIT:break;
		case PLAY:towerCheck();break;
		case BOSS:
			if (_boss != null && _boss.isDead()) {// 보스 처치
				Thread.sleep(20000L);
				_running = false;
			}
			break;
		}
	}

	@Override
	protected void towerSpawn() {
		towerDelete();
		_mainTower	= (L1TowerFromOccupyInstance) L1SpawnUtil.spawn(32750, 32802, WAR_MAP, 5, 800822, 0, _timer * 1000);
		_eastTower	= (L1TowerFromOccupyInstance) L1SpawnUtil.spawn(32777, 32829, WAR_MAP, 5, 800823, 0, _timer * 1000);
		_westTower	= (L1TowerFromOccupyInstance) L1SpawnUtil.spawn(32723, 32775, WAR_MAP, 5, 800824, 0, _timer * 1000);
		_southTower	= (L1TowerFromOccupyInstance) L1SpawnUtil.spawn(32723, 32829, WAR_MAP, 5, 800825, 0, _timer * 1000);
		_northTower	= (L1TowerFromOccupyInstance) L1SpawnUtil.spawn(32777, 32775, WAR_MAP, 5, 800826, 0, _timer * 1000);
	}
	
	@Override
	protected OccupyTeam getLargeTeam(L1TowerFromOccupyInstance tower, boolean mainTower) {
		int redCnt = 0, blackCnt = 0, goldCnt = 0;
		if (mainTower) {
			L1PcInstance pc;
			for (OccupyTeam team : TEAM_DATA.values()) {
				for (String name : team.getTeamNameList()) {
					pc = L1World.getInstance().getPlayer(name);
					if (pc == null || pc.getMapId() != WAR_MAP) {
						continue;
					}
					if (isMainTowerArea(pc.getX(), pc.getY())) {
						switch(pc._occupyTeamType){
						case RED_KNIGHT:	redCnt++;break;
						case GOLD_KNIGHT:	goldCnt++;break;
						case BLACK_KNIGHT:	blackCnt++;break;
						}
					}
				}
			}
		} else {
			int[][] buffLoc = tower == _eastTower ? BUFF_TOWER_LOC[0] : tower == _westTower ? BUFF_TOWER_LOC[1] : tower == _southTower ? BUFF_TOWER_LOC[2] : BUFF_TOWER_LOC[3];
			L1PcInstance pc;
			for (OccupyTeam team : TEAM_DATA.values()) {
				for (String name : team.getTeamNameList()) {
					pc = L1World.getInstance().getPlayer(name);
					if (pc == null || pc.getMapId() != WAR_MAP) {
						continue;
					}
					int x = pc.getX(), y = pc.getY();
					if (x >= buffLoc[0][0] && x <= buffLoc[1][0] && y >= buffLoc[0][1] && y <= buffLoc[1][1]) {
						switch(pc._occupyTeamType){
						case RED_KNIGHT:	redCnt++;break;
						case GOLD_KNIGHT:	goldCnt++;break;
						case BLACK_KNIGHT:	blackCnt++;break;
						}
					}
				}
			}
		}
		
		// 승리팀 비교
		if (redCnt > blackCnt && redCnt > goldCnt) {
			return TEAM_DATA.get(OccupyTeamType.RED_KNIGHT);
		}
		if (goldCnt > redCnt && goldCnt > blackCnt) {
			return TEAM_DATA.get(OccupyTeamType.GOLD_KNIGHT);
		}
		if (blackCnt > redCnt && blackCnt > goldCnt) {
			return TEAM_DATA.get(OccupyTeamType.BLACK_KNIGHT);
		}
		
		// 승리팀 임의 선택
		OccupyTeam[] teamArray = TEAM_DATA.values().toArray(new OccupyTeam[TEAM_DATA.size()]);
		return teamArray[CommonUtil.random(teamArray.length)];
	}
	
	@Override
	public boolean isMainTowerArea(int x, int y) {
		return (x >= MAIN_TOWER_LOC[0][0][0] && x <= MAIN_TOWER_LOC[0][1][0] && y >= MAIN_TOWER_LOC[0][0][1] && y <= MAIN_TOWER_LOC[0][1][1]) 
				|| (x >= MAIN_TOWER_LOC[1][0][0] && x <= MAIN_TOWER_LOC[1][1][0] && y >= MAIN_TOWER_LOC[1][0][1] && y <= MAIN_TOWER_LOC[1][1][1])
				|| (x >= MAIN_TOWER_LOC[2][0][0] && x <= MAIN_TOWER_LOC[2][1][0] && y >= MAIN_TOWER_LOC[2][0][1] && y <= MAIN_TOWER_LOC[2][1][1])
				|| (x >= MAIN_TOWER_LOC[3][0][0] && x <= MAIN_TOWER_LOC[3][1][0] && y >= MAIN_TOWER_LOC[3][0][1] && y <= MAIN_TOWER_LOC[3][1][1])
				|| (x >= MAIN_TOWER_LOC[4][0][0] && x <= MAIN_TOWER_LOC[4][1][0] && y >= MAIN_TOWER_LOC[4][0][1] && y <= MAIN_TOWER_LOC[4][1][1]);
	}

	@Override
	protected void start(int waiteMinut) {
		WAIT_TIME		= waiteMinut * 60;// 대기시간 설정
		LIMIT_TIME		= WAIT_TIME + PLAY_TIME + BOSS_TIME;// 총 시간 설정
		_timer			= LIMIT_TIME;
		_stage			= OccupyStage.WAIT;
		_running		= true;
		_winnerExplan	= false;
		_winnerTeam	= _deffensMissionTeam = null;
		_deffensMissionTimer = _offensMissionTimer = _teamCnt = _tipCnt = 0;
		System.out.println(String.format("■■■■■■■■■■ %s Guard Tower Conquest: Open (waiting for %d minutes) ■■■■■■■■■■■", TYPE.getDesc( ), waiteMinut));
		_moveTeleportor = L1SpawnUtil.spawn(32605, 33423, (short)4, 6, 800818, 0, _timer * 1000);// 미지의 균열
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1NotificationMessege.OCCUPY_WINDAWOOD_START);// 윈다우드 성을 삼겨버린 절벽 아래에 균열의 소용돌이가 생성되었습니다. 점령전이 시작되었습니다.
		world.broadcastPacketToAll(S_Notification.OCCUPY_WINDAWOOD_TOWER_ON);//	알람
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	protected void end() {
		endTeleport();
		dispose();
		System.out.println(String.format("■■■■■■■■■■ %s Guard Tower Conquest War: End ■■■■■■■■■■", TYPE.getDesc()));
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1NotificationMessege.OCCUPY_WINDAWOOD_END);// 윈다우드 점령전이 종료되었습니다. 균열이 절벽 아래로 모습을 감추었습니다.
		world.broadcastPacketToAll(S_Notification.OCCUPY_WINDAWOOD_TOWER_OFF);//	알람
	}
	
	@Override
	protected boolean townLocCheck(L1PcInstance pc, OccupyTeamType teamType){
		int[][] townLoc = teamType == OccupyTeamType.RED_KNIGHT ? TOWN_LOC[0] : teamType == OccupyTeamType.BLACK_KNIGHT ? TOWN_LOC[1] : TOWN_LOC[2];
		return pc.getX() >= townLoc[0][0] && pc.getX() <= townLoc[1][0] && pc.getY() >= townLoc[0][1] && pc.getY() <= townLoc[1][1];
	}
	
	// 보스 텔레포터 스폰
	@Override
	protected void bossTeleportSpawn(){
		_bossTeleportor = L1SpawnUtil.spawn(32781, 32803, WAR_MAP, 5, 800827, 0, _timer * 1000);
		//sendMessage(String.format("전장에 %s 성의 안마당으로 향하는 포탈이 생성되었습니다.", TYPE.getDesc())); // CHECKED OK
		sendMessage(String.format(S_SystemMessage.getRefText(139) + "%s " + S_SystemMessage.getRefText(132), TYPE.getDesc()));
		//sendMessage(String.format("%s은 포탈로 입장하여 마지막 보스를 쓰러뜨려 주세요.", _winnerTeam.getTeamType().getDesc())); // CHECKED OK
		if (_winnerTeam	!= null)
			sendMessage(String.format("%s " + S_SystemMessage.getRefText(133), _winnerTeam.getTeamType().getDesc()));
	}
	
	// 보스 스폰
	@Override
	protected void bossSpawn(){
		_boss = L1SpawnUtil.spawn(32768, 32834, BOSS_MAP, 5, 800552, 0, _timer * 1000);// 윈다우드 성주 아주르
		//sendMessageBoss(String.format("%s가 등장하였습니다.", _boss.getName())); // CHECKED OK
		sendMessageBoss(String.format("%s " + S_SystemMessage.getRefText(134), _boss.getName()));		
	}

	// 종료 텔레포트
	private void endTeleport(){
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
				if (pc == null || !(pc.getMapId() == WAR_MAP || pc.getMapId() == BOSS_MAP)) {
					continue;
				}
				cancelTeam(pc);
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_WINDAWOOD);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
			}
		}
	}
}


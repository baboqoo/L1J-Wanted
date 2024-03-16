package l1j.server.server.controller.action;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.sql.Timestamp;
import java.util.Calendar;

import l1j.server.Config;
import l1j.server.GameSystem.ai.AiManager;
import l1j.server.GameSystem.ai.constuct.AiArea;
import l1j.server.IndunSystem.antqueen.AntQueen;
import l1j.server.IndunSystem.treasureisland.TreasureIsland;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.message.S_Notification;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 특화 던전 컨트롤러
 * @author LinOffice
 */
public class SpecialDungeon implements ControllerInterface {
	private static class newInstance {
		public static final SpecialDungeon INSTANCE = new SpecialDungeon();
	}
	public static SpecialDungeon getInstance() {
		return newInstance.INSTANCE;
	}
	private SpecialDungeon(){
		serverStartOpen();
	}
	
	private int _omanCrackOpenHours, _omanCrackOpenMinute, _omanCrackLimit;
	private Timestamp _omanCrackDay;
	private boolean _omanCrackOpen;
	
	private int _cloneWarHours;
	private Timestamp _cloneWarDay;
	
	@Override
	public void execute() {
		isCheck();
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	@SuppressWarnings("deprecation")
	private void isCheck(){
		try{
			//System.out.println("SpecialDungeon.IsCheck");		
			Calendar cal	= Calendar.getInstance();
			int day			= cal.get(Calendar.DAY_OF_WEEK) - 1;
			String today	= CommonUtil.WEEK_DAY_ARRAY[day];
			int date		= cal.get(Calendar.DATE);
			int hour		= cal.get(Calendar.HOUR_OF_DAY);
			int minute		= cal.get(Calendar.MINUTE);
			if (Config.DUNGEON.DEVIL_KING_ACTIVE && !GameServerSetting.DEVIL_ZONE && hour == Config.DUNGEON.DEVIL_KING_OPEN_HOUR && minute == 0) {
				devilOpen();
			}
			if (Config.DUNGEON.ISLAND_ACTIVE && !GameServerSetting.FORGOTTEN_ISLAND && hour == Config.DUNGEON.ISLAND_OPEN_HOUR && minute == 0 && today.matches(Config.DUNGEON.ISLAND_DAY_REGEX)) {
				forgottenIslandOpen();
			}
			if (Config.DUNGEON.ISLAND_LOCAL_ACTIVE && !GameServerSetting.FORGOTTEN_ISLAND_LOCAL && hour >= Config.DUNGEON.ISLAND_OPEN_HOUR_LOCAL && hour < Config.DUNGEON.ISLAND_CLOSE_HOUR_LOCAL && today.matches(Config.DUNGEON.ISLAND_DAY_LOCAL_REGEX)) {
				//System.out.println("Let's go to start forgotten island");		
				forgottenIslandLocalOpen();
			} /*else {
			  if (GameServerSetting.FORGOTTEN_ISLAND_LOCAL)
			    System.out.println("We can not start fi becouse is already started");		
			  else {
				Boolean matches = today.matches(Config.DUNGEON.ISLAND_DAY_LOCAL_REGEX);
				if (matches)
			  	  System.out.println("We can not start fi becouse the time is not correct. Actual hour: " + hour + " ini hour: " + Config.DUNGEON.ISLAND_OPEN_HOUR_LOCAL + " final hour" + Config.DUNGEON.ISLAND_CLOSE_HOUR_LOCAL);		
				else 
				  System.out.println("We can not start fi becouse the time is not correct. Date don't match. Today " + today + " Island REGEX " + Config.DUNGEON.ISLAND_DAY_LOCAL_REGEX + " Hour: " + hour + " ini hour: " + Config.DUNGEON.ISLAND_OPEN_HOUR_LOCAL + " final hour " + Config.DUNGEON.ISLAND_CLOSE_HOUR_LOCAL);		
			  }
			}*/
			if (!GameServerSetting.DOMINATION_TOWER && hour == Config.DUNGEON.DOMINATION_TOWER_OPEN_HOUR && minute == 0) {
				dominationOpen();
			}
			if (!GameServerSetting.BLACK_DRAGON && hour == Config.DUNGEON.BLACK_DRAGON_DUNGEON_OPEN_HOUR && minute == 0) {
				blackDragonOpen();
			}
			if (!AntQueen.getInstance().isRunning() 
					&& (StringUtil.isNullOrEmpty(Config.DUNGEON.ANT_QUEEN_DAY) || today.equalsIgnoreCase(Config.DUNGEON.ANT_QUEEN_DAY)) 
					&& hour == Config.DUNGEON.ANT_QUEEN_OPEN_HOUR && minute == Config.DUNGEON.ANT_QUEEN_OPEN_MINUT) {
				AntQueen.getInstance().startAnt();
			}
			if (Config.DUNGEON.OMAN_CRACK && !_omanCrackOpen && (_omanCrackDay == null || (_omanCrackDay != null && _omanCrackDay.getDate() != date))) {// 1일 1번
				if (_omanCrackOpenHours == 0) {// 19시 15분 ~ 23시 50분 사이 셋팅
					_omanCrackOpenHours		= (int)(Math.random() * 5) + 19;// 시간 세팅
					_omanCrackOpenMinute	= _omanCrackOpenHours == 19 ? (int)(Math.random() * 45) + 15 : _omanCrackOpenHours == 23 ? (int)(Math.random() * 51) : (int)(Math.random() * 61);// 분 세팅
				}
				if (hour == _omanCrackOpenHours && minute == _omanCrackOpenMinute) {
					omanCrackOpen();
				}
			}
			if (Config.DUNGEON.CLONE_WAR_ACTIVE && !GameServerSetting.CLONE_WAR && (_cloneWarDay == null || (_cloneWarDay != null && _cloneWarDay.getDate() != date))) {
				if (_cloneWarHours == 0) {
					_cloneWarHours = (int)(Math.random() * Config.DUNGEON.CLONE_WAR_TIME[1]) + Config.DUNGEON.CLONE_WAR_TIME[0];
				}
				if (hour == _cloneWarHours) {
					cloneWarOpen();
				}
			}
			if (Config.DUNGEON.TREASURE_ISLAND_ACTIVE && hour == Config.DUNGEON.TREASURE_ISLAND_OPEN_HOUR && minute == Config.DUNGEON.TREASURE_ISLAND_OPEN_MINUT && !TreasureIsland.getInstance().isActive()) {
				TreasureIsland.getInstance().start();
			}
			
			if (GameServerSetting.DEVIL_ZONE && hour == Config.DUNGEON.DEVIL_KING_CLOSE_HOUR && minute == 0) {
				devilEnd();
			}
			if (GameServerSetting.DOMINATION_TOWER && hour == Config.DUNGEON.DOMINATION_TOWER_CLOSE_HOUR && minute == 0) {
				dominationEnd();
			}
			if (GameServerSetting.BLACK_DRAGON && hour == Config.DUNGEON.BLACK_DRAGON_DUNGEON_CLOSE_HOUR && minute == 0) {
				blackDragonEnd();
			}
			if (GameServerSetting.FORGOTTEN_ISLAND && hour == Config.DUNGEON.ISLAND_CLOSE_HOUR && minute == 0) {
				forgottenIslandEnd();
			}
			if (GameServerSetting.FORGOTTEN_ISLAND_LOCAL && hour == Config.DUNGEON.ISLAND_CLOSE_HOUR_LOCAL && minute == 0) {
				forgottenIslandLocalEnd();
			}
			if (_omanCrackOpen) {
				decreaseOmanCrackTimer();
			}
			if (GameServerSetting.CLONE_WAR) {
				cloneWarCheck();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void serverStartOpen(){// 서버 가동시 오픈시간에 포함될 시 오픈처리
		Calendar cal	= Calendar.getInstance();
		int day			= cal.get(Calendar.DAY_OF_WEEK) - 1;
		String today	= CommonUtil.WEEK_DAY_ARRAY[day];
		int hour		= cal.get(Calendar.HOUR_OF_DAY);
		int minute		= cal.get(Calendar.MINUTE);
		
		if (Config.DUNGEON.DEVIL_KING_ACTIVE && !GameServerSetting.DEVIL_ZONE && hour >= Config.DUNGEON.DEVIL_KING_OPEN_HOUR && hour < Config.DUNGEON.DEVIL_KING_CLOSE_HOUR) {
			devilOpen();
		}
		if (Config.DUNGEON.ISLAND_ACTIVE && !GameServerSetting.FORGOTTEN_ISLAND && hour >= Config.DUNGEON.ISLAND_OPEN_HOUR && hour < Config.DUNGEON.ISLAND_CLOSE_HOUR && today.matches(Config.DUNGEON.ISLAND_DAY_REGEX)) {
			forgottenIslandOpen();
		}
		if (Config.DUNGEON.ISLAND_LOCAL_ACTIVE && !GameServerSetting.FORGOTTEN_ISLAND_LOCAL && hour >= Config.DUNGEON.ISLAND_OPEN_HOUR_LOCAL && hour < Config.DUNGEON.ISLAND_CLOSE_HOUR_LOCAL && today.matches(Config.DUNGEON.ISLAND_DAY_LOCAL_REGEX)) {
			forgottenIslandLocalOpen();
		}
		if (!GameServerSetting.DOMINATION_TOWER && hour >= Config.DUNGEON.DOMINATION_TOWER_OPEN_HOUR && hour < Config.DUNGEON.DOMINATION_TOWER_CLOSE_HOUR) {
			dominationOpen();
		}
		if (!GameServerSetting.BLACK_DRAGON && hour >= Config.DUNGEON.BLACK_DRAGON_DUNGEON_OPEN_HOUR && hour < Config.DUNGEON.BLACK_DRAGON_DUNGEON_CLOSE_HOUR) {
			blackDragonOpen();
		}
		if (!AntQueen.getInstance().isRunning() 
				&& (StringUtil.isNullOrEmpty(Config.DUNGEON.ANT_QUEEN_DAY) || today.equalsIgnoreCase(Config.DUNGEON.ANT_QUEEN_DAY)) 
				&& hour == Config.DUNGEON.ANT_QUEEN_OPEN_HOUR && minute >= Config.DUNGEON.ANT_QUEEN_OPEN_MINUT) {
			AntQueen.getInstance().startAnt();
		}
		if (Config.DUNGEON.TREASURE_ISLAND_ACTIVE){
			int treasureHour		= Config.DUNGEON.TREASURE_ISLAND_OPEN_HOUR;
			int treasureHourEnd		= Config.DUNGEON.TREASURE_ISLAND_OPEN_HOUR;
			int treasureMinut		= Config.DUNGEON.TREASURE_ISLAND_OPEN_MINUT;
			int treasureMinutEnd	= treasureMinut + Config.DUNGEON.TREASURE_ISLAND_DURATION - 1;
			if (treasureMinutEnd >= 60) {
				treasureHourEnd += 1;
				treasureMinutEnd -= treasureMinut - 60;
			}
			if (hour >= treasureHour && hour <= treasureHourEnd && minute >= treasureMinut && minute <= treasureMinutEnd && !TreasureIsland.getInstance().isActive()) {
				TreasureIsland.getInstance().start();
			}
		}
	}
	
	public void omanCrackOpen(){
		_omanCrackOpen = true;
		if (_omanCrackDay == null) {
			_omanCrackDay	= new Timestamp(System.currentTimeMillis());
		} else {
			_omanCrackDay.setTime(System.currentTimeMillis());
		}
		_omanCrackLimit					= Config.DUNGEON.OMAN_CRACK_DURATION;
		int omanMapId					= (int)(Math.random() * 10) + 1;// 1~10층
		GameServerSetting.OMAN_CRACK	= (short)(omanMapId + 100);
		L1World world					= L1World.getInstance();
//AUTO SRM: 		world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, new StringBuilder().append("오만의 탑 ").append(omanMapId).append("층에 수상한 그림자가 드리우기 시작합니다.").toString()), true); // CHECKED OK
		world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,new StringBuilder().append(S_SystemMessage.getRefText(907)).append(omanMapId).append(S_SystemMessage.getRefText(908)).toString()), true);
		world.broadcastPacketToAll(new S_Notification(26, (int)GameServerSetting.OMAN_CRACK, true), true);	//	알람
		S_SceneNoti scene = new S_SceneNoti(Integer.toString((int)GameServerSetting.OMAN_CRACK), true, false);
		for (L1PcInstance pc : world.getMapPlayer((int)GameServerSetting.OMAN_CRACK)) {
			pc.sendPackets(scene);
		}
		scene.clear();
		scene = null;
	}
	
	private void decreaseOmanCrackTimer(){
		_omanCrackLimit--;
		if (_omanCrackLimit == 20) {
			razanusSpawn();
		} else if (_omanCrackLimit <= 0) {
			omanCrackEnd();
		}
	}
	
	private void razanusSpawn(){
		int x=0, y=0;
		switch(GameServerSetting.OMAN_CRACK){
		case 101:x=32795;y=32799;break;
		case 102:x=32796;y=32797;break;
		case 103:x=32796;y=32797;break;
		case 104:x=32666;y=32862;break;
		case 105:x=32668;y=32862;break;
		case 106:x=32685;y=32846;break;
		case 107:x=32667;y=32862;break;
		case 108:x=32668;y=32861;break;
		case 109:x=32668;y=32862;break;
		case 110:x=32796;y=32799;break;
		}
		L1Location loc = new L1Location(x, y, (int)GameServerSetting.OMAN_CRACK).randomLocation(20, true);
		L1SpawnUtil.spawn2(loc.getX(), loc.getY(), (short)loc.getMapId(), 5, 800220, 5, 3600000, 0);// 라자루스 스폰
		loc = null;
		for (L1PcInstance pc : L1World.getInstance().getMapPlayer((int)GameServerSetting.OMAN_CRACK)) {
			pc.sendPackets(L1SystemMessage.RAZARUS_SPAWN);
			pc.sendPackets(L1GreenMessage.RAZARUS_SPAWN);
		}
	}
	
	public void omanCrackEnd(){
		_omanCrackOpen = false;
		// 다음 오픈시간 설정
		_omanCrackOpenHours		= (int)(Math.random() * 5) + 19;// 시간 세팅
		_omanCrackOpenMinute	= _omanCrackOpenHours == 19 ? (int)(Math.random() * 45) + 15 : _omanCrackOpenHours == 23 ? (int)(Math.random() * 51) : (int)(Math.random() * 61);// 분 세팅
		
		L1World world			= L1World.getInstance();
		L1NpcInstance razanus	= world.findNpc(800220);
		if (razanus != null && !razanus.isDead()) {
			razanus.deleteMe();
		}
		world.broadcastPacketToAll(L1GreenMessage.OMAN_CRACK_END_MENT);// 오만의 탑으로부터 수상한 그림자가 달아났습니다.
		world.broadcastPacketToAll(new S_Notification(26, 0, false), true);// 알람
		S_SceneNoti scene = new S_SceneNoti(Integer.toString((int)GameServerSetting.OMAN_CRACK), false, false);
		for (L1PcInstance pc : world.getMapPlayer((int)GameServerSetting.OMAN_CRACK)) {
			pc.sendPackets(scene);
		}
		scene.clear();
		scene		= null;
		GameServerSetting.OMAN_CRACK	= (short)0;
	}
	
	public void cloneWarOpen(){
		GameServerSetting.CLONE_WAR = true;
		if (_cloneWarDay == null) {
			_cloneWarDay	= new Timestamp(System.currentTimeMillis());
		} else {
			_cloneWarDay.setTime(System.currentTimeMillis());
		}
		AiManager.getInstance().timeStart(AiArea.GIKAM_SECOND_FLOOR);
	}
	
	private void cloneWarCheck(){
		if (AiManager.getInstance().getHandler(AiArea.GIKAM_SECOND_FLOOR) == null) {
			cloneWarEnd();
		}
	}
	
	private void cloneWarEnd(){
		GameServerSetting.CLONE_WAR = false;
		// 다음 오픈시간 설정
		_cloneWarHours = (int)(Math.random() * 24);
	}
	
	public void devilOpen(){
		GameServerSetting.DEVIL_ZONE = true;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.DEVIL_OPEN_MENT);
		world.broadcastPacketToAll(L1SystemMessage.DEVIL_OPEN);
	}
	
	public void devilEnd(){
		GameServerSetting.DEVIL_ZONE = false;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.DEVIL_END_MENT);
		world.broadcastPacketToAll(L1SystemMessage.DEVIL_END);
		for (L1PcInstance pc : world.getMapPlayer(5167)) {
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		}
	}
	
	public void forgottenIslandOpen(){
		GameServerSetting.FORGOTTEN_ISLAND = true;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.ISLAND_OPEN_MENT);
		world.broadcastPacketToAll(L1SystemMessage.ISLAND_OPEN);
	}
	
	public void forgottenIslandEnd(){
		GameServerSetting.FORGOTTEN_ISLAND = false;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.ISLAND_END_MENT);
		world.broadcastPacketToAll(L1SystemMessage.ISLAND_END);
		for (L1PcInstance pc : world.getAllPlayers()) {
		    if (pc.getMap().getInter() == L1InterServer.FORGOTTEN_ISLAND) {
		        int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HEINE);
		        pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
		    }
	    }
	}
	
	public void forgottenIslandLocalOpen(){
		System.out.println("■■■■■■■■■■ Forgotten Island Local begins ■■■■■■■■■■ ");
		GameServerSetting.FORGOTTEN_ISLAND_LOCAL = true;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.ISLAND_OPEN_MENT);
		world.broadcastPacketToAll(L1SystemMessage.ISLAND_OPEN);		
	}
	
	public void forgottenIslandLocalEnd(){
		System.out.println("■■■■■■■■■■ Forgotten Island Local ends ■■■■■■■■■■ ");	
		GameServerSetting.FORGOTTEN_ISLAND_LOCAL = false;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.ISLAND_END_MENT);
		world.broadcastPacketToAll(L1SystemMessage.ISLAND_END);
		for (L1PcInstance pc : world.getMapPlayer(70)) {
			if (pc == null) {
				continue;
			}
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HEINE);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
	    }
	}
	
	public void dominationOpen(){
		GameServerSetting.DOMINATION_TOWER = true;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.DOMINATION_OPEN_MENT);
		world.broadcastPacketToAll(L1SystemMessage.DOMINATION_OPEN);
	}
	    
	public void dominationEnd() {
		GameServerSetting.DOMINATION_TOWER = false;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.DOMINATION_END_MENT);
		world.broadcastPacketToAll(L1SystemMessage.DOMINATION_END);
		for (L1PcInstance pc : world.getAllPlayers()) {
			if (pc.getMap().getInter() == L1InterServer.DOMINATION_TOWER) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_ADEN);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
			}
		}
	}
	
	public void blackDragonOpen(){
		GameServerSetting.BLACK_DRAGON = true;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.BLACK_DRAGON_OPEN_MENT);
		world.broadcastPacketToAll(L1SystemMessage.BLACK_DRAGON_OPEN);
	}
	    
	public void blackDragonEnd() {
		GameServerSetting.BLACK_DRAGON = false;
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(L1GreenMessage.BLACK_DRAGON_END_MENT);
		world.broadcastPacketToAll(L1SystemMessage.BLACK_DRAGON_END);
		for (L1PcInstance pc : world.getAllPlayers()) {
			if (pc.getMapId() >= 1318 && pc.getMapId() <= 1319) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			}
		}
	}

}



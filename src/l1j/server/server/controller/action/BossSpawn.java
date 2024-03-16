package l1j.server.server.controller.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.IndunSystem.dragonraid.DragonRaidCreator;
import l1j.server.IndunSystem.dragonraid.DragonRaildType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.BossSpawnTable;
import l1j.server.server.datatables.BossSpawnTable.BossMentType;
import l1j.server.server.datatables.BossSpawnTable.BossSign;
import l1j.server.server.datatables.BossSpawnTable.BossTemp;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1MobGroupSpawn;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;
import l1j.server.server.utils.L1SpawnUtil;
//import manager.Manager;  // MANAGER DISABLED

/**
 * 보스 스폰 처리 컨트롤러
 * @author LinOffice
 */
public class BossSpawn implements ControllerInterface {
	private static final Random random = new Random(System.nanoTime());
	private static Logger _log = Logger.getLogger(BossSpawn.class.getName());
	
	public static boolean _sasinSoulLeft, _sasinSoulRight, _erzabeRun, _sandwarmRun, _taros;
	public static boolean _wakeZenith, _wakeSeia, _wakeBampha, _wakeZombie, _wakeCouger, _wakeMumy, _wakeIris, _wakeKnight, _wakeRich, _wakeUgu, _wakeSasin;
	private L1World world			= L1World.getInstance();
	private L1EffectSpawn effect	= L1EffectSpawn.getInstance();
	private static ConcurrentHashMap<Integer, L1NpcInstance> alives;
	
	public static void putAliveBoss(int key, L1NpcInstance val) {
		alives.put(key, val);
	}
	
	public static boolean isAliveBoss(int bossId) {
		L1NpcInstance boss = alives.get(bossId);
		return boss != null && !boss.isDead() && !boss._destroyed;
	}
	
	public static L1NpcInstance getAliveBoss(int bossId) {
		L1NpcInstance boss = alives.get(bossId);
		if (boss == null || boss.isDead() || boss._destroyed) {
			return null;
		}
		return boss;
	}
	
	private static class newInstance {
		public static final BossSpawn INSTANCE = new BossSpawn();
	}
	public static BossSpawn getInstance() {
		return newInstance.INSTANCE;
	}
	private BossSpawn(){
		alives = new ConcurrentHashMap<>();
	}

	@Override
	public void execute() {
		try {
			timeCheck();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	void timeCheck() {
		Calendar cal	= Calendar.getInstance();
		int day			= cal.get(Calendar.DAY_OF_WEEK);
		int hour		= cal.get(Calendar.HOUR_OF_DAY);
		int minute		= cal.get(Calendar.MINUTE);
		for (BossTemp temp : BossSpawnTable.getlist()) {
			// 생존 여부 검사
			L1NpcInstance boss = alives.get(temp.npcid);
			if (boss != null && !boss.isDead() && !boss._destroyed && boss.getMapId() == temp.spawnLoc[2]) {
				continue;
			}
			
			boolean isDay = false;
			for (int d : temp.spawnDay) {
				if (d == day) {
					isDay = true;
					break;
				}
			}
			if (!isDay) {
				continue;
			}
			
			if (temp.isSpawn) {// 스폰상태 체크
				boolean ck = false;
				for (int m : temp.spawnMinute) {
					if (minute == m) {// 같은 시각
						ck = true;
						break;
					}
				}
				if (ck) {
					continue;
				}
				temp.isSpawn = false;
			}
			for (int i=0; i<temp.spawnHour.length; i++) {
				if (hour == temp.spawnHour[i] && minute == temp.spawnMinute[i]) {
					temp.isSpawn	= true;// 스폰 활성화
					GeneralThreadPool.getInstance().execute(new BossThread(temp));
					break;
				}	
			}
		}
	}
	
	class BossThread implements Runnable {// 보스 스폰 쓰레드
		final BossTemp temp;
		
		BossThread(BossTemp temp){
			this.temp = temp;
		}
		
		@Override
		public void run(){
			if (temp == null) {
				return;
			}
			try {
				if (temp.signMap != null && !temp.signMap.isEmpty() && temp.signMap.containsKey(temp.spawnLoc[2])) {
					signSpawn(temp.signMap.get(temp.spawnLoc[2]));// 전조 현상 스폰
				}
				if (temp.rndMinut > 0) {
					Thread.sleep((random.nextInt(temp.rndMinut) + 1) * 60000);// 랜덤 시간
				}
				if (random.nextInt(100) + 1 <= temp.percent) {// 멍탐 존재하도록
					switch(temp.spawnType){
					case DOMINATION_TOWER:	dominationTowerSpawn(temp);break;
					case DRAGON_RAID:		dragonRaidStart();break;
					case POISON_FEILD:		poisonFeildSpawn(temp);break;
					default:				spawnBoss(temp);break;
					}
				}
			} catch(Exception e){
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	static final Point[] TAROS_XY = {// 간수장 타로스 좌표
		new Point(32804, 32790), new Point(32791, 32731), new Point(32742, 32732), new Point(32736, 32792)
	};
	
	static final Point[] ARCMO_XY = {// 아크모 좌표
		new Point(32666, 32796), new Point(32732, 32801), new Point(32714, 32870)
	};

	/**
	 * 보스를 스폰시킨다.
	 * @param BossTemp
	 */
	void spawnBoss(final BossTemp temp) {
		try {
			NpcTable npcTemp	= NpcTable.getInstance();
			L1Npc template		= npcTemp.getTemplate(temp.npcid);
			if (template == null) {
				//System.out.println(String.format("[BossSpawn] npcid(%d) 가 존재하지 않습니다.", temp.npcid));
				System.out.println(String.format("[BossSpawn] npcid(%d) does not exist.", temp.npcid));
				return;
			}
			int npcId	= template.getNpcId();
			int locX	= temp.spawnLoc[0];
			int locY	= temp.spawnLoc[1];
			int mapId	= temp.spawnLoc[2];
			
			L1NpcInstance npc = npcTemp.newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			
			if (npcId == 45488 && mapId == 809) {
				mapId = mapId + random.nextInt(2);// 카스파 패밀리 3층 or 4층
			}
			
			npc.setMap((short)mapId);
			if (temp.rndRange == 0) {
				npc.getLocation().set(locX, locY, mapId);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(locX + (int) (Math.random() * temp.rndRange) - (int) (Math.random() * temp.rndRange));
					npc.setY(locY + (int) (Math.random() * temp.rndRange) - (int) (Math.random() * temp.rndRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(locX, locY, mapId);
					npc.getLocation().forward(temp.heading);
				}
			}
			
			if (npcId == 46025 && mapId == 54) {// 간수장 타로스 좌표 지정
				int rnd = random.nextInt(TAROS_XY.length);
				npc.setX(TAROS_XY[rnd].getX());
				npc.setY(TAROS_XY[rnd].getY());
			} else if (npcId == 7123 && mapId == 37) {// 아크모 좌표 지정
				int rnd = random.nextInt(ARCMO_XY.length);
				npc.setX(ARCMO_XY[rnd].getX());
				npc.setY(ARCMO_XY[rnd].getY());
			}
			
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(temp.heading);
			
			if (temp.groupid > 0) {
				L1MobGroupSpawn.getInstance().doSpawn(npc, temp.groupid, true, false, temp.aliveSecond);
			}
			
			if (temp.movementDistance > 0) {
				npc.setMovementDistance(temp.movementDistance);
			}

			world.storeObject(npc);
			world.addVisibleObject(npc);
			
			// 스폰 이미지
			L1SpawnUtil.spawnAction(npc, npcId);
			
			// 부가 액션
			if (npcId == 45573 && mapId == 2) {// 바포메트
				for (L1PcInstance pc : world.getMapPlayer(2)) {
					pc.getTeleport().start(32664, 32792, (short) 2, pc.getMoveState().getHeading(), true);// 2층 입구로 텔레포트
				}
			} else if (npcId == 45752 && mapId == 15404) {// 발록
				balrogAction();
			} else if (npcId == 81163) {// 기르타스
	        	//Broadcaster.broadcastPacket(npc, new S_NpcChatPacket(npc, "voice_kirtas_2", L1ChatType.SOUND), true);// 음성패킷
			} else if (npcId == 5136) {// 에르자베
	        	_erzabeRun = true;
			} else if (npcId == 5135) {// 샌드웜
				_sandwarmRun = true;
				sandwarmAlim();
			} else if (npcId == 7311090 && mapId == 111) {// 사신 그림리퍼(오만)
				for (L1PcInstance pc : world.getMapPlayer(111)) {
					pc.getTeleport().start(32693, 32902, (short) 111, pc.getMoveState().getHeading(), true);// 시작 지점
					pc.sendPackets(L1GreenMessage.OMAN_REAPER_SPAWN);// 목숨이 아깝다면.. 지금 도망가도 좋다.. 그 꼴을 보는 것도 재밌겠군..
				}
			} else if (npcId == 46025 && mapId == 54) {// 간수장 타로스 등장 맵변화
				_taros = true;
				tarosScriptAction(npc);
			}

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
			if (temp.mentType != BossMentType.NONE) {
				bossSpawnMent(temp.mentType, temp.ment, mapId, npc.getLocation());// 멘트 뛰우기
			}
			if (temp.isYn) {
				bossTeleportYN(npc);// 이동하기 Y_N 메세지 띄우기 C_Attr에 추가
			}
			if (temp.aliveSecond > 0) {
				new L1NpcDeleteTimer(npc, temp.aliveSecond * 1000).begin();// 자동 삭제 시간
			}
			alives.put(npcId, npc);
			
			if (temp.notification != null) {
				npc.set_notification_info(temp.notification);
				npc.do_notification(true);
			}
			
			//Manager.getInstance().BossAppend(npc.getName()); // MANAGER DISABLED
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	void balrogAction(){
		for (L1DoorInstance door : world.getAllDoor()) {
			if ((door.getDoorId() == 225 || door.getDoorId() == 226) && door.getOpenStatus() == ActionCodes.ACTION_Open) {// 발록방 10시 5시 문 스폰
				door.close();
			}
		}
		for (L1PcInstance pc : world.getMapPlayer(15404)) {
			int[] getBackLoc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_WERLDAN);
			pc.getTeleport().start(getBackLoc[0], getBackLoc[1], (short) getBackLoc[2], pc.getMoveState().getHeading(), true);
			pc.sendPackets(L1SystemMessage.BARLOG_SPAWN);
			pc.sendPackets(L1GreenMessage.BARLOG_SPAWN);
		}
	}
	
	void sandwarmAlim(){
		world.broadcastPacketToAll(S_PacketBox.WAKE);// 진동
		world.broadcastPacketToAll(L1GreenMessage.SANDWARM_SPAWN);// 이 진동은..!! 사막에 샌드 웜이 나타났나봐요!
	}
	
	void tarosScriptAction(L1NpcInstance npc){
		long script_start_time		= System.currentTimeMillis() / 1000;
		S_SceneNoti disable			= new S_SceneNoti(false, S_SceneNoti.ScriptName.MAP54_DISABLE.toString(), script_start_time, 0, 0);
		S_SceneNoti smokeStart		= new S_SceneNoti(true, S_SceneNoti.ScriptName.KIKAM_SMOKE.toString(), script_start_time, 0, 0); 
		S_SceneNoti kikamBossStart	= new S_SceneNoti(true, S_SceneNoti.ScriptName.KIKAM_BOSS.toString(), script_start_time, npc.getX(), npc.getY());
		ArrayList<L1PcInstance> pcList	= world.getMapPlayer(54);
		
		for (L1PcInstance pc : pcList) {
			if (pc == null) {
				continue;
			}
			pc.sendPackets(disable);// 리셋
			pc.sendPackets(S_SceneNoti.KIKAM_FULL);// 불끄기
			pc.sendPackets(smokeStart);// 보스 스모크 시작
			pc.sendPackets(kikamBossStart);// 보스 스폰위치 이팩트
		}
		disable.clear();
		smokeStart.clear();
		kikamBossStart.clear();
		disable			= null;
		smokeStart		= null;
		kikamBossStart	= null;
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					for (L1PcInstance pc : pcList) {
						if (pc == null) {
							continue;
						}
						pc.sendPackets(S_SceneNoti.KIKAM_SMOKE_STOP);// 보스 스모크 중단
						pc.sendPackets(S_SceneNoti.KIKAM_BOSS_STOP);// 보스 스폰위치 이팩트 중단
					}
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}, 2000L);// 2초 딜레이
	}
	
	void bossSpawnMent(BossMentType mentType, String ment, int mpaId, L1Location loc){
		S_PacketBox message	= new S_PacketBox(S_PacketBox.GREEN_MESSAGE, ment);
		switch(mentType){
		case WORLD:
			world.broadcastPacketToAll(message);
			break;
		case SCREEN:
			for (L1Object obj : world.getVisiblePoint(loc, 20)) {
				if(obj instanceof L1PcInstance == false)continue;
				L1PcInstance pc = (L1PcInstance)obj;
				pc.sendPackets(message);
			}
			break;
		case MAP:
			for (L1PcInstance pc : world.getMapPlayer(mpaId)) {
				pc.sendPackets(message);
			}
			break;
		default:break;
		}
		message.clear();
		message = null;
	}
	
	//static final String BOSS_YN_MESSAGE = "[%s] 보스지역으로 이동하시겠습니까?";
	static final String BOSS_YN_MESSAGE = "Would you like to move to the boss area [%s]?";
	void bossTeleportYN(L1NpcInstance npc){
        try {
        	S_MessageYN bossYn = new S_MessageYN(C_Attr.MSGCODE_BOSS, C_Attr.YN_MESSAGE_CODE, String.format(BOSS_YN_MESSAGE, npc.getName()));
    		for (L1PcInstance pc : world.getAllPlayers()) {
    			if (pc.isNotEnablePc()) {
    				continue;
    			}
    			pc.getConfig().setBossYN(npc.getNpcId());
                pc.sendPackets(bossYn);
            }
    		bossYn.clear();
    		bossYn = null;
    		
            GeneralThreadPool.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    for (L1PcInstance pc : world.getAllPlayers()) {
                        pc.getConfig().setBossYN(0);
                    }
                }
            }, 13000L);// 지속시간
        } catch (Exception e) {
        	_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
	}
	
	void dragonRaidStart(){
		int randomRaid = random.nextInt(DragonRaildType.ARRAY.length - 1);
		DragonRaildType raidType = null;
		switch (randomRaid) {
		case 0:raidType = DragonRaildType.ANTARAS;break;
		case 1:raidType = DragonRaildType.FAFURION;break;
		case 2:raidType = DragonRaildType.RINDVIOR;break;
		case 3:raidType = DragonRaildType.VALAKAS;break;
		default:return;
		}
		DragonRaidCreator.getInstance().create(null, raidType);
		world.broadcastPacketToAll(L1GreenMessage.DRAGON_RAID_START);// 어딘가에 드래곤의 힘이 느껴집니다.
	}
	
	void poisonFeildSpawn(final BossTemp temp){
		L1Location loc		= new L1Location(temp.spawnLoc[0], temp.spawnLoc[1], temp.spawnLoc[2]);
		if (temp.rndRange > 0) {
			loc = loc.randomLocation(temp.rndRange, false);
		}
		int poisonId		= temp.npcid;
		int locX			= loc.getX();
		int locY			= loc.getY();
		short locMapId		= (short)loc.getMapId();
		int deleteTime		= temp.aliveSecond * 1000;
		int poisonRange		= temp.groupid;
		int dis				= 8;
		for (int width = 0; width <= poisonRange; width++) {
			for (int height = 0; height <= poisonRange; height++) {
				effect.spawnEffect(poisonId, deleteTime, locX - (dis * width), locY - (dis * height), locMapId);
			}
		}
		if (temp.mentType != BossMentType.NONE) {
			bossSpawnMent(temp.mentType, temp.ment, locMapId, loc);// 멘트 뛰우기
		}
		loc = null;
	}
	
	/***************************************************************************************************************
	 ********************************************** 지배의 탑 보스 ********************************************************
	 ***************************************************************************************************************/
	void dominationTowerSpawn(final BossTemp temp){
		try {
			L1NpcInstance boss		= null;
			int npcId				= temp.npcid;
			int mapId				= temp.spawnLoc[2];
			BossMentType mentType	= temp.mentType;
			if (mentType != BossMentType.NONE && npcId != 800157) {
				S_PacketBox greenMsg = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, temp.ment);
				if (mentType == BossMentType.WORLD) {
					world.broadcastPacketToAll(greenMsg, true);
				} else {
					for (L1PcInstance pc : world.getMapPlayer(mapId)) {
						pc.sendPackets(greenMsg);
					}
				}
		    	greenMsg.clear();
		    	greenMsg = null;
		    }
			
			switch (npcId) {
			case 800144:
				if (mapId == 12852) {
					effect.spawnEffect(800172, 5, 32815, 32799, (short) mapId);
				} else {
					effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				}
			    Thread.sleep(2000L);
			    if (mapId == 12852) {
			    	deathAction(temp, 800173);// 거미알
			    	deathAction(temp, 800174);// 거미알
			    }
			    Thread.sleep(3000L);
			    if (mapId == 12852) {
			    	boss = L1SpawnUtil.spawn2(32815, 32799, (short) mapId, temp.heading, npcId, 0, temp.aliveSecond * 1000, 0);// 왜곡의 제니스 퀸
			    } else {
			    	boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);// 왜곡의 제니스 퀸
			    }
			    Thread.sleep(3000L);
			    if (mapId == 12852) {
			    	deathAction(temp, 800175);// 거미알
			    	deathAction(temp, 800176);// 거미알
			    }
			    //Manager.getInstance().BossAppend("왜곡의 제니스 퀸(지배의 탑)");
				//Manager.getInstance().BossAppend("Zenith Queen of Distortion (Tower of Domination)"); // MANAGER DISABLED
				break;
			case 800145:
				if (mapId == 12853) {
					effect.spawnEffect(800172, 5, 32802, 32797, (short) mapId);
				} else {
					effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				}
				Thread.sleep(5000L);
				if (mapId == 12853) {
					boss = deathAction(temp, 800185);// 감시자 시어 변신
				} else {
					boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);//불신의 시어
				}
				//Manager.getInstance().BossAppend("불신의 시어(지배의 탑)");
				//Manager.getInstance().BossAppend("Seer of Distrust (Tower of Domination)"); // MANAGER DISABLED
				break;
			case 800158:
				if (mapId == 12854) {
					effect.spawnEffect(800172, 5, 32802, 32794, (short) mapId);
				} else {
					effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				}
				Thread.sleep(5000L);
				if (mapId == 12854) {
					boss = deathAction(temp, 800186);// 박쥐 뱀파이어 변신
				} else {
					boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);// 공포의 뱀파이어
				}
				//Manager.getInstance().BossAppend("공포의 뱀파이어(지배의 탑)");
				//Manager.getInstance().BossAppend("Dread Vampire (Tower of Domination)"); // MANAGER DISABLED
				break;
			case 800147:
				effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				Thread.sleep(5000L);
				S_EffectLocation effect_1 = new S_EffectLocation(temp.spawnLoc[0] + 1, temp.spawnLoc[1] + 1, 8571);
				S_EffectLocation effect_2 = new S_EffectLocation(temp.spawnLoc[0] + 4, temp.spawnLoc[1] + 3, 16672);
				S_EffectLocation effect_3 = new S_EffectLocation(temp.spawnLoc[0] + 5, temp.spawnLoc[1], 16672);
				for (L1PcInstance pc : world.getMapPlayer(mapId)) {
					pc.sendPackets(effect_1);// 바닥 갈라짐
					pc.sendPackets(effect_2);// 뿌리기
				}
				Thread.sleep(1000L);
				for (L1PcInstance pc : world.getMapPlayer(mapId)) {
				 	pc.sendPackets(effect_3);// 뿌리기
				}
				Thread.sleep(500L);
				boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);// 죽음의 좀비 로드
				//Manager.getInstance().BossAppend("죽음의 좀비 로드(지배의 탑)");
				//Manager.getInstance().BossAppend("Zombie Lord of Death (Tower of Domination)"); // MANAGER DISABLED
				effect_1.clear();
				effect_2.clear();
				effect_3.clear();
				effect_1 = null;
				effect_2 = null;
				effect_3 = null;
				break;
			case 800148:
				if (mapId == 12856) {
					effect.spawnEffect(800172, 5, 32669, 32860, (short) mapId);
				} else {
					effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				}
			    Thread.sleep(5000L);
				if (mapId == 12856) {
					boss = L1SpawnUtil.spawn2(32669, 32860, (short) mapId, temp.heading, npcId, 0, temp.aliveSecond * 1000, 0);// 지옥의 쿠거
				} else {
					boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);// 지옥의 쿠거
				}
				//Manager.getInstance().BossAppend("지옥의 쿠거(지배의 탑)");
				//Manager.getInstance().BossAppend("Hell's Cougar (Tower of Domination)"); // MANAGER DISABLED
				break;
			case 800149:
				if (mapId == 12857) {
					effect.spawnEffect(800172, 5, 32672, 32853, (short) mapId);
				} else {
					effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				}
				Thread.sleep(5000L);
				if (mapId == 12857) {
					boss = deathAction(temp, 800184);// 머미폭풍 머미로드 변신
				} else {
					boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);// 불사의 머미로드
				}
				//Manager.getInstance().BossAppend("불사의 머미로드(지배의 탑)");
				//Manager.getInstance().BossAppend("Immortal Mummylord (Tower of Domination)"); // MANAGER DISABLED
				break;
			case 800150:
				effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				Thread.sleep(5000L);
				boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);// 잔혹한 아이리스
				//Manager.getInstance().BossAppend("잔혹한 아이리스(지배의 탑)");
				//Manager.getInstance().BossAppend("Cruel Iris (Tower of Domination)"); // MANAGER DISABLED
				break;
			case 800151:
				if (mapId == 12859) {
					effect.spawnEffect(800172, 5, 32669, 32838, (short) mapId);
				} else {
					effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				}
				Thread.sleep(5000L);
				if (mapId == 12859) {
					boss = deathAction(temp, 800161);// 나발검 나이트발드 변신
				    Thread.sleep(1000L);
				    deathAction(temp, 800162);// 나발검
				    Thread.sleep(2000L);
				    deathAction(temp, 800163);// 나발검
				} else {
					boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);//어둠의 나이트 발드
				}
				//Manager.getInstance().BossAppend("어둠의 나이트 발드(지배의 탑)");
				//Manager.getInstance().BossAppend("Dark Knight Bald (Tower of Domination)"); // MANAGER DISABLED
				break;
			case 800152:
				effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				Thread.sleep(5000L);
				boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);//불멸의 리치
				//Manager.getInstance().BossAppend("불멸의 리치(지배의 탑)");
				//Manager.getInstance().BossAppend("Immortal Lich (Tower of Domination)"); // MANAGER DISABLED
				break;
			case 800153:
				effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
				Thread.sleep(5000L);
				boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, temp.heading, npcId, temp.rndRange, temp.aliveSecond * 1000, 0);//오만한 우그누스
				//Manager.getInstance().BossAppend("오만한 우그누스(지배의 탑)");
				//Manager.getInstance().BossAppend("Ugnus the Arrogant (Tower of Domination)"); // MANAGER DISABLED
				break;
			case 800157:
				if (mapId == 12862) {
					L1SpawnUtil.spawn2(32714, 32901, (short) mapId, 5, 800154, 0, temp.aliveSecond * 1000, 0);// 사신영혼 좌
					L1SpawnUtil.spawn2(32723, 32912, (short) mapId, 5, 800155, 0, temp.aliveSecond * 1000, 0);// 사신영혼 우
					_sasinSoulLeft = _sasinSoulRight = false;
					for (L1PcInstance pc : world.getMapPlayer(mapId)) {
						pc.sendPackets(L1GreenMessage.LEFT_SASIN_SPAWN);
					}
					Thread.sleep(8000L);
					for (L1PcInstance pc : world.getMapPlayer(mapId)) {
						pc.sendPackets(L1GreenMessage.RIGHT_SASIN_SPAWN);
					}
					//Manager.getInstance().BossAppend("사신의 영혼(지배의 탑)");
					//Manager.getInstance().BossAppend("Reaper's Soul (Tower of Domination)"); // MANAGER DISABLED
				} else {
					if (mentType == BossMentType.MAP) {
						S_PacketBox greenMsg = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, temp.ment);
						for (L1PcInstance pc : world.getMapPlayer(mapId)) {
							pc.sendPackets(greenMsg);
						}
				    	greenMsg.clear();
				    	greenMsg = null;
				    }
					effect.spawnEffect(800172, 5, temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId);
					Thread.sleep(5000L);
					L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, 5, 800164, 0, 10000, 0);// 스폰이미지
					Thread.sleep(10000L);
					boss = L1SpawnUtil.spawn2(temp.spawnLoc[0], temp.spawnLoc[1], (short) mapId, 5, npcId, 0, temp.aliveSecond * 1000, 0);// 사신 그림 리퍼
					//Manager.getInstance().BossAppend("사신 그림 리퍼(지배의 탑)");
					//Manager.getInstance().BossAppend("Reaper Grim Reaper (Tower of Domination)"); // MANAGER DISABLED
				}
				break;
			}
			if (boss != null) {
				alives.put(boss.getNpcId(), boss);
				
				if (temp.notification != null) {
					boss.set_notification_info(temp.notification);
					boss.do_notification(true);
				}
			}
		} catch(Exception e){
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 전조 현상을 스폰시킨다
	 * @param signList
	 */
	void signSpawn(FastTable<BossSign> signList){
		for (BossSign sign : signList) {
			L1Location loc = new L1Location(sign.spawnLoc[0], sign.spawnLoc[1], sign.spawnLoc[2]);
			if (sign.rndRange > 0) {
				loc = loc.randomLocation(sign.rndRange, false);
			}
			L1SpawnUtil.spawn2(loc.getX(), loc.getY(), (short) loc.getMapId(), 5, sign.npcId, 0, sign.aliveSecond * 1000, 0);
			loc = null;
		}
	}
	
	/**
	 * 엔피씨가 죽으면 액션을 실행한다.
	 * @param temp
	 * @param npcid
	 */
	L1NpcInstance deathAction(BossTemp temp, int npcid) {
		L1NpcInstance npc = null;
		L1NpcInstance result = null;
	    for (L1Object obj : world.getVisibleObjects(temp.spawnLoc[2]).values()) {
	    	if (!(obj instanceof L1NpcInstance)) {
	    		continue;
	    	}
			npc = (L1NpcInstance) obj;
			if (npc.getNpcId() != npcid) {
				continue;
			}
			boolean bardSword	= npcid >= 800161 && npcid <= 800163;
			boolean spiderEgg	= npcid >= 800173 && npcid <= 800176;
			if (bardSword) {
				Broadcaster.broadcastPacket(npc, new S_Effect(npc.getId(), 16971), true);// 나발검 이팩트
			}
			npc.setCurrentHp(0);
			npc.setDead(true);
			npc.setActionStatus(ActionCodes.ACTION_Die);
			Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Die), true);
			npc.deleteMe();
			if (spiderEgg) {
				L1SpawnUtil.spawn2(npc.getX(), npc.getY(), npc.getMapId(), 5, 800123, 0, temp.aliveSecond * 1000, 0);// 변종 무리안
			} else if (npcid == 800185) {
				result = L1SpawnUtil.spawn2(npc.getX(), npc.getY(), npc.getMapId(), 5, temp.npcid, 0, temp.aliveSecond * 1000, 0);// 불신의 시어
			} else if (npcid == 800186) {
				result = L1SpawnUtil.spawn2(npc.getX(), npc.getY(), npc.getMapId(), 5, temp.npcid, 0, temp.aliveSecond * 1000, 0);// 공포의 뱀파이어
			} else if (npcid == 800184) {
				result = L1SpawnUtil.spawn2(npc.getX(), npc.getY(), npc.getMapId(), 5, temp.npcid, 0, temp.aliveSecond * 1000, 0);// 불사의 머미로드
			} else if (npcid == 800161) {
				result = L1SpawnUtil.spawn2(npc.getX(), npc.getY(), npc.getMapId(), 5, temp.npcid, 0, temp.aliveSecond * 1000, 0);// 어둠의 나이트발드
			}
		}
	    return result;
	}

}


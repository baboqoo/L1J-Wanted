package l1j.server.IndunSystem.antqueen;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.util.Arrays;
import java.util.List;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1CharacterInfo.L1Class;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_Notification;

/**
 * 여왕개미의 은신처 시스템 쓰레드
 * @author LinOffice
 */
public class AntQueen implements Runnable {
	public static final int TOWN_TELEPORTER			= 5170;// 여왕개미 배신자
	public static final int INTER_TELEPORTER		= 5178;// 모래폭풍
	public static final int OUTER_TELEPORTER		= 5174;// 균열의 정령
	public static final int INTER_SHOP				= 5171;// 보급품 상인
	public static final int UNKOWN_MAGISION			= 5172;// 무명의 마법사
	public static final int INTER_BUFF				= 5173;// 강화 마법사
	public static final int POLY_NPC				= 5175;// 보안요원 민제이
	public static final int CRAFT_NPC				= 5176;// 구원의 신녀
	public static final int ENTER_TRAP				= 5177;// 입장 트랩
	public static final int REWARD_TRAP				= 5179;// 보상 트랩
	public static final int BOSS_EGG				= 5181;// 알
	public static final int BOSS_EFFECT				= 5196;// 보스 스폰 이팩트
	public static final int EGG_EFFECT				= 5197;// 알 이팩트
	public static final int BOSS					= 5182;// 보스
	
	private static final List<Integer> ANT_QUEEN_NPCS = Arrays.asList(new Integer[] {
			TOWN_TELEPORTER, INTER_TELEPORTER, OUTER_TELEPORTER, INTER_SHOP, UNKOWN_MAGISION, INTER_BUFF, POLY_NPC, CRAFT_NPC
	});
	
	private static final int LIMIT_TIME				= 3600;
	protected int _timer							= LIMIT_TIME;
	protected boolean running						= false;
	
	private AntQueenStatus stage;
	
	public boolean isRunning(){
		return running;
	}
	
	private static class newInstance {
		public static final AntQueen INSTANCE = new AntQueen();
	}
	public static AntQueen getInstance() {
		return newInstance.INSTANCE;
	}
	private AntQueen(){}

	@Override
	public void run() {
		try {
			initSpawn();
			spawnEgg();
			while(running){
				try {
					timerCheck();
					switch(stage){
					case READY:
						if (_timer == 1800) {// 30분
							AntQueenUtil.sendAllMessage("$31968");// 지금 여왕의 방이 열렷습니다.
							AntQueenUtil.sendAllMessage("$31969");// 용사님 저를 구해주세요 빛을 따라서 제 방으로
							spawnFirstTrap();
							spawnSecondTrap();
							stage = AntQueenStatus.START;
						}
						break;
					case START:
						if (_timer == 1740 || _timer == 1680) {
							AntQueenUtil.deleteAction(ENTER_TRAP);
							AntQueenUtil.deleteAction(REWARD_TRAP);
							spawnFirstTrap();
							spawnSecondTrap();
						} else if (_timer == 1620) {
							AntQueenUtil.sendRoomMessage("$31974");// 덕분에 저를 속박하던 구속이 풀렷습니다.
							trapFailTeleleport();
							AntQueenUtil.deleteAction(ENTER_TRAP);
							AntQueenUtil.deleteAction(REWARD_TRAP);
							AntQueenUtil.sendEffect(19255);
						} else if (_timer == 1618) {
							AntQueenUtil.deleteAction(BOSS_EGG);	//	알오픈
							if (Config.DUNGEON.ANT_QUEEN_INCLUDE) {
								AntQueenUtil.spawn(32897, 32828, (short) 15891, 5, BOSS_EFFECT, 0, 2000);// 에르자베등장이팩트
							} else {
								for (int i=0; i<L1CharacterInfo.CLASS_SIZE; i++) {
									AntQueenUtil.spawn(32897, 32828, (short) (i == L1Class.LANCER.getType() ? 15902 : 15891 + i), 5, BOSS_EFFECT, 0, 2000);// 에르자베등장이팩트
								}
							}
						} else if (_timer == 1616) {
							AntQueenUtil.deleteAction(EGG_EFFECT);	//	바닥이팩트삭제
							AntQueenUtil.sendRoomMessage("$31975");// 식사시간이다.
							if (Config.DUNGEON.ANT_QUEEN_INCLUDE) {
								AntQueenUtil.spawn(32897, 32828, (short) 15891, 5, BOSS, 0, _timer * 1000);// 에르자베
								for (int j = 0; j <6 ; j++) {
									AntQueenUtil.spawn(32897, 32828, (short) 15891, 5, 45952, 6, _timer * 1000);// 수호 개미
								}
							} else {
								for (int i=0; i<L1CharacterInfo.CLASS_SIZE; i++) {
									AntQueenUtil.spawn(32897, 32828, (short) (i == L1Class.LANCER.getType() ? 15902 : 15891 + i), 5, BOSS, 0, _timer * 1000);// 에르자베
									for (int j = 0; j <6 ; j++) {
										AntQueenUtil.spawn(32897, 32828, (short) (i == L1Class.LANCER.getType() ? 15902 : 15891 + i), 5, 45952, 6, _timer * 1000);// 수호 개미
									}
								}
							}
						}
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					try {
						Thread.sleep(1000L);
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void timerCheck(){
		if (!running) {
			return;
		}
		if (_timer > 0) {
			_timer--;
		}
		if (_timer == 1770) {
			AntQueenUtil.sendRoomMessage("$31971");// 당신만이 절 속박하는 구속을 풀어주실 수 있습니다.
		} else if (_timer == 1710) {
			AntQueenUtil.sendRoomMessage("$31972");// 좀더 힘을 모아주세요.. 제발..
		} else if (_timer == 1650) {
			AntQueenUtil.sendRoomMessage("$31973");// 아아.. 이제 거의 다 됐어요..
		} else if (_timer == 1630) {
			AntQueenUtil.sendAllMessage("$31976");// 여왕개미가 깨어나려고 합니다..! 용사들의 무운을 빌어주세요.
		} else if (_timer == 1000) {
			AntQueenUtil.sendRoomMessage("$31966");// 여왕개미의 방이 무너지고 있습니다. 안전을 위해 주둔지로 귀환합니다.
			AntQueenUtil.sendRoomMessage("$31967");// 여왕개미가 방을 무너뜨렸습니다. 에르자베 공략에 실패하였습니다.
			failTeleleport();
		} else if (_timer == 300) {
			//AntQueenUtil.sendAllMessage("5분 뒤 종료됩니다.");
			AntQueenUtil.sendAllMessage(S_SystemMessage.getRefText(267));
		} else if (_timer == 240) {
			//AntQueenUtil.sendAllMessage("4분 뒤 종료됩니다.");
			AntQueenUtil.sendAllMessage(S_SystemMessage.getRefText(268));
		} else if (_timer == 180) {
			//AntQueenUtil.sendAllMessage("3분 뒤 종료됩니다.");
			AntQueenUtil.sendAllMessage(S_SystemMessage.getRefText(275));
		} else if (_timer == 120) {
			//AntQueenUtil.sendAllMessage("2분 뒤 종료됩니다.");
			AntQueenUtil.sendAllMessage(S_SystemMessage.getRefText(276));
		} else if (_timer == 60) {
			//AntQueenUtil.sendAllMessage("1분 뒤 종료됩니다.");
			AntQueenUtil.sendAllMessage(S_SystemMessage.getRefText(284));
		} else if (_timer == 30) {
			//AntQueenUtil.sendAllMessage("30초 후에 종료됩니다.");
			AntQueenUtil.sendAllMessage(S_SystemMessage.getRefText(287));
		} else if (_timer == 0) {
			endAnt();
		}
		AntQueenUtil.timerSend(_timer);
	}
	
	void failTeleleport(){
		FastTable<L1PcInstance> list = AntQueenUtil.PcStageCK();
		for (L1PcInstance pc : list) {
			int[] loc = Getback.GetBack_Location(pc);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		}
		list.clear();
	}
	
	void trapFailTeleleport(){
		L1NpcInstance npc = null;
		FastTable<L1PcInstance> list = AntQueenUtil.PcStageCK();
		for (L1PcInstance pc : list) {
			if (pc != null && !pc.isDead()) {
				boolean result = false;
				for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 1)) {
					if (obj instanceof L1NpcInstance) {
						npc = (L1NpcInstance) obj;
						if (npc.getNpcId() == 5179 && pc.getX() == npc.getX() && pc.getY() == npc.getY() && pc.getMapId() == npc.getMapId()) {
							result = true;
							break;
						}
					}
				}
				if (!result) {
					int[] loc = Getback.GetBack_Location(pc);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
				}
			}
		}
		list.clear();
	}
	
	void endTeleport(){
		int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_WINDAWOOD);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getMapId() >= 15871 && pc.getMapId() <= 15902) {
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			}
		}
	}
	
	void initSpawn(){
		AntQueenUtil.spawn(32570, 32947, (short) 0, 7, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 말하는섬
		AntQueenUtil.spawn(33437, 32814, (short) 4, 7, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 기란
		AntQueenUtil.spawn(33056, 32783, (short) 4, 7, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 켄트
		AntQueenUtil.spawn(33721, 32495, (short) 4, 5, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 웰던
		AntQueenUtil.spawn(32744, 32441, (short) 4, 7, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 화전민
		AntQueenUtil.spawn(32620, 32801, (short) 4, 7, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 글루딘
		AntQueenUtil.spawn(32624, 33188, (short) 4, 5, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 우드백
		AntQueenUtil.spawn(33073, 33401, (short) 4, 5, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 은기사
		AntQueenUtil.spawn(33604, 33254, (short) 4, 5, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 하이네
		AntQueenUtil.spawn(34053, 32292, (short) 4, 5, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 오렌
		AntQueenUtil.spawn(33925, 33346, (short) 4, 5, TOWN_TELEPORTER, 0, _timer * 1000);		//	여왕개미 배신자 아덴
		AntQueenUtil.spawn(32774, 32838, (short) 622, 5, TOWN_TELEPORTER, 0, _timer * 1000);	//	여왕개미 배신자 수상한하늘정원
		AntQueenUtil.spawn(32864, 33269, (short) 4, 0, INTER_TELEPORTER, 0, _timer * 1000);	//	모래폭풍
		if (Config.DUNGEON.ANT_QUEEN_INCLUDE) {
			int classMapId = 15871;
			AntQueenUtil.spawn(32825, 32756, (short) classMapId, 4, OUTER_TELEPORTER, 0, _timer * 1000);	//	균열의 정령
			AntQueenUtil.spawn(32831, 32761, (short) classMapId, 4, INTER_BUFF, 0, _timer * 1000);			//	강화 마법사
			AntQueenUtil.spawn(32830, 32767, (short) classMapId, 6, CRAFT_NPC, 0, _timer * 1000);			//	구원의 신녀
			AntQueenUtil.spawn(32834, 32771, (short) classMapId, 6, UNKOWN_MAGISION, 0, _timer * 1000);	//	무명의 마법사
			AntQueenUtil.spawn(32838, 32767, (short) classMapId, 6, INTER_SHOP, 0, _timer * 1000);			//	보급품 상인
		} else {
			for (int i=0; i<L1CharacterInfo.CLASS_SIZE; i++) {
				int classMapId = i == L1Class.LANCER.getType() ? 15900 : (15871 + i);
				AntQueenUtil.spawn(32825, 32756, (short) classMapId, 4, OUTER_TELEPORTER, 0, _timer * 1000);	//	균열의 정령
				AntQueenUtil.spawn(32831, 32761, (short) classMapId, 4, INTER_BUFF, 0, _timer * 1000);			//	강화 마법사
				AntQueenUtil.spawn(32830, 32767, (short) classMapId, 6, CRAFT_NPC, 0, _timer * 1000);			//	구원의 신녀
				AntQueenUtil.spawn(32834, 32771, (short) classMapId, 6, UNKOWN_MAGISION, 0, _timer * 1000);	//	무명의 마법사
				AntQueenUtil.spawn(32838, 32767, (short) classMapId, 6, INTER_SHOP, 0, _timer * 1000);			//	보급품 상인
			}
		}
		GeneralThreadPool.getInstance().schedule(new AntQueenPoisonWall(), 600 * 1000L);// 대미지 결계 생성
	}
	
	void spawnFirstTrap(){
		if (Config.DUNGEON.ANT_QUEEN_INCLUDE) {
			for (int count=0; count<3; count++) {
				AntQueenUtil.spawn(32844, 32819, (short) 15881, 5, ENTER_TRAP, 50, _timer * 1000);
			}
		} else {
			for (int type=0; type<L1CharacterInfo.CLASS_SIZE; type++) {
				for (int count=0; count<3; count++) {
					AntQueenUtil.spawn(32844, 32819, (short) (type == L1Class.LANCER.getType() ? 15901 : 15881 + type), 5, ENTER_TRAP, 50, _timer * 1000);
				}
			}
		}
	}
	
	void spawnSecondTrap(){
		AntQueenUtil.sendRoomMessage("$31970");// 빛나는 바닥돌이 저를 구할 수 있는 열쇠입니다. 그곳으로..
		if (Config.DUNGEON.ANT_QUEEN_INCLUDE) {
			for (int count=0; count<20; count++) {
				AntQueenUtil.spawn(32897, 32828, (short) 15891, 5, REWARD_TRAP, 12, _timer * 1000);
			}
		} else {
			for (int type=0; type<L1CharacterInfo.CLASS_SIZE; type++) {
				for (int count=0; count<20; count++) {
					AntQueenUtil.spawn(32897, 32828, (short) (type == L1Class.LANCER.getType() ? 15902 : 15891 + type), 5, REWARD_TRAP, 12, _timer * 1000);
				}
			}
		}
	}
	
	void spawnEgg(){
		if (Config.DUNGEON.ANT_QUEEN_INCLUDE) {
			AntQueenUtil.spawn(32897, 32828, (short) 15891, 5, BOSS_EGG, 0, _timer * 1000);	//	알
			AntQueenUtil.spawn(32897, 32828, (short) 15891, 5, EGG_EFFECT, 0, _timer * 1000);	//	바닥이팩트
		} else {
			for (int i=0; i<L1CharacterInfo.CLASS_SIZE; i++) {
				AntQueenUtil.spawn(32897, 32828, (short) (i == L1Class.LANCER.getType() ? 15902 : 15891 + i), 5, BOSS_EGG, 0, _timer * 1000);	//	알
				AntQueenUtil.spawn(32897, 32828, (short) (i == L1Class.LANCER.getType() ? 15902 : 15891 + i), 5, EGG_EFFECT, 0, _timer * 1000);//	바닥이팩트
			}
		}
	}
	
	public void startAnt() {
		System.out.println("■■■■■■■■■■ Ant Queen Dungeon begins ■■■■■■■■■■");
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$32011"), true);// 모래 바람 사이로 여왕 개미 은신처가 보이기 시작합니다.
//AUTO SRM: 		world.broadcastServerMessage("개미굴로 통하는 입구가 열렸습니다.", true); // CHECKED OK
		world.broadcastServerMessage(S_SystemMessage.getRefText(29), true);
		world.broadcastPacketToAll(S_Notification.ANT_QUEEN_ON);	//	알람
		stage			= AntQueenStatus.READY;
		running			= GameServerSetting.ANT_QUEEN = true;
		_timer			= LIMIT_TIME;
		GeneralThreadPool.getInstance().execute(this);
	}
	
	public void endAnt() {
		System.out.println("■■■■■■■■■■ Ant Queen Dungeon ends ■■■■■■■■■■");
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$32012"), true);// 모래 바람 사이로 여왕 개미 은신처가 사라지고 있습니다.
		world.broadcastPacketToAll(S_Notification.ANT_QUEEN_OFF);//	알람
		running			= GameServerSetting.ANT_QUEEN = false;
		endTeleport();
		for (L1Object obj : world.getObject()) {
			if (obj == null) {
				continue;
			}
			if (obj.getMapId() >= 15871 && obj.getMapId() <= 15902) {
				if (obj instanceof L1DollInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
					continue;
				}
				if (obj instanceof L1ItemInstance) {
					L1ItemInstance item = (L1ItemInstance)obj;
					L1Inventory groundInventory = world.getInventory(item.getX(), item.getY(), item.getMapId());
					groundInventory.removeItem(item);
				} else if (obj instanceof L1DoorInstance) {
					L1DoorInstance door = (L1DoorInstance)obj;
					door.isPassibleDoor(true);
					door.getMap().setPassable(door.getLocation(), true);
					door.deleteMe();
				} else if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance)obj;
					npc.deleteMe();
				}
			}
			if (obj instanceof L1NpcInstance && ANT_QUEEN_NPCS.contains(((L1NpcInstance)obj).getNpcId())) {
				((L1NpcInstance)obj).deleteMe();
			}
		}
	}
}



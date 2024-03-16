package l1j.server.server.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.common.data.ChatType;
//import l1j.server.IndunSystem.battlecoloseum.BattleColoseum;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.controller.action.UltimateBattleTime;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.UBSpawnTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.message.S_NotificationMessageNoti;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;

public class L1UltimateBattle {
	private static final Logger _log	= Logger.getLogger(L1UltimateBattle.class.getName());
	private L1Location _location;
	private int _locX, _locY, _locX1, _locY1, _locX2, _locY2;
	private short _mapId;
	private int _ubId;
	private int _pattern;
	private boolean _isNowUb;
	private boolean _active;
	private int _minLevel, _maxLevel, _maxPlayer;
	private boolean _enterRoyal, _enterKnight, _enterMage, _enterElf, _enterDarkelf, _enterDragonknight, _enterIllusionist, _enterWarrior, _enterFencer, _enterLancer;
	private boolean _enterMale, _enterFemale;
	private boolean _usePot;
	private int _hpr, _mpr;
	private static final int BEFORE_MINUTE = 5;
	private static int ubcount = 0;

	private Set<Integer> _managers					= new HashSet<Integer>();
	private SortedSet<Integer> _ubTimes				= new TreeSet<Integer>();
	private final ArrayList<L1PcInstance> _members	= new ArrayList<L1PcInstance>();

	private void sendRoundMessage(int curRound) {
		switch (_ubId) {
		case 1: //기란
			switch(curRound){
			case 1:sendProtoMessage("$35063", "ff f7 b1");break;// 제 1 군 투입!
			case 2:sendProtoMessage("$35067", "ff f7 b1");break;// 제 2 군 투입!
			case 3:sendProtoMessage("$35071", "ff f7 b1");break;// 제 3 군 투입!
			case 4:sendProtoMessage("$35075", "ff f7 b1");break;// 제 4 군 투입!
			case 5:sendProtoMessage("$35079", "ff f7 b1");break;// 제 5 군 투입!
			}
			break;
		case 2: //웰던
		case 3: //글루딘
		case 5: //은기사
			switch(curRound){
			//case 1:sendMessage("콜롯세움 관리인: 제 1 군 투입!");break;
			case 1:sendMessage("$1609: $35063");break;
			//case 2:sendMessage("콜롯세움 관리인: 제 2 군 투입!");break;
			case 2:sendMessage("$1609: $35067");break;
			//case 3:sendMessage("콜롯세움 관리인: 제 3 군 투입!");break;
			case 3:sendMessage("$1609: $35071");break;
			//case 4:sendMessage("콜롯세움 관리인: 최종전 개시! 제한 시간은 5분 입니다");break;
			case 4:sendMessage("$1609: " + S_SystemMessage.getRefText(512));break;
			}
			break;
		case 4: //말하는섬
			switch(curRound){
			//case 1:sendMessage("콜롯세움 관리인: 제 1 군 투입!");break;
			case 1:sendMessage("$1609: $35063");break;
			//case 2:sendMessage("콜롯세움 관리인: 제 2 군 투입!");break;
			case 2:sendMessage("$1609: $35067");break;
			case 3:sendMessage("$1609: " + S_SystemMessage.getRefText(512));break;
			}
			break;
		default:break;
		}
	}

	private void spawnSupplies(int curRound) {
		switch (_ubId) {
		case 1: //기란
			if (curRound == 1) {
				spawnGroundItem(L1ItemId.ADENA, 10000, 30);
				sendProtoMessage("$35064", "ff f7 b1");// 제 1 군의 투입이 완료되었습니다.
			} else if (curRound == 2) {
				spawnGroundItem(L1ItemId.ADENA, 30000, 30);
				sendProtoMessage("$35068", "ff f7 b1");// 제 2 군의 투입이 완료되었습니다.
			} else if (curRound == 3) {
				spawnGroundItem(L1ItemId.ADENA, 50000, 30);
				sendProtoMessage("$35072", "ff f7 b1");// 제 3 군의 투입이 완료되었습니다.
			} else if (curRound == 4) {
				spawnGroundItem(L1ItemId.ADENA, 100000, 30);
				sendProtoMessage("$35076", "ff f7 b1");// 제 4 군의 투입이 완료되었습니다.
			} else if (curRound == 5) {
				spawnGroundItem(L1ItemId.ADENA, 150000, 30);
				sendProtoMessage("$35080", "ff f7 b1");// 제 5 군의 투입이 완료되었습니다.
			}
			break;
		case 2: //웰던
			if (curRound == 1) {
				spawnGroundItem(L1ItemId.ADENA, 10000, 30);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 3, 20);
				spawnGroundItem(40024, 20, 20);
				spawnGroundItem(40317, 5, 5);
				spawnGroundItem(40079, 1, 10);
				
				//sendMessage("콜롯세움 관리인: 제 1 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35064");
				sendMessage("$1609: " + S_SystemMessage.getRefText(515)); // dos veces
			} else if (curRound == 2) {
				spawnGroundItem(L1ItemId.ADENA, 30000, 30);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 5, 20);
				spawnGroundItem(40024, 20, 20);
				spawnGroundItem(40317, 5, 7);
				spawnGroundItem(40079, 1, 10);
				spawnGroundItem(40093, 1, 10);
				
				//sendMessage("콜롯세움 관리인: 제 2 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35068");
				sendMessage("$1609: " + S_SystemMessage.getRefText(533)); // tres veces
			} else if (curRound == 3) {
				spawnGroundItem(L1ItemId.ADENA, 50000, 30);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 5, 20);
				spawnGroundItem(40024, 20, 20);
				spawnGroundItem(40317, 5, 10);
				spawnGroundItem(40079, 1, 10);
				spawnGroundItem(40094, 1, 10);

				//sendMessage("콜롯세움 관리인: 제 3 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35072");
				sendMessage("$1609: " + S_SystemMessage.getRefText(546)); // cuatro veces
			}
			break;
		case 3: //글루딘
			if (curRound == 1) {
				spawnGroundItem(L1ItemId.ADENA, 1000, 60);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 3, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 5, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 3, 20);
				spawnGroundItem(40317, 1, 5);
				spawnGroundItem(40079, 1, 10);
				//sendMessage("콜롯세움 관리인: 제 1 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35064");
				sendMessage("$1609: " + S_SystemMessage.getRefText(555)); // dos veces
			} else if (curRound == 2) {
				spawnGroundItem(L1ItemId.ADENA, 3000, 50);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 5, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 10, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 5, 20);
				spawnGroundItem(40317, 1, 7);
				spawnGroundItem(40093, 1, 10);
				//sendMessage("콜롯세움 관리인: 제 2 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35068");
				sendMessage("$1609: " + S_SystemMessage.getRefText(533)); // tres veces
			} else if (curRound == 3) {
				spawnGroundItem(L1ItemId.ADENA, 5000, 30);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 10, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 15, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 7, 10);
				spawnGroundItem(40317, 1, 10);
				spawnGroundItem(40094, 1, 10);
				//sendMessage("콜롯세움 관리인: 제 3 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35072");
				sendMessage("$1609: " + S_SystemMessage.getRefText(546)); // cuatro veces
			}
			break;
		case 4: //말하는섬
			if (curRound == 1) {
				spawnGroundItem(L1ItemId.ADENA, 200, 60);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 3, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 5, 20);
				spawnGroundItem(40317, 1, 5);
				spawnGroundItem(40079, 1, 10);
				//sendMessage("콜롯세움 관리인: 제 1 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35064");
				sendMessage("$1609: " + S_SystemMessage.getRefText(515)); // dos veces
			} else if (curRound == 2) {
				spawnGroundItem(L1ItemId.ADENA, 500, 50);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 7, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 12, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 5, 20);
				spawnGroundItem(40317, 1, 7);
				spawnGroundItem(40093, 1, 10);
				//sendMessage("콜롯세움 관리인: 제 2 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35068");
				sendMessage("$1609: " + S_SystemMessage.getRefText(546)); // cuatro veces
			}
			break;
		case 5: //은기사
			if (curRound == 1) {
				spawnGroundItem(L1ItemId.ADENA, 1000, 60);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 3, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 5, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 3, 20);
				spawnGroundItem(40317, 1, 5);
				spawnGroundItem(40079, 1, 10);
				//sendMessage("콜롯세움 관리인: 제 1 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35064");
				sendMessage("$1609: " + S_SystemMessage.getRefText(555)); // dos veces
			} else if (curRound == 2) {
				spawnGroundItem(L1ItemId.ADENA, 5000, 50);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 7, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 10, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 5, 20);
				spawnGroundItem(40317, 1, 7);
				spawnGroundItem(40093, 1, 10);
				spawnGroundItem(40079, 1, 10);
				//sendMessage("콜롯세움 관리인: 제 2 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35068");
				sendMessage("$1609: " + S_SystemMessage.getRefText(533)); // tres veces
			} else if (curRound == 3) {
				spawnGroundItem(L1ItemId.ADENA, 10000, 30);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 7, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 20, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 10, 10);
				spawnGroundItem(40317, 1, 10);
				spawnGroundItem(40094, 1, 10);
				spawnGroundItem(40079, 1, 10);
				//sendMessage("콜롯세움 관리인: 제 3 군의 투입이 완료되었습니다.");
				sendMessage("$1609: $35072");
				sendMessage("$1609: " + S_SystemMessage.getRefText(546)); // cuatro veces
			}
			break;
		}
	}
	
	private void bossWaitMent(int curRound){
		switch(curRound){
		case 1:sendProtoMessage("$35065", "ff f7 b1");break;// 곧 제 1군 보스가 투입 할 예정입니다.
		case 2:sendProtoMessage("$35069", "ff f7 b1");break;// 곧 제 2군 보스가 투입 할 예정입니다.
		case 3:sendProtoMessage("$35073", "ff f7 b1");break;// 곧 제 3군 보스가 투입 할 예정입니다.
		case 4:sendProtoMessage("$35077", "ff f7 b1");break;// 곧 제 4군 보스가 투입 할 예정입니다.
		case 5:sendProtoMessage("$35081", "ff f7 b1");break;// 곧 제 5군 보스가 투입 할 예정입니다.
		default:break;
		}
	}
	
	private void nextRoundMent(int time, int curRound){
		switch(curRound){
		case 1:sendProtoMessage(time + S_SystemMessage.getRefText(564), "ff f7 b1");break;
		case 2:sendProtoMessage(time + S_SystemMessage.getRefText(641), "ff f7 b1");break;
		case 3:sendProtoMessage(time + S_SystemMessage.getRefText(654), "ff f7 b1");break;
		case 4:sendProtoMessage(time + S_SystemMessage.getRefText(668), "ff f7 b1");break;
		case 6:sendProtoMessage(time + S_SystemMessage.getRefText(708), "ff f7 b1");break;
		default:break;
		}
	}
	
	private void sendScene(S_SceneNoti scene){
		removeRetiredMembers();
		for (L1PcInstance pc : getMembersArray()) {
			pc.sendPackets(scene);
		}
	}

	private void removeRetiredMembers() {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == null || temp[i].getMapId() != _mapId)
				removeMember(temp[i]);
		}
		temp = null;
	}

	private void sendMessage(String msg) {
		S_SystemMessage systemMsg = new S_SystemMessage(msg, true);
		for (L1PcInstance pc : getMembersArray()) {
			pc.sendPackets(systemMsg);
		}
		systemMsg.clear();
		systemMsg = null;
	}
	
	private void sendProtoMessage(String msg, String color) {
		S_NotificationMessageNoti gfxMsg = new S_NotificationMessageNoti(0, msg, color, 20);
		for (L1PcInstance pc : getMembersArray()) {
			pc.sendPackets(gfxMsg);
		}
		gfxMsg.clear();
		gfxMsg = null;
	}
	
	/** 멘트 엔피씨 **/
	public void broadcastNpc(String msg) {
		for (L1TeleporterInstance tel_npc : L1World.getInstance().getAllTeleporter()) {
			if (tel_npc.getNpcId() >= 150030 && tel_npc.getNpcId() <= 150035) {
				tel_npc.broadcastPacket(new S_NpcChatPacket(tel_npc, msg, ChatType.CHAT_NORMAL), true);
			}
		}
	}
	
	private void spawnGroundItem(int itemId, int stackCount, int count) {
		L1Item temp = ItemTable.getInstance().getTemplate(itemId);
		if (temp == null) {
			return;
		}
		L1Location loc = null;
		L1ItemInstance item = null;
		L1GroundInventory ground = null;
		for (int i = 0; i < count; i++) {
			loc = _location.randomLocation((getLocX2() - getLocX1()) >> 1, false);
			if (temp.isMerge()) {
				item = ItemTable.getInstance().createItem(itemId);
				item.setEnchantLevel(0);
				item.setCount(stackCount);
				ground = L1World.getInstance().getInventory(loc.getX(), loc.getY(), _mapId);
				if (ground.checkAddItem(item, stackCount) == L1Inventory.OK) {
					ground.storeItem(item);
				}
			} else {
				item = null;
				for (int createCount = 0; createCount < stackCount; createCount++) {
					item = ItemTable.getInstance().createItem(itemId);
					item.setEnchantLevel(0);
					ground = L1World.getInstance().getInventory(loc.getX(), loc.getY(), _mapId);
					if (ground.checkAddItem(item, stackCount) == L1Inventory.OK) {
						ground.storeItem(item);
					}
				}
			}
		}
	}

	private void clearColosseum() {
		L1MonsterInstance mob = null;
		L1Inventory inventory = null;
		for (Object obj : L1World.getInstance().getVisibleObjects(_mapId).values()) {
			if (obj instanceof L1MonsterInstance) {
				mob = (L1MonsterInstance) obj;
				if (!mob.isDead()) {
					mob.setDead(true);
					mob.setActionStatus(ActionCodes.ACTION_Die);
					mob.setCurrentHp(0);
					mob.deleteMe();

				}
			} else if (obj instanceof L1Inventory) {
				inventory = (L1Inventory) obj;
				inventory.clearItems();
			}
		}
	}

	public L1UltimateBattle() {
	}
	
	public int _gateKeeperCount = 0;// 문지기 처치 횟수

	class UbThread implements Runnable {
		
		private void countDown() throws InterruptedException {
			for (int loop = 0; loop < BEFORE_MINUTE * 60 - 15; loop++) {
				if (loop == 60) {
					//broadcastNpc("경기 시작까지 4 분 남았습니다. 참가를 원하시는 분들은 지금 입장하여주십시오.");
					broadcastNpc(S_SystemMessage.getRefText(711));
				} else if (loop == 120) {
					//broadcastNpc("경기 시작까지 3 분 남았습니다. 참가를 원하시는 분들은 지금 입장하여주십시오.");
					broadcastNpc(S_SystemMessage.getRefText(739));
				} else if (loop == 180) {
					//broadcastNpc("경기 시작까지 2 분 남았습니다. 참가를 원하시는 분들은 지금 입장하여주십시오.");
					broadcastNpc(S_SystemMessage.getRefText(961));
				} else if (loop == 240) {
					//broadcastNpc("경기 시작까지 1 분 남았습니다. 참가를 원하시는 분들은 지금 입장하여주십시오.");
					broadcastNpc(S_SystemMessage.getRefText(963));
				}
				Thread.sleep(1000L);
			}
			removeRetiredMembers();
			//sendMessage("콜롯세움 관리인: 이제 곧 몬스터들이 등장합니다. 건투를 빕니다."); // 15초전
			sendMessage("$1609: $1615"); // 15초전
			Thread.sleep(5000L);
			sendScene(S_SceneNoti.MAINFOG);// 안개
			//sendMessage("콜롯세움 관리인: 10초뒤에 경기를 시작 합니다."); // 10초전
			sendMessage("$1609: $35061"); // 10초전

			Thread.sleep(5000L);
			sendMessage("$1609: 5 !!"); // 5초전

			Thread.sleep(1000L);
			sendMessage("$1609: 4 !!"); // 4초전

			Thread.sleep(1000L);
			sendMessage("$1609: 3 !!"); // 3초전

			Thread.sleep(1000L);
			sendMessage("$1609: 2 !!"); // 2초전

			Thread.sleep(1000L);
			sendMessage("$1609: 1 !!"); // 1초전

			Thread.sleep(1000L);
			//broadcastNpc("경기가 시작되었습니다.");
			broadcastNpc(S_SystemMessage.getRefText(972));
			removeRetiredMembers();
		}

		public void run() {// 경기처리
			try {
				setActive(true);// 쓰레드 가동 시작(입장가능)
				countDown();// 5분 입장시간 딜레이
				UltimateBattleTime.getInstance()._coloseumRun = false;
				setNowUb(true);// 콜로세움 시작(입장 불가)
				_gateKeeperCount = 0;
				L1UbPattern pattern = null;
				ArrayList<L1UbSpawn> spawnList = null;
				for (int round = 1; round <= ubcount; round++) {
					pattern		= UBSpawnTable.getInstance().getPattern(_ubId, _pattern);
					if (pattern == null) {
						continue;
					}
					spawnList	= pattern.getSpawnList(round);// 라운드 스폰 몬스터
					
					sendRoundMessage(round);// 라운드 시작 멘트
					
					if (spawnList == null) {
						continue;
					}
					
					for (L1UbSpawn spawn : spawnList) {// 라운드 스폰 몬스터
						if (spawn == null || spawn.isBoss()) {
							continue;
						}
						int id = spawn.getId();
						if (id == 7 || id == 11 || id == 21 || id == 37 || id == 49 || id == 65 || id == 74 
								|| id == 88 || id == 96 || id == 104 || id == 114 || id == 119 || id == 125 
								|| id == 17 || id == 42 || id == 81 || id == 110 || id == 134){
							reaperReadyMent(id);
						}
						if (getMembersCount() > 0) {
							spawn.spawnAll();// 몬스터 스폰
						}
						Thread.sleep(spawn.getSpawnDelay() * 1000L);
					}

					if (getMembersCount() > 0) {
						spawnSupplies(round);// 아이템 스폰 및 투입완료 멘트
					}
					bossSpawn(round, spawnList);// 보스 스폰
				}
				
				UBTable.getInstance().writeUbScore(getUbId(), getMembersArray());// 점수 등록

				// 종료
				for (L1PcInstance pc : getMembersArray()) {
					int[] loc = Getback.GetBack_Location(pc);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], 5, true);
					removeMember(pc);
				}
				clearColosseum();
				setActive(false);
				setNowUb(false);
			} catch (Exception e) {
				e.printStackTrace();
				_log.log(Level.SEVERE, "L1UltimateBattle[]Error", e);
			}
		}
		
		private void reaperReadyMent(int id){
			switch(id){
			case 7:		sendProtoMessage("$35130", "B9 06 2F");break;// 어리석은 용사들이여..
			case 11:	sendProtoMessage("$35131", "B9 06 2F");break;// 누가 나의 깊은 잠을 깨우는가..
			case 21:	sendProtoMessage("$35132", "B9 06 2F");break;// 이렇게 무뎌셔야 나를 만날 수 있겠는가..
			case 37:	sendProtoMessage("$35133", "B9 06 2F");break;// 나는..어둠의 지배자..
			case 49:	sendProtoMessage("$35134", "B9 06 2F");break;// 나의 봉인이 풀리면..아덴 월드는 폐허가 될 지어니..
			case 65:	sendProtoMessage("$35135", "B9 06 2F");break;// 나의.. 존재가 궁금하지 않은가?
			case 74:	sendProtoMessage("$35136", "B9 06 2F");break;// 나를 만나고 싶다면.. 힌트를 하나 주지..
			case 88:	sendProtoMessage("$35137", "B9 06 2F");break;// 제한 된 시간 안에.. 나의 봉인석을 가지고 있는..문지기를..
			case 96:	sendProtoMessage("$35138", "B9 06 2F");break;// 5마리의 문지기를 찾아 없애버리면 될 것이야!
			case 104:	sendProtoMessage("$35139", "B9 06 2F");break;// 봉인이..봉인이 풀린다면..
			case 114:	sendProtoMessage("$35140", "B9 06 2F");break;// 나를 봉인한 콜롯세움부터 불바다로 만들어 버릴테다..
			case 119:	sendProtoMessage("$35141", "B9 06 2F");break;// 그 어느 누구도 나의 힘을 막지 못할 것이니라..
			case 125:	sendProtoMessage("$35142", "B9 06 2F");break;// 서둘러서 나의 봉인을 풀으란 말이닷!!
			case 17:case 42:case 81:case 110:case 134:sendProtoMessage("$35125", "00 00 FF");break;// 무한대전 문지기가 출현하였습니다.
			default:break;
			}
		}
		
		private void bossSpawn(int round, ArrayList<L1UbSpawn> spawnList) throws InterruptedException {
			if (round != 6) {
				Thread.sleep(5000L);
				bossWaitMent(round);// 스폰전 멘트
				Thread.sleep(5000L);
				boolean bossSelect = CommonUtil.nextBoolean();
				for (L1UbSpawn spawn : spawnList) {
					if (!spawn.isBoss()) {
						continue;
					}
					if (bossSelect) {
						bossSelect = false;
						continue;
					}
					if (getMembersCount() > 0){
						// 스폰 이팩트
						switch(spawn.getNpcTemplateId()){
						case 30113:sendScene(S_SceneNoti.BAPHOMET_WAKE);break;// 바포메트
						case 30194:sendScene(S_SceneNoti.ASTAROT_WAKE);break;// 제로스
						case 30125:sendScene(S_SceneNoti.DK_WAKE);break;// 데스나이트
						case 30195:sendScene(S_SceneNoti.DEMON_WAKE);break;// 데몬
						case 30196:sendScene(S_SceneNoti.PHOENIX_WAKE);break;// 피닉스
						case 30197:sendScene(S_SceneNoti.BIGDRAKE_WAKE);break;// 거대 드레이크
						case 30139:sendScene(S_SceneNoti.BARLOG_WAKE);break;// 발록
						case 30140:sendScene(S_SceneNoti.REAPER_WAKE);break;// 그림 리퍼
						case 30198:sendScene(S_SceneNoti.KING_WAKE);break;// 아투바킹
						case 30199:sendScene(S_SceneNoti.PBARLOG_WAKE);break;// 분노한 발록
						default:break;
						}
						Thread.sleep(2000L);
						spawn.spawnAll();// 몬스터 스폰
						for (int i=spawn.getSpawnDelay(); i>=1; i--) {// 제한 시간
							if (i == 20 || i == 10) {
								nextRoundMent(i, round);
							} else if (i == 5) {
								roundChageMap(round);
							}
							Thread.sleep(1000L);
						}
						break;
					}
				}
			} else {// 최종 보스
				if (_gateKeeperCount >= 5 && CommonUtil.random(100) + 1 <= _gateKeeperCount * 10) {
					Thread.sleep(5000L);
					roundChageMap(round);
					Thread.sleep(5000L);
					for (L1UbSpawn spawn : spawnList) {
						if (!spawn.isBoss()) {
							continue;
						}
						if (getMembersCount() > 0) {
							spawn.spawnAll();// 몬스터 스폰
							for (int i=spawn.getSpawnDelay(); i>=1; i--) {// 제한시간
								if (i == 60 || i == 30 || i == 20 || i == 10 || i == 5) {
									nextRoundMent(i, round);
								}
								Thread.sleep(1000L);
							}
							break;
						}
					}
				}
			}
			removeRetiredMembers();
		}
		
		private void roundChageMap(int round){
			switch(round){
			case 1:sendScene(S_SceneNoti.STAGE2_WAKE);break;
			case 2:sendScene(S_SceneNoti.STAGE3_WAKE);break;
			case 3:sendScene(S_SceneNoti.STAGE4_WAKE);break;
			case 4:sendScene(S_SceneNoti.STAGE5_WAKE);break;
			case 6:sendScene(S_SceneNoti.HIDDEN_WAKE1);break;
			default:break;
			}
		}
	}

	private static final String[] UP_READY_MSG = { 
		S_SystemMessage.getRefText(974),
		S_SystemMessage.getRefText(976),
		S_SystemMessage.getRefText(978),
		S_SystemMessage.getRefText(979),
		S_SystemMessage.getRefText(981)
		/* "잠시 후 기란 마을의 콜롯세움에서 무한대전 경기가 시작됩니다.",
		"잠시 후 웰던 마을의 콜롯세움에서 무한대전 경기가 시작됩니다.",
		"잠시 후 글루딘 마을의 콜롯세움에서 무한대전 경기가 시작됩니다.",
		"잠시 후 말하는 섬 마을의 콜롯세움에서 무한대전 경기가 시작됩니다.",
		"잠시 후 은기사 마을의 콜롯세움에서 무한대전 경기가 시작됩니다."
		*/
	};
	
	public void start() {
		L1World.getInstance().broadcastServerMessage(UP_READY_MSG[getUbId() - 1], true);
		UltimateBattleTime.getInstance()._coloseumRun = true;
		
		_pattern	= 1;// 몬스터 출현 패턴
		ubcount		= _ubId == 4 ? 3 : 6;// 라운드 갯수
		
		try {
			/*broadcastNpc("경기 시작까지 10 분 남았습니다.");
			Thread.sleep(60000L);
			broadcastNpc("경기 시작까지 9 분 남았습니다.");
			Thread.sleep(60000L);
			broadcastNpc("경기 시작까지 8 분 남았습니다.");
			Thread.sleep(60000L);
			broadcastNpc("경기 시작까지 7 분 남았습니다.");
			Thread.sleep(60000L);
			broadcastNpc("경기 시작까지 6 분 남았습니다.");
			Thread.sleep(60000L);
			broadcastNpc("경기 시작까지 5 분 남았습니다. 참가를 원하시는 분들은 지금 입장하여주십시오.");*/
			broadcastNpc(S_SystemMessage.getRefText(983));
			Thread.sleep(60000L);
			broadcastNpc(S_SystemMessage.getRefText(999));
			Thread.sleep(60000L);
			broadcastNpc(S_SystemMessage.getRefText(1004));
			Thread.sleep(60000L);
			broadcastNpc(S_SystemMessage.getRefText(1011));
			Thread.sleep(60000L);
			broadcastNpc(S_SystemMessage.getRefText(1012));
			Thread.sleep(60000L);
			broadcastNpc(S_SystemMessage.getRefText(1015));
			GeneralThreadPool.getInstance().execute(new UbThread());
			//BattleColoseum.getInstance().startColoseum();
		} catch (Exception e) {
			e.printStackTrace();
			_log.log(Level.SEVERE, "L1UltimateBattle[]Error", e);
		}		
	}	

	public void addMember(L1PcInstance pc) {
		if (!_members.contains(pc)) {
			_members.add(pc);
		}
	}

	public void removeMember(L1PcInstance pc) {
		_members.remove(pc);
	}

	public void clearMembers() {
		_members.clear();
	}

	public boolean isMember(L1PcInstance pc) {
		return _members.contains(pc);
	}

	public L1PcInstance[] getMembersArray() {
		return _members.toArray(new L1PcInstance[_members.size()]);
	}

	public int getMembersCount() {
		return _members.size();
	}

	private void setNowUb(boolean i) {
		_isNowUb = i;
	}

	public boolean isNowUb() {
		return _isNowUb;
	}

	public int getUbId() {
		return _ubId;
	}

	public void setUbId(int id) {
		_ubId = id;
	}

	public short getMapId() {
		return _mapId;
	}

	public void setMapId(short mapId) {
		this._mapId = mapId;
	}

	public int getMinLevel() {
		return _minLevel;
	}

	public void setMinLevel(int level) {
		_minLevel = level;
	}

	public int getMaxLevel() {
		return _maxLevel;
	}

	public void setMaxLevel(int level) {
		_maxLevel = level;
	}

	public int getMaxPlayer() {
		return _maxPlayer;
	}

	public void setMaxPlayer(int count) {
		_maxPlayer = count;
	}

	public void setEnterRoyal(boolean enterRoyal) {
		this._enterRoyal = enterRoyal;
	}

	public void setEnterKnight(boolean enterKnight) {
		this._enterKnight = enterKnight;
	}

	public void setEnterMage(boolean enterMage) {
		this._enterMage = enterMage;
	}

	public void setEnterElf(boolean enterElf) {
		this._enterElf = enterElf;
	}

	public void setEnterDarkelf(boolean enterDarkelf) {
		this._enterDarkelf = enterDarkelf;
	}

	public void setEnterDragonknight(boolean enterDragonknight) {
		this._enterDragonknight = enterDragonknight;
	}

	public void setEnterIllusionist(boolean enterIllusionist) {
		this._enterIllusionist = enterIllusionist;
	}

	public void setEnterWarrior(boolean enterWarrior) {
		this._enterWarrior = enterWarrior;
	}
	
	public void setEnterFencer(boolean enterFencer) {
		this._enterFencer = enterFencer;
	}
	
	public void setEnterLancer(boolean enterLancer) {
		this._enterLancer = enterLancer;
	}

	public void setEnterMale(boolean enterMale) {
		this._enterMale = enterMale;
	}

	public void setEnterFemale(boolean enterFemale) {
		this._enterFemale = enterFemale;
	}

	public boolean canUsePot() {
		return _usePot;
	}

	public void setUsePot(boolean usePot) {
		this._usePot = usePot;
	}

	public int getHpr() {
		return _hpr;
	}

	public void setHpr(int hpr) {
		this._hpr = hpr;
	}

	public int getMpr() {
		return _mpr;
	}

	public void setMpr(int mpr) {
		this._mpr = mpr;
	}

	public int getLocX1() {
		return _locX1;
	}

	public void setLocX1(int locX1) {
		this._locX1 = locX1;
	}

	public int getLocY1() {
		return _locY1;
	}

	public void setLocY1(int locY1) {
		this._locY1 = locY1;
	}

	public int getLocX2() {
		return _locX2;
	}

	public void setLocX2(int locX2) {
		this._locX2 = locX2;
	}

	public int getLocY2() {
		return _locY2;
	}

	public void setLocY2(int locY2) {
		this._locY2 = locY2;
	}

	public void resetLoc() {
		_locX = (_locX2 + _locX1) >> 1;
		_locY = (_locY2 + _locY1) >> 1;
		_location = new L1Location(_locX, _locY, _mapId);
	}

	public L1Location getLocation() {
		return _location;
	}

	public void addManager(int npcId) {
		_managers.add(npcId);
	}

	public boolean containsManager(int npcId) {
		return _managers.contains(npcId);
	}

	public void addUbTime(int time) {
		_ubTimes.add(time);
	}

	public String getNextUbTime() {
		return intToTimeFormat(nextUbTime());
	}

	private int nextUbTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		int nowTime = Integer.valueOf(sdf.format(getRealTime().getTime()));
		SortedSet<Integer> tailSet = _ubTimes.tailSet(nowTime);
		if (tailSet.isEmpty()) {
			tailSet = _ubTimes;
		}
		return tailSet.first();
	}

	private static String intToTimeFormat(int n) {
		return n / 100 + StringUtil.ColonString + n % 100 / 10 + StringUtil.EmptyString + n % 10;
	}

	private static Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.SERVER.TIME_ZONE);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}

	public boolean checkUbTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		Calendar realTime = getRealTime();
		realTime.add(Calendar.MINUTE, BEFORE_MINUTE + 5); // 10분전부터 시작해야 멘트를 띄울수 있다
		int nowTime = Integer.valueOf(sdf.format(realTime.getTime()));
		return _ubTimes.contains(nowTime);
	}

	private void setActive(boolean f) {
		_active = f;
	}

	public boolean isActive() {
		return _active;
	}

	public boolean canPcEnter(L1PcInstance pc) {
		_log.log(Level.FINE, "pcname=" + pc.getName() + " ubid=" + _ubId + " minlvl=" + _minLevel + " maxlvl=" + _maxLevel);
		if (!IntRange.includes(pc.getLevel(), _minLevel, _maxLevel)) {
			return false;
		}
		if (!((pc.isCrown() && _enterRoyal) 
	       || (pc.isKnight() && _enterKnight)
	       || (pc.isWizard() && _enterMage)
	       || (pc.isElf() && _enterElf)
	       || (pc.isDarkelf() && _enterDarkelf)
	       || (pc.isDragonknight() && _enterDragonknight) 
	       || (pc.isIllusionist() && _enterIllusionist)
	       || (pc.isWarrior() && _enterWarrior)
	       || (pc.isFencer() && _enterFencer)
	       || (pc.isLancer() && _enterLancer))) {
			return false;
		}
		return true;
	}

	private String[] _ubInfo;
	public String[] makeUbInfoStrings() {
		if (_ubInfo != null) {
			return _ubInfo;
		}
		String nextUbTime = getNextUbTime();
		StringBuilder classesBuff = new StringBuilder();
		/*if (_enterIllusionist)
			classesBuff.append("환술사 ");
		if (_enterDragonknight)
			classesBuff.append("용기사 ");
		if (_enterDarkelf)
			classesBuff.append("다크엘프 ");
		if (_enterMage)
			classesBuff.append("마법사 ");
		if (_enterElf)
			classesBuff.append("요정 ");
		if (_enterKnight)
			classesBuff.append("기사 ");
		if (_enterRoyal)
			classesBuff.append("군주 ");
		if (_enterWarrior)
			classesBuff.append("전사 ");
		if (_enterFencer)
			classesBuff.append("검사 ");
		if (_enterLancer)
			classesBuff.append("창기사 ");*/
			if (_enterIllusionist)
			classesBuff.append(S_SystemMessage.getRefText(1027) + " ");
		if (_enterDragonknight)
			classesBuff.append(S_SystemMessage.getRefText(1155) + " ");
		if (_enterDarkelf)
			classesBuff.append(S_SystemMessage.getRefText(1161) + " ");
		if (_enterMage)
			classesBuff.append(S_SystemMessage.getRefText(1164) + " ");
		if (_enterElf)
			classesBuff.append(S_SystemMessage.getRefText(1177) + " ");
		if (_enterKnight)
			classesBuff.append(S_SystemMessage.getRefText(1181) + " ");
		if (_enterRoyal)
			classesBuff.append(S_SystemMessage.getRefText(1189) + " ");
		if (_enterWarrior)
			classesBuff.append(S_SystemMessage.getRefText(1191) + " ");
		if (_enterFencer)
			classesBuff.append(S_SystemMessage.getRefText(1198) + " ");
		if (_enterLancer)
			classesBuff.append(S_SystemMessage.getRefText(1218) + " ");
		String classes = classesBuff.toString().trim();

		StringBuilder genderBuff = new StringBuilder();
		if (_enterMale) {
			//genderBuff.append("남자 ");
			genderBuff.append(S_SystemMessage.getRefText(1233) + " ");
		}
		if (_enterFemale) {
			//genderBuff.append("여자 ");
			genderBuff.append(S_SystemMessage.getRefText(1258) + " ");
		}
		String gender = genderBuff.toString().trim();
		String loLevel = String.valueOf(_minLevel);
		String hiLevel = String.valueOf(_maxLevel);
		String teleport = _location.getMap().isTeleportable() ? S_SystemMessage.getRefText(1295) : S_SystemMessage.getRefText(1296);
		String res = _location.getMap().isUseResurrection() ? S_SystemMessage.getRefText(1295) : S_SystemMessage.getRefText(1296);
		String pot = S_SystemMessage.getRefText(1295);
		String hpr = String.valueOf(_hpr);
		String mpr = String.valueOf(_mpr);
		String summon = _location.getMap().isTakePets() ? S_SystemMessage.getRefText(1295) : S_SystemMessage.getRefText(1296);
		String summon2 = _location.getMap().isRecallPets() ? S_SystemMessage.getRefText(1295) : S_SystemMessage.getRefText(1296);
		_ubInfo = new String[] { nextUbTime, classes, gender, loLevel, hiLevel, teleport, res, pot, hpr, mpr, summon, summon2 };
		return _ubInfo;
	}
}


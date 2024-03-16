package l1j.server.server.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.ChatType;
import l1j.server.common.data.Material;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.RaceTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.templates.L1Racer;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class DollRaceController implements Runnable {
	private static DollRaceController _instance;
	
	public static int ratio_normal = 70;
	public static int ratio_gura = 15;
	public static int ratio_big_gura = 15;
	public static int ratio_good_down = 40;
	public static int ratio_average_down = 50;
	public static int ratio_bad_down = 60;
	public static int ratio_average_minimum = 285;
	public static int ratio_bad_minimum = 290;
	public static int ratio_good_minimum = 280;
	public static int ratio_average_maximum = 320;
	public static int ratio_bad_maximum = 310;
	public static int ratio_good_maximum = 300;

	private static final int RACE_INTERVAL = 1 * 60 * 1000;

	private static final int EXECUTE_STATUS_NONE		= 0;
	private static final int EXECUTE_STATUS_PREPARE		= 1;
	private static final int EXECUTE_STATUS_READY		= 2;
	private static final int EXECUTE_STATUS_STANDBY		= 3;
	private static final int EXECUTE_STATUS_PROGRESS	= 4;
	private static final int EXECUTE_STATUS_FINALIZE	= 5;

	private int _executeStatus = EXECUTE_STATUS_NONE;

	private int _raceCount = 1;
	long _nextRaceTime = System.currentTimeMillis() + 60 * 1000;
	private int _DollRaceState = 2;

	private int _ticketSellRemainTime;
	private int _raceWatingTime;
	private int _currentBroadcastRacer;

	//ArrayList<L1NpcInstance> _npc = new ArrayList<L1NpcInstance>();
	L1NpcInstance[] _npc = new L1NpcInstance[3];
	ArrayList<L1DoorInstance> _door = new ArrayList<L1DoorInstance>();

	public int[] _ticketCount		= new int[5];
	public int[] _ticketTaxCount	= new int[5];
	public int[] _ticketId			= new int[5];
	private static DecimalFormat _df = new DecimalFormat("#.#");

	private int _ranking = 0;
	private boolean _complete = false;

	List<L1ShopItem> _purchasingList = new ArrayList<L1ShopItem>();
	public L1NpcInstance[] _doll = new L1NpcInstance[5];

	private static final int Start_X[] = { 33522, 33520, 33518, 33516, 33514 };
	private static final int Start_Y[] = { 32861, 32863, 32865, 32867, 32869 };
	private static final int[][] GFX = { 
		{ 16081, 16082, 16083, 16084, 16085 }, 
		{ 16086, 16087, 16088, 16089, 16090 },
		{ 16091, 16092, 16093, 16094, 16095 }, 
		{ 16096, 16097, 16098, 16099, 16100 }
	};
	
	private int[][] Number = { 
		{ 1, 2, 3, 4, 5 }, 
		{ 6, 7, 8, 9, 10 },
		{ 11, 12, 13, 14, 15 }, 
		{ 16, 17, 18, 19, 20 } 
	};
	
	private String[][] DollName = { 
		/*{ "카츠" ,"얼녀" , "바포" , "라기" , "랑카" }, 
		{ "데스" , "모니" , "마미" , "뱀파" , "아리" },
		{ "시어" , "나발" , "싸이" , "리치" , "다골" },
		{ "코아" , "퀴니" , "까미" , "자이" , "라바"}*/
		{ "$26190" ,"$26191" , "$26192" , "$26193" , "$26194" }, 
		{ "$26195" , "$26196" , "$26197" , "$26198" , "$26199" },
		{ "$26200" , "$26201" , "$26202" , "$26203" , "$26204" },
		{ "$26205" , "$26206" , "$26207" , "$26208" , "$26209"}
	};
	
	private static int[] _time		= new int[5];
	private static int[] _downRate	= new int[5];
	
	// 티켓 초기화
	private int[] ticket			= { 0, 0, 0, 0, 0 };
	// 승률 초기화
	public double[] _winRate		= { 0, 0, 0, 0, 0 };
	// 상태 초기화
	public String[] _dollCondition	= { "nervous", "nervous", "nervous", "nervous", "nervous" };
	// 배율 초기화
	private double _ration[]		= { 0, 0, 0, 0, 0 };

	public static DollRaceController getInstance() {
		if(_instance == null)_instance = new DollRaceController();
		return _instance;
	}
	
	private DollRaceController(){
		initNpc();
		initDoor();
	}
	
	public boolean _GMBroadcast = false;
	
	private int _winDoll = -1;

	public void setWinDoll(int i){
		if (i > 0 && i <= 5)
			_winDoll = i;
	}
	
	@Override
	public void run() {
		try {
			switch (_executeStatus) {
			case EXECUTE_STATUS_NONE:
				if (checkStartRace()) {
					initRaceGame();
					_executeStatus = EXECUTE_STATUS_PREPARE;
					GeneralThreadPool.getInstance().schedule(this, 1000L); // 1분
				} else {
					GeneralThreadPool.getInstance().schedule(this, 1000L); // 1초
				}
				break;
			case EXECUTE_STATUS_PREPARE:
				startSellTicket();
				_executeStatus = EXECUTE_STATUS_READY;
				GeneralThreadPool.getInstance().schedule(this, 1000L);
				break;
			case EXECUTE_STATUS_READY:
				long remainTime = checkTicketSellTime();
				if (remainTime > 0) {
					GeneralThreadPool.getInstance().schedule(this, remainTime);
				} else {
					_executeStatus = EXECUTE_STATUS_STANDBY;
					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
				break;
			case EXECUTE_STATUS_STANDBY:
				if (checkWatingTime()) {
					startDollRace();
					_executeStatus = EXECUTE_STATUS_PROGRESS;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000L);
				break;
			case EXECUTE_STATUS_PROGRESS:
				if (broadcastBettingRate()) {
					if (_complete) {
						_executeStatus = EXECUTE_STATUS_FINALIZE;
					}
				}
				GeneralThreadPool.getInstance().schedule(this, 1000L);
				break;
			case EXECUTE_STATUS_FINALIZE:
				wrapUpRace();
				_executeStatus = EXECUTE_STATUS_NONE;
				GeneralThreadPool.getInstance().schedule(this, Config.ALT.DOLL_RACE_TIME * 1000L);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean checkStartRace() {
		long currentTime = System.currentTimeMillis();
		if (_nextRaceTime < currentTime) {
			_nextRaceTime = currentTime + RACE_INTERVAL;
			return true;
		}
		return false;
	}

	private void initRaceGame() {
		try {
			_ranking	= 0;
			_complete	= false;
			_winDoll	= -1;
			initNpc(); // fix a bug. The NPC does not shout
			broadcastNpc(S_SystemMessage.getRefText(1328));
			// 토탈 판매 장수 초기화
			initTicketCount();
			// 상점 Npc초기화
			initShopNpc();
			// 인형 달리기 속도 지정
			sleepTime();
			// 인형 초기화 및 로딩
			loadDoll();
			// 승률 초기화
			initWinRate();
			// 게임시
			doorAction(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initTicketCount() {
		for (int row = 0; row < 5; row++) {
			_ticketCount[row] = 0;
		}
	}

	// 레이스 Npc를 담는다
	private void initNpc() {
		L1NpcInstance n = null;
		for (Object obj : L1World.getInstance().getVisibleObjects(4).values()) {
			if (obj instanceof L1NpcInstance) {
				n = (L1NpcInstance) obj;
			
				if (n.getNpcTemplate().getNpcId() == 70035) {
					_npc[0] = n;
				}
				if (n.getNpcTemplate().getNpcId() == 70041) {
					_npc[1] = n;
				}
				if (n.getNpcTemplate().getNpcId() == 70042) {
					_npc[2] = n;
				}
								
				/*
				L1NpcInstance npc = (L1NpcInstance) obj;
				int npcId = npc.getNpcTemplate().getNpcId();
				
				if(npcId == 70035 || npcId == 70041 || npcId == 70042){
					_npc.add(npc);
				}
				*/
			}
		}
	}
	
	// 레이스 door을 담는다
	private void initDoor(){
		for(L1DoorInstance door : L1World.getInstance().getAllDoor()){
			if(door != null && door.getSpriteId() == 1487 && door.getMapId() == 4) {
				_door.add(door);
			}
		}
	}

	private void initShopNpc() {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		L1Shop shop = new L1Shop(70035, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(70035, shop);
		L1Shop shop1 = new L1Shop(70041, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(70041, shop1);
		L1Shop shop2 = new L1Shop(70042, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(70042, shop2);
	}

	private double getWinRate(int id) {
		int total = this.getTotalTicketCount();
		int cnt = this._ticketCount[id];
		if(total == 0)total = 1;
		if(cnt == 0)cnt = 1;
		double rate = (double) total * 0.98 / (double) cnt;
		if(rate < 1.0)rate = 1.0;
		if(rate > 32)rate = 30 + CommonUtil.nextFloat() * 2;
		return Double.parseDouble(_df.format(rate));
	}
	
	private void sleepTime() {
		int ratio = CommonUtil.random(ratio_normal + ratio_gura + ratio_big_gura);
		if (ratio < ratio_normal)
			GameServerSetting.CHANGE_DOLL_RACE_SPEED = 0;
		else if (ratio < ratio_normal + ratio_gura)
			GameServerSetting.CHANGE_DOLL_RACE_SPEED = 1;
		else if (ratio < ratio_normal + ratio_gura + ratio_big_gura)
			GameServerSetting.CHANGE_DOLL_RACE_SPEED = 2;

		for (int i = 0; i < 5; i++) {
			int bugState = CommonUtil.random(3);
			int addValue = 0;
			int baseValue = 280;
			switch (bugState) {
			case 1:
				_dollCondition[i] = "nervous";
				if (GameServerSetting.CHANGE_DOLL_RACE_SPEED == 0) {
					addValue = CommonUtil.random(ratio_good_maximum - ratio_good_minimum);
					_downRate[i] = ratio_good_down;
				} else {
					addValue = CommonUtil.random(ratio_good_maximum - ratio_good_minimum) + 10;
					_downRate[i] = 10 + ratio_good_down;
				}
				baseValue = ratio_good_minimum;
				break;
			case 2:
				_dollCondition[i] = "excited";
				if (GameServerSetting.CHANGE_DOLL_RACE_SPEED == 2) {
					addValue = CommonUtil.random(ratio_bad_maximum - ratio_bad_minimum) - 20;
					_downRate[i] = ratio_bad_down - 10;
				} else {
					addValue = CommonUtil.random(ratio_bad_maximum - ratio_bad_minimum);
					_downRate[i] = ratio_bad_down;
				}
				baseValue = ratio_bad_minimum;
				break;
			default:
				_dollCondition[i] = "calm";
				if (GameServerSetting.CHANGE_DOLL_RACE_SPEED == 1) {
					addValue = CommonUtil.random(ratio_average_maximum - ratio_average_minimum) - 10;
					_downRate[i] = ratio_average_down - 5;
				} else if (GameServerSetting.CHANGE_DOLL_RACE_SPEED == 0) {
					addValue = CommonUtil.random(ratio_average_maximum - ratio_average_minimum);
					_downRate[i] = ratio_average_down;
				} else {
					addValue = CommonUtil.random(ratio_average_maximum - ratio_average_minimum) + 10;
					_downRate[i] = 5 + ratio_average_down;
				}
				baseValue = ratio_average_minimum;
				break;
			}
			_time[i] = baseValue + addValue;
		}
	}

	// 승률처리
	private void initWinRate() {
		L1Racer racer = null;
		for (int i = 0; i < 5; i++) {
			racer = RaceTable.getInstance().getTemplate(_doll[i].getNum());
			double rate = (double) racer.getWinCount() * 100.0 / (double) (racer.getWinCount() + racer.getLoseCount());
			//_winRate[i] = Double.parseDouble(_df.format(rate));
			_winRate[i] = rate;
		}
	}
	
	private void run_process() {
		int a1 = 0;
		int a2 = 0;
		for (int a = 0; a < 4; ++a) {
			for (int i = 0; i < 5; ++i) {
				try {
					Number[a][i] = 0;
				} catch (Exception e) {
				}
			}
		}
		while (true) {
			int value = CommonUtil.random(20) + 1;
			boolean ck = false;
			for (int a = 0; a < 4; ++a) {
				for (int i = 0; i < 5; ++i) {
					try {
						if (value == Number[a][i])
							ck = true;
					} catch (Exception e) {
					}
				}
			}
			if (ck)
				continue;
			Number[a1][a2] = value;
			a2++;
			if (a2 > 4) {
				a1++;
				a2 = 0;
				if (a1 > 3)
					break;
			}
		}
	}

	private void loadDoll() {
		run_process();
		L1Npc doll = null;
		for (int m = 0; m < 5; ++m) {
			try {
				int randNum = CommonUtil.random(4);
				doll = new L1Npc();
				if(_dollCondition[m].equals("nervous"))			doll.setPassiSpeed(420 - CommonUtil.random(80));	
				else if(_dollCondition[m].equals("calm"))		doll.setPassiSpeed(420 - CommonUtil.random(60));	
				else if(_dollCondition[m].equals("excited"))	doll.setPassiSpeed(420 - CommonUtil.random(40));	
				doll.setFamily(0);
				doll.setAgroFamily(0);
				doll.setPicupItem(false);

				Object[] parameters = { doll };

				_doll[m] = (L1NpcInstance) Class.forName("l1j.server.server.model.Instance.L1NpcInstance").getConstructors()[0].newInstance(parameters);
				_doll[m].setSpriteId(GFX[randNum][m]);
				_doll[m].setDesc(DollName[randNum][m]);
				_doll[m].setName(_doll[m].getDesc());
				_doll[m].setNum(Number[randNum][m]);
				_doll[m].setX(Start_X[m]);
				_doll[m].setY(Start_Y[m]);
				_doll[m].setMap((short) 4);
				_doll[m].getMoveState().setHeading(6);
				_doll[m].setId(IdFactory.getInstance().nextId());
				_doll[m].setAtkMagicspeed(_doll[m].getSprite().getActionSpeed(ActionCodes.ACTION_AltAttack) + 100);
				L1World world = L1World.getInstance();
				world.storeObject(_doll[m]);
				world.addVisibleObject(_doll[m]);
				for (L1PcInstance member : world.getVisiblePlayer(_doll[m])) {
					if (member != null)
						member.updateObject();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void broadcastNpc(String msg) {
		if(_npc[0] != null)_npc[0].broadcastPacket(new S_NpcChatPacket(_npc[0], msg, ChatType.CHAT_SHOUT), true);
		if(_npc[1] != null)_npc[1].broadcastPacket(new S_NpcChatPacket(_npc[1], msg, ChatType.CHAT_SHOUT), true);
		if(_npc[2] != null)_npc[2].broadcastPacket(new S_NpcChatPacket(_npc[2], msg, ChatType.CHAT_SHOUT), true);
		/*for (L1NpcInstance npc : _npc) {
			npc.broadcastPacket(new S_NpcChatPacket(npc, msg, ChatType.CHAT_SHOUT), true);
		}*/
	}
	private void winnerComment(String msg) {
		if(_npc[0] != null)_npc[0].broadcastPacket(new S_NpcChatPacket(_npc[0], msg, ChatType.CHAT_SHOUT), true);
		if(_npc[1] != null)_npc[1].broadcastPacket(new S_NpcChatPacket(_npc[1], msg, ChatType.CHAT_SHOUT), true);
		if(_npc[2] != null)_npc[2].broadcastPacket(new S_NpcChatPacket(_npc[2], msg, ChatType.CHAT_SHOUT), true);
		/*for (L1NpcInstance npc : _npc) {
			npc.broadcastPacket(new S_NpcChatPacket(npc, msg, ChatType.CHAT_SHOUT), true);
		}*/
	}

	private void doorAction(boolean open) {
		for(L1DoorInstance door : _door){
			if(open && door.getOpenStatus() == ActionCodes.ACTION_Close)
				door.open();
			else if(!open && door.getOpenStatus() == ActionCodes.ACTION_Open)
				door.close();
		}
	}

	private void startSellTicket() {
		LoadNpcShopList();
		broadcastNpc(S_SystemMessage.getRefText(1329));
		setDollState(0);
		_ticketSellRemainTime = 60 * Config.ALT.DOLL_RACE_TICKETS_TIME;
	}

	private long checkTicketSellTime() {
		if (_ticketSellRemainTime == 5 * 60) {// 5분 남은 경우
			_ticketSellRemainTime -= 60;
			broadcastNpc(S_SystemMessage.getRefText(1330));
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 4 * 60) {// 4분 남은 경우
			_ticketSellRemainTime -= 60;
			broadcastNpc(S_SystemMessage.getRefText(1331));
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 3 * 60) {// 3분 남은 경우
			_ticketSellRemainTime -= 60;
			broadcastNpc(S_SystemMessage.getRefText(1332));
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 2 * 60) {// 2분 남은 경우
			_ticketSellRemainTime -= 60;
			broadcastNpc(S_SystemMessage.getRefText(1333));
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 1 * 60) {// 1분 남은 경우
			_ticketSellRemainTime -= 45;
			broadcastNpc(S_SystemMessage.getRefText(1334));
			return 45 * 1000;
		} else if (_ticketSellRemainTime == 15) {
			_ticketSellRemainTime = 0;
			broadcastNpc(S_SystemMessage.getRefText(1335));
			return 15 * 1000;
		}

		initShopNpc();
		broadcastNpc(S_SystemMessage.getRefText(1336));
		SettingRate();
		_raceWatingTime = 10;
		setDollState(1);
		return 0;
	}

	private boolean checkWatingTime() {
		if (_raceWatingTime > 0) {
			broadcastNpc(S_SystemMessage.getRefText(1337) + _raceWatingTime + S_SystemMessage.getRefText(1338));
			--_raceWatingTime;
			return false;
		}
		return true;
	}

	private void startDollRace() {
		broadcastNpc(S_SystemMessage.getRefText(1339));
		doorAction(true);
		StartGame();
		castleTaxPut();
		_currentBroadcastRacer = 0;
	}

	private void castleTaxPut() {
		// TODO 자동 생성된 메소드 스텁
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				// TODO 자동 생성된 메소드 스텁
				L1Castle castle = CastleTable.getInstance().getCastleTable(2); // 오성
				synchronized (castle) {
					int money = castle.getPublicMoney();
					int count = 0;
					for (int i = 0; i < 5; i++) {
						count += _ticketTaxCount[i];
					}
					double castleTax = count * 500 * 0.10;
					if (2000000000 > money + castleTax) {
						money = (int) (money + castleTax);
						castle.setPublicMoney(money);
						CastleTable.getInstance().updateCastle(castle);
					}
				}
				for (int i = 0; i < 5; i++) {
					_ticketTaxCount[i] = 0;
				}
			}
		}, 1);
	}

	private boolean broadcastBettingRate() {
		if(_currentBroadcastRacer == 5)return true;
		if(_currentBroadcastRacer == 0)broadcastNpc(S_SystemMessage.getRefText(1322));
		broadcastNpc(_doll[_currentBroadcastRacer].getDesc() + ": " + _ration[_currentBroadcastRacer] + StringUtil.EmptyOneString);
		++_currentBroadcastRacer;
		return false;
	}
	
	private void SettingRate() {
		for (int row = 0; row < 5; row++) {
			_ration[row] = Double.parseDouble(_df.format(getWinRate(row)));
		}
		if (_GMBroadcast == true){
		    for (L1PcInstance gm : L1World.getInstance().getAllGms()) {
			    if (gm == null)continue;
//AUTO SRM: 			    gm.sendPackets(new S_SystemMessage("인형경주 티켓판매 현황"), true); // CHECKED OK
			    gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(939), true), true);
			    for (int i = 0; i < 5; i++) {
//AUTO SRM: 			    	gm.sendPackets(new S_SystemMessage(_doll[i].getName() + "의 판매 티켓은" + _ticketCount[i]+"장 입니다."), true); // CHECKED OK
			    	gm.sendPackets(new S_SystemMessage(_doll[i].getName()  + S_SystemMessage.getRefText(940) + _ticketCount[i] + S_SystemMessage.getRefText(941), true), true);
			    }
		    }
		}
	}

	private void AddWinCount(int j) {
		L1Racer racer = RaceTable.getInstance().getTemplate(_doll[j].getNum());
		racer.setWinCount(racer.getWinCount() + 1);
		racer.setLoseCount(racer.getLoseCount());
		SaveAllRacer(racer, _doll[j].getNum());
	}

	private void AddLoseCount(int j) {
		L1Racer racer = RaceTable.getInstance().getTemplate(_doll[j].getNum());
		racer.setWinCount(racer.getWinCount());
		racer.setLoseCount(racer.getLoseCount() + 1);
		SaveAllRacer(racer, _doll[j].getNum());
	}

	private void SaveAllRacer(L1Racer racer, int num) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE util_racer SET WinCount=?, LoseCount=? WHERE Num=" + num);
			pstm.setInt(1, racer.getWinCount());
			pstm.setInt(2, racer.getLoseCount());
			pstm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[::::::] SaveAllRacer Method error occurred");
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void SetWinRaceTicketPrice(int id, double rate) {
		L1ShopItem newItem = new L1ShopItem(id, (int) (500 * rate), 1);
		_purchasingList.add(newItem);
		initShopNpc();
	}

	private void LoadNpcShopList() {
		try {
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();

			for (int i = 0; i < 5; i++) {
				ticket[i] = ItemTable.getInstance().GetRaceTicketId();
				SaveRace(ticket[i], S_SystemMessage.getRefText(1340) + _doll[i].getDesc() + StringUtil.MinusString + (i + 1));
				L1ShopItem item = new L1ShopItem(ticket[i], 500, 1);
				sellingList.add(item);

				_ticketId[i] = ticket[i];
			}
			
			for(int i = 0; i < 5; i++)
				sellingList.add(new L1ShopItem(ticket[i], 25000000, 50000));
			for(int i = 0; i < 5; i++)
				sellingList.add(new L1ShopItem(ticket[i], 50000000, 100000));

			ShopTable.getInstance().addShop(70035, new L1Shop(70035, sellingList, _purchasingList));
			ShopTable.getInstance().addShop(70041, new L1Shop(70041, sellingList, _purchasingList));
			ShopTable.getInstance().addShop(70042, new L1Shop(70042, sellingList, _purchasingList));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /** 레이스 표 **/
	private void SaveRace(int itemId, String desc) {
		L1RaceTicket etcItem = new L1RaceTicket();
		etcItem.setItemType(L1ItemType.NORMAL);
		etcItem.setItemId(itemId);
		etcItem.setDescKr(desc);
		etcItem.setDescEn(desc);
		etcItem.setDesc(desc);
		etcItem.setType(12);
		etcItem.setMaterial(Material.PAPER);
		etcItem.setWeight(0);
		etcItem.setTicketPrice(500);
		etcItem.setIconId(5427);
		etcItem.setSpriteId(18168);
		etcItem.setMinLevel(0);
		etcItem.setMaxLevel(0);
		etcItem.setBless(1);
		etcItem.setTradable(false);
		etcItem.setCantSell(true);
		etcItem.setDmgSmall(0);
		etcItem.setDmgLarge(0);
		etcItem.set_stackable(true);
		etcItem.setTradable(true);

		ItemTable.getInstance().AddTicket(etcItem);
	}

	private void goalIn(int i) {
		synchronized (this) {
			_ranking = _ranking + 1;
			if (_ranking == 1) {
				SetWinRaceTicketPrice(ticket[i], _ration[i]);
				ItemTable.getInstance().updateTicketPrice(ticket[i], (int) (_ration[i] * 500));
				AddWinCount(i);
				//winnerComment("제 " + _raceCount + " 회 우승자는 '" + _doll[i].getDesc() + "' 입니다.");
				//winnerComment("The winner of round " + _raceCount + " is '" + _doll[i].getDesc() + "'.");
				winnerComment(S_SystemMessage.getRefText(1323) + _raceCount +  " " + S_SystemMessage.getRefText(1341) + "'" + _doll[i].getDesc() + "'" + S_SystemMessage.getRefText(1325));			
			} else {
				SetWinRaceTicketPrice(ticket[i], 0);
				ItemTable.getInstance().updateTicketPrice(ticket[i], 0);
				AddLoseCount(i);
			}
		}

		setDollState(2);
		if(_ranking == 5)_complete = true;
	}

	private void wrapUpRace() throws Exception {
		_doll[0].deleteMe();
		_doll[1].deleteMe();
		_doll[2].deleteMe();
		_doll[3].deleteMe();
		_doll[4].deleteMe();
		setDollState(2);
		_raceCount = _raceCount + 1;
		broadcastNpc(S_SystemMessage.getRefText(1342));
	}

	private void StartGame() {
		for (int i = 0; i < 5; ++i) {
			RunDoll doll = new RunDoll(i);
			GeneralThreadPool.getInstance().schedule(doll, 100);
		}
	}

	private class RunDoll implements Runnable {
		private int _status = 0;
		L1NpcInstance _racer;
		private int[][] _DOLL_INFO = { 
			{ 45, 4, 5, 6 }, 
			{ 42, 6, 5, 7 },
			{ 39, 8, 5, 8 }, 
			{ 36, 10, 5, 9 }, 
			{ 33, 12, 5, 10 }
		};

		private int _dollId;
		private int _remainRacingCount;

		private RunDoll(int dollId) {
			_dollId = dollId;
			_racer	= _doll[_dollId];
			_remainRacingCount = _DOLL_INFO[_dollId][0];
		}

		@Override
		public void run() {
			try {
				if(_winDoll != -1)_doll[_winDoll - 1].getNpcTemplate().setPassiSpeed(300);
				switch (_status) {
				case 0:
					if (_remainRacingCount == 0) {
						_remainRacingCount = _DOLL_INFO[_dollId][1];
						_status = 1;					
					} else {
						if (CommonUtil.random(100) < 1 && CommonUtil.random(100) > (int) (_winRate[_dollId]) && _dollId != _winDoll - 1) {
							_racer.broadcastPacket(new S_DoActionGFX(_racer.getId(),  ActionCodes.ACTION_AltAttack), true);
							GeneralThreadPool.getInstance().schedule(this, _racer.getAtkMagicspeed());
						} else if(CommonUtil.random(150) < 1 && _dollId != _winDoll - 1){
							_racer.broadcastPacket(new S_NpcChatPacket(_racer, "$16698", ChatType.CHAT_NORMAL), true);
							GeneralThreadPool.getInstance().schedule(this, 3360);
						} else {
							_racer.setDirectionMove(6);
							--_remainRacingCount;
							GeneralThreadPool.getInstance().schedule(this, _racer.getNpcTemplate().getPassiSpeed());
						}
						break;
					}
				case 1:
					if (_remainRacingCount == 0) {
						_remainRacingCount = _DOLL_INFO[_dollId][2];
						_status = 2;
					} else {
						if (CommonUtil.random(100) < 1 && CommonUtil.random(100) > (int) (_winRate[_dollId]) && _dollId != _winDoll - 1) {
							_racer.broadcastPacket(new S_DoActionGFX(_racer.getId(),  ActionCodes.ACTION_AltAttack), true);
							GeneralThreadPool.getInstance().schedule(this, _doll[_dollId].getAtkMagicspeed());
						} else if(CommonUtil.random(150) < 1 && _dollId != _winDoll - 1){
							_racer.broadcastPacket(new S_NpcChatPacket(_racer, "$16698", ChatType.CHAT_NORMAL), true);
							GeneralThreadPool.getInstance().schedule(this, 3360);
						} else {
							_racer.setDirectionMove(7);
							--_remainRacingCount;
							GeneralThreadPool.getInstance().schedule(this, _racer.getNpcTemplate().getPassiSpeed());
						}
						break;
					}
				case 2:
					if (_remainRacingCount == 0) {
						_remainRacingCount = _DOLL_INFO[_dollId][3];
						_status = 3;
					} else {
						if (CommonUtil.random(100) < 1 && CommonUtil.random(100) > (int) (_winRate[_dollId]) && _dollId != _winDoll - 1) {
							_racer.broadcastPacket(new S_DoActionGFX(_racer.getId(),  ActionCodes.ACTION_AltAttack), true);
							GeneralThreadPool.getInstance().schedule(this, _racer.getAtkMagicspeed());
						} else if(CommonUtil.random(150) < 1 && _dollId != _winDoll - 1){
							_racer.broadcastPacket(new S_NpcChatPacket(_racer, "$16698", ChatType.CHAT_NORMAL), true);
							GeneralThreadPool.getInstance().schedule(this, 3360);
						} else {
							_racer.setDirectionMove(0);
							--_remainRacingCount;
							GeneralThreadPool.getInstance().schedule(this, _racer.getNpcTemplate().getPassiSpeed());
						}
						break;
					}
				case 3:
					if (_remainRacingCount == 0) {
						_status = 4;
					} else {
						if (CommonUtil.random(100) < 1 && CommonUtil.random(100) > (int) (_winRate[_dollId]) && _dollId != _winDoll - 1) {
							_racer.broadcastPacket(new S_DoActionGFX(_racer.getId(),  ActionCodes.ACTION_AltAttack), true);
							GeneralThreadPool.getInstance().schedule(this, _racer.getAtkMagicspeed());
						} else if(CommonUtil.random(150) < 1 && _dollId != _winDoll - 1){
							_racer.broadcastPacket(new S_NpcChatPacket(_racer, "$16698", ChatType.CHAT_NORMAL), true);
							GeneralThreadPool.getInstance().schedule(this, 3360);
						} else {
							_racer.setDirectionMove(1);
							--_remainRacingCount;
							GeneralThreadPool.getInstance().schedule(this, _racer.getNpcTemplate().getPassiSpeed());
						}
						break;
					}
				case 4:
					if (_racer.getX() == 33527) {
						goalIn(_dollId);
					} else if (_racer.getX() < 33522 && CommonUtil.random(100) < 1 && CommonUtil.random(100) > (int) (_winRate[_dollId]) && _dollId != _winDoll - 1) {
						_racer.broadcastPacket(new S_DoActionGFX(_racer.getId(),  ActionCodes.ACTION_AltAttack), true);
						GeneralThreadPool.getInstance().schedule(this, _racer.getAtkMagicspeed());
					} else if(CommonUtil.random(150) < 1 && _dollId != _winDoll - 1){
						_racer.broadcastPacket(new S_NpcChatPacket(_racer, "$16698", ChatType.CHAT_NORMAL), true);
						GeneralThreadPool.getInstance().schedule(this, 3360);
					} else {
						_racer.setDirectionMove(2);
						--_remainRacingCount;
						GeneralThreadPool.getInstance().schedule(this, _racer.getNpcTemplate().getPassiSpeed());
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int getTotalTicketCount() {
		int total = 0;
		for (int row = 0; row < 5; row++) {
			total += _ticketCount[row];
		}
		return total;
	}

	public int getDollState() {
		return _DollRaceState;
	}

	public void setDollState(int state) {
		_DollRaceState = state;
	}

	public int getRaceCount() {
		return _raceCount;
	}

	public void setRaceCount(int cnt) {
		_raceCount = cnt;
	}
}


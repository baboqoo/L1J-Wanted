package l1j.server.server.controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.ChatType;
import l1j.server.common.data.Material;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.DogFightTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.action.S_AttackPacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1DogFight;
import l1j.server.server.templates.L1DogFightTicket;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class DogFightController implements Runnable {
	private static DogFightController _instance;

	private static final int FIGHT_INTERVAL = 1 * 60 * 1000;
	
	private static final double DOG_FIGHT_RATE = Config.ALT.DOG_FIGHT_TICKETS_RATE;

	private static final int EXECUTE_STATUS_NONE		= 0;
	private static final int EXECUTE_STATUS_PREPARE		= 1;
	private static final int EXECUTE_STATUS_READY		= 2;
	private static final int EXECUTE_STATUS_STANDBY		= 3;
	private static final int EXECUTE_STATUS_PROGRESS	= 4;
	private static final int EXECUTE_STATUS_FINALIZE	= 5;
	
	private int _executeStatus = EXECUTE_STATUS_NONE;

	private int _FightCount = 0;
	long _nextFightTime = System.currentTimeMillis() + 60000;
	private int _dogfightState = 2;

	private int _ticketSellRemainTime;
	private int _FightWatingTime;
	private int _currentBroadcastFighter;

	L1NpcInstance[] _npc = new L1NpcInstance[3];

	public int[] _ticketCount = new int[2];
	public int[] _ticketId = new int[2];
	private static DecimalFormat _df = new DecimalFormat("#.#");

	private int _ranking = 0;
	private boolean _complete = false;
	
	public boolean _GMBroadcast = false;
	
	public boolean _autowin = false;

	List<L1ShopItem> _purchasingList = new ArrayList<L1ShopItem>();
	public L1NpcInstance[] _dogfight = new L1NpcInstance[2];

	int Lucky = 0;

	/** 투견 추가 **/
	private final HashMap<Integer, L1DogFightTicket> _fight = new HashMap<Integer, L1DogFightTicket>(32);
	
	public HashMap<Integer, L1DogFightTicket> getAllTemplates() {
		return _fight;
	}

	private static final int Start_X[] = { 33528, 33528 };
	private static final int Start_Y[] = { 32864, 32866 };

	private static final ArrayList<DogStruct> _dogs;
	static{
		_dogs = new ArrayList<DogStruct>(32);
		/*_dogs.add(new DogStruct(1, 17133, "# 도베르만"));
		_dogs.add(new DogStruct(2, 17172, "# 세퍼드"));
		_dogs.add(new DogStruct(3, 17204, "# 허스키"));
		_dogs.add(new DogStruct(4, 17239, "# 진돗개"));
		_dogs.add(new DogStruct(5, 17211, "# 콜리"));
		_dogs.add(new DogStruct(6, 17168, "# 세인트버나드"));
		_dogs.add(new DogStruct(7, 17197, "# 유니콘"));
		_dogs.add(new DogStruct(8, 17315, "# 그리폰"));
		_dogs.add(new DogStruct(9, 17278, "# 골드 드래곤"));
		_dogs.add(new DogStruct(11, 17282, "# 블루 해츨링"));
		_dogs.add(new DogStruct(12, 17298, "# 레드 해츨링"));
		_dogs.add(new DogStruct(13, 17247, "# 호랑이"));
		_dogs.add(new DogStruct(14, 17121, "# 늑대"));*/
		_dogs.add(new DogStruct(1, 17133, "$905"));
		_dogs.add(new DogStruct(2, 17172, "$907"));
		_dogs.add(new DogStruct(3, 17204, "$1788"));
		_dogs.add(new DogStruct(4, 17239, "$4073"));
		_dogs.add(new DogStruct(5, 17211, "$906"));
		_dogs.add(new DogStruct(6, 17168, "$28376"));
		_dogs.add(new DogStruct(7, 17197, "$2488"));
		_dogs.add(new DogStruct(8, 17315, "$1176"));
		_dogs.add(new DogStruct(9, 17278, "$4076"));
		_dogs.add(new DogStruct(11, 17282, "$28370"));
		_dogs.add(new DogStruct(12, 17298, "$28369"));
		_dogs.add(new DogStruct(13, 17247, "$3041"));
		_dogs.add(new DogStruct(14, 17121, "$268"));

	}
	
	//private int[] SKILLID = {  17324, 17322, 17320, 17318, 17322, 17320 };

	private static int[] _time = new int[2];
	
	private int[] ticket = { 0, 0 };
	
	public double[] _winRate = { 0, 0 };
	
	//public String[] _FightCondition = { "좋음", "좋음" };
	public String[] _FightCondition = { "Good", "Good" };
	
	public double _ration[] = { 0, 0 };

	public static DogFightController getInstance() {
		if(_instance == null)_instance = new DogFightController();
		return _instance;
	}
	
	public void run() {
		try {
			switch (_executeStatus) {
			case EXECUTE_STATUS_NONE: {
				if (checkStartFight()) {
					initFightGame();
					_executeStatus = EXECUTE_STATUS_PREPARE;
					GeneralThreadPool.getInstance().schedule(this, 2000);
				} else {
					GeneralThreadPool.getInstance().schedule(this, 1000); // 1초
				}
			}
				break;
			case EXECUTE_STATUS_PREPARE: {
				startSellTicket();
				_executeStatus = EXECUTE_STATUS_READY;
				GeneralThreadPool.getInstance().schedule(this, 2000);
			}
				break;
			case EXECUTE_STATUS_READY: {
				long remainTime = checkTicketSellTime();
				if (remainTime > 0) {
					GeneralThreadPool.getInstance().schedule(this, remainTime);
				} else {
					_executeStatus = EXECUTE_STATUS_STANDBY;
					GeneralThreadPool.getInstance().schedule(this, 1000);
				}
			}
				break;
			case EXECUTE_STATUS_STANDBY: {
				if (checkWatingTime()) {
					initShopNpc();
					startFight();
					_executeStatus = EXECUTE_STATUS_PROGRESS;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			}
				break;

			case EXECUTE_STATUS_PROGRESS: {
				if (broadcastBettingRate()) {
					if (_complete) {
						_executeStatus = EXECUTE_STATUS_FINALIZE;
					}
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			}
				break;
			case EXECUTE_STATUS_FINALIZE: {
				_FightCount = _FightCount + 1;
				setdogState(2);
				//broadcastNpc("다음경기를 준비하는데 " + Config.ALT.DOG_FIGHT_TIME + "초가 소요됩니다");
				broadcastNpc(S_SystemMessage.getRefText(1311) + Config.ALT.DOG_FIGHT_TIME + S_SystemMessage.getRefText(1312));
				
				_executeStatus = EXECUTE_STATUS_NONE;
				GeneralThreadPool.getInstance().schedule(this, Config.ALT.DOG_FIGHT_TIME * 1000);
			}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkStartFight() {
		long currentTime = System.currentTimeMillis();
		if (_nextFightTime < currentTime) {
			_nextFightTime = currentTime + FIGHT_INTERVAL;
			return true;
		}
		return false;
	}

	private void initNpc() {
		L1NpcInstance n = null;
		for (Object obj : L1World.getInstance().getVisibleObjects(4).values()) {
			if (obj instanceof L1NpcInstance) {
				n = (L1NpcInstance) obj;
				if (n.getNpcTemplate().getNpcId() == 170041) {//투견 티켓 상인
					_npc[0] = n;
				}
				if (n.getNpcTemplate().getNpcId() == 170042) {//투견 도우미
					_npc[1] = n;
				}
			}
		}
	}
	
	private void initShopNpc() {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();

		L1Shop shop = new L1Shop(170041, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(170041, shop);
	}
	
	private void sleepTime() {
		for (int i = 0; i < 2; i++) {
			int dogState = CommonUtil.random(5);
			int addValue = 0;
			switch (dogState) {
			case 0:
				//_FightCondition[i] = "좋음";
				_FightCondition[i] = "Good";
				addValue = -15;
				break;
			case 1:
				//_FightCondition[i] = "좋음";
				_FightCondition[i] = "Good";
				addValue = -10;
				break;
			case 2:
				//_FightCondition[i] = "나쁨";
				_FightCondition[i] = "Bad";
				addValue = 10;
				break;
			case 3:
				//_FightCondition[i] = "나쁨";
				_FightCondition[i] = "Bad";
				addValue = 15;
				break;
			default:
				//_FightCondition[i] = "보통";
				_FightCondition[i] = "Average";
				addValue = 5;
				break;
			}
			_time[i] = 1100 + addValue;
		}
	}
	
	private FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();

	private void initWinRate() {
		L1DogFight dogfight = null;
		for (int i = 0; i < 2; i++) {
			dogfight = DogFightTable.getInstance().getTemplate(_dogfight[i].getNum());
			double rate = (double) dogfight.getWinCount() * 100.0 / (double) (dogfight.getWinCount() + dogfight.getLoseCount());
			_winRate[i] = Double.parseDouble(_df.format(rate));
		}
	}
	
	//private static final String[] DOG_FIRST_NAME = { "홀", "짝" };
	private static final String[] DOG_FIRST_NAME = { S_SystemMessage.getRefText(1309), S_SystemMessage.getRefText(1310) };
	
	private void loadDog() {
		L1Npc dogs = null;
		list.clear();
		Collections.shuffle(_dogs);
		DogStruct bs;
		for (int m = 0; m < 2; ++m) {
			try {
				bs = _dogs.get(m);
				dogs = new L1Npc();
				dogs.setFamily(0);
				dogs.setAgroFamily(0);
				dogs.setPicupItem(false);

				Object[] parameters = { dogs };

				_dogfight[m] = (L1NpcInstance) Class.forName("l1j.server.server.model.Instance.L1NpcInstance").getConstructors()[0].newInstance(parameters);
				_dogfight[m].setId(IdFactory.getInstance().nextId());
				_dogfight[m].setSpriteId(bs._gfx);
				_dogfight[m].setDesc(DOG_FIRST_NAME[m] + bs._name);
				_dogfight[m].setName(_dogfight[m].getDesc());
				_dogfight[m].setMaxHp(300);
				_dogfight[m].setCurrentHp(300);
				_dogfight[m].setNum(bs._id);
				_dogfight[m].setX(Start_X[m]);
				_dogfight[m].setY(Start_Y[m]);
				_dogfight[m].setMap((short) 4);
				_dogfight[m].getMoveState().setHeading(m == 0 ? 4 : 0);
				_dogfight[m].setAtkMagicspeed(_dogfight[m].getSprite().getActionSpeed(ActionCodes.ACTION_Aggress) + 100);
				L1World world = L1World.getInstance();
				world.storeObject(_dogfight[m]);
				world.addVisibleObject(_dogfight[m]);
				for (L1PcInstance member : world.getVisiblePlayer(_dogfight[m])) {
					if (member != null) {
						member.updateObject();
						member.sendPackets(new S_Effect(_dogfight[m].getId(), 5935), true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void broadcastNpc(String msg) {
		if(_npc[0] != null)_npc[0].broadcastPacket(new S_NpcChatPacket(_npc[0], msg, ChatType.CHAT_SHOUT), true);
	}
	
	private void broadcastNpc1(String msg) {
		if(_npc[1] != null)_npc[1].broadcastPacket(new S_NpcChatPacket(_npc[1], msg, ChatType.CHAT_NORMAL), true);
	}
	
	private void WinnerMent(String msg) {
		if(_npc[0] != null)_npc[0].broadcastPacket(new S_NpcChatPacket(_npc[0], msg, ChatType.CHAT_SHOUT), true);
		if(_npc[1] != null)_npc[1].broadcastPacket(new S_NpcChatPacket(_npc[1], msg, ChatType.CHAT_NORMAL), true);
	}
	
	private void startSellTicket() {
		LoadNpcShopList();
		//broadcastNpc("투견 티켓 판매를 시작하였습니다.");
		broadcastNpc(S_SystemMessage.getRefText(1313));
		setdogState(0);
		_ticketSellRemainTime = 60 * Config.ALT.DOG_FIGHT_TICKETS_TIME;
	}

	private long checkTicketSellTime() {
		if (_ticketSellRemainTime == 300) {
			_ticketSellRemainTime -= 60;
			broadcastNpc(S_SystemMessage.getRefText(1314));
			return 60000;
		} else if (_ticketSellRemainTime == 240) {
			_ticketSellRemainTime -= 60;
			broadcastNpc(S_SystemMessage.getRefText(1315));
			return 60000;
		} else if (_ticketSellRemainTime == 180) {
			_ticketSellRemainTime -= 60;
			broadcastNpc(S_SystemMessage.getRefText(1316));
			return 60000;
		} else if (_ticketSellRemainTime == 120) {
			_ticketSellRemainTime -= 60;
			broadcastNpc(S_SystemMessage.getRefText(1317));
			return 60000;
		} else if (_ticketSellRemainTime == 60) {
			_ticketSellRemainTime -= 60;
			broadcastNpc(S_SystemMessage.getRefText(1318));
			return 60000;
		}
		_ticketSellRemainTime = 0;
		//broadcastNpc("10초 후 투견 티켓 판매를 마감합니다.");
		broadcastNpc(S_SystemMessage.getRefText(1319));		
		_FightWatingTime = 10;
		return 0;
	}
	
	private boolean checkWatingTime() {		
		if (_FightWatingTime > 0) {
			//broadcastNpc1(_FightWatingTime + "초");
			broadcastNpc1(_FightWatingTime + " " + S_SystemMessage.getRefText(1306));
			--_FightWatingTime;
			return false;
		} else {
			SettingRate();
			setdogState(1);
		}
		return true;
	}
	
	public int getdogState() {
		return _dogfightState;
	}
	
	public void setdogState(int state) {
		_dogfightState = state;
	}
		
	private void LoadNpcShopList() {
		try {
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();

			for (int i = 0; i < 2; i++) {
				ticket[i] = ItemTable.getInstance().GetDogFightTicketId();				
				//SaveFight(ticket[i], "투견 티켓 " + _dogfight[i].getDesc() + StringUtil.MinusString + (i + 1));
				SaveFight(ticket[i], S_SystemMessage.getRefText(1308) + _dogfight[i].getDesc());
				L1ShopItem item = new L1ShopItem(ticket[i], 500, 1);
				sellingList.add(item);
				_ticketId[i] = ticket[i];
			}
			
			for (int i = 0; i < 2; i++) {
				L1ShopItem item1 = new L1ShopItem(ticket[i], 25000000, 50000);
				sellingList.add(item1);
			}
			
			for (int i = 0; i < 2; i++) {
				L1ShopItem item1 = new L1ShopItem(ticket[i], 50000000, 100000);
				sellingList.add(item1);
			}

			L1Shop shop = new L1Shop(170041, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(170041, shop);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 투견 티켓 **/
	private void SaveFight(int itemId, String desc) {
		L1DogFightTicket etcItem = new L1DogFightTicket();
		etcItem.setItemType(L1ItemType.NORMAL);
		etcItem.setItemId(itemId);
		etcItem.setDescKr(desc);
		etcItem.setDesc(desc);
		etcItem.setDescEn(desc);
		etcItem.setType(12);
		etcItem.setMaterial(Material.PAPER);
		etcItem.setWeight(0);
		etcItem.setTicketPrice(1000);
		etcItem.setIconId(143);
		etcItem.setSpriteId(18168);
		etcItem.setMinLevel(0);
		etcItem.setMaxLevel(0);
		etcItem.setBless(1);
		etcItem.setTradable(false);
		etcItem.setCantSell(true);
		etcItem.setDmgSmall(0);
		etcItem.setDmgLarge(0);
		etcItem.setMerge(true);
		
		ItemTable.getInstance().AddDogFightTicket(etcItem);
	}
	
	private void StartGame() {
		GeneralThreadPool.getInstance().schedule(new RunFight(0), 300);
		GeneralThreadPool.getInstance().schedule(new RunFight(1), 400);
	}
	
	private int _winDog = -1;
	public void setWinDog(int i){
		if(i >= 0 && i <= 1)_winDog = i;
	}
	
	private class RunFight implements Runnable {
		private int _status, _dogId;
		L1NpcInstance _attacker, _target;

		private RunFight(int dogId) {
			_dogId		= dogId;
			_attacker	= _dogfight[_dogId];
			_target		= _dogId == 0 ? _dogfight[1] : _dogfight[0];
		}

		@Override
		public void run() {
			try {
				switch(_status){
				case 0:
					int damage = CommonUtil.random(10) + 5;
					if (_target.getCurrentHp() > 0 && !_dogfight[_dogId].isDead()) {
						if (_target.isDead()) {
							break;
						}
						
						if (_winDog != -1) {// 승리 지목
							int newhp = CommonUtil.random(100) < 10 ? skillAttack(damage) : normalAttack(damage);
							if (newhp <= 0) {
								targetDie();
							}
							_target.setCurrentHp(newhp);
							GeneralThreadPool.getInstance().schedule(this, _time[_dogId]);
						} else {// 미지목
							if (_autowin) {// 자동 승리 처리
								int loseId = _ticketCount[0] > _ticketCount[1] ? 0 : _ticketCount[0] < _ticketCount[1] ? 1 : -1;
								if (loseId != -1) {// 티켓 판매 갯수에 따른 자동 처리
									int newhp = CommonUtil.random(100) < 10 ? skillAttack(damage) : autoAttack(damage, loseId);
									if (newhp <= 0) {
										targetDie();
									}
									_target.setCurrentHp(newhp);
									GeneralThreadPool.getInstance().schedule(this, _time[_dogId]);
								} else {// 동률 일때
									int newhp = CommonUtil.random(100) < 10 ? skillAttack(damage) : normalAttack(damage);
									if (newhp <= 0) {
										targetDie();
									}
									_target.setCurrentHp(newhp);
									GeneralThreadPool.getInstance().schedule(this, _time[_dogId]);
								}
							} else {// 무조작
								int newhp = CommonUtil.random(100) < 10 ? skillAttack(damage) : normalAttack(damage);
								if (newhp <= 0) {
									targetDie();
								}
								_target.setCurrentHp(newhp);
								GeneralThreadPool.getInstance().schedule(this, _time[_dogId]);
							}
						}
					} else if (_target.getCurrentHp() == 0 && _attacker.getCurrentHp() > 0) {
						//WinnerMent(String.format("제 %d 회 승자는 '%s' 입니다.", _FightCount, _attacker.getDesc()));
						WinnerMent(String.format(S_SystemMessage.getRefText(1323) + "%d " + S_SystemMessage.getRefText(1324) + "'%s'" + S_SystemMessage.getRefText(1325), _FightCount, _attacker.getDesc()));
						Thread.sleep(2000);
						//_attacker.broadcastPacket(new S_SkillSound(_attacker.getId(), 6354), true);
						_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_Aggress), true);
						Thread.sleep(_attacker.getAtkMagicspeed());
						Result(_dogId);
						_attacker.deleteMe();
						_target.deleteMe();
					} else if (_target.isDead() && _attacker.isDead()) {
						//WinnerMent(String.format("제 %d 회 결과는 무승부입니다.", _FightCount));
						// The result of round %d is a draw.
						WinnerMent(String.format(S_SystemMessage.getRefText(1326) + "%d " + S_SystemMessage.getRefText(1327), _FightCount));
						Thread.sleep(4000);
						Result(0);
						_attacker.deleteMe();
						_target.deleteMe();
					}
					_target.broadcastPacket(new S_HPMeter(_target), true);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private int normalAttack(int damage){
			_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_Attack), true);
			_target.broadcastPacket(new S_AttackPacket(_target, _target.getId(), ActionCodes.ACTION_Damage), true);
			if (_winDog != -1) {
				damage += _target == _dogfight[_winDog == 0 ? 1 : 0] ? CommonUtil.random(5) : 0;// 승리 지목시 추가 대미지
			}
			return _target.getCurrentHp() - damage;
		}
		
		private int skillAttack(int damage){
			_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillAttack), true);
			//_attacker.broadcastPacket(new S_SkillSound(target.getId(), SKILLID[CommonUtil.random(SKILLID.length)]), true);
			_attacker.broadcastPacket(new S_Effect(_target.getId(), 18402), true);
			_target.broadcastPacket(new S_AttackPacket(_target, _target.getId(), ActionCodes.ACTION_Damage), true);
			return _target.getCurrentHp() - (damage + damage);
		}
		
		private int autoAttack(int damage, int loseId){
			_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_Attack), true);
			_target.broadcastPacket(new S_AttackPacket(_target, _target.getId(), ActionCodes.ACTION_Damage), true);
			return _target.getCurrentHp() - (damage + (_target == _dogfight[loseId] ? CommonUtil.random(5) : 0));
		}
		
		private void targetDie(){
			_target.broadcastPacket(new S_DoActionGFX(_target.getId(), ActionCodes.ACTION_Die), true);
			_target.setDead(true);
			_target.getMap().setPassable(_target.getLocation(), true);
			_target.allTargetClear();
		}
	}
	
	private void Result(int i) {		
		synchronized (this) {
			_ranking = _ranking + 1;
			if (_ranking == 1) {
				SetWinFightTicketPrice(ticket[i], _ration[i]);
				AddWinCount(i);
			} 
		}
		if (i == 0) {
			SetLoseFightTicketPrice(ticket[1], _ration[1]);
			AddLoseCount(1);
		} else {
			SetLoseFightTicketPrice(ticket[0], _ration[0]);
			AddLoseCount(0);
		}

		
		_complete = true;
	}
	
	private void initFightGame() {
		try {	
			_ranking = 0;
			_complete = false;
			Lucky = CommonUtil.random(50);			
			_winDog = -1;

			initNpc();
			//broadcastNpc("잠시후 투견 경기가 시작됩니다.");
			broadcastNpc(S_SystemMessage.getRefText(1320));
			initTicketCount();
			initShopNpc();
			sleepTime();
			loadDog();
			initWinRate();
			
		} catch (Exception e) {
		}
	}
	
	private int getTotalTicketCount() {
		int total = 0;
		for (int row = 0; row < 2; row++) {
			total += _ticketCount[row];
		}
		return total;
	}
	
	private void SettingRate() {
		for (int row = 0; row < 2; row++) {
			double rate = 0;
			int total = getTotalTicketCount();
			int cnt = _ticketCount[row];
			if (total == 0)
				total = 1;
			if (cnt != 0) {
				rate = (double) total / (double) cnt;
				if (Lucky == row)
					rate *= 1.0;
			}
			_ration[row] = Double.parseDouble(_df.format(rate));
		}
		
		
		if (_GMBroadcast == true){
		    for (L1PcInstance gm : L1World.getInstance().getAllGms()) {
			    if(gm == null)continue;
//AUTO SRM: 			    gm.sendPackets(new S_SystemMessage("투견 티켓판매 현황"), true); // CHECKED OK
			    gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(938), true), true);
			    for (int i = 0; i < 2; i++) {
			    	//String chat = String.format("%s의 판매 티켓은 %d장 입니다.", _dogfight[i].getName(), _ticketCount[i]);
					String chat = String.format("%s has %d tickets for sale.", _dogfight[i].getName(), _ticketCount[i]);
					gm.sendPackets(new S_SystemMessage(chat), true);
			    }
		    }
		}
		
	}
	
	private void initTicketCount() {
		for (int row = 0; row < 2; row++) {
			_ticketCount[row] = 0;
		}
	}
	
	private void SetWinFightTicketPrice(int id, double rate) {	
		//배당 고정 아닐시 아래 주석
		rate = DOG_FIGHT_RATE;
		L1ShopItem newItem = new L1ShopItem(id, (int) ((500 * rate) * 1D), 1);
		_purchasingList.add(newItem);
		initShopNpc();
	}
	
	private void AddWinCount(int j) {
		L1DogFight Fighter = DogFightTable.getInstance().getTemplate(_dogfight[j].getNum());
		Fighter.setWinCount(Fighter.getWinCount() + 1);
		Fighter.setLoseCount(Fighter.getLoseCount());
		SaveAllFighter(Fighter, _dogfight[j].getNum());
	}
	
	private void SaveAllFighter(L1DogFight Fighter, int num) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE util_fighter SET WinCount=?, LoseCount=? WHERE Num=" + num);
			pstm.setInt(1, Fighter.getWinCount());
			pstm.setInt(2, Fighter.getLoseCount());
			pstm.execute();
		} catch (SQLException e) {
			System.out.println("[::::::] SaveAllFighter Method error occurred");
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private void SetLoseFightTicketPrice(int id, double rate) {
		L1ShopItem newItem = new L1ShopItem(id, 0, 1);
		_purchasingList.add(newItem);
		initShopNpc();
	}
	
	private void AddLoseCount(int j) {
		L1DogFight Fighter = DogFightTable.getInstance().getTemplate(_dogfight[j].getNum());
		Fighter.setWinCount(Fighter.getWinCount());
		Fighter.setLoseCount(Fighter.getLoseCount() + 1);
		SaveAllFighter(Fighter, _dogfight[j].getNum());
	}
	
	private void startFight() {
		//broadcastNpc("경기 시작!");
		//broadcastNpc1("경기 시작!");
		broadcastNpc(S_SystemMessage.getRefText(1321));
		broadcastNpc1(S_SystemMessage.getRefText(1321));

	    _npc[1].broadcastPacket(new S_DoActionGFX(_npc[1].getId(), 67), true);	
		StartGame();

		_currentBroadcastFighter = 0;
	}
	
	private boolean broadcastBettingRate() {
		if (_currentBroadcastFighter == 2)
			return true;
		if (_currentBroadcastFighter == 0)
			//broadcastNpc("배팅 배율을 발표하겠습니다.");
			broadcastNpc(S_SystemMessage.getRefText(1322));
			
		// 배당 고정아닐시 아래 주석
		_ration[_currentBroadcastFighter] = DOG_FIGHT_RATE;
		broadcastNpc(_dogfight[_currentBroadcastFighter].getDesc() + ": " + _ration[_currentBroadcastFighter] + StringUtil.EmptyOneString);

		++_currentBroadcastFighter;
		return false;
	}
	
	static class DogStruct{
		public DogStruct(int id, int gfx, String name){
			_id		= id;
			_gfx	= gfx;
			_name	= name;
		}
		public int _id;
		public int _gfx;
		public String _name;
	}
	
}



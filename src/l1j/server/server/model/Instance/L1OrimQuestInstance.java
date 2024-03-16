//package l1j.server.server.model.Instance;
//
//import java.awt.Point;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import l1j.server.server.ActionCodes;
//import l1j.server.server.IdFactory;
//import l1j.server.server.datatables.NpcTable;
//import l1j.server.server.datatables.TrapTable;
//import l1j.server.server.model.L1Inventory;
//import l1j.server.server.model.L1Location;
//import l1j.server.server.model.L1Object;
//import l1j.server.server.model.L1OrimQuest;
//import l1j.server.server.model.L1Teleport;
//import l1j.server.server.model.L1World;
//import l1j.server.server.model.trap.L1Trap;
//import l1j.server.server.model.trap.L1WorldTraps;
//import l1j.server.server.serverpackets.S_BlackWindow;
//import l1j.server.server.serverpackets.S_DisplayClack;
//import l1j.server.server.serverpackets.S_DoActionGFX;
//import l1j.server.server.serverpackets.S_EffectLocation;
//import l1j.server.server.serverpackets.S_GreenMessage;
//import l1j.server.server.serverpackets.S_RedMessage;
//import l1j.server.server.serverpackets.S_ShockWave;
//import l1j.server.server.serverpackets.S_SkillSound;
//import l1j.server.server.serverpackets.S_Sound;
//import l1j.server.server.serverpackets.S_YellowMessage;
//import l1j.server.server.serverpackets.ServerBasePacket;
//import l1j.server.server.templates.L1DoorGfx;
//
//public class L1OrimQuestInstance {
//	private short _mapId; // 생성 된 인스턴스 맵 ID를 저장
//	private int _OrimQuestStatus;// 퀘스트의 진행 상태를 저장
//	private boolean _isDeleteTransactionNow;// 배의 시설이 현재 사용 중
//	private boolean _acceptOrder;// 룬 작성 요청을 수락 여부
//	private int _comeShipCount;// 유령선의 습격이 있었는지 여부를 저장
//	private boolean _extraShipCome;
//	private boolean _isRankSelected;
//	private boolean _isCrakenDead;
//	private boolean _isLastBossDead;
//
//	private L1NpcInstance _ship;
//	private L1NpcInstance _diffence_rune_1;
//	private L1NpcInstance _diffence_rune_2;
//	private L1NpcInstance _attack_rune;
//	private L1NpcInstance _portal_rune;
//	private L1NpcInstance _cannon_1;
//	private L1NpcInstance _cannon_2;
//	private L1NpcInstance _shipWall;
//	private L1NpcInstance _craken_tail_left;
//	private L1NpcInstance _craken_tail_right;
//	private L1MonsterInstance _mimic_A;
//	private L1MonsterInstance _mimic_B;
//	private L1MonsterInstance _mimic_C;
//	private L1DoorInstance _createDoor;
//	private L1TrapInstance _moveTrap;
//	private int _startPoint;
//	private int _endPoint;
//
//	private int[] _roundDiffenceStatus = new int[12];
//	private int[] _roundHitDamage = new int[12];
//	private int[] _roundStatus = new int[12];
//	private int[] _bonusRoundStatus = new int[12];// 해양 생물의 습격을 저장
//	private int _currentRound;
//	private int _point;// 현재 위치를 저장
//	private int _ship_burning;
//	private int _tempNpcNumber;
//
//	void init() {
//		_cannon_1 = spawnOne(new L1Location(32803, 32809, _mapId), 91450, 0);
//		_cannon_2 = spawnOne(new L1Location(32792, 32809, _mapId), 91451, 0);
//		_cannon_1.getMap().setPassable(32803, 32809, false);
//		_cannon_2.getMap().setPassable(32792, 32809, false);
//		_shipWall = spawnOne(new L1Location(32799, 32806, _mapId), 91449, 0);
//		_shipWall.setStatus(32);
//		L1DoorGfx gfx = new L1DoorGfx(2510, 0, 10, -10);// 투명
//		//_createDoor = DoorTable.getInstance().createDoor(0, gfx, new L1Location(32798, 32795, _mapId), 0, 0, false);
//		spawnOne(new L1Location(32677, 32800, _mapId), 91452, 0);
//		spawnOne(new L1Location(32677, 32864, _mapId), 91452, 0);
//		spawnOne(new L1Location(32741, 32860, _mapId), 91452, 0);
//		spawnOne(new L1Location(32805, 32862, _mapId), 91452, 0);
//		setSwitch(new L1Location(32677, 32800, _mapId), 74);
//		setSwitch(new L1Location(32677, 32864, _mapId), 74);
//		setSwitch(new L1Location(32741, 32860, _mapId), 74);
//		setSwitch(new L1Location(32805, 32862, _mapId), 74);
//	}
//
//	public static final int _STATUS_ORIMQUEAT_NONE = 0;
//	public static final int _STATUS_ORIMQUEAT_READY_1 = 1;
//	public static final int _STATUS_ORIMQUEAT_READY_2 = 2;
//	public static final int _STATUS_ORIMQUEAT_READY_3 = 3;
//	public static final int _STATUS_ORIMQUEAT_READY_4 = 4;
//	public static final int _STATUS_ORIMQUEAT_READY_5 = 30;
//	public static final int _STATUS_ORIMQUEAT_READY_6 = 31;
//	public static final int _STATUS_ORIMQUEAT_READY_7 = 32;
//	public static final int _STATUS_ORIMQUEAT_READY_8 = 33;
//	public static final int _STATUS_ORIMQUEAT_READY_8_1 = 50;
//	public static final int _STATUS_ORIMQUEAT_READY_8_2 = 51;
//	public static final int _STATUS_ORIMQUEAT_READY_8_3 = 52;
//	public static final int _STATUS_ORIMQUEAT_READY_9 = 34;
//	public static final int _STATUS_ORIMQUEAT_READY_10 = 35;
//	public static final int _STATUS_ORIMQUEAT_READY_11 = 36;
//	public static final int _STATUS_ORIMQUEAT_READY_11_1 = 37;
//	public static final int _STATUS_ORIMQUEAT_READY_11_2 = 38;
//
//	public static final int _STATUS_ORIMQUEAT_START = 5;
//	public static final int _STATUS_ORIMQUEAT_START_2 = 6;
//	public static final int _STATUS_ORIMQUEAT_START_2_1 = 7;
//	public static final int _STATUS_ORIMQUEAT_START_2_2 = 8;
//	public static final int _STATUS_ORIMQUEAT_START_2_3 = 9;
//	public static final int _STATUS_ORIMQUEAT_START_2_4 = 10;
//	public static final int _STATUS_ORIMQUEAT_START_3 = 11;
//	public static final int _STATUS_ORIMQUEAT_START_3_1 = 12;
//	public static final int _STATUS_ORIMQUEAT_START_3_2 = 13;
//	public static final int _STATUS_ORIMQUEAT_START_3_3 = 14;
//	public static final int _STATUS_ORIMQUEAT_END_1 = 15;
//	public static final int _STATUS_ORIMQUEAT_END_2 = 16;
//	public static final int _STATUS_ORIMQUEAT_END_3 = 17;
//	public static final int _STATUS_ORIMQUEAT_END_4 = 18;
//	public static final int _STATUS_ORIMQUEAT_END_5 = 19;
//	public static final int _STATUS_ORIMQUEAT_END_6 = 21;
//	public static final int _STATUS_ORIMQUEAT_END = 20;
//
//	public static final int _STATUS_MOVE_SHIP_1 = 99;
//	public static final int _STATUS_MOVE_SHIP_2 = 98;
//	public static final int _STATUS_MOVE_SEA_MONSTER = 100;
//	public static final int _STATUS_ORIMQUEST_MONITOR = 1000;
//	public static final int _STATUS_SPAWN_MONSTERS = 10001;
//	public static final int _STATUS_CREATE_RUNES = 10002;
//	public static final int _STATUS_ENEMY_CANON = 10003;
//	public static final int _STATUS_FIRE_CANON_1 = 10004;
//	public static final int _STATUS_FIRE_CANON_2 = 10005;
//
//	static final int STATUS_ORIMQUEAT_LIMIT = 25;
//	static final int STATUS_ORIMQUEAT_LIMIT2 = 26;
//	static final int STATUS_ORIMQUEAT_LIMIT3 = 27;
//
//	public boolean _IS_SPAWN_SHIP_TYPEA;
//	public boolean _IS_SPAWN_SHIP_TYPEB;
//	public boolean _IS_SPAWN_SHIP_TYPEC;
//
//	public static final int STATUS_NONE = 0;
//	public static final int STATUS_READY_SPAWN = 1;
//	public static final int STATUS_SPAWN = 2;
//
//	private int _action;
//
//	private int[] _pointMessages = { 10638, 10639, 10640, 10641, 10642, 10643, 10644, 10645, 10646, 10647, 10648,
//			10649, 10666, 10667, 10668, 10669, 10670, 10671, 10672, 10673, 10674, 10675, 10676, 10677, 10678, 10679,
//			10680, 10681, 10682, 10683, 10684, 10685, 10686 };
//
//	private int[] _monstersA = { 91278, // 마계의 사라만다
//			91273, // 마계의 켈베로스
//			91459, // 마계의 아시타 지오
//			91457, // 마계의 헬 바운드
//			91456, // 마계의 혼 켈베로스
//			91458, // 마계의 라바 골렘
//			91460, // 마계의 미노타우르스
//			91464, // 마계의 미노타우르스
//			91461, // 마계의 마이노
//			91280, // 마계의 서큐버스
//			91462, // 마계의 가고일
//			91463, // 마계의 스피 리드
//			91495, // 마계의 레서 드래곤
//			91292, // 마계의 그레이트 미노타우르스
//			91465, // 마계의 타락의 사제
//			91466, // 마계의 타락의 사제
//			91467, // 마계의 타락의 사제
//			91468, // 마계의 타락의 사제
//			91469, // 마계의 타락의 사제
//			91470 // 마계의 혼돈의 사제
//	};
//	private int[] _monstersB = { 45374, // 지저 리자드 맨
//			45825, // 용감한 리자드 맨
//			45827, // 용감한 리자드 맨
//			45874, // 용감한 리자드 맨
//	};
//	private int[] _monstersC = { 46057, // 라버 본 아처
//			46056, // 라버 본 솔져
//			46059, // 라버 본 나이프
//			46058, // 라버 본 헤드
//			45502 // 심연의 궁수
//	};
//
//	public L1OrimQuestInstance(short mapId) {
//		setMapId(mapId);
//		init();// 초기화
//	}
//
//	/**
//	 * 오림 퀘스트를 시작
//	 */
//	public void start() {
//		if (_STATUS_ORIMQUEAT_NONE == _OrimQuestStatus) {
//			OrimQuestTimer timer_start = new OrimQuestTimer(_STATUS_ORIMQUEAT_NONE, 0);
//			timer_start.begin();
//		}
//	}
//
//	/**
//	 * 스레드 시작
//	 */
//	class OrimQuestTimer extends TimerTask {
//		int _time;// 시작 시간
//		int _order;// 처리 구분
//
//		OrimQuestTimer(int order, int time) {
//			_time = time;
//			_order = order;
//		}
//
//		public void begin() {
//			final Timer timer = new Timer();
//			timer.schedule(this, _time);
//		}
//
//		@Override
//		public void run() {
//			if (!_isDeleteTransactionNow) {// 제거 프로세스가 시작되어 있다면 모든 주문을 받아들이지 않는다
//				switch (_order) {
//				/********** 시작 전 준비 단계 START **************/
//				case _STATUS_ORIMQUEAT_NONE:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_1);// 퀘스트 상태를준비
//					OrimQuestTimer timer_1 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_1, 12000);
//					timer_1.begin();
//					// 지도 감시 용 모니터를 시작
//					OrimQuestTimer monitor = new OrimQuestTimer(_STATUS_ORIMQUEST_MONITOR, 30000);
//					monitor.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_1:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_2);
//					sendMessage("$9529", 0); // GreenMessage 오림 : 여러분 침착 듣고
//					OrimQuestTimer timer_2 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_2, 8000);
//					timer_2.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_2:
//					_action = 0;// 소셜 액션을 확실하게 통과한다.
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_3);
//					sendMessage("$9530", 0); // GreenMessage 오림 : 설명이 필요없는 경우
//					OrimQuestTimer timer_3 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_3, 8000);
//					timer_3.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_3:
//					int action = _action;
//					_action = 0;// 소셜 액션을 확실하게 통과한다.
//
//					if (action == 68) {// 단계를 단번에 인상
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_START);
//						OrimQuestTimer timer_start1 = new OrimQuestTimer(_STATUS_ORIMQUEAT_START, 8000);
//						timer_start1.begin();
//					} else {
//						// 설명 작업을 시작
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_4);
//						sendMessage("$9531", 0);
//						// GreenMessage 우리의 배를 공격하는 것은올 딘이라는 것입니다
//						OrimQuestTimer timer_4 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_4, 8000);
//						timer_4.begin();
//					}
//					break;
//				case _STATUS_ORIMQUEAT_READY_4:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_5);
//					sendMessage("$9532", 0);
//					// GreenMessage 오림 : 자세한 이야기는 살아나서합시다.
//					OrimQuestTimer timer_5 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_5, 8000);
//					timer_5.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_5:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_6);
//					sendMessage("$9533", 0); // 우선 생존하는 것입니다. 대포의 사용에서
//					OrimQuestTimer timer_6 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_6, 8000);
//					timer_6.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_6:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_7);
//					sendMessage("$9534", 0); // 싸움이 시작되면 바닥에 붉은 마법진이 나타납니다.
//					_attack_rune = spawnOne(
//							L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false), 91454, 5);
//					OrimQuestTimer timer_7 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_7, 8000);
//					timer_7.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_7:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_8);
//					sendMessage("$9535", 0);
//					// GreenMessage 그리고 파티 장 대포 사이에있는같은 모양의 발판 위에 서주세요
//					OrimQuestTimer timer_8 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_8, 8000);
//					timer_8.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_8:
//					_action = 0;// 소셜 액션을 확실하게 통과한다.
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_8_1);
//					sendMessage("$9536", 0);
//					// GreenMessage 그 상태에서 alt +4를 누르면 대포에서 총알이 발사됩니다.
//					OrimQuestTimer timer_8_1 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_8_1, 8000);
//					timer_8_1.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_8_1:
//					if (_attack_rune == null) {// 포탄이 발사되어 초기화 됨
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_9);
//						OrimQuestTimer timer_9 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_9, 8000);
//						timer_9.begin();
//					} else {
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_8_2);
//						sendMessage("$9545", 0); // 시간이 없습니다 서둘러.
//						OrimQuestTimer timer_8_2 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_8_2, 8000);
//						timer_8_2.begin();
//					}
//
//					break;
//				case _STATUS_ORIMQUEAT_READY_8_2:
//					if (_attack_rune == null) {// 포탄이 발사되어 초기화 됨
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_9);
//						OrimQuestTimer timer_9 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_9, 8000);
//						timer_9.begin();
//					} else {
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_9);
//						sendMessage("$9546", 0);// GreenMessage으로를 놓쳐 버린 것 같습니다
//
//						OrimQuestTimer timer_9 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_9, 8000);
//						timer_9.begin();
//					}
//
//					break;
//				case _STATUS_ORIMQUEAT_READY_9:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_10);
//					sendMessage("$9537", 0);
//					// GreenMessage 오림 :하지만 적도 공격을 걸어온다위한 방어해야합니다.
//					OrimQuestTimer timer_10 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_10, 8000);
//					timer_10.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_10:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_11);
//					sendMessage("$9538", 0);
//					// GreenMessage 오림 : 방어를 위해 노란색 마법진안에 들어 있습니다.
//					_diffence_rune_1 = spawnOne(
//							L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false), 91453, 5);
//					_diffence_rune_2 = spawnOne(
//							L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false), 91453, 5);
//					OrimQuestTimer timer_11 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_11, 8000);
//					timer_11.begin();
//					break;
//				case _STATUS_ORIMQUEAT_READY_11:
//					_action = 0;
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_11_1);
//					sendMessage("$9539", 0);
//					// GreenMessage 오림 : 그 상태에서 alt +1을누르면 내가 방어 마법을 시전합니다.
//					OrimQuestTimer timer_11_1 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_11_1, 8000);
//					timer_11_1.begin();
//					break;
//
//				case _STATUS_ORIMQUEAT_READY_11_1:
//					if (_diffence_rune_1 == null && _diffence_rune_2 == null) {
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_START);
//						OrimQuestTimer timer_start = new OrimQuestTimer(_STATUS_ORIMQUEAT_START, 8000);
//						timer_start.begin();
//					} else {
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_READY_11_2);
//						sendMessage("$9545", 0); // GreenMessage 시간이 없습니다 서둘러.
//						OrimQuestTimer timer_11_2 = new OrimQuestTimer(_STATUS_ORIMQUEAT_READY_11_2, 8000);
//						timer_11_2.begin();
//					}
//
//					break;
//				case _STATUS_ORIMQUEAT_READY_11_2:
//					if (_diffence_rune_1 == null && _diffence_rune_2 == null) {
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_START);
//						OrimQuestTimer timer_start = new OrimQuestTimer(_STATUS_ORIMQUEAT_START, 8000);
//						timer_start.begin();
//					} else {
//						setOrimQuestStatus(_STATUS_ORIMQUEAT_START);
//						sendMessage("$9546", 0); // GreenMessage으로를 놓쳐 버린 것 같습니다
//						OrimQuestTimer timer_start = new OrimQuestTimer(_STATUS_ORIMQUEAT_START, 8000);
//						timer_start.begin();
//					}
//
//					break;
//				/********** 시작 전 준비 단계 END **************/
//
//				case _STATUS_ORIMQUEAT_START:// 배 vs 선박 보통 라운드에서 3 회로 나누어 Mob 출현
//					clearAllRune();// RUNE를 초기화
//					try {
//						sendMessage("$9540", 0);// 자 적의 공격에 대비합시다.서둘러!
//						for (int round = 0; round < 12; round++) {
//							// 4.8.12라운드에서 한번만 보스 출현,MOB도 함께출현.
//							_currentRound = round;//
//							Thread.sleep(3000);
//							if (round == 0) {
//								sendMessage("$9603", 0);
//								// 오림 : 배의 어딘가에 숨어있는 몬스터가 있어요! 넘어뜨려주세요!
//								sendPackets(new S_DisplayClack());
//								sendPackets(new S_Sound(82));
//								OrimQuestTimer spawn = new OrimQuestTimer(_STATUS_SPAWN_MONSTERS, 0);
//								spawn.begin();
//								Thread.sleep(35000);
//								int monsterCount = checkAliveMonster();
//								if (monsterCount >= 5 && monsterCount <= 7) {// EASYMODE
//									_startPoint = 0;
//									_endPoint = 5;
//								} else if (monsterCount >= 3 && monsterCount <= 4) {
//									_startPoint = 2;
//									_endPoint = 7;
//								} else if (monsterCount == 2) {// 하드 모드
//									_startPoint = 10;
//									_endPoint = 19;
//								} else if (monsterCount == 1) {// 하드 모드
//									_startPoint = 8;
//									_endPoint = 12;
//								} else if (monsterCount == 0) {// 일반 모드
//									_startPoint = 5;
//									_endPoint = 9;
//								}
//
//							}
//							// 9588 오림 : 전보다 강한 마음이 느껴집니다! 조심하세요!
//							// 9589 오림 : 이전보다 약한 느낌이 느껴집니다. 하지만 조심하세요!
//							Thread.sleep(5000);
//							sendMessage("$9548", 0);
//							// 오림 :이 근처에 뭔가가 감돌고있는 것같습니다. 조심하십시오.
//							int i = _random.nextInt(4);
//							_bonusRoundStatus[round] = i;// 해양 생물의 습격을 저장)
//							Thread.sleep(5000);
//							if (i == 0) {// 좌표 고정
//								L1NpcInstance temp = spawnOne(new L1Location(32803, 32788, _mapId), 91445, 4);
//								temp.broadcastPacket(new S_DoActionGFX(temp.getId(), 0));
//								sendMessage(StringUtil.DollarString, 0);// 오림 : 주위에 상어 떼가 있습니다. 조심해!
//								deleteNpc(temp);
//							} else {
//								L1NpcInstance temp = null;
//								if (i == 1) {// 하피 랜덤이동
//									int rd = _random.nextInt(4) + 3;
//									_tempNpcNumber = 91483;
//									for (int z = 0; z < rd; z++) {
//										OrimQuestTimer timer = new OrimQuestTimer(_STATUS_MOVE_SEA_MONSTER, 0);
//										timer.begin();
//									}
//									sendMessage("$9542", 0);
//									// 오림 : 바로 옆에시 하피의거주지 곳이있는 것 같습니다.조심해!
//								} else if (i == 2) {
//									int rd = _random.nextInt(4) + 3;
//									_tempNpcNumber = 91482;
//									for (int z = 0; z < rd; z++) {
//										OrimQuestTimer timer = new OrimQuestTimer(_STATUS_MOVE_SEA_MONSTER, 0);
//										timer.begin();
//									}
//									sendMessage("$9543", 0);
//									// 오림 : 시드 레이크 네요.드물기 때문에는별로 다가는안돼요!
//								} else if (i == 3) {
//									temp = spawnOne(new L1Location(32800, 32794, _mapId), 91494, 4);
//									temp.broadcastPacket(new S_DoActionGFX(temp.getId(), 0));
//									deleteNpc(temp);
//									sendMessage("$9544", 0);
//									// 오림 : 그 무시 무시한 놈은어딘가 위험 해 보인다.
//								}
//							}
//							Thread.sleep(10000);
//							// 점수 출력
//							if (i != 0) {
//								if (checkSeaMonsterAttack(i)) {
//									// true가 돌아 오면해양생물의 습격 인식
//									if (i == 3) {// 다른 MOB에게 공투를 허용하지 않는다.
//										if (_createDoor != null) {
//											_createDoor.open();
//										}
//										Thread.sleep(10000);
//										sendMessage("$10720", 1);//
//										long startTime = System.currentTimeMillis();
//										while (!_isCrakenDead && !_isDeleteTransactionNow) {
//											// 삭제처리는아님
//											Thread.sleep(3000);
//										}
//										if (System.currentTimeMillis() - startTime <= 63000) {
//											// 60초이내의토글
//											// 보상의 출현)
//											spawnOneMob(new L1Location(32798, 32807, _mapId), 91440);
//										}
//										if (_createDoor != null) {
//											_createDoor.close();
//										}
//										Thread.sleep(10000);
//									}
//								}
//							}
//							sendMessage(StringUtil.DollarString + _pointMessages[_point / 100], 1);
//
//							Thread.sleep(10000);
//							if (round != 11) {// 최종 라운드
//								if (round > 0 && (round + 1) % 4 == 0) {
//									// 라운드가4의 배수
//									sendMessage("$9554", 0);
//									// 오림 : 조심하세요! 이번 배는다른보다 강한 느낌이느껴집니다.
//								} else {
//									// 적의 함대 접근에 대해 경고
//									sendMessage("$9550", 0);
//									// 오림 : 적의 전함이 다가오고있습니다! 공격에 대비하세요!
//								}
//								// 포털 같은 것이 나오는 두
//								_acceptOrder = true;
//								createRune();
//							} else {
//								sendMessage("$9556", 0);
//								// 오림 : 으으으 ... 이번 선박에서올 딘의 마음이 느껴집니다!
//							}
//
//							Thread.sleep(20000);
//							// 20 초 경과
//							if (round != 11) {
//								sendMessage("$9551", 0);
//								// 오림 : 선박들이 곧 충돌합니다! 바다에 떨어지지 않게 조심하세요!
//							}
//							// 또한 20 초 경과 = 충돌 ※ 여기까지 갑판에서 작업이 가능
//
//							OrimQuestTimer move_ship = new OrimQuestTimer(_STATUS_MOVE_SHIP_1, 0);
//							move_ship.begin();
//
//							Thread.sleep(20000);
//							// 10초후
//							sendMessage(StringUtil.DollarString + (9609 + round), 1);// 전투가 시작됩니다
//							Thread.sleep(3000);
//							sendPackets(new S_DisplayClack());
//							sendPackets(new S_Sound(82));
//							Thread.sleep(3000);
//							// 8~10초후
//							if (!(round == 11)) {// 최종 라운드가 아닌
//								for (int subRound = 0; subRound < 4; subRound++) {
//									if (subRound == 3) {// 마지막 하위 라운드라면 올 딘에 걸린다
//										if (_mimic_A == null && _mimic_B == null && _mimic_C == null) {
//											// 주술미믹을설치
//											sendMessage("$9608", 2);
//											// 올 딘 : 저놈잡아라!
//											sendPackets(new S_BlackWindow());
//											int[][] templocation = { { 32741, 32800 }, { 32732, 32798 },
//													{ 32733, 32807 } };
//											int rndval = _random.nextInt(3);
//											_mimic_A = spawnOneMob(new L1Location(templocation[0][0],
//													templocation[0][1], _mapId), 91455);
//											_mimic_B = spawnOneMob(new L1Location(templocation[1][0],
//													templocation[1][1], _mapId), 91455);
//											_mimic_C = spawnOneMob(new L1Location(templocation[2][0],
//													templocation[2][1], _mapId), 91455);
//											_mimic_A.getInventory().storeItem(50061, 1);
//											_mimic_B.getInventory().storeItem(50061, 1);
//											_mimic_C.getInventory().storeItem(50061, 1);
//
//											if (rndval == 0) {
//												_mimic_A.setCurseMimic(true);
//											} else if (rndval == 1) {
//												_mimic_B.setCurseMimic(true);
//											} else if (rndval == 2) {
//												_mimic_C.setCurseMimic(true);
//											}
//											// 누군가 텔레포트로 데려 간다
//											ArrayList<L1PcInstance> pclist = L1World.getInstance().getVisiblePlayer(
//													_shipWall, 15);
//											Collections.shuffle(pclist);
//											for (L1PcInstance pc : pclist) {
//												Thread.sleep(3000);
//												L1Teleport.teleport(pc, 32735, 32802, _mapId, 1, false);
//												break;
//											}
//											sendMessage("$9606", 1);
//											// 오림 : 빨리탈출하십시오.좌초하는
//											// 몬스터가어딘가에있을것입니다.
//										}
//										OrimQuestTimer moveShip = new OrimQuestTimer(_STATUS_MOVE_SHIP_2, 0);
//										moveShip.begin();
//									} else {
//										if (subRound == 1) {
//											int countMonster = checkAliveMonster();
//											if (countMonster >= 5) {
//												_roundStatus[round] = -1;// 느린
//											} else if (countMonster >= 2) {//
//												_roundStatus[round] = 0;// 보통 속도
//											} else if (countMonster >= 0) {//
//												_roundStatus[round] = 1;// 초기
//											}
//										}
//										spawnMonsters();
//										if (subRound == 0) {
//											if ((_currentRound == 3 || _currentRound == 7) && _portal_rune == null) {
//												// 일반적적선:타락<리치<제니스 퀸
//												int npcId = 0;
//												if (_point <= 300) {// 타락
//													npcId = 91480;
//												} else if (_point <= 600) {// 리치
//													npcId = 91476;
//												} else if (_point > 600) {// 제니스퀸
//													npcId = 91478;
//												}
//												spawnOneMob(L1Location.randomLocation(new L1Location(32799, 32803,
//														_mapId), 1, 6, false), npcId);
//											}
//										}
//										if (_roundStatus[round] == 1) {
//											sendMessage("$9560", 2);
//											// 오림 : 싸움에 합류하십시오. 적부대를 전멸시키지않으면
//											// 살아남을수 없습니다!
//										} else {
//											sendMessage(StringUtil.DollarString + (9563 + subRound), 1);
//											// 부대가 난입했습니다.
//										}
//									}
//									Thread.sleep(30000);
//
//								}
//								while (checkAliveMonster() >= 4) {
//									_ship_burning++;
//									sendPackets(new S_ShockWave());
//									L1NpcInstance fire = spawnOne(L1Location.randomLocation(new L1Location(32799,
//											32803, _mapId), 1, 6, false), 91487, 5);
//									fire.broadcastPacket(new S_SkillSound(fire.getId(), 762));
//									if (_ship_burning >= 10) {
//										sendMessage("$9562", 1);
//										// 오림 : 배가침몰합니다! 서둘러탈출하세요! 이 상태에서위험하다!
//										Thread.sleep(35000);
//										outPushPlayer();
//										return;
//									} else if (_ship_burning == 9) {
//										sendMessage("$9587", 1);
//										// 오림 : 배가 침몰하기직전입니다. 어떻게 든배를 사수해야합니다!
//									} else {
//										sendMessage("$9561", 1);
//										// 오림 : 난입 한 적이우리의 배를 부수 러달려 있습니다!
//										// 서둘러잡아!
//									}
//
//									Thread.sleep(35000);
//								}
//							} else {// 최종 라운드
//								spawnMonsters();
//								spawnOneMob(
//										L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false),
//										91471);//
//								long time = System.currentTimeMillis();
//								int opt = 0;
//								Thread.sleep(3000);
//								while (!_isLastBossDead && !_isDeleteTransactionNow) {
//									opt += 3000;
//									if (opt >= 30000) {
//										opt = 0;
//										// 배의 파괴를 시작
//										_ship_burning++;
//										sendPackets(new S_ShockWave());
//										L1NpcInstance fire = spawnOne(L1Location.randomLocation(new L1Location(32799,
//												32803, _mapId), 1, 6, false), 91487, 5);
//										fire.broadcastPacket(new S_SkillSound(fire.getId(), 762));
//										if (_ship_burning >= 10) {
//											sendMessage("$9562", 1);
//											// 오림 : 배가침몰합니다!서둘러탈출하세요!
//											Thread.sleep(35000);
//											outPushPlayer();
//											return;
//										} else if (_ship_burning == 9) {
//											sendMessage("$9587", 1);
//											// 오림 : 배가침몰하기직전입니다.어떻게 든 배를사수해야합니다!
//										} else {
//											sendMessage("$9561", 1);
//											// 오림 : 난입 한적이 우리의 배를부수 러 달려있습니다!
//											// 서둘러잡아!
//										}
//									}
//									Thread.sleep(3000);
//								}
//								OrimQuestTimer end_1 = new OrimQuestTimer(_STATUS_ORIMQUEAT_END_1, 0);
//								end_1.begin();
//							}
//						}
//					} catch (InterruptedException e) {
//						// TODO
//						e.printStackTrace();
//					}
//					break;
//				/**
//				 *  모든 보스는 그 시점에서의 적 레벨 (여기서 점수)에 따라 다릅니다.해적선 : 호세 <디에고
//				 * <드레이크 선장유령선 : 타락한 영혼 (몬스터) <좀비로드 <좀비 엘모어 마샬아군 배를 타고
//				 * 오는 보스 : 리치 <제니스 퀸 12 라운드의 보스 : 바포메트 <올 딘 <4 현자 (세마 · 카스파
//				 * 발타자르 멜키 오르) <푸드 미착용 세마 +3 현자 ※. 해적선, 유령선의 경우 출현 보스의 차이로 그
//				 * 라운드에 출현하는 적 몬스터의 종류도 달라집니다. 11 라운드 종료 시점에서 1900 점 이상으로 식품
//				 * 미착용 세마 +3 현자 출현 (미확인
//				 */
//
//				// ********** 습격 단계 END **************//
//				/********** 종료 준비 단계 START **************/
//				case _STATUS_ORIMQUEAT_END_1:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_END_2);// 퀘스트 상태를 준비로변경
//					sendMessage("$9579", 0);// 오림 : 더 이상 이것 저것 설명 할 필요가 없을 것입니다
//					spawnOne(new L1Location(1, 1, _mapId), 91455, 0);
//					// 주술 미믹 전원에게? 단계의상자를준다
//					OrimQuestTimer end_2 = new OrimQuestTimer(_STATUS_ORIMQUEAT_END_2, 12000);
//					end_2.begin();
//					break;
//				case _STATUS_ORIMQUEAT_END_2:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_END_3);
//					// 퀘스트 상태를 준비로변경
//					sendMessage("$9580", 0);
//					// 오림 : 이맘때 말하는 섬 놈들의 손으로 폐허가되어있는것입니다.
//					OrimQuestTimer end_3 = new OrimQuestTimer(_STATUS_ORIMQUEAT_END_3, 12000);
//					end_3.begin();
//					break;
//				case _STATUS_ORIMQUEAT_END_3:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_END_4);
//					// 퀘스트 상태를 준비로 변경
//					sendMessage("$9581", 0);
//					// 오림 : 돌아 왔 더니, 추억의 장소는 모두 흔적도없이사라져 버린 것 ....
//					OrimQuestTimer end_4 = new OrimQuestTimer(_STATUS_ORIMQUEAT_END_4, 12000);
//					end_4.begin();
//					break;
//				case _STATUS_ORIMQUEAT_END_4:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_END_5);
//					// 퀘스트 상태를 준비로 변경
//					sendMessage("$9582", 0);
//					// 오림 : 싸움은 시작에 불과합니다. 나는 스승을 만나이싸움을 끝낼 방법을 찾아보십시오.
//					OrimQuestTimer end_5 = new OrimQuestTimer(_STATUS_ORIMQUEAT_END_5, 12000);
//					end_5.begin();
//					break;
//				case _STATUS_ORIMQUEAT_END_5:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_END_6);
//					// 퀘스트 상태를 준비로변경
//					sendMessage("$9583", 0);
//					// 오림 : 여러분, 부디 살아남은하십시오. 이 싸움이 끝날때까지 ...
//					OrimQuestTimer end_6 = new OrimQuestTimer(_STATUS_ORIMQUEAT_END_6, 12000);
//					end_6.begin();
//					break;
//				case _STATUS_ORIMQUEAT_END_6:
//					setOrimQuestStatus(_STATUS_ORIMQUEAT_END);// 퀘스트 상태를 준비로 변경
//					sendMessage("$9584", 0);
//					// 오림 : 좀에서 글루딘 영지입니다! 빨리이 위기를 사람들에게알려야합니다!
//					OrimQuestTimer end = new OrimQuestTimer(_STATUS_ORIMQUEAT_END, 12000);
//					end.begin();
//					break;
//				case _STATUS_ORIMQUEAT_END:// 본래라면 순위 화면이 표시된다
//					// 모든 플레이어를 배출
//					outPushPlayer();
//					break;
//				/********** 종료 준비 단계 END **************/
//				case _STATUS_MOVE_SHIP_1:
//					try {
//						int totalShipDamage = 0;
//						int shipId = 0;
//						L1Location loc = new L1Location(32794 + _random.nextInt(8), 32831, _mapId);
//
//						int typeA = 0;// 호세 해적선
//						int typeB = 0;// 디에고 해적선
//						int typeC = 0;// 드레이크 유령선
//
//						for (int i = 0; i < 12; i++) {
//							if (i < 4) {// 4 라운드까지의 통계를
//								typeA += _roundHitDamage[i];
//							} else if (i >= 4 && 7 >= i) {
//								typeB += _roundHitDamage[i];
//							} else if (i >= 8 && 10 >= i) {
//								typeC += _roundHitDamage[i];
//							}
//							totalShipDamage += _roundHitDamage[i];
//						}
//
//						if ((_currentRound == 2 || _currentRound == 3) && !_IS_SPAWN_SHIP_TYPEA && typeA >= 9) {// 유령선
//																												// A
//							_IS_SPAWN_SHIP_TYPEA = true;
//							_ship = spawnOne(loc, 91441, 0);
//						} else if ((_currentRound == 6 || _currentRound == 7) && !_IS_SPAWN_SHIP_TYPEB && typeB >= 9) {// 유령선B
//							_IS_SPAWN_SHIP_TYPEB = true;
//							_ship = spawnOne(loc, 91441, 0);
//						} else if ((_currentRound == 10) && !_IS_SPAWN_SHIP_TYPEC && typeC >= 9) {// 유령선C
//							_IS_SPAWN_SHIP_TYPEC = true;
//							_ship = spawnOne(loc, 91442, 0);
//						} else {
//							int shipDamage = 0;
//							if (_currentRound >= 0 && _currentRound <= 3) {
//								shipDamage = typeA;
//							} else if (_currentRound >= 4 && _currentRound <= 7) {
//								shipDamage = typeB;
//							} else if (_currentRound >= 8 && _currentRound <= 11) {
//								shipDamage = typeC;
//							}
//							if (shipDamage >= 9) {// 24이상
//								shipId = 91448;
//							} else if (shipDamage >= 6) {// 12이상
//								shipId = 91447;
//							} else {
//								shipId = 91446;
//							}
//							_ship = spawnOne(loc, shipId, 0);
//						}
//						if (_ship != null) {
//							while (_ship.moveDirection(loc.getX(), loc.getY() - 17) != -1) {
//								if (_ship.getLocation().getLineDistance(new Point(loc.getX(), loc.getY() - 17)) != 0) {
//									_ship.setDirectionMove(_ship.moveDirection(loc.getX(), loc.getY() - 17));
//									Thread.sleep(_ship.getMoveSpeed());
//								} else {
//									break;
//								}
//							}
//							sendShipDamage();
//							sendPackets(new S_ShockWave());
//							_acceptOrder = false;
//							clearAllRune();
//							if (_ship.getNpcId() == 91441) {
//								sendMessage("$9553", 0);
//								// 오림 : 지금 유령선! 숨쉴 틈도없는것인지!
//							} else if (_ship.getNpcId() == 91442) {
//								sendMessage("$9552", 0);
//								// 9552 에엣! 드레이크 선장의 배이라니! 조심하세요! 강한 해요!
//							} else if (_currentRound == 11) {
//
//							} else {
//								sendMessage("$9555", 0);
//								// 오림 : 선박끼리 충돌하면 적을 뛰어들어 와요! 조심하세요!
//							}
//
//							Thread.sleep(2000);//
//							if (_ship.getNpcId() == 91441 || _ship.getNpcId() == 91442) {
//								// 해적선이라면탑승발생
//								sendMessage("$9559", 1);
//								// 오림 : 적의 함선 내부로 통하는포털입니다! 조심하십시오.중에서 강한 생각이
//								// 느껴집니다.
//								if (_portal_rune == null) {
//									_portal_rune = spawnOne(new L1Location(32799, 32809, _mapId), 91452, 0);
//								}
//								if (_moveTrap == null) {
//									if (_ship.getNpcId() == 91442) {
//										_moveTrap = setSwitch(new L1Location(32799, 32809, _mapId), 73);
//									} else {
//										_moveTrap = setSwitch(new L1Location(32799, 32809, _mapId), 72);
//										for (L1Object obj : L1World.getInstance().getVisiblePoint(
//												new L1Location(32741, 32860, _mapId), 15)) {
//											if (obj instanceof L1ItemInstance) {
//												L1Inventory groundInventory = L1World.getInstance().getInventory(
//														obj.getX(), obj.getY(), obj.getMapId());
//												groundInventory.deleteItem((L1ItemInstance) obj);
//												L1World.getInstance().removeVisibleObject(obj);
//												L1World.getInstance().removeObject(obj);
//											} else if (obj instanceof L1MonsterInstance) {
//												obj.getMap().setPassable(obj.getX(), obj.getY(), true);
//												((L1MonsterInstance) obj).deleteMe();
//											}
//										}
//
//									}
//								}
//							} else if (_currentRound == 3) {// 일반적 적선 : 타락
//															// <리치<제니스 퀸
//								int npcId = 0;
//								if (_point <= 300) {// 타락
//									npcId = 91480;
//								} else if (_point <= 600) {// 리치
//									npcId = 91476;
//								} else if (_point > 600) {// 제니스 퀸
//									npcId = 91478;
//								}
//								if (typeA >= 12) {
//									spawnOneMob(new L1Location(32671, 32802, _mapId), npcId);
//									if (_portal_rune == null) {
//										_portal_rune = spawnOne(new L1Location(32799, 32809, _mapId), 91452, 0);
//									}
//									if (_moveTrap == null) {
//										_moveTrap = setSwitch(new L1Location(32799, 32809, _mapId), 70);
//									}
//								}
//							} else if (_currentRound == 7) {// 일반적 적선 : 타락
//															// <리치<제니스 퀸
//								int npcId = 0;
//								if (_point <= 300) {
//									npcId = 91480;
//								} else if (_point <= 600) {
//									npcId = 91476;
//								} else if (_point > 600) {
//									npcId = 91478;
//								}
//								if (typeB >= 12) {
//									spawnOneMob(new L1Location(32671, 32866, _mapId), npcId);
//									if (_portal_rune == null) {
//										_portal_rune = spawnOne(new L1Location(32799, 32809, _mapId), 91452, 0);
//									}
//									if (_moveTrap == null) {
//										_moveTrap = setSwitch(new L1Location(32799, 32809, _mapId), 71);
//									}
//								}
//							}
//							while (_ship.moveDirection(loc.getX(), loc.getY() - 13) != -1) {
//								if (_ship.getLocation().getLineDistance(new Point(loc.getX(), loc.getY() - 13)) != 0) {
//									_ship.setDirectionMove(_ship.moveDirection(loc.getX(), loc.getY() - 13));
//									Thread.sleep(_ship.getMoveSpeed());
//								} else {
//									break;
//								}
//							}
//						}
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					break;
//				case _STATUS_MOVE_SHIP_2:
//					if (_ship != null) {
//						try {
//							if (_portal_rune != null) {
//								_portal_rune.deleteMe();
//								_portal_rune = null;
//							}
//							if (_moveTrap != null) {
//								L1WorldTraps.getInstance().removeTrap(_moveTrap);
//								_moveTrap = null;
//							}
//							while (_ship.moveDirection(_ship.getX(), _ship.getY() + 13) != -1) {
//								if (_ship.getLocation().getLineDistance(new Point(_ship.getX(), _ship.getY() + 13)) != 0) {
//									_ship.setDirectionMove(_ship.moveDirection(_ship.getX(), _ship.getY() + 13));
//									Thread.sleep(_ship.getMoveSpeed());
//								} else {
//									break;
//								}
//							}
//						} catch (InterruptedException e1) {
//							e1.printStackTrace();
//						}
//						deleteNpc(_ship);
//						_ship = null;
//					}
//					break;
//				case _STATUS_MOVE_SEA_MONSTER:
//					try {
//						L1NpcInstance npc = spawnOne(
//								L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false),
//								_tempNpcNumber, _random.nextInt(8));
//						Thread.sleep(1000);
//						L1Location temploc = new L1Location(npc.getX(), npc.getY(), npc.getMapId());
//						temploc.forward(npc.getHeading());
//						for (int i = 0; i < 40; i++) {
//							while (npc.moveDirection(temploc.getX(), temploc.getY()) != -1) {
//								if (npc.getLocation().getLineDistance(new Point(temploc.getX(), temploc.getY())) != 0) {
//									npc.setDirectionMove(npc.moveDirection(temploc.getX(), temploc.getY()));
//									Thread.sleep(npc.getMoveSpeed());
//								} else {
//									break;
//								}
//							}
//							temploc.forward(npc.getHeading());
//						}
//						deleteNpc(npc);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					break;
//				case _STATUS_ORIMQUEST_MONITOR:
//					boolean flag = false;
//					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
//						if (getMapId() == pc.getMapId()) {
//							flag = true;
//							break;
//						}
//					}
//					if (flag) {
//						OrimQuestTimer monitorRepeat = new OrimQuestTimer(_STATUS_ORIMQUEST_MONITOR, 600000);
//						monitorRepeat.begin();
//					} else {
//						_isDeleteTransactionNow = true;// 삭제 처리로 이상
//						reset();
//						L1OrimQuest.getInstance().resetActiveMaps(_mapId);// 인스턴스맵을해제
//					}
//					break;
//				case _STATUS_SPAWN_MONSTERS:
//					if (_extraShipCome) {
//						if (_comeShipCount == 0) {
//							for (int i = 0; i < 7; i++) {
//								spawnOneMob(
//										L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false),
//										_monstersC[_random.nextInt(_monstersC.length)]);
//							}
//						} else if (_comeShipCount == 1) {
//							for (int i = 0; i < 7; i++) {
//								spawnOneMob(
//										L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false),
//										_monstersC[_random.nextInt(_monstersC.length)]);
//							}
//						} else if (_comeShipCount == 2) {
//							for (int i = 0; i < 7; i++) {
//								spawnOneMob(
//										L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false),
//										_monstersB[_random.nextInt(_monstersB.length)]);
//							}
//						}
//						_comeShipCount++;
//					} else {
//						if (_currentRound == 0) {
//							if (!_isRankSelected) {// 연습 후에 출현하는 순위 결정 MOB
//								for (int i = 0; i < 7; i++) {
//									spawnOneMob(L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6,
//											false), _monstersA[_random.nextInt(5)]);
//								}
//							} else {
//
//							}
//						}
//					}
//					break;
//				case _STATUS_CREATE_RUNES:
//					createRune();
//					break;
//				case _STATUS_ENEMY_CANON:
//					int[][] table = { { 32780, 32818, 35 }, { 32793, 32788, 16 } };
//					int random = _random.nextInt(2);
//					S_EffectLocation se = new S_EffectLocation(table[random][0] + _random.nextInt(table[random][2]),
//							table[random][1], 8233);
//					sendPackets(se);
//					break;
//				case _STATUS_FIRE_CANON_1:
//					if (_cannon_1 != null) {
//						_cannon_1.broadcastPacket(new S_DoActionGFX(_cannon_1.getId(), 2));
//					}
//					break;
//				case _STATUS_FIRE_CANON_2:
//					if (_cannon_2 != null) {
//						_cannon_2.broadcastPacket(new S_DoActionGFX(_cannon_2.getId(), 2));
//					}
//					break;
//				}// isDeleteTrancaction End
//			}// swicth End
//			cancel();
//		}
//	}
//
//	/**
//	 * 퀘스트에 참여하는 플레이어에게 메시지를 보냅니다.
//	 */
//	private void sendMessage(String s, int cases) {
//		if (cases == 0) {
//			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
//				if (pc.getMapId() == _mapId) {
//					pc.sendPackets(new S_GreenMessage(s));
//				}
//			}
//		} else if (cases == 1) {
//			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
//				if (pc.getMapId() == _mapId) {
//					pc.sendPackets(new S_YellowMessage(s));
//				}
//			}
//		} else if (cases == 2) {
//			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
//				if (pc.getMapId() == _mapId) {
//					pc.sendPackets(new S_RedMessage(s));
//				}
//			}
//		}
//
//	}
//
//	/**
//	 * 퀘스트에 참여하는 플레이어에 인수로 전달 된 패킷을 전송한다.
//	 */
//	private void sendPackets(ServerBasePacket serverbasepacket) {
//		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
//			if (pc.getMapId() == _mapId) {
//				pc.sendPackets(serverbasepacket);
//			}
//		}
//	}
//
//	public short getMapId() {
//		return _mapId;
//	}
//
//	public void setMapId(short _mapId) {
//		this._mapId = _mapId;
//	}
//
//	public void setOrimQuestStatus(int step) {
//		_OrimQuestStatus = step;
//	}
//
//	/**
//	 * 소셜 액션에 대한 응답으로 플래그 설정
//	 */
//	public void setAction(L1PcInstance pc, int actionCode) {
//		if (pc != null) {
//			if (pc.isInParty()) {
//				if (pc.getParty().isLeader(pc) && pc.getMapId() == _mapId) {
//					if (_OrimQuestStatus == _STATUS_ORIMQUEAT_READY_3) {
//						_action = actionCode;
//					} else if (_OrimQuestStatus > _STATUS_ORIMQUEAT_READY_3) {
//						if (pc.getX() == 32799 && pc.getY() == 32808) {// 갑판에있습니까?
//							if (actionCode == 66) {// Alt +4 공격
//								fireGun();
//							} else if (actionCode == 69) {// Alt +2 방어
//								diffenceShip();
//							}
//						}
//					}
//				}
//			} else if (pc.isGm()) {
//				if (pc.getMapId() == _mapId) {
//					if (_OrimQuestStatus == _STATUS_ORIMQUEAT_READY_3) {
//						_action = actionCode;
//					} else if (_OrimQuestStatus > _STATUS_ORIMQUEAT_READY_3) {
//						if (pc.getX() == 32799 && pc.getY() == 32808) {// 갑판에있습니까?
//							if (actionCode == 66) {// Alt +4 공격
//								fireGun();
//							} else if (actionCode == 69) {// Alt+2 방어
//								diffenceShip();
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//
//	/**
//	 * 룬이 모두 사라 졌는지 조사한다.
//	 */
//
//	private boolean checkRune() {
//		return _diffence_rune_2 == null && _diffence_rune_1 == null && _attack_rune == null;
//	}
//
//	/**
//	 * 오림의 배를 방어 마법을 걸어 배를 강화
//	 */
//	public void diffenceShip() {
//		if (_diffence_rune_1 != null && _diffence_rune_2 != null) {
//			_diffence_rune_1.deleteMe();
//			_diffence_rune_2.deleteMe();
//			_diffence_rune_1 = null;
//			_diffence_rune_2 = null;
//			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
//				if (pc.getMapId() == _mapId) {
//					pc.broadcastPacket(new S_SkillSound(pc.getId(), 2030));
//					pc.sendPackets(new S_SkillSound(pc.getId(), 2030));
//				}
//			}
//			if (_OrimQuestStatus >= _STATUS_ORIMQUEAT_START) {
//				if (_random.nextInt(4) > 0) {
//					_roundDiffenceStatus[_currentRound]++;
//				} else {
//					enemyCanon();
//				}
//			}
//			if (checkRune()) {
//				OrimQuestTimer createRuneTimer = new OrimQuestTimer(_STATUS_CREATE_RUNES, 3000);
//				createRuneTimer.begin();
//			}
//		}
//	}
//
//	/**
//	 * 포격을 개시한다.
//	 */
//	public void fireGun() {
//		if (_attack_rune != null) {
//			_attack_rune.deleteMe();
//			_attack_rune = null;
//			// 불꽃 놀이 표시)
//			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
//				if (pc.getMapId() == _mapId) {
//					pc.broadcastPacket(new S_SkillSound(pc.getId(), 2029));
//					pc.sendPackets(new S_SkillSound(pc.getId(), 2029));
//				}
//			}
//
//			if (!(_OrimQuestStatus >= _STATUS_ORIMQUEAT_START)) {
//				sendMessage("HIT!", 1);
//			} else {
//				if (_random.nextInt(4) > 0) {
//					if (_roundHitDamage[_currentRound] == 0) {
//						_roundHitDamage[_currentRound]++;
//						sendMessage("HIT!", 1);
//					} else if (_roundHitDamage[_currentRound] == 1) {
//						_roundHitDamage[_currentRound]++;
//						sendMessage("HIT!", 0);
//					} else if (_roundHitDamage[_currentRound] == 2) {
//						_roundHitDamage[_currentRound]++;
//						sendMessage("HIT!", 2);
//					} else {
//						sendMessage("HIT!", 2);
//					}
//				} else {
//					enemyCanon();
//				}
//			}
//
//			fireCanon();
//
//			if (checkRune()) {
//				OrimQuestTimer createRuneTimer = new OrimQuestTimer(_STATUS_CREATE_RUNES, 3000);
//				createRuneTimer.begin();
//			}
//		}
//
//	}
//
//	private L1NpcInstance spawnOne(L1Location loc, int npcid, int heading) {
//		L1NpcInstance mob = new L1NpcInstance(NpcTable.getInstance().getTemplate(npcid));
//		if (mob == null) {
//			return mob;
//		}
//
//		mob.setId(IdFactory.getInstance().nextId());
//		mob.setHeading(heading);
//		mob.setX(loc.getX());
//		mob.setHomeX(loc.getX());
//		mob.setY(loc.getY());
//		mob.setHomeY(loc.getY());
//		mob.setMap((short) loc.getMapId());
//		if (mob.getNpcId() == 91487) {
//			mob.setTempCharGfx(8511 + _random.nextInt(5));
//		}
//
//		L1World.getInstance().storeObject(mob);
//		L1World.getInstance().addVisibleObject(mob);
//
//		S_NpcPack s_npcPack = new S_NpcPack(mob);
//		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(mob)) {
//			pc.addKnownObject(mob);
//			mob.addKnownObject(pc);
//			pc.sendPackets(s_npcPack);
//		}
//		// 몬스터의 AI를 시작
//		mob.onNpcAI();
//		mob.getLight().turnOnOffLight();
//		mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 시작
//		return mob;
//	}
//
//	/**
//	 * 지정된 지점에 모든 몬스터를 한 마리 생성한다.
//	 */
//	private L1MonsterInstance spawnOneMob(L1Location loc, int npcid) {
//		L1MonsterInstance mob = new L1MonsterInstance(NpcTable.getInstance().getTemplate(npcid));
//		if (mob == null) {
//			return mob;
//		}
//
//		mob.setId(IdFactory.getInstance().nextId());
//		mob.setHeading(5);
//		mob.setX(loc.getX());
//		mob.setHomeX(loc.getX());
//		mob.setY(loc.getY());
//		mob.setHomeY(loc.getY());
//		mob.setMap((short) loc.getMapId());
//		mob.set_storeDroped(false);
//		mob.setUbSealCount(0);
//
//		L1World.getInstance().storeObject(mob);
//		L1World.getInstance().addVisibleObject(mob);
//
//		S_NpcPack s_npcPack = new S_NpcPack(mob);
//		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(mob)) {
//			pc.addKnownObject(mob);
//			mob.addKnownObject(pc);
//			pc.sendPackets(s_npcPack);
//		}
//		// 몬스터의 AI를 시작
//		mob.onNpcAI();
//		mob.getLight().turnOnOffLight();
//		mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 시작
//		return mob;
//	}
//
//	private void clearAllRune() {
//		// 초기화
//		clearAttackRune();
//		clearDiffenceRune();
//	}
//
//	private void clearAttackRune() {
//		// 初초기화化
//		if (_attack_rune != null) {
//			_attack_rune.deleteMe();
//			_attack_rune = null;
//		}
//	}
//
//	private void clearDiffenceRune() {
//		// 초기화
//		if (_diffence_rune_1 != null) {
//			_diffence_rune_1.deleteMe();
//			_diffence_rune_1 = null;
//		}
//		if (_diffence_rune_2 != null) {
//			_diffence_rune_2.deleteMe();
//			_diffence_rune_2 = null;
//		}
//	}
//
//	private void createRune() {
//		if (_acceptOrder) {
//			if (_attack_rune == null) {
//				_attack_rune = spawnOne(L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false),
//						91454, 5);
//			}
//			if (_diffence_rune_1 == null) {
//				_diffence_rune_1 = spawnOne(
//						L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false), 91453, 5);
//			}
//			if (_diffence_rune_2 == null) {
//				_diffence_rune_2 = spawnOne(
//						L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false), 91453, 5);
//			}
//		}
//	}
//
//	private void deleteNpc(L1NpcInstance npc) {
//		npc.getMap().setPassable(npc.getX(), npc.getY(), true);
//		npc.deleteMe();
//	}
//
//	public void mimicDie(L1MonsterInstance npc) {
//		if (npc.isCurseMimic()) {
//			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 15)) {
//				L1Teleport.teleport(pc, 32792, 32802, _mapId, 2, true);
//			}
//			if (_mimic_A != null) {
//				if (!_mimic_A.isDead()) {
//					_mimic_A.deleteMe();
//				}
//				_mimic_A = null;
//			}
//			if (_mimic_B != null) {
//				if (!_mimic_B.isDead()) {
//					_mimic_B.deleteMe();
//				}
//				_mimic_B = null;
//			}
//			if (_mimic_C != null) {
//				if (!_mimic_C.isDead()) {
//					_mimic_C.deleteMe();
//				}
//				_mimic_C = null;
//			}
//		}
//	}
//
//	private boolean checkSeaMonsterAttack(int type) {
//		boolean flag = false;
//		int count = 0;
//		for (int i = 0; i < 12; i++) {
//			if (_bonusRoundStatus[i] == type) {
//				count++;
//			}
//		}
//		if (count == 3) {
//			flag = true;
//		}
//		if (flag) {
//			if (type == 1) {
//				for (int i = 0; i < 3; i++) {
//					spawnOneMob(L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false), 91483);
//				}
//				sendMessage("$9549", 1);// 오림 : 으윽! 해양 생물에서 공격을 실행 해오고 있습니다. 어째서
//										// 이런 때에!
//			} else if (type == 2) {
//				for (int i = 0; i < 3; i++) {
//					spawnOneMob(L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false), 91482);
//				}
//				sendMessage("$9549", 1);// 오림 : 으윽! 해양 생물에서 공격을 실행 해오고 있습니다. 어째서
//										// 이런 때에!
//			} else if (type == 3) {
//				spawnOneMob(new L1Location(32800, 32794, _mapId), 91481);// 크라켄
//				spawnOneMob(new L1Location(32793, 32795, _mapId), 91491);// 다리왼쪽
//				spawnOneMob(new L1Location(32804, 32796, _mapId), 91492);// 다리오른쪽
//				sendMessage("$9558", 1);// 오림 : 으윽! 성난 해양 생물이 밀려 들어오고 있습니다!
//			}
//		}
//		return flag;
//	}
//
//	/**
//	 * 월드에 새로운 함정을 만들어 추가 ※ L1ReloadTrap이 실행될 때 삭제되는 점에주의
//	 */
//	private static L1TrapInstance setSwitch(L1Location loc, int id) {
//		final int trapId = id;
//		final L1Trap trapTemp = TrapTable.getInstance().getTemplate(trapId);
//		final Point rndPt = new Point();
//		rndPt.setX(0);
//		rndPt.setY(0);
//		final int span = 0;
//		final L1TrapInstance trap = new L1TrapInstance(IdFactory.getInstance().nextId(), trapTemp, loc, rndPt, span);
//		L1World.getInstance().addVisibleObject(trap);
//		L1WorldTraps.getInstance().addTrap(trap);
//		return trap;
//	}
//
//	private int checkAliveMonster() {
//		int count = 0;
//		for (Object obj : L1World.getInstance().getVisiblePoint(new L1Location(32799, 32803, _mapId), 15)) {
//			if (obj instanceof L1MonsterInstance) {
//				if (!((L1MonsterInstance) obj).isDead()) {
//					count++;
//				}
//			}
//		}
//		return count;
//	}
//
//	private void spawnMonsters() {
//		int startPoint = _startPoint;
//		int endPoint = _endPoint;
//		for (int i = 0; i < _currentRound; i++) {
//			if (_roundStatus[i] == 1) {
//				if (endPoint + 1 >= _monstersA.length) {
//					continue;
//				}
//				startPoint++;
//				endPoint++;
//			} else if (_roundStatus[i] == -1) {
//				if (startPoint - 1 < 0) {
//					continue;
//				}
//				startPoint--;
//				endPoint--;
//			}
//		}
//		for (int i = 0; i < 7; i++) {
//			spawnOneMob(L1Location.randomLocation(new L1Location(32799, 32803, _mapId), 1, 6, false),
//					_monstersA[startPoint + _random.nextInt(endPoint - startPoint)]);
//		}
//	}
//
//	private void outPushPlayer() {
//		for (Object obj : L1World.getInstance().getVisibleObjects(_mapId).values()) {
//			if (obj instanceof L1PcInstance) {
//				L1PcInstance pc = (L1PcInstance) obj;
//				final int rndx = _random.nextInt(4);
//				final int rndy = _random.nextInt(4);
//				final int locx = 32587 + rndx;
//				final int locy = 32941 + rndy;
//				final short mapid = 0;// 말하는 섬
//				L1Teleport.teleport(pc, locx, locy, mapid, 5, false);
//			} else {
//				if (obj instanceof L1NpcInstance) {
//					deleteNpc((L1NpcInstance) obj);
//					;
//				}
//			}
//		}
//	}
//
//	private void sendShipDamage() {
//
//		if (_roundDiffenceStatus[_currentRound] < 2) {// 2 회 이상 방어진을 치고해야 연락선에
//														// 직접 대미지
//			if (_shipWall.getStatus() == 35) {
//				_shipWall.setStatus(36);// 침몰
//			} else {
//				_shipWall.setStatus(_shipWall.getStatus() + 1);
//			}
//			_shipWall.broadcastPacket(new S_DoActionGFX(_shipWall.getId(), _shipWall.getStatus()));
//			if (_shipWall.getStatus() > 35) {
//				sendMessage("$9562", 1);// 오림 : 배가 침몰합니다! 서둘러 탈출하세요! 이 상태에서
//										// 위험하다!
//				sendPackets(new S_ShockWave());
//				try {
//					Thread.sleep(35000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				outPushPlayer();
//			}
//		}
//	}
//
//	private void enemyCanon() {
//		int i = _random.nextInt(4);
//		for (int z = 0; z < i; z++) {
//			OrimQuestTimer timer = new OrimQuestTimer(_STATUS_ENEMY_CANON, 2000 + 2000 * z);
//			timer.begin();
//		}
//	}
//
//	private void fireCanon() {
//		int i = _random.nextInt(2) + 2;// 2~3회
//		int n = _random.nextInt(2);
//		for (int z = 0; z < i; z++) {
//			if ((n + z) % 2 == 1) {
//				OrimQuestTimer timer = new OrimQuestTimer(_STATUS_FIRE_CANON_1, 2000 * z);
//				timer.begin();
//			} else {
//				OrimQuestTimer timer = new OrimQuestTimer(_STATUS_FIRE_CANON_2, 2000 * z);
//				timer.begin();
//			}
//		}
//	}
//
//	private void reset() {
//		try {
//			for (L1Object obj : L1World.getInstance().getVisibleObjects(_mapId).values()) {
//				if (obj.getMapId() == getMapId()) {
//					if (obj instanceof L1FieldObjectInstance) {
//						L1World.getInstance().removeVisibleObject(obj);
//						L1World.getInstance().removeObject(obj);
//					} else if (obj instanceof L1EffectInstance) {
//						L1World.getInstance().removeVisibleObject(obj);
//						L1World.getInstance().removeObject(obj);
//					} else if (obj instanceof L1ItemInstance) {
//						final L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(),
//								obj.getMapId());
//						groundInventory.deleteItem((L1ItemInstance) obj);
//						L1World.getInstance().removeVisibleObject(obj);
//						L1World.getInstance().removeObject(obj);
//					} else if (obj instanceof L1DoorInstance) {
//						DoorTable.getInstance().deleteDoorByLocation(obj.getLocation());
//					} else if (obj instanceof L1NpcInstance) {
//						((L1NpcInstance) obj).deleteMe();
//					} else if (obj instanceof L1TrapInstance) {
//						L1WorldTraps.getInstance().removeTrap((L1TrapInstance) obj);
//					}
//				}
//			}
//		} catch (final Exception e) {
//
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 오징어의 사망 신고
//	 * 
//	 */
//	public void crakenDead() {
//		this._isCrakenDead = true;
//		if (_craken_tail_left != null) {
//			_craken_tail_left.broadcastPacket(new S_DoActionGFX(_craken_tail_left.getId(), ActionCodes.ACTION_Die));
//			_craken_tail_left.deleteMe();
//		}
//
//		if (_craken_tail_right != null) {
//			_craken_tail_right.broadcastPacket(new S_DoActionGFX(_craken_tail_right.getId(), ActionCodes.ACTION_Die));
//			_craken_tail_right.deleteMe();
//		}
//	}
//
//	public void crakenTailDead_Left() {
//		_craken_tail_left = null;
//	}
//
//	public void crakenTailDead_Right() {
//		_craken_tail_right = null;
//	}
//
//}


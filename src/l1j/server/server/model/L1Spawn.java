package l1j.server.server.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.controller.action.GameTimeNight;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class L1Spawn {
	private static Logger _log = Logger.getLogger(L1Spawn.class.getName());
	
	private final L1Npc _template;
	private L1Npc _templateNight;

	private int _id; // just to find this in the spawn table
	private String _location;
	private int _maximumCount;
	private int _npcid;
	private int _groupId;
	private int _locx;
	private int _locy;
	private int _randomx;
	private int _randomy;
	private int _locx1;
	private int _locy1;
	private int _locx2;
	private int _locy2;
	private int _heading;
	private int _minRespawnDelay;
	private int _maxRespawnDelay;
	private final Constructor<?> _constructor;
	private short _mapid;
	private boolean _respaenScreen;
	private int _movementDistance;
	private boolean _rest;
	private int _spawnType;
	private int _delayInterval;
	private HashMap<Integer, Point> _homePoint = null;
	private String _name;

	//TODO NPC 스폰 처리를 담당하는 쓰레드
	private class SpawnTask implements Runnable {
		private int _spawnNumber;
		private int _objectId;

		private SpawnTask(int spawnNumber, int objectId) {
			_spawnNumber	= spawnNumber;
			_objectId		= objectId;
		}

		@Override
		public void run(){
			doSpawn(_spawnNumber, _objectId);
		}
	}

	public L1Spawn(L1Npc template) throws SecurityException, ClassNotFoundException {
		_template		= template;
		_constructor	= Class.forName(String.format("l1j.server.server.model.Instance.%sInstance", _template.getImpl())).getConstructors()[0];
	}
	  
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public short getMapId() {
		return _mapid;
	}

	public void setMapId(short _mapid) {
		this._mapid = _mapid;
	}

	public boolean isRespawnScreen() {
		return _respaenScreen;
	}

	public void setRespawnScreen(boolean flag) {
		_respaenScreen = flag;
	}

	public int getMovementDistance() {
		return _movementDistance;
	}

	public void setMovementDistance(int i) {
		_movementDistance = i;
	}

	public int getAmount() {
		return _maximumCount;
	}

	public int getGroupId() {
		return _groupId;
	}

	public int getId() {
		return _id;
	}

	public String getLocation() {
		return _location;
	}

	public int getLocX() {
		return _locx;
	}

	public int getLocY() {
		return _locy;
	}

	public int getNpcId() {
		return _npcid;
	}

	public int getHeading() {
		return _heading;
	}

	public int getRandomx() {
		return _randomx;
	}

	public int getRandomy() {
		return _randomy;
	}

	public int getLocX1() {
		return _locx1;
	}

	public int getLocY1() {
		return _locy1;
	}

	public int getLocX2() {
		return _locx2;
	}

	public int getLocY2() {
		return _locy2;
	}

	public int getMinRespawnDelay() {
		return _minRespawnDelay;
	}

	public int getMaxRespawnDelay() {
		return _maxRespawnDelay;
	}

	public void setAmount(int amount) {
		_maximumCount = amount;
	}

	public void setId(int id) {
		_id = id;
	}

	public void setGroupId(int i) {
		_groupId = i;
	}

	public void setLocation(String location) {
		_location = location;
	}

	public void setLocX(int locx) {
		_locx = locx;
	}

	public void setLocY(int locy) {
		_locy = locy;
	}

	public void setNpcid(int npcid) {
		_npcid = npcid;
	}

	public void setHeading(int heading) {
		_heading = heading;
	}

	public void setRandomx(int randomx) {
		_randomx = randomx;
	}

	public void setRandomy(int randomy) {
		_randomy = randomy;
	}

	public void setLocX1(int locx1) {
		_locx1 = locx1;
	}

	public void setLocY1(int locy1) {
		_locy1 = locy1;
	}

	public void setLocX2(int locx2) {
		_locx2 = locx2;
	}

	public void setLocY2(int locy2) {
		_locy2 = locy2;
	}

	public void setMinRespawnDelay(int i) {
		_minRespawnDelay = i;
	}

	public void setMaxRespawnDelay(int i) {
		_maxRespawnDelay = i;
	}

	private int calcRespawnDelay() {
		int respawnDelay = _minRespawnDelay * 1000;
		if (_delayInterval > 0) {
			respawnDelay += CommonUtil.random(_delayInterval) * 1000;
		}
		return respawnDelay;
	}

	//TODO 스폰쓰레드 호출
	public void executeSpawnTask(int spawnNumber, int objectId) {
		SpawnTask task = new SpawnTask(spawnNumber, objectId);
		GeneralThreadPool.getInstance().schedule(task, calcRespawnDelay());
	}

	private boolean _initSpawn;
	private boolean _spawnHomePoint;

	public void init(boolean is_spawn) {
		_templateNight	= SpawnTable.getNightNpc(_template.getNpcId(), _mapid);
		_delayInterval = _maxRespawnDelay - _minRespawnDelay;
		if (Config.ALT.SPAWN_HOME_POINT && Config.ALT.SPAWN_HOME_POINT_COUNT <= getAmount() && Config.ALT.SPAWN_HOME_POINT_DELAY >= getMinRespawnDelay() && isAreaSpawn()) {
			_spawnHomePoint = true;
			_homePoint = new HashMap<Integer, Point>();
		}

		if (!is_spawn) {
			return;
		}
		_initSpawn = true;
		int spawnNum = 0;
		while (spawnNum < _maximumCount) {
			doSpawn(++spawnNum);
		}
		_initSpawn = false;
	}

	protected void doSpawn(int spawnNumber) { 
		doSpawn(spawnNumber, 0);
	}

	protected void doSpawn(int spawnNumber, int objectId) {
		L1NpcInstance npc = null;
		try {
			boolean isNight =  _templateNight != null && GameTimeNight.isNight();
			Object parameters[] = { isNight ? _templateNight : _template };
			int newlocx	= _locx, newlocy = _locy, tryCount = 0;
			
			npc = (L1NpcInstance) _constructor.newInstance(parameters);
			
			npc.setId(objectId == 0 ? IdFactory.getInstance().nextId() : objectId);
			npc.getMoveState().setHeading((0 <= _heading && _heading <= 7) ? _heading : 5);

			int npcId = npc.getNpcTemplate().getNpcId();

			//if ((npcId >= 18302) && (npcId <= 18305))
			//	System.out.println("[L1Spawn.doSpawn()] Treasure with ID " + npcId);

			if (npcId == 45488 && _mapid == 809) {
				npc.setMap((short) (_mapid + CommonUtil.random(2)));// 카스파패밀리
			} else {
				npc.setMap(_mapid);
			}
			
			npc.setMovementDistance(_movementDistance);
			npc.setRest(_rest); 
			ArrayList<L1PcInstance> players	= null;
			L1PcInstance pc					= null;
			L1Location loc					= null;
			Point pt						= null;
			L1World world					= L1World.getInstance();
			while(tryCount <= 50){
				switch(_spawnType){
				case SPAWN_TYPE_PC_AROUND:// PC주변에서 스폰
					if (!_initSpawn) { 
						players = new ArrayList<L1PcInstance>();			
						for (L1PcInstance _pc : world.getAllPlayers()) {
							if (_mapid == _pc.getMapId()) {
								players.add(_pc);
							}
						}
						if (players.size() > 0) {
							pc		= players.get(CommonUtil.random(players.size()));
							loc		= pc.getLocation().randomLocation(PC_AROUND_DISTANCE, false);
							newlocx	= loc.getX();
							newlocy	= loc.getY();
							break;
						}
					}
				default:
					if (isAreaSpawn()) {// 범위스폰
						if (_mapid == L1TownLocation.GETBACK_MAP_TREASURE_ISLAND) {
							newlocx		= CommonUtil.random(_locx2 - _locx1) + _locx1;
							newlocy		= CommonUtil.random(_locy2 - _locy1) + _locy1;
						} else if (!_initSpawn && _spawnHomePoint) {
							pt		= _homePoint.get(spawnNumber);
							loc		= new L1Location(pt, _mapid).randomLocation(Config.ALT.SPAWN_HOME_POINT_RANGE, false);
							newlocx	= loc.getX();
							newlocy	= loc.getY();
						} else {
							newlocx		= CommonUtil.random(_locx2 - _locx1) + _locx1;
							newlocy		= CommonUtil.random(_locy2 - _locy1) + _locy1;
						}
						if (tryCount > 49) {
							newlocx = _locx;
							newlocy = _locy;
						}
					} else if (isRandomSpawn()) {
						newlocx = (_locx + ((int) (Math.random() * _randomx) - (int) (Math.random() * _randomx)));
						newlocy = (_locy + ((int) (Math.random() * _randomy) - (int) (Math.random() * _randomy)));
					} else {
						newlocx = _locx;
						newlocy = _locy;
					}
					break;
				}
				npc.setX(newlocx);
				npc.setHomeX(newlocx);
				npc.setY(newlocy);
				npc.setHomeY(newlocy);
				
				if (!(npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation()))) {
					npc.setX(_locx);
					npc.setHomeX(_locx);
					npc.setY(_locy);
					npc.setHomeY(_locy);
				}

				// 세이프티존 스폰 불가
				if (npc instanceof L1MonsterInstance 
						&& (npc.getMap().getInter() == L1InterServer.DOMINATION_TOWER || npc.getMap().getInter() == L1InterServer.ABADON || npc.getMapId() == 70)
						&& npc.getRegion() == L1RegionStatus.SAFETY) {
					continue;
				}
				
				if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
					if (npc instanceof L1MonsterInstance) {
						if (_respaenScreen) {
							break;
						}
						L1MonsterInstance mobtemp = (L1MonsterInstance) npc;
						if (world.getVisiblePlayer(mobtemp).size() == 0) {
							break;
						}
						SpawnTask task = new SpawnTask(spawnNumber, npc.getId());
						GeneralThreadPool.getInstance().schedule(task, 3000L);
						return;
					}
				}
				tryCount++;
			}

			if (npc instanceof L1MonsterInstance) {
				((L1MonsterInstance) npc).initHide();
			}

			npc.setSpawn(this);
			npc.setRespawn(true);
			npc.setSpawnNumber(spawnNumber); 
			if (_initSpawn && _spawnHomePoint) { 
				pt = new Point(npc.getX(), npc.getY());
				_homePoint.put(spawnNumber, pt);
			}

			if (npc instanceof L1MonsterInstance && npc.getMapId() == 666) {
				((L1MonsterInstance) npc).setStoreDroped(true);
			}
			
			if (npcId == 45573 && npc.getMapId() == 2) {// 바포메트 스폰시 텔레포트
				for (L1PcInstance each : world.getMapPlayer(npc.getMapId())) {
					each.getTeleport().start(32664, 32797, npc.getMapId(), 0, true);
				}
			} else if (npcId == 5095) { // 모래 폭풍 60초후에 삭제.
				Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 4), true);
				new L1NpcDeleteTimer(npc, 60000).begin();
			} else if (npcId == 8511 || npcId == 8512) {// 검은 전함 보스
				S_Effect effect = new S_Effect(npc.getId(), 12261);
				for (L1PcInstance each : world.getVisiblePlayer(npc)) {
					npc.onPerceive(each);
					each.sendPackets(effect);
				}
				effect.clear();
				effect = null;
			}/*else if(npcId == 7000094 && mob.getMapId() == 4){// 대흑장로 스폰시 주변으로 텔레포트
				for(L1PcInstance each : world.getVisiblePlayer(npc, 3))
					L1Teleport.teleport(each, npc.getX() + 5, npc.getY() + 5, (short) npc.getMapId(), each.getHeading(), true);
			}*/
			
			L1SpawnUtil.spawnAction(npc, npcId);
			
			world.storeObject(npc);
			world.addVisibleObject(npc);
			// ai start
			if (!_initSpawn && npc instanceof L1MonsterInstance) {
				L1MonsterInstance mobtemp = (L1MonsterInstance) npc;
				if (mobtemp.getHiddenStatus() == 0) {
					mobtemp.onNpcAI();
				}
			}
			
			// group spawn
			if (getGroupId() != 0) {
				L1MobGroupSpawn.getInstance().doSpawn(npc, getGroupId(), _respaenScreen, _initSpawn);
			}
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
		} catch (Exception e) {
			if (npc != null) {
				//System.out.println("엔피씨아이디: "+ npc.getNpcId());
				System.out.println("NPC ID: "+ npc.getNpcId());
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public void setRest(boolean flag) {
		_rest = flag;
	}

	public boolean isRest() {
		return _rest;
	}

	private static final int SPAWN_TYPE_PC_AROUND = 1;
	private static final int PC_AROUND_DISTANCE = 30;

	public void setSpawnType(int type) {
		_spawnType = type;
	}

	private boolean isAreaSpawn() {
		return _locx1 != 0 && _locy1 != 0 && _locx2 != 0 && _locy2 != 0;
	}

	private boolean isRandomSpawn() {
		return _randomx != 0 || _randomy != 0;
	}
	
}


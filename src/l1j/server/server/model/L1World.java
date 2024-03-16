package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1FakePcInstance;
import l1j.server.server.model.Instance.L1GuardInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.map.L1MapCluster;
import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.types.Point;
import l1j.server.server.utils.StringUtil;

public class L1World {
	private static Logger _log = Logger.getLogger(L1World.class.getName());
	private final ConcurrentHashMap<String, L1AiUserInstance> _allAiUsers;
	private final ConcurrentHashMap<String, L1PcInstance> _allPlayers;
	private final ConcurrentHashMap<Integer, L1PcInstance> _allPlayers_from_id;
	private final ConcurrentHashMap<String, L1PcInstance> _allGms;
	private final ConcurrentHashMap<Integer, L1PetInstance> _allPets;
	private final ConcurrentHashMap<Integer, L1SummonInstance> _allSummons;
	private final ConcurrentHashMap<Integer, L1Object> _allObjects;
	private final ConcurrentHashMap<Integer, L1Object>[] _visibleObjects;
	private final CopyOnWriteArrayList<L1War> _allWars;
	private final ConcurrentHashMap<Integer, L1ItemInstance> _allitem;
	private final ConcurrentHashMap<String, L1Clan> _allClans;
	private final ConcurrentHashMap<Integer, L1Clan> _allClansById;
	private final ConcurrentHashMap<Integer, L1DoorInstance> _allDoor;
	private final ConcurrentHashMap<Integer, L1NpcInstance> _allNpc;
	private final ConcurrentHashMap<Integer, L1NpcInstance> _allNpcObject;
	private final ConcurrentHashMap<Integer, L1NpcInstance> _allNpcChat;
	private final ConcurrentHashMap<Integer, L1FakePcInstance> _allFakePc;
	private final ConcurrentHashMap<Integer, L1NpcShopInstance> _allShopNpc;
	private final ConcurrentHashMap<Integer, L1TeleporterInstance> _allTeleporter;
	private final ConcurrentHashMap<Integer, L1GuardInstance> _allGuard;
	private final ConcurrentHashMap<Integer, L1CastleGuardInstance> _allCastleGuard;
	private final ConcurrentHashMap<Integer, L1EffectInstance> _allEffect;
	private final ConcurrentHashMap<Integer, L1MerchantInstance> _allMerchant;
	private final ConcurrentHashMap<Integer, L1TowerInstance> _allTower;
	
	private final L1MapCluster[] _visibleObjectCluster;
	//private int _weather = 4;
	private int _weather = 0;
	private boolean _worldChatEnabled = true;
	private static final int MAX_MAP_ID = 32768;
	private static L1World _instance;
	
	@SuppressWarnings("unchecked")
	private L1World() {
		_allAiUsers				= new ConcurrentHashMap<String, L1AiUserInstance>(); // 모든 aiUser
		_allPlayers				= new ConcurrentHashMap<String, L1PcInstance>(); // 모든 플레이어
		_allPlayers_from_id		= new ConcurrentHashMap<Integer, L1PcInstance>(); // 모든 플레이어
		_allGms					= new ConcurrentHashMap<String, L1PcInstance>(); // 모든 관리자
		_allPets				= new ConcurrentHashMap<Integer, L1PetInstance>(); // 모든 애완동물
		_allSummons				= new ConcurrentHashMap<Integer, L1SummonInstance>(); // 모든 사몬몬스타
		_allObjects				= new ConcurrentHashMap<Integer, L1Object>(); // 모든 오브젝트(L1ItemInstance 들어가, L1Inventory는 없음)
		_visibleObjects			= new ConcurrentHashMap[MAX_MAP_ID + 1]; // MAP 마다의 오브젝트(L1Inventory 들어가, L1ItemInstance는 없음)
		_visibleObjectCluster	= new L1MapCluster[MAX_MAP_ID + 1]; 
		_allWars				= new CopyOnWriteArrayList<L1War>(); // 모든 전쟁
		_allClansById			= new ConcurrentHashMap<Integer, L1Clan>(); // 모든 크란(Online/Offline 어느쪽이나)
		_allClans				= new ConcurrentHashMap<String, L1Clan>(); // 모든 크란(Online/Offline 어느쪽이나)
		_allDoor				= new ConcurrentHashMap<Integer, L1DoorInstance>(); // 모든 door
		_allNpc					= new ConcurrentHashMap<Integer, L1NpcInstance>(); // 모든 npc
		_allNpcObject			= new ConcurrentHashMap<Integer, L1NpcInstance>(); // 모든 npc obj
		_allNpcChat				= new ConcurrentHashMap<Integer, L1NpcInstance>(); // 모든 npc chat
		_allFakePc				= new ConcurrentHashMap<Integer, L1FakePcInstance>(); 
		_allShopNpc				= new ConcurrentHashMap<Integer, L1NpcShopInstance>(); // 모든 무인NPC상점
		_allitem				= new ConcurrentHashMap<Integer, L1ItemInstance>();
		_allTeleporter			= new ConcurrentHashMap<Integer, L1TeleporterInstance>(); // 모든 텔레포터
		_allGuard				= new ConcurrentHashMap<Integer, L1GuardInstance>(); // 모든 경비병
		_allCastleGuard			= new ConcurrentHashMap<Integer, L1CastleGuardInstance>();// 모든 성 경비병
		_allEffect				= new ConcurrentHashMap<Integer, L1EffectInstance>();// 모든 이팩트
		_allMerchant			= new ConcurrentHashMap<Integer, L1MerchantInstance>();
		_allTower				= new ConcurrentHashMap<Integer, L1TowerInstance>();// 모든 타워

		for (int i = 0; i <= MAX_MAP_ID; i++) {
			_visibleObjects[i] = new ConcurrentHashMap<Integer, L1Object>();
		}
		for (int i = 0; i <= MAX_MAP_ID; ++i) {
			_visibleObjectCluster[i] = null;
		}

		for (Map.Entry<Integer, MapData> mapData : MapsTable.getInstance().getMaps().entrySet()) {
			int mapId		= mapData.getKey();
			MapData data	= mapData.getValue();
			_visibleObjectCluster[mapId] = new L1MapCluster(data.getStartX(), data.getEndX(), data.getStartY(), data.getEndY());
		}
		
		for (int i = 0; i <= MAX_MAP_ID; ++i) {
			if (_visibleObjectCluster[i] == null) {
				_visibleObjectCluster[i] = new L1MapCluster(0, 20, 0, 20);	// 1개짜리 클러스터 만들어준다.
			}
		}
	}

	public static L1World getInstance() {
		if (_instance == null) {
			_instance = new L1World();
		}
		return _instance;
	}
	
	public void ClusterAdd(int mapid, L1V1Map map){
		_visibleObjectCluster[mapid] = new L1MapCluster(map.getX(), map.getX() + map.getWidth() - 1, map.getY(),  map.getY() + map.getHeight() - 1);
	}

	/**
	 * 모든 상태를 클리어 한다.<br>
	 * 디버그, 테스트등이 특수한 목적 이외로 호출해서는 안 된다.
	 */
	public void clear() {
		_instance = new L1World();
	}
	
	/**
	 * 월드 상에 오브젝트가 생성되면 호출
	 * @param object
	 */
	public void storeObject(L1Object object) {
		if (object == null) {
			throw new NullPointerException();
		}
		_allObjects.put(object.getId(), object);
		if (object instanceof L1PcInstance){
			L1PcInstance pc = (L1PcInstance) object;
			_allPlayers.put(pc.getName(), pc);
			_allPlayers_from_id.put(pc.getId(), pc);
			if (pc.isGm()) {
				_allGms.put(pc.getName(), pc);
			}
			if (object instanceof L1AiUserInstance) {
				_allAiUsers.put(pc.getName(), (L1AiUserInstance) object);
			}
		}
		if (object instanceof L1PetInstance) {
			_allPets.put(object.getId(), (L1PetInstance) object);
		}
		if (object instanceof L1SummonInstance) {
			_allSummons.put(object.getId(), (L1SummonInstance) object);
		}
		if (object instanceof L1DoorInstance) {
			_allDoor.put(object.getId(), (L1DoorInstance) object);
		}
		if (object instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) object;
			_allNpc.put(npc.getNpcTemplate().getNpcId(), npc);
			_allNpcObject.put(object.getId(), npc);
			if (npc.getNpcTemplate().isChat()) {
				_allNpcChat.put(object.getId(), npc);
			}
		}
		if (object instanceof L1NpcShopInstance) {
			_allShopNpc.put(object.getId(), (L1NpcShopInstance) object);
		}
		if (object instanceof L1FakePcInstance) {
			_allFakePc.put(object.getId(), (L1FakePcInstance) object);
		}
		if (object instanceof L1TeleporterInstance) {
			_allTeleporter.put(object.getId(), (L1TeleporterInstance) object);
		}
		if (object instanceof L1EffectInstance) {
			_allEffect.put(object.getId(), (L1EffectInstance) object);
		}
		if (object instanceof L1MerchantInstance) {
			_allMerchant.put(object.getId(), (L1MerchantInstance) object);
		}
		if (object instanceof L1TowerInstance) {
			_allTower.put(object.getId(), (L1TowerInstance) object);
		}
		if (object instanceof L1GuardInstance) {
			_allGuard.put(object.getId(), (L1GuardInstance) object);
		}
		if (object instanceof L1CastleGuardInstance) {
			_allCastleGuard.put(object.getId(), (L1CastleGuardInstance) object);
		}
		if (object instanceof L1ItemInstance) {
			_allitem.put(object.getId(), (L1ItemInstance) object);
		}
	}

	/**
	 * 월드 상에 오브젝트가 삭제되면 호출
	 * @param object
	 */
	public void removeObject(L1Object object) {
		if (object == null) {
			throw new NullPointerException();
		}
		_allObjects.remove(object.getId());
		if (object instanceof L1PcInstance){
			L1PcInstance pc = (L1PcInstance) object;
			_allPlayers.remove(pc.getName());
			_allPlayers_from_id.remove(pc.getId());
			if (pc.isGm()) {
				_allGms.remove(pc.getName());
			}
			if (object instanceof L1AiUserInstance) {
				_allAiUsers.remove(pc.getName());
			}
		}
		if (object instanceof L1PetInstance) {
			_allPets.remove(object.getId());
		}
		if (object instanceof L1SummonInstance) {
			_allSummons.remove(object.getId());
		}
		if (object instanceof L1DoorInstance) {
			_allDoor.remove(object.getId());
		}
		if (object instanceof L1NpcInstance){
			L1NpcInstance npc = (L1NpcInstance) object;
			_allNpc.remove(npc.getNpcTemplate().getNpcId());
			_allNpcObject.remove(object.getId());
			if (npc.getNpcTemplate().isChat()) {
				_allNpcChat.remove(object.getId());
			}
		}
		if (object instanceof L1NpcShopInstance) {
			_allShopNpc.remove(object.getId());
		}
		if (object instanceof L1FakePcInstance) {
			_allFakePc.remove(object.getId());
		}
		if (object instanceof L1TeleporterInstance) {
			_allTeleporter.remove(object.getId());
		}
		if (object instanceof L1EffectInstance) {
			_allEffect.remove(object.getId());
		}
		if (object instanceof L1MerchantInstance) {
			_allMerchant.remove(object.getId());
		}
		if (object instanceof L1TowerInstance) {
			_allTower.remove(object.getId());
		}
		if (object instanceof L1GuardInstance) {
			_allGuard.remove(object.getId());
		}
		if (object instanceof L1CastleGuardInstance) {
			_allCastleGuard.remove(object.getId());
		}
		if (object instanceof L1ItemInstance) {
			_allitem.remove(object.getId());
		}
	}

	public L1Object findObject(int obj_id) {
		return _allObjects.get(obj_id);
	}

	// _allObjects의 뷰
	private Collection<L1Object> _allValues;

	public Collection<L1Object> getObject() {
		Collection<L1Object> vs = _allValues;
		return (vs != null) ? vs : (_allValues = Collections.unmodifiableCollection(_allObjects.values()));
	}
	
	public L1Object[] getObjectArray() {
		return _allObjects.values().toArray(new L1Object[_allObjects.size()]);
	}

	public L1GroundInventory getInventory(int x, int y, short map) {
		int inventoryKey = ((x - 30000) * 10000 + (y - 30000)) * -1; // xy의 마이너스치를 인벤트리 키로서 사용
		Object object = _visibleObjects[map].get(inventoryKey);
		return object == null ? new L1GroundInventory(inventoryKey, x, y, map) : (L1GroundInventory) object;
	}

	public L1GroundInventory getInventory(L1Location loc) {
		return getInventory(loc.getX(), loc.getY(), (short) loc.getMap().getId());
	}

	public void addVisibleObject(L1Object object) {
		if (object.getMapId() <= MAX_MAP_ID) {
			_visibleObjects[object.getMapId()].put(object.getId(), object);
			_visibleObjectCluster[object.getMapId()].setObject(object);
			if (!(object instanceof L1Inventory) && !(object instanceof L1DollInstance) && !(object instanceof L1DoorInstance)
					&& object.getMap().isPassable(object.getLocation())) {
				object.getMap().setPassable(object.getLocation(), false);
			}
		}
	}

	public void removeVisibleObject(L1Object object) {
		if (object.getMapId() <= MAX_MAP_ID) {
			_visibleObjects[object.getMapId()].remove(object.getId());
			_visibleObjectCluster[object.getMapId()].removeObject(object);
			if (!(object instanceof L1Inventory) && !(object instanceof L1DollInstance) && !(object instanceof L1DoorInstance)) {
				if ((object instanceof L1MonsterInstance || object instanceof L1SummonInstance || object instanceof L1PetInstance) 
						&& (((L1NpcInstance) object).getCurrentHp() < 1 || ((L1NpcInstance) object).isDead())) {
					return;
				}
				if (object instanceof L1PcInstance && (((L1PcInstance) object).getCurrentHp() < 1 || ((L1PcInstance) object).isDead())) {
					return;
				}
				if (!object.getMap().isPassable(object.getLocation())) {
					object.getMap().setPassable(object.getLocation(), true);
				}
			}
		}
	}

	public void moveVisibleObject(L1Object object, int newMap, int x, int y){ // set_Map로 새로운 Map로 하기 전에 부르는 것
		if (object == null) {
			return;
		}
		int map_id = object.getMapId();
		int id = object.getId();
		if (map_id != newMap) {
			if (map_id <= MAX_MAP_ID) {
				_visibleObjectCluster[map_id].removeObject(object);
				_visibleObjects[map_id].remove(id);
			}
			if (newMap <= MAX_MAP_ID) {
				_visibleObjectCluster[newMap].setObject(object, x, y);
				_visibleObjects[newMap].put(id, object);
			}
		} else {
			if (map_id <= MAX_MAP_ID) {
				_visibleObjectCluster[map_id].onMove(object, x, y);
			}
		}
	}

	private ConcurrentHashMap<Integer, Integer> createLineMap(Point src, Point target) {
		ConcurrentHashMap<Integer, Integer> lineMap = new ConcurrentHashMap<Integer, Integer>();

		/*
		 * http://www2.starcat.ne.jp/~fussy/algo/algo1-1.htm보다
		 */
		int E;
		int x;
		int y;
		int key;
		int i;
		int x0 = src.getX();
		int y0 = src.getY();
		int x1 = target.getX();
		int y1 = target.getY();
		int sx = (x1 > x0) ? 1 : -1;
		int dx = (x1 > x0) ? x1 - x0 : x0 - x1;
		int sy = (y1 > y0) ? 1 : -1;
		int dy = (y1 > y0) ? y1 - y0 : y0 - y1;

		x = x0;
		y = y0;
		/* 기울기가 1 이하의 경우 */
		if (dx >= dy) {
			E = -dx;
			for (i = 0; i <= dx; i++) {
				key = (x << 16) + y;
				lineMap.put(key, key);
				x += sx;
				E += dy << 1;
				if (E >= 0) {
					y += sy;
					E -= dx << 1;
				}
			}
			/* 기울기가 1보다 큰 경우 */
		} else {
			E = -dy;
			for (i = 0; i <= dy; i++) {
				key = (x << 16) + y;
				lineMap.put(key, key);
				y += sy;
				E += dx << 1;
				if (E >= 0) {
					x += sx;
					E -= dy << 1;
				}
			}
		}
		return lineMap;
	}

	public ArrayList<L1Object> getVisibleLineObjects(L1Object src, L1Object target) {
		ConcurrentHashMap<Integer, Integer> lineMap = createLineMap(src.getLocation(), target.getLocation());
		int map = target.getMapId();
		ArrayList<L1Object> result = new ArrayList<L1Object>();
		if (map <= MAX_MAP_ID) {
			for (L1Object element : _visibleObjects[map].values()) {
				if (element == null || element.equals(src)) {
					continue;
				}
				int key = (element.getX() << 16) + element.getY();
				if (lineMap.containsKey(key)) {
					result.add(element);
				}
			}
		}
		return result;
	}
	
	public ArrayList<L1Object> getVisibleLineHeading(L1PcInstance pc, int heading, int range) {
		int x = pc.getX(), y = pc.getY();
		switch(heading){
		case 0:y-=range;break;
		case 1:x+=range;y-=range;break;
		case 2:x+=range;break;
		case 3:x+=range;y+=range;break;
		case 4:y+=range;break;
		case 5:x-=range;y+=range;break;
		case 6:x-=range;break;
		case 7:x-=range;y-=range;break;
		}
		L1Location loc = new L1Location(x, y, pc.getMapId());
		ConcurrentHashMap<Integer, Integer> lineMap = createLineMap(pc.getLocation(), loc);
		int map = loc.getMapId();
		ArrayList<L1Object> result = new ArrayList<L1Object>();
		if (map <= MAX_MAP_ID) {
			for (L1Object element : _visibleObjects[map].values()) {
				if (element == null || element.equals(pc)) {
					continue;
				}
				int key = (element.getX() << 16) + element.getY();
				if (lineMap.containsKey(key)) {
					result.add(element);
				}
			}
		}
		return result;
	}
	
	/**
	 * 지정 범위내 파티 멤버 리스트(자신포함)
	 * @param pc
	 * @param radius
	 * @return ArrayList<L1PcInstance>
	 */
	public ArrayList<L1PcInstance> getVisiblePartyPlayer(L1PcInstance pc, int radius) {
		if (pc.getParty() == null) {
			return null;
		}
		int map		= pc.getMapId();
		Point pt	= pc.getLocation();
		ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();
		for (L1PcInstance element : pc.getParty().getMembersArray()) {
			if (element == null || map != element.getMapId()) {
				continue;
			}
			if (radius == -1) {
				if (pt.isInScreen(element.getLocation())) {
					result.add(element);
				}
			} else if (radius == 0){
				if (pt.isSamePoint(element.getLocation())) {
					result.add(element);
				}
			} else {
				if (pt.getTileLineDistance(element.getLocation()) <= radius) {
					result.add(element);
				}
			}
		}
		return result;
	}
	
	/**
	 * 지정 범위내 혈맹원 리스트(자신포함)
	 * @param pc
	 * @param radius
	 * @return ArrayList<L1PcInstance>
	 */
	public ArrayList<L1PcInstance> getVisibleClanPlayer(L1PcInstance pc, int radius) {
		int map		= pc.getMapId();
		Point pt	= pc.getLocation();
		ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();
		if (pc.getClan() != null)
			for (L1PcInstance element : pc.getClan().getOnlineClanMember()) {
				if (element == null || map != element.getMapId()) {
					continue;
				}
				if (radius == -1) {
					if (pt.isInScreen(element.getLocation())) {
						result.add(element);
					}
				} else if (radius == 0) {
					if (pt.isSamePoint(element.getLocation())) {
						result.add(element);
					}
				} else {
					if (pt.getTileLineDistance(element.getLocation()) <= radius) {
						result.add(element);
					}
				}
			}
		return result;
	}
	
	public ArrayList<L1PcInstance> getVisibleBoxPlayer(L1Object object, int heading, int width, int height) {
		int x	= object.getX();
		int y	= object.getY();
		int map	= object.getMapId();
		L1Location location = object.getLocation();
		ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();
		int headingRotate[] = { 6, 7, 0, 1, 2, 3, 4, 5 };
		double cosSita = Math.cos(headingRotate[heading] * Math.PI / 4);
		double sinSita = Math.sin(headingRotate[heading] * Math.PI / 4);
		if (map <= MAX_MAP_ID) {
			for (L1Object element : _visibleObjects[map].values()) {
				if (element == null || element.equals(object)) {
					continue;
				}
				if (!(element instanceof L1PcInstance)) {
					continue;
				}
				if (map != element.getMapId()) {
					continue;
				}
				if (location.isSamePoint(element.getLocation())) {
					result.add((L1PcInstance)element);
					continue;
				}
				int distance = location.getTileLineDistance(element.getLocation());
				// 직선 거리가 높이, 폭어느 쪽보다 큰 경우, 계산할 것도 없이 범위외
				if (distance > height && distance > width) {
					continue;
				}

				// object의 위치를 원점과하기 위한 좌표 보정
				int x1 = element.getX() - x;
				int y1 = element.getY() - y;

				// Z축회전시키고 각도를 0번으로 한다.
				int rotX = (int) Math.round(x1 * cosSita + y1 * sinSita);
				int rotY = (int) Math.round(-x1 * sinSita + y1 * cosSita);

				int xmin = 0;
				int xmax = height;
				int ymin = -width;
				int ymax = width;

				// 깊이가 사정과 맞물리지 않기 때문에 직선 거리로 판정하도록(듯이) 변경.
				// if (rotX > xmin && rotX <= xmax && rotY >= ymin && rotY <=
				// ymax) {
				if (rotX > xmin && distance <= xmax && rotY >= ymin && rotY <= ymax) {
					result.add((L1PcInstance)element);
				}
			}
		}
		return result;
	}

	public ArrayList<L1Object> getVisibleBoxObjects(L1Object object, int heading, int width, int height) {
		int x	= object.getX();
		int y	= object.getY();
		int map	= object.getMapId();
		L1Location location = object.getLocation();
		ArrayList<L1Object> result = new ArrayList<L1Object>();
		int headingRotate[] = { 6, 7, 0, 1, 2, 3, 4, 5 };
		double cosSita = Math.cos(headingRotate[heading] * Math.PI / 4);
		double sinSita = Math.sin(headingRotate[heading] * Math.PI / 4);
		if (map <= MAX_MAP_ID) {
			for (L1Object element : _visibleObjects[map].values()) {
				if (element == null || element.equals(object)) {
					continue;
				}
				if (map != element.getMapId()) {
					continue;
				}
				if (location.isSamePoint(element.getLocation())) {
					result.add(element);
					continue;
				}
				int distance = location.getTileLineDistance(element.getLocation());
				// 직선 거리가 높이, 폭어느 쪽보다 큰 경우, 계산할 것도 없이 범위외
				if (distance > height && distance > width) {
					continue;
				}

				// object의 위치를 원점과하기 위한 좌표 보정
				int x1 = element.getX() - x;
				int y1 = element.getY() - y;

				// Z축회전시키고 각도를 0번으로 한다.
				int rotX = (int) Math.round(x1 * cosSita + y1 * sinSita);
				int rotY = (int) Math.round(-x1 * sinSita + y1 * cosSita);

				int xmin = 0;
				int xmax = height;
				int ymin = -width;
				int ymax = width;

				// 깊이가 사정과 맞물리지 않기 때문에 직선 거리로 판정하도록(듯이) 변경.
				// if (rotX > xmin && rotX <= xmax && rotY >= ymin && rotY <=
				// ymax) {
				if (rotX > xmin && distance <= xmax && rotY >= ymin && rotY <= ymax) {
					result.add(element);
				}
			}
		}
		return result;
	}

	public ArrayList<L1Object> getVisibleObjects(L1Object object) {
		return getVisibleObjects(object, -1);
	}

	public ArrayList<L1Object> getVisibleObjects(L1Object object, int radius) {
		if (object.getMapId() <= MAX_MAP_ID) {
			return _visibleObjectCluster[object.getMapId()].getVisibleObjects(object, radius);
		}
		return new ArrayList<L1Object>();
	}
	
	public void onMoveObject(L1Object object) {
		if (object.getMapId() <= MAX_MAP_ID) {
			_visibleObjectCluster[object.getMapId()].onMove(object);
		}
	}

	public ArrayList<L1Object> getVisiblePoint(L1Location loc, int radius) {
		if (loc.getMapId() <= MAX_MAP_ID) {
			return _visibleObjectCluster[loc.getMapId()].getVisiblePoint(loc, radius);
		}
		return new ArrayList<L1Object>();
	}

	public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object) {
		if (object.getMapId() <= MAX_MAP_ID) {
			return getVisiblePlayer(object, -1);
		}
		return new ArrayList<L1PcInstance>();
	}
	
	public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object, int radius) {
		if (object.getMapId() <= MAX_MAP_ID) {
			return _visibleObjectCluster[object.getMapId()].getVisiblePlayer(object, radius);
		}
		return new ArrayList<L1PcInstance>();
	}
	
	public ArrayList<L1PcInstance> getVisiblePlayerWithTarget(L1Object object, int radius) {
		if (object.getMapId() <= MAX_MAP_ID) {
			return _visibleObjectCluster[object.getMapId()].getVisiblePlayerWithTarget(object, radius);
		}
		return new ArrayList<L1PcInstance>();
	}
	
	/**
	 * 오브젝트간 사이에 잇는 유저 리스트
	 * @param object
	 * @param target
	 * @return ArrayList<L1PcInstance>
	 */
	public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(L1Object object, L1Object target) {
		if (object.getMapId() <= MAX_MAP_ID) {
			return _visibleObjectCluster[object.getMapId()].getVisiblePlayerExceptTargetSight(object, target);
		}
		return new ArrayList<L1PcInstance>();
	}

	/**
	 * object를 인식할 수 있는 범위에 있는 플레이어를 취득한다
	 * 
	 * @param object
	 * @return
	 */
	public ArrayList<L1PcInstance> getRecognizePlayer(L1Object object) {
		return getVisiblePlayer(object, Config.SERVER.PC_RECOGNIZE_RANGE);
	}
	public L1PcInstance[] getAllPlayers3() {
		return _allPlayers.values().toArray(new L1PcInstance[_allPlayers.size()]);
	}
	
	private Collection<L1PcInstance> _allPlayerValues;
	public Collection<L1PcInstance> getAllPlayers() {
		Collection<L1PcInstance> vs = _allPlayerValues;
		return (vs != null) ? vs : (_allPlayerValues = Collections.unmodifiableCollection(_allPlayers.values()));
	}
	
	private Collection<L1PcInstance> _allGmValues;
	public Collection<L1PcInstance> getAllGms() {
		Collection<L1PcInstance> vs = _allGmValues;
		return (vs != null) ? vs : (_allGmValues = Collections.unmodifiableCollection(_allGms.values()));
	}
	
	private Collection<L1AiUserInstance> _allAiUserValues;
	public Collection<L1AiUserInstance> getAllAiUsers() {
		Collection<L1AiUserInstance> vs = _allAiUserValues;
		return (vs != null) ? vs : (_allAiUserValues = Collections.unmodifiableCollection(_allAiUsers.values()));
	}
	
	public Collection<L1NpcShopInstance> getAllNpcShop() {
		return Collections.unmodifiableCollection(_allShopNpc.values());
	}
	
	public Collection<L1DoorInstance> getAllDoor() {
		return Collections.unmodifiableCollection(_allDoor.values());
	}
	
	public Collection<L1NpcInstance> getAllNpc() {
		return Collections.unmodifiableCollection(_allNpcObject.values());
	}
	
	public Collection<L1NpcInstance> getAllNpcChat() {
		return Collections.unmodifiableCollection(_allNpcChat.values());
	}
	
	public Collection<L1TeleporterInstance> getAllTeleporter() {
		return Collections.unmodifiableCollection(_allTeleporter.values());
	}

	public Collection<L1GuardInstance> getAllGuard() {
		return Collections.unmodifiableCollection(_allGuard.values());
	}

	public Collection<L1CastleGuardInstance> getAllCastleGuard() {
		return Collections.unmodifiableCollection(_allCastleGuard.values());
	}
	
	public Collection<L1FakePcInstance> getAllFakePc() {
		return Collections.unmodifiableCollection(_allFakePc.values());
	}
	
	public Collection<L1NpcInstance> getAllEffect() {
		return Collections.unmodifiableCollection(_allEffect.values());
	}
	
	public Collection<L1MerchantInstance> getAllMerchant() {
		return Collections.unmodifiableCollection(_allMerchant.values());
	}
	
	public Collection<L1TowerInstance> getAllTower() {
		return Collections.unmodifiableCollection(_allTower.values());
	}
	
	public int get_players_size(){
		return _allPlayers.size();
	}
	public int get_npc_size() {
		return _allNpcObject.size();
	}
	public int get_item_size(){
		return _allitem.size();
	}

	/**
	 * 월드내에 있는 지정된 이름의 플레이어를 취득한다.
	 * 
	 * @param name -
	 *            플레이어명(소문자·대문자는 무시된다)
	 * @return 지정된 이름의 L1PcInstance. 해당 플레이어가 존재하지 않는 경우는 null를 돌려준다.
	 */
	public L1PcInstance getPlayer(String name) {
		/*if (StringUtil.isNullOrEmpty(name)) {
			return null;
		}
		return _allPlayers.get(name.toUpperCase());*/
		Collection<L1PcInstance> pc = null;
		pc = getAllPlayers();
		for (L1PcInstance each : pc) {
			if (each == null)
				continue;
			if (each.getName().equalsIgnoreCase(name)) {
				return each;
			}
		}
		return null;				
	}
	
	public L1PcInstance getPlayer(int id) {
		return _allPlayers_from_id.get(id);
	}
	
	/**
	 * 월드내에 있는 지정된 이름의 AiUser를 취득한다.
	 * 
	 * @param name -
	 *            AiUser명(소문자·대문자는 무시된다)
	 * @return 지정된 이름의 L1AiUserInstance. 해당 플레이어가 존재하지 않는 경우는 null를 돌려준다.
	 */
	public L1AiUserInstance getAiUser(String name) {
		if (StringUtil.isNullOrEmpty(name)) {
			return null;
		}
		return _allAiUsers.get(name.toUpperCase());
	}
	
	/**
	 * 월드내에 있는 지정된 이름의 무인NPC상점을 취득한다.
	 * 
	 * @param name -
	 *            무인NPC상점명(소문자·대문자는 무시된다)
	 * @return 지정된 이름의 L1ShopNpcInstance. 해당 마네킹 존재하지 않는 경우는 null를 돌려준다.
	 */
	public L1NpcShopInstance getShopNpc(String name) {
		for (L1NpcShopInstance each : getAllShopNpc()) {
			if (each == null) {
				continue;
			}
			if (each.getName().equalsIgnoreCase(name)) {
				return each;
			}
		}
		return null;
	}
	
	// _allShopNpc의 뷰
	private Collection<L1NpcShopInstance> _allShopNpcValues;
	public Collection<L1NpcShopInstance> getAllShopNpc() {
		Collection<L1NpcShopInstance> vs = _allShopNpcValues;
		return (vs != null) ? vs : (_allShopNpcValues = Collections.unmodifiableCollection(_allShopNpc.values()));
	}

	// _allPets의 뷰
	private Collection<L1PetInstance> _allPetValues;
	public Collection<L1PetInstance> getAllPets() {
		Collection<L1PetInstance> vs = _allPetValues;
		return (vs != null) ? vs :  (_allPetValues = Collections.unmodifiableCollection(_allPets.values()));
	}

	// _allSummons의 뷰
	private Collection<L1SummonInstance> _allSummonValues;
	public Collection<L1SummonInstance> getAllSummons() {
		Collection<L1SummonInstance> vs = _allSummonValues;
		return (vs != null) ? vs : (_allSummonValues = Collections.unmodifiableCollection(_allSummons.values()));
	}

	public final Map<Integer, L1Object> getVisibleObjects(int mapId) {
		return _visibleObjects[mapId];
	}
	
	/**
	 * map내 존재하는 pc들
	 * @param mapId
	 * @return list
	 */
	public final ArrayList<L1PcInstance> getMapPlayer(int mapId){
		ArrayList<L1PcInstance> list = new ArrayList<L1PcInstance>();
		for (L1Object obj : _visibleObjects[mapId].values()) {
			if (obj == null) {
				continue;
			}
			if (obj instanceof L1PcInstance) {
				list.add((L1PcInstance)obj);
			}
		}
		return list;
	}

	public void addWar(L1War war) {
		if (!_allWars.contains(war)) {
			_allWars.add(war);
		}
	}

	public void removeWar(L1War war) {
		if (_allWars.contains(war)) {
			_allWars.remove(war);
		}
	}
	
	// 추가
	public L1War[] get_wars() {
		return _allWars.toArray(new L1War[_allWars.size()]);
	}

	// _allWars의 뷰
	private List<L1War> _allWarList;
	public List<L1War> getWarList() {
		List<L1War> vs = _allWarList;
		return (vs != null) ? vs : (_allWarList = Collections.unmodifiableList(_allWars));
	}

	public void storeClan(L1Clan clan) {
		L1Clan temp = getClan(clan.getClanId());
		if (temp == null) {
			_allClansById.put(clan.getClanId(), clan);
			_allClans.put(clan.getClanName(), clan);
		}
	}

	public void removeClan(L1Clan clan) {
		L1Clan temp = getClan(clan.getClanId());
		if (temp != null) {
			_allClansById.remove(clan.getClanId());
			_allClans.remove(clan.getClanName());
		}
	}

	public L1Clan getClan(String clan_name) {
		return _allClans.get(clan_name);
	}
	
	public L1Clan getClan(int clanId) {
		return _allClansById.get(clanId);
	}

	// _allClans의 뷰
	private Collection<L1Clan> _allClanValues;
	public Collection<L1Clan> getAllClans() {
		Collection<L1Clan> vs = _allClanValues;
		return (vs != null) ? vs : (_allClanValues = Collections.unmodifiableCollection(_allClans.values()));
	}

	public void setWeather(int weather) {
		_weather = weather;
	}

	public int getWeather() {
		return _weather;
	}

	public void setWorldChatElabled(boolean flag) {
		_worldChatEnabled = flag;
	}

	public boolean isWorldChatElabled() {
		return _worldChatEnabled;
	}

	/**
	 * 월드상에 존재하는 모든 플레이어에 패킷을 송신한다.
	 * 
	 * @param packet
	 *            송신하는 패킷을 나타내는 ServerBasePacket 오브젝트.
	 */
	public void broadcastPacketToAll(ServerBasePacket packet) {
		_log.finest("players to notify : " + getAllPlayers().size());
		for (L1PcInstance pc : getAllPlayers()) {
			pc.sendPackets(packet);
		}
	}

	public void broadcastPacketToAll(ServerBasePacket packet, boolean clear) {
		try {
			Collection<L1PcInstance> pclist = null;
			pclist = getAllPlayers();
			_log.finest("players to notify : " + pclist.size());
			for (L1PcInstance pc : pclist) {
				if (pc != null) {
					pc.sendPackets(packet);
				}
			}
			if (clear) {
				packet.clear();
				packet = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 자신을 제외한 모든 유저한테 송신한다.
	public void broadcastPacket(L1PcInstance pc, ServerBasePacket packet, boolean clear) {
		try {
			Collection<L1PcInstance> pclist = null;
			pclist = getAllPlayers();
			_log.finest("players to notify : " + pclist.size());
			for (L1PcInstance _pc : pclist) {
				if (_pc.getId() == pc.getId()) {
					continue;
				}
				if (_pc != null) {
					_pc.sendPackets(packet);
				}
			}
			if (clear) {
				packet.clear();
				packet = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 월드상에 존재하는 모든 플레이어에 서버 메세지를 송신한다.
	 * 
	 * @param message
	 *            송신하는 메세지
	 */
	public void broadcastServerMessage(String message) {
		broadcastPacketToAll(new S_SystemMessage(message));
	}
	
	public void broadcastServerMessage(String message, boolean clear) {
		broadcastPacketToAll(new S_SystemMessage(message), clear);
	}
	
	public L1NpcInstance findNpc(int id) {
		return _allNpc.get(id);
	}
	 
	private Collection<L1ItemInstance> _allItemValues;
	public Collection<L1ItemInstance> getAllItem() {
		Collection<L1ItemInstance> vs = _allItemValues;
		return (vs != null) ? vs : (_allItemValues = Collections.unmodifiableCollection(_allitem.values()));
	}
	
}

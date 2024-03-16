package l1j.server.IndunSystem.training;

import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;

public class TrainingCreator {
	public static final int MAX_ROOM_COUNT	= 100;
	private static final int DEFAULT_MAP_ID	= 1400;
	private static final long MAX_TIME		= 7200000L;// 2시간
	private static Map<Integer, Training> _list = new ConcurrentHashMap<Integer, Training>();

	private static TrainingCreator _instance;
	public static TrainingCreator getInstance() {
		if (_instance == null) {
			_instance = new TrainingCreator();
		}
		return _instance;
	}

	public void create(L1PcInstance pc, int map_id) {
		if (map_id != DEFAULT_MAP_ID) {
			L1WorldMap.getInstance().cloneMap(DEFAULT_MAP_ID, map_id);
		}
		Training room = new Training(map_id);
		new Timer().schedule(room, MAX_TIME);
		fillSpawn(map_id);
		_list.put(map_id, room);
	}

	protected Training removeRoom(int map_id) {
		return _list.remove(map_id);
	}

	public int blankMapId() {
		if (_list.size() == 0) {
			return DEFAULT_MAP_ID;
		}
		MapData data = MapsTable.getInstance().getMap(DEFAULT_MAP_ID);
		for (int i=data.getCloneStart(); i<=data.getCloneEnd(); i++) {
			if (!_list.containsKey(i)) {
				return i;
			}
		}
		return 0;
	}

	private void fillSpawn(int mapid) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(7000080);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setX(32902);
			npc.setY(32818);
			npc.setMap((short) mapid);
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(5);
			npc.setLightSize(0);
			npc.getLight().turnOnOffLight();
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int countRoom() {
		return _list.size();
	}
}

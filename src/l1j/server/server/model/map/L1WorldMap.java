package l1j.server.server.model.map;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.MapReader;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.L1World;

public class L1WorldMap {
	private static Logger _log = Logger.getLogger(L1WorldMap.class.getName());

	private static L1WorldMap _instance;
	private Map<Integer, L1Map> _maps;

	public static L1WorldMap getInstance() {
		if (_instance == null) {
			_instance = new L1WorldMap();
		}
		return _instance;
	}

	private L1WorldMap() {
		MapReader in = MapReader.getDefaultReader();
		try {
			_maps = in.read();
			if (_maps == null)
				//throw new RuntimeException("MAP의 read에 실패");
				throw new RuntimeException("Failed to read MAP");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			System.exit(0);
		}
	}

	public L1Map getMap(short mapId) {
		L1Map map = _maps.get((int) mapId);
		if (map == null)
			map = L1Map.newNull(); 
		return map;
	}
	
	public boolean getMapCK(short mapId) {
		L1Map map = _maps.get((int) mapId);
		return map != null;
	}
	
	public L1Map cloneMap(int targetId, int newId){
		L1Map copymap = null;
		copymap = _maps.get(targetId).copyMap(newId);
		_maps.put(newId, copymap);
		L1World.getInstance().ClusterAdd(newId, (L1V1Map) copymap);
		MapsTable.getInstance().CloneInsert(targetId, newId);
		return copymap;
	}
	
	public synchronized void addMap(L1Map map) {
		_maps.put(map.getId(), map);
	}

	public synchronized void removeMap(int mapId) {
		_maps.remove(mapId);
	}
}


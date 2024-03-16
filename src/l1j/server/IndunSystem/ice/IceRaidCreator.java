package l1j.server.IndunSystem.ice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;

public class IceRaidCreator {
	private final Map<Integer, IceRaid> _list = new ConcurrentHashMap<Integer, IceRaid>();
	private static IceRaidCreator _instance;
	public static IceRaidCreator getInstance() {
		if (_instance == null) {
			_instance = new IceRaidCreator();
		}
		return _instance;
	}

	private IceRaidCreator(){
	}

	public boolean create(L1PcInstance pc, IceRaidType type){
		if (countIceRaid(type) >= 49) {
			pc.sendPackets(L1SystemMessage.INSTANCE_DUNGEON_MAX);
			return false;
		}
		int mapId = getRaidMapId(type);
		if (mapId == 0) {
			pc.sendPackets(L1SystemMessage.INSTANCE_DUNGEON_MAX);
			return false;
		}
		IceRaid ice = new IceRaid(mapId, type);
		pc.getTeleport().start(32728, 32819, (short) mapId, 5, true);
		_list.put(mapId, ice);
		ice.Start();
		return true;
	}
	
	private int getRaidMapId(IceRaidType type){
		int copyMapId = getCopyMapId(type);
		if (copyMapId <= 0) {
			return 0;
		}
		switch(type){
		case NORMAL:
			if (copyMapId != 3000) {
				L1WorldMap.getInstance().cloneMap(3000, copyMapId);
			}
			return copyMapId;
		case HARD:
			if (copyMapId != 3050) {
				L1WorldMap.getInstance().cloneMap(3050, copyMapId);
			}
			return copyMapId;
		default:
			return 0;
		}
	}
	
	private int getCopyMapId(IceRaidType type){
		MapData data = null;
		switch(type){
		case NORMAL:data = MapsTable.getInstance().getMap(3000);break;
		case HARD:	data = MapsTable.getInstance().getMap(3050);break;
		}
		if (data == null) {
			return 0;
		}
		IceRaid handler = null;
		for (int i = data.getCloneStart(); i < data.getCloneEnd(); i++) {
			handler = _list.get(i);
			if (handler == null) {
				return i;
			}
		}  
		return 0;
	}

	public IceRaid getIceRaid(int mapId){
		return _list.get(mapId);
	}

	public void removeIceRaid(int mapId){
		_list.remove(mapId);
	}
	
	// 타입별 가동중인 레이드 수
	public int countIceRaid(IceRaidType raidType){
		int cnt = 0;
		for (IceRaid ice : _list.values()) {
			if (ice._type == raidType) {
				cnt++;
			}
		}
		return cnt;
	}
}


package l1j.server.IndunSystem.fantasyisland;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.IndunSystem.fantasyisland.action.Boost;
import l1j.server.IndunSystem.fantasyisland.action.Normal;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;

public class FantasyIslandCreator {
	private final Map<Integer, FantasylslandHandler> _list = new ConcurrentHashMap<Integer, FantasylslandHandler>();
	private static FantasyIslandCreator _instance;
	public static FantasyIslandCreator getInstance() {
		if (_instance == null) {
			_instance = new FantasyIslandCreator();
		}
		return _instance;
	}

	public void create(L1PcInstance pc, FantasylslandType type){
		int mapId = getRaidMapId(type);
		if (mapId == 0) {
			pc.sendPackets(L1SystemMessage.INSTANCE_DUNGEON_MAX);
			return;
		}
		FantasylslandHandler handler = getHandler(pc, (short)mapId, type);
		pc.getTeleport().start(32798, 32865, (short) mapId, pc.getMoveState().getHeading(), true);
		_list.put(mapId, handler);
		handler.raidStart();
	}
	
	private FantasylslandHandler getHandler(L1PcInstance pc, short mapId, FantasylslandType type){
		switch(type){
		case NORMAL:return new Normal(pc, mapId, type);
		case BOOST:	return new Boost(pc, mapId, type);
		default:return null;
		}
	}
	
	private int getRaidMapId(FantasylslandType type){
		int copyMapId = getCopyMapId(type);
		if (copyMapId <= 0) {
			return 0;
		}
		if (copyMapId != type._mpaId) {
			L1WorldMap.getInstance().cloneMap(type._mpaId, copyMapId);
		}
		return copyMapId;
	}
	
	private int getCopyMapId(FantasylslandType type){
		MapData data = MapsTable.getInstance().getMap(type._mpaId);
		if (data == null) {
			return 0;
		}
		FantasylslandHandler handler = null;
		for (int i = data.getCloneStart(); i <= data.getCloneEnd(); i++) {
			handler = _list.get(i);
			if (handler == null) {
				return i;
			}
		}  
		return 0;
	}

	// 레이드 취득
	public FantasylslandHandler getRaid(int mapId){
		return _list.get(mapId);
	}
		
	// 레이드 제거
	public void removeRaid(int mapId){
		_list.remove(mapId);
	}

	// 타입별 가동중인 레이드 수
	public int getRaidCount(FantasylslandType raidType){
		int cnt = 0;
		for (FantasylslandHandler handler : _list.values()) {
			if (handler._raidType == raidType) {
				cnt++;
			}
		}
		return cnt;
	}
}


package l1j.server.IndunSystem.clandungeon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.IndunSystem.clandungeon.action.Area;
import l1j.server.IndunSystem.clandungeon.action.Daily;
import l1j.server.IndunSystem.clandungeon.action.Weekly;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;

public class ClanDungeonCreator {
	private final Map<Integer, ClanDungeonHandler> _list = new ConcurrentHashMap<Integer, ClanDungeonHandler>();
	
	private static class newInstance {
		public static final ClanDungeonCreator INSTANCE = new ClanDungeonCreator();
	}
	public static ClanDungeonCreator getInstance() {
		return newInstance.INSTANCE;
	}
	
	private ClanDungeonCreator(){
	}
	
	/**
	 * 레이드를 시작한다.
	 * @param pc
	 * @param type
	 * @return
	 */
	public boolean create(L1PcInstance pc, ClanDungeonType type){
		int mapId = getRaidMapId(type);
		if (mapId == 0) {
			pc.sendPackets(L1SystemMessage.INSTANCE_DUNGEON_MAX);
			return false;
		}
		ClanDungeonHandler handler = getHandler(pc, (short)mapId, type);
		if (handler == null) {
			return false;
		}
		handler.startSetting(pc);
		_list.put(mapId, handler);
		return true;
	}
	
	/**
	 * 레이드 핸들러를 취득한다.
	 * @param pc
	 * @param mapId
	 * @param type
	 * @return ClanDungeonHandler
	 */
	ClanDungeonHandler getHandler(L1PcInstance pc, short mapId, ClanDungeonType type){
		switch(type){
		case DAILY:		return new Daily(mapId, pc.getClanName(), type);
		case WEEKLY:	return new Weekly(mapId, pc.getClanName(), type);
		case AREA:		return new Area(mapId, pc.getClanName(), type);
		default:return null;
		}
	}
	
	/**
	 * 레이드를 진행할 맵을 생성 후 반환한다.
	 * @param type
	 * @return mapId
	 */
	int getRaidMapId(ClanDungeonType type){
		int copyMapId = getCopyMapId(type);
		if (copyMapId <= 0) {
			return 0;
		}
		if (copyMapId != type.getMapId()) {
			L1WorldMap.getInstance().cloneMap(type.getMapId(), copyMapId);
		}
		return copyMapId;
	}
	
	/**
	 * 카피할 맵 번호를 취득한다.
	 * @param type
	 * @return mapId
	 */
	int getCopyMapId(ClanDungeonType type){
		MapData data = MapsTable.getInstance().getMap(type.getMapId());
		if (data == null) {
			return 0;
		}
		ClanDungeonHandler handler = null;
		for (int i = data.getCloneStart(); i <= data.getCloneEnd(); i++) {
			handler = _list.get(i);
			if (handler == null) {
				return i;
			}
		}  
		return 0;
	}
	
	/**
	 * 핸들러를  취득한다.
	 * @param mapId
	 * @return ClanDungeonHandler
	 */
	public ClanDungeonHandler getRaid(int mapId){
		return _list.get(mapId);
	}
		
	/**
	 * 핸들러를 제거한다
	 * @param mapId
	 */
	protected void removeRaid(int mapId){
		_list.remove(mapId);
	}

	/**
	 * 타입별 레이드 수를 취득한다.
	 * @param type
	 * @return count
	 */
	public int getRaidCount(ClanDungeonType type){
		int cnt = 0;
		for (ClanDungeonHandler handler : _list.values()) {
			if (handler._type == type) {
				cnt++;
			}
		}
		return cnt;
	}
}


package l1j.server.IndunSystem.indun;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.IndunSystem.indun.action.Aurakia;
import l1j.server.IndunSystem.indun.action.Crocodile;
import l1j.server.IndunSystem.indun.action.Fantasy;
import l1j.server.IndunSystem.indun.action.Orim;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.utils.CommonUtil;

public class IndunCreator {
	private final Map<Integer, IndunHandler> _list;
	
	private static final int[][][] START_LOC = {
		{ {32805, 32863}, {32800, 32868}, {32795, 32863}, {32800, 32858} },// orim
		{ {33384, 33194}, {33383, 33196}, {33385, 33194}, {33385, 33196} },// crocodile
		{ {32769, 32761}, {32763, 32764}, {32768, 32769}, {32771, 32765} },// fantasy
		{ {0, 0}, {0, 0}, {0, 0}, {0, 0} },// tangle space
		{ {32798, 32826}, {32799, 32824}, {32796, 32826}, {32799, 32828} }// aurakia
	};
	
	private static class newInstance {
		public static final IndunCreator INSTANCE = new IndunCreator();
	}
	public static IndunCreator getInstance() {
		return newInstance.INSTANCE;
	}
	
	private IndunCreator() {
		_list = new ConcurrentHashMap<Integer, IndunHandler>();
	}
	
	/**
	 * 인스턴스 던전을 시작한다.
	 * @param info
	 */
	public void create(IndunInfo info){
		int mapId = getIndunMapId(info.indunType);
		if (mapId == 0) {
			return;
		}
		IndunHandler handler = createHandler(info, (short)mapId);
		if (handler == null) {
			return;
		}
		IndunUtill.getInstance().mapObjectDelete(mapId);
		int[][] startLocArray = getStartLoc(info.indunType);
		for (L1PcInstance member : info.getMembers()) {
			member.getInventory().consumeItem(420092, 1);// 열쇠 제거
			member.getAccount().setIndunCount(member.getAccount().getIndunCount() + 1);// 이용횟수
			member.getAccount().updateIndunCount();
			int rnd = CommonUtil.random(startLocArray.length);
			member.getTeleport().start(startLocArray[rnd][0], startLocArray[rnd][1], (short) mapId, member.getMoveState().getHeading(), false);
		}
		_list.put(mapId, handler);
		handler.startIndun();
	}
	
	/**
	 * 담당할 핸들러를 생성한다.
	 * @param info
	 * @param mapId
	 * @return IndunHandler
	 */
	IndunHandler createHandler(IndunInfo info, short mapId){
		switch(info.indunType){
		case ORIM:			return new Orim(info, mapId);
		case CROCODILE:		return new Crocodile(info, mapId);
		case FANTASY:		return new Fantasy(info, mapId);
		case SPACE:			return null;
		case AURAKIA:		return new Aurakia(info, mapId);
		default:return null;
		}
	}
	
	/**
	 * 시작좌표를 반환한다.
	 * @param type
	 * @return loc
	 */
	int[][] getStartLoc(IndunType type){
		switch(type){
		case ORIM:			return START_LOC[0];
		case CROCODILE:		return START_LOC[1];
		case FANTASY:		return START_LOC[2];
		case SPACE:			return START_LOC[3];
		case AURAKIA:		return START_LOC[4];
		default:return null;
		}
	}
	
	/**
	 * 진행할 맵을 생성 후 맵번호를 반환한다.
	 * @param type
	 * @return mapId
	 */
	int getIndunMapId(IndunType type){
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
	 * 복사할 맵 번호를 취득한다.
	 * @param type
	 * @return mapId
	 */
	int getCopyMapId(IndunType type){
		MapData data = MapsTable.getInstance().getMap(type.getMapId());
		if (data == null) {
			return 0;
		}
		IndunHandler handler = null;
		for (int i = data.getCloneStart(); i <= data.getCloneEnd(); i++) {
			handler = _list.get(i);
			if (handler == null) {
				return i;
			}
		}  
		return 0;
	}
	
	/**
	 * 진행중인 핸들러를 취득한다.
	 * @param mapId
	 * @return IndunHandler
	 */
	public IndunHandler getIndun(int mapId){
		return _list.get(mapId);
	}

	/**
	 * 핸들러를 제거한다.
	 * @param mapId
	 * @param info
	 */
	public void removeIndun(int mapId, IndunInfo info){
		_list.remove(mapId);
	}
	
	/**
	 * 타입별 진행중인 핸들러 수를 반환한다.
	 * @param type
	 * @return count
	 */
	public int countIndun(IndunType type){
		int cnt = 0;
		for (IndunHandler handler : _list.values()) {
			if (handler._info.indunType == type) {
				cnt++;
			}
		}
		return cnt;
	}
}


package l1j.server.server.controller.action;

import java.util.ArrayList;
import java.util.logging.Logger;

import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1TimeMap;

/**
 * 시간에 따른 맵 가동 컨트롤러(사용안함)
 * @author LinOffice
 */
public class TimeMap implements ControllerInterface {
	private static Logger _log = Logger.getLogger(TimeMap.class.getName());
	private static class newInstance {
		public static final TimeMap INSTANCE = new TimeMap();
	}
	public static TimeMap getInstance() {
		return newInstance.INSTANCE;
	}
	
	private ArrayList<L1TimeMap> mapList;
	
	private TimeMap(){
		mapList = new ArrayList<L1TimeMap>();
	}

	@Override
	public void execute() {
		if (array() == null || array().length <= 0) {
			return;
		}
		try {
			for (L1TimeMap timeMap : array()) {
				if (timeMap.count()) {
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						if (timeMap.getId() != pc.getMapId()) {
							continue;
						}
						switch (pc.getMapId()){
						case 72:
						case 73:
						case 74:
							pc.getTeleport().start(34056, 32279, (short) 4, 5, true);
							break;
						case 460:
						case 461:
						case 462:
						case 463:
						case 464:
						case 465:
						case 466:
							pc.getTeleport().start(32664, 32855, (short) 457, 5, true);
							break;
						case 470:
						case 471:
						case 472:
						case 473:
						case 474:
							pc.getTeleport().start(32663, 32853, (short) 467, 5, true);
							break;
						case 475:
						case 476:
						case 477:
						case 478:
							pc.getTeleport().start(32660, 32876, (short) 468, 5, true);
							break;
						default:
							break;
						}
					}
					DoorSpawnTable.getInstance().getDoor(timeMap.getDoor()).close();
					remove(timeMap);
				}
			}
		} catch(Exception e){
			_log.warning(e.getMessage());
		}
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	/**
	 * 타임 이벤트가 있는 맵 등록
	 * 중복 등록이 되지 않도록 이미 등록된 맵 아이디와 비교 없다면 등록
	 * 사이즈가 0 이라면 즉 초기라면 비교대상이 없기때문에 무조건 등록
	 * @param	(TimeMap)	등록할 맵 객체
	*/
	public void add(L1TimeMap map){
		if (mapList.size() > 0) {
			boolean found = false;
			for (L1TimeMap m : array()) {
				if (m.getId() == map.getId()) {
					found = true;
					break;
				}
			}
			if (!found) {
				mapList.add(map);
			}
		} else {
			mapList.add(map);
		}
	}
	/**
	 * 타임 이벤트가 있는 맵 삭제
	 * 중복 삭제 또는 IndexOutOfBoundsException이 되지 않도록 이미 등록된 맵 아이디와 비교 있다면 삭제
	 * @param	(TimeMap)	삭제할 맵 객체
	*/
	private void remove(L1TimeMap map){
		for (L1TimeMap m : array()) {
			if (m.getId() == map.getId()) {
				mapList.remove(map);
				break;
			}
		}
		map = null;
	}
	/**
	 * 등록된 이벤트 맵 배열 리턴
	 * @return	(TimeMap[])	맵 객체 배열
	*/
	private L1TimeMap[] array(){
		return mapList.toArray(new L1TimeMap[mapList.size()]);
	}

}


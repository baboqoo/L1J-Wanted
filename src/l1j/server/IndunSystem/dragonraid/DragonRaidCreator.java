package l1j.server.IndunSystem.dragonraid;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.IndunSystem.dragonraid.action.Antaras;
import l1j.server.IndunSystem.dragonraid.action.Fafurion;
import l1j.server.IndunSystem.dragonraid.action.Halpas;
import l1j.server.IndunSystem.dragonraid.action.Rindvior;
import l1j.server.IndunSystem.dragonraid.action.Valakas;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;

/**
 * 드래곤 레이드 생성 클래스
 * @author LinOffice
 */
public class DragonRaidCreator {
	private static class newInstance {
		public static final DragonRaidCreator INSTANCE = new DragonRaidCreator();
	}
	public static DragonRaidCreator getInstance(){
		return newInstance.INSTANCE;
	}
	
	private final Map<Integer, DragonRaidHandler> _list;// 레이드 리스트
	
	private DragonRaidCreator(){
		_list = new ConcurrentHashMap<Integer, DragonRaidHandler>();
	}
	
	/**
	 * 레이드를 시작한다.
	 * @param pc
	 * @param raidType
	 * @return boolean
	 */
	public boolean create(L1PcInstance pc, DragonRaildType raidType){
		int mapId = getRaidMap(raidType);
		if (mapId == 0) {
			pc.sendPackets(L1SystemMessage.INSTANCE_DUNGEON_MAX);
			return false;
		}
		DragonRaidHandler raid = createHandler(mapId, raidType);
		if (raid == null) {
			System.out.println(String.format("[DragonRaidCreator] HANDLER_CREATE_FAIL : CHAR_NAME(%s), TYPE(%s)", pc.getName(), raidType.name()));
			return false;
		}
		raid.createSpwan(pc);// 엔피씨 스폰
		_list.put(mapId, raid);
		
		if (raidType != DragonRaildType.HALPAS && !raid.isRun()) {// 바로 시작하기
			raid.setRun(true);
			raid.raidStart();
		}
		return true;
	}
	
	/**
	 * 담당할 핸들러를 생성한다.
	 * @param mapId
	 * @param raidType
	 * @return DragonRaidHandler
	 */
	private DragonRaidHandler createHandler(int mapId, DragonRaildType raidType){
		switch(raidType){
		case ANTARAS:	return new Antaras(mapId, raidType);
		case FAFURION:	return new Fafurion(mapId, raidType);
		case RINDVIOR:	return new Rindvior(mapId, raidType);
		case VALAKAS:	return new Valakas(mapId, raidType);
		case HALPAS:	return new Halpas(mapId, raidType);
		default:		return null;
		}
	}
	
	/**
	 * 진행할 맵 번호를 복사하고 맵번호를 반환한다.
	 * @param raidType
	 * @return mapId
	 */
	private int getRaidMap(DragonRaildType raidType){
		if (raidType == DragonRaildType.HALPAS) {
			int copyMapId = getCopyMapId(raidType);
			if (copyMapId <= 0) {
				return 0;
			}
			if (copyMapId != raidType._mapId) {
				L1WorldMap.getInstance().cloneMap(raidType._mapId, copyMapId);
			}
			return copyMapId;
		}
		return raidType._mapId;
	}
	
	/**
	 * 카피할 맵 번호를 취득한다.
	 * @param raidType
	 * @return mapId
	 */
	private int getCopyMapId(DragonRaildType raidType){
		MapData data = MapsTable.getInstance().getMap(raidType._mapId);
		if (data == null) {
			return 0;
		}
		DragonRaidHandler handler = null;
		for (int i = data.getCloneStart(); i <= data.getCloneEnd(); i++) {
			handler = _list.get(i);
			if (handler == null) {
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * 타입별 진행중인 레이드 수를 반환한다.
	 * @param raidType
	 * @return count
	 */
	public int getRaidCount(DragonRaildType raidType){
		int cnt = 0;
		for (DragonRaidHandler handler : _list.values()) {
			if (handler._raidType == raidType) {
				cnt++;
			}
		}
		return cnt;
	}
	
	/**
	 * 진행중인 핸들러를 취득한다.
	 * @param mapId
	 * @return DragonRaidHandler
	 */
	public DragonRaidHandler getRaid(int mapId){
		return _list.get(mapId);
	}
	
	/**
	 * 핸들러를 제거한다.
	 * @param mapId
	 */
	public void removeRaid(int mapId){
		_list.remove(mapId);
	}
}


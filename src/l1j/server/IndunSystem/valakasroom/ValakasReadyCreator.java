package l1j.server.IndunSystem.valakasroom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;

public class ValakasReadyCreator {

	private static ValakasReadyCreator _instance;

	private final Map<Integer, ValakasReady> _list = new ConcurrentHashMap<Integer, ValakasReady>();

	public static ValakasReadyCreator getInstance() {
		if (_instance == null) {
			_instance = new ValakasReadyCreator();
		}
		return _instance;
	}

	public void startReady(L1PcInstance pc) {
		int id = blankMapId();
		if(id == 0){
			pc.sendPackets(L1SystemMessage.INSTANCE_DUNGEON_MAX);
			return;
		}
		if (id != 2699)
			L1WorldMap.getInstance().cloneMap(2699, id);
		ValakasReady ar = new ValakasReady(id, pc);
		pc.isInValakas = true;
		pc.getTeleport().start(32624, 33059, (short) id, 5, false);
		ar.BasicNpcList = ValakasUtil.getInstance().fillSpawnTable(id, 0, true);
		_list.put(id, ar);
		ar.Start();
	}

	/**
	 * 빈 맵 아이디를 가져온다
	 * 
	 * @return
	 */
	public int blankMapId() {
		if(_list.size() == 0)return 2699;
		MapData data = MapsTable.getInstance().getMap(2699);
		for (int i = data.getCloneStart(); i <= data.getCloneEnd(); i++) {
			ValakasReady h = _list.get(i);
			if (h == null)
				return i;
		}
		return 0;
	}

	public void removeReady(int id) {
		_list.remove(id);
	}

	public int countReadyRaid() {
		return _list.size();
	}

}


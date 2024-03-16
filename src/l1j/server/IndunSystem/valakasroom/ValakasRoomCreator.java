package l1j.server.IndunSystem.valakasroom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.utils.CommonUtil;

public class ValakasRoomCreator {

	private static ValakasRoomCreator _instance;

	private final Map<Integer, ValakasStart> _list = new ConcurrentHashMap<Integer, ValakasStart>();

	public static ValakasRoomCreator getInstance() {
		if(_instance == null)_instance = new ValakasRoomCreator();
		return _instance;
	}
		
	public void startRaid(L1PcInstance pc){
		int id = blankMapId();
		if(id == 0){
			pc.sendPackets(L1SystemMessage.INSTANCE_DUNGEON_MAX);
			return;
		}
		if(id != 2600)
			L1WorldMap.getInstance().cloneMap(2600, id);
		
		ValakasStart ar = new ValakasStart(id, pc);
		pc.isInValakas = true;
		pc.getTeleport().start(32624, 33059, (short) id, 5, false);
	
		ValakasUtil util = ValakasUtil.getInstance();
		int rnd = CommonUtil.random(3);
		if (rnd == 0) {
			ar.BasicNpcList	= util.fillSpawnTable(id, 2, true);
			ar.BossList		= util.fillSpawnTable(id, 1000, true);
		} else if (rnd == 1) {
			ar.BasicNpcList	= util.fillSpawnTable(id, 2, true);
			ar.BossList		= util.fillSpawnTable(id, 1001, true);
		} else {   
			ar.BasicNpcList	= util.fillSpawnTable(id, 2, true);
			ar.BossList		= util.fillSpawnTable(id, 1002, true);
		}
		
		_list.put(id, ar);
		ar.Start();
	}
		
	public int blankMapId(){
		if(_list.size() == 0)return 2600;
		MapData data = MapsTable.getInstance().getMap(2600);
		for(int i = data.getCloneStart(); i <= data.getCloneEnd(); i++){
			ValakasStart h = _list.get(i);
			if(h == null)return i;
		}  
		return 0;
	}

	public void removeStart(int id){
		_list.remove(id);
	}

	public int countStartRaid(){
		return _list.size();
	}

}


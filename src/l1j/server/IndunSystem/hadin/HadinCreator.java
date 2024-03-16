package l1j.server.IndunSystem.hadin;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class HadinCreator {
	private final ArrayList<Integer> _map	= new ArrayList<Integer>();
	private final Map<Integer, Hadin> _list = new ConcurrentHashMap<Integer, Hadin>();
	private static final int DEFAULT_MAP_ID	= 9000;
	
	private static HadinCreator _instance;
	public static HadinCreator getInstance() {
		if(_instance == null)_instance = new HadinCreator();
		return _instance;
	}

	public HadinCreator(){
		_map.add(DEFAULT_MAP_ID);
	}

	/**
		HadinSystem.java는 맵 관리만 해준다고 보면됨
		맵 생성 및 연구소로 텔 시키고
		하딘.java에 파티를 넘겨주고
		스레드 가동해서 파티 이용 이벤트 처리 
	**/
	public void startHadin(L1PcInstance pc){
		int id = blankMapId();
		if(id == 0){
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("인스턴스 던전에 진입한 인원이 너무 많습니다"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(31), true), true);
			return;
		}
		if(id != DEFAULT_MAP_ID)L1WorldMap.getInstance().cloneMap(DEFAULT_MAP_ID, id);
		Hadin hadin = new Hadin(id);
		for(L1PcInstance member : pc.getParty().getMembersArray()){
			if(member == null)continue;
			member.getTeleport().start(32726, 32724, (short) id, member.getMoveState().getHeading(), true);
		}
		hadin.BasicNpcList = HadinSpawn.getInstance().fillSpawnTable(id, 0, true);
		hadin.setParty(pc.getParty());
		_list.put(id, hadin);
		hadin.Start();
	}

	/**
	 * 빈 맵 아이디를 가져온다
	 * @return
	 */
	public int blankMapId(){
		if(_list.isEmpty())return DEFAULT_MAP_ID;
		MapData data = MapsTable.getInstance().getMap(DEFAULT_MAP_ID);
		for(int i = data.getCloneStart(); i <= data.getCloneEnd(); i++){
			Hadin h = _list.get(i);
			if(h == null)return i;
		}  
		return 0;
	}

	public Hadin getHadin(int id){
		return _list.get(id);
	}

	public void removeHadin(int id){
		_list.remove(id);
	}

	public int countHadin(){
		return _list.size();
	}

}



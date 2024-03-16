package l1j.server.IndunSystem.indun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;

public class IndunList {
	private final static Map<Integer, IndunInfo> DATA		= new ConcurrentHashMap<Integer, IndunInfo>();
	private final static Map<IndunType, Integer> TYPE_SIZE	= new ConcurrentHashMap<IndunType, Integer>();
	static {
		for (IndunType type : IndunType.getArray()) {
			TYPE_SIZE.put(type, 0);
		}
	}
	
	public final static int MAX_ROOM_COUNT	= 100;// 최대 동시에 가동할 수 있는 수량
	
	private IndunList(){}
	
	private static int _counter = 0;
	public static int create_room_id() {// 맵에 파생되어있는 빈공간 찾음
		if (++_counter >= 1000) {
			_counter = 0;
		}
		return _counter;
	}
	
	public static void setIndunInfo(int room_id, IndunInfo info) {// 정보 기입
		if (DATA.containsKey(room_id)) {
			return;
		}
		DATA.put(room_id, info);
		Integer count = TYPE_SIZE.get(info.indunType);
		count++;
	}
	
	public static IndunInfo getIndunInfo(int room_id) {// 정보 취득
		return DATA.containsKey(room_id) ? DATA.get(room_id) : null;
	}
	
	public static boolean isIndunInfoPcCheck(L1PcInstance pc) {// PC가 전체리스트에 있는지 체크
        Iterator<Integer> it = new ArrayList<Integer>(DATA.keySet()).iterator();
        IndunInfo info;
        boolean infoCheck = false;
        while(it.hasNext()){
        	info = DATA.get(it.next());
        	if (info.infoUserList.contains(info.getUserInfo(pc))) {
        		infoCheck = true;
        		break;
        	}
        }
        return infoCheck;
	}
	
	public static int getIndunInfoPcCheckRoomNumber(L1PcInstance pc) {// PC의 방번호를 취득
        Iterator<Integer> it = new ArrayList<Integer>(DATA.keySet()).iterator();
        IndunInfo info;
        int room_id = 0;
        while(it.hasNext()){
        	info = DATA.get(it.next());
        	if (info.infoUserList.contains(info.getUserInfo(pc))) {
        		room_id = info.room_id;
        		break;
        	}
        }
        return room_id;
	}
	
	public static Map<Integer, IndunInfo> getIndunInfoList() {// 전체 룸
		return DATA;
	}

	public static void removeIndunInfo(int room_id) {// 룸 정보 삭제
		if (!DATA.containsKey(room_id)) {
			return;
		}
		IndunInfo info = DATA.remove(room_id);
		Integer count = TYPE_SIZE.get(info.indunType);
		count--;
	}
	
	public static int getTypeSize(IndunType type){
		return TYPE_SIZE.get(type);
	}
}

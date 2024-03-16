//package l1j.server.server.model;
//
//import java.util.HashMap;
//import java.util.logging.Logger;
//
//import l1j.server.server.model.Instance.L1OrimQuestInstance;
//import l1j.server.server.model.map.L1InstanceMap;
//
//public class L1OrimQuest {
//	private static Logger _log = Logger.getLogger(L1OrimQuest.class.getName());
//
//	private static HashMap<Integer, L1OrimQuestInstance> _activeMaps;
//
//	private static L1OrimQuest _instance;
//
//	public static L1OrimQuest getInstance() {
//		if (_instance == null) {
//			_instance = new L1OrimQuest();
//		}
//		return _instance;
//	}
//
//	L1OrimQuest() {
//		_activeMaps = new HashMap<Integer, L1OrimQuestInstance>();
//	}
//
//	/**
//	 * 지정된 요소에 포함 된 인스턴스를 반환
//	 * 
//	 * @param x
//	 * @return 저장되는 인스턴스
//	 */
//	public L1OrimQuestInstance getActiveMaps(int mapId) {
//		if (!_activeMaps.containsKey(mapId)) {
//			return null;
//		}
//		return _activeMaps.get(mapId);
//	}
//
//	/**
//	 * 인스턴스 맵을 생성 HashMap으로 링크를 생성
//	 * 
//	 * @param mapId
//	 *            원본 맵 ID
//	 * @return
//	 */
//	public short setActiveMaps(int mapId) {
//		int instanceMapId = L1InstanceMap.getInstance().addInstanceMap(mapId);
//		L1OrimQuestInstance obj = new L1OrimQuestInstance((short) instanceMapId);
//		_activeMaps.put(instanceMapId, obj);
//		return obj.getMapId();
//	}
//
//	/**
//	 * 퀘스트 완료에 대해 생성 된 인스턴스 맵을 개방
//	 * 
//	 * @param mapId
//	 */
//	public void resetActiveMaps(int mapId) {
//		if (!_activeMaps.containsKey(mapId)) {
//			return;
//		}
//		_activeMaps.remove(mapId);
//		L1InstanceMap.getInstance().removeInstanceMap(mapId);
//	}
//
//	/**
//	 * 진행중인 퀘스트의 결과
//	 * 
//	 * @return
//	 */
//	public int getNumOfActiveMaps() {
//		return _activeMaps.size();
//	}
//}


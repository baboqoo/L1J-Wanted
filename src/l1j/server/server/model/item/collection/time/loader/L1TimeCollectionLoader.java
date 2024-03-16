package l1j.server.server.model.item.collection.time.loader;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.common.bin.TimeCollectionCommonBinLoader;
import l1j.server.common.bin.timecollection.TimeCollection;
import l1j.server.common.data.NPCDialogInfoT;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;

/**
 * 실렉티스 전시회 데이터 로드 클래스
 * @author LinOffice
 */
public class L1TimeCollectionLoader {
	private static Logger _log	= Logger.getLogger(L1TimeCollectionLoader.class.getName());
	private static L1TimeCollectionLoader _instance;
	
	public static L1NpcInstance TIME_COLLECTION_SHOP_NPC, TIME_COLLECTION_CRAFT_NPC;
	
	// KEY: groupId, VALUE: (key: setId, L1TimeCollection)
	private static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, L1TimeCollection>> GROUP_DATA = new ConcurrentHashMap<>();
	// KEY: setId, VALUE: L1TimeCollection
	private static final ConcurrentHashMap<Integer, L1TimeCollection> SET_ID_DATA = new ConcurrentHashMap<>();
	
	/**
	 * 전체 데이터 조사
	 * @return Map
	 */
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, L1TimeCollection>> getAllData(){
		return GROUP_DATA;
	}
	
	/**
	 * 그룹별 데이터 조사
	 * @param groupId
	 * @return Map
	 */
	public static ConcurrentHashMap<Integer, L1TimeCollection> getTypeData(int groupId){
		return GROUP_DATA.get(groupId);
	}
	
	/**
	 * 컬렉션에 대한 데이터 조사
	 * @param groupId
	 * @param setId
	 * @return L1TimeCollection
	 */
	public static L1TimeCollection getData(int groupId, int setId){
		ConcurrentHashMap<Integer, L1TimeCollection> map = getTypeData(groupId);
		return map == null || map.isEmpty() ? null : map.get(setId);
	}
	
	/**
	 * 컬렉션에 대한 데이터 조사
	 * @param setId
	 * @return L1TimeCollection
	 */
	public static L1TimeCollection getData(int setId){
		return SET_ID_DATA.get(setId);
	}
	
	/**
	 * 싱글톤 생성
	 * @return L1TimeCollectionLoader
	 */
	public static L1TimeCollectionLoader getInstance(){
		if (_instance == null) {
			_instance = new L1TimeCollectionLoader();
		}
		return _instance;
	}
	
	/**
	 * 기본 생성자
	 */
	private L1TimeCollectionLoader(){
		load();
	}
	
	/**
	 * 데이터 로드
	 * time_collection-common.bin 기준으로 사용할 캐시를 설정한다.
	 */
	private void load(){
		TimeCollection collect	= TimeCollectionCommonBinLoader.getData();
		
		NPCDialogInfoT npcInfoT = collect.get_NPCDialogInfo();
		if (npcInfoT != null) {
			L1NpcInstance npc = null;
			L1World world = L1World.getInstance();
			for (NPCDialogInfoT.LinkerT linkerT : npcInfoT.get_Linker()) {
				int npcId = 0;
				int index = linkerT.get_Index();
				switch (index) {
				case 1:
					npcId = Config.COLLECTION.TIME_COLLECTION_SHOP_NPC_ID;
					break;
				case 2:
					npcId = Config.COLLECTION.TIME_COLLECTION_CRAFT_NPC_ID;
					break;
				default:
					System.out.println(String.format("[L1TimeCollectionLoader] NPC_DIALOG_UNDEFINDE_INDEX : INDEX(%d)", index));
					continue;
				}
				npc = world.findNpc(npcId);
				if (npc == null) {
					System.out.println(String.format("[L1TimeCollectionLoader] NPC_DIALOG_OBJECT_NOT_FOUND : NPC_ID(%d)", npcId));
					continue;
				}
				npc.getNpcTemplate().setNotification(true);
				switch (index) {
				case 1:
					TIME_COLLECTION_SHOP_NPC = npc;
					break;
				case 2:
					TIME_COLLECTION_CRAFT_NPC = npc;
					break;
				}
			}
		}
		
		L1TimeCollection obj	= null;
		try {
			for (TimeCollection.GroupT group : collect.get_Group()) {
				for (TimeCollection.GroupT.SetT set : group.get_Set()) {
					obj = new L1TimeCollection(collect, group.get_ID(), set);
					ConcurrentHashMap<Integer, L1TimeCollection> map = GROUP_DATA.get(obj.getGroupId());
					if (map == null) {
						map = new ConcurrentHashMap<Integer, L1TimeCollection>();
						GROUP_DATA.put(obj.getGroupId(), map);
					}
					map.put(set.get_ID(), obj);
					SET_ID_DATA.put(set.get_ID(), obj);
				}
			}
			if (SET_ID_DATA.isEmpty()) {
				System.out.println("[L1TimeCollectionLoader] COMMON_BIN_DATA_EMPTY");
				return;
			}
			
			// 데이터의 바인드 검증
			for (L1TimeCollection val : SET_ID_DATA.values()) {
				if (!val.isValidation()) {
					System.out.println(String.format("[L1TimeCollectionLoader] DEFINED_FAILURE : GROUP(%d) ID(%d)", val.getGroupId(), val.getSet().get_ID()));
				}
			}
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 데이터 리로드
	 */
	public void reload(){
		if (!GROUP_DATA.isEmpty()) {
			for (ConcurrentHashMap<Integer, L1TimeCollection> map : GROUP_DATA.values()) {
				if (map == null || map.isEmpty()) {
					continue;
				}
				map.clear();
			}
			GROUP_DATA.clear();
		}
		SET_ID_DATA.clear();
		
		if (TIME_COLLECTION_SHOP_NPC != null) {
			TIME_COLLECTION_SHOP_NPC.getNpcTemplate().setNotification(false);
		}
		if (TIME_COLLECTION_CRAFT_NPC != null) {
			TIME_COLLECTION_CRAFT_NPC.getNpcTemplate().setNotification(false);
		}
		
		load();
	}
}


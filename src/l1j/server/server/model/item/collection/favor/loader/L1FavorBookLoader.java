package l1j.server.server.model.item.collection.favor.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.FavorBookCommonBinLoader;
import l1j.server.common.bin.favorbook.AUBIBookInfoForClient;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookCategoryObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 성물 데이터 로드 클래스
 * @author LinOffice
 */
public class L1FavorBookLoader {
	private static Logger _log	= Logger.getLogger(L1FavorBookLoader.class.getName());
	private static L1FavorBookLoader _instance;
	
	private static final ConcurrentHashMap<Integer, ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>>> DATA	= new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<L1FavorBookCategoryObject, ConcurrentHashMap<Integer, L1FavorBookObject>> DATA_FROM_CATEGORY	= new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Integer, L1FavorBookObject> DATA_FROM_ITEMID			= new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Integer, L1FavorBookCategoryObject> CATEGORY_DATA	= new ConcurrentHashMap<>();
	
	/**
	 * 성물 아이템에 해당하는지 조사한다.
	 * @param itemId
	 * @return boolean
	 */
	public static boolean isFavorItem(int itemId){
		return DATA_FROM_ITEMID.containsKey(itemId);
	}
	
	/**
	 * 성물 반환
	 * @param itemId
	 * @return L1FavorBookObject
	 */
	public static L1FavorBookObject getFavor(int itemId){
		return DATA_FROM_ITEMID.get(itemId);
	}
	
	/**
	 * 성물 반환
	 * @param category
	 * @param slotId
	 * @return L1FavorBookObject
	 */
	public static L1FavorBookObject getFavor(L1FavorBookCategoryObject category, int slotId) {
		ConcurrentHashMap<Integer, L1FavorBookObject> map = DATA_FROM_CATEGORY.get(category);
		if (map == null) {
			return null;
		}
		return map.get(slotId);
	}
	
	/**
	 * 전체 성물 데이터 반환
	 * @return ConcurrentHashMap<Integer, ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>>>
	 */
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>>> getAllData(){
		return DATA;
	}
	
	/**
	 * 성물 타입별 리스트 데이터 반환
	 * @param listId
	 * @return ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>>
	 */
	public static ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>> getListToId(int listId){
		return DATA.get(listId);
	}
	
	/**
	 * 성물 반환
	 * @param listId
	 * @param category
	 * @param slotId
	 * @return L1FavorBookObject
	 */
	public static L1FavorBookObject getFavor(int listId, L1FavorBookCategoryObject category, int slotId){
		if (listId == 0) {
			return selectToAll(category, slotId);
		}
		ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>> map = getListToId(listId);
		if (map == null || map.isEmpty()) {
			return null;
		}
		ArrayList<L1FavorBookObject> list = map.get(category);
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (L1FavorBookObject obj : list) {
			if (!obj.getCategory().equals(category) || obj.getSlotId() != slotId) {
				continue;
			}
			return obj;
		}
		return null;
	}
	
	/**
	 * 성물 반환(전체 목록에서 조사)
	 * @param category
	 * @param slotId
	 * @return L1FavorBookObject
	 */
	private static L1FavorBookObject selectToAll(L1FavorBookCategoryObject category, int slotId){
		for (L1FavorBookObject obj : DATA_FROM_ITEMID.values()) {
			if (!obj.getCategory().equals(category) || obj.getSlotId() != slotId) {
				continue;
			}
			return obj;
		}
		return null;
	}
	
	/**
	 * 카테고리 오브젝트를 조사한다.
	 * @param category
	 * @return L1FavorBookCategoryObject
	 */
	public static L1FavorBookCategoryObject getCategory(int category){
		return CATEGORY_DATA.get(category);
	}
	
	/**
	 * 싱글톤 생성
	 * @return L1FavorBookLoader
	 */
	public static L1FavorBookLoader getInstance(){
		if (_instance == null) {
			_instance = new L1FavorBookLoader();
		}
		return _instance;
	}
	
	/**
	 * 기본 생성자
	 */
	private L1FavorBookLoader() {
		load();
	}
	
	/**
	 * 데이터를 로드한다.
	 */
	private void load(){
		// bin 데이터 카테고리 설정
		L1FavorBookCategoryObject category	= null;
		for (AUBIBookInfoForClient.BookT.CategoryT categoryT : FavorBookCommonBinLoader.get_categories()) {
			category = new L1FavorBookCategoryObject(categoryT);
			CATEGORY_DATA.put(category.getCategory(), category);
		}
		
		Connection con						= null;
		PreparedStatement pstm				= null;
		ResultSet rs						= null;
		L1FavorBookObject favor				= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();

			// 상세 로드
			pstm	= con.prepareStatement("SELECT * FROM favorbook ORDER BY category, slotId");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int listId						= rs.getInt("listId");
				int categoryid					= rs.getInt("category");
				category						= CATEGORY_DATA.get(categoryid);
				if (category == null) {
					System.out.println(String.format("[L1FavorBookLoader] NOT_USE_CATEGORY : CATEGORY(%d)", categoryid));
					continue;
				}
				int slotId						= rs.getInt("slotId");
				ArrayList<Integer> itemIds		= parseItemIds(rs.getString("itemIds"));
				favor = new L1FavorBookObject(listId, category, slotId, itemIds);
				
				if (itemIds != null && !itemIds.isEmpty()) {
					for (int itemId : itemIds) {
						DATA_FROM_ITEMID.put(itemId, favor);
					}
				}
				
				ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>> categoryMap = DATA.get(listId);
				if (categoryMap == null) {
					categoryMap = new ConcurrentHashMap<>();
					DATA.put(listId, categoryMap);
				}
				
				ArrayList<L1FavorBookObject> list = categoryMap.get(category);
				if (list == null) {
					list = new ArrayList<>();
					categoryMap.put(category, list);
				}
				list.add(favor);
				
				ConcurrentHashMap<Integer, L1FavorBookObject> map = DATA_FROM_CATEGORY.get(category);
				if (map == null) {
					map = new ConcurrentHashMap<>();
					DATA_FROM_CATEGORY.put(category, map);
				}
				map.put(slotId, favor);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 성물 아이템아이디를 조사한다.
	 * @param str
	 * @return ArrayList<Integer>
	 */
	private ArrayList<Integer> parseItemIds(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		ItemTable temp = ItemTable.getInstance();
		ArrayList<Integer> list = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(str, StringUtil.LineString);
		while (st.hasMoreTokens()) {
			int itemId = Integer.parseInt(st.nextToken().trim());
			if (temp.getTemplate(itemId) == null) {
				System.out.println(String.format("[L1FavorBookLoader] ITEM NOT FOUND! ID: %d", itemId));
				continue;
			}
			list.add(itemId);
		}
		return list;
	}
	
	/**
	 * 데이터 리로드
	 */
	public void reload(){
		if (!DATA.isEmpty()) {
			for (ConcurrentHashMap<L1FavorBookCategoryObject, ArrayList<L1FavorBookObject>> value : DATA.values()){
				if (value == null || value.isEmpty()) {
					continue;
				}
				for (ArrayList<L1FavorBookObject> list : value.values()) {
					if (list == null || list.isEmpty()) {
						continue;
					}
					list.clear();
				}
			}
			DATA.clear();
		}
		DATA_FROM_CATEGORY.clear();
		DATA_FROM_ITEMID.clear();
		CATEGORY_DATA.clear();
		load();
	}
}


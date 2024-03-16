package l1j.server.server.model.item.collection.time.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionMaterial;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 실렉티스 전시회 플레이어 데이터 로드 클래스
 * @author LinOffice
 */
public class L1TimeCollectionUserLoader {
	private static Logger _log = Logger.getLogger(L1TimeCollectionUserLoader.class.getName());
	private static L1TimeCollectionUserLoader _instance;
	
	private static final ConcurrentHashMap<Integer, ArrayList<L1TimeCollectionUser>> DATA = new ConcurrentHashMap<>();
	
	/**
	 * 플레이어의 데이터를 조사한다.
	 * 이미 사용후 기간이 지난 데이터는 제거한다.
	 * @param charObjId
	 * @return ArrayList<L1TimeCollectionUser>
	 */
	public static ArrayList<L1TimeCollectionUser> getUserList(int charObjId){
		ArrayList<L1TimeCollectionUser> list = DATA.get(charObjId);
		if (list != null && !list.isEmpty()) {
			long currentTime = System.currentTimeMillis();
			ArrayList<L1TimeCollectionUser> deleteList = null;
			for (L1TimeCollectionUser user : list) {
				if (user.getBuffTime() != null && user.getBuffTime().getTime() <= currentTime) {
					if (deleteList == null) {
						deleteList = new ArrayList<>();
					}
					deleteList.add(user);
				}
			}
			if (deleteList != null && !deleteList.isEmpty()) {
				delete(deleteList);
				for (L1TimeCollectionUser delete : deleteList) {
					list.remove(delete);
				}
				deleteList.clear();
				deleteList = null;
			}
		}
		return list;
	}
	
	/**
	 * 싱글톤 생성
	 * @return L1TimeCollectionUserLoader
	 */
	public static L1TimeCollectionUserLoader getInstance(){
		if (_instance == null) {
			_instance = new L1TimeCollectionUserLoader();
		}
		return _instance;
	}
	
	/**
	 * 기본 생성자
	 */
	private L1TimeCollectionUserLoader(){
		load();
	}
	
	/**
	 * 데이터를 로드한다.
	 */
	private void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		L1TimeCollectionUser user	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			
			// 시간이 지난 데이터 제거
			pstm	= con.prepareStatement("DELETE FROM character_timecollection WHERE (buffTime IS NOT NULL AND buffTime < NOW()) OR NULLIF(slots, '') IS NULL");
			pstm.execute();
			SQLUtil.close(pstm);
			
			// 로드
			pstm	= con.prepareStatement("SELECT * FROM character_timecollection");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int charObjId				= rs.getInt("charObjId");
				int groupId					= rs.getInt("groupId");
				int setId					= rs.getInt("setId");
				L1TimeCollection obj		= L1TimeCollectionLoader.getData(groupId, setId);
				if (obj == null) {
					System.out.println(String.format("[L1TimeCollectionUserLoader] TEMPLATE_NOT_FOUND : GROUP(%d), ID(%d)", groupId, setId));
					continue;
				}
				ConcurrentHashMap<Integer, L1ItemInstance> slots	= parseSlotsToLoad(rs.getString("slots"));
				if (slots != null && !slots.isEmpty() && !isValidationSlotMaterial(slots, obj)) {
					continue;
				}
				
				boolean registComplet				= Boolean.valueOf(rs.getString("registComplet"));
				int sumEnchant						= 0;
				if (registComplet && slots != null && !slots.isEmpty()) {
					for (L1ItemInstance item : slots.values()) {
						sumEnchant += item.getEnchantLevel();
					}
				}
				L1TimeCollectionBuffType buffType	= L1TimeCollectionBuffType.fromString(rs.getString("buffType"));
				Timestamp buffTime					= rs.getTimestamp("buffTime");
				int refillCount						= rs.getInt("refillCount");
				
				user = new L1TimeCollectionUser(charObjId, groupId, setId, slots, registComplet, sumEnchant, buffType, buffTime, refillCount, obj);
				
				ArrayList<L1TimeCollectionUser> list = DATA.get(user.getCharObjId());
				if (list == null) {
					list = new ArrayList<>();
					DATA.put(user.getCharObjId(), list);
				}
				list.add(user);
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
	 * 재료 검증
	 * @param slots
	 * @param obj
	 * @return boolean
	 */
	private boolean isValidationSlotMaterial(ConcurrentHashMap<Integer, L1ItemInstance> slots, L1TimeCollection obj) {
		for (Map.Entry<Integer, L1ItemInstance> entry : slots.entrySet()) {
			L1TimeCollectionMaterial material	= obj.getMaterial(entry.getKey());
			// 슬롯에 대한 재료 존재 여부
			if (material == null) {
				return false;
			}
			// 슬롯에 대한 재료 등록 가능 여부
			if (!material.isMaterial(entry.getValue())) {
				return false;
			}
		}
		return true;
	}
	
	private static final String UPSERT_QUERY = "INSERT INTO character_timecollection SET "
			+ "charObjId=?, groupId=?, setId=?, slots=?, registComplet=?, buffType=?, buffTime=?, refillCount=? "
			+ "ON DUPLICATE KEY UPDATE "
			+ "slots=?, registComplet=?, buffType=?, buffTime=?, refillCount=?";
	
	/**
	 * 데이터를 등록한다.
	 * @param user
	 * @return boolean
	 */
	public boolean insert(L1TimeCollectionUser user){
		Connection con				= null;
		PreparedStatement pstm		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(UPSERT_QUERY);
			
			String slots		= parseSlotsToInsert(user.getSlots());
			
			int i	= 0;
			pstm.setInt(++i, user.getCharObjId());
			pstm.setInt(++i, user.getGroupId());
			pstm.setInt(++i, user.getSetId());
			pstm.setString(++i, slots);
			pstm.setString(++i, Boolean.toString(user.isRegistComplet()));
			pstm.setString(++i, user.getBuffType().toName());
			pstm.setTimestamp(++i, user.getBuffTime());
			pstm.setInt(++i, user.getRefillCount());
			pstm.setString(++i, slots);
			pstm.setString(++i, Boolean.toString(user.isRegistComplet()));
			pstm.setString(++i, user.getBuffType().toName());
			pstm.setTimestamp(++i, user.getBuffTime());
			pstm.setInt(++i, user.getRefillCount());
			
			if (pstm.executeUpdate() > 0) {
				ArrayList<L1TimeCollectionUser> list = DATA.get(user.getCharObjId());
				if (list == null) {
					list = new ArrayList<>();
					DATA.put(user.getCharObjId(), list);
				}
				list.add(user);
				return true;
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	/**
	 * 플레이어의 데이터를 갱신한다.
	 * @param pc
	 */
	public void merge(L1PcInstance pc){
		L1TimeCollectionHandler handler = pc.getTimeCollection();
		if (handler == null) {
			return;
		}
		ConcurrentHashMap<Integer, L1TimeCollectionUser> map = handler.getData();
		if (map == null || map.isEmpty()) {
			return;
		}
		DATA.put(pc.getId(), new ArrayList<>(map.values()));
		
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			
			pstm	= con.prepareStatement(UPSERT_QUERY);
			for (L1TimeCollectionUser user : map.values()) {
				if (user.getSlots() == null || user.getSlots().isEmpty()) {
					continue;
				}
				String slots	= parseSlotsToInsert(user.getSlots());
				int i	= 0;
				pstm.setInt(++i, user.getCharObjId());
				pstm.setInt(++i, user.getGroupId());
				pstm.setInt(++i, user.getSetId());
				pstm.setString(++i, slots);
				pstm.setString(++i, Boolean.toString(user.isRegistComplet()));
				pstm.setString(++i, user.getBuffType().toName());
				pstm.setTimestamp(++i, user.getBuffTime());
				pstm.setInt(++i, user.getRefillCount());
				pstm.setString(++i, slots);
				pstm.setString(++i, Boolean.toString(user.isRegistComplet()));
				pstm.setString(++i, user.getBuffType().toName());
				pstm.setTimestamp(++i, user.getBuffTime());
				pstm.setInt(++i, user.getRefillCount());
				pstm.addBatch();
				pstm.clearParameters();
			}
			
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch(SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 데이터를 제거한다.
	 * @param pc
	 * @param user
	 * @return boolean
	 */
	public boolean delete(L1TimeCollectionUser user){
		Connection con				= null;
		PreparedStatement pstm		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM character_timecollection WHERE charObjId=? AND groupId=? AND setId=?");
			pstm.setInt(1, user.getCharObjId());
			pstm.setInt(2, user.getGroupId());
			pstm.setInt(3, user.getSetId());
			if (pstm.executeUpdate() > 0) {
				ArrayList<L1TimeCollectionUser> list = DATA.get(user.getCharObjId());
				if (list == null) {
					return false;
				}
				list.remove(user);
				return true;
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	/**
	 * 데이터를 제거한다.
	 * @param deleteList
	 */
	private static void delete(ArrayList<L1TimeCollectionUser> deleteList){
		Connection con				= null;
		PreparedStatement pstm		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			
			pstm	= con.prepareStatement("DELETE FROM character_timecollection WHERE charObjId=? AND groupId=? AND setId=?");
			for(L1TimeCollectionUser user : deleteList){
				pstm.setInt(1, user.getCharObjId());
				pstm.setInt(2, user.getGroupId());
				pstm.setInt(3, user.getSetId());
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch(SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 캐릭터의 모든 데이터를 삭제한다.
	 * @param charObjId
	 */
	public void remove(int charObjId){
		DATA.remove(charObjId);
	}
	
	private static final String DIVID_STRING = "===============";
	
	/**
	 * 등록된 아이템을 파싱한다.
	 * @param str
	 * @return Map
	 */
	private ConcurrentHashMap<Integer, L1ItemInstance> parseSlotsToLoad(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		ConcurrentHashMap<Integer, L1ItemInstance> map = new ConcurrentHashMap<>();
		String[] array = str.split(DIVID_STRING);
		
		ItemTable itemTable	= ItemTable.getInstance();
		L1ItemInstance item	= null;
		for (int i=0; i<array.length; i++) {
			String one = array[i];
			if (StringUtil.isNullOrEmpty(one)) {
				continue;
			}
			String[] tempArray = one.split(StringUtil.LineString);
			int slotIndex = 0, itemId = 0, enchant = 0;
			for (int j=0; j<tempArray.length; j++) {
				String temp = tempArray[j].trim();
				if (StringUtil.isNullOrEmpty(temp)) {
					continue;
				}
				if (temp.startsWith("SLOT_INDEX:")) {
					slotIndex = Integer.parseInt(temp.replace("SLOT_INDEX:", StringUtil.EmptyString).trim());
				} else if (temp.startsWith("ITEMID:")) {
					itemId = Integer.parseInt(temp.replace("ITEMID:", StringUtil.EmptyString).trim());
				} else if (temp.startsWith("ENCHANT:")) {
					enchant = Integer.parseInt(temp.replace("ENCHANT:", StringUtil.EmptyString).trim());
				}
			}
			if (slotIndex <= 0 || itemId <= 0 || enchant <= 0) {
				continue;
			}
			
			item = itemTable.createItem(itemId);
			if (item == null) {
				System.out.println(String.format("[L1TimeCollectionUserLoader] NOT FOUND ITEM TEMPLATE ITEMID(%d)", itemId));
				continue;
			}
			
			item.setEnchantLevel(enchant);
			item.setIdentified(true);
			item.updateItemAbility(null);
			map.put(slotIndex, item);
		}
		return map;
	}
	
	/**
	 * 등록할 아이템을 파싱한다.
	 * @param map
	 * @return String
	 */
	private String parseSlotsToInsert(ConcurrentHashMap<Integer, L1ItemInstance> map){
		if (map == null || map.isEmpty()) {
			return StringUtil.EmptyString;
		}
		StringBuilder sb		= new StringBuilder();
		int key					= 0;
		L1ItemInstance value	= null;
		int cnt					= 0;
		for (Map.Entry<Integer, L1ItemInstance> entry : map.entrySet()) {
			key		= entry.getKey();// slotIndex
			value	= entry.getValue();// item
			sb.append("SLOT_INDEX:").append(key).append(StringUtil.LineString);
			sb.append("ITEMID:").append(value.getItemId()).append(StringUtil.LineString);
			sb.append("ENCHANT:").append(value.getEnchantLevel()).append(StringUtil.LineString);
			sb.append(DIVID_STRING);
			if (++cnt < map.size()) {
				sb.append(StringUtil.LineString);
			}
		}
		return sb.toString();
	}
}


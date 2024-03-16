package l1j.server.server.model.item.collection.favor.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookCategoryObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.utils.SQLUtil;

/**
 * 성물 데이터 등록 정보 로드 클래스
 * @author LinOffice
 */
public class L1FavorBookUserLoader {
	private static Logger _log = Logger.getLogger(L1FavorBookUserLoader.class.getName());
	private static L1FavorBookUserLoader _instance;
	private static final ConcurrentHashMap<Integer, ArrayList<L1FavorBookUserObject>> DATA = new ConcurrentHashMap<>();
	
	/**
	 * 플레이어의 성물 정보를 조사한다.
	 * @param charObjId
	 * @return ArrayList<L1FavorBookUser>
	 */
	public static ArrayList<L1FavorBookUserObject> getFavorUserList(int charObjId){
		return DATA.get(charObjId);
	}
	
	/**
	 * 싱글톤 생성
	 * @return L1FavorBookUserLoader
	 */
	public static L1FavorBookUserLoader getInstance(){
		if (_instance == null) {
			_instance = new L1FavorBookUserLoader();
		}
		return _instance;
	}
	
	/**
	 * 기본 생성자
	 */
	private L1FavorBookUserLoader(){
		load();
	}
	
	/**
	 * 데이터를 로드한다.
	 */
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1ItemInstance item		= null;
		ItemTable itemTable		= ItemTable.getInstance();
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			
			// 기간이 지난 성물 제거
			pstm	= con.prepareStatement("DELETE T1 FROM character_favorbook AS T1 INNER JOIN bin_favorbook_common AS T2 ON T2.category_id = T1.category AND T2.slot_id = T1.slotId WHERE T2.end_date IS NOT NULL AND T2.end_date < NOW()");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM character_favorbook");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int charObjId						= rs.getInt("charObjId");
				L1FavorBookCategoryObject category	= L1FavorBookLoader.getCategory(rs.getInt("category"));
				int slotId							= rs.getInt("slotId");
				L1FavorBookObject obj				= L1FavorBookLoader.getFavor(category, slotId);
				if (obj == null) {
					System.out.println(String.format("[L1FavorBookUserLoader] FAVOR OBJ NOT FOUND category(%d), slotId(%d)", 
							category.getCategory(), slotId));
					continue;
				}
				int itemObjId						= rs.getInt("itemObjId");
				int itemId							= rs.getInt("itemId");
				int count							= rs.getInt("count");
				int enchantLevel					= rs.getInt("enchantLevel");
				int attrLevel						= rs.getInt("attrLevel");
				int bless							= rs.getInt("bless");
				Timestamp endTime					= rs.getTimestamp("endTime");
				int craftId							= rs.getInt("craftId");
				int awakening						= rs.getInt("awakening");
				item								= itemTable.createItem(itemId, itemObjId);
				if (item != null) {
					item.setCount(count);
					item.setEnchantLevel(enchantLevel);
					item.setAttrEnchantLevel(attrLevel);
					item.setBless(bless);
					item.setEndTime(endTime);
					item.setIdentified(true);
					item.updateItemAbility(null);
				}
				ArrayList<L1FavorBookUserObject> list = DATA.get(charObjId);
				if (list == null) {
					list = new ArrayList<>();
					DATA.put(charObjId, list);
				}
				
				list.add(new L1FavorBookUserObject(category, slotId, item, craftId, awakening, obj));
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private static final String UPSERT_QUERY = "INSERT INTO character_favorbook SET "
			+ "charObjId=?, category=?, slotId=?, itemObjId=?, itemId=?, itemName=?, count=?, enchantLevel=?, attrLevel=?, bless=?, endTime=?, craftId=?, awakening=? "
			+ "ON DUPLICATE KEY UPDATE "
			+ "itemObjId=?, itemId=?, itemName=?, count=?, enchantLevel=?, attrLevel=?, bless=?, endTime=?, craftId=?, awakening=?";
	
	/**
	 * 데이터를 생성한다.
	 * @param pc
	 * @param user
	 * @return boolean
	 */
	public boolean insert(L1PcInstance pc, L1FavorBookUserObject user){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(UPSERT_QUERY);
			L1ItemInstance item = user.getCurrentItem();
			int i = 0;
			pstm.setInt(++i, pc.getId());
			pstm.setInt(++i, user.getCategory().getCategory());
			pstm.setInt(++i, user.getSlotId());
			pstm.setInt(++i, item.getId());
			pstm.setInt(++i, item.getItemId());
			//pstm.setString(++i, item.getDescKr());
			pstm.setString(++i, item.getDescEn());
			pstm.setInt(++i, item.getCount());
			pstm.setInt(++i, item.getEnchantLevel());
			pstm.setInt(++i, item.getAttrEnchantLevel());
			pstm.setInt(++i, item.getBless());
			pstm.setTimestamp(++i, item.getEndTime());
			pstm.setInt(++i, user.getCraftId());
			pstm.setInt(++i, user.getAwakening());
			
			pstm.setInt(++i, item.getId());
			pstm.setInt(++i, item.getItemId());
			//pstm.setString(++i, item.getDescKr());
			pstm.setString(++i, item.getDescEn());
			pstm.setInt(++i, item.getCount());
			pstm.setInt(++i, item.getEnchantLevel());
			pstm.setInt(++i, item.getAttrEnchantLevel());
			pstm.setInt(++i, item.getBless());
			pstm.setTimestamp(++i, item.getEndTime());
			pstm.setInt(++i, user.getCraftId());
			pstm.setInt(++i, user.getAwakening());
			
			if (pstm.executeUpdate() > 0) {
				ArrayList<L1FavorBookUserObject> list = DATA.get(pc.getId());
				if (list == null) {
					list = new ArrayList<>();
					DATA.put(pc.getId(), list);
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
	 * 데이터를 갱신한다.
	 * @param pc
	 */
	public void merge(L1PcInstance pc){
		L1FavorBookInventory favorBook = pc.getFavorBook();
		if (favorBook == null) {
			return;
		}
		ArrayList<L1FavorBookUserObject> list = favorBook.getList();
		if (list == null || list.isEmpty()) {
			return;
		}
		DATA.put(pc.getId(), new ArrayList<L1FavorBookUserObject>(list));
		
		Connection con			= null;
		PreparedStatement pstm	= null;
		L1ItemInstance item		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			
			pstm	= con.prepareStatement(UPSERT_QUERY);
			for (L1FavorBookUserObject user : list) {
				int i = 0;
				item = user.getCurrentItem();
				pstm.setInt(++i, pc.getId());
				pstm.setInt(++i, user.getCategory().getCategory());
				pstm.setInt(++i, user.getSlotId());
				pstm.setInt(++i, item.getId());
				pstm.setInt(++i, item.getItemId());
				//pstm.setString(++i, item.getDescKr());
				pstm.setString(++i, item.getDescEn());
				pstm.setInt(++i, item.getCount());
				pstm.setInt(++i, item.getEnchantLevel());
				pstm.setInt(++i, item.getAttrEnchantLevel());
				pstm.setInt(++i, item.getBless());
				pstm.setTimestamp(++i, item.getEndTime());
				pstm.setInt(++i, user.getCraftId());
				pstm.setInt(++i, user.getAwakening());
				
				pstm.setInt(++i, item.getId());
				pstm.setInt(++i, item.getItemId());
				//pstm.setString(++i, item.getDescKr());
				pstm.setString(++i, item.getDescEn());
				pstm.setInt(++i, item.getCount());
				pstm.setInt(++i, item.getEnchantLevel());
				pstm.setInt(++i, item.getAttrEnchantLevel());
				pstm.setInt(++i, item.getBless());
				pstm.setTimestamp(++i, item.getEndTime());
				pstm.setInt(++i, user.getCraftId());
				pstm.setInt(++i, user.getAwakening());
				
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
	 * 데이터를 삭제한다.
	 * @param pc
	 * @param user
	 * @return boolean
	 */
	public boolean delete(L1PcInstance pc, L1FavorBookUserObject user){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM character_favorbook WHERE charObjId=? AND category=? AND slotId=?");
			pstm.setInt(1, pc.getId());
			pstm.setInt(2, user.getCategory().getCategory());
			pstm.setInt(3, user.getSlotId());
			if (pstm.executeUpdate() > 0) {
				ArrayList<L1FavorBookUserObject> list = DATA.get(pc.getId());
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
	 * 캐릭터의 모든 데이터를 제거한다.
	 * @param charId
	 */
	public void remove(int charId){
		DATA.remove(charId);
	}
}


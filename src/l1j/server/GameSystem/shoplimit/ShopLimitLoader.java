package l1j.server.GameSystem.shoplimit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.shoplimit.bean.ShopLimitObject;
import l1j.server.GameSystem.shoplimit.bean.ShopLimitInformation;
import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

/**
 * 상점 제한 정보
 * @author LinOffice
 */
public class ShopLimitLoader {
	private static ShopLimitLoader _instance;
	public static ShopLimitLoader getInstance(){
		if (_instance == null) {
			_instance = new ShopLimitLoader();
		}
		return _instance;
	}
	
	private static final FastMap<Integer, FastTable<ShopLimitInformation>> LIMIT_DATA	= new FastMap<>();
	private static final FastMap<String, ShopLimitUser> ACCOUNT_DATA					= new FastMap<>();// 계정별 정보
	private static final FastMap<Integer, ShopLimitUser> CHARACTER_DATA					= new FastMap<>();// 캐릭터별 정보
	
	/**
	 * 계정의 제한 정보
	 * @param accountName
	 * @return ShopLimitUser
	 */
	public static ShopLimitUser getShopLimitFromAccount(String accountName){
		return ACCOUNT_DATA.get(accountName);
	}
	
	/**
	 * 캐릭터의 제한 정보
	 * @param characterId
	 * @return ShopLimitUser
	 */
	public static ShopLimitUser getShopLimitFromCharacter(int characterId){
		return CHARACTER_DATA.get(characterId);
	}
	
	/**
	 * 엔피씨의 제한 정보
	 * @param npcId
	 * @return FastTable<ShopLimitInformation>
	 */
	public static FastTable<ShopLimitInformation> getShopLimitList(int npcId){
		return LIMIT_DATA.get(npcId);
	}
	
	/**
	 * 엔피씨의 제한 정보
	 * @param npcId
	 * @param ItemId
	 * @return ShopLimitInformation
	 */
	public static ShopLimitInformation getShopLimit(int npcId, int ItemId){
		FastTable<ShopLimitInformation> list = getShopLimitList(npcId);
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (ShopLimitInformation obj : list) {
			if (obj.getItemId() == ItemId) {
				return obj;
			}
		}
		return null;
	}
	
	private ShopLimitLoader(){
		load();
	}
	
	void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		ShopLimitInformation limit	= null;
		ShopLimitObject obj			= null;
		ShopLimitUser limitUser		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM shop_limit");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int shopId				= rs.getInt("shopId");
				int itemId				= rs.getInt("itemId");
				ShopLimitTerm limitTerm	= ShopLimitTerm.fromString(rs.getString("limitTerm"));
				int limitCount			= rs.getInt("limitCount");
				ShopLimitType limitType	= ShopLimitType.fromString(rs.getString("limitType"));
				limit = new ShopLimitInformation(shopId, itemId, limitTerm, limitCount, limitType);
				
				FastTable<ShopLimitInformation> list = LIMIT_DATA.get(shopId);
				if (list == null) {
					list = new FastTable<ShopLimitInformation>();
					LIMIT_DATA.put(shopId, list);
				}
				list.add(limit);
			}
			SQLUtil.close(rs, pstm);
			
			// 기간이 지난 데이터 삭제(프로시저 호출)
			pstm	= con.prepareStatement("CALL SHOP_BUY_LIMIT_USER_INIT()");
			pstm.execute();
			SQLUtil.close(pstm);
			
			// 구매 제한 정보 로드(계정)
			pstm	= con.prepareStatement("SELECT * FROM accounts_shop_limit");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				String accountName		= rs.getString("accountName");
				int buyShopId			= rs.getInt("buyShopId");
				int buyItemId			= rs.getInt("buyItemId");
				int buyCount			= rs.getInt("buyCount");
				Timestamp buyTime		= rs.getTimestamp("buyTime");
				ShopLimitTerm limitTerm	= ShopLimitTerm.fromString(rs.getString("limitTerm"));
				obj = new ShopLimitObject(buyShopId, buyItemId, buyCount, buyTime, limitTerm);
				
				limitUser = ACCOUNT_DATA.get(accountName);
				if (limitUser == null) {
					limitUser = new ShopLimitUser();
					ACCOUNT_DATA.put(accountName, limitUser);
				}
				limitUser.addLimit(obj);
			}
			SQLUtil.close(rs, pstm);
			
			// 구매 제한 정보 로드(캐릭터)
			pstm	= con.prepareStatement("SELECT * FROM character_shop_limit");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int characterId			= rs.getInt("characterId");
				int buyShopId			= rs.getInt("buyShopId");
				int buyItemId			= rs.getInt("buyItemId");
				int buyCount			= rs.getInt("buyCount");
				Timestamp buyTime		= rs.getTimestamp("buyTime");
				ShopLimitTerm limitTerm	= ShopLimitTerm.fromString(rs.getString("limitTerm"));
				obj = new ShopLimitObject(buyShopId, buyItemId, buyCount, buyTime, limitTerm);
				
				limitUser = CHARACTER_DATA.get(characterId);
				if (limitUser == null) {
					limitUser = new ShopLimitUser();
					CHARACTER_DATA.put(characterId, limitUser);
				}
				limitUser.addLimit(obj);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 제한아이템 구매 등록
	 * @param pc
	 * @param limit
	 * @param shopId
	 * @param item
	 */
	public void buyLimitItem(L1PcInstance pc, ShopLimitInformation limit, int shopId, L1ItemInstance item){
		ShopLimitObject limitObj	= null;
		ShopLimitUser limitUser		= null;
		switch (limit.getLimitType()) {
		case ACCOUNT:
			Account account = pc.getAccount();
			limitUser = account.getShopLimit();
			if (limitUser == null) {
				limitUser = new ShopLimitUser();
				ACCOUNT_DATA.put(account.getName(), limitUser);
				account.setShopLimit(limitUser);
			}
			limitObj = limitUser.getLimit(shopId, item.getItemId());
			if (limitObj == null) {
				limitObj = new ShopLimitObject(shopId, item.getItemId(), 1, new Timestamp(System.currentTimeMillis()), limit.getLimitTerm());
				limitUser.addLimit(limitObj);
			} else {
				limitObj.setBuyCount(limitObj.getBuyCount() + 1);
			}
			break;
		case CHARACTER:
			limitUser = pc.getShopLimit();
			if (limitUser == null) {
				limitUser = new ShopLimitUser();
				CHARACTER_DATA.put(pc.getId(), limitUser);
				pc.setShopLimit(limitUser);
			}
			limitObj = limitUser.getLimit(shopId, item.getItemId());
			if (limitObj == null) {
				limitObj = new ShopLimitObject(shopId, item.getItemId(), 1, new Timestamp(System.currentTimeMillis()), limit.getLimitTerm());
				limitUser.addLimit(limitObj);
			} else {
				limitObj.setBuyCount(limitObj.getBuyCount() + 1);
			}
			break;
		}
	}
	
	/**
	 * 제한 초기화
	 * @param pc
	 * @param obj
	 * @return boolean
	 */
	public boolean resetShopLimit(L1PcInstance pc, ShopLimitObject obj, ShopLimitType limitType){
		obj.setBuyCount(0);
		obj.getBuyTime().setTime(System.currentTimeMillis());
		switch (limitType) {
		case ACCOUNT:
			return updateShopLimitFromAccount(pc.getAccountName(), obj);
		case CHARACTER:
			return updateShopLimitFromCharacter(pc.getId(), obj);
		default:
			throw new Error(String.format("[ShopLimitLoader] RESET_UNDEFINED_LIMIT_TYPE : TYPE(%s), NAME(%s)", limitType.name(), pc.getName()));
		}
	}
	
	/**
	 * 계정의 제한 정보 업데이트
	 * @param account_name
	 * @param obj
	 * @return boolean
	 */
	boolean updateShopLimitFromAccount(String account_name, ShopLimitObject obj){
		Connection con						= null;
		PreparedStatement pstm				= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts_shop_limit SET buyCount=0, buyTime=NOW() WHERE accountName=? AND buyShopId=? AND buyItemId=?");
			pstm.setString(1, account_name);
			pstm.setInt(2, obj.getBuyShopId());
			pstm.setInt(3, obj.getBuyItemId());
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	/**
	 * 캐릭터의 제한 정보 업데이트
	 * @param characterId
	 * @param obj
	 * @return boolean
	 */
	boolean updateShopLimitFromCharacter(int characterId, ShopLimitObject obj){
		Connection con						= null;
		PreparedStatement pstm				= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE character_shop_limit SET buyCount=0, buyTime=NOW() WHERE characterId=? AND buyShopId=? AND buyItemId=?");
			pstm.setInt(1, characterId);
			pstm.setInt(2, obj.getBuyShopId());
			pstm.setInt(3, obj.getBuyItemId());
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	// 클라이언트 종료 Database갱신
	public void storeFromAccount(Account account){
		ShopLimitUser limitUser			= account.getShopLimit();
		if (limitUser == null) {
			return;
		}
		FastTable<ShopLimitObject> list	= limitUser.getLimitList();
		if (list == null || list.isEmpty()) {
			return;
		}
		Connection con							= null;
		PreparedStatement pstm					= null;
		ShopLimitObject obj				= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm	= con.prepareStatement("INSERT INTO accounts_shop_limit "
					+ "(accountName, buyShopId, buyItemId, buyCount, buyTime, limitTerm) VALUES (?,?,?,?,?,?) "
					+ "ON DUPLICATE KEY UPDATE "
					+ "buyCount = ?, buyTime = ?, limitTerm = ?");
			for (int i=0; i<list.size(); i++) {
				obj = list.get(i);
				int index = 0;
				pstm.setString(++index, account.getName());
				pstm.setInt(++index, obj.getBuyShopId());
				pstm.setInt(++index, obj.getBuyItemId());
				pstm.setInt(++index, obj.getBuyCount());
				pstm.setTimestamp(++index, obj.getBuyTime());
				pstm.setString(++index, obj.getLimitTerm().name());
				pstm.setInt(++index, obj.getBuyCount());
				pstm.setTimestamp(++index, obj.getBuyTime());
				pstm.setString(++index, obj.getLimitTerm().name());
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
				sqle.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	// 케릭터 logout Database갱신
	public void storeFromCharacter(L1PcInstance pc){
		ShopLimitUser limitUser		= pc.getShopLimit();
		if (limitUser == null) {
			return;
		}
		FastTable<ShopLimitObject> list	= limitUser.getLimitList();
		if (list == null || list.isEmpty()) {
			return;
		}
		Connection con							= null;
		PreparedStatement pstm					= null;
		ShopLimitObject obj				= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm	= con.prepareStatement("INSERT INTO character_shop_limit "
					+ "(characterId, buyShopId, buyItemId, buyCount, buyTime, limitTerm) VALUES (?,?,?,?,?,?) "
					+ "ON DUPLICATE KEY UPDATE "
					+ "buyCount = ?, buyTime = ?, limitTerm = ?");
			for (int i=0; i<list.size(); i++) {
				obj = list.get(i);
				int index = 0;
				pstm.setInt(++index, pc.getId());
				pstm.setInt(++index, obj.getBuyShopId());
				pstm.setInt(++index, obj.getBuyItemId());
				pstm.setInt(++index, obj.getBuyCount());
				pstm.setTimestamp(++index, obj.getBuyTime());
				pstm.setString(++index, obj.getLimitTerm().name());
				pstm.setInt(++index, obj.getBuyCount());
				pstm.setTimestamp(++index, obj.getBuyTime());
				pstm.setString(++index, obj.getLimitTerm().name());
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
				sqle.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}
	
}


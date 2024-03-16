package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

public class ShopTable {
	private static Logger _log = Logger.getLogger(ShopTable.class.getName());
	
	private final Map<Integer, L1Shop> _allShops		= new HashMap<Integer, L1Shop>();
	private final Map<Integer, L1ShopInfo> _shopInfos	= new HashMap<Integer, L1ShopInfo>();
	
	private static ShopTable _instance;
	public static ShopTable getInstance() {
		if (_instance == null) {
			_instance = new ShopTable();
		}
		return _instance;
	}

	private ShopTable() {
		loadShops();
	}
	
	public static enum ShopType {
		ITEM, BERRY, TAM, N_COIN, EIN_POINT, CLAN
	}
	
	private static ShopType getShopType(String str) {
		switch(str){
		case "item":	return ShopType.ITEM;
		case "berry":	return ShopType.BERRY;
		case "tam":		return ShopType.TAM;
		case "ncoin":	return ShopType.N_COIN;
		case "ein":		return ShopType.EIN_POINT;
		case "clan":	return ShopType.CLAN;
		default:		return null;
		}
	}
	
	public class L1ShopInfo {
		private int _npcId;
		private ShopType _type;
		private int _currencyId;
		private int _currencyDescId;
		
		public L1ShopInfo(ResultSet rs) throws SQLException {
			this._npcId				= rs.getInt("npcId");
			this._type				= getShopType(rs.getString("type"));
			this._currencyId		= rs.getInt("currencyId");
			this._currencyDescId	= rs.getInt("currencyDescId");
		}
		
		public int getNpcId() {
			return _npcId;
		}
		public ShopType getType() {
			return _type;
		}
		public int getCurrencyId() {
			return _currencyId;
		}
		public int getCurrencyDescId() {
			return _currencyDescId;
		}
	}
	
	public L1ShopInfo getShopInfo(int npcId){
		return _shopInfos.get(npcId);
	}
	
	private ArrayList<Integer> enumNpcIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT DISTINCT npc_id FROM shop");
			rs = pstm.executeQuery();
			while(rs.next()) {
				ids.add(rs.getInt("npc_id"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return ids;
	}

	private L1Shop loadShop(int npcId, ResultSet rs) throws SQLException {
		List<L1ShopItem> sellingList	= new ArrayList<L1ShopItem>();
		List<L1ShopItem> purchasingList	= new ArrayList<L1ShopItem>();
		L1ShopItem item					= null;
		while (rs.next()){
			int itemId			= rs.getInt("item_id");
			if ((!Config.ATTEND.ATTENDANCE_GROW_USE && (itemId == Config.ATTEND.ATTENDANCE_GROW_OPEN_ITEM_ID || itemId == 43301))
					|| (!Config.ATTEND.ATTENDANCE_DOMINATION_USE && (itemId == Config.ATTEND.ATTENDANCE_DOMINATION_OPEN_ITEM_ID || itemId == 43302))
					|| (!Config.ATTEND.ATTENDANCE_EXPLORER_USE && itemId == Config.ATTEND.ATTENDANCE_EXPLORER_OPEN_ITEM_ID)
					|| (!Config.ETC.EVENT_LEVEL_100 && itemId == 30940)) {
				continue;
			}
			int sellingPrice	= rs.getInt("selling_price");
			int purchasingPrice	= rs.getInt("purchasing_price");
			int packCount		= rs.getInt("pack_count");
			int enchant			= rs.getInt("enchant");
			eBloodPledgeRankType pledge_rank = eBloodPledgeRankType.fromString(rs.getString("pledge_rank"));
			packCount = packCount == 0 ? 1 : packCount;
			if (0 <= sellingPrice) {
				item = new L1ShopItem(itemId, sellingPrice, packCount, enchant, pledge_rank);
				sellingList.add(item);
			}
			if (0 <= purchasingPrice) {
				item = new L1ShopItem(itemId, purchasingPrice, packCount, enchant, pledge_rank);
				purchasingList.add(item);
			}
		}
		return new L1Shop(npcId, sellingList, purchasingList);
	}
	public void Reload(int npcid) {// 특정 NPC리로드
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM shop WHERE npc_id=? ORDER BY order_id");
			L1Shop shop = null;
			for (int npcId : enumNpcIds()) {
				if (npcId != npcid) {
					continue;
				}
				pstm.setInt(1, npcId);
				rs = pstm.executeQuery();
				shop = loadShop(npcId, rs);
				_allShops.put(npcId, shop);
				rs.close();
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	private void loadShops() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM shop WHERE npc_id=? ORDER BY order_id");
			L1Shop shop = null;
			// 70035 70041 70042 
			for (int npcId : enumNpcIds()) {
				if (npcId == 70035 || npcId == 70041 || npcId == 70042) {
					continue;
				}
				pstm.setInt(1, npcId);
				rs = pstm.executeQuery();
				shop = loadShop(npcId, rs);
				_allShops.put(npcId, shop);
				SQLUtil.close(rs);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM shop_info");
			rs = pstm.executeQuery();
			L1ShopInfo info = null;
			while (rs.next()) {
				info = new L1ShopInfo(rs);
				_shopInfos.put(info._npcId, info);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1Shop get(int npcId) {
		return _allShops.get(npcId);
	}
	/*버경 관련*/
	public void addShop(int npcId, L1Shop shop){		
		_allShops.put(npcId, shop);
	}

	/*버경 관련*/
	public void delShop(int npcId) {
		_allShops.remove(npcId);	
	}
	
	public static void reload(){
		ShopTable oldInstance = _instance;
		_instance = new ShopTable();
		oldInstance._allShops.clear();
		oldInstance._shopInfos.clear();
	}
}


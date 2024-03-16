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

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

public class NpcShopTable {
	private static Logger _log = Logger.getLogger(NpcShopTable.class.getName());

	private static NpcShopTable _instance;

	private final Map<Integer, L1Shop> _npcShops = new HashMap<Integer, L1Shop>();

	public static NpcShopTable getInstance() {
		if (_instance == null) {
			_instance = new NpcShopTable();
		}
		return _instance;
	}
	
	public L1Shop get(int npcId) {
		return _npcShops.get(npcId);
	}
	
	public  Map<Integer, L1Shop> get(){
		return _npcShops;
	}

	public static void reloding() {
		NpcShopTable oldInstance = _instance;
		_instance = new NpcShopTable();
		oldInstance._npcShops.clear();
		oldInstance = null;
	}

	private NpcShopTable() {
		loadShops();
	}

	private void loadShops() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM shop_npc WHERE npc_id=?");
			L1Shop shop = null;
			for (int npcId : enumNpcIds()) {
				pstm.setInt(1, npcId);
				rs = pstm.executeQuery();
				shop = loadShop(npcId, rs);
				_npcShops.put(npcId, shop);
				rs.close();
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "NpcShopTable[]Error1", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private ArrayList<Integer> enumNpcIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT DISTINCT npc_id FROM shop_npc");
			rs = pstm.executeQuery();
			while (rs.next()){
				ids.add(rs.getInt("npc_id"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "NpcShopTable[]Error", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return ids;
	}
	
	private L1Shop loadShop(int npcId, ResultSet rs) throws SQLException {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();
		L1ShopItem item = null;
		while(rs.next()){
			int itemId			= rs.getInt("item_id");
			int sellingPrice	= rs.getInt("selling_price");
			int purchasingPrice	= rs.getInt("purchasing_price");
			int count			= rs.getInt("count");
			int enchant			= rs.getInt("enchant");
			
			if (0 <= sellingPrice) {
				item = new L1ShopItem(itemId, sellingPrice, 1, enchant);
				item.setCount(count);
				sellingList.add(item);
			}
			if (0 <= purchasingPrice) {
				item = new L1ShopItem(itemId, purchasingPrice, 1, enchant);
				purchasingList.add(item);
			}
		}
		return new L1Shop(npcId, sellingList, purchasingList);
	}
}



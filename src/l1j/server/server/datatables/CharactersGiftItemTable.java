package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class CharactersGiftItemTable {

	private static CharactersGiftItemTable _instance;
	private HashMap<Integer, ArrayList<Item>> _itemList;

	public static CharactersGiftItemTable getInstance() {
		if (_instance == null) {
			_instance = new CharactersGiftItemTable();
		}
		return _instance;
	}

	public static void reload(){
		CharactersGiftItemTable oldInstance = _instance;
		_instance = new CharactersGiftItemTable();
		oldInstance._itemList.clear();
	}

	private CharactersGiftItemTable(){
		_itemList = new HashMap<Integer, ArrayList<Item>>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		// 아이템
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT level FROM levelup_quests_item GROUP BY level ORDER BY level");
			rs = pstm.executeQuery();
			while (rs.next()) {
				readItem(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	private void readItem(int level){
		ArrayList<Item> list = new ArrayList<CharactersGiftItemTable.Item>();
		// 아이템
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT type, item_name, item_id, count, enchant, attrlevel, bless FROM levelup_quests_item WHERE level=?");
			pstm.setInt(1, level);
			rs = pstm.executeQuery();
			while(rs.next()){
				Item item = new Item();
				item.type = rs.getInt(1);
				item.itemName = rs.getString(2);
				item.itemId = rs.getInt(3);
				item.count = rs.getInt(4);
				item.enchant = rs.getInt(5);
				item.attrLevel = rs.getInt(6);
				item.bless = rs.getInt(7);
				list.add(item);
			}
			_itemList.put(level, list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public Item[] getItems(int level){
		ArrayList<Item> result = _itemList.get(level);
		if (result == null) {
			return null;
		}
		return result.toArray(new Item[result.size()]);
	}

	public static class Item {
		private int type;
		private String itemName;
		private int itemId;
		private int count;
		private int enchant;
		private int attrLevel;
		private int bless;

		public int getType() {
			return type;
		}
		public void setType(int i) {
			type = i;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String Name) {
			itemName = Name;
		}
		public int getItemId() {
			return itemId;
		}
		public void setItemId(int Id) {
			itemId = Id;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int i) {
			count = i;
		}
		public int getEnchant() {
			return enchant;
		}
		public void setEnchant(int i) {
			enchant = i;
		}
		public int getAttrLevel() {
			return attrLevel;
		}
		public void setAttrLevel(int Level) {
			attrLevel = Level;
		}
		public int getBless() {
			return bless;
		}
		public void setBless(int i) {
			bless = i;
		}
	}
}

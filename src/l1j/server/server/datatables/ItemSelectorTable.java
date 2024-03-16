package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.inventory.S_ItemsNameIdInSelectionBagNoti;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

/**
 * 아이템 개봉형 선택 정보
 * @author LinOffice
 */
public class ItemSelectorTable {
	private static Logger _log = Logger.getLogger(ItemSelectorTable.class.getName());
	
	public class SelectorData {
		public int _itemId, _selectItemId, _count, _enchant, _attr, _bless, _limitTime;
		public boolean _delete;
		public L1Item _template;
		public SelectorData(ResultSet rs) throws SQLException {
			_itemId			= rs.getInt("itemId");
			_selectItemId	= rs.getInt("selectItemId");
			_count			= rs.getInt("count");
			_enchant		= rs.getInt("enchant");
			_attr			= rs.getInt("attr");
			_bless			= rs.getInt("bless");
			_limitTime		= rs.getInt("limitTime");
			_delete			= Boolean.valueOf(rs.getString("delete"));
			_template		= ItemTable.getInstance().getTemplate(_selectItemId, _bless);
			if (_template == null) {
				System.out.println(String.format("[ItemSelectorTable] SELECT_DATA_TEMPLATE_NOT_FOUND : ITEM_ID(%d), BLESS(%d)", _selectItemId, _bless));
			}
		}
	}
	
	public class SelectorWarehouseData {
		public int _itemId, _index, _selectItemId, _enchantLevel, _attrLevel;
		public L1ItemInstance _item;
		public SelectorWarehouseData(ResultSet rs) throws SQLException {
			_itemId			= rs.getInt("itemId");
			_index			= rs.getInt("index");
			_selectItemId	= rs.getInt("selectItemId");
			_enchantLevel	= rs.getInt("enchantLevel");
			_attrLevel		= rs.getInt("attrLevel");
			_item			= ItemTable.getInstance().createItem(_selectItemId);
			if (_item != null) {
				_item.setIdentified(true);
				_item.setEnchantLevel(_enchantLevel);
				_item.setAttrEnchantLevel(_attrLevel);
			}
		}
	}
	
	private static final FastMap<Integer, FastTable<SelectorData>> _dataMap					= new FastMap<Integer, FastTable<SelectorData>>();
	private static final FastMap<Integer, FastTable<SelectorWarehouseData>> _dataWareMap	= new FastMap<Integer, FastTable<SelectorWarehouseData>>();
	
	private static ItemSelectorTable _instance;
	public static ItemSelectorTable getInstance() {
		if (_instance == null) {
			_instance = new ItemSelectorTable();
		}
		return _instance;
	}
	
	public static boolean isSelectorInfo(int itemId){
		return _dataMap.containsKey(itemId);
	}
	
	public static FastTable<SelectorData> getSelectorInfo(int itemId){
		return _dataMap.containsKey(itemId) ? _dataMap.get(itemId) : null;
	}
	
	public static boolean isSelectorWareInfo(int itemId){
		return _dataWareMap.containsKey(itemId);
	}
	
	public static FastTable<SelectorWarehouseData> getSelectorWareInfo(int itemId){
		return _dataWareMap.containsKey(itemId) ? _dataWareMap.get(itemId) : null;
	}

	private ItemSelectorTable() {
		load();
	}

	private void load() {
		Connection con					= null;
		PreparedStatement pstm			= null;
		ResultSet rs					= null;
		SelectorData data				= null;
		SelectorWarehouseData dataWare	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM item_selector");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				data	= new SelectorData(rs);
				FastTable<SelectorData> list = _dataMap.get(data._itemId);
				if (list == null) {
					list = new FastTable<SelectorData>();
					_dataMap.put(data._itemId, list);
				}
				list.add(data);
			}
			SQLUtil.close(rs, pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM item_selector_warehouse");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				dataWare	= new SelectorWarehouseData(rs);
				FastTable<SelectorWarehouseData> list = _dataWareMap.get(dataWare._itemId);
				if (list == null) {
					list = new FastTable<SelectorWarehouseData>();
					_dataWareMap.put(dataWare._itemId, list);
				}
				list.add(dataWare);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ItemSelectorTable[]Error", e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ItemSelectorTable[]Error", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void reload() {
		for (FastTable<SelectorData> data : _dataMap.values()) {
			data.clear();
		}
		for (FastTable<SelectorWarehouseData> data : _dataWareMap.values()) {
			data.clear();
		}
		_dataMap.clear();
		_dataWareMap.clear();
		S_ItemsNameIdInSelectionBagNoti.reload();
		load();
	}
}


package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 아이템 버프 정보
 * @author LinOffice
 */
public class ItemBuffTable {
	private static Logger _log = Logger.getLogger(ItemBuffTable.class.getName());
	
	public class ItemBuff {
		private int _itemId;
		private int[] _skillIds;
		private boolean _delete;
		
		public ItemBuff(ResultSet rs) throws SQLException {
			_itemId = rs.getInt("item_id");
			if (ItemTable.getInstance().getTemplate(_itemId) == null) {
				System.out.println(String.format("[ItemBuff] ITEM_TEMP_NOT_FOUND : ITEM_ID(%d)", _itemId));
			}
			String[] array = rs.getString("skill_ids").split(StringUtil.CommaString);
			_skillIds = new int[array.length];
			for (int i=0; i<array.length; i++) {
				_skillIds[i] = Integer.parseInt(array[i].trim());
				if (SkillsTable.getTemplate(_skillIds[i]) == null) {
					System.out.println(String.format("[ItemBuff] SKILL_ID_NOT_FOUND : SKILL_ID(%d)", _skillIds[i]));
				}
			}
			_delete = Boolean.parseBoolean(rs.getString("delete"));
		}

		public int get_itemId() {
			return _itemId;
		}

		public int[] get_skillIds() {
			return _skillIds;
		}

		public boolean is_delete() {
			return _delete;
		}
	}
	
	private static final ConcurrentHashMap<Integer, ItemBuff> DATA = new ConcurrentHashMap<Integer, ItemBuff>();
	
	public static boolean isItemBuff(int itemId){
		return DATA.containsKey(itemId);
	}
	
	public static ItemBuff getItemBuff(int itemId){
		return DATA.get(itemId);
	}
	
	private static ItemBuffTable _instance;
	public static ItemBuffTable getInstance() {
		if (_instance == null) {
			_instance = new ItemBuffTable();
		}
		return _instance;
	}

	private ItemBuffTable() {
		load();
	}

	private void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		ItemBuff obj			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM item_buff");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				obj	= new ItemBuff(rs);
				DATA.put(obj._itemId, obj);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void reload() {
		DATA.clear();
		load();
	}
}


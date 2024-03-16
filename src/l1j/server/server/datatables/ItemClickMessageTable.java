package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 아이템 클릭 메세지 정보
 * @author LinOffice
 */
public class ItemClickMessageTable {
	private static Logger _log = Logger.getLogger(ItemClickMessageTable.class.getName());
	
	public static class ItemClickMessage {
		public int _itemId;
		public boolean _type;
		public String _msg;
		public boolean _delete;
	}
	
	private static final FastMap<Integer, ItemClickMessage> DATA = new FastMap<Integer, ItemClickMessage>();
	
	public static boolean isMessageItem(int itemId){
		return DATA.containsKey(itemId);
	}
	
	public static ItemClickMessage getData(int itemId){
		return DATA.get(itemId);
	}
	
	private static ItemClickMessageTable _instance;
	public static ItemClickMessageTable getInstance() {
		if (_instance == null) {
			_instance = new ItemClickMessageTable();
		}
		return _instance;
	}

	private ItemClickMessageTable() {
		load();
	}

	private void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		ItemClickMessage data	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM item_click_message");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				data			= new ItemClickMessage();
				data._itemId	= rs.getInt("itemId");
				data._type		= Boolean.valueOf(rs.getString("type"));
				data._msg		= rs.getString("msg");
				data._delete	= Boolean.valueOf(rs.getString("delete"));
				DATA.put(data._itemId, data);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ItemClickMessageTable[]Error", e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ItemClickMessageTable[]Error", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void reload() {
		DATA.clear();
		load();
	}
}


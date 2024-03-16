package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 인첸트 결과 알림 정보
 * @author LinOffice
 */
public class EnchantResultTable {
	private static final HashMap<Integer, EnchantResultData> DATA = new HashMap<>();
	public static EnchantResultData getData(int itemId){
		return DATA.get(itemId);
	}
	
	public static class EnchantResultData {
		private int item_id;
		private boolean color_item, bm_scroll;
		
		private EnchantResultData(ResultSet rs) throws SQLException{
			item_id			= rs.getInt("item_id");
			color_item		= Boolean.parseBoolean(rs.getString("color_item"));
			bm_scroll		= Boolean.parseBoolean(rs.getString("bm_scroll"));
		}
		
		public int get_item_id() {
			return item_id;
		}
		public boolean is_color_item() {
			return color_item;
		}
		public boolean is_bm_scroll() {
			return bm_scroll;
		}
	}
	
	private static EnchantResultTable _instance;
	public static EnchantResultTable getInstance(){
		if (_instance == null) {
			_instance = new EnchantResultTable();
		}
		return _instance;
	}
	
	private EnchantResultTable(){
		load();
	}
	
	private void load(){
		Connection con				=	null;
		PreparedStatement pstm		=	null;
		ResultSet rs				=	null;
		EnchantResultData enchant	=	null;
		try {
			con		=	L1DatabaseFactory.getInstance().getConnection();
			pstm	=	con.prepareStatement("SELECT * FROM enchant_result");
			rs		=	pstm.executeQuery();
			while(rs.next()){
				enchant	=	new EnchantResultData(rs);
				DATA.put(enchant.item_id, enchant);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void reload(){
		DATA.clear();
		_instance = null;
		_instance = new EnchantResultTable();
	}
}


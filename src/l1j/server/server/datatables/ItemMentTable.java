package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

/**
 * 아이템 월드 메세지(줍기, 드랍, 제작, 박스오픈)
 * @author LinOffice
 */
public class ItemMentTable {
	//public static final String DROP_MESSAGE		= "아덴월드의 어딘가 %s가(이) 드랍되었습니다.";
	//public static final String PICKUP_MESSAGE	= "아덴월드의 누군가 %s를(을) 획득하였습니다.";
	public static final String DROP_MESSAGE		= S_SystemMessage.getRefTextNS(44) + "%s " + S_SystemMessage.getRefTextNS(45);
	public static final String PICKUP_MESSAGE	= S_SystemMessage.getRefText(46) + "%s" + S_SystemMessage.getRefTextNS(47);

	private static ItemMentTable _instance;
	public static ItemMentTable getInstance(){
		if (_instance == null) {
			_instance = new ItemMentTable();
		}
		return _instance;
	}
	
	public static enum ItemMentType {
		PICK_UP, DROP, CRAFT, TREASURE_BOX
	}
	
	private static final FastMap<ItemMentType, FastTable<Integer>> DATA = new FastMap<ItemMentType, FastTable<Integer>>();
	
	public static boolean isMent(ItemMentType type, int itemId){
		if (!DATA.containsKey(type)) {
			return false;
		}
		return DATA.get(type).contains(itemId);
	}
	
	private ItemMentTable(){
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM item_ment ORDER BY itemId ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int itemId			= rs.getInt("itemId");
				ItemMentType type	= getMentType(rs.getString("mentType"));
				FastTable<Integer> list = DATA.get(type);
				if (list == null) {
					list = new FastTable<Integer>();
					DATA.put(type, list);
				}
				list.add(itemId);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private ItemMentType getMentType(String type){
		switch(type){
		case "pickup":		return ItemMentType.PICK_UP;
		case "drop":		return ItemMentType.DROP;
		case "craft":		return ItemMentType.CRAFT;
		case "treasurebox":	return ItemMentType.TREASURE_BOX;
		default:			return null;
		}
	}
	
	public void reload(){
		for (FastTable<Integer> list : DATA.values()) {
			list.clear();
		}
		DATA.clear();
		load();
	}
}


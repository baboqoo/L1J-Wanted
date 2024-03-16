package l1j.server.web.dispatcher.response.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.web.dispatcher.response.item.ItemDAO;
import l1j.server.web.dispatcher.response.item.ItemVO;

/**
 * 마이페이지 인벤토리
 * @author LinOffice
 */
public class CharacterInventoryDAO {
	private static CharacterInventoryDAO _instance;
	public static CharacterInventoryDAO getInstance() {
		if (_instance == null) {
			_instance = new CharacterInventoryDAO();
		}
		return _instance;
	}
	private CharacterInventoryDAO() {
	}
	
	private String createQuery(String key, ChracterInventoryType type) {
		StringBuilder sb = new StringBuilder();
	    sb.append("SELECT * FROM ");
	    switch(type) {
	    case INVENTOY:
	    	sb.append("character_items");
	    	break;
	    case NORMAL_WAREHOUSE:
	    	sb.append("character_warehouse");
	    	break;
	    case PACKAGE_WAREHOUSE:
	    	sb.append("character_package_warehouse");
	    	break;
	    }
	    sb.append(" WHERE ");
	    switch(type) {
	    case INVENTOY:
	    	sb.append("char_id=");
	    	break;
	    case NORMAL_WAREHOUSE:
	    case PACKAGE_WAREHOUSE:
	    	sb.append("account_name=");
	    	break;
	    }
	    sb.append("'").append(key).append("'");
	    return sb.toString();
	}
	
	public List<CharacterInventoryVO> getInventoryList(String accoutName, ChracterInventoryType type){
		List<CharacterInventoryVO> list	= new ArrayList<CharacterInventoryVO>();
		Connection con					= null;
	    PreparedStatement pstm			= null;
	    ResultSet rs					= null;
	    CharacterInventoryVO inv		= null;
	    try {
	    	con = L1DatabaseFactory.getInstance().getConnection();
	    	pstm = con.prepareStatement(createQuery(accoutName, type));
	    	rs = pstm.executeQuery();
	    	while(rs.next()) {
	    		int itemId	= rs.getInt("item_id");
	    		ItemVO item	= ItemDAO.getItemInfo(itemId);
	    		if (item == null) {
	    			continue;
	    		}
	    		inv			= new CharacterInventoryVO(rs.getInt("id"), item, rs.getInt("count"), rs.getInt("enchantlvl"), rs.getInt("attr_enchantlvl"), rs.getInt("bless"));
	    		list.add(inv);
	    	}
	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	    return list;
	}
	
	public List<CharacterInventoryVO> getInventoryList(int objId, ChracterInventoryType type){
		List<CharacterInventoryVO> list	= new ArrayList<CharacterInventoryVO>();
		Connection con					= null;
	    PreparedStatement pstm			= null;
	    ResultSet rs					= null;
	    CharacterInventoryVO inv		= null;
	    try {
	    	con = L1DatabaseFactory.getInstance().getConnection();
	    	pstm = con.prepareStatement(createQuery(String.valueOf(objId), type));
	    	rs = pstm.executeQuery();
	    	while(rs.next()) {
	    		int itemId	= rs.getInt("item_id");
	    		ItemVO item	= ItemDAO.getItemInfo(itemId);
	    		if (item == null) {
	    			continue;
	    		}
	    		inv			= new CharacterInventoryVO(rs.getInt("id"), item, rs.getInt("count"), rs.getInt("enchantlvl"), rs.getInt("attr_enchantlvl"), rs.getInt("bless"));
	    		list.add(inv);
	    	}
	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	    return list;
	}
	
	public boolean delete_user_inventory_item(int id) {
		Connection con					= null;
	    PreparedStatement pstm			= null;
	    try {
	    	con = L1DatabaseFactory.getInstance().getConnection();
	    	pstm = con.prepareStatement("DELETE FROM character_items WHERE id = ?");
	    	pstm.setInt(1, id);
	    	pstm.execute();
	    	return true;
	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	    return false;
	}
}


package l1j.server.web.dispatcher.response.alim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 알림 내역
 * @author LinOffice
 */
public class AlimDAO {
	private static AlimDAO _instance;
	public static AlimDAO getInstance() {
		if (_instance == null) {
			_instance = new AlimDAO();
		}
		return _instance;
	}
	
	private static final ConcurrentHashMap<String, java.util.LinkedList<AlimVO>> DATA = new ConcurrentHashMap<>();
	
	public static java.util.LinkedList<AlimVO> getList(String account_name){
		java.util.LinkedList<AlimVO> list = DATA.get(account_name);
		if (list == null) {
			list = new java.util.LinkedList<>();
			DATA.put(account_name, list);
		}
		return list;
	}
	
	private int _id;
	
	private AlimDAO() {
		load();
	}
	
	private void load() {
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    ResultSet rs			= null;
	    AlimVO vo				= null;
		try {
    		con		= L1DatabaseFactory.getInstance().getConnection();
    		
    		pstm	= con.prepareStatement("SELECT MAX(id) FROM app_alim_log");
    		rs		= pstm.executeQuery();
    		if (rs.next()) {
    			_id = rs.getInt(1);
    		}
    		SQLUtil.close(rs, pstm);
    		
    		pstm	= con.prepareStatement("SELECT * FROM app_alim_log WHERE status='false' AND insertTime BETWEEN DATE_SUB(NOW(), INTERVAL 6 MONTH) AND NOW() ORDER BY id DESC");
    		rs		= pstm.executeQuery();
    		while(rs.next()) {
    			vo = new AlimVO(rs.getInt("id"), rs.getString("account_name"), rs.getString("logContent"), rs.getInt("type"), 
    					rs.getTimestamp("insertTime"), Boolean.valueOf(rs.getString("status")));
    			
    			java.util.LinkedList<AlimVO> list = DATA.get(vo.getAccountName());
    			if (list == null) {
    				list = new java.util.LinkedList<>();
    				DATA.put(vo.getAccountName(), list);
    			}
    			list.add(vo);
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public boolean insert(AlimVO vo) {
		vo.setId(++_id);
		Connection con = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_alim_log SET id=?, account_name=?, logContent=?, type=?, insertTime=?, status=?");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setString(++index, vo.getAccountName());
    		pstm.setString(++index, vo.getLogContent());
    		pstm.setInt(++index, vo.getType());
    		pstm.setTimestamp(++index, vo.getInsertTime());
    		pstm.setString(++index, String.valueOf(vo.isStatus()));
    		if (pstm.executeUpdate() > 0) {
    			getList(vo.getAccountName()).addFirst(vo);
    			return true;
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}
	
	public boolean delete(AlimVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
	    try {
	    	con = L1DatabaseFactory.getInstance().getConnection();
	    	pstm = con.prepareStatement("UPDATE app_alim_log SET status='true' WHERE id=?");
	    	pstm.setInt(1, vo.getId());
	    	if (pstm.executeUpdate() > 0) {
	    		getList(vo.getAccountName()).remove(vo);
	    		return true;
	    	}
	    } catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public static void release() {
		for (java.util.LinkedList<AlimVO> val : DATA.values()) {
			val.clear();
		}
		DATA.clear();
		_instance = null;
	}
}


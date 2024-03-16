package l1j.server.web.dispatcher.response.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class SupportDAO {
	private static SupportDAO _instance;
	public static SupportDAO getInstance() {
		if (_instance == null) {
			_instance = new SupportDAO();
		}
		return _instance;
	}
	
	private static final java.util.LinkedList<SupportVO> DATA = new java.util.LinkedList<SupportVO>();
	private static final java.util.LinkedList<SupportBankRequestVO> REQUEST_DATA = new java.util.LinkedList<SupportBankRequestVO>();
	
	private static final ConcurrentHashMap<SupportMessageType, java.util.LinkedList<SupportMessageVO>> MESSAGE_DATA = new ConcurrentHashMap<>();
	
	public static java.util.LinkedList<SupportVO> getData() {
		return DATA;
	}
	
	public static java.util.LinkedList<SupportBankRequestVO> getRequestData() {
		return REQUEST_DATA;
	}
	
	public static java.util.LinkedList<SupportVO> getData(SupportStatus status) {
		java.util.LinkedList<SupportVO> result = null;
		for (SupportVO vo : DATA) {
			if (vo.getStatus() == status) {
				if (result == null) {
					result = new java.util.LinkedList<SupportVO>();
				}
				result.add(vo);
			}
		}
		return result;
	}
	
	public static SupportVO getSupport(int id) {
		for (SupportVO vo : DATA) {
			if (vo.getId() == id) {
				return vo;
			}
		}
		return null;
	}
	
	public static SupportBankRequestVO getSupportRequest(int id) {
		for (SupportBankRequestVO vo : REQUEST_DATA) {
			if (vo.getId() == id) {
				return vo;
			}
		}
		return null;
	}
	
	public static ConcurrentHashMap<SupportMessageType, java.util.LinkedList<SupportMessageVO>> getMessage() {
		return MESSAGE_DATA;
	}
	
	public static java.util.LinkedList<SupportMessageVO> getMessage(SupportMessageType type) {
		return MESSAGE_DATA.get(type);
	}
	
	private int _key;
	public int get_id() {
		return ++_key;
	}
	
	private int _req_key;
	public int get_req_id() {
		return ++_req_key;
	}
	
	private SupportDAO() {
		load();
	}
	
	void load() {
		Connection con				= null;
	    PreparedStatement pstm		= null;
	    ResultSet rs				= null;
	    SupportVO vo				= null;
	    SupportBankRequestVO req	= null;
	    SupportMessageVO msg		= null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		
    		pstm = con.prepareStatement("SELECT * FROM app_support ORDER BY write_date DESC");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			vo = new SupportVO(rs.getInt("id"), 
    					rs.getString("account_name"), 
    					rs.getString("character_name"), 
    					rs.getInt("pay_amount"), 
    					rs.getTimestamp("write_date"), 
    					SupportStatus.fromString(rs.getString("status")));
    			DATA.add(vo);
    			if (_key < vo.getId()) {
    				_key = vo.getId();
    			}
    		}
    		SQLUtil.close(rs, pstm);
    		
    		pstm = con.prepareStatement("SELECT * FROM app_support_request ORDER BY request_date DESC");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			req = new SupportBankRequestVO(rs.getInt("id"), 
    					rs.getString("account_name"), 
    					rs.getString("character_name"), 
    					rs.getTimestamp("request_date"), 
    					rs.getString("response"), 
    					rs.getTimestamp("response_date"));
    			REQUEST_DATA.add(req);
    			if (_req_key < req.getId()) {
    				_req_key = req.getId();
    			}
    		}
    		SQLUtil.close(rs, pstm);
    		
    		pstm = con.prepareStatement("SELECT * FROM app_support_message ORDER BY type, index_id");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			msg = new SupportMessageVO(SupportMessageType.fromString(rs.getString("type")), 
    					rs.getInt("index_id"), 
    					rs.getString("content"));
    			
    			java.util.LinkedList<SupportMessageVO> list = MESSAGE_DATA.get(msg.getType());
    			if (list == null) {
    				list = new java.util.LinkedList<>();
    				MESSAGE_DATA.put(msg.getType(), list);
    			}
    			list.add(msg);
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public boolean insert(SupportVO vo) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_support (id, account_name, character_name, pay_amount, write_date, status) VALUES (?,?,?,?,?,?)");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setString(++index, vo.getAccount_name());
    		pstm.setString(++index, vo.getCharacter_name());
    		pstm.setInt(++index, vo.getPay_amount());
    		pstm.setTimestamp(++index, vo.getWrite_date());
    		pstm.setString(++index, vo.getStatus().name());
    		if (pstm.executeUpdate() > 0) {
    			DATA.addFirst(vo);
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
	
	public boolean update(SupportVO vo) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_support SET status = ? WHERE id = ?");
    		pstm.setString(1, vo.getStatus().name());
    		pstm.setInt(2, vo.getId());
    		if (pstm.executeUpdate() > 0) {
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
	
	public boolean delete(SupportVO vo) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_support WHERE id = ?");
    		pstm.setInt(1, vo.getId());
    		if (pstm.executeUpdate() > 0) {
    			DATA.remove(vo);
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
	
	public boolean insertRequest(SupportBankRequestVO vo) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_support_request (id, account_name, character_name, request_date) VALUES (?,?,?,?)");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setString(++index, vo.getAccount_name());
    		pstm.setString(++index, vo.getCharacter_name());
    		pstm.setTimestamp(++index, vo.getRequest_date());
    		if (pstm.executeUpdate() > 0) {
    			REQUEST_DATA.addFirst(vo);
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
	
	public boolean updateRequest(SupportBankRequestVO vo) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_support_request SET response = ?, response_date = ? WHERE id = ?");
    		pstm.setString(1, vo.getResponse());
    		pstm.setTimestamp(2, vo.getResponse_date());
    		pstm.setInt(3, vo.getId());
    		if (pstm.executeUpdate() > 0) {
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
	
	public boolean deleteRequest(SupportBankRequestVO vo) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_support_request WHERE id = ?");
    		pstm.setInt(1, vo.getId());
    		if (pstm.executeUpdate() > 0) {
    			REQUEST_DATA.remove(vo);
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
	
	public static void reload() {
		release();
		_instance = new SupportDAO();
	}
	
	public static void release() {
		DATA.clear();
		REQUEST_DATA.clear();
		MESSAGE_DATA.clear();
	}
}


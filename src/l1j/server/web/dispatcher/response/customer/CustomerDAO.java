package l1j.server.web.dispatcher.response.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 고객센터
 * @author LinOffice
 */
public class CustomerDAO {
	private static CustomerDAO _instance;
	public static CustomerDAO getInstance() {
		if (_instance == null) {
			_instance = new CustomerDAO();
		}
		return _instance;
	}
	
	private Map<Integer, CustomerVO> _questionData;
	private Map<Integer, CustomerVO> _reportData;
	private List<CustomerNormalVO> _normalData;
	
	private int _id;
	
	private CustomerDAO() {
		_questionData	= new ConcurrentHashMap<Integer, CustomerVO>();
		_reportData		= new ConcurrentHashMap<Integer, CustomerVO>();
		_normalData		= new ArrayList<CustomerNormalVO>();
		load();
	}
	
	private void load() {
		Connection con = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		
    		// 6개월전 문의 삭제
    		pstm = con.prepareStatement("DELETE FROM app_customer WHERE date NOT BETWEEN DATE_SUB(NOW(), INTERVAL 6 MONTH) AND NOW()");
    		pstm.execute();
    		SQLUtil.close(pstm);
    		
    		pstm = con.prepareStatement("SELECT @RNUM := @RNUM + 1 AS ROWNUM, t.* FROM (SELECT id, login, type, title, content, status, date, comment, commentDate FROM app_customer WHERE type=1 ORDER BY date DESC) t, (SELECT @RNUM := 0) R");
    		rs = pstm.executeQuery();
    		CustomerVO question;
    		while(rs.next()) {
    			question = new CustomerVO(rs.getInt("id"), rs.getString("login"), rs.getInt("type"), rs.getString("title"), rs.getString("content"), rs.getString("status"), rs.getTimestamp("date"), rs.getString("comment"), rs.getTimestamp("commentDate"), rs.getInt("ROWNUM"));
    			_questionData.put(question.getRownum(), question);
    			if (question.getId() > _id) {
    				_id = question.getId();
    			}
    		}
    		SQLUtil.close(rs, pstm);
    		
    		pstm = con.prepareStatement("SELECT @RNUM := @RNUM + 1 AS ROWNUM, t.* FROM (SELECT id, login, type, title, content, status, date, comment, commentDate FROM app_customer WHERE type=2 ORDER BY date DESC) t, (SELECT @RNUM := 0) R");
    		rs = pstm.executeQuery();
    		CustomerVO report;
    		while(rs.next()) {
    			report = new CustomerVO(rs.getInt("id"), rs.getString("login"), rs.getInt("type"), rs.getString("title"), rs.getString("content"), rs.getString("status"), rs.getTimestamp("date"), rs.getString("comment"), rs.getTimestamp("commentDate"), rs.getInt("ROWNUM"));
    			_reportData.put(report.getRownum(), report);
    			if (report.getId() > _id) {
    				_id = report.getId();
    			}
    		}
    		SQLUtil.close(rs, pstm);
    		
    		pstm = con.prepareStatement("SELECT id, title, content FROM app_customer_normal ORDER BY id DESC");
    		rs = pstm.executeQuery();
    		CustomerNormalVO normal;
    		while(rs.next()) {
    			normal = new CustomerNormalVO(rs.getInt("id"), rs.getString("title"), rs.getString("content"));
    			_normalData.add(normal);
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public List<CustomerNormalVO> getNormalList(){
		return _normalData;
	}
	
	public CustomerNormalVO getNormal(int id) {
		CustomerNormalVO vo = null;
		for (CustomerNormalVO normal : _normalData) {
			if (normal.getId() == id) {
				vo = normal;
				break;
			}
		}
		return vo;
	}
	
	public Map<Integer, CustomerVO> getCustomerMap(int type){
		return (type == 1 ? _questionData : _reportData);
	}
	
	public ArrayList<CustomerVO> getCustomerData(int type, String account){
		ArrayList<CustomerVO> list = new ArrayList<CustomerVO>();
		if (type == 1) {
			for (CustomerVO vo : _questionData.values()) {
				if (vo.getLogin().equals(account)) {
					list.add(vo);
				}
			}
		} else {
			for (CustomerVO vo : _reportData.values()) {
				if (vo.getLogin().equals(account)) {
					list.add(vo);
				}
			}
		}
		return list;
	}
	
	public CustomerVO getCustomerInfo(int rownum, int type) {
    	if (type == 1) {
    		if (_questionData.containsKey(rownum)) {
    			return _questionData.get(rownum);
    		}
    	} else if (type == 2) {
    		if (_reportData.containsKey(rownum)) {
    			return _reportData.get(rownum);
    		}
    	}
    	return null;
    }
	
	public int getNextNum() {
    	return ++_id;
    }
	
	public int getNormalNextNum() {
		int num = 0;
		for (CustomerNormalVO vo : _normalData) {
			if (vo.getId() > num) {
				num = vo.getId();
			}
		}
		return num + 1;
	}
	
	public boolean insert(CustomerVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_customer (id, login, type, title, content, status, date, comment, commentDate) VALUES (?,?,?,?,?,?,?,?,?)");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setString(++index, vo.getLogin());
    		pstm.setInt(++index, vo.getType());
    		pstm.setString(++index, vo.getTitle());
    		pstm.setString(++index, vo.getContent());
    		pstm.setString(++index, vo.getStatus());
    		pstm.setTimestamp(++index, vo.getDate());
    		pstm.setString(++index, vo.getComment());
    		pstm.setTimestamp(++index, vo.getCommentDate());
    		if (pstm.executeUpdate() > 0) {
    			mapInsert(vo);
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public void mapInsert(CustomerVO vo) {
    	Map<Integer, CustomerVO> chage = new ConcurrentHashMap<>();
    	chage.put(1, vo);
    	if (vo.getType() == 1) {
    		for (int i=1; i<=_questionData.size(); i++) {
    			_questionData.get(i).setRownum(i+1);
        		chage.put(i+1, _questionData.get(i));
        		
        	}
    		_questionData.clear();
    		_questionData=null;
    		_questionData=chage;
    	} else if (vo.getType() == 2) {
    		for (int i=1; i<=_reportData.size(); i++) {
    			_reportData.get(i).setRownum(i+1);
        		chage.put(i+1, _reportData.get(i));
        		
        	}
    		_reportData.clear();
    		_reportData=null;
    		_reportData=chage;
    	}
    }
	
	public boolean updateComment(CustomerVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_customer SET status=?, comment=?, commentDate=? WHERE id=?");
    		pstm.setString(1, vo.getStatus());
    		pstm.setString(2, vo.getComment());
    		pstm.setTimestamp(3, vo.getCommentDate());
    		pstm.setInt(4, vo.getId());
    		if (pstm.executeUpdate() > 0) {
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean insertNormal(CustomerNormalVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_customer_normal SET id=?, title=?, content=?");
    		pstm.setInt(1, vo.getId());
    		pstm.setString(2, vo.getTitle());
    		pstm.setString(3, vo.getContent());
    		if (pstm.executeUpdate() > 0) {
    			_normalData.add(0, vo);
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean updateNormal(CustomerNormalVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_customer_normal SET title=?, content=? WHERE id=?");
    		pstm.setString(1, vo.getTitle());
    		pstm.setString(2, vo.getContent());
    		pstm.setInt(3, vo.getId());
    		if (pstm.executeUpdate() > 0) {
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean deleteNormal(CustomerNormalVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_customer_normal WHERE id=?");
    		pstm.setInt(1, vo.getId());
    		if (pstm.executeUpdate() > 0) {
    			_normalData.remove(vo);
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean delete(CustomerVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_customer WHERE id=?");
    		pstm.setInt(1, vo.getId());
    		if (pstm.executeUpdate() > 0) {
    			mapDelete(vo);
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public void mapDelete(CustomerVO vo) {
    	int deleRownum = vo.getRownum();
    	if (vo.getType() == 1) {
    		for (int i=1; i<=_questionData.size(); i++) {
        		if (i>deleRownum) {
        			_questionData.get(i).setRownum(i-1);
        			_questionData.put(i-1, _questionData.get(i));
        		}
        	}
    		_questionData.remove(_questionData.size());
    	} else if (vo.getType() == 2) {
    		for (int i=1; i<=_reportData.size(); i++) {
        		if (i>deleRownum) {
        			_reportData.get(i).setRownum(i-1);
        			_reportData.put(i-1, _reportData.get(i));
        		}
        	}
    		_reportData.remove(_reportData.size());
    	}
    }
	
	public static void release() {
		_instance._questionData.clear();
		_instance._reportData.clear();
		_instance._normalData.clear();
		_instance._questionData = null;
		_instance._reportData = null;
		_instance._normalData = null;
		_instance = null;
	}
}


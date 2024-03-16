package l1j.server.web.dispatcher.response.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.dispatcher.response.pitch.PitchDAO;
import l1j.server.web.dispatcher.response.trade.TradeDAO;

/**
 * 신고
 * @author LinOffice
 */
public class ReportDAO {
	private static ReportDAO _instance;
	public static ReportDAO getInstance() {
		if( _instance == null) {
			_instance = new ReportDAO();
		}
		return _instance;
	}
	private int _id;
	
	public LinkedList<ReportVO> _reportData;
	
	private ReportDAO() {
		_reportData = load();
	}
	
	private LinkedList<ReportVO> load() {
		LinkedList<ReportVO> reportData = new LinkedList<ReportVO>();
		Connection con = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT id, name, targetName, type, log, date FROM app_report ORDER BY date DESC");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			int id = rs.getInt("id");
    			String name = rs.getString("name");
    			String targetName = rs.getString("targetName");
    			ReportType type = ReportType.fromString(rs.getString("type"));
    			String log = rs.getString("log");
    			Timestamp date = rs.getTimestamp("date");
    			ReportVO vo = new ReportVO(id, name, targetName, type, log, date, get_object(log));
    			reportData.add(vo);
    			if (id > _id) {
    				_id = id;
    			}
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return reportData;
	}
	
	public int insert(ReportVO vo) {
		int result = 0;
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_report (id, name, targetName, type, log, date) VALUES (?,?,?,?,?,?)");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setString(++index, vo.getName());
    		pstm.setString(++index, vo.getTargetName());
    		pstm.setString(++index, vo.getType().get_desc());
    		pstm.setString(++index, vo.getLog());
    		pstm.setTimestamp(++index, vo.getDate());
    		result = pstm.executeUpdate();
    		_reportData.addFirst(vo);
    	} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return result;
	}
	
	public boolean delete(ReportVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_report WHERE id=?");
    		pstm.setInt(1, vo.getId());
    		if (pstm.executeUpdate() > 0) {
    			_reportData.remove(vo);
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
	
	public int nextId() {
		return ++_id;
	}
	
	public LinkedList<ReportVO> getData(){
		return _reportData;
	}
	
	public ReportVO getReport(int id) {
		for (ReportVO vo : _reportData) {
			if (vo.getId() == id) {
				return vo;
			}
		}
		return null;
	}
	
	public Object get_object(String log) {
		int id = Integer.parseInt(log.split("_")[2]);
		/*if (log.startsWith("게시글_ID_")) {
			return BoardDAO.getBoardFromId(id);
		}
		if (log.startsWith("거래소_ID_")) {
			return TradeDAO.getTradeFromId(id);
		}
		if (log.startsWith("홍보_ID_")) {
			return PitchDAO.getBoardFromId(id);
		}
		if (log.startsWith("컨텐츠공모_ID_")) {
			return ContentDAO.getBoardFromId(id);
		}*/
		if (log.startsWith("Post_ID_")) {
			return BoardDAO.getBoardFromId(id);
		}
		if (log.startsWith("Trade_ID_")) {
			return TradeDAO.getTradeFromId(id);
		}
		if (log.startsWith("Promotion_ID_")) {
			return PitchDAO.getBoardFromId(id);
		}
		if (log.startsWith("Contest_ID_")) {
			return ContentDAO.getBoardFromId(id);
		}
		
		return null;
	}
	
	public static void release() {
		_instance._reportData.clear();
		_instance._reportData = null;
		_instance = null;
	}
}


package l1j.server.server.templates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class L1BoardPost {
	private static Logger _log = Logger.getLogger(L1BoardPost.class.getName());
	private static final String LFCBOARD		= "board_app_lfc";	
	
	private final int _id;
	private final String _name;
	private final String _date;
	private final String _title;
	private final String _content;

	public int getId() {
		return _id;
	}
	public String getName() {
		return _name;
	}
	public String getDate() {
		return _date;
	}
	public String getTitle() {
		return _title;
	}
	public String getContent() {
		return _content;
	}
	
	private String today(String timeZoneID) {
		TimeZone tz = TimeZone.getTimeZone(timeZoneID);
		Calendar cal = Calendar.getInstance(tz);
		int year = cal.get(Calendar.YEAR) - 2000;
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);
		return String.format("%02d/%02d/%02d", year, month, date);
	}

	private L1BoardPost(int id, String name, String title, String content) {
		_id = id;
		_name = name;
		_date = today(Config.SERVER.TIME_ZONE);
		_title = title;
		_content = content;
	}

	private L1BoardPost(ResultSet rs) throws SQLException {
		_id = rs.getInt("id");
		_name = rs.getString("name");
		_date = rs.getString("date");
		_title = rs.getString("title");
		_content = rs.getString("content");
	}

	public synchronized static L1BoardPost create(String name, String title, String content) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT max(id) + 1 as newid FROM board_free");
			rs = pstm.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			pstm.close();
			L1BoardPost topic = new L1BoardPost(id, name, title, content);
			pstm = con.prepareStatement("INSERT INTO board_free SET id=?, name=?, date=?, title=?, content=?");
			pstm.setInt(1, topic.getId());
			pstm.setString(2, topic.getName());
			pstm.setString(3, topic.getDate());
			pstm.setString(4, topic.getTitle());
			pstm.setString(5, topic.getContent());
			pstm.execute();
			return topic;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	public synchronized static L1BoardPost createGM(String name, String title, String content) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT max(id) + 1 as newid FROM board_notice");
			rs = pstm.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			pstm.close();
			L1BoardPost topic = new L1BoardPost(id, name, title, content);
			pstm = con.prepareStatement("INSERT INTO board_notice SET id=?, name=?, date=?, title=?, content=?");
			pstm.setInt(1, topic.getId());
			pstm.setString(2, topic.getName());
			pstm.setString(3, topic.getDate());
			pstm.setString(4, topic.getTitle());
			pstm.setString(5, topic.getContent());
			pstm.execute();
			return topic;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	public synchronized static L1BoardPost createGM1(String name, String title, String content) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT max(id) + 1 as newid FROM board_notice1");
			rs = pstm.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			pstm.close();
			L1BoardPost topic = new L1BoardPost(id, name, title, content);
			pstm = con.prepareStatement("INSERT INTO board_notice1 SET id=?, name=?, date=?, title=?, content=?");
			pstm.setInt(1, topic.getId());
			pstm.setString(2, topic.getName());
			pstm.setString(3, topic.getDate());
			pstm.setString(4, topic.getTitle());
			pstm.setString(5, topic.getContent());
			pstm.execute();
			return topic;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	public synchronized static L1BoardPost createGM2(String name, String title, String content) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT max(id) + 1 as newid FROM board_notice2");
			rs = pstm.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			pstm.close();
			L1BoardPost topic = new L1BoardPost(id, name, title, content);
			pstm = con.prepareStatement("INSERT INTO board_notice2 SET id=?, name=?, date=?, title=?, content=?");
			pstm.setInt(1, topic.getId());
			pstm.setString(2, topic.getName());
			pstm.setString(3, topic.getDate());
			pstm.setString(4, topic.getTitle());
			pstm.setString(5, topic.getContent());
			pstm.execute();
			return topic;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	public synchronized static L1BoardPost createGM3(String name, String title, String content) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT max(id) + 1 as newid FROM board_notice3");
			rs = pstm.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			pstm.close();
			L1BoardPost topic = new L1BoardPost(id, name, title, content);
			pstm = con.prepareStatement("INSERT INTO board_notice3 SET id=?, name=?, date=?, title=?, content=?");
			pstm.setInt(1, topic.getId());
			pstm.setString(2, topic.getName());
			pstm.setString(3, topic.getDate());
			pstm.setString(4, topic.getTitle());
			pstm.setString(5, topic.getContent());
			pstm.execute();
			return topic;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}

	public synchronized static L1BoardPost createKey(String name, String title, String content) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT max(id) + 1 as newid FROM board_free_key");
			rs = pstm.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			pstm.close();
			L1BoardPost topic = new L1BoardPost(id, name, title, content);
			pstm = con.prepareStatement("INSERT INTO board_free_key SET id=?, name=?, date=?, title=?, content=?");
			pstm.setInt(1, topic.getId());
			pstm.setString(2, topic.getName());
			pstm.setString(3, topic.getDate());
			pstm.setString(4, topic.getTitle());
			pstm.setString(5, topic.getContent());
			pstm.execute();
			return topic;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
		
	public synchronized static L1BoardPost createFix(String name, String title, String content) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT max(id) + 1 as newid FROM board_posts_fix");
			rs = pstm.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			pstm.close();
			L1BoardPost topic = new L1BoardPost(id, name, title, content);
			pstm = con.prepareStatement("INSERT INTO board_posts_fix SET id=?, name=?, date=?, title=?, content=?");
			pstm.setInt(1, topic.getId());
			pstm.setString(2, topic.getName());
			pstm.setString(3, topic.getDate());
			pstm.setString(4, topic.getTitle());
			pstm.setString(5, topic.getContent());
			pstm.execute();
			return topic;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	
	public void freeBoard() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM board_free WHERE id=?");
			pstm.setInt(1, getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void serverInformation() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM board_notice WHERE id=?");
			pstm.setInt(1, getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void operator1() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM board_notice1 WHERE id=?");
			pstm.setInt(1, getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	public void operator2() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM board_notice2 WHERE id=?");
			pstm.setInt(1, getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	public void operator3() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM board_notice2 WHERE id=?");
			pstm.setInt(1, getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	public void suggestions() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM board_posts_fix WHERE id=?");
			pstm.setInt(1, getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public static L1BoardPost findById(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_free WHERE id='" + id + "'");
			rs = pstm.executeQuery();
			if(rs.next())return new L1BoardPost(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	public static L1BoardPost findByIdGM(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_notice WHERE id='" + id + "'");
			rs = pstm.executeQuery();
			if(rs.next())return new L1BoardPost(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	public static L1BoardPost findByIdGM1(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_notice1 WHERE id=?");
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			if(rs.next())return new L1BoardPost(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	public static L1BoardPost findByIdGM2(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_notice2 WHERE id=?");
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			if(rs.next())return new L1BoardPost(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	public static L1BoardPost findByIdGM3(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_notice3 WHERE id=?");
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			if(rs.next())return new L1BoardPost(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	public static L1BoardPost findByIdFix(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_posts_fix WHERE id=?");
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			if(rs.next())return new L1BoardPost(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}

	public static L1BoardPost findByIdKey(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_free_key WHERE id=?");
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			if(rs.next())return new L1BoardPost(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}

	private static PreparedStatement makeIndexStatement(Connection con, int id,int limit) throws SQLException {
		PreparedStatement result = null;
		int offset = 1;
		if(id == 0)result = con.prepareStatement("SELECT * FROM board_free ORDER BY id DESC LIMIT ?");
		else{
			result = con.prepareStatement("SELECT * FROM board_free WHERE id < ? ORDER BY id DESC LIMIT ?");
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}
	private static PreparedStatement makeIndexStatementGM(Connection con, int id,int limit) throws SQLException {
		PreparedStatement result = null;
		int offset = 1;
		if(id == 0)result = con.prepareStatement("SELECT * FROM board_notice ORDER BY id DESC LIMIT ?");
		else {
			result = con.prepareStatement("SELECT * FROM board_notice WHERE id < ? ORDER BY id DESC LIMIT ?");
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}
	private static PreparedStatement makeIndexStatementGM1(Connection con, int id,int limit) throws SQLException {
		PreparedStatement result = null;
		int offset = 1;
		if(id == 0)result = con.prepareStatement("SELECT * FROM board_notice1 ORDER BY id DESC LIMIT ?");
		else {
			result = con.prepareStatement("SELECT * FROM board_notice1 WHERE id < ? ORDER BY id DESC LIMIT ?");
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}
	private static PreparedStatement makeIndexStatementGM2(Connection con, int id,int limit) throws SQLException {
		PreparedStatement result = null;
		int offset = 1;
		if(id == 0)result = con.prepareStatement("SELECT * FROM board_notice2 ORDER BY id DESC LIMIT ?");
		else {
			result = con.prepareStatement("SELECT * FROM board_notice2 WHERE id < ? ORDER BY id DESC LIMIT ?");
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}
	private static PreparedStatement makeIndexStatementGM3(Connection con, int id,int limit) throws SQLException {
		PreparedStatement result = null;
		int offset = 1;
		if(id == 0)result = con.prepareStatement("SELECT * FROM board_notice3 ORDER BY id DESC LIMIT ?");
		else {
			result = con.prepareStatement("SELECT * FROM board_notice3 WHERE id < ? ORDER BY id DESC LIMIT ?");
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}

	private static PreparedStatement makeIndexStatementFix(Connection con, int id,int limit) throws SQLException {
		PreparedStatement result = null;
		int offset = 1;
		if(id == 0)result = con.prepareStatement("SELECT * FROM board_posts_fix ORDER BY id DESC LIMIT ?");
		else {
			result = con.prepareStatement("SELECT * FROM board_posts_fix WHERE id < ? ORDER BY id DESC LIMIT ?");
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}
	private static PreparedStatement makeIndexStatementKey(Connection con, int id,int limit) throws SQLException {
		PreparedStatement result = null;
		int offset = 1;
		if(id == 0)result = con.prepareStatement("SELECT * FROM board_free_key ORDER BY id DESC LIMIT ?");
		else {
			result = con.prepareStatement("SELECT * FROM board_free_key WHERE id < ? ORDER BY id DESC LIMIT ?");
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}
	
	

	public static List<L1BoardPost> index(int limit) {
		return index(0, limit);
	}

	public static List<L1BoardPost> index(int id, int limit) {
		List<L1BoardPost> result = new ArrayList<L1BoardPost>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatement(con, id, limit);
			rs = pstm.executeQuery();
			while(rs.next())result.add(new L1BoardPost(rs));
			return result;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	public static List<L1BoardPost> indexGM(int id, int limit) {
		List<L1BoardPost> result = new ArrayList<L1BoardPost>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatementGM(con, id, limit);
			rs = pstm.executeQuery();
			while(rs.next())result.add(new L1BoardPost(rs));
			return result;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	public static List<L1BoardPost> indexGM1(int id, int limit) {
		List<L1BoardPost> result = new ArrayList<L1BoardPost>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatementGM1(con, id, limit);
			rs = pstm.executeQuery();
			while(rs.next())result.add(new L1BoardPost(rs));
			return result;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	public static List<L1BoardPost> indexGM2(int id, int limit) {
		List<L1BoardPost> result = new ArrayList<L1BoardPost>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatementGM2(con, id, limit);
			rs = pstm.executeQuery();
			while(rs.next())result.add(new L1BoardPost(rs));
			return result;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	public static List<L1BoardPost> indexGM3(int id, int limit) {
		List<L1BoardPost> result = new ArrayList<L1BoardPost>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatementGM3(con, id, limit);
			rs = pstm.executeQuery();
			while(rs.next())result.add(new L1BoardPost(rs));
			return result;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	

	public static List<L1BoardPost> indexFix(int id, int limit) {
		List<L1BoardPost> result = new ArrayList<L1BoardPost>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatementFix(con, id, limit);
			rs = pstm.executeQuery();
			while(rs.next())result.add(new L1BoardPost(rs));
			return result;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	public static List<L1BoardPost> indexKey(int id, int limit) {
		List<L1BoardPost> result = new ArrayList<L1BoardPost>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatementKey(con, id, limit);
			rs = pstm.executeQuery();
			while(rs.next())result.add(new L1BoardPost(rs));
			return result;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	
	/** LFC **/
	private synchronized static L1BoardPost create(String name, String title, String content, String table){
		Connection con 			= null;
		PreparedStatement pstm	= null;
		ResultSet rs 			= null;
		StringBuilder sbQry 	= new StringBuilder();
		try {
			sbQry.append("SELECT max(id) + 1 as newid FROM ").append(table);
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sbQry.toString());
			rs = pstm.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			pstm.close();
			L1BoardPost topic = new L1BoardPost(id, name, title, content);
			sbQry = new StringBuilder();
			sbQry.append("INSERT INTO ").append(table).append(" SET id=?, name=?, date=?, title=?, content=?");
			pstm = con.prepareStatement(sbQry.toString());
			pstm.setInt(1, topic.getId());
			pstm.setString(2, topic.getName());
			pstm.setString(3, topic.getDate());
			pstm.setString(4, topic.getTitle());
			pstm.setString(5, topic.getContent());
			pstm.execute();
			return topic;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	private static void delData(String name, String table){
		Connection con = null;
		PreparedStatement pstm = null;
		StringBuilder sbQry = new StringBuilder();
		try {
			sbQry.append("DELETE FROM ").append(table).append(" WHERE name=?");
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sbQry.toString());
			pstm.setString(1, name);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void delData(int id, String table){
		Connection con = null;
		PreparedStatement pstm = null;
		StringBuilder sbQry = new StringBuilder();
		try {
			sbQry.append("DELETE FROM ").append(table).append(" WHERE id=?");
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sbQry.toString());
			pstm.setInt(1, id);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private static L1BoardPost findById(int id, String table){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		StringBuilder sbQry = new StringBuilder();
		try {
			sbQry.append("SELECT * FROM ").append(table).append(" WHERE id=?");
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sbQry.toString());
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			if(rs.next())return new L1BoardPost(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	public static List<L1BoardPost> index(int id, int limit, String table){
		List<L1BoardPost> result = new ArrayList<L1BoardPost>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatement(con, id, limit, table);
			rs = pstm.executeQuery();
			while(rs.next())result.add(new L1BoardPost(rs));
			return result;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	private static PreparedStatement makeIndexStatement(Connection con, int id, int limit, String table) throws SQLException{
		PreparedStatement result = null;
		int offset = 1;
		StringBuilder sbQry = new StringBuilder();
		sbQry.append("SELECT * FROM ").append(table);
		if(id == 0){
			sbQry.append(" ORDER BY id DESC LIMIT ?");
			result = con.prepareStatement(sbQry.toString());
		}else{
			sbQry.append(" WHERE id < ? ORDER BY id DESC LIMIT ?");
			result = con.prepareStatement(sbQry.toString());
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}
	
	public static L1BoardPost createLfc(String name, String title, String content){
		return create(name, title, content, LFCBOARD);
	}
	
	public static void delLfc(String name){
		delData(name, LFCBOARD);
	}
	public static void delLfc(int id){
		delData(id, LFCBOARD);
	}
	
	public static L1BoardPost findByIdLfc(int id){
		return findById(id, LFCBOARD);
	}
	
	public static List<L1BoardPost> indexLfc(int id, int limit){
		return index(id, limit, LFCBOARD);	
	}
}


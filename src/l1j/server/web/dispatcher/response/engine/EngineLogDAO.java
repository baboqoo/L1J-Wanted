package l1j.server.web.dispatcher.response.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 불허가 엔진
 * @author LinOffice
 */
public class EngineLogDAO {
	private static EngineLogDAO _instance;
	public static EngineLogDAO getInstance(){
		if (_instance == null) {
			_instance = new EngineLogDAO();
		}
		return _instance;
	}
	
	private static final FastTable<EngineLogVO> DATA = new FastTable<EngineLogVO>();
	
	public static String getLogAll(){
		StringBuilder sb = new StringBuilder();
		sb.append("===== [EngineLog] =====\n");
		for (EngineLogVO log : DATA) {
			sb.append(log.toString()).append("\n");
		}
		sb.append("===== [END] =====\n");
		return sb.toString();
	}
	
	public static String getLog(String accountName){
		StringBuilder sb = new StringBuilder();
		sb.append("===== [EngineLog] =====\n");
		for (EngineLogVO log : DATA) {
			if (log.getAccount().equalsIgnoreCase(accountName)) {
				sb.append(log.toString()).append("\n");
			}
		}
		sb.append("===== [END] =====\n");
		return sb.toString();
	}
	
	private EngineLogDAO(){
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		EngineLogVO vo			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM app_engine_log ORDER BY id DESC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				vo	= new EngineLogVO(rs.getString("account"), rs.getString("engine"), rs.getTimestamp("time"));
				DATA.add(vo);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public boolean insert(EngineLogVO vo){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("INSERT INTO app_engine_log (id, account, engine, time) VALUES (((SELECT IFNULL(MAX(B.id), 0) FROM app_engine_log B) + 1), ?, ?, NOW())");
			pstm.setString(1, vo.getAccount());
			pstm.setString(2, vo.getEngine());
			if(pstm.executeUpdate() > 0){
				DATA.add(vo);
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
		return false;
	}
}


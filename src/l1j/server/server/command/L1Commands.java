package l1j.server.server.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Command;
import l1j.server.server.utils.SQLUtil;

public class L1Commands {
	private static Logger _log = Logger.getLogger(L1Commands.class.getName());
	private static final FastMap<String, L1Command> COMMAND_DATA = new FastMap<String, L1Command>();
	
	private static L1Commands _instance;
	public static L1Commands getInstance(){
		if (_instance == null) {
			_instance = new L1Commands();
		}
		return _instance;
	}
	
	public static L1Command get(String name) {
		return COMMAND_DATA.containsKey(name) ? COMMAND_DATA.get(name) : null;
	}
	
	public static FastMap<String, L1Command> getCommnads(){
		return COMMAND_DATA;
	}
	
	private L1Commands(){
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1Command command		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM commands ORDER BY name ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				command			= new L1Command(rs);
				COMMAND_DATA.put(command.getName(), command);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//_log.log(Level.SEVERE, "커멘드 취득 에러", e);
			_log.log(Level.SEVERE, "Command acquisition error", e);
		} catch(Exception e){
			e.printStackTrace();
			//_log.log(Level.SEVERE, "커멘드 취득 에러", e);
			_log.log(Level.SEVERE, "Command acquisition error", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}


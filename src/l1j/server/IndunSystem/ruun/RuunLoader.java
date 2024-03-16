package l1j.server.IndunSystem.ruun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class RuunLoader {
	private static RuunLoader _instance;
	public static RuunLoader getInstance(){
		if (_instance == null) {
			_instance = new RuunLoader();
		}
		return _instance;
	}
	private FastTable<RuunSpawnObject> _ruunSpawnInfo;
	protected FastTable<RuunSpawnObject> getRuunData(){
		return _ruunSpawnInfo;
	}
	
	private RuunLoader(){
		_ruunSpawnInfo = load();
	}
	
	private FastTable<RuunSpawnObject> load(){
		FastTable<RuunSpawnObject> list = new FastTable<>();
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM spawnlist_ruun");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				list.add(new RuunSpawnObject(rs));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return list;
	}
	
	public static void reload(){
		RuunLoader oldInstance = _instance;
		_instance = new RuunLoader();
		oldInstance._ruunSpawnInfo.clear();
		oldInstance._ruunSpawnInfo = null;
		oldInstance = null;
	}
}


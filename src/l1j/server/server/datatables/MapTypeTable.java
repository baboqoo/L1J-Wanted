package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.utils.SQLUtil;

/**
 * 맵 타입 지정(custom)
 * @author LinOffice
 */
public class MapTypeTable {
	private static MapTypeTable _instance;
	public static MapTypeTable getInstance(){
		if (_instance == null) {
			_instance = new MapTypeTable();
		}
		return _instance;
	}
	
	private static final ConcurrentHashMap<Integer, L1RegionStatus> DATA = new ConcurrentHashMap<Integer, L1RegionStatus>();
	
	public static L1RegionStatus getRegion(int mapId){
		return DATA.get(mapId);
	}
	
	private MapTypeTable(){
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT mapId, type FROM map_type");
			rs		= pstm.executeQuery();
			while(rs.next()){
				DATA.put(rs.getInt("mapId"), L1RegionStatus.fromString(rs.getString("type")));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void reload(){
		DATA.clear();
		load();
	}
}


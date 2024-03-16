package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 인터서버 인형레이스 텔레포트 지역 정보
 * @author LinOffice
 */
public class InterRaceRegionTable {
	private static InterRaceRegionTable _instance;

	private static final ArrayList<String> LOC_KEY = new ArrayList<String>();
	
	/**
	 * 지역 조사
	 * @param key
	 * @return boolean
	 */
	public static boolean isLockey(String key) {
		return LOC_KEY.contains(key);
	}

	public static InterRaceRegionTable getInstance() {
		if (_instance == null) {
			_instance = new InterRaceRegionTable();
		}
		return _instance;
	}
	
	private InterRaceRegionTable() {
		load();
	}

	private void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM inter_race_region");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int srcX		= rs.getInt("loc_X");
				int srcY		= rs.getInt("loc_Y");
				int srcMapId	= rs.getInt("loc_mapId");
				String key = new StringBuilder().append(srcMapId).append(srcX).append(srcY).toString();
				LOC_KEY.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}


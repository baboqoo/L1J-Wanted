package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class WarTimeTable {
	private static WarTimeTable _instance;
	public static WarTimeTable getInstance(){
		if (_instance == null) {
			_instance = new WarTimeTable();
		}
		return _instance;
	}
	
	public class WarTimeData {
		public int _castleId;
		public FastTable<WarTimeInfo> _times;
		
		public WarTimeData(int _castleId, FastTable<WarTimeInfo> _times) {
			this._castleId	= _castleId;
			this._times		= _times;
		}
	}
	
	public class WarTimeInfo {
		public int _day, _hour, _minute;
		
		public WarTimeInfo(int _day, int _hour, int _minute) {
			this._day		= _day;
			this._hour		= _hour;
			this._minute	= _minute;
		}
	}
	
	private static final FastTable<WarTimeData> DATA = new FastTable<WarTimeData>();
	
	public static FastTable<WarTimeData> getWarTimeDatas(){
		return DATA;
	}
	
	private WarTimeData getWarTime(int castleId){
		for (WarTimeData data : DATA) {
			if (data._castleId == castleId) {
				return data;
			}
		}
		return null;
	}
	
	private WarTimeTable(){
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		WarTimeData data		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM war_time ORDER BY castleId ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int castleId	= rs.getInt("castleId");
				data = getWarTime(castleId);
				if (data == null) {
					data = new WarTimeData(castleId, new FastTable<WarTimeInfo>());
					DATA.add(data);
				}
				data._times.add(new WarTimeInfo(getDay(rs.getString("day")), Integer.parseInt(rs.getString("hour")), Integer.parseInt(rs.getString("minute"))));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private int getDay(String day) {
		for (int i=0; i<CommonUtil.WEEK_DAY_ARRAY.length; i++) {
			if (day.equals(CommonUtil.WEEK_DAY_ARRAY[i])) {
				return i;
			}
		}
		return 0;
	}
	
	public void reload(){
		for (WarTimeData data : DATA) {
			data._times.clear();
		}
		DATA.clear();
		load();
	}
}


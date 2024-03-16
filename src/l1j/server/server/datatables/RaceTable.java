package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Racer;
import l1j.server.server.utils.SQLUtil;

public class RaceTable{
	private static RaceTable _instance;
	private HashMap<Integer, L1Racer> _namelist;

	public static RaceTable getInstance(){
		if (_instance == null) {
			_instance = new RaceTable();
		}
		return _instance;
	}

	public RaceTable(){
		_namelist = new HashMap<Integer, L1Racer>();
		bnl();
	}
	
	private void bnl(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM util_racer");
			rs = pstm.executeQuery();
			BadnameTable(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void BadnameTable(ResultSet Data) throws Exception{
		L1Racer name = null;;
		while(Data.next()){
			name = new L1Racer();
			name.setNum(Data.getInt(1));
			name.setWinCount(Data.getInt(2));
			name.setLoseCount(Data.getInt(3));
			
			_namelist.put(name.getNum(), name);
		}
		//Data.close();
//		System.out.println("[::::::] util_racer: "+_namelist.size()+"개의 정보가 로드되었습니다.");
//                eros.tarea.append("\n[::::::] util_racer: "+_namelist.size()+"개의 정보가 로드되었습니다.");
	}

	public L1Racer getTemplate(int name){
		return (L1Racer) _namelist.get(new Integer(name));
	}

}


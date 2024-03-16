package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1DogFight;
import l1j.server.server.utils.SQLUtil;

public class DogFightTable {
	private static DogFightTable _instance;
	private HashMap<Integer, L1DogFight> _namelist;

	public static DogFightTable getInstance() {
		if (_instance == null) {
			_instance = new DogFightTable();
		}
		return _instance;
	}

	public DogFightTable() {
		_namelist = new HashMap<Integer, L1DogFight>();
		bnl();
	}

	private void bnl() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM util_fighter");
			rs = pstm.executeQuery();
			BadnameTable(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void BadnameTable(ResultSet Data) throws Exception {
		L1DogFight name = null;
		while(Data.next()){
			name = new L1DogFight();
			name.setNum(Data.getInt(1));
			name.setWinCount(Data.getInt(2));
			name.setLoseCount(Data.getInt(3));
			_namelist.put(name.getNum(), name);
		}
	}

	public L1DogFight getTemplate(int name) {
		return _namelist.get(new Integer(name));
	}

}


package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OutputRawString;
import l1j.server.server.utils.SQLUtil;

public class ServerExplainTable {
	private static Logger _log = Logger.getLogger(ServerExplainTable.class.getName());

	private static ServerExplainTable _instance;
	public static ServerExplainTable getInstance() {
		if (_instance == null) {
			_instance = new ServerExplainTable();
		}
		return _instance;
	}
	
	private static final FastMap<Integer, ServerExplain> DATA = new FastMap<>();
	
	protected class ServerExplain {
		protected int _num;
		protected String _title, _content;
		protected ServerExplain(ResultSet rs) throws SQLException {
			_num		= rs.getInt("num");
			_title		= rs.getString("subject");
			_content	= rs.getString("content");
		}
	}
	
	/**
	 * ■■■ 서버 설명에 관한 명령어 ■■■<br>
	 * 
	 * @param pc
	 * @param num
	 *            - 테이블의 해당 번호
	 */
	public void explain(L1PcInstance pc, int num) {
		ServerExplain explain = DATA.get(num);
		if (explain == null) {
			return;
		}
		pc.sendPackets(new S_OutputRawString(pc.getId(), explain._title, explain._content), true);
	}
	
	public void explain(L1PcInstance pc, String html, int num) {
		ServerExplain explain = DATA.get(num);
		if (explain == null) {
			return;
		}
		pc.sendPackets(new S_OutputRawString(pc.getId(), html, explain._title, explain._content), true);
	}
	
	private ServerExplainTable(){
		load();
	}
	
	private void load(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM server_explain");
			rs = pstm.executeQuery();
			ServerExplain explain = null;
			while(rs.next()){
				explain = new ServerExplain(rs);
				DATA.put(explain._num, explain);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void reload(){
		DATA.clear();
		load();
	}

}

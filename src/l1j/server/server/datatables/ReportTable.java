package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class ReportTable {
	private static Logger _log = Logger.getLogger(ReportTable.class.getName());
	
	private static ReportTable _instance;
	public static ReportTable getInstance() {
		if (_instance == null) {
			_instance = new ReportTable();
		}
		return _instance;
	}
	public ReportTable() {
		load();
	}

	private class ReportModel {
		private String target, reporter;
	}
	
	private static final FastTable<ReportModel> LIST = new FastTable<ReportModel>();
	private boolean countainReport(L1PcInstance target, L1PcInstance reporter){
		for (ReportModel model : LIST) {
			if (model.target.equals(target.getName()) && model.reporter.equals(reporter.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public void report(L1PcInstance reporter, L1PcInstance target){
		if (target.isGm()) {
			return;
		}
		if (!reporter.isReport()) {
			reporter.sendPackets(L1ServerMessage.sm1021);// 잠시후 다시 신고 해주세요.
			return;
		}
		if (countainReport(target, reporter)) {
			reporter.sendPackets(L1ServerMessage.sm1020);// 신고: 이미 신고 된 대상
			return;
		}
		
		ReportModel model	= new ReportModel();
		model.target		= target.getName();
		model.reporter		= reporter.getName();
		if (insert(model)) {
			reporter.startReportDeley();
			reporter.sendPackets(L1ServerMessage.sm1019);
			for (L1PcInstance gm : L1World.getInstance().getAllGms()) {
				//gm.sendPackets(new S_SystemMessage(String.format("신고알림 : %s (신고자 : %s)", target.getName(), reporter.getName())), true);
				gm.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(87), target.getName(), reporter.getName()), true);
			}
		}
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		ReportModel model		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT target, reporter FROM report");
			rs		= pstm.executeQuery();
			while(rs.next()){
				model			= new ReportModel();
				model.target	= rs.getString("target");
				model.reporter	= rs.getString("reporter");
				LIST.add(model);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private boolean insert(ReportModel model) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO report (target, reporter, count, date) VALUES (?, ?, 1, NOW()) ON DUPLICATE KEY UPDATE count=count+1, date=NOW()");
			pstm.setString(1, model.target);
			pstm.setString(2, model.reporter);
			if (pstm.executeUpdate() > 0) {
				LIST.add(model);
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
}



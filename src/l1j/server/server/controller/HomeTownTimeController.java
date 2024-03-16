package l1j.server.server.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.GameTime;
import l1j.server.server.model.gametime.GameTimeAdapter;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.SQLUtil;

public class HomeTownTimeController {
	private static Logger _log = Logger.getLogger(HomeTownTimeController.class.getName());

	private static HomeTownTimeController _instance;
	
	private static L1TownFixedProcListener _listener;

	public static HomeTownTimeController getInstance() {
		if (_instance == null) {
			_instance = new HomeTownTimeController();
		}
		return _instance;
	}

	private HomeTownTimeController() {
		startListener();
	}

	private void startListener() {
		if (_listener == null) {
			_listener = new L1TownFixedProcListener();
			GameTimeClock.getInstance().addListener(_listener);
		}
	}

	private class L1TownFixedProcListener extends GameTimeAdapter {
		@Override
		public void onDayChanged(BaseTime time) {
			fixedProc((GameTime) time);
		}
	}

	private void fixedProc(GameTime time) {
		Calendar cal = time.getCalendar();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day == 25) {
			monthlyProc();
		} else {
			dailyProc();
		}
	}

	public void dailyProc() {
		//_log.info("홈 타운 시스템：일시 처리 개시");
		_log.info("Home Town System: Temporary Processing Start");
		TownTable.getInstance().updateTaxRate();
		TownTable.getInstance().updateSalesMoneyYesterday();
		TownTable.getInstance().load();
	}

	public void monthlyProc() {
		//_log.info("홈 타운 시스템：월시 처리 개시");
		_log.info("Home Town System: Temporary Processing Started");
		GameServerSetting.PROCESSING_CONTRIBUTION_TOTAL = true;

		for (int townId = 1; townId <= 10; townId++) {
			String leaderName = totalContribution(townId);
			if (leaderName != null) {
				S_PacketBox packet = new S_PacketBox(S_PacketBox.MSG_TOWN_LEADER, leaderName);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc.getHomeTownId() == townId) {
						pc.setContribution(0);
						pc.sendPackets(packet, true);
					}
				}
			}
		}
		TownTable.getInstance().load();

		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getHomeTownId() == -1) {
				pc.setHomeTownId(0);
			}
			pc.setContribution(0);
			try {
				// DB에 캐릭터 정보를 기입한다
				pc.save();
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		clearHomeTownID();
		GameServerSetting.PROCESSING_CONTRIBUTION_TOTAL = false;
	}

	private static String totalContribution(int townId) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int leaderId = 0;
		String leaderName = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT objid, char_name FROM characters WHERE HomeTownID = ? AND Contribution > 0 ORDER BY Contribution DESC LIMIT 1");
			pstm.setInt(1, townId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				leaderId = rs.getInt("objid");
				leaderName = rs.getString("char_name");
			}
			SQLUtil.close(rs, pstm);
			
			double totalContribution = 0;
			pstm = con.prepareStatement("SELECT SUM(Contribution) AS TotalContribution FROM characters WHERE HomeTownID = ?");
			pstm.setInt(1, townId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				totalContribution = rs.getInt("TotalContribution");
			}
			SQLUtil.close(rs, pstm);
			
			double townFixTax = 0;
			pstm = con.prepareStatement("SELECT town_fix_tax FROM town WHERE town_id = ?");
			pstm.setInt(1, townId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				townFixTax = rs.getInt("town_fix_tax");
			}
			SQLUtil.close(rs, pstm);
			
			double contributionUnit = 0;
			if (totalContribution != 0) {
				contributionUnit = Math.floor(townFixTax / totalContribution * 100) / 100;
			}
			pstm = con.prepareStatement("UPDATE characters SET Contribution = 0, Pay = Contribution * ? WHERE HomeTownID = ? AND Contribution > 0");
			pstm.setDouble(1, contributionUnit);
			pstm.setInt(2, townId);
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("UPDATE town SET leader_id = ?, leader_name = ?, tax_rate = 0, tax_rate_reserved = 0, sales_money = 0, sales_money_yesterday = sales_money, town_tax = 0, town_fix_tax = 0 WHERE town_id = ?");
			pstm.setInt(1, leaderId);
			pstm.setString(2, leaderName);
			pstm.setInt(3, townId);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		return leaderName;
	}

	private static void clearHomeTownID() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET HomeTownID = 0 WHERE HomeTownID = -1");
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 보수를 취득해 클리어 한다
	 * 
	 * @return 보수
	 */
	public static int getPay(int objid) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int pay = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT Pay FROM characters WHERE objid = ? FOR UPDATE");// session update other lock
			pstm.setInt(1, objid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				pay = rs.getInt("Pay");
			}
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("UPDATE characters SET Pay = 0 WHERE objid = ?");
			pstm.setInt(1, objid);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return pay;
	}
}


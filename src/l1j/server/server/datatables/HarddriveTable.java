package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 하드디스크 벤 정보
 * @author LinOffice
 */
public class HarddriveTable {
	private static Logger _log = Logger.getLogger(HarddriveTable.class.getName());
	
	public class BanHarddrive {
		private String number;
		private String account;
		private Timestamp registTime;
		
		public BanHarddrive(String number, String account, Timestamp registTime) {
			this.number = number;
			this.account = account;
			this.registTime = registTime;
		}

		public String getNumber() {
			return number;
		}
		public String getAccount() {
			return account;
		}
		public Timestamp getRegistTime() {
			return registTime;
		}
		
		@Override
		public String toString() {
			return new StringBuilder().append("ACCOUNT: ").append(account).append(StringUtil.LineString)
					.append("NUMBER: ").append(number).append(StringUtil.LineString)
					.append("REGISTTIME: ").append(registTime.toString()).toString();
		}
	}
	
	private static final ConcurrentHashMap<String, BanHarddrive> DATA = new ConcurrentHashMap<>();
	private static HarddriveTable _instance;
	public static HarddriveTable getInstance() {
		if (_instance == null) {
			_instance = new HarddriveTable();
		}
		return _instance;
	}

	public static boolean isBan(String hdd) {
		return DATA.containsKey(hdd);
	}
	
	public static BanHarddrive getHardDiskBan(String account){
		if (DATA.isEmpty()) {
			return null;
		}
		BanHarddrive ban = null;
		for (BanHarddrive obj : DATA.values()) {
			if (obj.account.equalsIgnoreCase(account)) {
				ban = obj;
				break;
			}
		}
		return ban;
	}
	
	private HarddriveTable() {
		load();
	}

	private void load() {
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		BanHarddrive ban			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM ban_hdd");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				ban = new BanHarddrive(rs.getString("number"), rs.getString("account"), rs.getTimestamp("registTime"));
				DATA.put(ban.number, ban);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "HarddriveTable Error", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void insert(String hdd, String account) {
		if (isBan(hdd)) {
			return;
		}
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("INSERT INTO ban_hdd SET number=?, account=?, registTime=NOW()");
			pstm.setString(1, hdd);
			pstm.setString(2, account);
			pstm.execute();
			BanHarddrive ban = new BanHarddrive(hdd, account, new Timestamp(System.currentTimeMillis()));
			DATA.put(ban.number, ban);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "HarddriveTable Error", e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void delete(String hdd) {
		if (!isBan(hdd)) {
			return;
		}
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM ban_hdd WHERE number=?");
			pstm.setString(1, hdd);
			pstm.execute();
			DATA.remove(hdd);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "HarddriveTable Error", e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void deleteAccount(String account) {
		if (DATA.isEmpty()) {
			return;
		}
		BanHarddrive ban = null;
		for (BanHarddrive obj : DATA.values()) {
			if (obj.account.equalsIgnoreCase(account)) {
				ban = obj;
				break;
			}
		}
		if (ban == null) {
			return;
		}
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM ban_hdd WHERE number=?");
			pstm.setString(1, ban.number);
			pstm.execute();
			DATA.remove(ban.number);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "HarddriveTable Error", e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void reload() {
		DATA.clear();
		load();
	}
}


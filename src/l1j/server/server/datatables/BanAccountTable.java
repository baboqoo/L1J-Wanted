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
 * 계정 벤
 * @author LinOffice
 */
public class BanAccountTable {
	
	public enum BanAccountReason {
		/*ETC(			0,	"ETC",			"기타"),
		CHEAT(			1,	"CHEAT",		"사기"),
		CHAT_ABOUS(		2,	"CHAT_ABOUS",	"채팅 욕설"),
		BUG_ABOUS(		3,	"BUG_ABOUS",	"버그 악용");*/

		ETC(			0, 	"ETC", 			"Miscellaneous"),
		CHEAT(			1, 	"CHEAT", 		"Cheat"),
		CHAT_ABOUS(		2, 	"CHAT_ABOUS",	"Chat Abuse"),
		BUG_ABOUS(		3, 	"BUG_ABOUS",	"Bug Abuse");
		
		private int code;
		private String name;
		private String reason;
		private BanAccountReason(int code, String name, String reason) {
			this.code	= code;
			this.name	= name;
			this.reason	= reason;
		}
		public int getCode(){
			return code;
		}
		public String getName(){
			return name;
		}
		public String getReason(){
			return reason;
		}
		
		private static final BanAccountReason[] ARRAY	= BanAccountReason.values();
		private static final ConcurrentHashMap<Integer, BanAccountReason> CODE_DATA;
		private static final ConcurrentHashMap<String, BanAccountReason> NAME_DATA;
		static {
			CODE_DATA	= new ConcurrentHashMap<>();
			NAME_DATA	= new ConcurrentHashMap<>();
			for (BanAccountReason reason : ARRAY) {
				CODE_DATA.put(reason.code, reason);
				NAME_DATA.put(reason.name, reason);
			}
		}
		
		public static BanAccountReason getReason(int code){
			return CODE_DATA.get(code);
		}
		
		public static BanAccountReason getReason(String str){
			return NAME_DATA.get(str);
		}
		
		public static BanAccountReason[] getAllReason(){
			return ARRAY;
		}
	}
	
	public class BanAccount {
		private String account;
		private BanAccountReason reason;
		private int counter;
		private Timestamp limitTime;
		
		public BanAccount(String account, BanAccountReason reason, int counter, Timestamp limitTime) {
			this.account	= account;
			this.reason		= reason;
			this.counter	= counter;
			this.limitTime	= limitTime;
		}
		
		public String getAccount() {
			return account;
		}
		public BanAccountReason getReason() {
			return reason;
		}
		public int getCounter() {
			return counter;
		}
		public Timestamp getLimitTime() {
			return limitTime;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("ACCOUNT: ").append(account).append(StringUtil.LineString);
			sb.append("REASON: ").append(reason.reason).append(StringUtil.LineString);
			sb.append("COUNT: ").append(counter).append(StringUtil.LineString);
			sb.append("LIMIT_TIME: ").append(limitTime).append(StringUtil.LineString);
			return sb.toString();
		}
	}
	
	private static final ConcurrentHashMap<String, ConcurrentHashMap<BanAccountReason, BanAccount>> DATA = new ConcurrentHashMap<>();
	
	public static BanAccount getBan(String account){
		if (StringUtil.isNullOrEmpty(account)) {
			return null;
		}
		ConcurrentHashMap<BanAccountReason, BanAccount> map = DATA.get(account);
		if (map == null || map.isEmpty()) {
			return null;
		}
		long currentTime = System.currentTimeMillis();
		for (BanAccount ban : map.values()) {
			if (ban.limitTime.getTime() > currentTime) {
				return ban;
			}
		}
		return null;
	}
	
	public static BanAccountTable getInstance() {
		if (_instance == null) {
			_instance = new BanAccountTable();
		}
		return _instance;
	}

	private BanAccountTable() {
		load();
	}
	
	public static void reload() {
		DATA.clear();
		_instance.load();
	}
	
	public BanAccount insert(String account, BanAccountReason reason) {
		ConcurrentHashMap<BanAccountReason, BanAccount> map = DATA.get(account);
		if (map == null) {
			map = new ConcurrentHashMap<>();
			DATA.put(account, map);
		}
		
		BanAccount obj = map.get(reason);
		if (obj == null) {
			obj = new BanAccount(
					account,
					reason,
					1,
					new Timestamp(System.currentTimeMillis() + (1 * 86400 * 1000))
					);
			map.put(reason, obj);
		} else {
			obj.counter *= 2;
			obj.limitTime.setTime(System.currentTimeMillis() + (obj.counter * 86400 * 1000));
		}

		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("INSERT INTO ban_account SET account=?, reason=?, counter=?, limitTime=? ON DUPLICATE KEY UPDATE counter=?, limitTime=?");
			pstm.setString(1, obj.account);
			pstm.setString(2, obj.reason.name);
			pstm.setInt(3, obj.counter);
			pstm.setTimestamp(4, obj.limitTime);
			pstm.setInt(5, obj.counter);
			pstm.setTimestamp(6, obj.limitTime);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return obj;
	}
	
	public boolean delete(String account) {
		if (!DATA.containsKey(account)) {
			return false;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM ban_account WHERE account=?");
			pstm.setString(1, account);
			if (pstm.executeUpdate() > 0) {
				DATA.remove(account);
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}

	public void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		BanAccount obj			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ban_account");
			rs = pstm.executeQuery();
			while (rs.next()) {
				obj = new BanAccount(
						rs.getString("account"),
						BanAccountReason.getReason(rs.getString("reason")),
						rs.getInt("counter"),
						rs.getTimestamp("limitTime"));
				ConcurrentHashMap<BanAccountReason, BanAccount> map = DATA.get(obj.account);
				if (map == null) {
					map = new ConcurrentHashMap<>();
					DATA.put(obj.account, map);
				}
				map.put(obj.reason, obj);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private static Logger _log = Logger.getLogger(BanAccountTable.class.getName());
	private static BanAccountTable _instance;

}


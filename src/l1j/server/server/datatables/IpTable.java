package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 아이피 벤 정보
 * @author LinOffice
 */
public class IpTable {
	
	public enum BanIpReason {
		/*ETC(				0,	"ETC",					"기타"),						// 기타
		CHEAT(				1,	"CHEAT",				"사기"),						// 사기
		BUG_ABOUS(			2,	"BUG_ABOUS",			"버그 악용"),					// 버그 악용
		WELLKNOWN_PORT(		3,	"WELLKNOWN_PORT",		"웰노운 포트 접속"),				// 웰노운 포트 접속
		SERVER_SLANDER(		4,	"SERVER_SLANDER",		"서버 비방"),					// 서버 비방
		WEB_URI_LENGTH_OVER(5,	"WEB_URI_LENGTH_OVER",	"웹 URI 제한길이 초과"),			// 웹 uri길이 비정상
		WEB_REQUEST_OVER(	6,	"WEB_REQUEST_OVER",		"웹 과다 요청"),					// 웹 과다 요청
		UNSUAL_REQUEST(		7,	"UNSUAL_REQUEST",		"비정상적 요청"),					// 비정상적인 요청
		BAD_USER(			8,	"BAD_USER",				"악질적 유저"),					// 악질적인 유저
		PACKET_ATTACK(		9,	"PACKET_ATTACK",		"패킷 공격"),					// 패킷 공격
		CONNECTION_OVER(	10,	"CONNECTION_OVER",		"커넥션 수 초과"),					// 커넥션 수 초과
		WEB_ATTACK_REQUEST(	11,	"WEB_ATTACK_REQUEST",	"웹 허용하지 않는 URI 또는 스크립트 공격"),	// 웹 공격 요청
		WEB_NOT_AUTH_IP(	12,	"WEB_NOT_AUTH_IP",		"웹 VPN 또는 해외 IP 요청");		// 웹 VPN 또는 해외 IP 요청*/

		ETC(				0, 	"ETC", 					"Miscellaneous"),                           		// 기타
		CHEAT(				1, 	"CHEAT",				"Cheat"),                            				// 사기
		BUG_ABOUS(			2, 	"BUG_ABOUS",			"Bug Abuse"),                   					// 버그 악용
		WELLKNOWN_PORT(		3, 	"WELLKNOWN_PORT", 		"Connection to Well-Known Port"),   				// 웰노운 포트 접속
		SERVER_SLANDER(		4, 	"SERVER_SLANDER", 		"Server Slander"),    								// 서버 비방
		WEB_URI_LENGTH_OVER(5, 	"WEB_URI_LENGTH_OVER", 	"Web URI Length Exceeded"),  						// 웹 uri길이 비정상
		WEB_REQUEST_OVER(	6, 	"WEB_REQUEST_OVER", 	"Excessive Web Requests"),        					// 웹 과다 요청
		UNSUAL_REQUEST(		7, 	"UNSUAL_REQUEST", 		"Unusual Request"),   								// 비정상적인 요청
		BAD_USER(			8, 	"BAD_USER", 			"Malicious User"),               					// 악질적인 유저
		PACKET_ATTACK(		9, 	"PACKET_ATTACK", 		"Packet Attack"),       							// 패킷 공격
		CONNECTION_OVER(	10,	"CONNECTION_OVER", 		"Exceeded Connection Limit"),      					// 커넥션 수 초과
		WEB_ATTACK_REQUEST(	11, "WEB_ATTACK_REQUEST", 	"Web Attack Request to Non-Allowed URI or Script"), // 웹 공격 요청
		WEB_NOT_AUTH_IP(	12, "WEB_NOT_AUTH_IP", 		"Web VPN or Overseas IP Request"); 					// 웹 VPN 또는 해외 IP 요청		

		private int code;
		private String name;
		private String reason;
		private BanIpReason(int code, String name, String reason) {
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
		
		private static final BanIpReason[] ARRAY	= BanIpReason.values();
		private static final ConcurrentHashMap<Integer, BanIpReason> CODE_DATA;
		private static final ConcurrentHashMap<String, BanIpReason> NAME_DATA;
		static {
			CODE_DATA	= new ConcurrentHashMap<>();
			NAME_DATA	= new ConcurrentHashMap<>();
			for (BanIpReason reason : ARRAY) {
				CODE_DATA.put(reason.code, reason);
				NAME_DATA.put(reason.name, reason);
			}
		}
		
		public static BanIpReason getReason(int code){
			return CODE_DATA.get(code);
		}
		
		public static BanIpReason getReason(String str){
			return NAME_DATA.get(str);
		}
		
		public static BanIpReason[] getAllReason(){
			return ARRAY;
		}
	}
	
	public class BanIp {
		private String address;
		private BanIpReason reason;
		private Timestamp registTime;
		
		public BanIp(String address, BanIpReason reason, Timestamp registTime) {
			this.address	= address;
			this.reason		= reason;
			this.registTime	= registTime;
		}

		public String getAddress() {
			return address;
		}
		public BanIpReason getReason() {
			return reason;
		}
		public Timestamp getRegistTime() {
			return registTime;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("ADDRESS: ").append(address).append(StringUtil.LineString);
			sb.append("REASON: ").append(reason.name).append(StringUtil.LineString);
			sb.append("REGIST_TIME: ").append(registTime).append(StringUtil.LineString);
			return sb.toString();
		}
	}
	
	private static final ConcurrentHashMap<String, BanIp> DATA = new ConcurrentHashMap<>();
	
	public static boolean isBannedIp(String address) {
		if (StringUtil.isNullOrEmpty(address)) {
			return false;
		}
		return DATA.containsKey(address);
	}
	
	public static BanIp getBanIp(String address){
		if (StringUtil.isNullOrEmpty(address)) {
			return null;
		}
		return DATA.get(address);
	}
	
	public static IpTable getInstance() {
		if (_instance == null) {
			_instance = new IpTable();
		}
		return _instance;
	}

	private IpTable() {
		load();
	}
	
	public static void reload() {
		DATA.clear();
		_instance.load();
	}
	
	public void insert(String address, BanIpReason reason) {
		if (DATA.containsKey(address)) {
			return;
		}
		BanIp obj = new BanIp(
				address,
				reason,
				new Timestamp(System.currentTimeMillis())
				);
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("INSERT INTO ban_ip SET address=?, reason=?, registTime=NOW()");
			pstm.setString(1, obj.address);
			pstm.setString(2, obj.reason.name);
			pstm.execute();
			DATA.put(obj.address, obj);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public boolean delete(String address) {
		if (!DATA.containsKey(address)) {
			return false;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM ban_ip WHERE address=?");
			pstm.setString(1, address);
			if (pstm.executeUpdate() > 0) {
				DATA.remove(address);
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
	
	public void insertRange(String address, BanIpReason reason) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		BanIp obj				= null;
		String ip1, ip2, ip3;
		StringTokenizer st = new StringTokenizer(address, StringUtil.PeriodString);
		ip1 = st.nextToken();
		ip2 = st.nextToken();
		ip3 = st.nextToken();
		Timestamp registTime = new Timestamp(System.currentTimeMillis());
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm	= con.prepareStatement("INSERT INTO ban_ip SET address=?, reason=?, registTime=NOW()");
			for (int i = 1; i <= 255; i++) {
				String temp = ip1 + StringUtil.PeriodString + ip2 + StringUtil.PeriodString + ip3 + StringUtil.PeriodString + i;
				if(isBannedIp(temp))continue;
				obj = new BanIp(temp, reason, registTime);
				pstm.setString(1, obj.address);
				pstm.setString(2, obj.reason.name);
				pstm.addBatch();
				pstm.clearParameters();
				DATA.put(obj.address, obj);
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}

	public void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		BanIp obj				= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ban_ip");
			rs = pstm.executeQuery();
			while (rs.next()) {
				obj = new BanIp(
						rs.getString("address"),
						BanIpReason.getReason(rs.getString("reason")),
						rs.getTimestamp("registTime")
						);
				DATA.put(obj.address, obj);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private static Logger _log = Logger.getLogger(IpTable.class.getName());
	private static IpTable _instance;

}


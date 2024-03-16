package l1j.server.server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Base64;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.arca.L1Arca;
import l1j.server.GameSystem.attendance.AttendanceAccountTable;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.shoplimit.ShopLimitUser;
import l1j.server.GameSystem.shoplimit.ShopLimitLoader;
import l1j.server.server.model.L1Einhasad;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.account.AccountDAO;
import l1j.server.web.dispatcher.response.account.AccountVO;
import l1j.server.web.dispatcher.response.account.CharacterVO;
import l1j.server.web.http.connector.HttpLoginSession;
//import manager.ManagerInfoThread;  // MANAGER DISABLED 
import java.time.LocalDate;

public class Account {
	private static Logger _log = Logger.getLogger(Account.class.getName());
	
	/** 계정명 */
	private String _name;
	/** 접속자 IP주소 */
	private String _ip;
	/** 패스워드(암호화 됨) */
	private String _password;
	/** 최근 접속일 */
	private Timestamp _lastActive;
	/** 최종 종료일 */
	private Timestamp _lastQuit;
	/** 엑세스 등급(GM인가?) */
	private int _accessLevel;
	/** 접속자 호스트명 */
	private String _host;
	/** 밴 유무(True == 금지) */
	private boolean _banned;
	/** 계정 유효 유무(True == 유효) */
	private boolean _isValid;
	/** 캐릭터 슬롯(태고의옥쇄) */
	private int _charslot;
	private boolean _charSlotChange;
	/** 창고 비밀번호 */
	private int _warehouse_password;
	/** 캐릭터 비밀번호 */
	private String _second_password;
	private byte[] _waitPacket;
	private boolean _is_auth_second_password;
	
	private boolean _gameMaster;
	
	private String _phone;
	private String _quiz;
	private String _hddId;
	private String _boardId;
	
	private HttpLoginSession loginSession;
	
	private int Ncoin, Npoint;
	private int shopOpenCount;

	private Timestamp _Buff_DMG, _Buff_REDUC, _Buff_MAGIC, _Buff_STUN, _Buff_HOLD, 
	//_Buff_정방, _Buff_화방, _Buff_지방, _Buff_수방, _Buff_풍방, _Buff_화공, _Buff_지공, 
	_Buff_Orthodoxy, _Buff_Fire, _Buff_Earth, _Buff_Water, _Buff_Wind, _Buff_FireAttack, _Buff_EarthAttack, 
	//_Buff_수공, _Buff_풍공, _Buff_지혜, _Buff_지식, _Buff_민첩, _Buff_완력;
	_Buff_WaterAttack, _Buff_WindAttack, _Buff_Intelligence, _Buff_Knowledge, _Buff_Agility, _Buff_Prowess;

	private Timestamp _BuffPCCafe, _BuffHero, _BuffLife, _dragonRaidBuff;
	
	private Timestamp _indunCheckTime;
	private int _indunCount;
	
	private boolean termsAgree;
	private AccountVO accountVO;
	private List<CharacterVO> charList;
	private CharacterVO firstChar;
	private int appChar;
	
	private ShopLimitUser shopLimit;
	private L1Arca arca;
	private L1Einhasad einhasad;
	private AttendanceAccount attend;

	private int _PSSTime;
	private boolean _PSSTimeAdded = false;

	public Account() {}

	/**
	 * 패스워드를 암호화한다.
	 *
	 * @param rawPassword 패스워드
	 * @return String
	 * @throws NoSuchAlgorithmException
	 *             암호화 알고리즘을 사용할 수 없을 때
	 * @throws UnsupportedEncodingException
	 *             인코딩이 지원되지 않을 때
	 */
	@SuppressWarnings("unused")
	private static String encodePassword(final String rawPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] buf = rawPassword.getBytes(CharsetUtil.UTF_8_STR);
		buf = MessageDigest.getInstance("SHA").digest(buf);
		return Base64.encodeBytes(buf);
	}

	// 영구추방 아이피 체크
	public static String checkIP(String name) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(String.format("SELECT ip FROM accounts WHERE login='%s'", name));
			rs		= pstm.executeQuery();
			if (rs.next()) {
				return rs.getString("ip");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	/**
	 * 계정 생성
	 *
	 * @param name 계정명
	 * @param password 패스워드
	 * @param ip 접속자 IP주소
	 * @param host 접속자 호스트명
	 */
	public static void create(final String name, final String password, final String ip, final String host) {
		create(name, password, ip, host, null, null, null);
	}
	
	/**
	 * 계정 생성
	 *
	 * @param name 계정명
	 * @param password 패스워드
	 * @param ip 접속자 IP주소
	 * @param host 접속자 호스트명
	 * @param phone 연락처
	 * @param hddId 하드디스크 아이디
	 */
	public static void create(final String name, final String password, final String ip, final String host, final String phone, final String hddId, final String boardId) {
		Calendar cal	= Calendar.getInstance();
		String AMPM		= cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
		Connection con	= null;
		PreparedStatement pstm = null;
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO accounts SET login=?, password=?, lastactive=NOW(), access_level=0, ip=?, host=?, banned=0, charslot=6, warehouse_password=0, Einhasad=2000000, EinDayBonus=1");
		boolean phoneQuery = !StringUtil.isNullOrEmpty(phone);
		if (phoneQuery) {
			query.append(", phone=?");
		}
		boolean hddQuery = !StringUtil.isNullOrEmpty(hddId);
		if (hddQuery) {
			query.append(", hddId=?");
		}
		boolean boardQuery = !StringUtil.isNullOrEmpty(boardId);
		if (boardQuery) {
			query.append(", boardId=?");
		}
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(query.toString());
			int i	= 0;
			pstm.setString(++i, name);
			pstm.setString(++i, password);
			pstm.setString(++i, ip);
			pstm.setString(++i, host);
			if (phoneQuery) {
				pstm.setString(++i, phone);
			}
			if (hddQuery) {
				pstm.setString(++i, hddId);
			}
			if (boardQuery) {
				pstm.setString(++i, boardId);
			}
			pstm.execute();
			System.out.println(cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + " " + AMPM + "   ■ New Account ["+name+"] created ■");
			//ManagerInfoThread.AccountCount += 1;  // MANAGER DISABLED
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * DB에서 계정 정보 불러오기 
	 *
	 * @param name 계정명
	 * @return Account
	 */
	public static Account load(final String name) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		Account account			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM accounts WHERE login=? LIMIT 1");
			pstm.setString(1, name);
			rs		= pstm.executeQuery();
			if(!rs.next())return null;
			account						= new Account();
			account._name				= rs.getString("login");
			account._password			= rs.getString("password");
			account._lastActive			= rs.getTimestamp("lastactive");
			account._lastQuit			= rs.getTimestamp("lastQuit");
			account._accessLevel		= rs.getInt("access_level");
			account._gameMaster			= account._accessLevel > 0;
			account._host				= rs.getString("host");
			account._banned				= rs.getInt("banned") != 0;
			account._charslot			= rs.getInt("charslot");
			account._warehouse_password	= rs.getInt("warehouse_password");
			account._phone				= rs.getString("phone");
			account._quiz				= rs.getString("quiz");
			account._hddId				= rs.getString("hddId");
			account._boardId			= rs.getString("boardId");
			
			account._Buff_DMG			= rs.getTimestamp("BUFF_DMG_Time");
			account._Buff_REDUC			= rs.getTimestamp("BUFF_REDUC_Time");
			account._Buff_MAGIC			= rs.getTimestamp("BUFF_MAGIC_Time");
			account._Buff_STUN			= rs.getTimestamp("BUFF_STUN_Time");
			account._Buff_HOLD			= rs.getTimestamp("BUFF_HOLD_Time");
			account._BuffPCCafe			= rs.getTimestamp("BUFF_PCROOM_Time");
			
			account._Buff_Agility			= rs.getTimestamp("Buff_Dex_Time");
			account._Buff_Prowess			= rs.getTimestamp("Buff_Str_Time");
			account._Buff_Intelligence			= rs.getTimestamp("Buff_Wis_Time");
			account._Buff_Knowledge			= rs.getTimestamp("Buff_Int_Time");
			account._Buff_Orthodoxy			= rs.getTimestamp("Buff_SoulDefence_Time");	
			account._Buff_WaterAttack			= rs.getTimestamp("Buff_WaterAttack_Time");
			account._Buff_Water			= rs.getTimestamp("Buff_WaterDefence_Time");
			account._Buff_EarthAttack			= rs.getTimestamp("Buff_EarthAttack_Time");
			account._Buff_Earth			= rs.getTimestamp("Buff_EarthDefence_Time");
			account._Buff_WindAttack			= rs.getTimestamp("Buff_WindAttack_Time");
			account._Buff_Wind			= rs.getTimestamp("Buff_WindDefence_Time");
			account._Buff_FireAttack			= rs.getTimestamp("Buff_FireAttack_Time");
			account._Buff_Fire			= rs.getTimestamp("Buff_FireDefence_Time");
			
			account._BuffHero			= rs.getTimestamp("Buff_Hero_Time");
			account._BuffLife			= rs.getTimestamp("Buff_Life_Time");
			
			account.Ncoin				= rs.getInt("Ncoin");
			account.Npoint				= rs.getInt("Npoint");
			account.shopOpenCount		= rs.getInt("Shop_open_count");
			
			account._dragonRaidBuff		= rs.getTimestamp("DragonRaid_Buff");
			account._indunCheckTime		= rs.getTimestamp("IndunCheckTime");
			account._indunCount			= rs.getInt("IndunCount");
			account._second_password	= rs.getString("second_password");	
			
			account.shopLimit			= ShopLimitLoader.getShopLimitFromAccount(account._name);
			account.arca				= new L1Arca(account, rs.getInt("Tam_Point"));
			account.einhasad			= new L1Einhasad(account, rs.getInt("Einhasad"), rs.getInt("EinDayBonus"));
			account.attend				= AttendanceAccountTable.getInstance().getAccountInfos(account._name);
			
			account.termsAgree			= Boolean.valueOf(rs.getString("app_terms_agree"));
			account.appChar				= rs.getInt("app_char");
			account.charList			= AccountDAO.getInstance().getCharList(account._name, account._gameMaster);

			account._PSSTime			= rs.getInt("PSSTime");
			_log.fine("account exists");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return account;
	}

	public void addPSSTime() {	
		if (Config.PSS.PLAY_SUPPORT_TIME_LIMITED) {
			LocalDate dateNow = LocalDate.now();
			LocalDate dateLastActive = _lastActive.toLocalDateTime().toLocalDate();
			if (!dateNow.isEqual(dateLastActive)) {
				int newPSSTime = getPSSTime() + (Config.PSS.PLAY_SUPPORT_TIME_DAY_ADD * 60);
				if (newPSSTime > Config.PSS.PLAY_SUPPORT_TIME_MAX * 60)
				  newPSSTime = Config.PSS.PLAY_SUPPORT_TIME_MAX * 60;
				setPSSTime(newPSSTime);
				setPSSTimeAdded(true);
			} else
				setPSSTimeAdded(false);
		}
	}

	public boolean getPSSTimeAdded() {
		return _PSSTimeAdded;
	}
	public void setPSSTimeAdded(boolean i) {
		_PSSTimeAdded = i;
	}
	
	/**
	 * DB에 최근 접속일 업데이트
	 * @param ip
	 */
	public void updateLastActive(final String ip) {
		_ip = ip;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET lastactive=NOW(), ip=? WHERE login=?");
			pstm.setString(1, ip);
			pstm.setString(2, getName());
			pstm.execute();
			if (_lastActive != null) {
				_lastActive.setTime(System.currentTimeMillis());
			} else {
				_lastActive = new Timestamp(System.currentTimeMillis());
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * DB에 최종 종료 업데이트(클라이언트 소켓 종료 시 호출)
	 * 계정 관련 업데이트를 실시한다.
	 */
	public void updateQuit() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET lastQuit=NOW() WHERE login=?");
			pstm.setString(1, _name);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * DB에 최종 종료 업데이트(캐릭터 종료시)
	 * 계정 관련 업데이트를 실시한다.
	 */
	public void update_logout() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET Einhasad=?, Ncoin=?, Npoint=? WHERE login=?");
			int index = 0;
			pstm.setInt(++index, einhasad.getPoint());
			pstm.setInt(++index, Ncoin);
			pstm.setInt(++index, Npoint);
			pstm.setString(++index, _name);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		// 출석 체크 업데이트
		if (Config.ATTEND.ATTENDANCE_ACTIVE) {
			AttendanceAccountTable.getInstance().store(attend);
		}
		// 탐 업데이트
		arca.merge();
		// 상점 제한 업데이트
		ShopLimitLoader.getInstance().storeFromAccount(this);
	}
	
	/**
	 * 해당 계정의 캐릭터수
	 * @return result 캐릭터수
	 */
	public int countCharacters() {
		int result				= 0;
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT COUNT(*) AS cnt FROM characters WHERE account_name=?");
			pstm.setString(1, _name);
			rs		= pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}
	
	public void updateNcoin() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET Ncoin=? WHERE login=?");
			pstm.setInt(1, Ncoin);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateNpoint() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET Npoint=? WHERE login=?");
			pstm.setInt(1, Npoint);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateShopOpenCount() {
		shopOpenCount++;
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET Shop_open_count=? WHERE login=?");
			pstm.setInt(1, shopOpenCount);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/** 글루디오 연구실 **/
	public Timestamp getIndunCheckTime() {
		return _indunCheckTime;
	}
	public void setIndunCheckTime(Timestamp i) {
		_indunCheckTime = i;
	}
	
	public int getIndunCount() {
		return _indunCount;
	}
	public void setIndunCount(int i) {
		_indunCount = i;
	}

	public void updateIndunCount() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET IndunCount=? WHERE login=?");
			pstm.setInt(1, getIndunCount());
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public int getPSSTime() {
		return _PSSTime;
	}
	public void setPSSTime(int i) {
		_PSSTime = i;
		updatePSSTime();
	}

	public void updatePSSTime() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET PSSTime=? WHERE login=?");
			pstm.setInt(1, getPSSTime());
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void ban(final String accountName) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET banned=1 WHERE login=?");
			pstm.setString(1, accountName);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 비밀번호 유효성 체크
	 * @param password 패스워드
	 * @return boolean
	 */
	public boolean validatePassword(final String password) {
		try {
			_isValid = _password.equals(password);
			return _isValid;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return false;
	}
	
	public void updatePhone() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET phone=? WHERE login=?");
			pstm.setString(1, getPhone());
			pstm.setString(2, getName());
			pstm.execute();
		} catch (Exception e) {
			//_log.log(Level.SEVERE, "accounts updatePhone 에러발생", e);
			_log.log(Level.SEVERE, "accounts updatePhone error occurred", e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 유효한 계정인가 
	 *
	 * @return boolean
	 */
	public boolean isValid() {
		return _isValid;
	}
	public void setValid(boolean tt) {
		_isValid = tt;
	}

	/**
	 * GM 계정인가
	 *
	 * @return boolean
	 */
	public boolean isGameMaster() {
		return _gameMaster;
	}
	public int getAccessLevel() {
		return _accessLevel;
	}
	public String getName() {
		return _name;
	}
	public String getPassword() {
		return _password;
	}

	public Timestamp getBuff_DMG() {
		return _Buff_DMG;
	}
	public void setBuff_DMG(Timestamp ts) {
		_Buff_DMG = ts;
	}

	public Timestamp getBuff_REDUC() {
		return _Buff_REDUC;
	}
	public void setBuff_REDUC(Timestamp ts) {
		_Buff_REDUC = ts;
	}

	public Timestamp getBuff_MAGIC() {
		return _Buff_MAGIC;
	}
	public void setBuff_MAGIC(Timestamp ts) {
		_Buff_MAGIC = ts;
	}

	public Timestamp getBuff_STUN() {
		return _Buff_STUN;
	}
	public void setBuff_STUN(Timestamp ts) {
		_Buff_STUN = ts;
	}

	public Timestamp getBuff_HOLD() {
		return _Buff_HOLD;
	}
	public void setBuff_HOLD(Timestamp ts) {
		_Buff_HOLD = ts;
	}
	
	public Timestamp getBuff_PCCafe() {
		return _BuffPCCafe;
	}
	public void setBuff_PCCafe(Timestamp ts) {
		_BuffPCCafe = ts;
	}
	

	public Timestamp getBuff_FireATTACK() {
		return _Buff_FireAttack;
	}
	public void setBuff_FireATTACK(Timestamp ts) {
		_Buff_FireAttack = ts;
	}
	
	public Timestamp getBuff_EarthATTACK() {
		return _Buff_EarthAttack;
	}
	public void setBuff_EarthATTACK(Timestamp ts) {
		_Buff_EarthAttack = ts;
	}
	
	public Timestamp getBuff_WaterATTACK() {
		return _Buff_WaterAttack;
	}
	public void setBuff_WaterATTACK(Timestamp ts) {
		_Buff_WaterAttack = ts;
	}
	
	public Timestamp getBuff_WindATTACK() {
		return _Buff_WindAttack;
	}
	public void setBuff_WindATTACK(Timestamp ts) {
		_Buff_WindAttack = ts;
	}
	
	public Timestamp getBuff_SoulDEFENCE() {
		return _Buff_Orthodoxy;
	}
	public void setBuff_SoulDEFENCE(Timestamp ts) {
		_Buff_Orthodoxy = ts;
	}
	
	public Timestamp getBuff_FireDEFENCE() {
		return _Buff_Fire;
	}
	public void setBuff_FireDEFENCE(Timestamp ts) {
		_Buff_Fire = ts;
	}
	
	public Timestamp getBuff_EarthDEFENCE() {
		return _Buff_Earth;
	}
	public void setBuff_EarthDEFENCE(Timestamp ts) {
		_Buff_Earth = ts;
	}
	
	public Timestamp getBuff_WaterDEFENCE() {
		return _Buff_Water;
	}
	public void setBuff_WaterDEFENCE(Timestamp ts) {
		_Buff_Water = ts;
	}
	
	public Timestamp getBuff_WindDEFENCE() {
		return _Buff_Wind;
	}
	public void setBuff_WindDEFENCE(Timestamp ts) {
		_Buff_Wind = ts;
	}
	
	public Timestamp getBuff_STR() {
		return _Buff_Prowess;
	}
	public void setBuff_STR(Timestamp ts) {
		_Buff_Prowess = ts;
	}
	
	public Timestamp getBuff_DEX() {
		return _Buff_Agility;
	}
	public void setBuff_DEX(Timestamp ts) {
		_Buff_Agility = ts;
	}
	
	public Timestamp getBuff_INT() {
		return _Buff_Knowledge;
	}
	public void setBuff_INT(Timestamp ts) {
		_Buff_Knowledge = ts;
	}
	
	public Timestamp getBuff_WIS() {
		return _Buff_Intelligence;
	}
	public void setBuff_WIS(Timestamp ts) {
		_Buff_Intelligence = ts;
	}
	
	/** 영웅의 가호 **/
	public Timestamp getBuff_HERO() {
		return _BuffHero;
	}
	public void setBuff_HERO(Timestamp ts) {
		_BuffHero = ts;
	}
	
	/** 생명의 가호 **/
	public Timestamp getBuff_LIFE() {
		return _BuffLife;
	}
	public void setBuff_LIFE(Timestamp ts) {
		_BuffLife = ts;
	}
	
	public void updateHeroBuff() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET Buff_Hero_Time=? WHERE login=?");
			pstm.setTimestamp(1, _BuffHero);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateLifeBuff() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET Buff_Life_Time=? WHERE login=?");
			pstm.setTimestamp(1, _BuffLife);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void setIp(String ip) {
		_ip = ip;
	}
	public String getIp() {
		return _ip;
	}
	
	public String getHost() {
		return _host;
	}

	/**
	  * 최종 로그인일을 취득한다.
	  */
	public Timestamp getLastActive() {
		return _lastActive;
	}
	
	/**
	  * 최종 로그아웃일을 취득한다.
	  */
	public Timestamp getLastQuit(){
		return _lastQuit;
	}

	public boolean isBanned() {
		return _banned;
	}

	public int getCharSlot() {
		return _charslot;
	}	
	
	/**
	 * 연락처를 취득한다.
	 * @return String
	 */
	public String getPhone() {
		return _phone;
	}
	public void setPhone(String s) {
		_phone = s;
	}
	
	/**
	 * 하드정보를 취득한다.
	 * @return String
	 */
	public String getHddId() {
		return _hddId;
	}
	public void setHddId(String s) {
		_hddId = s;
	}
	
	/**
	 * 마더보드정보를 취득한다.
	 * @return String
	 */
	public String getBoardId() {
		return _boardId;
	}
	public void setBoardId(String s) {
		_boardId = s;
	}

	/**
	 * 캐릭터 슬롯수 설정 
	 * @return boolean
	 */
	public void updateCharSlot(int value) {
		_charslot = value;
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET charslot=? WHERE login=?");
			pstm.setInt(1, _charslot);
			pstm.setString(2, _name);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 아이피에 해당하는 계정 개수 조사
	 * @param ip
	 * @return count
	 */
	public static boolean checkCountFromIP(String ip) {  
		int num = 0;  
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT COUNT(ip) AS cnt FROM accounts WHERE ip=?");
			pstm.setString(1, ip);
			rs = pstm.executeQuery();
			if (rs.next()) {
				num = rs.getInt("cnt");
			}
			// 동일 IP로 생성된 계정이 설정 미만인 경우
			if (num < Config.SERVER.CREATE_IP_ACCOUNT_COUNT) {// 계정생성외부화
				return false;
			}
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}
	
	/**
	 * 하드번호에 해당하는 계정 개수 조사
	 * @param hddId
	 * @return count
	 */
	public static boolean checkCountFromHDD(String hddId) {  
		int num = 0;  
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT COUNT(hddId) AS cnt FROM accounts WHERE hddId=?");
			pstm.setString(1, hddId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				num = rs.getInt("cnt");
			}
			// 동일 하드번호로 생성된 계정이 설정 미만인 경우
			if (num < Config.SERVER.CREATE_HDD_ACCOUNT_COUNT) {// 계정생성외부화
				return false;
			}
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}
	
	/**
	 * updateBUFF
	 */
	public void updateBuff() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();//n버프 ㅗㅇ포내성
			pstm	= con.prepareStatement(
					"UPDATE accounts SET Buff_DMG_Time=?,Buff_Reduc_Time=?,Buff_Magic_Time=?,Buff_Stun_Time=?,Buff_Hold_Time=?, "
					+ "Buff_FireDefence_Time=?,Buff_EarthDefence_Time=?,Buff_WaterDefence_Time=?,Buff_WindDefence_Time=?,Buff_SoulDefence_Time=?,"
					+ "Buff_Str_Time=?,Buff_Dex_Time=?,Buff_Int_Time=?,Buff_Wis_Time=?,"
					+ "Buff_FireAttack_Time=?,Buff_EarthAttack_Time=?,Buff_WaterAttack_Time=?,Buff_WindAttack_Time=? WHERE login=?");
			int i = 0;
			pstm.setTimestamp(++i, _Buff_DMG);
			pstm.setTimestamp(++i, _Buff_REDUC);
			pstm.setTimestamp(++i, _Buff_MAGIC);
			pstm.setTimestamp(++i, _Buff_STUN);
			pstm.setTimestamp(++i, _Buff_HOLD);
			pstm.setTimestamp(++i, _Buff_Fire);
			pstm.setTimestamp(++i, _Buff_Earth);
			pstm.setTimestamp(++i, _Buff_Water);
			pstm.setTimestamp(++i, _Buff_Wind);
			pstm.setTimestamp(++i, _Buff_Orthodoxy);
			pstm.setTimestamp(++i, _Buff_Prowess);
			pstm.setTimestamp(++i, _Buff_Agility);
			pstm.setTimestamp(++i, _Buff_Knowledge);
			pstm.setTimestamp(++i, _Buff_Intelligence);
			pstm.setTimestamp(++i, _Buff_FireAttack);
			pstm.setTimestamp(++i, _Buff_EarthAttack);
			pstm.setTimestamp(++i, _Buff_WaterAttack);
			pstm.setTimestamp(++i, _Buff_WindAttack);
			pstm.setString(++i, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * update피씨방
	 */
	public void updatePCCafeBuff() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET BUFF_PCROOM_Time=? WHERE login=?");
			pstm.setTimestamp(1, _BuffPCCafe);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 창고 비번
	 */
	public void updateWarehousePassword(int pass) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET warehouse_password=? WHERE login =?");
			pstm.setInt(1, pass);
			pstm.setString(2, getName());
			pstm.execute();
			_warehouse_password = pass;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 2차 비번 변경
	 * @param account 계정명
	 */
	public void update_second_password(String pwd) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET second_password=? WHERE login=?");
			pstm.setString(1, pwd);
			pstm.setString(2, getName());
			pstm.executeUpdate();
			_second_password = pwd;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * update 하드디스크
	 */
	public void updateHddId(String hddId) {
		_hddId					= hddId;
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET hddId=? WHERE login=?");
			pstm.setString(1, _hddId);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * update 마더보드
	 */
	public void updateBoardId(String boardId) {
		_boardId				= boardId;
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET boardId=? WHERE login=?");
			pstm.setString(1, _boardId);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	// -- 캐릭 2차 비번
	public void set_auth_second_password(boolean val) {
		_is_auth_second_password = val;
	}
	public boolean is_auth_second_password() {
		return _is_auth_second_password;
	}
	
	public byte[] getWaitPacket() {
		return _waitPacket;
	}
	public void setWaitPacket(byte[] data) {
		_waitPacket = data;
	}

	public String get_second_password() {
		return _second_password;
	}
	
	public String getQuiz() {
		return _quiz;
	}
	public void setQuiz(String _quiz) {
		this._quiz = _quiz;
	}
	
	public HttpLoginSession getLoginSession(){
		return loginSession;
	}
	public void setLoginSession(HttpLoginSession session){
		loginSession = session;
	}
	
	public int getNcoin(){
		return Ncoin;
	}
	public void setNcoin(int value){
		Ncoin = value;
		if (accountVO != null) {
			accountVO.setNcoin(Ncoin);
		}
	}
	public void addNcoin(int value){
		Ncoin += value;
		if (accountVO != null) {
			accountVO.setNcoin(Ncoin);
		}
	}
	
	public int getNpoint(){
		return Npoint;
	}
	public void setNpoint(int value){
		Npoint = value;
		if (accountVO != null) {
			accountVO.setNpoint(Npoint);
		}
	}
	public void addNpoint(int value){
		Npoint += value;
		if (accountVO != null) {
			accountVO.setNpoint(Npoint);
		}
	}
	
	public int getShopOpenCount(){
		return shopOpenCount;
	}
	public void setShopOpenCount(int value){
		shopOpenCount = value;
	}
	
	public int getWarehousePassword() {
		return _warehouse_password;
	}
	
	public Timestamp getDragonRaid() {
		return _dragonRaidBuff;
	}
	public void setDragonRaid(Timestamp ts) {
		_dragonRaidBuff = ts;
	}
	
	public boolean isCharSlotChange(){
		return _charSlotChange;
	}
	public void setCharSlotChange(boolean flag){
		_charSlotChange = flag;
	}
	
	public ShopLimitUser getShopLimit(){
		return shopLimit;
	}
	public void setShopLimit(ShopLimitUser limit){
		shopLimit = limit;
	}
	
	public L1Arca getArca(){
		return arca;
	}
	
	public L1Einhasad getEinhasad(){
		return einhasad;
	}
	
	public AttendanceAccount getAttendance(){
		return attend;
	}
	
	public boolean isTermsAgree(){
		return termsAgree;
	}
	public void setTermsAgree(boolean flag){
		termsAgree = flag;
	}
	
	public AccountVO getAccountVO() {
		return accountVO;
	}
	public void setAccountVO(AccountVO val) {
		accountVO = val;
	}
	
	public List<CharacterVO> getCharList(){
		return charList;
	}
	public void setCharList(List<CharacterVO> list){
		charList = list;
	}
	public void addCharList(L1PcInstance pc){
		if (charList == null) {
			charList = new ArrayList<CharacterVO>();
		}
		CharacterVO vo = AccountDAO.getInstance().getChar(pc.getName(), _gameMaster);
		if (vo == null) {
			return;
		}
		charList.add(vo);
	}
	public void removeCharList(L1PcInstance pc){
		if (charList == null) {
			return;
		}
		for (CharacterVO vo : charList) {
			if (vo.getObjId() == pc.getId()) {
				charList.remove(vo);
				break;
			}
		}
	}
	
	public CharacterVO getFirstChar(){
		return firstChar;
	}
	public void setFirstChar(CharacterVO vo){
		firstChar = vo;
	}
	
	public int getAppChar(){
		return appChar;
	}
	public void setAppChar(int objId){
		appChar = objId;
	}
	
}


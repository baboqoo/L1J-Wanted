package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.Instance.L1FakePcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.warehouse.ElfWarehouse;
import l1j.server.server.model.warehouse.PackageWarehouse;
import l1j.server.server.model.warehouse.PrivateWarehouse;
import l1j.server.server.model.warehouse.SpecialWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.warehouse.S_GoodsInvenNoti;
import l1j.server.server.storage.CharacterStorage;
import l1j.server.server.storage.mysql.MySqlCharacterStorage;
import l1j.server.server.templates.L1CharName;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class CharacterTable {
	private CharacterStorage _charStorage;

	private static CharacterTable _instance;

	private static Logger _log = Logger.getLogger(CharacterTable.class.getName());
	
	private final Map<String, L1CharName> _charNameList = new ConcurrentHashMap<String, L1CharName>();

	private CharacterTable() {
		_charStorage = new MySqlCharacterStorage();
	}

	public static CharacterTable getInstance() {
		if (_instance == null) {
			_instance = new CharacterTable();
		}
		return _instance;
	}

	public void storeNewCharacter(L1PcInstance pc) throws Exception {
		synchronized (pc) {
			_charStorage.createCharacter(pc);
			_log.finest("storeNewCharacter");
		}
	}
	
	public void updateCharacterAccount(L1PcInstance pc) throws Exception {
		synchronized (pc) {
			_charStorage.updateAccountName(pc);
			_log.finest("updateCharacterAccount");
		}
	}

	public void storeCharacter(L1PcInstance pc) throws Exception {
		synchronized (pc) {
			_charStorage.storeCharacter(pc);
			String name = pc.getName();
			if (!_charNameList.containsKey(name)) {
				L1CharName cn = new L1CharName();
				cn.setName(name);
				cn.setId(pc.getId());
				_charNameList.put(name, cn);
			}
			_log.finest("storeCharacter: " + pc.getName());
		}
	}

	public void deleteCharacter(String accountName, String charName) throws Exception {
		_charStorage.deleteCharacter(accountName, charName);
		if (_charNameList.containsKey(charName)) {
			_charNameList.remove(charName);
		}
		_log.finest("deleteCharacter");
	}

	public L1PcInstance restoreCharacter(String charName) throws Exception {
		return _charStorage.loadCharacter(charName);
	}

	public L1PcInstance loadCharacter(String charName) throws Exception {
		L1PcInstance pc = null;
		try {
			pc = restoreCharacter(charName);
			if (pc == null) {
				return null;
			}
			L1Map map = L1WorldMap.getInstance().getMap(pc.getMapId());
			if (!map.isInMap(pc.getX(), pc.getY())) {
				pc.setX(33087);
				pc.setY(33396);
				pc.setMap((short) 4);
			}
			_log.finest("loadCharacter: " + pc.getName());
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return pc;
	}
	
	public L1FakePcInstance loadFakeCharacter(String charName) throws Exception {
		L1FakePcInstance pc = null;
		try {
			pc = _charStorage.loadFakeCharacter(charName);
			if (pc == null) {
				return null;
			}
			L1Map map = L1WorldMap.getInstance().getMap(pc.getMapId());
			if (!map.isInMap(pc.getX(), pc.getY())) {
				pc.setX(33087);
				pc.setY(33396);
				pc.setMap((short) 4);
			}
			_log.finest("loadFakeCharacter: " + pc.getName());
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return pc;
	}


	public static void clearOnlineStatus() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET OnlineStatus=0");
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public static void updateOnlineStatus(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET OnlineStatus=1, lastLoginTime=now() WHERE objid=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 아이템 로드 메소드
	 * 인벤토리, 창고의 아이템 리스트 로드
	 * 로그인 외 호출시 아이템 복사가 발생될 수 잇으므로 신중하게 진행하십시오.
	 * @param pc
	 */
	public void restoreInventory(L1PcInstance pc) {
		pc.getInventory().loadItems();
		WarehouseManager manager = WarehouseManager.getInstance();
		String account_name = pc.getAccountName();
		PrivateWarehouse warehouse = manager.getPrivateWarehouse(account_name);
		warehouse.loadItems();
		ElfWarehouse elfwarehouse = manager.getElfWarehouse(account_name);
		elfwarehouse.loadItems();
		PackageWarehouse packwarehouse = manager.getPackageWarehouse(account_name);
		packwarehouse.loadItems();
		SpecialWarehouse specialwarehouse = manager.getSpecialWarehouse(pc.getName());
		specialwarehouse.loadItems();
		if (packwarehouse.getSize() > 0) {
			pc.sendPackets(S_GoodsInvenNoti.GOODS_INVEN_ON);
		}
	}

	public static boolean doesCharNameExist(String name) {
		boolean result = true;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT account_name FROM characters WHERE char_name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			_log.warning("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}
	
	public void loadAllCharName() {
		L1CharName cn = null;
		String name = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters");
			rs = pstm.executeQuery();
			while(rs.next()){
				cn = new L1CharName();
				name = rs.getString("char_name");
				cn.setName(name);
				cn.setId(rs.getInt("objid"));
				_charNameList.put(name, cn);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public int PcLevelInDB(int pcid) { // DB에 저장된 레벨값을 불러온다. 
	    int result = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
		    con = L1DatabaseFactory.getInstance().getConnection();
		    pstm = con.prepareStatement("SELECT level FROM characters WHERE objid=?");
		    pstm.setInt(1, pcid);
		    rs = pstm.executeQuery();
		    if (rs.next()) {
		    	result = rs.getInt(1);
		    }
		} catch (SQLException e) {
		    _log.warning("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
    }
	
	public int PcEXP(String charName) { //db에 저장된 경험치를 불러온다
	    int result = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
		    con = L1DatabaseFactory.getInstance().getConnection();
		    pstm = con.prepareStatement("SELECT Exp FROM characters WHERE char_name=?");
		    pstm.setString(1, charName);
		    rs = pstm.executeQuery();
		    if (rs.next()) {
		    	result = rs.getInt(1);
		    }
		} catch (SQLException e) {
		    _log.warning("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}

	public L1CharName[] getCharNameList() {
		return _charNameList.values().toArray(new L1CharName[_charNameList.size()]);
	}
	
	public void updateLoc(int castleid, int a, int b, int c, int d, int f) {
		Connection con = null;
		PreparedStatement pstm = null;
		int[] loc = new int[3];
		loc = L1CastleLocation.getGetBackLoc(castleid);
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			// pstm =
			// con.prepareStatement("UPDATE characters SET LocX=?, LocY=?, MapID=? WHERE OnlineStatus=0, MapID IN (?,?,?,?,?)");
			pstm = con.prepareStatement("UPDATE characters SET LocX=?, LocY=?, MapID=? WHERE OnlineStatus=0 AND (MapID=? OR MapID=? OR MapID=? OR MapID=? OR MapID=?)");
			pstm.setInt(1, loc[0]);
			pstm.setInt(2, loc[1]);
			pstm.setInt(3, loc[2]);
			pstm.setInt(4, a);
			pstm.setInt(5, b);
			pstm.setInt(6, c);
			pstm.setInt(7, d);
			pstm.setInt(8, f);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	/**
	 * objid로 케릭터 이름을 찾는다
	 * @param objid
	 * @return
	 */
	public String getCharName(int objid){
		String name = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT char_name FROM characters WHERE objid=?");
			pstm.setInt(1, objid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				name = rs.getString("char_name");
			}
		} catch (SQLException e) {
			_log.warning("could not getCharName:" + e.getMessage());
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return name;
	}
	
	public void CharacterAccountCheck(L1PcInstance pc, String charName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet loginRs = null;
		ResultSet characterRs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT login, password, phone FROM accounts WHERE ip = ");
			sb.append("(SELECT ip FROM accounts WHERE login = ");
			sb.append("(SELECT account_name FROM characters WHERE char_name = ?))");
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sb.toString());
			pstm.setString(1, charName);
			loginRs = pstm.executeQuery();
			while(loginRs.next()){
				pstm = con.prepareStatement("SELECT char_name, level, highlevel, clanname, onlinestatus FROM characters WHERE account_name = ?");
				pstm.setString(1, loginRs.getString("login"));
				characterRs = pstm.executeQuery();

				pc.sendPackets(new S_SystemMessage("----------------------------------------------------"), true);
				pc.sendPackets(new S_SystemMessage("\\fYAccounts : " + loginRs.getString("login") + ", PassWord : " + loginRs.getString("password") + ", " + loginRs.getString("phone")), true);
				String onlineStatus;
				while(characterRs.next()){
					onlineStatus = characterRs.getInt("onlinestatus") == 0 ? StringUtil.EmptyString : S_SystemMessage.getRefText(48);
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("* " + characterRs.getString("char_name") + " (Lv:" + characterRs.getInt("level") + ") (HLv:" + characterRs.getInt("highlevel") + ") " + "(혈맹:" + characterRs.getString("clanname") + ") " + "\\fY" + onlineStatus), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage("* " + characterRs.getString("char_name")  + " (Lv:" + characterRs.getInt("level")  + ") (HLv:" + characterRs.getInt("highlevel")  + ") " + S_SystemMessage.getRefText(942) + characterRs.getString("clanname")  + ") " + "\\fY" + onlineStatus, true), true);
				}
			}
			pc.sendPackets(new S_SystemMessage("----------------------------------------------------"), true);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(characterRs);
			SQLUtil.close(loginRs);			
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	/**한 계정에서 이중혈맹 가입 막기*/
	public ArrayList<Integer> CharacterClanChecked(String accountName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<Integer> clanIdList = new ArrayList<Integer>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT clanid FROM characters WHERE account_name = ?");
			pstm.setString(1, accountName);
			rs = pstm.executeQuery();
			while(rs.next()) {
				clanIdList.add(rs.getInt("clanid"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return clanIdList;
	}
	
	/**
	 * 봉인 아이템 보유 검증(캐릭터 삭제 시 호출)
	 * @param objid
	 * @return boolean
	 */
	public boolean is_seal_item_inventory(int objid){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT COUNT(*) AS cnt FROM character_items WHERE char_id = ? AND bless >= 128");
			pstm.setInt(1, objid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				int val = rs.getInt("cnt");
				if (val > 0) {
					return true;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}
	
	/**
	 * 인벤토리 아이템 삭제
	 * @param char_id
	 * @param id
	 * @return boolean
	 */
	public boolean delete_inventory_item(int id){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_items WHERE id = ?");
			pstm.setInt(1, id);
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean isContainNameList(String name) {
		return _charNameList.containsKey(name);
	}
	
	public void removeContainNameList(String name) {
		_charNameList.remove(name);
	}
	
	public void putContainNameList(String name, L1CharName cn) {
		_charNameList.put(name, cn);
	}
	
	public void CharacterNameChange(String sql){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
}



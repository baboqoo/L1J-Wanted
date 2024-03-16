package l1j.server.web.dispatcher.response.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.Gender;
import l1j.server.server.Account;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.controller.action.UserRanking;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1UserRanking;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.alim.AlimDAO;
import l1j.server.web.dispatcher.response.world.BloodPledgeDAO;
import l1j.server.web.dispatcher.response.world.BloodPledgeVO;

/**
 * 계정
 * @author LinOffice
 */
public class AccountDAO {
	private static AccountDAO _instance;
	public static AccountDAO getInstance() {
		if (_instance == null) {
			_instance = new AccountDAO();
		}
		return _instance;
	}
	
	private AccountDAO() {
	}
	
	// 웹 내 로그인
	public AccountVO getAccount(String login, String password) {
	    Account account	= Account.load(login);
	    if (account == null) {
	    	return null;
	    }
	    if (!account.validatePassword(password)) {
	    	return null;
	    }
		return getAccount(account, null);
	}
	
	// 인게임 내 로그인
	public AccountVO getAccount(Account account, L1PcInstance pc) {
	    CharacterVO firstChar		= null;
	    List<CharacterVO> charList	= account.getCharList();
	    if (charList != null && !charList.isEmpty()) {
	    	firstChar = get_first_character(pc, charList);
			int app_char = account.getAppChar();
			firstChar = firstChar != null ? firstChar : (app_char == 0 ? charList.get(0) : getCharacters(charList, app_char));
			account.setFirstChar(firstChar);
	    }
	    CharacterInventoryDAO inv = CharacterInventoryDAO.getInstance();
		List<CharacterInventoryVO> normalWarehouse	= inv.getInventoryList(account.getName(), ChracterInventoryType.NORMAL_WAREHOUSE);
		List<CharacterInventoryVO> packageWarehouse	= inv.getInventoryList(account.getName(), ChracterInventoryType.PACKAGE_WAREHOUSE);
		AccountVO vo	= new AccountVO(account.getName(), account.getPassword(), account.getIp(), charList, firstChar, account.getNcoin(), account.getNpoint(),
	    		account.getAccessLevel(), account.isBanned(), account.isTermsAgree(), false,
	    		AlimDAO.getList(account.getName()), normalWarehouse, packageWarehouse, account.getLoginSession());
		account.setAccountVO(vo);
		return vo;
	}
	
	/**
	 * 주 케릭터 검증
	 * @param pc
	 * @param charList
	 * @return CharacterVO
	 */
	CharacterVO get_first_character(L1PcInstance pc, List<CharacterVO> charList) {
		for (CharacterVO cha : charList) {
			// 게임 클라이언트에서 사용되는 케릭터
			if (pc != null && pc.getId() == cha.getObjId()) {
				return cha;
			}
			// 게임 월드내 접속중인 케릭터
			if (L1World.getInstance().getPlayer(cha.getName()) != null) {
				return cha;
			}
		}
		return null;
	}
	
	public CharacterVO getChar(String charName, boolean isGm){
		CharacterVO vo			= null;
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    ResultSet rs			= null;
		try {
			con	= L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT account_name, objid, char_name, level, Exp, MaxHp, MaxMp, Str, Con, Dex, Cha, Intel, Wis, Type, gender, ClanID, AccessLevel, OnlineStatus, Alignment, PKcount FROM characters WHERE char_name=?");
    		pstm.setString(1, charName);
    		rs = pstm.executeQuery();
    		if (rs.next()) {
    			String accountName		= rs.getString("account_name");
    			int objId				= rs.getInt("objid");
    			int level				= rs.getInt("level");
    			int exp					= rs.getInt("Exp");
    			int maxhp				= rs.getInt("MaxHp");
    			int maxmp				= rs.getInt("MaxMp");
    			int str					= rs.getInt("Str");
    			int conn				= rs.getInt("Con");
    			int dex					= rs.getInt("Dex");
    			int cha					= rs.getInt("Cha");
    			int intel				= rs.getInt("Intel");
    			int wis					= rs.getInt("Wis");
    			int type				= rs.getInt("Type");
    			String className		= L1CharacterInfo.L1Class.getNameEn(type);
    			Gender gender			= Gender.fromInt(rs.getInt("gender"));
    			int clanId				= rs.getInt("ClanID");
    			int accessLevel			= rs.getInt("AccessLevel");
    			boolean gm				= isGm || accessLevel == Config.ALT.GMCODE;
    			int alignment			= rs.getInt("Alignment");
    			int pk					= rs.getInt("PKcount");
    			boolean onlineStatus	= rs.getInt("OnlineStatus") == 1;
    			int expPercent			= ExpTable.getExpPercentage(level, exp);
    			String profileUrl		= getCharacterImg(type, gender);
    			BloodPledgeVO clan				= null;
    			if (clanId > 0) {
    				clan = BloodPledgeDAO.getPledge(clanId);
    			}
    			int allRank				= 0;
    			int classRank			= 0;
    			L1UserRanking rank		= UserRanking.getTotalRank(charName);
    			if (rank != null) {
    				allRank				= rank.getCurRank();
    			}
    			rank 					= UserRanking.getClassRank(type, charName);
    			if (rank != null) {
    				classRank			= rank.getCurRank();
    			}
    			
    			List<CharacterInventoryVO> inventory	= CharacterInventoryDAO.getInstance().getInventoryList(objId, ChracterInventoryType.INVENTOY);
    			List<CharacterMailVO> mail				= CharacterMailDAO.getInstance().getList(charName);
    			vo = new CharacterVO(accountName, objId, charName, level, exp, expPercent, maxhp, maxmp, 
    					str, conn, dex, cha, intel, wis, type, className, gender, 
    					clanId, clan, (clan == null ? StringUtil.EmptyString : clan.getPledgeName()), alignment, pk, accessLevel, onlineStatus, profileUrl, 
    					allRank, classRank, gm, inventory, mail);
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return vo;
	}
	
	public List<CharacterVO> getCharList(String accountName, boolean isGm){
		List<CharacterVO> list	= new ArrayList<CharacterVO>();
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    ResultSet rs			= null;
		try {
			con	= L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT objid, char_name, level, Exp, MaxHp, MaxMp, Str, Con, Dex, Cha, Intel, Wis, Type, gender, ClanID, AccessLevel, OnlineStatus, Alignment, PKcount FROM characters WHERE account_name=? ORDER BY level DESC");
    		pstm.setString(1, accountName);
    		rs = pstm.executeQuery();
    		CharacterVO vo = null;
    		while(rs.next()) {
    			int objId				= rs.getInt("objid");
    			String name				= rs.getString("char_name");
    			int level				= rs.getInt("level");
    			int exp					= rs.getInt("Exp");
    			int maxhp				= rs.getInt("MaxHp");
    			int maxmp				= rs.getInt("MaxMp");
    			int str					= rs.getInt("Str");
    			int conn				= rs.getInt("Con");
    			int dex					= rs.getInt("Dex");
    			int cha					= rs.getInt("Cha");
    			int intel				= rs.getInt("Intel");
    			int wis					= rs.getInt("Wis");
    			int type				= rs.getInt("Type");
    			String className		= L1CharacterInfo.L1Class.getNameEn(type);
    			Gender gender			= Gender.fromInt(rs.getInt("gender"));
    			int clanId				= rs.getInt("ClanID");
    			int accessLevel			= rs.getInt("AccessLevel");
    			boolean gm				= isGm || accessLevel == Config.ALT.GMCODE;
    			int alignment			= rs.getInt("Alignment");
    			int pk					= rs.getInt("PKcount");
    			boolean onlineStatus	= rs.getInt("OnlineStatus") == 1;
    			int expPercent			= ExpTable.getExpPercentage(level, exp);
    			String profileUrl		= getCharacterImg(type, gender);
    			BloodPledgeVO clan				= null;
    			if (clanId > 0) {
    				clan = BloodPledgeDAO.getPledge(clanId);
    			}
    			
    			int allRank				= 0;
    			int classRank			= 0;
    			L1UserRanking rank		= UserRanking.getTotalRank(name);
    			if (rank != null) {
    				allRank				= rank.getCurRank();
    			}
    			rank 					= UserRanking.getClassRank(type, name);
    			if (rank != null) {
    				classRank			= rank.getCurRank();
    			}
    			
    			List<CharacterInventoryVO> inventory	= CharacterInventoryDAO.getInstance().getInventoryList(objId, ChracterInventoryType.INVENTOY);
    			List<CharacterMailVO> mail				= CharacterMailDAO.getInstance().getList(name);
    			vo = new CharacterVO(accountName, objId, name, level, exp, expPercent, maxhp, maxmp, 
    					str, conn, dex, cha, intel, wis, type, className, gender, 
    					clanId, clan, (clan == null ? StringUtil.EmptyString : clan.getPledgeName()), alignment, pk, accessLevel, onlineStatus, profileUrl, 
    					allRank, classRank, gm, inventory, mail);
    			list.add(vo);
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return list;
	}
	
	public java.util.LinkedList<CharacterVO> getAllChracter() {
		java.util.LinkedList<CharacterVO> list	= new java.util.LinkedList<CharacterVO>();
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    ResultSet rs			= null;
		try {
			con	= L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT account_name, objid, char_name, level, Exp, MaxHp, MaxMp, Str, Con, Dex, Cha, Intel, Wis, Type, gender, ClanID, AccessLevel, OnlineStatus, Alignment, PKcount FROM characters WHERE account_name IS NOT NULL ORDER BY level DESC");
    		rs = pstm.executeQuery();
    		CharacterVO vo = null;
    		while(rs.next()) {
    			String account_name		= rs.getString("account_name");
    			int objId				= rs.getInt("objid");
    			String name				= rs.getString("char_name");
    			int level				= rs.getInt("level");
    			int exp					= rs.getInt("Exp");
    			int maxhp				= rs.getInt("MaxHp");
    			int maxmp				= rs.getInt("MaxMp");
    			int str					= rs.getInt("Str");
    			int conn				= rs.getInt("Con");
    			int dex					= rs.getInt("Dex");
    			int cha					= rs.getInt("Cha");
    			int intel				= rs.getInt("Intel");
    			int wis					= rs.getInt("Wis");
    			int type				= rs.getInt("Type");
    			String className		= L1CharacterInfo.L1Class.getNameEn(type);
    			Gender gender			= Gender.fromInt(rs.getInt("gender"));
    			int clanId				= rs.getInt("ClanID");
    			int accessLevel			= rs.getInt("AccessLevel");
    			boolean gm				= accessLevel == Config.ALT.GMCODE;
    			int alignment			= rs.getInt("Alignment");
    			int pk					= rs.getInt("PKcount");
    			boolean onlineStatus	= rs.getInt("OnlineStatus") == 1;
    			int expPercent			= ExpTable.getExpPercentage(level, exp);
    			String profileUrl		= getCharacterImg(type, gender);
    			BloodPledgeVO clan				= null;
    			if (clanId > 0) {
    				clan = BloodPledgeDAO.getPledge(clanId);
    			}
    			
    			int allRank				= 0;
    			int classRank			= 0;
    			L1UserRanking rank		= UserRanking.getTotalRank(name);
    			if (rank != null) {
    				allRank				= rank.getCurRank();
    			}
    			rank 					= UserRanking.getClassRank(type, name);
    			if (rank != null) {
    				classRank			= rank.getCurRank();
    			}
    			
    			List<CharacterInventoryVO> inventory	= CharacterInventoryDAO.getInstance().getInventoryList(objId, ChracterInventoryType.INVENTOY);
    			List<CharacterMailVO> mail				= CharacterMailDAO.getInstance().getList(name);
    			vo = new CharacterVO(account_name, objId, name, level, exp, expPercent, maxhp, maxmp, 
    					str, conn, dex, cha, intel, wis, type, className, gender, 
    					clanId, clan, (clan == null ? StringUtil.EmptyString : clan.getPledgeName()), alignment, pk, accessLevel, onlineStatus, profileUrl, 
    					allRank, classRank, gm, inventory, mail);
    			list.add(vo);
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return list;
	}
	
	private static final String PROFILE_FORMAT	= "/img/char/char%d_%s.png";
	private static final String[] GENDER_FLAG	= { "m", "f" };
	public static String getCharacterImg(int type, Gender gender) {
		return String.format(PROFILE_FORMAT, type, (gender == Gender.MALE ? GENDER_FLAG[0] : GENDER_FLAG[1]));
	}
	
	public CharacterVO getCharacters(List<CharacterVO> list, int objid) {
		CharacterVO charter = null;
		for (CharacterVO vo : list) {
			if (vo.getObjId() == objid) {
				charter = vo;
				break;
			}
		}
		return charter;
	}
	
	public void changeCharacter(AccountVO account, int chageId) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE accounts SET app_char=? WHERE login=?");
    		pstm.setInt(1, chageId);
    		pstm.setString(2, account.getName());
    		if (pstm.executeUpdate() > 0) {
    			account.setFirstChar(getCharacters(account.getCharList(), chageId));
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public boolean termsAgreeUpdate(AccountVO account) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE accounts SET app_terms_agree=? WHERE login=?");
    		pstm.setString(1, String.valueOf(account.isTermsAgree()));
    		pstm.setString(2, account.getName());
    		if (pstm.executeUpdate() > 0) {
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
}


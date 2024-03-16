package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1BalanceOption;
import l1j.server.server.utils.SQLUtil;

/**
 * 캐릭터 밸런스
 * @author LinOffice
 */
public class BalanceTable {
	private static Logger _log = Logger.getLogger(BalanceTable.class.getName());
	private static BalanceTable _instance;
	public static BalanceTable getInstance() {
		if (_instance == null) {
			_instance = new BalanceTable();
		}
		return _instance;
	}
	
	private static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, L1BalanceOption>> DATA = new ConcurrentHashMap<>();
	
	private static L1BalanceOption getOption(int attackerType, int targetType){
		ConcurrentHashMap<Integer, L1BalanceOption> map = DATA.get(attackerType);
		return map == null ? null : map.get(targetType);
	}
	
	/**
	 * 캐릭터간 물리 대미지
	 * @param attackerType
	 * @param targetType
	 * @return dmg
	 */
	public static int getPhysicalDmg(int attackerType, int targetType){
		L1BalanceOption option = getOption(attackerType, targetType);
		return option == null ? 0 : option.getPhysicalDmg() - (option.getPhysicalReduc() > 0 ? option.getPhysicalReduc() : 0);
	}
	
	/**
	 * 캐릭터간 물리 성공
	 * @param attackerType
	 * @param targetType
	 * @return hit
	 */
	public static int getPhysicalHit(int attackerType, int targetType){
		L1BalanceOption option = getOption(attackerType, targetType);
		return option == null ? 0 : option.getPhysicalHit();
	}
	
	/**
	 * 캐릭터간 마법 대미지
	 * @param attackerType
	 * @param targetType
	 * @return dmg
	 */
	public static int getMagicDmg(int attackerType, int targetType){
		L1BalanceOption option = getOption(attackerType, targetType);
		return option == null ? 0 : option.getMagicDmg() - (option.getMagicReduc() > 0 ? option.getMagicReduc() : 0);
	}
	
	/**
	 * 캐릭터간 마법 성공
	 * @param attackerType
	 * @param targetType
	 * @return hit
	 */
	public static int getMagicHit(int attackerType, int targetType){
		L1BalanceOption option = getOption(attackerType, targetType);
		return option == null ? 0 : option.getMagicHit();
	}

	private BalanceTable() {
		load();		
	}

	public void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1BalanceOption option	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM balance ORDER BY attackerType ASC, targetType ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				option = new L1BalanceOption(rs);
				ConcurrentHashMap<Integer, L1BalanceOption> map = DATA.get(option.getAttackerType());
				if (map == null) {
					map = new ConcurrentHashMap<>();
					DATA.put(option.getAttackerType(), map);
				}
				map.put(option.getTargetType(), option);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void reload() {
		for (ConcurrentHashMap<Integer, L1BalanceOption> value : DATA.values()) {
			value.clear();
		}
		DATA.clear();
		load();
	}
}

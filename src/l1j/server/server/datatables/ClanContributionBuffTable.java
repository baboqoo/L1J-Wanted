package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 혈맹 공헌도 버프 정보
 * @author LinOffice
 */
public final class ClanContributionBuffTable {
	
	public static class ContributionBuff {
		public String pledge_name;
		public int exp_buff_type, battle_buff_type, defens_buff_type;
		public Timestamp exp_buff_time, battle_buff_time, defens_buff_time;
		
		public ContributionBuff(String pledge_name, 
				int exp_buff_type, Timestamp exp_buff_time, 
				int battle_buff_type, Timestamp battle_buff_time, 
				int defens_buff_type, Timestamp defens_buff_time){
			this.pledge_name		= pledge_name;
			this.exp_buff_type		= exp_buff_type;
			this.exp_buff_time		= exp_buff_time;
			this.battle_buff_type	= battle_buff_type;
			this.battle_buff_time	= battle_buff_time;
			this.defens_buff_type	= defens_buff_type;
			this.defens_buff_time	= defens_buff_time;
		}
	}
	
	private static ClanContributionBuffTable _instance;

	private final static HashMap<Integer, ContributionBuff> BUFF_DATA = new HashMap<Integer, ContributionBuff>();

	public static ClanContributionBuffTable getInstance() {
		if (_instance == null) {
			_instance = new ClanContributionBuffTable();
		}
		return _instance;
	}
	
	private ClanContributionBuffTable() {
		loadBuff();
	}
	
	public static void reload() {
		_instance = new ClanContributionBuffTable();
		ClanContributionBuffTable.BUFF_DATA.clear();
	}

	/**
	 * MAP 정보를 받아와서 해쉬맵에 정보 저장한다, HashMap _maps에 격납한다.
	 */
	private void loadBuff() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		ContributionBuff data	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_contribution_buff");
			rs		= pstm.executeQuery();
			while (rs.next()){
				int pledge_id			= rs.getInt("clan_id");
				String pledge_name		= rs.getString("clan_name");
				int exp_buff_type		= rs.getInt("exp_buff_type");
				Timestamp exp_time		= rs.getTimestamp("exp_buff_time");
				int battle_buff_type	= rs.getInt("battle_buff_type");
				Timestamp battle_time	= rs.getTimestamp("battle_buff_time");
				int defens_buff_type	= rs.getInt("defens_buff_type");
				Timestamp defens_time	= rs.getTimestamp("defens_buff_time");
				data = new ContributionBuff(pledge_name, exp_buff_type, exp_time, battle_buff_type, battle_time, defens_buff_type, defens_time);
				BUFF_DATA.put(pledge_id, data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void insertBuff(int plege_id, ContributionBuff buff){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_contribution_buff SET clan_id=?, clan_name=?, exp_buff_type=?, exp_buff_time=?, battle_buff_type=?, battle_buff_time=?, defens_buff_type=?, defens_buff_time=?");
			int index = 0;
			pstm.setInt(++index, plege_id);
			pstm.setString(++index, buff.pledge_name);
			pstm.setInt(++index, buff.exp_buff_type);
			pstm.setTimestamp(++index, buff.exp_buff_time);
			pstm.setInt(++index, buff.battle_buff_type);
			pstm.setTimestamp(++index, buff.battle_buff_time);
			pstm.setInt(++index, buff.defens_buff_type);
			pstm.setTimestamp(++index, buff.defens_buff_time);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateBuff(int plege_id, ContributionBuff buff){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_contribution_buff SET exp_buff_type=?, exp_buff_time=?, battle_buff_type=?, battle_buff_time=?, defens_buff_type=?, defens_buff_time=? WHERE clan_id=?");
			int index = 0;
			pstm.setInt(++index, buff.exp_buff_type);
			pstm.setTimestamp(++index, buff.exp_buff_time);
			pstm.setInt(++index, buff.battle_buff_type);
			pstm.setTimestamp(++index, buff.battle_buff_time);
			pstm.setInt(++index, buff.defens_buff_type);
			pstm.setTimestamp(++index, buff.defens_buff_time);
			pstm.setInt(++index, plege_id);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void deleteBuff(int plege_id){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_contribution_buff WHERE clan_id=?");
			pstm.setInt(1, plege_id);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public ContributionBuff getContributionBuff(int plege_id){
		if (BUFF_DATA.containsKey(plege_id)) {
			return BUFF_DATA.get(plege_id);
		}
		return null;
	}
	
	public void addContributionBuff(int plege_id, ContributionBuff buff){
		BUFF_DATA.put(plege_id, buff);
	}
	
	public void removeContributionBuff(int plege_id){
		if (BUFF_DATA.containsKey(plege_id)) {
			BUFF_DATA.remove(plege_id);
		}
	}
	
	public int getBuffSize() {
		return BUFF_DATA.size();
	}
}

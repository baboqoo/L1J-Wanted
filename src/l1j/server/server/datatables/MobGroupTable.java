package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1MobGroup;
import l1j.server.server.utils.SQLUtil;

public class MobGroupTable {
	private static Logger _log = Logger.getLogger(MobGroupTable.class.getName());

	private static MobGroupTable _instance;

	private final HashMap<Integer, L1MobGroup> _mobGroupIndex = new HashMap<Integer, L1MobGroup>();

	public static MobGroupTable getInstance() {
		if (_instance == null) {
			_instance = new MobGroupTable();
		}
		return _instance;
	}

	private MobGroupTable() {
		loadMobGroup();
	}

	private void loadMobGroup() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			int mobGroupId = 0;
			L1MobGroup mobGroup;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM mobgroup");
			rs = pstm.executeQuery();
			while(rs.next()){
				mobGroup = new L1MobGroup();
				mobGroupId = rs.getInt("id");
				mobGroup.setId(mobGroupId);
				mobGroup.setRemoveGroupIfLeaderDie(rs.getBoolean("remove_group_if_leader_die"));
				mobGroup.setLeaderId(rs.getInt("leader_id"));
				mobGroup.setMinion1Id(rs.getInt("minion1_id"));
				mobGroup.setMinion1Count(rs.getInt("minion1_count"));
				mobGroup.setMinion2Id(rs.getInt("minion2_id"));
				mobGroup.setMinion2Count(rs.getInt("minion2_count"));
				mobGroup.setMinion3Id(rs.getInt("minion3_id"));
				mobGroup.setMinion3Count(rs.getInt("minion3_count"));
				mobGroup.setMinion4Id(rs.getInt("minion4_id"));
				mobGroup.setMinion4Count(rs.getInt("minion4_count"));
				mobGroup.setMinion5Id(rs.getInt("minion5_id"));
				mobGroup.setMinion5Count(rs.getInt("minion5_count"));
				mobGroup.setMinion6Id(rs.getInt("minion6_id"));
				mobGroup.setMinion6Count(rs.getInt("minion6_count"));
				mobGroup.setMinion7Id(rs.getInt("minion7_id"));
				mobGroup.setMinion7Count(rs.getInt("minion7_count"));
				mobGroup.setMinion8Id(rs.getInt("minion8_id"));
				mobGroup.setMinion8Count(rs.getInt("minion8_count"));
				_mobGroupIndex.put(mobGroupId, mobGroup);
			}
			//_log.config("MOB 그룹 리스트 " + _mobGroupIndex.size() + "건 로드");
			_log.config("MOB Group List " + _mobGroupIndex.size() + " entries loaded");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating mobgroup table", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1MobGroup getTemplate(int mobGroupId) {
		return _mobGroupIndex.get(mobGroupId);
	}

}


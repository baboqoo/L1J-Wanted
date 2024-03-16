package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1SpellProbabilityInfo;
import l1j.server.server.utils.SQLUtil;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;


public class SpellProbabilityTable {
	private static Logger _log = Logger.getLogger(SpellProbabilityTable.class.getName());    

	private static SpellProbabilityTable _instance;

	public static SpellProbabilityTable getInstance() {
		if(_instance == null)
			_instance = new SpellProbabilityTable();
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new SpellProbabilityTable();
		}
	}
	
	private HashMap<Integer, L1SpellProbabilityInfo> _spells;
	private SpellProbabilityTable() {
		load();
	}
	
	private void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM probability_by_spell");
			rs		= pstm.executeQuery();
			FillSpellsTable(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating spells probability table", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void FillSpellsTable(ResultSet rs) throws SQLException {
        final HashMap<Integer, L1SpellProbabilityInfo> spells = new HashMap<Integer, L1SpellProbabilityInfo>(256);
        L1SpellProbabilityInfo pInfo;
		while(rs.next()){
            pInfo = new L1SpellProbabilityInfo(rs);
            if(pInfo == null)
                continue;
            
            spells.put(pInfo.get_skill_id(), pInfo);
		}
        _spells = spells;
		_log.config("Spells Probability " + _spells.size() + " entries loaded");
	}

	
	public int calc_probability(int skill_id, L1PcInstance pc, L1Character target, int attacker_int, int target_mr) {
		L1SpellProbabilityInfo pInfo = _spells.get(skill_id);
		return pInfo == null ? -1 : pInfo.calc_probability(pc, target, attacker_int, target_mr);
	}
	
	public boolean contains_probability(int skill_id) {
		return _spells.containsKey(skill_id);
	}    
}

package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class SpamTable {
    private final static Logger _log = Logger.getLogger(SpamTable.class.getName());

    private static SpamTable _instance;

    private final Map<Integer, L1ExcludingList> _excludes = new HashMap<Integer, L1ExcludingList>();

    public static SpamTable getInstance() {
        if (_instance == null) {
        	_instance = new SpamTable();
        }
        return _instance;
    }

    private SpamTable() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT DISTINCT(char_id) AS char_id FROM character_exclude");

            rs = pstm.executeQuery();
            PreparedStatement excludePS = null;
            ResultSet excludeRS = null;
            while (rs.next()) {
                try {
                    excludePS = con.prepareStatement("SELECT exclude_id, exclude_name FROM character_exclude WHERE char_id = ?");
                    int charId = rs.getInt("char_id");
                    excludePS.setInt(1, charId);
                    L1ExcludingList exclude = new L1ExcludingList(charId);
                    excludeRS = excludePS.executeQuery();
                    while (excludeRS.next()) {
                        exclude.add(excludeRS.getInt("exclude_id"), excludeRS.getString("exclude_name"));
                    }
                    _excludes.put(exclude.getCharId(), exclude);
                } catch (Exception e) {
                    _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
                } finally {
                    SQLUtil.close(excludeRS, excludePS);
                }
            }
            _log.config("loaded " + _excludes.size() + " character's excludelists");
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
    }

    public L1ExcludingList getExcludeTable(int charId) {
        L1ExcludingList exclude = _excludes.get(charId);
        if (exclude == null) {
            exclude = new L1ExcludingList(charId);
            _excludes.put(charId, exclude);
        }
        return exclude;
    }

    public void addExclude(int charId, int objId, String name) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO character_exclude SET char_id=?, exclude_id=?, exclude_name=?");
            pstm.setInt(1, charId);
            pstm.setInt(2, objId);
            pstm.setString(3, name);
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
        	SQLUtil.close(pstm, con);
        }
    }
    
    public void insertExclude(L1PcInstance pc, int subType, int objId, String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_exclude SET char_id=?, type=?, exclude_id=?, exclude_name=?");
			pstm.setInt(1, pc.getId());
			pstm.setInt(2, subType);
			pstm.setInt(3, objId);
			pstm.setString(4, name);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

    public boolean ExcludeList(int charId, String spamname) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM character_exclude WHERE char_id=? AND exclude_name=?");
            pstm.setInt(1, charId);
            pstm.setString(2, spamname);
            rs = pstm.executeQuery();
            if (rs.next()) {
            	return true;
            }
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
        	SQLUtil.close(rs, pstm, con);
        }
        return false;
    }
    
    public void deleteExclude(L1PcInstance pc, int subType, String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_exclude WHERE char_id=? AND type=? AND exclude_name=?");
			pstm.setInt(1, pc.getId());
			pstm.setInt(2, subType);
			pstm.setString(3, name);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}


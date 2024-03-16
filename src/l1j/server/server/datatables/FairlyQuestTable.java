package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class FairlyQuestTable {
	private static Logger _log = Logger.getLogger(FairlyQuestTable.class.getName());
	
	private static FairlyQuestTable _instance;
	public static FairlyQuestTable getInstance(){
		if (_instance == null) {
			_instance = new FairlyQuestTable();
		}
		return _instance;
	}
	
	private FairlyQuestTable(){
	}
	
	public int fairlycount(int objectId) {
        int result = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT count(*) as cnt FROM character_Fairly_Config WHERE object_id=?");
            pstm.setInt(1, objectId);
            rs = pstm.executeQuery();
            if(rs.next())result = rs.getInt("cnt");
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        return result;
    }

    public void fairlystore(int objectId, byte[] data) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO character_Fairly_Config SET object_id=?, data=?");
            pstm.setInt(1, objectId);
            pstm.setBytes(2, data);
            pstm.executeUpdate();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
        	SQLUtil.close(pstm, con);
        }
    }

    public void fairlupdate(int objectId, byte[] data) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("UPDATE character_Fairly_Config SET data=? WHERE object_id=?");
            pstm.setBytes(1, data);
            pstm.setInt(2, objectId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            // _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
        	SQLUtil.close(pstm, con);
        }
    }
}


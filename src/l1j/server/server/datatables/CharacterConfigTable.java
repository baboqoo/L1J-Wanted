package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class CharacterConfigTable {
	private static Logger _log = Logger.getLogger(CharacterConfigTable.class.getName());

	private static CharacterConfigTable _instance;
	public static CharacterConfigTable getInstance() {
		if (_instance == null) {
			_instance = new CharacterConfigTable();
		}
		return _instance;
	}

	private CharacterConfigTable() {}

	public void upsertCharacterConfig(int objectId, int length, byte[] data) {// 데이터 없으면 insert or 있으면 update
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_config (object_id, length, data) VALUES (?,?,?) ON DUPLICATE KEY UPDATE length=?, data=?");
			pstm.setInt(1, objectId);
			pstm.setInt(2, length);
			pstm.setBytes(3, data);
			pstm.setInt(4, length);
			pstm.setBytes(5, data);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void deleteCharacterConfig(int objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_config WHERE object_id=?");
			pstm.setInt(1, objectId);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

}


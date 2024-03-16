package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.SQLUtil;

public class KeyTable {
	private static Logger _log = Logger.getLogger(KeyTable.class.getName());

	private KeyTable() {}

	public static void storeKey(L1ItemInstance item) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("INSERT INTO item_key_boss SET item_obj_id=?, key_id=?");
			pstm.setInt(1, item.getId());
			pstm.setInt(2, item.getKeyId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public static void deleteKey(L1ItemInstance item) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM item_key_boss WHERE item_obj_id=?");
			pstm.setInt(1, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void deleteKeyId(int keyId) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM item_key_boss WHERE key_id=?");
			pstm.setInt(1, keyId);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void initBossKey() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		PreparedStatement pstm1	= null;
		PreparedStatement pstm2	= null;
		PreparedStatement pstm3	= null;
		PreparedStatement pstm4	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("TRUNCATE `item_key_boss`;");// 테이블 초기화
			pstm1	= con.prepareStatement("DELETE FROM character_items WHERE item_id = 80500");
			pstm2	= con.prepareStatement("DELETE FROM character_elf_warehouse WHERE item_id = 80500");
			pstm3	= con.prepareStatement("DELETE FROM clan_warehouse WHERE item_id = 80500");
			pstm4	= con.prepareStatement("DELETE FROM character_warehouse WHERE item_id = 80500");
			pstm.execute();
			pstm1.execute();
			pstm2.execute();
			pstm3.execute();
			pstm4.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(pstm3);
			SQLUtil.close(pstm4);
			SQLUtil.close(con);
		}
	}
	
	public static void updateKey(L1ItemInstance item) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE item_key_boss SET key_id=? WHERE item_obj_id=?");
			pstm.setInt(1, item.getKeyId());
			pstm.setInt(2, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public static boolean checKey(L1ItemInstance item) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT key_id FROM item_key_boss WHERE item_obj_id=?");
			pstm.setInt(1, item.getId());
			rs		= pstm.executeQuery();
			if (rs.next()) {
				item.setKeyId(rs.getInt("key_id"));
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}

}


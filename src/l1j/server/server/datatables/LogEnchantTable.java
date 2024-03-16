package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class LogEnchantTable {
	private static Logger _log = Logger.getLogger(LogEnchantTable.class.getName());
	
	public static void storeLogEnchant(int char_id, int item_id, int old_enchantlvl, int new_enchantlvl) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO log_enchant SET char_id=?, item_id=?, old_enchantlvl=?, new_enchantlvl=?");
			pstm.setInt(1, char_id);
			pstm.setInt(2, item_id);
			pstm.setInt(3, old_enchantlvl);
			pstm.setInt(4, new_enchantlvl);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

}


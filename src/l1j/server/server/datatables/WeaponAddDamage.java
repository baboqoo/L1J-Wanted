package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Weapon;
import l1j.server.server.utils.SQLUtil;

public class WeaponAddDamage {
	private static Logger _log = Logger.getLogger(WeaponAddDamage.class.getName());

	private static WeaponAddDamage _instance;
	public static WeaponAddDamage getInstance() {
		if (_instance == null) {
			_instance = new WeaponAddDamage();
		}
		return _instance;
	}

	private WeaponAddDamage() {
		load();
	}

	public void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1Item item				= null;
		ItemTable temp			= ItemTable.getInstance();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT item_id, addDamege FROM weapon_damege");
			rs = pstm.executeQuery();
			while(rs.next()){
				item = temp.getTemplate(rs.getInt("item_id"));
				if (item == null) {
					continue;
				}
				((L1Weapon)item).setWeaponAddDamage(rs.getInt("addDamege"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static void reload() {
		_instance = new WeaponAddDamage();
	}
}


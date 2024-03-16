package l1j.server.server.datatables;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class LightSpawnTable {
	private static Logger _log = Logger.getLogger(LightSpawnTable.class.getName());

	private static LightSpawnTable _instance;

	public static LightSpawnTable getInstance() {
		if (_instance == null) {
			_instance = new LightSpawnTable();
		}
		return _instance;
	}

	private LightSpawnTable() {
		FillLightSpawnTable();
	}

	private void FillLightSpawnTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_light");
			rs = pstm.executeQuery();
			do {
				if (!rs.next()) {
					break;
				}
				L1Npc l1npc = NpcTable.getInstance().getTemplate(rs.getInt(2));
				if (l1npc != null) {
					String s = l1npc.getImpl();
					Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
					Object parameters[] = { l1npc };
					L1FieldObjectInstance field = (L1FieldObjectInstance) constructor.newInstance(parameters);
					field = (L1FieldObjectInstance) constructor.newInstance(parameters);
					field.setId(IdFactory.getInstance().nextId());
					field.setX(rs.getInt("locx"));
					field.setY(rs.getInt("locy"));
					field.setMap((short) rs.getInt("mapid"));
					field.setHomeX(field.getX());
					field.setHomeY(field.getY());
					field.getMoveState().setHeading(0);
					field.setLightSize(l1npc.getLightSize());

					L1World.getInstance().storeObject(field);
					L1World.getInstance().addVisibleObject(field);
				}
			} while (true);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (ClassNotFoundException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (IllegalArgumentException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (InstantiationException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (IllegalAccessException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (InvocationTargetException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}


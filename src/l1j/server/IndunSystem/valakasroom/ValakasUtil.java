package l1j.server.IndunSystem.valakasroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class ValakasUtil {
	private static Logger _log = Logger.getLogger(ValakasUtil.class.getName());
	private static ValakasUtil _instance;

	public static ValakasUtil getInstance() {
		if (_instance == null) {
			_instance = new ValakasUtil();
		}
		return _instance;
	}

	private ValakasUtil() {}

	public void fillSpawnTable(int mapid, int type) {
		fillSpawnTable(mapid, type, false);
	}

	public ArrayList<L1NpcInstance> fillSpawnTable(int mapid, int type, boolean RT) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		ArrayList<L1NpcInstance> list = null;
		if(RT)
			list = new ArrayList<L1NpcInstance>();

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_valakas_room");
			rs = pstm.executeQuery();
			while(rs.next()){
				if (type != rs.getInt("type")) continue;

				l1npc = NpcTable.getInstance().getTemplate(rs.getInt("npc_id"));
				if (l1npc != null) {
					try {
						field = NpcTable.getInstance().newNpcInstance(rs.getInt("npc_id"));
						field.setId(IdFactory.getInstance().nextId());
						field.setX(rs.getInt("locx"));
						field.setY(rs.getInt("locy"));
						field.setMap((short) mapid);
						if(field instanceof L1DoorInstance){
							L1DoorInstance door = (L1DoorInstance) field;
							door.setDoorId(rs.getInt("npc_id"));
							if(door.getDoorId() == 7210013){
								door.setDirection(1);
								door.setLeftEdgeLocation(32999);
								door.setRightEdgeLocation(33001);
							}else if(door.getDoorId() == 7210014){
								door.setDirection(1);
								door.setLeftEdgeLocation(33051);
								door.setRightEdgeLocation(33053);
							}else if(door.getDoorId() == 7210015){
								door.setDirection(0);
								door.setLeftEdgeLocation(32734);
								door.setRightEdgeLocation(32738);
							}
							door.isPassibleDoor(false);
						}
						field.setHomeX(field.getX());
						field.setHomeY(field.getY());
						field.getMoveState().setHeading(rs.getInt("heading"));
						field.setLightSize(l1npc.getLightSize());
						field.getLight().turnOnOffLight();
						L1World.getInstance().storeObject(field);
						L1World.getInstance().addVisibleObject(field);
						if(RT)
							list.add(field);
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
				l1npc = null;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (IllegalArgumentException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		return list;
	}
}


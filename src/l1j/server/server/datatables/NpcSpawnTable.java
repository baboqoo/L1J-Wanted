package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.PerformanceTimer;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class NpcSpawnTable {
	private static Logger _log = Logger.getLogger(NpcSpawnTable.class.getName());
	private static NpcSpawnTable _instance;
	public boolean isReload = false;
	private Map<Integer, L1Spawn> _spawntable = new HashMap<Integer, L1Spawn>();
	private int _highestId;
	
	public static NpcSpawnTable getInstance() {
		if (_instance == null) {
			_instance = new NpcSpawnTable();
		}
		return _instance;
	}
	
	public static void reload() {
		NpcSpawnTable oldInstance = _instance;
		_instance = new NpcSpawnTable();
		oldInstance._spawntable.clear();
	}
	
	public void reload1() {
		PerformanceTimer timer = new PerformanceTimer();
		System.out.print("loading " + _log.getName().substring(_log.getName().lastIndexOf(StringUtil.PeriodString) + 1) + "...");
		_instance._spawntable.clear();
		isReload = true;
		fillNpcSpawnTable();
		System.out.println("OK! " + timer.get() + " ms");
	}

	private NpcSpawnTable() {
		fillNpcSpawnTable();
	}

	private void fillNpcSpawnTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_npc");
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				int npcTemplateid = rs.getInt("npc_templateid");
				if (Config.ALT.ALT_GMSHOP == false && (id >= Config.ALT.ALT_GMSHOP_MIN_ID && id <= Config.ALT.ALT_GMSHOP_MAX_ID)) {
					continue;
				}
				if (Config.ALT.ALT_BASETOWN == false && (id >= Config.ALT.ALT_BASETOWN_MIN_ID && id <= Config.ALT.ALT_BASETOWN_MAX_ID)) {
					continue;
				}
				if (Config.ALT.ALT_HALLOWEENIVENT == false && (id >= 6000007 && id <= 6000009)) {
					continue;
				}
				if (Config.ALT.ALT_FANTASYEVENT == false && (id >= 6000000 && id <= 6000006)) {
					continue;
				}
				if (Config.ETC.FISH_LOC_TOWN == false && (npcTemplateid >= 73341 && npcTemplateid <= 73345)) {
					continue; 
				}
				if (Config.ALT.ALT_RABBITEVENT == false && (id >= 1310387 && id <= 1310414)) { //신묘 이벤트
					continue;
				}
				if (Config.ALT.DOLL_RACE_ENABLED == false && (npcTemplateid == 70035 || npcTemplateid == 70041 || npcTemplateid == 70042)) {
					continue;
				}
				if (Config.ALT.DOG_FIGHT_ENABLED == false && (npcTemplateid == 170041 || npcTemplateid == 170042)) {
					continue;
				}
				if (Config.DUNGEON.BATTLE_ZONE_ACTIVE == false && npcTemplateid == 7000096) {
					continue;
				}
				if (Config.DUNGEON.TREASURE_ISLAND_ACTIVE == false && (npcTemplateid == 18306 || npcTemplateid == 18308 || npcTemplateid == 18309)) {
					continue;
				}
				if (Config.ETC.EVENT_LEVEL_100 == false && (npcTemplateid == 38391 || npcTemplateid == 38392 || npcTemplateid == 38393 || npcTemplateid == 38394 || npcTemplateid == 38395)) {
					continue;
				}
				if (Config.SERVER.PROFIT_SERVER_ACTIVE == false && (npcTemplateid == 40000 || npcTemplateid == 900504)) {
					continue;
				}
				
				L1Npc template = NpcTable.getInstance().getTemplate(npcTemplateid);
				if (template == null) {
					_log.warning("spawnlist_npc mob data for id:" + npcTemplateid + " missing in npc table");
					continue;
				}
				if (rs.getInt("count") == 0) {
					continue;
				}
				createSpawnData(template, id, rs.getInt("count"), 
						rs.getInt("locx"), rs.getInt("locy"), rs.getInt("randomx"), rs.getInt("randomy"), 
						rs.getInt("heading"), rs.getInt("respawn_delay"), rs.getShort("mapid"), 
						rs.getInt("movement_distance"), true);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (ClassNotFoundException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		//_log.config("NPC 배치 리스트 " + _spawntable.size() + "건 로드");
		_log.config("NPC Placement List " + _spawntable.size() + " entries loaded");
	}
	
	L1Spawn createSpawnData(L1Npc template, int id, int count, 
			int locx, int locy, int randomx, int randomy,
			int heading, int respawn_delay, short mapid,
			int movement_distance, boolean is_spawn) throws SecurityException, ClassNotFoundException {
		L1Spawn spawn = new L1Spawn(template);
		spawn.setId(id);
		spawn.setAmount(count);
		spawn.setLocX(locx);
		spawn.setLocY(locy);
		spawn.setRandomx(randomx);
		spawn.setRandomy(randomy);
		spawn.setLocX1(0);
		spawn.setLocY1(0);
		spawn.setLocX2(0);
		spawn.setLocY2(0);
		spawn.setHeading(heading);
		spawn.setMinRespawnDelay(respawn_delay);
		spawn.setMapId(mapid);
		spawn.setMovementDistance(movement_distance);
		//spawn.setName(template.getDescKr());
		spawn.setName(template.getDescEn());
		spawn.init(is_spawn);
		_spawntable.put(new Integer(spawn.getId()), spawn);
		if (spawn.getId() > _highestId) {
			_highestId = spawn.getId();
		}
		return spawn;
	}
	
	public L1Spawn storeSpawn(L1PcInstance pc, L1Npc npc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO spawnlist_npc SET location=?, npc_templateid=?, locx=?, locy=?, mapid=?, heading=?, count=1");
			int index = 0;
			//pstm.setString(++index, npc.getDescKr());
			pstm.setString(++index, npc.getDescEn());
			pstm.setInt(++index, npc.getNpcId());
			pstm.setInt(++index, pc.getX());
			pstm.setInt(++index, pc.getY());
			pstm.setInt(++index, pc.getMapId());
			pstm.setInt(++index, pc.getMoveState().getHeading());
			pstm.execute();
			SQLUtil.close(pstm);
			index = 0;
			
			pstm = con.prepareStatement("SELECT id FROM spawnlist_npc WHERE npc_templateid=? AND locx=? AND locy=? AND mapid=? AND heading=? AND count=1");
			pstm.setInt(++index, npc.getNpcId());
			pstm.setInt(++index, pc.getX());
			pstm.setInt(++index, pc.getY());
			pstm.setInt(++index, pc.getMapId());
			pstm.setInt(++index, pc.getMoveState().getHeading());
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return null;
			}
			int id = rs.getInt("id");
			return createSpawnData(npc, id, 1, 
					pc.getX(), pc.getY(), 0, 0, 
					pc.getMoveState().getHeading(), 0, pc.getMapId(), 
					0, false);
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return null;
	}
	
	public boolean removeSpawn(int id) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM spawnlist_npc WHERE id = ?");
			pstm.setInt(1, id);
			if (pstm.executeUpdate() > 0) {
				_spawntable.remove(id);
				return true;
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean removeSpawn(L1NpcInstance paramL1NpcInstance) {
		L1Spawn spawn = paramL1NpcInstance.getSpawn();
		if (spawn != null) {
			return removeSpawn(spawn.getId());
		}
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT id FROM spawnlist_npc WHERE npc_templateid=? AND mapid=? AND locx=? AND locy=?");
			pstm.setInt(1, paramL1NpcInstance.getNpcId());
			pstm.setInt(2, paramL1NpcInstance.getMapId());
			pstm.setInt(3, paramL1NpcInstance.getX());
			pstm.setInt(4, paramL1NpcInstance.getY());
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return false;
			}
			int id = rs.getInt("id");
			this._spawntable.remove(Integer.valueOf(id));
			SQLUtil.close(rs, pstm);
			pstm = con.prepareStatement("DELETE FROM spawnlist_npc WHERE id=?");
			pstm.setInt(1, id);
			pstm.execute();
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}

	public L1Spawn getTemplate(int i) {
		return _spawntable.get(i);
	}

	public void addNewSpawn(L1Spawn l1spawn) {
		_highestId++;
		l1spawn.setId(_highestId);
		_spawntable.put(l1spawn.getId(), l1spawn);
	}

}


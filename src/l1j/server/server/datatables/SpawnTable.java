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
import l1j.server.server.templates.L1NpcNight;
import l1j.server.server.utils.NumberUtil;
import l1j.server.server.utils.PerformanceTimer;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class SpawnTable {
	private static Logger _log = Logger.getLogger(SpawnTable.class.getName());
	private static SpawnTable _instance;
	private Map<Integer, L1Spawn> _spawntable					= new HashMap<>();
	private static final Map<Integer, L1NpcNight> _spawnDay		= new HashMap<>();
	private static final Map<Integer, L1NpcNight> _spawnNight	= new HashMap<>();
	private int _highestId;
	public boolean isReload;
	
	public static Map<Integer, L1NpcNight> getDayNpcs(){
		return _spawnDay;
	}
	
	public static Map<Integer, L1NpcNight> getNightNpcs(){
		return _spawnNight;
	}
	
	public static L1Npc getDayNpc(int npcId){
		L1NpcNight night = _spawnDay.get(npcId);
		if (night == null) {
			return null;
		}
		return night.getDayTemplate();
	}
	
	public static L1Npc getDayNpc(int npcId, int mapId){
		L1NpcNight night = _spawnDay.get(npcId);
		if (night == null || night.getTargetMapId() != mapId) {
			return null;
		}
		return night.getDayTemplate();
	}
	
	public static L1Npc getNightNpc(int npcId){
		L1NpcNight night = _spawnNight.get(npcId);
		if (night == null) {
			return null;
		}
		return night.getNightTemplate();
	}
	
	public static L1Npc getNightNpc(int npcId, int mapId){
		L1NpcNight night = _spawnNight.get(npcId);
		if (night == null || night.getTargetMapId() != mapId) {
			return null;
		}
		return night.getNightTemplate();
	}
	
	public static SpawnTable getInstance() {
		if (_instance == null) {
			_instance = new SpawnTable();
		}
		return _instance;
	}

	private SpawnTable() {
//		PerformanceTimer timer = new PerformanceTimer();
//		System.out.print("■ 스폰 테이블 .......................... ");
		fillSpawnTable();
		nightNpcTable();
//		_log.config("배치 리스트 " + _spawntable.size() + "건 로드");
//		System.out.println("■ 로딩 정상 완료 " + timer.get() + " ms");
	}
	
	public static void reload() {
		_spawnDay.clear();
		_spawnNight.clear();
		SpawnTable oldInstance = _instance;
		_instance = new SpawnTable();
		oldInstance._spawntable.clear();
	}
	
	public void reload1() {
		_spawnDay.clear();
		_spawnNight.clear();
		PerformanceTimer timer = new PerformanceTimer();
		System.out.print("loading " + _log.getName().substring(_log.getName().lastIndexOf(StringUtil.PeriodString) + 1) + "...");
		_instance._spawntable.clear();
		isReload = true;
		fillSpawnTable();
		System.out.println("OK! " + timer.get() + " ms");
	}
	
	private void nightNpcTable(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1NpcNight dayNight		= null;
		L1Npc dayTemplate, nightTemplate;
		NpcTable temp			= NpcTable.getInstance();
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM npc_night ORDER BY npcId ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int npcId		= rs.getInt("npcId");
				dayTemplate		= temp.getTemplate(npcId);
				if (dayTemplate == null) {
					_log.warning(String.format("[SpawnTable: npc_night] dayNpc data for id:%d missing in npc table", npcId));
					continue;
				}
				int targetId	= rs.getInt("targetId");
				nightTemplate	= temp.getTemplate(targetId);
				if (nightTemplate == null) {
					_log.warning(String.format("[SpawnTable: npc_night] nightNpc data for id:%d missing in npc table", targetId));
					continue;
				}
				int targetMapId	= rs.getInt("targetMapId");
				dayNight = new L1NpcNight(npcId, dayTemplate, targetId, nightTemplate, targetMapId);
				_spawnDay.put(targetId,	dayNight);
				_spawnNight.put(npcId,	dayNight);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void fillSpawnTable() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM spawnlist");
			rs		= pstm.executeQuery();

			L1Npc template;
			while (rs.next()) {
				int npcid = rs.getInt("npc_templateid");
				if (!Config.ALT.ALT_HALLOWEENIVENT && (npcid == 45166 || npcid == 45167)) {
					continue;
				}
				template = NpcTable.getInstance().getTemplate(npcid);
				int count;
				if (template == null) {
					_log.warning("mob data for id:" + npcid + " missing in npc table");
					continue;
				}
				if (rs.getInt("count") == 0) {
					continue;
				}
				double amount_rate = MapsTable.getInstance().getMonsterAmount(rs.getShort("mapid"));
				count = calcCount(template, rs.getInt("count"), amount_rate);
				if (count == 0) {
					continue;
				}
				
				createSpawnData(template, rs.getInt("id"), count, rs.getInt("group_id"), 
						rs.getInt("locx"), rs.getInt("locy"), rs.getInt("randomx"), rs.getInt("randomy"), 
						rs.getInt("locx1"), rs.getInt("locy1"), rs.getInt("locx2"), rs.getInt("locy2"), rs.getInt("heading"), 
						rs.getInt("min_respawn_delay"), rs.getInt("max_respawn_delay"), rs.getShort("mapid"), 
						rs.getBoolean("respawn_screen"), rs.getInt("movement_distance"), 
						rs.getBoolean("rest"), rs.getInt("near_spawn"), true);
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
	}
	
	L1Spawn createSpawnData(L1Npc template, int id, int count, int group_id, 
			int locx, int locy, int randomx, int randomy, 
			int locx1, int locy1, int locx2, int locy2, int heading,
			int min_respawn_delay, int max_respawn_delay, short mapid, 
			boolean respawn_screen, int movement_distance,
			boolean rest, int near_spawn, boolean is_spawn) throws SecurityException, ClassNotFoundException {
		L1Spawn spawnDat = new L1Spawn(template);
		spawnDat.setId(id);
		spawnDat.setAmount(count);
		spawnDat.setGroupId(group_id);
		spawnDat.setLocX(locx);
		spawnDat.setLocY(locy);
		spawnDat.setRandomx(randomx);
		spawnDat.setRandomy(randomy);
		spawnDat.setLocX1(locx1);
		spawnDat.setLocY1(locy1);
		spawnDat.setLocX2(locx2);
		spawnDat.setLocY2(locy2);
		spawnDat.setHeading(heading);
		spawnDat.setMinRespawnDelay(min_respawn_delay);
		spawnDat.setMaxRespawnDelay(max_respawn_delay);
		spawnDat.setMapId(mapid);
		spawnDat.setRespawnScreen(respawn_screen);
		spawnDat.setMovementDistance(movement_distance);
		spawnDat.setRest(rest);
		spawnDat.setSpawnType(near_spawn);
		//spawnDat.setName(template.getDescKr());
		spawnDat.setName(template.getDescEn());
		if (count > 1 && spawnDat.getLocX1() == 0) {
			// 복수 또한 고정 spawn의 경우는, 개체수 * 6 의 범위 spawn로 바꾼다.
			// 다만 범위가 30을 넘지 않게 한다
			int range = Math.min(count * 6, 30);
			spawnDat.setLocX1(spawnDat.getLocX() - range);
			spawnDat.setLocY1(spawnDat.getLocY() - range);
			spawnDat.setLocX2(spawnDat.getLocX() + range);
			spawnDat.setLocY2(spawnDat.getLocY() + range);
		}
		
		// start the spawning
		spawnDat.init(is_spawn);
		_spawntable.put(new Integer(spawnDat.getId()), spawnDat);
		if (spawnDat.getId() > _highestId) {
			_highestId = spawnDat.getId();
		}
		return spawnDat;
	}

	public L1Spawn getTemplate(int Id) {
		return _spawntable.get(new Integer(Id));
	}

	public void addNewSpawn(L1Spawn spawn) {
		_highestId++;
		spawn.setId(_highestId);
		_spawntable.put(new Integer(spawn.getId()), spawn);
	}

	public L1Spawn storeSpawn(L1PcInstance pc, L1Npc npc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("INSERT INTO spawnlist SET location=?, npc_templateid=?, locx=?, locy=?, mapid=?, heading=?, count=1, randomx=12, randomy=12, min_respawn_delay=30, max_respawn_delay=60");
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
			
			pstm = con.prepareStatement("SELECT id FROM spawnlist WHERE npc_templateid=? AND locx=? AND locy=? AND mapid=? AND heading=? AND count=1 AND randomx=12 AND randomy=12 AND min_respawn_delay=30 AND max_respawn_delay=60");
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
			return createSpawnData(npc, id, 1, 0, 
						pc.getX(), pc.getY(), 0, 0, 
						0, 0, 0, 0, pc.getMoveState().getHeading(), 
						30, 60, pc.getMapId(), 
						false, 0, 
						false, 0, false);
			
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (ClassNotFoundException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return null;
	}
	
	public boolean removeSpawn(int id){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM spawnlist WHERE id = ?");
			pstm.setInt(1, id);
			if (pstm.executeUpdate() > 0) {
				_spawntable.remove(id);
				return true;
			}
		} catch (SQLException e) {
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
			pstm = con.prepareStatement("SELECT id FROM spawnlist WHERE npc_templateid=? AND mapid=? AND locx=? AND locy=?");
			pstm.setInt(1, paramL1NpcInstance.getNpcId());
			pstm.setInt(2, paramL1NpcInstance.getMapId());
			pstm.setInt(3, paramL1NpcInstance.getHomeX());
			pstm.setInt(4, paramL1NpcInstance.getHomeY());
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return false;
			}
			int id = rs.getInt("id");
			_spawntable.remove(Integer.valueOf(id));
			SQLUtil.close(rs, pstm);
			pstm = con.prepareStatement("DELETE FROM spawnlist WHERE id=?");
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
	
	private static int calcCount(L1Npc npc, int count, double rate) {
		if (rate == 0) {
			return 0;
		}
		if (rate == 1 || npc.isAmountFixed()) {
			return count;
		}
		return NumberUtil.randomRound((count * rate));
	}
}


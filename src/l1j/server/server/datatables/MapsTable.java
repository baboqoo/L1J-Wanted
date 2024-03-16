package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.utils.SQLUtil;

import org.apache.mina.util.ConcurrentHashSet;

public final class MapsTable {
	public class MapData {
		private int mapId = -1;
		private String mapName;
		private int startX, startY;
		private int endX, endY;
		private double monster_amount	= 1D;
		private double dropRate			= 1D;
		private boolean isUnderwater;
		private boolean markable;
		private boolean teleportable;
		private boolean escapable;
		private boolean isUseResurrection;
		private boolean isUsePainwand;
		private boolean isEnabledDeathPenalty;
		private boolean isTakePets;
		private boolean isRecallPets;
		private boolean isUsableItem;
		private boolean isUsableSkill;
		private boolean isDungeon;
		private int dmgModiPc2Npc;
		private int dmgModiNpc2Pc;
		private boolean isDecreaseHp;
		private boolean isDominationTeleport;
		private boolean isBeginZone;
		private boolean isRedKnightZone;
		private boolean isRuunCastleZone;
		private boolean isInterWarZone;
		private boolean isGeradBuffZone;
		private boolean isGrowBuffZone;
		private L1InterServer inter;
		private String script;
		private int cloneStart;
		private int cloneEnd;
		
		public int getMapId() {
			return mapId;
		}
		public String getMapName() {
			return mapName;
		}
		public int getStartX() {
			return startX;
		}
		public int getStartY() {
			return startY;
		}
		public int getEndX() {
			return endX;
		}
		public int getEndY() {
			return endY;
		}
		public double getMonster_amount() {
			return monster_amount;
		}
		public double getDropRate() {
			return dropRate;
		}
		public boolean isUnderwater() {
			return isUnderwater;
		}
		public boolean isMarkable() {
			return markable;
		}
		public boolean isTeleportable() {
			return teleportable;
		}
		public boolean isEscapable() {
			return escapable;
		}
		public boolean isUseResurrection() {
			return isUseResurrection;
		}
		public boolean isUsePainwand() {
			return isUsePainwand;
		}
		public boolean isEnabledDeathPenalty() {
			return isEnabledDeathPenalty;
		}
		public boolean isTakePets() {
			return isTakePets;
		}
		public boolean isRecallPets() {
			return isRecallPets;
		}
		public boolean isUsableItem() {
			return isUsableItem;
		}
		public boolean isUsableSkill() {
			return isUsableSkill;
		}
		public boolean isDungeon() {
			return isDungeon;
		}
		public int getDmgModiPc2Npc() {
			return dmgModiPc2Npc;
		}
		public int getDmgModiNpc2Pc() {
			return dmgModiNpc2Pc;
		}
		public boolean isDecreaseHp() {
			return isDecreaseHp;
		}
		public boolean isDominationTeleport() {
			return isDominationTeleport;
		}
		public boolean isBeginZone() {
			return isBeginZone;
		}
		public boolean isRedKnightZone() {
			return isRedKnightZone;
		}
		public boolean isRuunCastleZone(){
			return isRuunCastleZone;
		}
		public boolean isInterWarZone() {
			return isInterWarZone;
		}
		public boolean isGeradBuffZone() {
			return isGeradBuffZone;
		}
		public boolean isGrowBuffZone() {
			return isGrowBuffZone;
		}
		public L1InterServer getInter(){
			return inter;
		}
		public String getScript(){
			return script;
		}
		public int getCloneStart() {
			return cloneStart;
		}
		public int getCloneEnd() {
			return cloneEnd;
		}
	}

	private static Logger _log = Logger.getLogger(MapsTable.class.getName());

	private static MapsTable _instance;

	/**
	 * Key에 MAP ID, Value에 텔레포트 가부 플래그가 격납되는 HashMap
	 */
	private final Map<Integer, MapData> _maps = new HashMap<Integer, MapData>();
	
	private static final ConcurrentHashMap<Integer, Integer> COPY_MAPS = new ConcurrentHashMap<>();
	public static int getCopyMap(int mapId){
		if (!COPY_MAPS.containsKey(mapId)) {
			return -1;
		}
		return COPY_MAPS.get(mapId);
	}
	
	private static final ConcurrentHashMap<Integer, L1InterServer> INTER_SERVER_MAPS = new ConcurrentHashMap<>();
	public static L1InterServer getInterServerMap(int mapId){
		return INTER_SERVER_MAPS.get(mapId);
	}
	
	private static final ConcurrentHashMap<Integer, String> SCRIPT_MAPS	= new ConcurrentHashMap<>();
	public static String getScriptName(int mapId){
		return SCRIPT_MAPS.get(mapId);
	}
	
	private static final ConcurrentHashSet<Integer> DOMINATION_TELEPORTS = new ConcurrentHashSet<>();
	public static boolean isDominationTeleports(int mapId){
		return DOMINATION_TELEPORTS.contains(mapId);
	}
	
	private static final ConcurrentHashSet<Integer> GERAD_BUFF = new ConcurrentHashSet<>();
	public static boolean isGeradBuffMap(int mapId){
		return GERAD_BUFF.contains(mapId);
	}
	
	private static final ConcurrentHashSet<Integer> GROWW_BUFF = new ConcurrentHashSet<>();
	public static boolean isGrowBuffMap(int mapId){
		return GROWW_BUFF.contains(mapId);
	}

	/**
	 * 새롭고 MapsTable 오브젝트를 생성해, MAP의 텔레포트 가부 플래그를 읽어들인다.
	 */
	private MapsTable() {
		loadMapsFromDatabase();
	}
	
	public static void reload() {
		COPY_MAPS.clear();
		INTER_SERVER_MAPS.clear();
		SCRIPT_MAPS.clear();
		DOMINATION_TELEPORTS.clear();
		GERAD_BUFF.clear();
		GROWW_BUFF.clear();
		MapsTable oldInstance = _instance;
		_instance = new MapsTable();
		oldInstance._maps.clear();
	}

	/**
	 * MAP의 텔레포트 가부 플래그를 데이타베이스로부터 읽어들여, HashMap _maps에 격납한다.
	 */
	private void loadMapsFromDatabase() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM mapids");
			MapData data = null;
			for (rs = pstm.executeQuery(); rs.next();) {
				data						= new MapData();
				data.mapId					= rs.getInt("mapid");
				data.mapName				= rs.getString("locationname"); // not really in use
				data.startX					= rs.getInt("startX");
				data.endX					= rs.getInt("endX");
				data.startY					= rs.getInt("startY");
				data.endY					= rs.getInt("endY");
				data.monster_amount			= rs.getDouble("monster_amount");
				data.dropRate				= rs.getDouble("drop_rate");
				data.isUnderwater			= rs.getBoolean("underwater");
				data.markable				= rs.getBoolean("markable");
				data.teleportable			= rs.getBoolean("teleportable");
				data.escapable				= rs.getBoolean("escapable");
				data.isUseResurrection		= rs.getBoolean("resurrection");
				data.isUsePainwand			= rs.getBoolean("painwand");
				data.isEnabledDeathPenalty	= rs.getBoolean("penalty");
				data.isTakePets				= rs.getBoolean("take_pets");
				data.isRecallPets			= rs.getBoolean("recall_pets");
				data.isUsableItem			= rs.getBoolean("usable_item");
				data.isUsableSkill			= rs.getBoolean("usable_skill");
				data.isDungeon				= rs.getBoolean("dungeon");
				data.dmgModiPc2Npc			= rs.getInt("dmgModiPc2Npc");
				data.dmgModiNpc2Pc			= rs.getInt("dmgModiNpc2Pc");
				data.isDecreaseHp			= rs.getBoolean("decreaseHp");
				data.isDominationTeleport	= rs.getBoolean("dominationTeleport");
				data.isBeginZone			= rs.getBoolean("beginZone");
				data.isRedKnightZone		= rs.getBoolean("redKnightZone");
				data.isRuunCastleZone		= rs.getBoolean("ruunCastleZone");
				data.isInterWarZone			= rs.getBoolean("interWarZone");
				data.isGeradBuffZone		= rs.getBoolean("geradBuffZone");
				data.isGrowBuffZone			= rs.getBoolean("growBuffZone");
				data.inter					= L1InterServer.fromInt(rs.getInt("interKind"));
				data.script					= rs.getString("script");
				data.cloneStart				= rs.getInt("cloneStart");
				data.cloneEnd				= rs.getInt("cloneEnd");
				if (data.inter != null) {
					INTER_SERVER_MAPS.put(data.mapId, data.inter);
				}
				if (data.script != null) {
					SCRIPT_MAPS.put(data.mapId, data.script);
				}
				if (data.isDominationTeleport) {
					DOMINATION_TELEPORTS.add(data.mapId);
				}
				if (data.isGeradBuffZone) {
					GERAD_BUFF.add(data.mapId);
				}
				if (data.isGrowBuffZone) {
					GROWW_BUFF.add(data.mapId);
				}
				if (data.cloneStart > 0 && data.cloneEnd > 0) {
					for (int i=data.cloneStart; i<=data.cloneEnd; i++) {
						COPY_MAPS.put(i, data.mapId);
						if (data.inter != null) {
							INTER_SERVER_MAPS.put(i, data.inter);
						}
						if (data.script != null) {
							SCRIPT_MAPS.put(i, data.script);
						}
						if (data.isDominationTeleport) {
							DOMINATION_TELEPORTS.add(i);
						}
						if (data.isGeradBuffZone) {
							GERAD_BUFF.add(data.mapId);
						}
						if (data.isGrowBuffZone) {
							GROWW_BUFF.add(data.mapId);
						}
					}
				}
				_maps.put(new Integer(data.mapId), data);
			}
			_log.config("Maps " + _maps.size());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	/**
	 * MapsTable의 인스턴스를 돌려준다.
	 * 
	 * @return MapsTable의 인스턴스
	 */
	public static MapsTable getInstance() {
		if (_instance == null) {
			_instance = new MapsTable();
		}
		return _instance;
	}
	
	public void CloneInsert(int oriMap, int newMap){
		MapData oriData					= _maps.get(oriMap);
		MapData cloneData				= new MapData();
		cloneData.mapId					= newMap;
		cloneData.startX				= oriData.startX;
		cloneData.endX					= oriData.endX;
		cloneData.startY				= oriData.startY;
		cloneData.endY					= oriData.endY;
		cloneData.monster_amount		= oriData.monster_amount;
		cloneData.dropRate				= oriData.dropRate;
		cloneData.isUnderwater			= oriData.isUnderwater;
		cloneData.markable				= oriData.markable;
		cloneData.teleportable			= oriData.teleportable;
		cloneData.escapable				= oriData.escapable;
		cloneData.isUseResurrection		= oriData.isUseResurrection;
		cloneData.isUsePainwand			= oriData.isUsePainwand;
		cloneData.isEnabledDeathPenalty	= oriData.isEnabledDeathPenalty;
		cloneData.isTakePets			= oriData.isTakePets;
		cloneData.isRecallPets			= oriData.isRecallPets;
		cloneData.isUsableItem			= oriData.isUsableItem;
		cloneData.isUsableSkill			= oriData.isUsableSkill;
		cloneData.isDungeon				= oriData.isDungeon;
		cloneData.dmgModiPc2Npc			= oriData.dmgModiPc2Npc;
		cloneData.dmgModiNpc2Pc			= oriData.dmgModiNpc2Pc;
		cloneData.isDecreaseHp			= oriData.isDecreaseHp;
		cloneData.isDominationTeleport	= oriData.isDominationTeleport;
		cloneData.isBeginZone			= oriData.isBeginZone;
		cloneData.isRedKnightZone		= oriData.isRedKnightZone;
		cloneData.isRuunCastleZone		= oriData.isRuunCastleZone;
		cloneData.isInterWarZone		= oriData.isInterWarZone;
		cloneData.isGeradBuffZone		= oriData.isGeradBuffZone;
		cloneData.isGrowBuffZone		= oriData.isGrowBuffZone;
		cloneData.inter					= oriData.inter;
		cloneData.script				= oriData.script;
		_maps.put(new Integer(newMap), oriData);
	}

	/**
	 * MAP가의 X개시 좌표를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * @return X개시 좌표
	 */
	public int getStartX(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).startX;
	}

	/**
	 * MAP가의 X종료 좌표를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * @return X종료 좌표
	 */
	public int getEndX(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).endX;
	}

	/**
	 * MAP가의 Y개시 좌표를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * @return Y개시 좌표
	 */
	public int getStartY(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).startY;
	}

	/**
	 * MAP가의 Y종료 좌표를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * @return Y종료 좌표
	 */
	public int getEndY(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).endY;
	}

	/**
	 * 맵의 monster량 배율을 돌려준다
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * @return monster량의 배율
	 */
	public double getMonsterAmount(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return map.monster_amount;
	}

	/**
	 * 맵의 드롭 배율을 돌려준다
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * @return 드롭 배율
	 */
	public double getDropRate(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return map.dropRate;
	}

	/**
	 * MAP가, 수중일까를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * 
	 * @return 수중이면 true
	 */
	public boolean isUnderwater(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUnderwater;
	}

	/**
	 * MAP가, 북마크 가능한가를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * @return 북마크 가능하면 true
	 */
	public boolean isMarkable(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).markable;
	}

	/**
	 * MAP가, 랜덤 텔레포트 가능한가를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * @return 가능하면 true
	 */
	public boolean isTeleportable(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).teleportable;
	}

	/**
	 * MAP가, MAP를 넘은 텔레포트 가능한가를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * @return 가능하면 true
	 */
	public boolean isEscapable(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).escapable;
	}

	/**
	 * MAP가, restore 가능한가를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * 
	 * @return restore 가능하면 true
	 */
	public boolean isUseResurrection(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUseResurrection;
	}

	/**
	 * MAP가, 파인쥬스 wand 사용 가능한가를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * 
	 * @return 파인쥬스 wand 사용 가능하면 true
	 */
	public boolean isUsePainwand(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUsePainwand;
	}

	/**
	 * MAP가, 데스페나르티가 있을까를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * 
	 * @return 데스페나르티이면 true
	 */
	public boolean isEnabledDeathPenalty(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isEnabledDeathPenalty;
	}

	/**
	 * MAP가, 애완동물·사몬을 데리고 갈 수 있을까를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * 
	 * @return 펫·사몬을 데리고 갈 수 있다면 true
	 */
	public boolean isTakePets(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isTakePets;
	}

	/**
	 * MAP가, 애완동물·사몬을 호출할 수 있을까를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 MAP의 MAP ID
	 * 
	 * @return 펫·사몬을 호출할 수 있다면 true
	 */
	public boolean isRecallPets(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isRecallPets;
	}
	
	/**
	 * 맵이, 아이템을 사용할 수 있을까를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 아이템을 사용할 수 있다면 true
	 */
	public boolean isUsableItem(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUsableItem;
	}

	/**
	 * 맵이, 스킬을 사용할 수 있을까를 돌려준다.
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 스킬을 사용할 수 있다면 true
	 */
	public boolean isUsableSkill(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUsableSkill;
	}
	
	/**
	 * 맵이, 던전에 속하는지 체크한다
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 던전이라면 true
	 */
	public boolean isDungeon(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isDungeon;
	}
	
	/**
	 * 맵이, PC가 NPC에게 공격 시 xx% 데미지만 적용. ex)dmgModiPc2Npc="50" PC가 NPC에게 공격 시 50%의 데미지만 적용
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return int
	 */
	public int getDmgModiPc2Npc(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).dmgModiPc2Npc;
	}
	
	/**
	 * 맵이, NPC가 PC에게 공격 시 xx% 데미지만 적용. ex)dmgModiNpc2Pc="50" NPC가 PC에게 공격 시 50%의 데미지만 적용
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return int
	 */
	public int getDmgModiNpc2Pc(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).dmgModiNpc2Pc;
	}
	
	/**
	 * 맵이, HP감소되는지 체크한다.
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return true or false
	 */
	public boolean isDecreaseHp(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isDecreaseHp;
	}
	
	/**
	 * 맵이, 지배텔레포트에 속하는지 체크한다
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 지배 맵이라면 true
	 */
	public boolean isDominationTeleport(int mapId){
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isDominationTeleport;
	}
	
	/**
	 * 맵이, 초보지역에 속하는지 체크한다
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 초보 맵이라면 true
	 */
	public boolean isBeginZone(int mapId){
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isBeginZone;
	}
	
	/**
	 * 맵이, 붉은기사단 지역에 속하는지 체크한다
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 지역 맵이라면 true
	 */
	public boolean isRedKnightZone(int mapId){
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isRedKnightZone;
	}
	
	/**
	 * 맵이, 루운성 맵에 해당하는지 채크
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 지역 맵이라면 true
	 */
	public boolean isRuunCastleZone(int mapId){
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isRuunCastleZone;
	}
	
	/**
	 * 맵이, 월드공성전 지역에 속하는지 체크한다
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 지역 맵이라면 true
	 */
	public boolean isInterWarZone(int mapId){
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isInterWarZone;
	}
	
	/**
	 * 맵이, 게라드 버프 지역에 속하는지 체크
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 지역 맵이라면 true
	 */
	public boolean isGeradBuffZone(int mapId){
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isGeradBuffZone;
	}
	
	/**
	 * 맵이, 성장 버프 지역에 속하는지 체크
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return 지역 맵이라면 true
	 */
	public boolean isGrowBuffZone(int mapId){
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isGrowBuffZone;
	}
	
	/**
	 * 맵의 인터서버 인지 반환한다.
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return L1InterServerConstruct
	 */
	public L1InterServer getInter(int mapId){
		MapData map = _maps.get(mapId);
		if (map == null) {
			return null;
		}
		return _maps.get(mapId).inter;
	}
	
	/**
	 * 맵의 스크립트를 반환한다.
	 * 
	 * @param mapId
	 *            조사하는 맵의 맵 ID
	 * 
	 * @return String
	 */
	public String getScript(int mapId){
		MapData map = _maps.get(mapId);
		if (map == null) {
			return null;
		}
		return _maps.get(mapId).script;
	}

	/**
	 * 맵의 이름을 취득한다.
	 * @param mapId
	 * @return name
	 */
	public String getMapName(int mapId) {
	    MapData map = _maps.get(mapId);
		if (map == null) {
			return null;
		}
		return _maps.get(mapId).mapName;
	}
	
	public Map<Integer, MapData> getMaps(){
		return _maps;
	}
	
	public MapData getMap(int mapId){
		return _maps.get(mapId);
	}
	 
}


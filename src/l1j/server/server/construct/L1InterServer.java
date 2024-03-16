package l1j.server.server.construct;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public enum L1InterServer {
	THEBERAS(			3,	100),
	FORGOTTEN_ISLAND(	4,	101),
	LFC(				6,	102),
	DOMINATION_TOWER(	7,	103),
	WORLD_WAR(			8,	104),
	DOLL_RACE(			9,	105),
	INSTANCE_DUNGEON(	10,	106),
	ANT_QUEEN(			11,	107),
	OCCUPY_HEINE(		13,	109),
	ABADON(				14,	110),
	TREASURE_ISLAND(	15,	111),
	OCCUPY_WINDAWOOD(	18,	112),
	LEAVE(				99,	99),
	;
	private int _kind;
	private int _reserverId;
	L1InterServer(int kind, int reserverId) {
		_kind		= kind;
		_reserverId	= reserverId;
	}
	public int getKind(){
		return _kind;
	}
	public int getReserverId(){
		return _reserverId;
	}
	
	private static final ConcurrentHashMap<Integer, L1InterServer> INTER_SERVERS;
	static {
		L1InterServer[] array = L1InterServer.values();
		INTER_SERVERS = new ConcurrentHashMap<Integer, L1InterServer>(array.length);
		for (L1InterServer inter : array) {
			INTER_SERVERS.put(inter._kind, inter);
		}
	}
	private static final List<L1InterServer> NOT_POLYS = Arrays.asList(new L1InterServer[] {
			WORLD_WAR, ANT_QUEEN, OCCUPY_HEINE, OCCUPY_WINDAWOOD, TREASURE_ISLAND
	});
	private static final List<L1InterServer> WORLD_WARS = Arrays.asList(new L1InterServer[] {
			WORLD_WAR, OCCUPY_HEINE, OCCUPY_WINDAWOOD
	});
	private static final List<L1InterServer> WORLD_POLYS = Arrays.asList(new L1InterServer[] {
			WORLD_WAR, ANT_QUEEN, OCCUPY_HEINE, OCCUPY_WINDAWOOD
	});
	private static final List<L1InterServer> OCCUPYS = Arrays.asList(new L1InterServer[] {
			OCCUPY_HEINE, OCCUPY_WINDAWOOD
	});
	private static final List<L1InterServer> ANONYMITY_NAMES = Arrays.asList(new L1InterServer[] {
			OCCUPY_HEINE, OCCUPY_WINDAWOOD, ABADON
	});
	private static final List<L1InterServer> NOT_NOTIFICATION = Arrays.asList(new L1InterServer[] {
			INSTANCE_DUNGEON, ANT_QUEEN
	});
	private static final List<L1InterServer> TEAM_ID_MAPPINGS = Arrays.asList(new L1InterServer[] {
			FORGOTTEN_ISLAND, LFC, DOMINATION_TOWER
	});
	
	public static L1InterServer fromInt(int kind){
		return INTER_SERVERS.get(kind);
	}
	
	public static boolean isNotPolyInter(L1InterServer inter){
		if (inter == null) {
			return false;
		}
		return NOT_POLYS.contains(inter);
	}
	
	public static boolean isWorldWarInter(L1InterServer inter){
		if (inter == null) {
			return false;
		}
		return WORLD_WARS.contains(inter);
	}
	
	public static boolean isWorldPolyInter(L1InterServer inter){
		if (inter == null) {
			return false;
		}
		return WORLD_POLYS.contains(inter);
	}
	
	public static boolean isOccupyInter(L1InterServer inter){
		if (inter == null) {
			return false;
		}
		return OCCUPYS.contains(inter);
	}
	
	public static boolean isAnonymityInter(L1InterServer inter){
		if (inter == null) {
			return false;
		}
		return ANONYMITY_NAMES.contains(inter);
	}
	
	public static boolean isNotNotificationInter(L1InterServer inter){
		if (inter == null) {
			return false;
		}
		return NOT_NOTIFICATION.contains(inter);
	}
	
	public static boolean isTeamMappingInter(L1InterServer inter){
		if (inter == null) {
			return false;
		}
		return TEAM_ID_MAPPINGS.contains(inter);
	}
	
	public static void init(){}
}


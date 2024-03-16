package l1j.server.GameSystem.inn;

import javolution.util.FastMap;
import javolution.util.FastTable;

public enum InnHelper {
	TALKING_ISLAND(	70012,	new int[]{ 16384, 16684 },	InnLoc.TALKING_ISLAND_ROOM,	new int[]{ 16896, 17196 },	InnLoc.TALKING_ISLAND_HALL,	InnLoc.TALKING_ISLAND_OUT	),
	GLUDIO(			70019,	new int[]{ 17408, 17708 },	InnLoc.GLUDIO_ROOM,			new int[]{ 17920, 18220 },	InnLoc.GLUDIO_HALL,			InnLoc.GLUDIO_OUT			),
	GIRAN(			70031,	new int[]{ 18432, 18732 },	InnLoc.GIRAN_ROOM,			new int[]{ 18944, 19244 },	InnLoc.GIRAN_HALL,			InnLoc.GIRAN_OUT			),
	ADEN(			70054,	new int[]{ 19456, 19756 },	InnLoc.ADEN_ROOM,			new int[]{ 19968, 20268 },	InnLoc.ADEN_HALL,			InnLoc.ADEN_OUT				),
	OREN(			70065,	new int[]{ 23552, 23852 },	InnLoc.OREN_ROOM,			new int[]{ 24064, 24364 },	InnLoc.OREN_HALL,			InnLoc.OREN_OUT				),
	WINDAWOOD(		70070,	new int[]{ 20480, 20780 },	InnLoc.WINDAWOOD_ROOM,		new int[]{ 20992, 21292 },	InnLoc.WINDAWOOD_HALL,		InnLoc.WINDAWOOD_OUT		),
	SILVER_KNIGHT(	70075,	new int[]{ 21504, 21804 },	InnLoc.SILVER_KNIGHT_ROOM,	new int[]{ 22016, 22316 },	InnLoc.SILVER_KNIGHT_HALL,	InnLoc.SILVER_KNIGHT_OUT	),
	HEINE(			70084,	new int[]{ 22528, 22828 },	InnLoc.HEINE_ROOM,			new int[]{ 23040, 23340 },	InnLoc.HEINE_HALL,			InnLoc.HEINE_OUT			),
	RUUN(			70101,	new int[]{ 24576, 24876 },	InnLoc.RUUN_ROOM,			new int[]{ 25088, 25388 },	InnLoc.RUUN_HALL,			InnLoc.RUUN_OUT				),
	;
	private int helperId;
	private int[] roomIds;
	private InnLoc roomLoc;
	private int[] hallIds;
	private InnLoc hallLoc;
	private InnLoc outLoc;
	private FastTable<Integer> roomMapList;
	private FastTable<Integer> hallMapList;
	
	InnHelper(int helperId, int[] roomIds, InnLoc roomLoc, int[] hallIds, InnLoc hallLoc, InnLoc outLoc) {
		this.helperId	= helperId;
		this.roomIds	= roomIds;
		this.roomLoc	= roomLoc;
		this.hallIds	= hallIds;
		this.hallLoc	= hallLoc;
		this.outLoc		= outLoc;
		
		roomMapList		= new FastTable<Integer>();
		hallMapList		= new FastTable<Integer>();
		for (int map = roomIds[0]; map <= roomIds[1]; map++) {
			roomMapList.add(map);
		}
		for (int map = hallIds[0]; map <= hallIds[1]; map++) {
			hallMapList.add(map);
		}
	}
	
	public int getHelperId(){
		return helperId;
	}
	public int[] getRoomIds(){
		return roomIds;
	}
	public InnLoc getRoomLoc(){
		return roomLoc;
	}
	public int[] getHallIds(){
		return hallIds;
	}
	public InnLoc getHallLoc(){
		return hallLoc;
	}
	public InnLoc getOutLoc(){
		return outLoc;
	}
	public boolean isRoomMap(int mapId){
		return roomMapList.contains(mapId);
	}
	public boolean isHallMap(int mapId){
		return hallMapList.contains(mapId);
	}
	
	private static final InnHelper[] ARRAY = InnHelper.values();
	private static final FastMap<Integer, InnHelper> HELPER_DATA;
	private static final FastTable<Integer> INN_MAP_DATA;
	static {
		HELPER_DATA		= new FastMap<>(ARRAY.length);
		INN_MAP_DATA	= new FastTable<>();
		for (InnHelper helper : ARRAY) {
			HELPER_DATA.put(helper.helperId, helper);
			
			for (int map = helper.roomIds[0]; map <= helper.roomIds[1]; map++) {
				INN_MAP_DATA.add(map);
			}
			for (int map = helper.hallIds[0]; map <= helper.hallIds[1]; map++) {
				INN_MAP_DATA.add(map);
			}
		}
	}
	
	public static boolean isHelper(int npcId){
		return HELPER_DATA.containsKey(npcId);
	}
	
	public static InnHelper getHelper(int npcId){
		return HELPER_DATA.get(npcId);
	}
	
	public static int[] getInLoc(int keyMapId){
		for (InnHelper helper : ARRAY) {
			if (keyMapId >= helper.roomIds[0] && keyMapId <= helper.roomIds[1]) {
				return helper.roomLoc.getLoc();
			}
			if (keyMapId >= helper.hallIds[0] && keyMapId <= helper.hallIds[1]) {
				return helper.hallLoc.getLoc();
			}
		}
		return null;
	}
	
	public static int[] getOutLoc(int keyMapId){
		for (InnHelper helper : ARRAY) {
			if (keyMapId >= helper.roomIds[0] && keyMapId <= helper.roomIds[1]) {
				return helper.outLoc.getLoc();
			}
			if (keyMapId >= helper.hallIds[0] && keyMapId <= helper.hallIds[1]) {
				return helper.outLoc.getLoc();
			}
		}
		return null;
	}
	
	public static boolean isInnMap(int mapId){
		return INN_MAP_DATA.contains(mapId);
	}
	
	public static void init(){}
}


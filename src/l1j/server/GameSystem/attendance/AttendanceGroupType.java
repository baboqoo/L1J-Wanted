package l1j.server.GameSystem.attendance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;

public enum AttendanceGroupType {
	NORMAL(			0,	false,	0,													0,														Config.ATTEND.ATTENDANCE_NORMAL_EINHASAD,		0),
	PC_CAFE(		1,	false,	0,													0,														Config.ATTEND.ATTENDANCE_PCROOM_EINHASAD,		0),
	PREMIUM(		2,	true,	Config.ATTEND.ATTENDANCE_PREMIUM_OPEN_ITEM_ID,		Config.ATTEND.ATTENDANCE_PREMIUM_OPEN_ITEM_COUNT,		Config.ATTEND.ATTENDANCE_PREMIUM_EINHASAD,		0),
	SPECIAL(		3,	true,	Config.ATTEND.ATTENDANCE_SPECIAL_OPEN_ITEM_ID,		Config.ATTEND.ATTENDANCE_SPECIAL_OPEN_ITEM_COUNT,		Config.ATTEND.ATTENDANCE_SPECIAL_EINHASAD,		0),
	GROW(			4,	true,	Config.ATTEND.ATTENDANCE_GROW_OPEN_ITEM_ID,			Config.ATTEND.ATTENDANCE_GROW_OPEN_ITEM_COUNT,			Config.ATTEND.ATTENDANCE_GROW_EINHASAD,			1),
	DOMINATION(		5,	true,	Config.ATTEND.ATTENDANCE_DOMINATION_OPEN_ITEM_ID,	Config.ATTEND.ATTENDANCE_DOMINATION_OPEN_ITEM_COUNT,	Config.ATTEND.ATTENDANCE_DOMINATION_EINHASAD,	1),
	BRAVERY_MEDAL(	6,	true,	Config.ATTEND.ATTENDANCE_BRAVE_OPEN_ITEM_ID,		Config.ATTEND.ATTENDANCE_BRAVE_OPEN_ITEM_COUNT,			Config.ATTEND.ATTENDANCE_BRAVE_EINHASAD,		1),
	EXPLORER(		7,	true,	Config.ATTEND.ATTENDANCE_EXPLORER_OPEN_ITEM_ID,		Config.ATTEND.ATTENDANCE_EXPLORER_OPEN_ITEM_COUNT,		Config.ATTEND.ATTENDANCE_EXPLORER_EINHASAD,		1),
	;
	private int groupId;
	private boolean isTab;
	private int tabOpenItemId;
	private int tabOpenItemCount;
	private int rewardEinhasad;
	private int seasonId;
	
	AttendanceGroupType(int groupId, boolean isTab, int tabOpenItemId, int tabOpenItemCount, int rewardEinhasad, int seasonId) {
		this.groupId			= groupId;
		this.isTab				= isTab;
		this.tabOpenItemId		= tabOpenItemId;
		this.tabOpenItemCount	= tabOpenItemCount;
		this.rewardEinhasad		= rewardEinhasad;
		this.seasonId			= seasonId;
	}
	
	public int getGroupId() {
		return groupId;
	}
	public boolean isTab() {
		return isTab;
	}
	public int getTabOpenItemId() {
		return tabOpenItemId;
	}
	public int getTabOpenItemCount() {
		return tabOpenItemCount;
	}
	public int getRewardEinhasad() {
		return rewardEinhasad;
	}
	public int getSeasonId() {
		return seasonId;
	}
	
	private static final ConcurrentHashMap<Integer, AttendanceGroupType> DATA;
	private static final ArrayList<AttendanceGroupType> USE_DATA;
	
	static {
		AttendanceGroupType[] array	= AttendanceGroupType.values();
		DATA		= new ConcurrentHashMap<>(array.length);
		USE_DATA	= new ArrayList<>();
		for (AttendanceGroupType attend : array) {
			DATA.put(attend.groupId, attend);
			if ((!Config.ATTEND.ATTENDANCE_PCROOM_USE && attend == PC_CAFE)
					|| (!Config.ATTEND.ATTENDANCE_PREMIUM_USE && attend == PREMIUM)
					|| (!Config.ATTEND.ATTENDANCE_SPECIAL_USE && attend == SPECIAL)
					|| (!Config.ATTEND.ATTENDANCE_GROW_USE && attend == GROW)
					|| (!Config.ATTEND.ATTENDANCE_DOMINATION_USE && attend == DOMINATION)
					|| (!Config.ATTEND.ATTENDANCE_BRAVE_USE && attend == BRAVERY_MEDAL)
					|| (!Config.ATTEND.ATTENDANCE_EXPLORER_USE && attend == EXPLORER)) {
				continue;
			}
			USE_DATA.add(attend);
		}
	}
	
	public static Collection<AttendanceGroupType> getAllList(){
		return DATA.values();
	}
	
	public static AttendanceGroupType fromInt(int type){
		return DATA.get(type);
	}
	
	public static int getAllSize(){
		return DATA.size();
	}
	
	public static ArrayList<AttendanceGroupType> getUseList(){
		return USE_DATA;
	}
	
	public static boolean isUse(AttendanceGroupType type){
		return USE_DATA.contains(type);
	}
	
	public static int getUseSize(){
		return USE_DATA.size();
	}
	
	public static void init(){}
}


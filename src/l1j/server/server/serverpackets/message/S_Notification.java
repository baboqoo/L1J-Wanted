package l1j.server.server.serverpackets.message;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.common.data.eNotiAnimationType;
import l1j.server.server.GameServerSetting;
import l1j.server.server.Opcodes;
import l1j.server.server.controller.action.BossSpawn;
import l1j.server.server.controller.action.UltimateBattleTime;
import l1j.server.server.datatables.BossSpawnTable.BossTemp;
import l1j.server.server.datatables.NotificationTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Notification;
import l1j.server.server.templates.L1Notification.NotificationDateType;
import l1j.server.server.templates.L1Notification.NotificationType;
import l1j.server.server.templates.L1NotificationEventNpc;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class S_Notification extends ServerBasePacket {
	private static final String S_NOTIFICATION						= "[S] S_Notification";
	private byte[] _byte											= null;
	
	public static final int NORMAL									= 0x008d;
	public static final int CHANGE									= 0x008e;
	
	private static final byte[] TELEPORT_BYTES						= getTeleportBytes();
	private static final String HYPER_LINK_URL						= "http://g.lineage.power.plaync.com/wiki/%s";// 파워북 url
	
	public static S_Notification ANT_QUEEN_ON						= new S_Notification(23, 0, true);
	public static S_Notification ANT_QUEEN_OFF						= new S_Notification(23, 0, false);
	public static S_Notification OCCUPY_HEINE_TOWER_ON				= new S_Notification(33, 0, true);
	public static S_Notification OCCUPY_HEINE_TOWER_OFF				= new S_Notification(33, 0, false);
	public static S_Notification OCCUPY_WINDAWOOD_TOWER_ON			= new S_Notification(35, 0, true);
	public static S_Notification OCCUPY_WINDAWOOD_TOWER_OFF			= new S_Notification(35, 0, false);
	public static S_Notification CLONE_WAR_ON						= new S_Notification(100, 0, true);
	public static S_Notification CLONE_WAR_OFF						= new S_Notification(100, 0, false);
	
	public static final S_Notification REMOVE_NORMAL				= new S_Notification(1, 1);
	
	public S_Notification(int maxpagecount, int currentpagecount) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NORMAL);
		writeRaw(0x08);// maxpagecount
		writeRaw(maxpagecount);
		writeRaw(0x10);// currentpagecount
		writeRaw(currentpagecount);
		writeH(0x00);
	}
	
	/**
	 * 보스인포에 등록된 요일을 모두 체크하여 현재시간 기준 다음 시작 시간을 계산한다.
	 * @param bossTemp
	 * @param currentTime
	 * @return long
	 */
	long getNextBossTime(BossTemp bossTemp, long currentTime){
		long nextOpenTime	= currentTime + (long)(86400000 * 14);// 2주후부터 체크
		long checkTime		= 0L;
		for (int i=0; i<bossTemp.spawnDay.length; i++) {
			for (int j=0; j<bossTemp.spawnHour.length; j++) {
				checkTime		= CommonUtil.getNextDayHourMinutTime(bossTemp.spawnDay[i] - 1, bossTemp.spawnHour[j], bossTemp.spawnMinute[j]);
				if (currentTime < checkTime && nextOpenTime > checkTime) {// 현재 시간보다 크고 가장 낮은 시간이 다음 오픈 시간이 된다.
					nextOpenTime = checkTime;
				}
			}
		}
		if (currentTime > nextOpenTime) {
			return 0L;
		}
		return nextOpenTime;
	}
	
	public S_Notification() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NORMAL);

		writeRaw(0x08);// maxpagecount
		writeRaw(1);
		
		writeRaw(0x10);// currentpagecount
		writeRaw(1);
		
		long currentTime	= System.currentTimeMillis();
		for (L1Notification noti : NotificationTable.getNotifications().values()) {
			if (noti.getNotificationType() != NotificationType.NORMAL) {
				continue;
			}
			if (!noti.isUse()) {
				continue;
			}
			if (noti.getDateType() == NotificationDateType.BOSS) {
				continue;
			}
			if (!Config.DUNGEON.TREASURE_ISLAND_ACTIVE && noti.getDateType() == NotificationDateType.TREASURE) {
				continue;
			}
			writeRaw(0x1a);
			writeBytesWithLength(getNotificationInfo(noti, currentTime));
		}
		
		writeH(0x00);
	}
	
	public S_Notification(L1Notification noti, boolean flag) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(flag ? NORMAL : CHANGE);
		if (flag) {
			writeRaw(0x08);// maxpagecount
			writeRaw(1);
			writeRaw(0x10);// currentpagecount
			writeRaw(1);
			writeRaw(0x1a);
			writeBytesWithLength(getNotificationInfo(noti, System.currentTimeMillis()));
		} else {
			writeRaw(0x08);// delete_notification_id
			writeBit(noti.getNotificationId());
		}
		writeH(0x00);
	}
	
	byte[] getNotificationInfo(L1Notification noti, long currentTime) {
		String displaydesc			= noti.getDisplaydesc();
		boolean is_displaydesc_null	= StringUtil.isNullOrEmpty(displaydesc);
		int starDate = -1, endDate = -1;
		switch (noti.getDateType()) {
		case CUSTOM:
			long customStart		= noti.getDateCustomStart().getTime();
			long customEnd			= noti.getDateCustomEnd().getTime();
			if (currentTime >= customEnd) {
				return null;
			}
			starDate				= (int) ((currentTime >= customStart ? currentTime : customStart) / 1000);
			endDate					= (int) (customEnd / 1000);
			break;
		case BOSS:
			if (BossSpawn.isAliveBoss(noti.getDateBoss().npcid)) {
				starDate			= (int) (currentTime / 1000);
				endDate				= (int) (starDate + 3600);
			} else {
				long nextOpenTime	= getNextBossTime(noti.getDateBoss(), currentTime);
				if (nextOpenTime == 0L) {
					return null;
				}
				starDate			= (int) (nextOpenTime / 1000);
				endDate				= (int) (starDate + 3600);
			}
			break;
		case DOMINATION_TOWER:
			starDate				= GameServerSetting.DOMINATION_TOWER ? (int) (currentTime / 1000) : (int) (CommonUtil.getNextTime(Config.DUNGEON.DOMINATION_TOWER_OPEN_HOUR) / 1000);
			endDate					= (int) (CommonUtil.getNextTime(Config.DUNGEON.DOMINATION_TOWER_CLOSE_HOUR) / 1000);
			break;
		case COLOSSEUM:
			if (UltimateBattleTime.getInstance()._coloseumRun) {
				starDate			= (int) (currentTime / 1000);
				endDate				= (int) (starDate + 300);
			} else {
				String[] upTimes	= UBTable.getInstance().getUb(1).getNextUbTime().split(StringUtil.ColonString);
				long nextOpenTime	= CommonUtil.getNextTime(Integer.parseInt(upTimes[0]), Integer.parseInt(upTimes[1]));
				starDate			= (int) (nextOpenTime / 1000);
				endDate				= (int) (starDate + 300);
			}
			break;
		case TREASURE:
			starDate				= GameServerSetting.TREASURE_ISLAND ? (int) (currentTime / 1000) : (int) (CommonUtil.getNextTime(Config.DUNGEON.TREASURE_ISLAND_OPEN_HOUR, Config.DUNGEON.TREASURE_ISLAND_OPEN_MINUT) / 1000);
			endDate					= (int) (starDate + (Config.DUNGEON.TREASURE_ISLAND_DURATION * 60));
			break;
		case FORGOTTEN:
			starDate				= GameServerSetting.FORGOTTEN_ISLAND_LOCAL ? (int) (currentTime / 1000) : (int) (CommonUtil.getNextTime(Config.DUNGEON.ISLAND_OPEN_HOUR_LOCAL, 1) / 1000);
			endDate					= (int) (starDate + ((Config.DUNGEON.ISLAND_CLOSE_HOUR_LOCAL - Config.DUNGEON.ISLAND_OPEN_HOUR_LOCAL) * 60 * 60));
		break;
	default:
			break;
		}
		
		NotificationInfomationStream os	= null;
		try {
			os	= new NotificationInfomationStream();
			os.write_notification_id(noti.getNotificationId());
			if (noti.isHyperlink() && !is_displaydesc_null) {
				String hyperlink	= String.format(HYPER_LINK_URL, displaydesc.replace(StringUtil.EmptyOneString, StringUtil.EmptyString));
				os.write_hyperlink(hyperlink.getBytes());
			} else {
				os.write_hyperlink(null);
			}
			os.write_displaydesc(is_displaydesc_null ? null : displaydesc.getBytes());
			os.write_startdate(starDate);
			os.write_enddate(endDate);
			if (noti.getTeleportLoc() != null) {// 텔레포트 정보
				os.write_teleport(TELEPORT_BYTES);
			}
			if (!noti.getEventNpcList().isEmpty()) {
				os.writeByte(getEventNpcInfo(noti));
			}
			if (noti.getRestGaugeBonus() > 0) {
				os.write_rest_gauge_icon_display(true);
				os.write_rest_gauge_bonus_display(noti.getRestGaugeBonus());
			}
			if (noti.isNew()) {
				os.write_new(true);
			}
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {}
			}
		}
		return null;
	}
	
	static ConcurrentHashMap<L1Notification, byte[]> EVENT_NPC_BYTES = new ConcurrentHashMap<>();
	byte[] getEventNpcInfo(L1Notification noti){
		byte[] bytes = EVENT_NPC_BYTES.get(noti);
		if (bytes == null) {
			bytes = eventNpcInfoByte(noti);
			EVENT_NPC_BYTES.put(noti, bytes);
		}
		return bytes;
	}
	
	static byte[] eventNpcInfoByte(L1Notification noti) {
		BinaryOutputStream os		= null;
		BinaryOutputStream detail	= null;
		try {
			os		= new BinaryOutputStream();
			detail	= new BinaryOutputStream();
			for (L1NotificationEventNpc event : noti.getEventNpcList()) {
				if (!event.isUse()) {
					continue;
				}
				detail.writeC(0x0a);
				detail.writeBytesWithLength(getEventNpcInfo(event));
			}
			
			os.writeC(0x42);
			os.writeBytesWithLength(detail.getBytes());
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (detail != null) {
				try {
					detail.close();
					detail = null;
				} catch (Exception e) {}
			}
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {}
			}
		}
		return null;
	}
	
	static byte[] getEventNpcInfo(L1NotificationEventNpc eventNpc) {
		L1NpcInstance npc		= L1World.getInstance().findNpc(eventNpc.getNpcId());
		if (npc == null) {
			System.out.println(String.format("[S_Notification] EVENT_NPC_NOT_FOUND_FROM_WORLD : NPC_ID(%d)", eventNpc.getNpcId()));
			return null;
		}
		NotificationEventNpcInfoStream os	= null;
		try {
			os	= new NotificationEventNpcInfoStream(npc.getId(), eventNpc.getDisplaydesc().getBytes(), eventNpc.getRestGaugeBonus());
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {}
			}
		}
		return null;
	}
	
	public S_Notification(int notificationId, int worlds, boolean onoff) {
		L1Notification noti = NotificationTable.getNotification(notificationId);
		if (noti == null) {
			System.out.println(String.format("[S_Notification] NOTIFICATION_NOT_FOUND : NOTI_ID(%d)", notificationId));
			return;
		}
		if (!noti.getNotificationType().equals(NotificationType.CHANGE)) {
			System.out.println(String.format("[S_Notification] NOT_CHANGE_TYPE : NOTI_ID(%d)", notificationId));
			return;
		}
		if (!noti.isUse()) {
			System.out.println(String.format("[S_Notification] NOT_USE : NOTI_ID(%d)", notificationId));
			return;
		}
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHANGE);
		if (onoff) {// 시작
			writeRaw(0x1a);// change
			writeBytesWithLength(getChange(noti, worlds));
		} else {// 종료
			writeRaw(0x08);// delete_notification_id
			writeBit(noti.getNotificationId());
		}
		writeH(0x00);
	}
	
	byte[] getChange(L1Notification noti, int worlds){
		NotificationInfomationStream os = null;
		try {
			os = new NotificationInfomationStream();
			os.write_notification_id(noti.getNotificationId());
			os.write_hyperlink(null);
			os.write_displaydesc(noti.getDisplaydesc().getBytes());
			if (noti.getTeleportLoc() != null) {
				os.write_teleport(TELEPORT_BYTES);
			}
			if (noti.getAnimationType() != null && !noti.getAnimationType().equals(eNotiAnimationType.NO_ANIMATION)) {
				os.write_animation_type(noti.getAnimationType());
			}
			if (worlds > 0) {
				os.write_worlds(worlds);
			}
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {}
			}
		}
		return null;
	}
	
	public S_Notification(int deleteObjectId) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHANGE);
		
		for (L1Notification noti : NotificationTable.getNotifications().values()) {
			writeRaw(0x08);// delete_notification_id
			writeBit(noti.getNotificationId());
			
			writeRaw(0x10);// delete_object_id
			writeBit(deleteObjectId);
		}
		
		writeH(0x00);
	}
	
	static byte[] getTeleportBytes(){
		NotificationTeleportDataStream os = null;
		try {
			os = new NotificationTeleportDataStream();
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {}
			}
		}
		return null;
	}
	
	public static void reload() {
		EVENT_NPC_BYTES.clear();
		
		ANT_QUEEN_ON.clear();
		ANT_QUEEN_OFF.clear();
		OCCUPY_HEINE_TOWER_ON.clear();
		OCCUPY_HEINE_TOWER_OFF.clear();
		OCCUPY_WINDAWOOD_TOWER_ON.clear();
		OCCUPY_WINDAWOOD_TOWER_OFF.clear();
		CLONE_WAR_ON.clear();
		CLONE_WAR_OFF.clear();
		
		ANT_QUEEN_ON				= new S_Notification(23, 0, true);
		ANT_QUEEN_OFF				= new S_Notification(23, 0, false);
		OCCUPY_HEINE_TOWER_ON		= new S_Notification(33, 0, true);
		OCCUPY_HEINE_TOWER_OFF		= new S_Notification(33, 0, false);
		OCCUPY_WINDAWOOD_TOWER_ON	= new S_Notification(35, 0, true);
		OCCUPY_WINDAWOOD_TOWER_OFF	= new S_Notification(35, 0, false);
		CLONE_WAR_ON				= new S_Notification(100, 0, true);
		CLONE_WAR_OFF				= new S_Notification(100, 0, false);
	}
	
	public static void init(){}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_NOTIFICATION;
	}
}


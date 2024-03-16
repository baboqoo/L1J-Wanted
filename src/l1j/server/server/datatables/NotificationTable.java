package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.IndunSystem.occupy.action.Heine;
import l1j.server.common.data.eNotiAnimationType;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.controller.action.BossSpawn;
import l1j.server.server.datatables.BossSpawnTable.BossTemp;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.message.S_Notification;
import l1j.server.server.templates.L1Notification;
import l1j.server.server.templates.L1Notification.NotificationDateType;
import l1j.server.server.templates.L1Notification.NotificationType;
import l1j.server.server.templates.L1NotificationEventNpc;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 이벤트 알람
 * @author LinOffice
 */
public class NotificationTable {
	private static NotificationTable _instance;
	public static NotificationTable getInstance(){
		if (_instance == null) {
			_instance = new NotificationTable();
		}
		return _instance;
	}
	
	private static final LinkedHashMap<Integer, L1Notification> DATA = new LinkedHashMap<>();
	
	public static LinkedHashMap<Integer, L1Notification> getNotifications() {
		return DATA;
	}
	
	public static L1Notification getNotification(int notificationId) {
		return DATA.get(notificationId);
	}
	
	private NotificationTable(){
		load();
	}
	
	private void load(){
		Connection con					= null;
		PreparedStatement pstm			= null;
		ResultSet rs					= null;
		L1Notification notification		= null;
		try {
			con							= L1DatabaseFactory.getInstance().getConnection();
			
			pstm						= con.prepareStatement("SELECT * FROM notification ORDER BY notification_id");
			rs							= pstm.executeQuery();
			while(rs.next()){
				int notification_id						= rs.getInt("notification_id");
				String notificationType					= rs.getString("notification_type");
				notificationType						= notificationType.substring(notificationType.indexOf("(") + 1, notificationType.indexOf(")"));
				NotificationType notification_type		= NotificationType.fromInt(Integer.parseInt(notificationType));
				boolean is_use							= Boolean.parseBoolean(rs.getString("is_use"));
				boolean is_hyperlink					= Boolean.parseBoolean(rs.getString("is_hyperlink"));
				String displaydesc						= rs.getString("displaydesc");
				String dateType							= rs.getString("date_type");
				dateType								= dateType.substring(dateType.indexOf("(") + 1, dateType.indexOf(")"));
				NotificationDateType date_type			= NotificationDateType.fromInt(Integer.parseInt(dateType));
				int date_boss_id						= rs.getInt("date_boss_id");
				BossTemp date_boss						= null;
				if (date_type == NotificationDateType.BOSS) {
					date_boss = BossSpawnTable.getBossInfo(date_boss_id);
					if (date_boss == null) {
						System.out.println(String.format("[NotificationTable] BOSS_TEMP_NOT_FOUND : BOSS_ID(%d)", date_boss_id));
						continue;
					}
				}
				Timestamp date_custom_start				= rs.getTimestamp("date_custom_start");
				Timestamp date_custom_end				= rs.getTimestamp("date_custom_end");
				if (date_type == NotificationDateType.CUSTOM) {
					if (date_custom_start == null) {
						System.out.println(String.format("[NotificationTable] CUSTOM_START_DATE_EMPTY : NOTI_ID(%d)", notification_id));
						continue;
					}
					if (date_custom_end == null) {
						System.out.println(String.format("[NotificationTable] CUSTOM_END_DATE_EMPTY : NOTI_ID(%d)", notification_id));
						continue;
					}
				}
				String teleportLoc						= rs.getString("teleport_loc");
				int[] teleport_loc						= null;
				if (!StringUtil.isNullOrEmpty(teleportLoc)) {
					String[] telArray					= teleportLoc.split(StringUtil.CommaString);
					if (telArray != null && telArray.length == 3) {
						teleport_loc					= new int[3];
						teleport_loc[0]					= Integer.parseInt(telArray[0].trim());
						teleport_loc[1]					= Integer.parseInt(telArray[1].trim());
						teleport_loc[2]					= Integer.parseInt(telArray[2].trim());
					}
				}
				int rest_gauge_bonus					= rs.getInt("rest_gauge_bonus");
				boolean is_new							= Boolean.parseBoolean(rs.getString("is_new"));
				String animationType					= rs.getString("animation_type");
				animationType							= animationType.substring(animationType.indexOf("(") + 1, animationType.indexOf(")"));
				eNotiAnimationType animation_type		= eNotiAnimationType.fromInt(Integer.parseInt(animationType));
				
				notification = new L1Notification(
						notification_id, notification_type, 
						is_use, 
						is_hyperlink, 
						displaydesc, 
						date_type, date_boss, date_custom_start, date_custom_end,
						teleport_loc, 
						new LinkedList<>(), 
						rest_gauge_bonus, 
						is_new, animation_type);
				if (date_boss != null) {
					date_boss.notification = notification;
				}
				DATA.put(notification.getNotificationId(), notification);
			}
			SQLUtil.close(rs, pstm);
			
			pstm						= con.prepareStatement("SELECT * FROM notification_event_npc ORDER BY notification_id, order_id");
			rs							= pstm.executeQuery();
			while(rs.next()){
				int notification_id		= rs.getInt("notification_id");
				boolean is_use			= Boolean.parseBoolean(rs.getString("is_use"));
				int npc_id				= rs.getInt("npc_id");
				String displaydesc		= rs.getString("displaydesc");
				int rest_gauge_bonus	= rs.getInt("rest_gauge_bonus");
				
				notification			= DATA.get(notification_id);
				if (notification == null) {
					System.out.println(String.format("[NotificationTable] EVENT_NPC_NOTIFICATION_NOT_FOUND : NOTI_ID(%d)", notification_id));
					continue;
				}
				L1Npc npc				= NpcTable.getInstance().getTemplate(npc_id);
				if (npc == null) {
					System.out.println(String.format("[NotificationTable] EVENT_NPC_TEMPLATE_NOT_FOUND : NOTI_ID(%d), NPC_ID(%d)", notification_id, npc_id));
					continue;
				}
				npc.setNotification(true);
				notification.getEventNpcList().add(new L1NotificationEventNpc(notification_id, is_use, npc_id, displaydesc, rest_gauge_bonus));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 알람 출력
	 * @param pc
	 */
	public void sendNotification(L1PcInstance pc){
		pc.sendPackets(new S_Notification(), true);// 기본 알람
		if (GameServerSetting.ANT_QUEEN) {
			pc.sendPackets(S_Notification.ANT_QUEEN_ON);// 여왕개미 은신처
		}
		if (GameServerSetting.OMAN_CRACK > 0) {
			pc.sendPackets(new S_Notification(26, GameServerSetting.OMAN_CRACK, true), true);// 균열의 오만의 탑
			if (pc.getMapId() == GameServerSetting.OMAN_CRACK) {
				pc.sendPackets(new S_SceneNoti(Integer.toString((int)(pc.getMapId())), true, false), true);
			}
		}
		Map<OccupyType, OccupyHandler> ocHandlers = OccupyManager.getInstance().getHandlers();
		if (ocHandlers != null && !ocHandlers.isEmpty()) {
			OccupyHandler ocHandler = ocHandlers.get(ocHandlers.keySet().iterator().next());
			if (ocHandler != null && ocHandler.isRunning()) {
				pc.sendPackets(ocHandler instanceof Heine ? S_Notification.OCCUPY_HEINE_TOWER_ON : S_Notification.OCCUPY_WINDAWOOD_TOWER_ON);
			}
		}
		if (GameServerSetting.CLONE_WAR) {
			pc.sendPackets(S_Notification.CLONE_WAR_ON);// 거울전쟁
		}
		
		// 보스 알람
		for (L1Notification noti : DATA.values()) {
			BossTemp boss = noti.getDateBoss();
			if (boss == null) {
				continue;
			}
			L1NpcInstance npc = BossSpawn.getAliveBoss(boss.npcid);
			if (npc == null || npc.get_notification_info() == null) {
				continue;
			}
			pc.sendPackets(new S_Notification(npc.get_notification_info(), true), true);
		}
	}
	
	public void reload(){
		L1World world	= L1World.getInstance();
		for (L1Notification noti : DATA.values()) {
			for (L1NotificationEventNpc eventNpc : noti.getEventNpcList()) {
				L1Npc npc = NpcTable.getInstance().getTemplate(eventNpc.getNpcId());
				if (npc == null) {
					continue;
				}
				npc.setNotification(false);
			}
			noti.getEventNpcList().clear();
		}
		DATA.clear();
		load();
		S_Notification.reload();
		
		for (L1PcInstance pc : world.getAllPlayers()) {
			if (pc == null || pc.getNetConnection() == null || pc.isPrivateShop() || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			if (L1InterServer.isNotNotificationInter(pc.getNetConnection().getInter())) {
				continue;
			}
			pc.sendPackets(S_Notification.REMOVE_NORMAL);
			pc.sendPackets(new S_Notification(pc.getId()), true);
			sendNotification(pc);
		}
	}
}


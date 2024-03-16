package l1j.server.server.templates;

import java.sql.Timestamp;
import java.util.LinkedList;

import l1j.server.common.data.eNotiAnimationType;
import l1j.server.server.datatables.BossSpawnTable.BossTemp;

public class L1Notification {
	private int notification_id;
	private NotificationType notification_type;
	private boolean is_use;
	private boolean is_hyperlink;
	private String displaydesc;
	private NotificationDateType date_type;
	private BossTemp date_boss;
	private Timestamp date_custom_start;
	private Timestamp date_custom_end;
	private int[] teleport_loc;
	private LinkedList<L1NotificationEventNpc> event_npc_list;
	private int rest_gauge_bonus;
	private boolean is_new;
	private eNotiAnimationType animation_type;
	
	public L1Notification(int notification_id,
			NotificationType notification_type,
			boolean is_use,
			boolean is_hyperlink, String displaydesc,
			NotificationDateType date_type, BossTemp date_boss, Timestamp date_custom_start, Timestamp date_custom_end,
			int[] teleport_loc, 
			LinkedList<L1NotificationEventNpc> event_npc_list,
			int rest_gauge_bonus, boolean is_new, eNotiAnimationType animation_type) {
		this.notification_id		= notification_id;
		this.notification_type		= notification_type;
		this.is_use					= is_use;
		this.is_hyperlink			= is_hyperlink;
		this.displaydesc			= displaydesc;
		this.date_type				= date_type;
		this.date_boss				= date_boss;
		this.date_custom_start		= date_custom_start;
		this.date_custom_end		= date_custom_end;
		this.teleport_loc			= teleport_loc;
		this.event_npc_list			= event_npc_list;
		this.rest_gauge_bonus		= rest_gauge_bonus;
		this.is_new					= is_new;
		this.animation_type			= animation_type;
	}

	public int getNotificationId() {
		return notification_id;
	}

	public NotificationType getNotificationType() {
		return notification_type;
	}

	public boolean isUse() {
		return is_use;
	}

	public boolean isHyperlink() {
		return is_hyperlink;
	}

	public String getDisplaydesc() {
		return displaydesc;
	}

	public NotificationDateType getDateType() {
		return date_type;
	}

	public BossTemp getDateBoss() {
		return date_boss;
	}
	
	public Timestamp getDateCustomStart() {
		return date_custom_start;
	}
	
	public Timestamp getDateCustomEnd() {
		return date_custom_end;
	}

	public int[] getTeleportLoc() {
		return teleport_loc;
	}

	public LinkedList<L1NotificationEventNpc> getEventNpcList() {
		return event_npc_list;
	}

	public int getRestGaugeBonus() {
		return rest_gauge_bonus;
	}

	public boolean isNew() {
		return is_new;
	}

	public eNotiAnimationType getAnimationType() {
		return animation_type;
	}
	
	public static enum NotificationType {
		NORMAL(0),
		CHANGE(1),
		;
		private int _type;
		private NotificationType(int type) {
			_type = type;
		}
		public int getType() {
			return _type;
		}
		public static NotificationType fromInt(int val) {
			switch (val) {
			case 1:
				return CHANGE;
			default:
				return NORMAL;
			}
		}
	}
	
	public static enum NotificationDateType {
		NONE(0),
		CUSTOM(1),
		BOSS(2),
		DOMINATION_TOWER(3),
		COLOSSEUM(4),
		TREASURE(5),
		FORGOTTEN(6),
		;
		private int _type;
		private NotificationDateType(int type) {
			_type = type;
		}
		public int getType() {
			return _type;
		}
		public static NotificationDateType fromInt(int val) {
			switch (val) {
			case 1:
				return CUSTOM;
			case 2:
				return BOSS;
			case 3:
				return DOMINATION_TOWER;
			case 4:
				return COLOSSEUM;
			case 5:
				return TREASURE;
			case 6:
				return FORGOTTEN;
			default:
				return NONE;
			}
		}
	}
	
}


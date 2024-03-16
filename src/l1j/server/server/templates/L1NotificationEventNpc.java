package l1j.server.server.templates;

public class L1NotificationEventNpc {
	private int notification_id;
	private boolean is_use;
	private int npc_id;
	private String displaydesc;
	private int rest_gauge_bonus;
	
	public L1NotificationEventNpc(int notification_id, boolean is_use, int npc_id, String displaydesc, int rest_gauge_bonus) {
		this.notification_id	= notification_id;
		this.is_use				= is_use;
		this.npc_id				= npc_id;
		this.displaydesc		= displaydesc;
		this.rest_gauge_bonus	= rest_gauge_bonus;
	}
	
	public int getNotificationId() {
		return notification_id;
	}
	public boolean isUse() {
		return is_use;
	}
	public int getNpcId() {
		return npc_id;
	}
	public String getDisplaydesc() {
		return displaydesc;
	}
	public int getRestGaugeBonus() {
		return rest_gauge_bonus;
	}
}


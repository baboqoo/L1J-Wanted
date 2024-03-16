package l1j.server.server.controller.action;

import l1j.server.server.construct.L1InterServer;
import l1j.server.server.datatables.NotificationTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_Notification;

/**
 * 알림 재 출력
 * @author LinOffice
 */
public class NotificationRefresh implements ControllerInterface {
	private static class newInstance {
		public static final NotificationRefresh INSTANCE = new NotificationRefresh();
	}
	public static NotificationRefresh getInstance() {
		return newInstance.INSTANCE;
	}
	private NotificationRefresh(){}

	@Override
	public void execute() {
		try {
			
		} catch(Exception e2){
			e2.printStackTrace();
		}
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	void do_refresh() {
		NotificationTable noti = NotificationTable.getInstance();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.getNetConnection() == null) {
				continue;
			}
			if (L1InterServer.isNotNotificationInter(pc.getNetConnection().getInter())) {
				continue;
			}
			pc.sendPackets(S_Notification.REMOVE_NORMAL);
			pc.sendPackets(new S_Notification(pc.getId()), true);
			noti.sendNotification(pc);
		}
	}

}


package l1j.server.server.command.executor;

import java.util.Calendar;

import l1j.server.Server;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ServerDriveTime implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ServerDriveTime();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ServerDriveTime() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			Calendar cal = Calendar.getInstance();
			long timeMin = ((cal.getTimeInMillis() - Server.StartTime.getTimeInMillis()) / 1000) / 60;			
			long timeHour = timeMin / 60;
			timeMin -= timeHour * 60;
			long timeDay = timeHour / 24;
			timeHour -= timeDay * 24;
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(timeDay+"일 " + timeHour+"시간 "+timeMin+"분 "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(timeDay + S_SystemMessage.getRefText(663) + timeHour + S_SystemMessage.getRefText(664) + timeMin + S_SystemMessage.getRefText(106), true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".가동시간"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(665), true), true);
			return false;
		}
	}
}



package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.command.S_UserCommands4;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Rank implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Rank();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Rank() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime2() + 5 > curtime) {
			    long time = (pc.getQuizTime2() + 5) - curtime;
//AUTO SRM: 			    pc.sendPackets(new S_SystemMessage(time + "초 후 사용할 수 있습니다."), true); // CHECKED OK
			    pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(560) + time + " " + S_SystemMessage.getRefText(1306), true), true);
			    return false;
			}
			pc.sendPackets(new S_UserCommands4(pc,1), true);
			pc.setQuizTime2(curtime);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}



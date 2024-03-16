package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Thread implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Thread();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Thread() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null || pc.getNetConnection() == null) {
				return false;
			}
			Thread[] th = new Thread[Thread.activeCount()];
			Thread.enumerate(th);
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<th.length; i++) {
				//sb.append("[").append(i).append("]").append(" 사용 쓰레드 : [").append(th[i].getName()).append("]\r\n");
				sb.append("[").append(i).append("]").append(" Used Thread: [").append(th[i].getName()).append("]\r\n");
			}
			//sb.append("현재 사용되는 쓰레드 갯수 : [").append(Thread.activeCount()).append("]");
			sb.append("Number of currently used threads: [").append(Thread.activeCount()).append("]");
			pc.sendPackets(new S_SystemMessage(sb.toString()), true);
			sb = null;
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + "갯수 라고 입력 바랍니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(702), true), true);
			return false;
		}
	}
}



package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ChatWorld implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ChatWorld();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ChatWorld() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			if (st.hasMoreTokens()) {
				String flag = st.nextToken();
				String msg;
				if (flag.compareToIgnoreCase("on") == 0) {
					L1World.getInstance(). setWorldChatElabled(true);
					//msg = "월드 채팅을 유효하게 했습니다. ";
					msg = S_SystemMessage.getRefText(1353);
				} else if (flag.compareToIgnoreCase("off") == 0) {
					L1World.getInstance(). setWorldChatElabled(false);
					//msg = "월드 채팅을 정지했습니다. ";
					msg = S_SystemMessage.getRefText(1354);
				} else {
					throw new Exception();
				}
				pc.sendPackets(new S_SystemMessage(msg, true), true);
				return true;
			}
			String msg;
			if (L1World.getInstance(). isWorldChatElabled()) {
				//msg = "현재 월드 채팅은 유효합니다.. 채팅 끔 로 정지할 수 있습니다. ";
				msg = S_SystemMessage.getRefText(1355);
			} else {
				//msg = "현재 월드 채팅은 정지하고 있습니다.. 채팅 켬 로 유효하게 할 수 있습니다. ";
				msg = S_SystemMessage.getRefText(1356);
			}
			pc.sendPackets(new S_SystemMessage(msg, true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬, 끔]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		}
	}
}



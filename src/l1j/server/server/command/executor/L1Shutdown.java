package l1j.server.server.command.executor;

import l1j.server.server.GameServer;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Shutdown implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Shutdown();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Shutdown() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			//if (arg.equalsIgnoreCase("지금")) {
			if (arg.equalsIgnoreCase("now")) {
				GameServer.getInstance().shutdownWithCountdown(0);
				return false;
			}

			//if (arg.equalsIgnoreCase("취소")) {
				if (arg.equalsIgnoreCase("cancel")) {
				GameServer.getInstance().abortShutdown();
				return false;
			}

			int sec = Math.max(5, Integer.parseInt(arg));
			GameServer.getInstance().shutdownWithCountdown(sec);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".종료 [대기초, 지금, 취소] 라고 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(674), true), true);
			return false;
		}
	}
}



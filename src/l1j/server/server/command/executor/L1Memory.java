package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Memory implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Memory();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Memory() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			System.gc();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("gc 사용후 메모리 정보"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(479), true), true);
			long long_total	= Runtime.getRuntime().totalMemory();
			int int_total	= Math.round(long_total / 1000000);
			long long_free	= Runtime.getRuntime().freeMemory();
			int int_free	= Math.round(long_free / 1000000);
			long long_max	= Runtime.getRuntime().maxMemory();
			int int_max		= Math.round(long_max / 1000000);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("사용한 메모리 : " + int_total + "MB"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(480) + int_total  + "MB", true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("남은 메모리 : " + int_free + "MB"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(481) + int_free  + "MB", true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("최대 사용가능 메모리 : " + int_max + "MB"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(482) + int_max  + "MB", true), true);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}



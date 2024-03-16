package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.controller.HomeTownTimeController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1HomeTown implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1HomeTown();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1HomeTown() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String para1 = st.nextToken();
			//if (para1.equalsIgnoreCase("매일")) {
			if (para1.equalsIgnoreCase("daily")) {
				HomeTownTimeController.getInstance(). dailyProc();
				return true;
			}
			//if (para1.equalsIgnoreCase("매달")) {
			if (para1.equalsIgnoreCase("monthly")) {
				HomeTownTimeController.getInstance(). monthlyProc();
				return true;
			}
			throw new Exception();
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".홈타운 [매일,매달] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(455), true), true);
			return false;
		}
	}
}



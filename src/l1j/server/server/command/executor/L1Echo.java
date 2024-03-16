package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

/**
 * 입력된 인수를 그대로 돌려주는 커멘드.  테스트, 디버그 및 커멘드 실장 샘플용.
 */
public class L1Echo implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Echo();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Echo() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		pc.sendPackets(new S_SystemMessage(arg), true);
		return true;
	}
}


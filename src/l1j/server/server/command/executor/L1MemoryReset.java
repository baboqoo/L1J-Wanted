package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1MemoryReset implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1MemoryReset();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1MemoryReset() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("\\aG경고: 몇분안에 메모리를 초기화 합니다"), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(483), true), true);
		//System.out.println("강제로 가비지 처리를 진행 합니다.");
		System.out.println("Force garbage processing.");
		try {
			System.gc();
		} catch (Exception e) {
		}
		//System.out.println("메모리 정리가 완료 되었습니다.");
		System.out.println("Memory cleaning completed.");
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("\\aG알림: 메모리 정리가 완료 되었습니다."), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(484), true), true);
		return true;
	}
}



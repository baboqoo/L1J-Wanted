package l1j.server.server.command.executor;

import l1j.server.server.controller.FakeCharacterController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1FakeCharacterStart implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1FakeCharacterStart();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1FakeCharacterStart() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {

			FakeCharacterController.getInstance().startBot();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("무인피씨를 작동합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(407), true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 커맨드 오류"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(168), true), true);
			return false;
		}
	}
}


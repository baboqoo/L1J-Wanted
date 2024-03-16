package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.controller.FakeCharacterController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Fake implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Fake();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Fake() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			String name = stringtokenizer.nextToken();

			if (!FakeCharacterController.getInstance().createNewBot(name, pc.getX(), pc.getY(), pc.getMoveState().getHeading(), pc.getMapId(), false)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("이미 존재하는 캐릭터 이름입니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(171), true), true);
				return false;
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(cmdName + " [캐릭이름]로 입력해 주세요. ").toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157)).toString(), true), true);
			return false;
		}
	}
}


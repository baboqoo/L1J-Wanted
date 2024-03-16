package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.controller.FakeCharacterController;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1FakeCharacterKick implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1FakeCharacterKick();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1FakeCharacterKick() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			String name = stringtokenizer.nextToken();

			L1PcInstance target = L1World.getInstance().getPlayer(name);

			if (target == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(name + "는 존재하지 않는 캐릭터 명입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(name  + S_SystemMessage.getRefText(405), true), true);
				return false;
			}

			if (!target.getFake()) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(name + "은 무인 캐릭이 아닙니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(name  + S_SystemMessage.getRefText(406), true), true);
				return false;
			}

			FakeCharacterController.getInstance().deleteBot(name);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(cmdName + " [캐릭이름]로 입력해 주세요. ").toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157)).toString(), true), true);
			return false;
		}
	}
}


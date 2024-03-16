package l1j.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ToPC implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ToPC();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ToPC() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null || pc.getNetConnection() == null) {
				return false;
			}
			L1PcInstance target = L1World.getInstance().getPlayer(arg);

			if (target != null) {
				pc.getTeleport().start(target.getX(), target.getY(), target.getMapId(), 5, false);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(arg).append(" : 해당 캐릭터는 없습니다. ").toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(arg).append(S_SystemMessage.getRefText(704)).toString(), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭터명]으로 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}



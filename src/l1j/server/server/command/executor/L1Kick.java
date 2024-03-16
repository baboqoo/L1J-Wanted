package l1j.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Kick implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Kick();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Kick() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(arg);

			if (target != null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(" 를 추방 했습니다. ").toString()), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(S_SystemMessage.getRefText(460)).toString(), true), true);
				target.getNetConnection().kick();	//만일의 경우를 대비
				target.getNetConnection().close();	//만일의 경우를 대비
				target.sendPackets(S_Disconnect.DISCONNECT);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("그러한 이름의 캐릭터는 월드내에는 존재하지 않습니다. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(156), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 캐릭명으로 입력. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}



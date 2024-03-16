package l1j.server.server.command.executor;

import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Unprison implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Unprison();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Unprison() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(arg);
			if (target == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("그런 이름의 캐릭터는 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(712), true), true);
				return false;
			}
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			target.getTeleport().start(loc[0], loc[1], (short)loc[2], 5, false);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(" 님을 마을로 이동.").toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(S_SystemMessage.getRefText(713)).toString(), true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}



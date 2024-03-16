package l1j.server.server.command.executor;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1PartyRecall implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1PartyRecall.class.getName());

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1PartyRecall();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1PartyRecall() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		L1PcInstance target = L1World.getInstance(). getPlayer(arg);

		if (target != null) {
			L1Party party = target.getParty();
			if (party != null) {
				int x = pc.getX();
				int y = pc.getY() + 2;
				short map = pc.getMapId();
				for (L1PcInstance member : party.getMembersArray()) {
					try {
						member.getTeleport().start(x, y, map, 5, true);
//AUTO SRM: 						member.sendPackets(new S_SystemMessage("게임마스터에 소환되었습니다."), true); // CHECKED OK
						member.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(522), true), true);
					} catch (Exception e) {
						_log.log(Level.SEVERE, StringUtil.EmptyString, e);
					}
				}
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("파티 멤버가 아닙니다. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(523), true), true);
			return false;
		} else {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("그러한 캐릭터는 없습니다. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(524), true), true);
			return false;
		}
	}
}



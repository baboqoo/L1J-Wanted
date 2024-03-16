package l1j.server.server.command.executor;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Speed implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Speed.class.getName());

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Speed();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Speed() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null || pc.getNetConnection() == null) {
				return false;
			}
			L1BuffUtil.haste(pc, 9999 * 1000);
			L1BuffUtil.brave(pc, 9999 * 1000);
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".속도 커멘드 에러"), true);// CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(168), true), true);
			return false;
		}
	}
}



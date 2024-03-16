package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.SpecialEventHandler;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1BuffAll implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1BuffAll();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1BuffAll() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String status = st.nextToken();
			if (status.equalsIgnoreCase("1")) {
				SpecialEventHandler.getInstance().fullPullUp();
				return true;
			}
			if (status.equalsIgnoreCase("2")) {
				SpecialEventHandler.getInstance().fullBlessing();
				return true;
			}
			if (status.equalsIgnoreCase("3")) {
				SpecialEventHandler.getInstance().fullRawHorse();
				return true;
			}
			if (status.equalsIgnoreCase("4")) {
				SpecialEventHandler.getInstance().fullBlack();
				return true;
			}
			if (status.equalsIgnoreCase("5")) {
				SpecialEventHandler.getInstance().fullComa();
				return true;
			}
			pc.sendPackets(new S_SystemMessage("----------------------------------------------------"), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 1:풀업 2:축복 3:생마 4:흑사 5:코마"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(227), true), true);
			pc.sendPackets(new S_SystemMessage("----------------------------------------------------"), true);
			return false;
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("----------------------------------------------------"), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 1:풀업 2:축복 3:생마 4:흑사 5:코마"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(227), true), true);
			pc.sendPackets(new S_SystemMessage("----------------------------------------------------"), true);
			return false;
		}
	}
}



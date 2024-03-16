package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.command.S_SearchIcon;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1IconId implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1IconId();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1IconId() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			StringTokenizer st = new StringTokenizer(arg);
			int iconId	= Integer.parseInt(st.nextToken(), 10);
			int count	= Integer.parseInt(st.nextToken(), 10);
			pc.sendPackets(new S_SearchIcon(pc, iconId, count), true);
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [id] [출현시키는 수]로 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(230), true), true);
			return false;
		}
	}
	
	
}



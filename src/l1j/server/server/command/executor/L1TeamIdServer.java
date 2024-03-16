package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_TeamIdServerNoMappingInfo;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1TeamIdServer implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1TeamIdServer();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1TeamIdServer() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int team_id = Integer.parseInt(st.nextToken(), 10);
			pc.sendPackets(new S_TeamIdServerNoMappingInfo(team_id), true);
			//pc.sendPackets(new S_SystemMessage(String.format("팀번호: %d", team_id)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(75), String.valueOf(team_id)), true);
			st = null;
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [번호] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(702), true), true);
			return false;
		}
	}
}



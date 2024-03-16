package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1AddExp implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AddExp();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1AddExp() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int level	= Integer.parseInt(st.nextToken());
			int percent	= Integer.parseInt(st.nextToken());
			
			long addExp	= ExpTable.getExpFromLevelAndPercent(pc.getLevel(), level, percent);
			pc.addExp(addExp);
			//pc.sendPackets(new S_SystemMessage(String.format("%d레벨 %d퍼센트 지급", level, percent)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(44), String.valueOf(level), String.valueOf(percent)), true);
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [레벨] [퍼센트] 라고 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(167), true), true);
			return false;
		}
	}
}



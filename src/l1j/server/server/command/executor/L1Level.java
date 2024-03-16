package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.IntRange;

public class L1Level implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Level();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Level() {}

	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String param = tok.nextToken();
			if (tok.hasMoreTokens()) {
				L1PcInstance target = L1World.getInstance().getPlayer(param);
				int level = Integer.parseInt(tok.nextToken());
				if (level == target.getLevel()) {
					return false;
				}
				if (!IntRange.includes(level, 1, ExpTable.MAX_LEVEL)) {
					//pc.sendPackets(new S_SystemMessage(String.format("1-%d 의 범위에서 지정해 주세요.", ExpTable.MAX_LEVEL)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(64), String.valueOf(ExpTable.MAX_LEVEL)), true);
					return false;
				}
				target.setExp(ExpTable.getExpByLevel(level));
				//pc.sendPackets(new S_SystemMessage(String.format("%s님의 레벨이 변경되었습니다.", target.getName())), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(65), target.getName()), true);
				return true;
			}
			int level = Integer.parseInt(param);
			if (level == pc.getLevel()) {
				return false;
			}
			if (!IntRange.includes(level, 1, ExpTable.MAX_LEVEL)) {
				//pc.sendPackets(new S_SystemMessage(String.format("1-%d 의 범위에서 지정해 주세요.", ExpTable.MAX_LEVEL)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(64), String.valueOf(ExpTable.MAX_LEVEL)), true);
				return false;
			}
			pc.setExp(ExpTable.getExpByLevel(level));
			return true;
		} catch (Exception e) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s [캐릭명] [레벨] 라고 입력해 주세요", cmdName)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(66), cmdName), true);
			return false;
		}
	}
}



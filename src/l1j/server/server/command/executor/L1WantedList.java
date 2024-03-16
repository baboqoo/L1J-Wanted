package l1j.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1WantedList implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1WantedList();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1WantedList() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringBuilder sb = new StringBuilder();			
			//sb.append("--------------------<수배목록>--------------------").append("\n");
			sb.append("--------------------<Wanted List>--------------------" ).append("\n");			
			for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
				if (target.getConfig().getHuntCount() != 0){
					//sb.append("[").append(target.getName()).append("]님이 수배중입니다.").append("\n");
					sb.append("[").append(target.getName()).append("] is wanted.").append("\n");
				}
			}
			//sb.append("--------------------<수배목록>--------------------");
			sb.append("--------------------<Wanted List>--------------------" ).append("\n");
			pc.sendPackets(new S_SystemMessage(sb.toString()), true);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}


package l1j.server.server.command.executor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.command.L1Commands;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Command;

public class L1ShowCommand implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ShowCommand();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ShowCommand() {}
	
	private static final ConcurrentHashMap<Short, S_SystemMessage> MESSAGE_MAP = new ConcurrentHashMap<>();

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null || pc.getAccessLevel() <= 0) {
				return false;
			}
			S_SystemMessage message = MESSAGE_MAP.get(pc.getAccessLevel());
			if (message == null) {
				message = createMessage(pc.getAccessLevel());
				MESSAGE_MAP.put(pc.getAccessLevel(), message);
			}
			pc.sendPackets(message);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private S_SystemMessage createMessage(short gmCode){
		StringBuilder sb = new StringBuilder();
		sb.append("\\aA-----------★[GM Commands]★-----------------").append("\n");
		int count = 0;
		for(Map.Entry<String, L1Command> entry : L1Commands.getCommnads().entrySet()){
			if(entry.getValue().getLevel() > gmCode)continue;
			if(count > 0 && count % 4 == 0)sb.append("\n");
			sb.append(".").append(entry.getKey());
			count++;
		}
		sb.append("\n").append("\\aA----------●[Gm Commands]●-------------");
		return new S_SystemMessage(sb.toString());
	}
}


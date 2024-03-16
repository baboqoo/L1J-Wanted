package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OpcodeTest;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Opcode implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Opcode();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Opcode() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			StringTokenizer st = new StringTokenizer(arg);
			int opcode	= Integer.parseInt(st.nextToken());
			int type	= Integer.parseInt(st.nextToken());
			//String msg	= String.format("\n옵코드 [%d], 타입[%d]\n", opcode, type);
			String msg	= String.format("\nOpcode [%d], type [%d]\n", opcode, type);
			pc.sendPackets(new S_SystemMessage(msg, true), true);
			System.out.println(msg);
			pc.sendPackets(new S_OpcodeTest(pc, opcode, type), true);
			st = null;
			return true;
		} catch (Exception exception) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s [옵코드] [타입] 라고 입력해 주세요.", cmdName)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(70), cmdName), true);
			return false;
		}
	}
}



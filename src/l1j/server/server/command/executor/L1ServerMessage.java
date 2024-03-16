package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ServerMessage implements L1CommandExecutor{
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ServerMessage();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ServerMessage() {}
	
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			StringTokenizer st = new StringTokenizer(arg);
			int ment = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);

			for (int i = 0; i <= count; i++ ) {
				pc.sendPackets(new S_ServerMessage(ment + i), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("(" + (ment + i) +")번의 멘트는 위와 같습니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage("(" + (ment  + i)  + S_SystemMessage.getRefText(666), true), true);
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".메세지 [번호] [갯수]을 입력 해주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(667), true), true);
			return false;
		}
	}

}



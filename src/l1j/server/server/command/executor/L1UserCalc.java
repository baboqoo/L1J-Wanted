package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1UserCalc implements L1CommandExecutor {
	private static int calcUser = 0;

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1UserCalc();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1UserCalc() {}
	
	public static int getClacUser() {
		return calcUser;
	}

	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		//String msg = null;
		S_ServerMessage msg_server = null;

		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String type = tok.nextToken();
			int count = Integer.parseInt(tok.nextToken());

			if (type.equalsIgnoreCase(StringUtil.PlusString)) {
				calcUser += count;
				//msg = new StringBuilder().append("뻥튀기 : " + count + "명 추가 / 현재 뻥튀기 : " + calcUser + "명").toString();
				msg_server = new S_ServerMessage(S_ServerMessage.getStringIdx(108), String.valueOf(count), String.valueOf(calcUser));
				return true;
			} else if (type.equalsIgnoreCase(StringUtil.MinusString)) {
				int temp = calcUser - count;
				if (temp < 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("뻥튀기가 -가 될수는 없습니다. 현재 뻥튀기 : " + calcUser), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(714) + calcUser, true), true);
					return false;
				}
				calcUser = temp;
				//msg = new StringBuilder().append("뻥튀기 : " + count + "명 감소 / 현재 뻥튀기 : " + calcUser + "명").toString();
				msg_server = new S_ServerMessage(S_ServerMessage.getStringIdx(109), String.valueOf(count), String.valueOf(calcUser));
				return true;
			}
		} catch (Exception e) {
			//msg = new StringBuilder().append(cmdName).append(" [+,-] [COUNT] 입력").toString();
			msg_server = new S_ServerMessage(S_ServerMessage.getStringIdx(110), cmdName);
		} finally {
			//if (msg != null) 
				//pc.sendPackets(new S_SystemMessage(msg, true), true);
			if (msg_server != null) 
				pc.sendPackets(msg_server, true);		
		}
		return false;
	}
}



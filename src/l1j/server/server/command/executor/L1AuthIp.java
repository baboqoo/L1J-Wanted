package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.AuthIP;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1AuthIp implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AuthIp();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1AuthIp() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String swtich	= tok.nextToken();
			//if (swtich.equals("추가")) {
			if (swtich.equals("add")) {
				String ip		= tok.nextToken();
				if (!AuthIP.getInstance().insertAuth(ip)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("잘못된 IP입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(193), true), true);
					return false;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("승인 IP가 추가되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(194), true), true);
				return true;
			}
			//if (swtich.equals("삭제")) {
			if (swtich.equals("delete")) {
				String ip		= tok.nextToken();
				if (!AuthIP.getInstance().deleteAuth(ip)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("잘못된 IP입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(193), true), true);
					return false;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("승인 IP가 삭제되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(195), true), true);
				return true;
			}
			//if (swtich.equals("리로드")) {
			if (swtich.equals("reload")) {
				AuthIP.reload();
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("승인 IP가 리로드 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(196), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".vpn [추가/삭제/리로드] [ip]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(197), true), true);
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [추가/삭제/리로드] [ip] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(198), true), true);
			return false;
		}
	}
}



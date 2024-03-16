package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1WebServer implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1WebServer();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1WebServer() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String onoff = st.nextToken();
			switch(onoff){
			//case "켬":
			case "on":
				if (Config.WEB.WEB_SERVER_ENABLE) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 웹서버가 활성화 되어 있습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(735), true), true);
					return false;
				}
				Config.WEB.WEB_SERVER_ENABLE = true;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("웹서버가 활성화 되어 접속이 가능합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(736), true), true);
				return true;
			//case "끔":
			case "off":
				if (!Config.WEB.WEB_SERVER_ENABLE) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 웹서버가 비활성화 되어 있습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(737), true), true);
					return false;
				}
				Config.WEB.WEB_SERVER_ENABLE = false;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("웹서버가 비활성화되어 접속이 차단됩니다.(주의: 인게임 접속 불가능)"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(738), true), true);
				return true;
			default:
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(cmdName + " [켬 / 끔] 라고 입력해 주세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
				return false;
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬 / 끔] 라고 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		}
	}
}



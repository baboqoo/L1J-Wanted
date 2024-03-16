package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ChatSwitch implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ChatSwitch();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ChatSwitch() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String onoff = tok.nextToken();
			//if (onoff.equals("켬")) {
			if (onoff.equals("on")) {
					GameServerSetting.SERVER_NOT_CHAT = true;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("일부 채팅을 비활성화 합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(282), true), true);
				return true;
			}
			//if (onoff.equals("끔")) {
			if (onoff.equals("off")) {
					GameServerSetting.SERVER_NOT_CHAT = false;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("모든 채팅을 활성화 합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(283), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬 or 끔]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬 or 끔]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		}
	}
}



package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Grangkain implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Grangkain();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Grangkain() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String onoff = tok.nextToken();
			//if (onoff.equals("켬")) {
			if (onoff.equals("on")) {
				if (Config.FATIGUE.FATIGUE_ACTIVE) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 그랑카인 시스템이 작동중입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(443), true), true);
					return false;
				}
				Config.FATIGUE.FATIGUE_ACTIVE = true;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("그랑카인 시스템이 활성화 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(444), true), true);
				return true;
			}
			//if (onoff.equals("끔")) {
			if (onoff.equals("off")) {
				if (!Config.FATIGUE.FATIGUE_ACTIVE) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 그랑카인 시스템이 작동중이지 않습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(445), true), true);
					return false;
				}
				Config.FATIGUE.FATIGUE_ACTIVE = false;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("그랑카인 시스템이 비활성화 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(446), true), true);
				return true;
			}
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		}
	}
}



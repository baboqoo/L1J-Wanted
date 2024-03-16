package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1EnchatGfxSwitch implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1EnchatGfxSwitch();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1EnchatGfxSwitch() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String onoff = tok.nextToken();
			//if (onoff.equals("켬")) {
			if (onoff.equals("on")) {
					pc.getConfig()._enchantGfx = true;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("인첸트 연출 on"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(396), true), true);
				return true;
			}
			//if (onoff.equals("끔")) {
			if (onoff.equals("off")) {
					pc.getConfig()._enchantGfx = false;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("인첸트 연출 off"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(397), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		}
	}
}



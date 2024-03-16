/**
 * 무인 엔피씨 상점 시작 명령어
 * by - Eva Team.
 */
package l1j.server.server.command.executor;

import l1j.server.server.controller.action.NpcShop;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1NpcShopSwitch implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1NpcShopSwitch();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1NpcShopSwitch() {}

	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			boolean power = NpcShop.getInstance().isPower();
			//if (arg.equalsIgnoreCase("켬")) {
				if (arg.equalsIgnoreCase("on")) {
				if (power) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미실행중입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(501), true), true);
					return false;
				}
				NpcShop.getInstance().npcShopStart();
				return true;
			//} else if (arg.equalsIgnoreCase("끔")) {
			} else if (arg.equalsIgnoreCase("off")) {
				if (!power) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("실행되지 않았습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(502), true), true);
					return false;
				}
				NpcShop.getInstance().npcShopStop();
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".영자상점 켬/끔"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(447), true), true);
			return false;
		} catch (Exception e) {
			e.printStackTrace();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".영자상점 메소드오류"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(168), true), true);
			return false;
		}
	}
}
	
	


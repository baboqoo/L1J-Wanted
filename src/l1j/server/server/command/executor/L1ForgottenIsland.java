package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GameServerSetting;
import l1j.server.server.controller.action.SpecialDungeon;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ForgottenIsland implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ForgottenIsland();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ForgottenIsland() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(arg);
			String onoff = tokenizer.nextToken();
			//if (onoff.equals("시작")){
			if (onoff.equals("start")){
				if (GameServerSetting.FORGOTTEN_ISLAND_LOCAL){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\aG알림:\\aA 이미 잊혀진 섬이 열려있습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(418), true), true);
					return false;
				}
				SpecialDungeon.getInstance().forgottenIslandLocalOpen();
				return true;
			}
			//if (onoff.equals("종료")){
			if (onoff.equals("stop")){
					if (!GameServerSetting.FORGOTTEN_ISLAND_LOCAL){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\aG알림:\\aA 이미 잊혀진 섬이 닫혀있습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(419), true), true);
					return false;
				}
				SpecialDungeon.getInstance().forgottenIslandLocalEnd();
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [시작 or 종료] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(182), true), true);
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [시작 or 종료] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(182), true), true);
			return false;
		}
	}
}



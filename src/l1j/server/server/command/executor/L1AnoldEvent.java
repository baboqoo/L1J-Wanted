package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.controller.AnoldEventController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1AnoldEvent implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AnoldEvent();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1AnoldEvent() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(arg);
			String onoff = tokenizer.nextToken();
			//if (onoff.equals("시작")){
			if (onoff.equals("start")){
					if (Config.ETC.ANOLD_EVENT_ACTIVE){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("현재 아놀드 이벤트가 진행중 입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(179), true), true);
					return false;
				}
				AnoldEventController.getInstance().isGmOpen = true;
				AnoldEventController.getInstance().start();
				return true;
			}
			//if (onoff.equals("종료")){
			if (onoff.equals("stop")){
				if (!Config.ETC.ANOLD_EVENT_ACTIVE){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("아놀드 이벤트 진행중이 아닙니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(180), true), true);
					return false;
				}
				AnoldEventController.getInstance().End();
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("아놀드 이벤트가 강제 종료 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(181), true), true);
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



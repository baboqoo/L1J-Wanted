package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.IndunSystem.minigame.BattleZone;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1BattleZone implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1BattleZone();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1BattleZone() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(arg);
			String onoff = tokenizer.nextToken();
			//if (onoff.equals("시작")){
			if (onoff.equals("start")){
					if (BattleZone.getInstance().getGmStart() == true) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("배틀존이 이미 실행중 입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(219), true), true);
					return false;
				}
				BattleZone.getInstance().setGmStart(true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("배틀존이 실행 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(220), true), true);
				return true;
			} else 
			//if (onoff.equals("종료")){
			if (onoff.equals("stop")){
					if (BattleZone.getInstance().getGmStart() == false) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("배틀존이 이미 종료상태 입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(221), true), true);
					return false;
				}
				BattleZone.getInstance().setGmStart(false);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("배틀존이 종료 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(222), true), true);
				return true;
			}
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [시작 or 종료] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(182), true), true);
			return false;
		}
	}
}



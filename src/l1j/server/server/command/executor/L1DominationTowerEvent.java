package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1DominationTowerEvent implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1DominationTowerEvent();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1DominationTowerEvent() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(arg);
			String onoff = tokenizer.nextToken();
			//if (onoff.equals("시작")){
			if (onoff.equals("start")){
					if (GameServerSetting.EVENT_DOMINATION_OPEN){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("현재 지배의탑 개방 이벤트가 진행중 입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(378), true), true);
					return false;
				}
				GameServerSetting.EVENT_DOMINATION_OPEN = true;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("지배의탑 개방 이벤트가 실행 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(379), true), true);
				return true;
			}
			//if (onoff.equals("종료")){
			if (onoff.equals("stop")){
					if (!GameServerSetting.EVENT_DOMINATION_OPEN){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("지배의탑 개방 이벤트가 진행중이지 않습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(380), true), true);
					return false;
				}
				GameServerSetting.EVENT_DOMINATION_OPEN = false;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("지배의탑 개방 이벤트가 중지 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(381), true), true);
				return true;
			}
			//pc.sendPackets(new S_SystemMessage(String.format("%s [시작 or 종료] 라고 입력해 주세요.", cmdName)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(56), cmdName), true);
			return false;
		} catch (Exception exception) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s [시작 or 종료] 라고 입력해 주세요.", cmdName)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(56), cmdName), true);
			return false;
		}
	}
}



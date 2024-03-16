package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GameServerSetting;
import l1j.server.server.controller.action.SpecialDungeon;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1DominationTower implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1DominationTower();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1DominationTower() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st	= new StringTokenizer(arg);
			String onoff		= st.nextToken();
			//if (onoff.equals("시작")){
			if (onoff.equals("start")){
					if (GameServerSetting.DOMINATION_TOWER){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\aG알림:\\aA 이미 지배의 탑이 열려있습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + S_SystemMessage.getRefText(374), true), true);
					return false;
				}
				SpecialDungeon.getInstance().dominationOpen();
				return true;
			}
			//if (onoff.equals("종료")){
			if (onoff.equals("stop")){
					if (!GameServerSetting.DOMINATION_TOWER){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\aG알림:\\aA 이미 지배의 탑이 닫혀있습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(376), true), true);
					return false;
				}
				SpecialDungeon.getInstance().dominationEnd();
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



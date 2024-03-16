package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.pledge.S_PledgeWatch;

public class L1ClanMark implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ClanMark();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ClanMark() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String onoff = st.nextToken();
			
			if (pc.getNetConnection() != null && pc.getNetConnection().getInter() != null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("해당 맵에서는 사용이 불가능 합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(317), true), true);
				return false;
			}
			
			//if (onoff.equalsIgnoreCase("켬")) {
			if (onoff.equalsIgnoreCase("on")) {
				pc.sendPackets(new S_PledgeWatch(pc, 2, true), true);
				pc.sendPackets(new S_PledgeWatch(pc, 0, true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("혈마크 표기를 시작 합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(318), true), true);
				return true;
			}
			//if (onoff.equalsIgnoreCase("끔")) {
			if (onoff.equalsIgnoreCase("off")) {
				pc.sendPackets(new S_PledgeWatch(pc, 2, false), true);
				pc.sendPackets(new S_PledgeWatch(pc, 1, false), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("혈마크 표기를 종료 합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(319), true), true);
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



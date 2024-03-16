package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1StanbyServer implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1StanbyServer();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1StanbyServer() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String status = st.nextToken();
			if (status.equalsIgnoreCase("on")) {
				if (Config.SERVER.STANDBY_SERVER) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 대기 상태로 돌입하였습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(682), true), true);
					return false;
				}
				Config.SERVER.STANDBY_SERVER = true;
				Config.RATE.RATE_XP = 0;// 오픈대기때 경험치 강제 0으로만듬
//AUTO SRM: 				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("[시스템]:\\aA서버가 오픈대기에 돌입합니다. 일부 패킷이 차단되었습니다."), true); // CHECKED OK
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(683), true), true);
				return true;
			}
			if (status.equalsIgnoreCase("off")) {
				if (!Config.SERVER.STANDBY_SERVER) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("대기 상태가 아닙니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(684), true), true);
					return false;
				}
				Config.load();// 기존 컨피그 경험치 리로드시켜버림
				Config.SERVER.STANDBY_SERVER = false;
//AUTO SRM: 				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("[시스템]:\\aA대기 상태가 해지되고, 정상적인 플레이가 가능합니다."), true); // CHECKED OK
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(685), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔] 으로 입력하세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("켬 - 오픈대기 상태로 전환 | 끔 - 일반모드로 게임시작"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(686), true), true);
			return false;
		} catch (Exception eee) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔] 으로 입력하세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("켬 - 오픈대기 상태로 전환 | 끔 - 일반모드로 게임시작"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(686), true), true);
			return false;
		}
	}
}



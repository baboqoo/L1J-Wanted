package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_TamPointNoti;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1TamPresent implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1TamPresent();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1TamPresent() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			switch(name){
			//case "전체":
			case "all":
				for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					if (!player.isPrivateShop() && !player.isAutoClanjoin() && !player.noPlayerCK) {
						player.getAccount().getArca().addPoint(id);
						player.sendPackets(new S_TamPointNoti(pc.getAccount().getArca().getPoint()),true);
//AUTO SRM: 						player.sendPackets(new S_SystemMessage("운영자가 탐 " + id + "개를 지급해주었습니다."), true); // CHECKED OK
						player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(697) + id  + S_SystemMessage.getRefText(703), true), true);
					}
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("전체 접속된 유저에게 탐 " + id + "개를 주었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(698) + id  + S_SystemMessage.getRefText(681), true), true);
				return true;
			default:
				L1PcInstance user = L1World.getInstance().getPlayer(name);
				if (user != null) {
					user.getAccount().getArca().addPoint(id);
					user.sendPackets(new S_TamPointNoti(user.getAccount().getArca().getPoint()),true);
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(user.getName() + "에게 탐 " + id + "개를 주었습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(user.getName()  + S_SystemMessage.getRefText(699) + id  + S_SystemMessage.getRefText(681), true), true);
					return true;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("존재하지 않는 유저 입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(394), true), true);
				return false;
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [전체/캐릭명] [갯수]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(700), true), true);
			return false;
		}
	}
}



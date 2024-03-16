package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1NcoinPresent implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1NcoinPresent();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1NcoinPresent() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			
			switch(name){
			//case "전체":
			case "all":
				int allCount = Integer.parseInt(st.nextToken());
				for (L1PcInstance user : L1World.getInstance().getAllPlayers()) {
					if (!user.isPrivateShop() && !user.isAutoClanjoin() && !user.noPlayerCK) {
						user.getAccount().addNcoin(allCount);
						user.getAccount().updateNcoin();
//AUTO SRM: 						user.sendPackets(new S_SystemMessage("운영자가 엔코인 " + allCount + "개를 지급해주었습니다."), true); // CHECKED OK
						user.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(490) + allCount  + S_SystemMessage.getRefText(491), true), true);
					}
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("전체 접속된 유저에게 엔코인 " + allCount + "개를 주었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(492) + allCount  + S_SystemMessage.getRefText(473), true), true);
				return true;
			//case "초기화":
			case "reset":
				L1PcInstance resetUser = L1World.getInstance().getPlayer(st.nextToken());
				if (resetUser != null) {
					resetUser.getAccount().setNcoin(0);
					resetUser.getAccount().updateNcoin();
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(resetUser.getName() + "님 N코인을 초기화하였습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(resetUser.getName()  + S_SystemMessage.getRefText(493), true), true);
					return true;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("존재하지 않는 유저 입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(394), true), true);
				return false;
			default:
				int id = Integer.parseInt(st.nextToken());
				L1PcInstance user = L1World.getInstance().getPlayer(name);
				if (user != null) {
					user.getAccount().addNcoin(id);
					user.getAccount().updateNcoin();
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(user.getName() + "에게 엔코인 " + id + "개를 주었습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(user.getName()  + S_SystemMessage.getRefText(494) + id  + S_SystemMessage.getRefText(491), true), true);
					return true;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("존재하지 않는 유저 입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(394), true), true);
				return false;
			}
			
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [전체/캐릭명/초기화] [갯수/캐릭명]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(495), true), true);
			return false;
		}
	}
}



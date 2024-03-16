package l1j.server.server.command.executor;

import java.util.Collection;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.command.S_WhoAmount;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Who implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Who();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Who() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int CalcUser = L1UserCalc.getClacUser();
			Collection<L1PcInstance> players = L1World.getInstance().getAllPlayers();
			int robotcount = 0; // 무인
			int playercount = 0; 
			int AutoShopUser = 0;
			for (L1PcInstance each : players) {
				if (each.noPlayerCK) {
					robotcount++;
				} else if (each.isPrivateShop() && each.getNetConnection() == null) {
					AutoShopUser++;
				} else {
					playercount++;
				}
			}
			String amount = String.valueOf(playercount);
			S_WhoAmount s_whoamount = new S_WhoAmount(amount);
			pc.sendPackets(s_whoamount, true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("접 속 중 : " + playercount), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(740) + playercount, true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("로 보 트 : " + robotcount), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(741) + robotcount, true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("무인상점 : " + AutoShopUser), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(742) + AutoShopUser, true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("뻥 튀 기 : " + CalcUser), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(743) + CalcUser, true), true);

			
			// 온라인의 플레이어 리스트를 표시
			//if (arg.equalsIgnoreCase("전체")) {
			if (arg.equalsIgnoreCase("all")) {
				StringBuffer gmList = new StringBuffer();
				StringBuffer playList = new StringBuffer();
				StringBuffer shopList = new StringBuffer();
				StringBuffer robotList = new StringBuffer();

				int countGM = 0, countPlayer = 0, countShop = 0, countRobot = 0;

				for (L1PcInstance each : players) {
					if (each.isGm()) {
						gmList.append(each.getName() + ", ");
						countGM++;
						continue;
					}
					if (each.noPlayerCK) {
						robotList.append(each.getName() + ", ");
						countRobot++;
						continue;
					}
					if (!each.isPrivateShop()) {
						playList.append(each.getName() + ", ");
						countPlayer++;
						continue;
					}
					if (each.isPrivateShop()) {
						shopList.append(each.getName() + ", ");
						countShop++;
					}
				}
				if (gmList.length() > 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("-- 운영자 (" + countGM + "명)"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(744) + countGM  + S_SystemMessage.getRefText(745), true), true);
					pc.sendPackets(new S_SystemMessage(gmList.toString()), true);
				}

				if (playList.length() > 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("-- 플레이어 (" + countPlayer + "명)"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(746) + countPlayer  + S_SystemMessage.getRefText(745), true), true);
					pc.sendPackets(new S_SystemMessage(playList.toString()), true);
				}
				if (robotList.length() > 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("-- 로봇유저 (" + countRobot + "명)"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(747) + countRobot  + S_SystemMessage.getRefText(745), true), true);
					pc.sendPackets(new S_SystemMessage(robotList.toString()), true);
				}
				if (shopList.length() > 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("-- 개인상점 (" + countShop + "명)"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(748) + countShop  + S_SystemMessage.getRefText(745), true), true);
					pc.sendPackets(new S_SystemMessage(shopList.toString()), true);
				}
			}
			players = null;
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".누구 [전체] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(749), true), true);
			return false;
		}
	}
}



package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1GoldBless implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1GoldBless();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1GoldBless() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int bless = Integer.parseInt(st.nextToken());
			GameServerSetting.GOLD_BLESS_EXP=bless;
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
				if (!player.isPrivateShop() && !player.isAutoClanjoin() && !player.noPlayerCK) {
					player.sendPackets(new S_RestExpInfoNoti(player), true);
				}
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("황금성단의 축복 " + bless + "%가 적용되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(440) + bless  + S_SystemMessage.getRefText(441), true), true);
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [수치] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(442), true), true);
			return false;
		}
	}
}



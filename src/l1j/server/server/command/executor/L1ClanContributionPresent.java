package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1ClanContributionPresent implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ClanContributionPresent();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ClanContributionPresent() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int value = Integer.parseInt(st.nextToken());
			switch(name){
			//case "전체":
			case "all":
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if(clan == null || clan.isBot())continue;
					clan.addContribution(value);
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("전체 혈맹에게 [공헌도] " + StringUtil.comma(value) + "개를 주었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(306) + StringUtil.comma(value)  + S_SystemMessage.getRefText(307), true), true);
				return true;
			default:
				L1Clan clan = L1World.getInstance().getClan(name);
				if(clan == null){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("존재하지 않는 혈맹입니다."), true) // CHECKED OK;
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(308), true), true);
					return false;
				}
				if(clan.isBot()){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("공헌도를 줄수 없는 혈맹입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(309), true), true);
					return false;
				}
				clan.addContribution(value);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(name + "혈맹에게 [공헌도] " + StringUtil.comma(value) + "개를 주었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(name  + S_SystemMessage.getRefText(306) + StringUtil.comma(value)  + S_SystemMessage.getRefText(310), true), true);
				return true;
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [전체/혈맹명] [수량]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(311), true), true);
			return false;
		}
	}
}



package l1j.server.server.command.executor;

import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1ClanCheck implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ClanCheck();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ClanCheck() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (!StringUtil.isNullOrEmpty(arg)) {
				L1Clan clan = L1World.getInstance().getClan(arg);
				if (clan == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(arg + " 이란 혈맹은 존재하지 않습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(arg  + S_SystemMessage.getRefText(304), true), true);
					return false;
				}
				//String clnancheck = "■ " + clan.getClanName() + " ▶  총원: " + clan.getClanMemberList().size() + "명  /  접속인원: "+ clan.getOnlineClanMember().length + "명"; // CHECKED OK
				String clnancheck = "■ " + clan.getClanName() + " ▶  " + S_SystemMessage.getRefText(135) + clan.getClanMemberList().size() + S_SystemMessage.getRefText(136)  + "/ " + S_SystemMessage.getRefText(128) + clan.getOnlineClanMember().length + S_SystemMessage.getRefText(136);
				pc.sendPackets(new S_SystemMessage(clnancheck), true);
				return true;
			}
			String clnancheck = StringUtil.EmptyString;
			for (L1Clan clan : L1World.getInstance().getAllClans()) {
				if (clan == null) continue;
				clnancheck = "■ " + clan.getClanName() + " ▶  " + S_SystemMessage.getRefText(135) + clan.getClanMemberList().size() + S_SystemMessage.getRefText(136) + "/ " + S_SystemMessage.getRefText(128) + clan.getOnlineClanMember().length + S_SystemMessage.getRefText(136);
				pc.sendPackets(new S_SystemMessage(clnancheck), true);
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [혈맹명]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(305), true), true);
			return false;
		}
	}
}



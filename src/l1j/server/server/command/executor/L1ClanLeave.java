package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ClanLeave implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ClanLeave();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ClanLeave() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(arg);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("그런 유저는 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(312), true), true);
				return false;
			}
			L1Clan clan = target.getClan();
			L1PcInstance clanMember[] = clan.getOnlineClanMember();
			for(int i = 0; i < clanMember.length; i++)
				clanMember[i].sendPackets(new S_ServerMessage(178, pcName, clan.getClanName()), true);// \f1%0이 %1혈맹을 탈퇴했습니다. 
			target.clearPlayerClanData(clan);
			clan.removeClanMember(target.getName());	
			target.getTeleport().start(target.getX(), target.getY(), target.getMapId(), target.getMoveState().getHeading(), false);
			return true;
		} catch(Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명] 입력"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}



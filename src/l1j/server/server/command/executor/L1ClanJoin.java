package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1ClanJoin implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ClanJoin();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ClanJoin() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			String clanname = st.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(name);
			L1Clan clan = L1World.getInstance().getClan(clanname);
			if (target == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("그런 유저는 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(312), true), true);
				return false;
			}
			if (clan == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("그런 혈맹은 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(313), true), true);
				return false;
			}
			if (target.getClanid() != 0) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(target.getName() + "님은 혈맹이 있기때문에 탈퇴 시키겠습니다."), true);   // CHECKED OK
				pc.sendPackets(new S_SystemMessage(target.getName()  + S_SystemMessage.getRefText(314), true), true);
				target.clearPlayerClanData(clan);
				clan.removeClanMember(target.getName());	
				target.save();
				return true;
			}
			
			for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
				clanMembers.sendPackets(new S_ServerMessage(94, target.getName()), true);// \f1%0이 혈맹의 일원으로서 받아들여졌습니다.
			}
			
			int join_date = (int)(System.currentTimeMillis() / 1000);
			
			target.setClanid(clan.getClanId());
			target.setClanName(clanname);
			target.setBloodPledgeRank(eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT);
			target.setTitle(StringUtil.EmptyString);
			target.setClanMemberNotes(StringUtil.EmptyString);	
			target.setPledgeJoinDate(join_date);
			target.sendPackets(new S_CharTitle(target.getId(), StringUtil.EmptyString), true);
			Broadcaster.broadcastPacket(target, new S_CharTitle(target.getId(), StringUtil.EmptyString), true);
			clan.addClanMember(target.getName(), target.getBloodPledgeRank(), target.getLevel(), StringUtil.EmptyString, target.getId(), target.getType(), target.getOnlineStatus(), 
					target.getClanContribution(), target.getClanWeekContribution(), 
					target.getPledgeJoinDate(), target.getLastLogoutTime() == null ? 0 : (int) (target.getLastLogoutTime().getTime() / 1000),
					target);
			target.save(); // DB에 캐릭터 정보를 기입한다
			target.sendPackets(new S_PacketBox(target, S_PacketBox.PLEDGE_REFRESH_PLUS), true);
			target.sendPackets(new S_ServerMessage(95, clanname), true); // \f1%0 혈맹에 가입했습니다.
			target.getTeleport().start(target.getX(), target.getY(), target.getMapId(), target.getMoveState().getHeading(), false);
			return true;
		} catch(Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명] [혈맹이름] 입력"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(315), true), true);
			return false;
		}
	}
}



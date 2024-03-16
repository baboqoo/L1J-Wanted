package l1j.server.server.clientpackets;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class C_BanClan extends ClientBasePacket {

	private static final String C_BAN_CLAN = "[C] C_BanClan";
	private static Logger _log = Logger.getLogger(C_BanClan.class.getName());

	public C_BanClan(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);

		String s = readS();
		if (StringUtil.isNullOrEmpty(s)) {
			return;
		}
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		L1Clan clan = pc.getClan();
		if (clan == null) {
			return;
		}
		if (!(pc.isCrown() || pc.getId() == clan.getLeaderId() || pc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_PRINCE)) {// 군주 또는 부군주
			pc.sendPackets(L1ServerMessage.sm518); // 이 명령은 혈맹의 군주만 이용할 수 있습니다.
			return;
		}
		if (pc.getName().toLowerCase().equals(s.toLowerCase())) {
			return;
		}
		int castle_id = clan.getCastleId();
		if (castle_id != 0 && War.getInstance().isNowWar(castle_id)) {
			pc.sendPackets(L1ServerMessage.sm439);
			return;
		}
		L1PcInstance member = L1World.getInstance().getPlayer(s);
		if (member != null) { // 온라인중
			if (member.getClanid() == pc.getClanid()) { // 같은 크란
				if (pc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_PRINCE && (member.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_KING || member.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_PRINCE)) {
					return;
				}
				member.clearPlayerClanData(clan);
				clan.removeClanMember(member.getName());
				pc.sendPackets(new S_PacketBox(pc, S_PacketBox.PLEDGE_REFRESH_MINUS), true);
				member.sendPackets(new S_ServerMessage(238, pc.getClanName()), true);// 당신은%0혈맹으로부터추방되었습니다.
				pc.sendPackets(new S_ServerMessage(240, member.getName()), true);// %0가당신의 혈맹으로부터추방되었습니다.
			} else {
				pc.sendPackets(new S_ServerMessage(109, s), true);// %0라는이름의사람은없습니다.
			}
		} else { // 오프 라인중
			try {
				L1PcInstance restore_member = CharacterTable.getInstance().restoreCharacter(s);
				if (restore_member != null && restore_member.getClanid() == pc.getClanid()) {
					if (pc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_PRINCE && (restore_member.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_KING || restore_member.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_PRINCE)) {
						return;
					}
					restore_member.clearPlayerClanData(clan);
					clan.removeClanMember(restore_member.getName());
					pc.sendPackets(new S_ServerMessage(240, restore_member.getName()), true);// %0가당신의 혈맹으로부터추방되었습니다.
				} else {
					pc.sendPackets(new S_ServerMessage(109, s), true);// %0라는이름의사람은없습니다.
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	@Override
	public String getType() {
		return C_BAN_CLAN;
	}
}


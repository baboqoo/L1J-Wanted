package l1j.server.server.model.item.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.Gender;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.pledge.S_Pledge;
import l1j.server.server.serverpackets.pledge.S_PledgeWatch;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeEmblem;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeInfo;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeUserInfo;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class PledgeJoinItem extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public PledgeJoinItem(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch(this.getItemId()){
			case 500035:
				myPledgeJoin(pc);
				break;
			case 210118:
				beginnerPledgeJoin(pc);
				break;
			}
		}
	}
	
	/**
	 * 신규 혈맹 가입
	 * @param pc
	 */
	private void beginnerPledgeJoin(L1PcInstance pc){
		if (pc.getLevel() >= Config.PLEDGE.BEGINNER_PLEDGE_LEAVE_LEVEL) {
			//pc.sendPackets(new S_SystemMessage(String.format("%d레벨 이상은 신규혈맹에 가입할 수 없습니다.", Config.PLEDGE.BEGINNER_PLEDGE_LEAVE_LEVEL)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(100), String.valueOf(Config.PLEDGE.BEGINNER_PLEDGE_LEAVE_LEVEL)), true);
			return;
		}
		if (pc.getClanid() == 0) {
			L1Clan clan = L1World.getInstance().getClan(Config.PLEDGE.BEGINNER_PLEDGE_ID);
			if (clan == null) {
				return;
			}
			S_ServerMessage joinMessage = new S_ServerMessage(94, pc.getName());
			for (L1PcInstance member :  clan.getOnlineClanMember()) {
				member.sendPackets(joinMessage);
			}
			joinMessage.clear();
			joinMessage = null;
			pc.setClan(clan);
			pc.setClanid(clan.getClanId());
			pc.setClanName(clan.getClanName());
			pc.setTitle("\\f:New Templar Pledge");
			pc.setPledgeJoinDate((int)(System.currentTimeMillis() / 1000));
			pc.setBloodPledgeRank(eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT);
			try {
				pc.save(); // DB에 캐릭터 정보를 기입한다
			} catch(Exception e) {
				e.printStackTrace();
			}
			clan.addClanMember(pc.getName(), pc.getBloodPledgeRank(), pc.getLevel(), StringUtil.EmptyString, pc.getId(), pc.getType(), pc.getOnlineStatus(), 
					pc.getClanContribution(), pc.getClanWeekContribution(), 
					pc.getPledgeJoinDate(), (pc.getLastLogoutTime() == null ? 0 : (int)(pc.getLastLogoutTime().getTime() / 1000)),
					pc);
			pc.sendPackets(new S_BloodPledgeUserInfo(clan.getClanName(), pc.getBloodPledgeRank(), false), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aA[메티스]:신규 보호 혈맹에 가입 되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1090), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aA[메티스]:레벨(" + Config.PLEDGE.BEGINNER_PLEDGE_LEAVE_LEVEL + ")가 되면 자동으로 탈퇴됩니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1091) + Config.PLEDGE.BEGINNER_PLEDGE_LEAVE_LEVEL  + S_SystemMessage.getRefText(1092), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aA[메티스]:신규보호혈은 PK 데미지가 50%만 적용됩니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1093), true), true);
			pc.getInventory().removeItem(this, 1);
			
			L1ClanJoin.getInstance().pledge_join_object(pc);
		} else {
			pc.sendPackets(L1ServerMessage.sm89);
		}
	}
	
	/**
	 * 내 혈맹 가입
	 * @param pc
	 */
	private void myPledgeJoin(L1PcInstance pc){
		if (pc.isCrown()) {// 군주라면
			pc.sendPackets(pc.getGender() == Gender.MALE ? L1ServerMessage.sm87 : L1ServerMessage.sm88);
			return;
		}
		if (pc.getClanid() != 0) {// 혈맹이 있다면
			pc.sendPackets(L1ServerMessage.sm89);// 이미혈맹이있습니다
			return;
		}
		Connection con			=	null;
		PreparedStatement pstm	=	null;
		ResultSet rs			=	null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT `ClanID` FROM `characters` WHERE account_name = ? AND Type = 0 AND ClanID <> 0 LIMIT 1");
			pstm.setString(1, pc.getNetConnection().getAccountName());
			rs		= pstm.executeQuery();
			if (rs.next()) {
				L1Clan clan = L1World.getInstance().getClan(rs.getInt("ClanID"));
				if (clan == null) {
					pc.sendPackets(L1SystemMessage.SELF_CLAN_JOIN_EMPTY);
					return;
				}
				// 군주의 혈맹으로 가입
				S_ServerMessage joinMessage = new S_ServerMessage(94, pc.getName());
				for (L1PcInstance member : clan.getOnlineClanMember()) {// 접속한 혈맹원에게 메세지 뿌리고
					member.sendPackets(joinMessage);// \f1%0이 혈맹의 일원으로서 받아들여졌습니다.
				}
				joinMessage.clear();
				joinMessage = null;
				pc.setClan(clan);
				pc.setClanid(clan.getClanId());
				pc.setClanName(clan.getClanName());
				pc.setBloodPledgeRank(eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT);
				pc.setPledgeJoinDate((int)(System.currentTimeMillis() / 1000));
				pc.save(); // DB에 캐릭터 정보를 기입한다
				
				clan.addClanMember(pc.getName(), pc.getBloodPledgeRank(), pc.getLevel(), StringUtil.EmptyString, pc.getId(), pc.getType(), pc.getOnlineStatus(), 
						pc.getClanContribution(), pc.getClanWeekContribution(), 
						pc.getPledgeJoinDate(), (pc.getLastLogoutTime() == null ? 0 : (int)(pc.getLastLogoutTime().getTime() / 1000)),
						pc);
				pc.setClanMemberNotes(StringUtil.EmptyString);
				pc.sendPackets(new S_Pledge(pc, clan.getEmblemId(), pc.getBloodPledgeRank()), true);
				pc.broadcastPacketWithMe(new S_BloodPledgeEmblem(pc.getId(), clan.getClanId()), true);
				pc.sendPackets(new S_BloodPledgeInfo(pc.getClan().getEmblemStatus() == 1), true); // TODO
				pc.sendPackets(new S_PledgeWatch(null), true);
				for (L1PcInstance player : clan.getOnlineClanMember()) {
					player.sendPackets(new S_BloodPledgeEmblem(pc.getId(), pc.getClan().getEmblemId()), true);
				}
				pc.sendPackets(new S_ServerMessage(95, rs.getString("Clanname")), true);
				pc.getInventory().removeItem(this, 1);
				
				L1ClanJoin.getInstance().pledge_join_object(pc);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		if (pc.getClanid() == 0) {
			pc.sendPackets(L1SystemMessage.SELF_CLAN_JOIN_EMPTY);
		}
	}
}



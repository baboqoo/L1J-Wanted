package l1j.server.server.clientpackets;

import java.io.File;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanContributionBuffTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanMatching;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeMemberChanged;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeMemberChanged.MemberChangedReason;
import l1j.server.server.templates.L1House;
import l1j.server.web.dispatcher.response.world.BloodPledgeDAO;
import l1j.server.web.dispatcher.response.world.BloodPledgeVO;

public class C_LeaveClan extends ClientBasePacket {

	private static final String C_LEAVE_CLAN = "[C] C_LeaveClan";

	public C_LeaveClan(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance player = client.getActiveChar();
		if (player == null || player.getClanid() == 0) {
			return;
		}
		L1Clan clan = player.getClan();
		if (clan == null) {
			return;
		}
		// 해당 혈의 군주인가?
		if (player.isCrown() && player.getId() == clan.getLeaderId()) {
			L1ClanMatching clanMatching = L1ClanMatching.getInstance();
			clanMatching.deleteClanMatching(player);
			leaveClanBoss(clan, player);
		} else {// 군주가 아닌 혈맹원의 탈퇴
			leaveClanMember(clan, player);
		}
	}

	private void leaveClanBoss(L1Clan clan, L1PcInstance player) throws Exception {
		String playerName	= player.getName();
		String clanName		= player.getClanName();

		/*if (clan.getCastleId() > 0 || clan.getHouseId() > 0) {
			player.sendPackets(L1ServerMessage.sm665);
			return;
		}*/

		for (L1War war : L1World.getInstance().getWarList()) {
			if (war.CheckClanInWar(clanName)) {
				player.sendPackets(L1ServerMessage.sm302);
				return;
			}
		}

		if (!clan.getAlliance().isEmpty()) {// 동맹 존재
			player.sendPackets(L1ServerMessage.sm1235);
			return;
		}
		try {
			String emblemFile = String.valueOf(clan.getEmblemId());
			File deleteFile = new File("emblem/" + emblemFile);
			if (deleteFile.exists()) {
				deleteFile.delete();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		if (clan.getHouseId() > 0) {
			houseUpdate(clan.getHouseId());
		}
		
		ClanContributionBuffTable contribution = ClanContributionBuffTable.getInstance();
		contribution.removeContributionBuff(clan.getClanId());// 공헌도 버프 제거
		contribution.deleteBuff(clan.getClanId());// DB제거
		player.sendPackets(new S_BloodPledgeMemberChanged(MemberChangedReason.DEL_ME, player.getId()), true);// 혈맹원정보
		player.sendPackets(new S_BloodPledgeMemberChanged(MemberChangedReason.DEL, player.getId()), true);// 혈맹원정보
		L1PcInstance pc = null;
		for (int i = 0; i < clan.getClanMemberList().size(); i++) { 
			pc = L1World.getInstance().getPlayer(clan.getClanMemberList().get(i).name);// 혈맹원들의혈맹정보를초기화
			if (pc == null) {
				pc = CharacterTable.getInstance().restoreCharacter(clan.getClanMemberList().get(i).name);// 혈맹원이 오프라인인 경우
			} else {
				pc.sendPackets(new S_ServerMessage(269, playerName, clanName), true);// %1혈맹의 군주 %0가 혈맹을 해산시켰습니다.
			}
			pc.clearPlayerClanData(clan);
		}
		ClanTable.getInstance().deleteClan(clan);
	}

	private void leaveClanMember(L1Clan clan, L1PcInstance player) throws Exception {
		String playerName	= player.getName();
		String clanName		= player.getClanName();
		L1PcInstance[] clanMember = clan.getOnlineClanMember();
		for (int i = 0; i < clanMember.length; i++) {
			clanMember[i].sendPackets(new S_ServerMessage(178, playerName, clanName), true); // \f1%0이%1혈맹을 탈퇴했습니다.
		}
		if (player.isClanBuff()) {
			player.sendPackets(S_PacketBox.CLAN_BUFF_OFF);
			player.setClanBuff(false);
		}
		player.clearPlayerClanData(clan);
		clan.removeClanMember(playerName);
		player.broadcastPacket(new S_PCObject(player), true);
		if (Config.WEB.WEB_SERVER_ENABLE) {
			BloodPledgeVO webClan = BloodPledgeDAO.getPledge(clan.getClanId());
			if (webClan != null) {
				webClan.setTotalMember(clan.getClanMemberList().size());
			}
		}
	}
	
	private void houseUpdate(int houseId){
		HouseTable table = HouseTable.getInstance();
		L1House house = table.getHouseTable(houseId);
		if (house == null) {
			return;
		}
		house.setOnSale(true);  // 경매중으로 설정
		house.setPurchaseBasement(false); // 지하 아지트미구입으로 설정
		table.updateHouse(house); // DB에 기입해
		
	}

	@Override
	public String getType() {
		return C_LEAVE_CLAN;
	}
}

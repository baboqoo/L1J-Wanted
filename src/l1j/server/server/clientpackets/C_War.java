package l1j.server.server.clientpackets;

import java.util.List;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class C_War extends ClientBasePacket {
	private static final String C_WAR = "[C] C_War";

	public C_War(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance player = client.getActiveChar();
		if (player == null) {
			return;
		}
		int type = readC();
		String s = readS();

		String playerName	= player.getName();
		String clanName		= player.getClanName();
		int clanId			= player.getClanid();

		if (!player.isCrown()) { // 군주 이외
			player.sendPackets(L1ServerMessage.sm478);// \f1프린스와 프린세스만 전쟁을 포고할 수 있습니다.
			return;
		}
		if (clanId == 0) { // 크란미소속
			player.sendPackets(L1ServerMessage.sm272); // \f1전쟁하기 위해서는 우선 혈맹을 창설하지 않으면 안됩니다.
			return;
		}
		L1Clan clan = player.getClan();
		if (clan == null) {
			player.sendPackets(L1SystemMessage.TARGET_CLAN_EMPTY);
			return;
		}
		if (player.getId() != clan.getLeaderId()) { // 혈맹주
			player.sendPackets(L1ServerMessage.sm478); // \f1프린스와 프린세스만 전쟁을 포고할 수 있습니다.
			return;
		}
		if (clanName.toLowerCase().equals(s.toLowerCase())) {// 자크란을 지정
			return;
		}
		
		boolean inWar = false;
		List<L1War> warList = L1World.getInstance().getWarList(); // 전쟁 리스트를 취득
		for (L1War war : warList) {
			if (war.CheckClanInWar(clanName)) { // 자크란이 이미 전쟁중
				if (type == 0) { // 선전포고
					player.sendPackets(L1ServerMessage.sm234); // \f1당신의 혈맹은 벌써 전쟁중입니다.
					return;
				}
				inWar = true;
				break;
			}
		}
		if (!inWar && (type == 2 || type == 3)) {// 자크란이 전쟁중 이외로, 항복 또는 종결
			return;
		}
		if (clan.getCastleId() != 0) { // 자크란이 성주
			if (type == 0) { // 선전포고
				player.sendPackets(L1ServerMessage.sm474); // 당신은 벌써 성을 소유하고 있으므로, 다른 시로를 잡을 수 없습니다.
				return;
			}
			if (type == 2 || type == 3) {// 항복, 종결
				return;
			}
		}
		
		L1Clan enemyClan = L1World.getInstance().getClan(s);// 크란명을 체크
		if (enemyClan == null) {
			player.sendPackets(L1SystemMessage.TARGET_CLAN_EMPTY);
			return;
		}
		String enemyClanName = s;

		if (enemyClan.getCastleId() == 0 && player.getLevel() <= 15) {// 상대 크란이 성주는 아니고, 자캐릭터가 Lv15 이하
			player.sendPackets(L1ServerMessage.sm232); // \f1레벨 15 이하의 군주는 선전포고할 수 없습니다.
			return;
		}

		if (enemyClan.getCastleId() != 0 && player.getLevel() <  Config.PLEDGE.WAR_DECLARE_LEVEL) {// 상대 크란이 성주로, 자캐릭터가 Lv25 미만
			//player.sendPackets(new S_SystemMessage(String.format("레벨 %d부터 선포할 수 있습니다.", Config.PLEDGE.WAR_DECLARE_LEVEL)), true);
			player.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(35), String.valueOf(Config.PLEDGE.WAR_DECLARE_LEVEL)), true);
			return;
		}
		
		if (clan.getOnlineClanMember().length <= Config.PLEDGE.WAR_DECLARE_USER_COUNT) {   
			//player.sendPackets(new S_SystemMessage(String.format("접속한 혈맹원이 %d명 이상이면 선포가 가능합니다.", Config.PLEDGE.WAR_DECLARE_USER_COUNT)), true);
			player.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(36), String.valueOf(Config.PLEDGE.WAR_DECLARE_USER_COUNT)), true);
			return;
		}
		
		if (type == 0 && !clan.getAlliance().isEmpty() && enemyClan.getClanId() == clan.getAlliance().keySet().iterator().next()) {
			player.sendPackets(L1ServerMessage.sm1205);// 동맹: 전쟁 선포 불가(동맹 결성 혈맹)
			return;
		}

		if (enemyClan.getCastleId() != 0) { // 상대 크란이 성주
			int castle_id = enemyClan.getCastleId();
			if (War.getInstance().isNowWar(castle_id)) { // 전쟁 시간내
				L1PcInstance clanMember[] = clan.getOnlineClanMember();
				for (int k = 0; k < clanMember.length; k++) {
					if (L1CastleLocation.checkInWarArea(castle_id, clanMember[k]) && player != clanMember[k]) {
						player.sendPackets(new S_ServerMessage(477), true);
						// 공성: 선포 불가(성안에 혈맹원이 있음)
						int[] loc = Getback.GetBack_Location(clanMember[k]);
						L1Location _loc = new L1Location(loc[0], loc[1], (short) loc[2]);
						L1Map map = _loc.getMap();
						L1Location loc2 = L1Location.randomLocation(loc[0], loc[1], map, (short) loc[2], 1, 5, false);
						clanMember[k].getTeleport().start(loc2, 5, true);
						return;
					}
				}
				boolean enemyInWar = false;
				for (L1War war : warList) {
					if (war.CheckClanInWar(enemyClanName)) { // 상대 크란이 이미 전쟁중
						if (type == 0) { // 선전포고
							war.DeclareWar(clanName, enemyClanName);
							war.AddAttackClan(clanName);
						} else if (type == 2 || type == 3) {
							if (!war.CheckClanInSameWar(clanName, enemyClanName)) {// 자크란과 상대 크란이 다른 전쟁
								return;
							}
							if (type == 2) {// 항복
								war.SurrenderWar(clanName, enemyClanName);
							} else if (type == 3) {// 종결
								war.CeaseWar(clanName, enemyClanName);
							}
						}
						enemyInWar = true;
						break;
					}
				}
				if (!enemyInWar && type == 0) { // 상대 크란이 전쟁중 이외로, 선전포고
					L1War war = new L1War();
					war.handleCommands(1, clanName, enemyClanName); // 공성전 개시
				}
			} else { // 전쟁 시간외
				if (type == 0) {// 선전포고
					player.sendPackets(L1ServerMessage.sm476); // 아직 공성전의 시간이 아닙니다.
				}
			}
		} else { // 상대 크란이 성주는 아니다
			boolean enemyInWar = false;
			for (L1War war : warList) {
				if (war.CheckClanInWar(enemyClanName)) { // 상대 크란이 이미 전쟁중
					if (type == 0) { // 선전포고
						player.sendPackets(new S_ServerMessage(236, enemyClanName), true); // %0혈맹이 당신의 혈맹과의 전쟁을 거절했습니다.
						return;
					}
					if (type == 2 || type == 3) { // 항복 또는 종결
						if (!war.CheckClanInSameWar(clanName, enemyClanName)) {// 자크란과 상대 크란이 다른 전쟁
							return;
						}
					}
					enemyInWar = true;
					break;
				}
			}
			if (!enemyInWar && (type == 2 || type == 3)) {// 상대 크란이 전쟁중 이외로, 항복 또는 종결
				return;
			}

			// 공성전이 아닌 경우, 상대의 혈맹주의 승인이 필요
			L1PcInstance enemyLeader = L1World.getInstance().getPlayer(enemyClan.getLeaderName());

			if (enemyLeader == null) { // 상대의 혈맹주가 발견되지 않았다
				player.sendPackets(new S_ServerMessage(218, enemyClanName), true); // \f1%0 혈맹의 군주는 현재 월드에 없습니다.
				return;
			}

			switch(type) {
			case 0:// 선전포고
				enemyLeader.setTempID(player.getId()); // 상대의 오브젝트 ID를 보존해 둔다
				enemyLeader.sendPackets(new S_MessageYN(217, clanName, playerName), true); // %0혈맹의%1가 당신의 혈맹과의 전쟁을 바라고 있습니다. 전쟁에 응합니까? (Y/N)
				break;
			case 2:// 항복
				enemyLeader.setTempID(player.getId()); // 상대의 오브젝트 ID를 보존해 둔다
				enemyLeader.sendPackets(new S_MessageYN(221, clanName), true); // %0혈맹이 항복을 바라고 있습니다. 받아들입니까? (Y/N)
				break;
			case 3:// 종결
				enemyLeader.setTempID(player.getId()); // 상대의 오브젝트 ID를 보존해 둔다
				enemyLeader.sendPackets(new S_MessageYN(222, clanName), true); // %0혈맹이 전쟁의 종결을 바라고 있습니다. 종결합니까? (Y/N)
				break;
			default:break;
			}
		}
	}
	
	@Override
	public String getType() {
		return C_WAR;
	}

}



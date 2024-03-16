package l1j.server.server.model.Instance;

import java.util.List;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1WarSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_CastleMaster;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SiegeInjuryTimeNoti;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1CrownInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public L1CrownInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		if (player.isDead() || player.getClanid() == 0) {
			return;
		}
		String clanName = player.getClanName();
		L1Clan clan = player.getClan();
		if (clan == null || !player.isCrown()) {
			return;
		}
		if (player.isShapeChange()) {
//AUTO SRM: 			player.sendPackets(new S_SystemMessage("변신중에는 면류관을 취득할 수 없습니다."), true); // CHECKED OK
			player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1020), true), true);
			return;
		}
		if (player.getId() != clan.getLeaderId()) {// 혈맹주 이외
			return;
		}
		if (!checkRange(player)) {// 크라운의 1 셀 이내
			return;
		}
		if (clan.getCastleId() != 0){// 성주 크란
			player.sendPackets(L1ServerMessage.sm474);// 당신은 벌써 성을 소유하고 있으므로, 다른 시로를 잡을 수 없습니다.
			return;
		}

		// 크라운의 좌표로부터 castle_id를 취득
		int castle_id = L1CastleLocation.getCastleId(getX(), getY(), getMapId());

		// 포고하고 있을까 체크.단, 성주가 없는 경우는 포고 불요
		boolean existDefenseClan = false;
		L1Clan defence_clan = null;
		for (L1Clan defClan : L1World.getInstance().getAllClans()) {
			if (castle_id == defClan.getCastleId()) {
				// 전의 성주 크란
				defence_clan = defClan;
				existDefenseClan = true;
				break;
			}
		}
		boolean inWar = false;
		List<L1War> wars = L1World.getInstance().getWarList(); // 전전쟁 리스트를 취득
		for (L1War war : wars) {
			if (castle_id == war.GetCastleId()) { // 이마이성의 전쟁
				inWar = war.CheckClanInWar(clanName);
				break;
			}
		}
		if (existDefenseClan && !inWar) {// 성주가 있어, 포고하고 있지 않는 경우
			return;
		}
		
		
		// clan_data의 hascastle를 갱신해, 캐릭터에 크라운을 붙인다
		if (existDefenseClan && defence_clan != null) { // 전의 성주 크란이 있다
			defence_clan.setCastleId(0);
			ClanTable.getInstance().updateClan(defence_clan);
			L1PcInstance defence_clan_member[] = defence_clan.getOnlineClanMember();
			for (int m = 0; m < defence_clan_member.length; m++) {
				if (defence_clan_member[m].getId() == defence_clan.getLeaderId()) { // 전의 성주 크란의 군주
					defence_clan_member[m].sendPackets(new S_CastleMaster(0, defence_clan_member[m].getId()), true);
					// Broadcaster.broadcastPacket(defence_clan_member[m], new S_CastleMaster(0, defence_clan_member[m].getId()), true);
					L1World.getInstance().broadcastPacketToAll(new S_CastleMaster(0, defence_clan_member[m].getId()), true);
					break;
				}
			}
		}

		clan.setCastleId(castle_id);
		ClanTable.getInstance().updateClan(clan);

		player.sendPackets(new S_CastleMaster(castle_id, player.getId()), true);

		// Broadcaster.broadcastPacket(player, new S_CastleMaster(castle_id, player.getId()), true);
		L1World.getInstance().broadcastPacketToAll(new S_CastleMaster(castle_id, player.getId()), true);

		// 크란원 이외를 거리에 강제 텔레포트
		GeneralThreadPool.getInstance().execute(new tel(player, castle_id));

		// 메세지 표시
		for (L1War war : wars) {
			// System.out.println(defence_clan.getClanName() + " > " + war.GetDefenceClanName());
			if (defence_clan.getClanName().equalsIgnoreCase(war.GetDefenceClanName()) && war.CheckClanInWar(clanName) && existDefenseClan) {
				// 자크란이 참가중에서, 성주가 교대
				// System.out.println(war.GetCastleId() + " > 끝");
				war.WinCastleWar(clanName);
				break;
			}
		}

		if (clan.getOnlineClanMember().length > 0) {
			for (L1PcInstance pc : clan.getOnlineClanMember()) {
				pc.sendPackets(L1ServerMessage.sm643); // 성을 점거했습니다.
			}
		}
		deleteMe();
		L1TowerInstance lt = null;
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1TowerInstance) {
				lt = (L1TowerInstance) l1object;
				if (L1CastleLocation.checkInWarArea(castle_id, lt)) {
					lt.deleteMe();
				}
			}
		}
		
		// 타워를 spawn 한다
		L1WarSpawn warspawn = new L1WarSpawn();
		warspawn.SpawnTower(castle_id);

		/** 성문 수리 **/
		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (L1CastleLocation.checkInWarArea(castle_id, door)) {
				door.repairGate();
			}
		}
		
		/** 성 근위병  **/
		L1CastleGuardInstance npc = null;
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1CastleGuardInstance) {
				npc = (L1CastleGuardInstance) object;
				if (L1CastleLocation.checkInWarArea(castle_id, npc)) {
					npc.allTargetClear();
					if (npc.getX() != npc.getHomeX() || npc.getY() != npc.getHomeY()) {
					    npc.teleport(npc.getHomeX(), npc.getHomeY(), npc.getSpawn().getHeading());
					} else if (npc.getMoveState().getHeading() != npc.getSpawn().getHeading()) {
						npc.getMoveState().setHeading(npc.getSpawn().getHeading());
						npc.broadcastPacket(new S_ChangeHeading(npc), true);
					}
				}
			}
		}
		
		// 면류관을 잡았으니 전쟁중 삭제해야 다시 선포할수잇음
		L1War[] wr = L1World.getInstance().get_wars();
		for (int i = 0; i < wr.length; i++) {
			if (castle_id == wr[i].GetCastleId()) {
				L1World.getInstance().removeWar(wr[i]);
				continue;
			}
			if (wr[i].CheckClanInWar(clanName)) {
				wr[i].CeaseWar(clanName, wr[i].GetDefenceClanName());
			}
		}
		wr = null;

		War.getInstance().AttackClanSetting(castle_id, clanName);

		L1PcInstance defence_clan_member[] = clan.getOnlineClanMember();
		for (L1PcInstance pp : defence_clan_member) {
			int castleid = L1CastleLocation.getCastleIdByArea(pp);
			if (castleid == castle_id) {
				War.getInstance().WarTime_SendPacket(castleid, pp);
			}
		}
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		removeAllKnownObjects();
	}

	private boolean checkRange(L1PcInstance pc) {
		return (getX() - 1 <= pc.getX() && pc.getX() <= getX() + 1 && getY() - 1 <= pc.getY() && pc.getY() <= getY() + 1);
	}

	private class tel implements Runnable {
		L1PcInstance player;
		int castleId;

		public tel(L1PcInstance pc, int castleid) {
			player = pc;
			castleId = castleid;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(10);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc.getClanid() != player.getClanid() && !pc.isGm()) {
						if (L1CastleLocation.checkInWarArea(castleId, pc)) {
							int[] loc = L1CastleLocation.getGetBackLoc(castleId);
							pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
							Thread.sleep(5);
						}
					} else {
						if (pc.warZone) {
							pc.warZone = false;
							pc.sendPackets(S_SiegeInjuryTimeNoti.CASTLE_WAR_TIME_NONE);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}




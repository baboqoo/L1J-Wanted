package l1j.server.server.clientpackets.proto;

import java.util.List;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class A_SiegeWar extends ProtoHandler {
	protected A_SiegeWar(){}
	private A_SiegeWar(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (!_pc.isCrown()) {// 군주 이외
			_pc.sendPackets(L1ServerMessage.sm478);
			return;
		}
		if (_pc.getClanid() == 0) {// 혈맹 미소속
			_pc.sendPackets(L1ServerMessage.sm272);
			return;
		}
		
		L1Clan clan		= _pc.getClan();
		if (clan == null) {// 자크란이 발견되지 않는다
			_pc.sendPackets(L1ServerMessage.sm272);
			return;
		}
		if (_pc.getId() != clan.getLeaderId()) {// 혈맹주
			_pc.sendPackets(L1ServerMessage.sm478);
			return;
		}

		try {
			readP(1);// 0x08
			int castleId = readC();// 1켄트 2기란 4오크요새
			
			L1Clan targetClan		= null;
			String targetClanName	= null;
			
			// 성을 소유하고 있는 혈맹 조사
			for (L1Clan val : L1World.getInstance().getAllClans()) {
				if (castleId == val.getCastleId()) {
					targetClan		= val;
					targetClanName	= val.getClanName();
					break;
				}
			}
			
			if (targetClan == null) {// 상대 크란이 발견되지 않았다
				_pc.sendPackets(L1SystemMessage.TARGET_CLAN_EMPTY);
				return;
			}
			
			String clanName	= _pc.getClanName();
			if (clanName.toLowerCase().equals(targetClanName.toLowerCase())) {// 자크란을 지정
				_pc.sendPackets(L1SystemMessage.SAME_CLAN_CLIME_FAIL);
				return;
			}
			
			if (clan.getAlliance() != null && clan.getAlliance().containsKey(targetClan.getClanId())) {// 동맹 혈맹
				_pc.sendPackets(L1ServerMessage.sm1205);
				return;
			}
			
			if (clan.getCastleId() != 0) {// 자크란이 성주
				_pc.sendPackets(L1ServerMessage.sm474);
				return;
			}
			if (_pc.getLevel() < Config.PLEDGE.WAR_DECLARE_LEVEL) {
				//_pc.sendPackets(new S_SystemMessage(String.format("레벨 %d부터 선포할 수 있습니다.", Config.PLEDGE.WAR_DECLARE_LEVEL)), true);
				_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(35), String.valueOf(Config.PLEDGE.WAR_DECLARE_LEVEL)), true);
				return;
			}
			
			L1PcInstance clanMember[] = clan.getOnlineClanMember();
			if (clanMember.length <= Config.PLEDGE.WAR_DECLARE_USER_COUNT) {   
				//_pc.sendPackets(new S_SystemMessage(String.format("접속한 혈맹원이 %d명 이상이면 선포가 가능합니다.", Config.PLEDGE.WAR_DECLARE_USER_COUNT)), true);
				_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(36), String.valueOf(Config.PLEDGE.WAR_DECLARE_USER_COUNT)), true);
				return;
			}
			/*if (clan.getHouseId() > 0) {
//AUTO SRM:  				player.sendPackets(new S_SystemMessage("아지트가 있는 상태에서는 선전 포고를 할 수 없습니다."), true);				
 				player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(3), true), true);
				return;
			}*/
			
			if (!War.getInstance().isNowWar(castleId)) {
				_pc.sendPackets(L1ServerMessage.sm476);// 아직 공성전의 시간이 아닙니다.
				return;
			}

			int[] loc;
			for (int k = 0; k < clanMember.length; k++) {
				if (L1CastleLocation.checkInWarArea(castleId, clanMember[k])) {
					loc = L1CastleLocation.getGetBackLoc(castleId);
					clanMember[k].getTeleport().start(loc[0], loc[1], (short) loc[2], clanMember[k].getMoveState().getHeading(), true);
				}
			}
			loc = null;
			boolean enemyInWar = false;
			List<L1War> warList = L1World.getInstance().getWarList();// 전쟁 리스트를 취득
			for (L1War war : warList) {
				if (war.CheckClanInWar(targetClanName)) {// 상대 크란이 이미 전쟁중
					war.DeclareWar(clanName, targetClanName);
					war.AddAttackClan(clanName);
					enemyInWar = true;
					break;
				}
			}
			if (!enemyInWar) {// 상대 크란이 전쟁중 이외로, 선전포고
				new L1War().handleCommands(1, clanName, targetClanName);// 공성전 개시
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SiegeWar(data, client);
	}

}



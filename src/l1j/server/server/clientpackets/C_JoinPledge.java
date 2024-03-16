package l1j.server.server.clientpackets;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoin;
import l1j.server.server.utils.FaceToFace;

public class C_JoinPledge extends ClientBasePacket {
	private static final String C_JOIN_PLEDGE = "[C] C_JoinPledge";

	public C_JoinPledge(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null || pc.isGhost() ) {
			return;
		}

		L1PcInstance target = FaceToFace.faceToFace(pc);
		if (target != null) {
			join(pc, target);
		}
	}

	public static void join(L1PcInstance player, L1PcInstance target) {
		if (!eBloodPledgeRankType.isAuthRankAtKnight(target.getBloodPledgeRank())) {
			//player.sendPackets(new S_SystemMessage(String.format("%s는 왕자나 공주 수호기사가 아닙니다.", target.getName())), true);
			player.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(23), target.getName()), true);
			return;
		}

		L1Clan clan = target.getClan();
		if (clan == null) {
			player.sendPackets(new S_ServerMessage(90, target.getName()), true); // \f1%0은 혈맹을 창설하고 있지 않는 상태입니다.
			return;
		}
		// 가입제한 레벨
		if (clan.get_limit_level() > player.getLevel()) {
			player.sendPackets(S_BloodPledgeJoin.JOIN_LIMIT_LEVEL);
			return;
		}
		// 가입제한 이름
		if (clan.is_limit_user_names(player.getName())) {
			player.sendPackets(S_BloodPledgeJoin.JOIN_LIMIT_USER);
			return;
		}

		L1Clan player_pledge = player.getClan();
		if (player_pledge != null) {// 이미 혈맹에 가입한 상태
			if (player.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING 
					|| player.getId() != player_pledge.getLeaderId()) {
				player.sendPackets(L1ServerMessage.sm89);// 혈맹: 이미 혈맹에 가입한 상태
				return;
			}

			if (player_pledge.getCastleId() != 0 || player_pledge.getHouseId() != 0) {
				player.sendPackets(L1ServerMessage.sm665);// 혈맹: 해산 불가(성 또는 아지트 소유중)
				return;
			}
		}
		if (target.isAutoClanjoin()){
			L1ClanJoin.getInstance().join(target, player);
			return;
		}

		target.setTempID(player.getId()); // 상대의 오브젝트 ID를 보존해 둔다
		target.sendPackets(new S_MessageYN(97, player.getName()), true); // %0가 혈맹에 가입했지만은 있습니다. 승낙합니까? (Y/N)
	}

	@Override
	public String getType() {
		return C_JOIN_PLEDGE;
	}
}



package l1j.server.server.clientpackets;

import java.util.List;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ClanAttentionTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;

public class C_ClanAttention extends ClientBasePacket {
	private static final String C_CLAN_ATTENTION = "[C] C_ClanAttention";

	public C_ClanAttention(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		if (pc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING) {
			return;
		}
		
		int i = readC();
		if (i == 0) {
			if (pc.getClanid() == 0) {
				pc.sendPackets(L1ServerMessage.sm3349);//혈맹 주시: 문장 주시 불가, 없는 혈맹 혹은 연합혈맹 이거나 군주가 오프라인 상태
				return;
			}
			String clan_name = readS();
			if (clan_name == null || clan_name.isEmpty() || clan_name.length() <= 0) {
				pc.sendPackets(L1ServerMessage.sm3361);//혈맹 주시: 상대 혈맹의 이름을 입력하세요.
				return;
			}
			if (clan_name.equalsIgnoreCase(pc.getClanName())) {
				return;
			}

			L1Clan clan = L1World.getInstance().getClan(clan_name);
			if (clan == null || clan.getLeaderName() == null) {
				pc.sendPackets(L1ServerMessage.sm3349);//혈맹 주시: 문장 주시 불가, 없는 혈맹 혹은 연합혈맹 이거나 군주가 오프라인 상태
			}
			try {
				L1PcInstance targetPc = L1World.getInstance().getPlayer(clan.getLeaderName());
				if (targetPc == null) {
					pc.sendPackets(L1ServerMessage.sm3349);//혈맹 주시: 문장 주시 불가, 없는 혈맹 혹은 연합혈맹 이거나 군주가 오프라인 상태
					return;
				}
		
				List<L1War> warList = L1World.getInstance().getWarList(); // 전쟁 리스트를 취득
				for (L1War war : warList) {
					if (war.CheckClanInWar(pc.getClanName())) {
						if (war.GetEnemyClanName(pc.getClanName()).equalsIgnoreCase(clan_name)) {
							pc.sendPackets(L1ServerMessage.sm3324);//혈맹 주시: 문장 주시 불가, 대상 혈맹과 전쟁 상태
						} else {
							pc.sendPackets(L1ServerMessage.sm3383);//혈맹 주시: 전쟁 상태에선 사용 불가
						}
						return;
					}
				}
				targetPc.setTempID(pc.getId());
				targetPc.sendPackets(new S_MessageYN(3348, pc.getClanName()), true);
			} catch (Exception e) {
				return;
			}
			//혈맹 주시: %0 혈맹의 문장 주시를 승낙 하시겠습니까?
		} else if (i == 1) {
			String clan_name = readS();
			L1Clan clan = L1World.getInstance().getClan(clan_name);
			if (clan == null) {
				return;
			}
			ClanAttentionTable atten = ClanAttentionTable.getInstance();
			atten.deleteEmblemAttention(pc.getClanName(), clan_name);
			atten.deleteEmblemAttention(clan_name, pc.getClanName());
		}
	}

	public String getType() {
		return C_CLAN_ATTENTION;
	}
}

package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class C_BanParty extends ClientBasePacket {

	private static final String C_BAN_PARTY = "[C] C_BanParty";

	public C_BanParty(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		String s = readS();

		L1PcInstance player = client.getActiveChar();
		if ( player == null) {
			return;
		}
		if (!player.getParty().isLeader(player)) {
			// 파티 리더가 아닌 경우
			player.sendPackets(L1ServerMessage.sm427); // 파티의 리더만을 추방할 수 있습니다.
			return;
		}

		for (L1PcInstance member : player.getParty().getMembersArray()) {
			if (member.getName().toLowerCase().equals(s.toLowerCase())) {
				player.getParty().kickMember(member);
				return;
			}
		}
		// 발견되지 않았다
		player.sendPackets(new S_ServerMessage(426, s), true); // %0는 파티 멤버가 아닙니다.
	}

	@Override
	public String getType() {
		return C_BAN_PARTY;
	}

}


package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
//import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
//import l1j.server.server.templates.L1Castle;

public class C_ChangeWarTime extends ClientBasePacket {

	private static final String C_CHANGE_WAR_TIME = "[C] C_ChangeWarTime";

	public C_ChangeWarTime(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if (player == null) {
			return;
		}

		L1Clan clan = player.getClan();
		if (clan != null) {
			int castle_id = clan.getCastleId();
			if (castle_id != 0) {// 성주 크란
//				L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
				player.sendPackets(L1ServerMessage.sm305); // 추가
				//Calendar cal = l1castle.getWarTime(); // 주석처리
				//player.sendPackets(new S_WarTime(cal), true); // 주석처리
			}
		}
	}

	@Override
	public String getType() {
		return C_CHANGE_WAR_TIME;
	}

}


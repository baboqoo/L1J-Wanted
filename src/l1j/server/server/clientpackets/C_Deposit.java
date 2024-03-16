package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.templates.L1Castle;

public class C_Deposit extends ClientBasePacket {
	private static final String C_DEPOSIT = "[C] C_Deposit";

	public C_Deposit(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		int i = readD();
		int j = readD();
		L1PcInstance player = clientthread.getActiveChar();
		if ( player == null) {
			return;
		}
		if (i == player.getId()) {
			L1Clan clan = player.getClan();
			if (clan != null) {
				int castle_id = clan.getCastleId();
				if (castle_id == 0) {
					return;
				}
				if (!player.isCrown() || clan.getLeaderId() != player.getId()) {
					return;
				}
				if (War.getInstance().isNowWar(castle_id)) {
					player.sendPackets(L1SystemMessage.WAR_TIME_ACTION_FAIL);
					return;
				}
				if (castle_id != 0) {// 성주 크란
					L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
					synchronized (l1castle) {
						int money = l1castle.getPublicMoney();
						if (j < 0 || money < 0) {
							return;// 공과급 버그
						}
						if (player.getInventory().consumeItem(L1ItemId.ADENA, j)) {
							money += j;
							l1castle.setPublicMoney(money);
							CastleTable.getInstance().updateCastle(l1castle);
						}
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_DEPOSIT;
	}

}


package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Castle;

public class C_TaxRate extends ClientBasePacket {

	private static final String C_TAX_RATE = "[C] C_TaxRate";
	public C_TaxRate(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		L1PcInstance player = clientthread.getActiveChar();
		if (player == null) {
			return;
		}
		
		int i = readD();
		int j = readC();
		if (i != player.getId()) {
			return;
		}

		L1Clan clan = player.getClan();
		if (clan == null){
			return;
		}
		int castle_id = clan.getCastleId();
		if (castle_id == 0) {
			return;
		}
		L1Castle castle = CastleTable.getInstance().getCastleTable(castle_id);
		if (j >= 10 && j <= 50) {
			castle.setTaxRate(j);
			CastleTable.getInstance().updateCastle(castle);
		}
	}

	@Override
	public String getType() {
		return C_TAX_RATE;
	}

}


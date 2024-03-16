package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.templates.L1Castle;

public class C_SecurityStatusSet extends ClientBasePacket {

	private static final String C_SECURITYSTATUSSET = "[C] C_SecurityStatusSet";

	public C_SecurityStatusSet(byte abyte0[], GameClient client) {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		int objid = readD();
		int type = readC();
		@SuppressWarnings("unused")
		int unknow = readD();// ????

		L1Clan clan = pc.getClan();
		L1Castle castle = CastleTable.getInstance().getCastleTable(clan.getCastleId());

		int money = castle.getPublicMoney();

		if (castle.getCastleSecurity() == type || money < 100000) {
			return;
		}

		if (type == 1) {
			castle.setPublicMoney(money - 100000);
		}
		castle.setCastleSecurity(type);
		CastleTable.getInstance().updateCastle(castle);
		pc.sendPackets(new S_CloseList(objid), true);
	}

	@Override
	public String getType() {
		return C_SECURITYSTATUSSET;
	}
}


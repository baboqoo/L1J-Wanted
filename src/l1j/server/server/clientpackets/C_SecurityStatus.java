package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Castle;

public class C_SecurityStatus extends ClientBasePacket {

	private static final String C_SECURITYSTATUS = "[C] C_SecurityStatus";

	public C_SecurityStatus(byte abyte0[], GameClient client) {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		int objid = readD();

		L1Clan clan = pc.getClan();
		if (clan == null || clan.getCastleId() == 0) {
			return;
		}

		int castle_id = clan.getCastleId();
		String npcName = null;
		L1Castle castle = CastleTable.getInstance().getCastleTable(castle_id);

		switch (castle_id) {
		case 1:break;
		case 2:break;
		case 3:break;
		case 4:npcName = "$1238";break;
		case 5:break;
		default:break;
		}

		String[] htmldata = new String[]{ npcName, castle.getCastleSecurity() == 0 ? "$1118" : "$1117"};
		if (pc.isGm())
			pc.sendPackets(new S_SystemMessage("Dialog " + "CastleS"), true);													

		pc.sendPackets(new S_NPCTalkReturn(objid, "CastleS", htmldata), true);
	}

	@Override
	public String getType() {
		return C_SECURITYSTATUS;
	}
}


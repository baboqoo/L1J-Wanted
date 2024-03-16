package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.SoldierTable;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1Soldier;

public class S_SoldierBuyList extends ServerBasePacket {

	/**
	 * 성의 용병 리스트를 출력한다
	 */
	public S_SoldierBuyList(int objId, int castle_id) {
		L1Soldier soldier = SoldierTable.getInstance().getSoldierTable(castle_id);
		L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
		if (soldier == null || l1castle == null) {
			return;
		}
		// System.out.println(soldier.getSoldier1Name()+soldier.getSoldier2Name()+soldier.getSoldier3Name()+soldier.getSoldier4Name());zz
		writeC(Opcodes.S_MERCENARYEMPLOY);
		writeD(objId);
		writeD(l1castle.getPublicMoney());
		writeH(castle_id);
		writeH(0);
		writeS(soldier.getSoldier1Name());
		writeH(10000);
		writeH(1);
		writeS(soldier.getSoldier2Name());
		writeH(10000);
		writeH(2);
		writeS(soldier.getSoldier3Name());
		writeH(10000);
		writeH(3);
		writeS(soldier.getSoldier4Name());
		writeH(10000);
	}

	public S_SoldierBuyList(int objId, int i, boolean t) {
		L1Soldier soldier = SoldierTable.getInstance().getSoldierTable(4);
		L1Castle l1castle = CastleTable.getInstance().getCastleTable(4);
		if (soldier == null || l1castle == null) {
			return;
		}
		// System.out.println(soldier.getSoldier1Name()+soldier.getSoldier2Name()+soldier.getSoldier3Name()+soldier.getSoldier4Name());zz
		writeC(i);
		writeD(objId);
		writeD(l1castle.getPublicMoney());
		writeH(4);
		writeH(0);
		writeS(soldier.getSoldier1Name());
		writeH(10000);
		writeH(1);
		writeS(soldier.getSoldier2Name());
		writeH(10000);
		writeH(2);
		writeS(soldier.getSoldier3Name());
		writeH(10000);
		writeH(3);
		writeS(soldier.getSoldier4Name());
		writeH(10000);
	}

	@Override
	public byte[] getContent() throws IOException {
		return _bao.toByteArray();
	}
}


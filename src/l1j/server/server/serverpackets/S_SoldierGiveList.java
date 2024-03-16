package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.SoldierTable;
import l1j.server.server.templates.L1Soldier;

public class S_SoldierGiveList extends ServerBasePacket {

	private static final String S_SOLDIER_GIVE_LIST = "[S] S_SoldierGiveList";

	private byte[] _byte = null;

	public S_SoldierGiveList(int objid, int castle_id) {
		writeC(Opcodes.S_MERCENARYSELECT);
		L1Soldier soldier = SoldierTable.getInstance().getSoldierTable(castle_id);
		if(soldier == null)return;
		writeD(objid);
		writeH(castle_id);
		writeH(0);
		writeS(soldier.getSoldier1Name());
		writeH(1);
		writeS(soldier.getSoldier2Name());
		writeH(2);
		writeS(soldier.getSoldier3Name());
		writeH(3);
		writeS(soldier.getSoldier4Name());
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_SOLDIER_GIVE_LIST;
	}
}


package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.RevengeTable;
import l1j.server.server.serverpackets.revenge.S_RevengeDelete;

public class A_RevengeDelete extends ProtoHandler {
	protected A_RevengeDelete(){}
	private A_RevengeDelete(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		int deleteobj = readBit();// 삭제할타겟
		RevengeTable.getInstance().DeleteRevenge(_pc, deleteobj);
		_pc.sendPackets(S_RevengeDelete.SUCCESS);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_RevengeDelete(data, client);
	}

}


package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.serverpackets.S_Buddy;

public class A_BuddyMemo extends ProtoHandler {
	protected A_BuddyMemo(){}
	private A_BuddyMemo(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		int nameLength	= readC();
		String name		= readS(nameLength);
		readP(1);
		int memoLength	= readC();
		if (memoLength > 20) {
			_pc.sendPackets(new S_Buddy(name, false), true);
			return;// 길이제한
		}
		String memo		= readS(memoLength);
		BuddyTable bt	= BuddyTable.getInstance();
		L1Buddy buddy	= bt.getBuddyTable(_pc.getId());
		if (buddy.getMemo(name).equals(memo)) {
			_pc.sendPackets(new S_Buddy(name, false), true);
			return;
		}
		if (bt.updateBuddyMemo(_pc.getId(), name, memo) > 0) {
			_pc.sendPackets(new S_Buddy(name, true), true);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_BuddyMemo(data, client);
	}

}


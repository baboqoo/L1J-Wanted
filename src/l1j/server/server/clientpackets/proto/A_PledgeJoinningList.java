package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoinningList;
import l1j.server.server.serverpackets.pledge.ePLEDGE_JOINING_LIST_TYPE;

public class A_PledgeJoinningList extends ProtoHandler {
	protected A_PledgeJoinningList(){}
	private A_PledgeJoinningList(byte[] data, GameClient client) {
		super(data, client);
	}
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		ePLEDGE_JOINING_LIST_TYPE type = ePLEDGE_JOINING_LIST_TYPE.fromInt(readC());
		if (type == null) {
			return;
		}
		_pc.sendPackets(new S_BloodPledgeJoinningList(_pc, type));
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeJoinningList(data, client);
	}

}


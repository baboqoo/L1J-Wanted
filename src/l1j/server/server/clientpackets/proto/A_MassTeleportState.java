package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.spell.S_MassTeleportState;

public class A_MassTeleportState extends ProtoHandler {
	protected A_MassTeleportState(){}
	private A_MassTeleportState(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		_pc.getConfig()._massTeleportState = readC() == 1;
		_pc.sendPackets(_pc.getConfig()._massTeleportState ? S_MassTeleportState.ON : S_MassTeleportState.OFF);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_MassTeleportState(data, client);
	}

}


package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadPointEnchantStart;

public class A_EinhasadPointEnchantStart extends ProtoHandler {
	protected A_EinhasadPointEnchantStart(){}
	private A_EinhasadPointEnchantStart(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1Teleport tel = _pc.getTeleport();
		tel.setStateLoc(_pc.getX(), _pc.getY(), _pc.getMapId());
		tel.c_start(32761, 32832, (short) 5167, 5, true);// 축복의 땅
		_pc.sendPackets(new S_EinhasadPointEnchantStart(_pc), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EinhasadPointEnchantStart(data, client);
	}

}


package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadFaithListNoti;

public class A_EinhasadFaithList extends ProtoHandler {
	protected A_EinhasadFaithList(){}
	private A_EinhasadFaithList(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		_pc.sendPackets(new S_EinhasadFaithListNoti(_pc.getEinhasadFaith().getInfos()), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EinhasadFaithList(data, client);
	}
	
}


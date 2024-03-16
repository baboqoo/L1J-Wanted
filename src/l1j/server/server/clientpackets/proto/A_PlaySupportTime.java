package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.PlaySupportTable;
import l1j.server.server.serverpackets.playsupport.S_PSSTimeCheck;

public class A_PlaySupportTime extends ProtoHandler {
	protected A_PlaySupportTime(){}
	private A_PlaySupportTime(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !_pc.getConfig().isPlaySupport()) {
			return;
		}
		if (!PlaySupportTable.isSupportMap(_pc.getConfig().getPlaySupportType(), _pc.getMap().getBaseMapId())) {
			_pc.getConfig().finishPlaySupport();
			return;
		}
		int dungeonTime = _pc.getDungoenTimer().getRestTimer(_pc.getMapId());
		_pc.sendPackets(new S_PSSTimeCheck(dungeonTime), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PlaySupportTime(data, client);
	}

}


package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.system.S_PCMasterInfo;

public class A_PCMasterInfo extends ProtoHandler {
	protected A_PCMasterInfo(){}
	private A_PCMasterInfo(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !_pc.isPCCafe()) {
			return;
		}
		FreeBuffShieldHandler handler = _pc.getConfig().get_free_buff_shield();
		if (handler == null) {
			System.out.println(String.format("[A_PCMasterInfo] HANDLER_NOT_FOUND : CHAR_NAME(%s)", _pc.getName()));
			return;
		}
		_pc.sendPackets(new S_PCMasterInfo(handler), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PCMasterInfo(data, client);
	}

}


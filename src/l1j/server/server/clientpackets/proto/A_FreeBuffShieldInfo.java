package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.system.S_FreeBuffShieldInfo;

public class A_FreeBuffShieldInfo extends ProtoHandler {
	protected A_FreeBuffShieldInfo(){}
	private A_FreeBuffShieldInfo(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		FreeBuffShieldHandler handler = _pc.getConfig().get_free_buff_shield();
		if (handler == null) {
			System.out.println(String.format("[A_FreeBuffShieldInfo] HANDLER_NOT_FOUND : CHAR_NAME(%s)", _pc.getName()));
			return;
		}
		_pc.sendPackets(new S_FreeBuffShieldInfo(handler), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_FreeBuffShieldInfo(data, client);
	}

}


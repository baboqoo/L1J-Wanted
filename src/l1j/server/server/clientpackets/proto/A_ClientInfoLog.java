package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.serverpackets.spell.S_SpellLateHandlingNoti;

public class A_ClientInfoLog extends ProtoHandler {
	protected A_ClientInfoLog(){}
	private A_ClientInfoLog(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.noPlayerCK || _pc instanceof L1AiUserInstance) {
			return;
		}
		// 0x12이후 정보 출력
		
		_pc.sendPackets(S_SpellLateHandlingNoti.NOT_CORRECTION);// 무작위 주기로 호출된다.
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ClientInfoLog(data, client);
	}

}


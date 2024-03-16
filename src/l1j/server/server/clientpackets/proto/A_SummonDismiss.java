package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

public class A_SummonDismiss extends ProtoHandler {
	protected A_SummonDismiss(){}
	private A_SummonDismiss(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1SummonInstance summon = null;
		for (L1NpcInstance npc : _pc.getPetList().values()) {
			if (npc instanceof L1SummonInstance) {
				summon = (L1SummonInstance)npc;
				break;
			}
		}
		if (summon == null) {
			_pc.sendPackets(L1ServerMessage.sm8415);// 소환된 몬스터가 없습니다.
			return;
		}
		summon.death(null);// 소환 해제
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SummonDismiss(data, client);
	}

}


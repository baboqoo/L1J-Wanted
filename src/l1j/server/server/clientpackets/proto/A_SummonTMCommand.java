package l1j.server.server.clientpackets.proto;

import l1j.server.common.bin.companion.CompanionT;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

public class A_SummonTMCommand extends ProtoHandler {
	protected A_SummonTMCommand(){}
	private A_SummonTMCommand(byte[] data, GameClient client) {
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
		readP(1);// 0x08
		CompanionT.eCommand command = CompanionT.eCommand.fromInt(readC());
		if (command == null) {
			return;
		}
		
		switch(command){// 1:공격태세, 2:방어태세, 3:휴식, 4:산개, 5:경계
		case TM_Attack:
			summon.setCurrentStatus(1);
			break;
		case TM_Defensive:
			summon.setCurrentStatus(2);
			break;
		default:
			break;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SummonTMCommand(data, client);
	}

}


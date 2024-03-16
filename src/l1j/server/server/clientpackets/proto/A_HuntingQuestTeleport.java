package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.huntingquest.HuntingQuestTable;
import l1j.server.GameSystem.huntingquest.HuntingQuestTeleportObject;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.serverpackets.S_Paralysis;

public class A_HuntingQuestTeleport extends ProtoHandler {
	protected A_HuntingQuestTeleport(){}
	private A_HuntingQuestTeleport(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (!_pc.getMap().isEscapable() || _client.isInterServer()) {
			_pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			_pc.sendPackets(L1ServerMessage.sm4726);
			return;
		}
		readP(1);// 0x0a
		int length				= readC();
		String action_string	= readS(length);
		HuntingQuestTeleportObject obj = HuntingQuestTable.getHuntTelInfo(action_string);
		if (obj == null) {
			return;
		}
		if (!_pc.getInventory().consumeItem(obj.getTelItemId(), 1)) {
			_pc.sendPackets(L1ServerMessage.sm5359);// 입장에 필요한 조건이 맞지 않습니다.
			return;
		}
		_pc.getTeleport().start(obj.getTelX(), obj.getTelY(), (short) obj.getTelMapId(), _pc.getMoveState().getHeading(), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_HuntingQuestTeleport(data, client);
	}

}


package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.IndunSystem.indun.IndunType;
import l1j.server.common.bin.IndunCommonBinLoader;
import l1j.server.common.bin.indun.IndunInfoForClient;
import l1j.server.common.data.eArenaMapKind;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.indun.S_IndunMatchingRegister;
import l1j.server.server.serverpackets.indun.S_IndunMatchingRegister.ArenaMatchingRegisterResult;

public class A_IndunMatchingRegister extends ProtoHandler {
	protected A_IndunMatchingRegister(){}
	private A_IndunMatchingRegister(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (!Config.INTER.INTER_SERVER_ACTIVE || _client.isInterServer() || _pc.getConfig()._indunAutoMatching) {
			return;
		}
		readP(1);
		eArenaMapKind mapKind	= eArenaMapKind.fromInt(readBit());
		IndunType type = IndunType.getIndunType(mapKind.toInt());
		if (type == null) {
			return;
		}
		IndunInfoForClient indun = IndunCommonBinLoader.getCommonInfo(mapKind.toInt());
		if (indun == null) {
			System.out.println(String.format("[A_IndunAutomatchStart] INDUN_BIN_EMPTY : KIND(%d)", mapKind.toInt()));
			return;
		}
		if (indun.get_minLevel().getFirst() > _pc.getLevel()) {
			_pc.sendPackets(new S_IndunMatchingRegister(ArenaMatchingRegisterResult.FAIL_INVALID_LEVEL, mapKind), true);
			return;
		}
		
		L1ItemInstance keyItem = _pc.getInventory().findItemNameId(indun.get_keyItemId());
		if (keyItem == null) {
			keyItem = _pc.getInventory().findItemNameId(indun.get_bmkeyItemId());
		}
		if (keyItem == null) {
			keyItem = _pc.getInventory().findItemNameId(indun.get_eventKeyItemId());
		}
		if (keyItem == null) {
			_pc.sendPackets(new S_IndunMatchingRegister(ArenaMatchingRegisterResult.FAIL_INVALID_KEY, mapKind), true);
			return;
		}
		
		if (type == IndunType.AURAKIA && _pc.getInventory().checkItem(420121, 1)) {// 아우라키아 정화 정화의 파편 조각 소지
			_pc.sendPackets(L1ServerMessage.sm8598);
			return;
		}
		
		_pc.getConfig()._indunAutoMatching			= true;
		_pc.getConfig()._indunAutoMatchingMapKind	= mapKind;
		_pc.sendPackets(new S_IndunMatchingRegister(ArenaMatchingRegisterResult.SUCCESS, mapKind), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunMatchingRegister(data, client);
	}

}


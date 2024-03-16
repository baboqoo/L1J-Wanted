package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.IndunSystem.indun.IndunType;
import l1j.server.common.bin.IndunCommonBinLoader;
import l1j.server.common.bin.indun.IndunInfoForClient;
import l1j.server.common.data.eArenaMapKind;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.indun.S_IndunCreateRoom;
import l1j.server.server.serverpackets.indun.S_IndunQuickStart;

public class A_IndunSpeedEnter extends ProtoHandler {
	protected A_IndunSpeedEnter(){}
	private A_IndunSpeedEnter(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !Config.INTER.INTER_SERVER_ACTIVE || _client.isInterServer() || _pc.getConfig()._indunAutoMatching || _pc.isPrivateShop() || _pc.isFishing()) {
			return;
		}
		if (_pc.getAccount().getIndunCount() >= 5) {// 플레이 가능횟수 체크
			_pc.sendPackets(S_IndunCreateRoom.INDUN_PLAY_MAX);
			return;
		}
		if (IndunList.getIndunInfoList().size() >= IndunList.MAX_ROOM_COUNT) {// 최대 진행중인 맵체크
			_pc.sendPackets(S_IndunCreateRoom.INDUN_ROOM_MAX);
			return;
		}
		readP(1);
		eArenaMapKind mapKind		= eArenaMapKind.fromInt(readBit());
		IndunType indunType 		= IndunType.getIndunType(mapKind.toInt());
		IndunInfoForClient indun 	= IndunCommonBinLoader.getCommonInfo(mapKind.toInt());
		if (indun == null) {
			System.out.println(String.format("[A_IndunSpeedEnter] INDUN_BIN_EMPTY : KIND(%d)", mapKind.toInt()));
			return;
		}
		if (indun.get_minLevel().getFirst() > _pc.getLevel()) {
			_pc.sendPackets(S_IndunCreateRoom.INDUN_CREATE_FAIL);
			return;
		}
		
		L1PcInventory inv = _pc.getInventory();
		L1ItemInstance keyItem = inv.findItemNameId(indun.get_keyItemId());
		if (keyItem == null) {
			keyItem = inv.findItemNameId(indun.get_bmkeyItemId());
		}
		if (keyItem == null) {
			keyItem = inv.findItemNameId(indun.get_eventKeyItemId());
		}
		if (keyItem == null) {
			_pc.sendPackets(new S_IndunQuickStart(false, 0, mapKind), true);
			return;
		}
		if (indunType == IndunType.AURAKIA && inv.checkItem(420121, 1)) {// 정화의 파편 조각 소지
			_pc.sendPackets(L1ServerMessage.sm8598);
			return;
		}
		_pc.getConfig()._indunAutoMatching			= true;
		_pc.getConfig()._indunAutoMatchingMapKind		= mapKind;
		_pc.getConfig()._indunAutoMatchingNumber	= IndunList.create_room_id();
		_pc.sendPackets(new S_IndunQuickStart(true, _pc.getConfig()._indunAutoMatchingNumber, mapKind), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunSpeedEnter(data, client);
	}

}


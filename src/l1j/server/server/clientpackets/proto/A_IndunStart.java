package l1j.server.server.clientpackets.proto;

import l1j.server.IndunSystem.indun.IndunCreator;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_IndunGameStart;
import l1j.server.server.serverpackets.indun.S_IndunGameStart.ArenaGameStartResult;

public class A_IndunStart extends ProtoHandler {
	protected A_IndunStart(){}
	private A_IndunStart(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		int roomnumber = readBit();	// 방번호
		IndunInfo info = IndunList.getIndunInfo(roomnumber);
		if (info == null) {	// 방 존재여부 체크
			_pc.sendPackets(new S_IndunGameStart(ArenaGameStartResult.FAIL_INVALID_ROOM, roomnumber), true);
			return;
		}
		if (info.is_playing) {// 플레이상태 체크
			return;
		}
		if (!_pc.isGm() && info.infoUserList.size() < info.min_player) {	// 최소 참가자수 체크
			_pc.sendPackets(new S_IndunGameStart(ArenaGameStartResult.FAIL_INVALID_STATE, roomnumber), true);
			return;
		}
		for (L1PcInstance member : info.getMembers()) {// 준비상태 체크
			if (!member.getConfig()._IndunReady) {
				_pc.sendPackets(new S_IndunGameStart(ArenaGameStartResult.FAIL_NOT_ENOUGH_KEY, roomnumber), true);
				return;
			}
		}
		if (info.chief_id != _pc.getId()) {	// 방장체크
			_pc.sendPackets(new S_IndunGameStart(ArenaGameStartResult.FAIL_INVALID_OWNER, roomnumber), true);
			return;
		}
		
		IndunCreator creator = IndunCreator.getInstance();
		if (creator.countIndun(info.indunType) >= 50) {// 방 개수 체크
			_pc.sendPackets(new S_IndunGameStart(ArenaGameStartResult.FAIL_NOT_ENOUGH_KEY, roomnumber), true);
			return;
		}
		
		/** 게임시작 **/
		_pc.sendPackets(new S_IndunGameStart(ArenaGameStartResult.SUCCESS, roomnumber), true);
		info.is_playing = true;
		creator.create(info);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunStart(data, client);
	}

}


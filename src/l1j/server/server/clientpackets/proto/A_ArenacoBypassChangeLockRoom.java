package l1j.server.server.clientpackets.proto;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_ArenacoBypassChangeLockRoom;

public class A_ArenacoBypassChangeLockRoom extends ProtoHandler {
	protected A_ArenacoBypassChangeLockRoom(){}
	private A_ArenacoBypassChangeLockRoom(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		int room_id		= readBit();// 방번호
		readP(1);// 0x10
		boolean is_lock	= readBool();

		IndunInfo info	= IndunList.getIndunInfo(room_id);
		if (info == null) {
			_pc.sendPackets(new S_ArenacoBypassChangeLockRoom(S_ArenacoBypassChangeLockRoom.eResult.FAIL, room_id, is_lock), true);
			return;
		}
		if (info.is_playing || info.is_locked == is_lock) {
			_pc.sendPackets(new S_ArenacoBypassChangeLockRoom(S_ArenacoBypassChangeLockRoom.eResult.FAIL_INVALID_ROOM_STATUS, room_id, is_lock), true);
			return;
		}
		if (info.chief_id != _pc.getId()) {
			_pc.sendPackets(new S_ArenacoBypassChangeLockRoom(S_ArenacoBypassChangeLockRoom.eResult.FAIL_NOT_OWNER, room_id, is_lock), true);
			return;
		}
		info.is_locked	= is_lock;
		S_ArenacoBypassChangeLockRoom lock = new S_ArenacoBypassChangeLockRoom(S_ArenacoBypassChangeLockRoom.eResult.SUCCESS, room_id, is_lock);
		for (L1PcInstance member : info.getMembers()) {
			member.sendPackets(lock);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ArenacoBypassChangeLockRoom(data, client);
	}

}


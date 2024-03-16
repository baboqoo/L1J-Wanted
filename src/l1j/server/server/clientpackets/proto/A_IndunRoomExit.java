package l1j.server.server.clientpackets.proto;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_IndunChangeRoomStatus;
import l1j.server.server.serverpackets.indun.S_IndunExitRoom;
import l1j.server.server.serverpackets.indun.S_IndunExitRoom.ArenaRoomExitResult;

public class A_IndunRoomExit extends ProtoHandler {
	protected A_IndunRoomExit(){}
	private A_IndunRoomExit(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		int roomnumber = readBit();
		IndunInfo info = IndunList.getIndunInfo(roomnumber);
		if (info != null && !info.is_playing) {
			S_IndunExitRoom leave = new S_IndunExitRoom(ArenaRoomExitResult.SUCCESS, roomnumber);
			if (info.chief_id == _pc.getId()) {// 방장
				for (L1PcInstance member : info.getMembers()) {
					member.getConfig()._IndunReady = false;
					info.setUser(member);
					member.sendPackets(leave);
					member.getTeleport().start(33464, 32757, (short) 4, member.getMoveState().getHeading(), true);
				}
				IndunList.removeIndunInfo(roomnumber);
			} else {
				_pc.getConfig()._IndunReady = false;
				info.setUser(_pc);
				_pc.sendPackets(leave);
				S_IndunChangeRoomStatus change = new S_IndunChangeRoomStatus(info);
				for (L1PcInstance member : info.getMembers()) {
					member.sendPackets(change);
				}
				change.clear();
				change = null;
				_pc.getTeleport().start(33464, 32757, (short) 4, _pc.getMoveState().getHeading(), true);
			}
			leave.clear();
			leave = null;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunRoomExit(data, client);
	}

}


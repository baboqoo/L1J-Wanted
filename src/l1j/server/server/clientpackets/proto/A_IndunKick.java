package l1j.server.server.clientpackets.proto;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_IndunChangeRoomStatus;
import l1j.server.server.serverpackets.indun.S_IndunExitRoom;
import l1j.server.server.serverpackets.indun.S_IndunExitRoom.ArenaRoomExitResult;
import l1j.server.server.serverpackets.indun.S_IndunKick;
import l1j.server.server.serverpackets.indun.S_IndunKick.ArenaKickResult;
import l1j.server.server.serverpackets.indun.S_IndunKickNoti;

public class A_IndunKick extends ProtoHandler {
	protected A_IndunKick(){}
	private A_IndunKick(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		int roomnumber		= readBit();// 방번호
		readP(1);
		int targetid		= readBit();// 타겟오브젝트
		IndunInfo info		= IndunList.getIndunInfo(roomnumber);
		L1PcInstance target	= info.getInfoCheckUser(targetid);
		if (target == null) {
			return;
		}
		target.getConfig()._IndunReady = false;
		info.setUser(_pc);
		_pc.getTeleport().start(33464, 32757, (short) 4, _pc.getMoveState().getHeading(), true);
		target.sendPackets(new S_IndunExitRoom(ArenaRoomExitResult.SUCCESS, roomnumber), true);
		target.sendPackets(new S_IndunKick(ArenaKickResult.SUCCESS, roomnumber, target.getId()), true);
		target.sendPackets(new S_IndunKickNoti(roomnumber, target), true);
		S_IndunChangeRoomStatus change = new S_IndunChangeRoomStatus(info);
		for (L1PcInstance member : info.getMembers()) {
			member.sendPackets(change);
		}
		change.clear();
		change = null;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunKick(data, client);
	}

}


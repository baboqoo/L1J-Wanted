package l1j.server.IndunSystem.indun;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_IndunExitRoom;
import l1j.server.server.serverpackets.indun.S_IndunExitRoom.ArenaRoomExitResult;

public class IndunReadyCounter implements Runnable {
	private boolean active;
	private IndunInfo info;
	
	public IndunReadyCounter(IndunInfo info) {
		this.info = info;
		this.active = true;
	}
	
	@Override
	public void run() {
		if (!active || info.is_playing) {
			return;
		}
		try {
			S_IndunExitRoom leave = new S_IndunExitRoom(ArenaRoomExitResult.SUCCESS, info.room_id);
			for (L1PcInstance member : info.getMembers()) {
				if (member == null) {
					continue;
				}
				if (member.getConfig()._IndunReady) {
					member.getConfig()._IndunReady = false;
				}
				info.setUser(member);
				member.sendPackets(leave);
				member.getTeleport().start(33464, 32757, (short) 4, member.getMoveState().getHeading(), true);
			}
			leave.clear();
			leave = null;
			IndunList.removeIndunInfo(info.room_id);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cancel(){
		active = false;
	}
}


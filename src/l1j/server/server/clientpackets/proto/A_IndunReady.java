package l1j.server.server.clientpackets.proto;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.IndunSystem.indun.IndunReadyCounter;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_IndunChangeReadyEnter;
import l1j.server.server.serverpackets.indun.S_IndunChangeReadyEnter.ArenaReadyResult;
import l1j.server.server.serverpackets.indun.S_ArenaoBypassChangeReadyEnterNoti;

public class A_IndunReady extends ProtoHandler {
	protected A_IndunReady(){}
	private A_IndunReady(byte[] data, GameClient client) {
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
		boolean ready		= readBool();
		_pc.getConfig()._IndunReady		= ready;
		IndunInfo info		= IndunList.getIndunInfo(roomnumber);
		
		int readyCnt		= 0;
		
		S_IndunChangeReadyEnter roomready		= new S_IndunChangeReadyEnter(ArenaReadyResult.SUCCESS, roomnumber, _pc.getConfig()._IndunReady);
		S_ArenaoBypassChangeReadyEnterNoti noti	= new S_ArenaoBypassChangeReadyEnterNoti(roomnumber, _pc.getId(), _pc.getConfig()._IndunReady);
		for (L1PcInstance member : info.getMembers()) {
			member.sendPackets(roomready);
			member.sendPackets(noti);
			if (member.getConfig()._IndunReady) {
				readyCnt++;
			}
		}
		
		// 모든 인원 래디
		if (info.max_player == readyCnt) {
			if (info.readyCounter == null) {
				for (L1PcInstance member : info.getMembers()) {
					if (member == null) {
						continue;
					}
					member.sendPackets(L1ServerMessage.sm4923);// 모든 인원이 준비되었습니다. 1분 이내에 시작하지 않으면 방에서 퇴장하게 됩니다.
				}
				info.readyCounter = new IndunReadyCounter(info);
				GeneralThreadPool.getInstance().schedule(info.readyCounter, 60000L);
			}
		} else {
			if (info.readyCounter != null) {
				info.readyCounter.cancel();
				info.readyCounter = null;
			}
		}
		
		roomready.clear();
		noti.clear();
		roomready = null;
		noti = null;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunReady(data, client);
	}

}


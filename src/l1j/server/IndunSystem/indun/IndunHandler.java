package l1j.server.IndunSystem.indun;

import java.util.ArrayList;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_ArenaPlayStatusNoti;
import l1j.server.server.serverpackets.indun.S_ArenaGameInfoNoti;
import l1j.server.server.serverpackets.indun.S_ArenaPlayEventNoti;

/**
 * 인스턴스 던전 핸들러(인터서버 전용)
 * @author LinOffice
 */
public abstract class IndunHandler implements Runnable {
	protected final IndunInfo _info;
	protected final short _map;
	protected final ArrayList<L1PcInstance> _pclist;
	protected final IndunUtill _util;
	
	protected boolean running = true;
	protected int limitTime;
	
	protected IndunHandler(IndunInfo info, short mapId){
		_info		= info;
		_map		= mapId;
		_pclist		= new ArrayList<>();
		_util		= IndunUtill.getInstance();
		limitTime	= info.indunType == IndunType.AURAKIA ? 1800 : 900;
	}
	
	@Override
	public void run() {
		try {
			startSetting();
			startSpawn();
			while(running){
				try {
					process();
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					Thread.sleep(1500L);
				}
			}
			endIndun();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void startSpawn() throws Exception;
	protected abstract void process() throws Exception;
	protected abstract void timerCheck() throws Exception;
	protected abstract void startIndun();
	protected abstract void endIndun();
	
	protected class TimerCheck implements Runnable {
		public TimerCheck() {}
		@Override
		public void run() {
			try {
				if (!running) {
					return;
				}
				timerCheck();
				GeneralThreadPool.getInstance().schedule(TimerCheck.this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void dispose(){
		clearPcList();
		_info.clearUserList();
		IndunUtill.getInstance().mapObjectDelete(_map);
		IndunCreator.getInstance().removeIndun(_map, _info);
		IndunList.removeIndunInfo(_info.room_id);
	}
	
	void startSetting(){
		try {
			boolean waiting = true;
			ArrayList<L1PcInstance> userList = null;
			int waitCount = 0;
			while(waiting){
				if (_info == null || waitCount >= 60) {
					break;
				}
				waitCount++;
				userList = _info.getMembers(); 
				if (userList.size() != _info.infoUserList.size()) {
					continue;
				}
				waiting = false;
				for (L1PcInstance pc : userList) {
					if (!pc.getConfig().isIndunLoginCheck()) {
						waiting = true;
						break;
					}
				}
				if (waiting) {
					Thread.sleep(100L);
				}
			}
			
			L1Party party = new L1Party();
		    for (L1PcInstance member : _info.getMembers()) {
		    	member.sendPackets(new S_ArenaPlayEventNoti(member, S_ArenaPlayEventNoti.eType.YourSelfEntered, _info), true);
		    	member.sendPackets(new S_ArenaGameInfoNoti(member, true, _info), true);
		    	member.sendPackets(new S_ArenaPlayStatusNoti(member, true, _info), true);
		    	for (L1PcInstance user : _info.getMembers()) {
		    		if (member.getId() != user.getId()) {
		    			member.sendPackets(new S_ArenaPlayEventNoti(member, S_ArenaPlayEventNoti.eType.PlayerEntered, _info), true);
		    		}
		    		member.sendPackets(new S_ArenaPlayStatusNoti(member, false, _info), true);
		    	}
		    	party.addIndunMember(member, _info);
		    	addPc(member);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void closeTeleport(){
		for (int i=0; i<_pclist.size(); i++) {
			L1PcInstance pc = _pclist.get(i);
			if (pc == null) {
				continue;
			}
			if (pc.isInParty()) {
				pc.getParty().leaveMember(pc);// 파티탈퇴
			}
			removePc(pc);
			if (!pc.isDead()) {
				pc.getTeleport().start(33464, 32757, (short) 4, pc.getMoveState().getHeading(), true, true);
			}
		}
	}
	
	protected void addPc(L1PcInstance pc){
		if (_pclist.contains(pc)) {
			return;
		}
		_pclist.add(pc);
	}
	
	protected void removePc(L1PcInstance pc){
		if (!_pclist.contains(pc)) {
			return;
		}
		pc.getConfig().setBossId(0);
		_pclist.remove(pc);
	}
	
	protected void clearPcList(){
		if (_pclist != null) {
			for (L1PcInstance pc : _pclist) {
				pc.getConfig().setBossId(0);
			}
			_pclist.clear();
		}
	}
}


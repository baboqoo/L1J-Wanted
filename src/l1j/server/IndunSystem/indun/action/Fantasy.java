package l1j.server.IndunSystem.indun.action;

import l1j.server.IndunSystem.indun.IndunHandler;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.indun.S_ArenaPlayStatusNoti;
import l1j.server.server.serverpackets.message.S_DialogueMessage;
import l1j.server.server.utils.CommonUtil;

/**
 * 몽환의 섬
 * @author LinOffice
 */
public class Fantasy extends IndunHandler {
	private int stage					= 1;
	private static final int WAIT_STEP	= 1;
	private static final int BOSS_STEP	= 2;
	private static final int LAST_STEP	= 3;
	private static final int EVENT_STEP	= 4;
	private static final int END		= 5;
	
	private static final int SUCUBUS_POINT = 80;// 몽환의 서큐버스 필요점수
	private int current_point = 0;// 현재 포인트
	
	private static final String[] MENT_ARRAY = {
			"$31901", "$31902", "$31903", "$31904", "$31905", "$31906", "$31907", "$31908", "$31909", "$31910",
			"$31911", "$31912", "$31913", "$31914", "$31915", "$31916", "$31917", "$31918", "$31919", "$31920",
			"$31921", "$31922", "$31923", "$31924", "$31925", "$31926", "$31927", "$31928", "$31929", "$31930",
			"$31931", "$31932", "$31933", "$31934", "$31935", "$31936", "$31937", "$31938", "$31939", "$31940",
			"$31941", "$31942", "$31943", "$31944", "$31945"
	};
	
	private L1NpcInstance water, fire, wind, earth, unicon, nightmare, event_boss, endTrap;
	private boolean spiritResult = true;
	
	public Fantasy(IndunInfo info, short mapId){
		super(info, mapId);
	}

	@Override
	protected void startSpawn() throws Exception {
		spawn_fire();
	}

	@Override
	protected void process() throws Exception {
		switch(stage){
		case WAIT_STEP:
			Thread.sleep(20000L);
			start_Spawn();
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(S_DialogueMessage.FANTASY_START_MENT);	//	이미지멘트
			}
			Thread.sleep(20000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.FANTASY_MENT_1);	//	각 속성방에 있는 대정령의 처치하세요
				pc.sendPackets(new S_ArenaPlayStatusNoti(_info, null), true);	//	타이머 시작
			}
			_util.deleteDoor(_map);
			GeneralThreadPool.getInstance().execute(new TimerCheck());	//	제한시간 타이머 시작
			stage = BOSS_STEP;
			break;
		case BOSS_STEP:
			if (water == null && fire == null && wind == null && earth == null) {
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
				}
				Thread.sleep(2000L);
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
				}
				Thread.sleep(2000L);
				_util.deleteNpc(7800200, _map);//	속박당한 유니콘 삭제
				if (spiritResult) {
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(S_DialogueMessage.UNICON_START_MENT);	//	이미지멘트
					}
					unicon = _util.spawn(_map, 77, 0);	//	유니콘
				} else {
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(S_DialogueMessage.UNICON_END_MENT);	//	이미지멘트
					}
					nightmare = _util.spawn(_map, 78, 0);	//	나이트메어
				}
				stage = LAST_STEP;
			}
			break;
		case LAST_STEP:
			if (unicon != null && unicon.isDead()) {
				int number = CommonUtil.random(45);
				_util.sendPacket(_pclist, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, MENT_ARRAY[number]), true);
				current_point += number + 1;
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_DialogueMessage.UNICON_END_MENT);	//	이미지멘트
				}
				Thread.sleep(2000L);
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
				}
				Thread.sleep(2000L);
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
				}
				Thread.sleep(2000L);
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
				}
				nightmare = _util.spawn(_map, 78, 0);	//	나이트메어
				unicon = null;
			}
			if (nightmare != null && nightmare.isDead()) {
				int number = CommonUtil.random(45);
				_util.sendPacket(_pclist, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, MENT_ARRAY[number]), true);
				current_point += number + 1;
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_DialogueMessage.NIGHTMARE_END_MENT);	//	이미지멘트
				}
				Thread.sleep(2000L);
				if (current_point > SUCUBUS_POINT) {	//	히든보스
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
					}
					Thread.sleep(2000L);
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
					}
					Thread.sleep(2000L);
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
					}
					event_boss = _util.spawn(_map, 79, 0);	//	몽환의 서큐버스
					stage = EVENT_STEP;
				} else {
					stage = END;
				}
				nightmare = null;
			}
			break;
		case EVENT_STEP:
			if (event_boss != null && event_boss.isDead()) {
				if (event_boss != null) {
					event_boss = null;
				}
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_DialogueMessage.SUCUBUS_END_MENT);	//	이미지멘트
				}
				Thread.sleep(4000L);
				stage = END;
			}
			break;
		case END:
			Thread.sleep(10000L);
			endTrap = _util.spawn(_map, 80, 60000);	//	이동법진
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.FANTASY_MENT_2);	//	이제 당신의 세계로 돌아가십시오.
			}
			Thread.sleep(10000L);
			closeTeleport();
			break;
		default:break;
		}
	}

	@Override
	protected void timerCheck() throws Exception {
		if (limitTime <= 0) {
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.INDUN_CLOSE_MENT);	//	시공 여행이 종료 됩니다.
				pc.sendPackets(L1GreenMessage.INDUN_CLOSE_MENT_2);	//	다음에 다시 도전해주세요.
			}
			closeTeleport();
			return;
		}
		limitTime--;
		if (limitTime == 60 * 12) {
			failSpirit();//	대정령제한시간
		}
		checkPc();
		checkSpirit();
	}
	
	private void checkPc() throws Exception {
		if (_pclist.isEmpty()) {
			endIndun();
			return;
		}
		int diechck = 0;
		for (L1PcInstance pc : _pclist) {
			if (pc != null && pc.isDead()) {
				diechck++;
			}
		}
		if (_pclist.size() <= diechck) {
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.INDUN_PARTY_DEATH);	//	모든 파티원이 사망하였습니다.
			}
			Thread.sleep(10000L);
			endIndun();
			return;
		}
		for (int i=0; i<_pclist.size(); i++) {
			L1PcInstance pc = _pclist.get(i);// pc타입으로 변환
			if (pc == null || pc.getMapId() != _map) {
				removePc(_pclist.get(i));// 존재여부 체크
			}
		}
		if (endTrap != null) {
			for (int i=0; i<_pclist.size(); i++) {
				L1PcInstance pc = _pclist.get(i);
				if (pc == null) {
					continue;
				}
				if (!pc.isDead() && (pc.getX() >= endTrap.getX() - 1 && pc.getX() <= endTrap.getX() + 1) && (pc.getY() >= endTrap.getY() - 1 && pc.getY() <= endTrap.getY() + 1) && pc.getMapId() == endTrap.getMapId()) {
					if (pc.isInParty()) {
						pc.getParty().leaveMember(pc);//	파티탈퇴
					}
					removePc(pc);
					pc.getTeleport().start(33464, 32757, (short) 4, pc.getMoveState().getHeading(), true, true);
				}
			}
		}
	}
	
	private void checkSpirit() {
    	if (water != null && water.isDead()) {
    		int number = CommonUtil.random(45);
    		_util.sendPacket(_pclist, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, MENT_ARRAY[number]), true);
			current_point += number + 1;
			water = null;
		}
		if (fire != null && fire.isDead()) {
			int number = CommonUtil.random(45);
			_util.sendPacket(_pclist, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, MENT_ARRAY[number]), true);
			current_point += number + 1;
			fire = null;
		}
		if (wind != null && wind.isDead()) {
			int number = CommonUtil.random(45);
			_util.sendPacket(_pclist, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, MENT_ARRAY[number]), true);
			current_point += number + 1;
			wind = null;
		}
		if (earth != null && earth.isDead()) {
			int number = CommonUtil.random(45);
			_util.sendPacket(_pclist, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, MENT_ARRAY[number]), true);
			current_point += number + 1;
			earth = null;
		}
	}
	
	private void failSpirit(){
		if (water != null && !water.isDead()) {
			for (int i = 0; i < 2; i++) {
				_util.spawn(_map, 65, 0);
			}
			for (int i = 0; i < 4; i++) {
				_util.spawn(_map, 66, 0);
			}
			for (int i = 0; i < 4; i++) {
				_util.spawn(_map, 67, 0);
			}
			_util.deleteNpc(7800202, _map);
			water = null;
			spiritResult = false;
		}
		if (fire != null && !fire.isDead()) {
			for (int i = 0; i < 2; i++) {
				_util.spawn(_map, 68, 0);
			}
			for (int i = 0; i < 4; i++) {
				_util.spawn(_map, 69, 0);
			}
			for (int i = 0; i < 4; i++) {
				_util.spawn(_map, 70, 0);
			}
			_util.deleteNpc(7800204, _map);
			fire = null;
			spiritResult = false;
		}
		if (wind != null && !wind.isDead()) {
			for (int i = 0; i < 2; i++) {
				_util.spawn(_map, 71, 0);
			}
			for (int i = 0; i < 4; i++) {
				_util.spawn(_map, 72, 0);
			}
			for (int i = 0; i < 4; i++) {
				_util.spawn(_map, 73, 0);
			}
			_util.deleteNpc(7800206, _map);
			wind = null;
			spiritResult = false;
		}
		if (earth != null && !earth.isDead()) {
			for (int i = 0; i < 2; i++) {
				_util.spawn(_map, 74, 0);
			}
			for (int i = 0; i < 4; i++) {
				_util.spawn(_map, 75, 0);
			}
			for (int i = 0; i < 4; i++) {
				_util.spawn(_map, 76, 0);
			}
			_util.deleteNpc(7800208, _map);
			earth = null;
			spiritResult = false;
		}
	}
	
	private void spawn_fire(){
		_util.doorSpawn(12754, 32779, 32766, (short) _map, true);
		_util.doorSpawn(12754, 32779, 32767, (short) _map, true);
		_util.doorSpawn(12754, 32779, 32768, (short) _map, true);
		
		_util.doorSpawn(12754, 32767, 32778, (short) _map, false);
		_util.doorSpawn(12754, 32768, 32778, (short) _map, false);
		_util.doorSpawn(12754, 32769, 32778, (short) _map, false);
		
		_util.doorSpawn(12754, 32757, 32766, (short) _map, true);
		_util.doorSpawn(12754, 32757, 32767, (short) _map, true);
		_util.doorSpawn(12754, 32757, 32768, (short) _map, true);
		
		_util.doorSpawn(12754, 32766, 32752, (short) _map, false);
		_util.doorSpawn(12754, 32767, 32752, (short) _map, false);
		_util.doorSpawn(12754, 32768, 32752, (short) _map, false);
	}
	
	private void start_Spawn(){
		_util.spawn(_map, 60, 0);				//	속박당한 유니콘
		water	= _util.spawn(_map, 61, 0);		//	물의 대정령
		fire	= _util.spawn(_map, 62, 0);		//	불의 대정령
		wind	= _util.spawn(_map, 63, 0);		//	바람의 대정령
		earth	= _util.spawn(_map, 64, 0);		//	땅의 대정령
	}
	
	@Override
	protected void startIndun() {
		System.out.println("■■■■■■ Dream Island (in-dungeon) start map number " + _map + " room number " + _info.room_id + " ■■■■■■");
		GeneralThreadPool.getInstance().schedule(this, 4000);
	}

	@Override
	protected void endIndun() {
		if (running) {
			running = false;
			System.out.println("■■■■■■ Dream Island (in-dungeon) end map number " + _map + " room number " + _info.room_id + " ■■■■■■");
			dispose();
		}
	}
	
	@Override
	protected void dispose() {
		super.dispose();
	}
}


package l1j.server.IndunSystem.indun.action;

import l1j.server.IndunSystem.indun.IndunHandler;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.indun.S_ArenaPlayStatusNoti;
import l1j.server.server.serverpackets.message.S_DialogueMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 악어섬의 비밀
 * @author LinOffice
 */
public class Crocodile extends IndunHandler {
	private int stage						= 1;
	private static final int WAIT_STEP		= 1;
	private static final int FIRST_STEP		= 2;
	private static final int SECOND_STEP	= 3;
	private static final int LAST_STEP		= 4;
	private static final int EVENT_STEP		= 5;
	private static final int END			= 6;
	
	private static final int[] _First_MonsterList	= { 7800106, 7800107 };
	private static final int[] _Second_MonsterList	= { 7800108, 7800109, 7800110, 7800111 };
	private static final int[] _third_MonsterList	= { 7800112, 7800113 };
	private L1NpcInstance boss_1, boss_2, boss_3, event_boss, moveTrap_1, moveTrap_2, moveTrap_3;
	
	public Crocodile(IndunInfo info, short mapId){
		super(info, mapId);
	}

	@Override
	protected void startSpawn() throws Exception {
		_util.spawn(_map, 50, 0);// 게렝
		boss_1 = _util.spawn(_map, 51, 0);// 로서스의 감시자
		boss_2 = _util.spawn(_map, 52, 0);// 로서스의 감시자
		spawn_monster();
		spawn_door();
	}

	@Override
	protected void process() throws Exception {
		switch(stage){
		case WAIT_STEP:
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(420113), 1) != L1Inventory.OK) continue;
				pc.getInventory().storeItem(420113, 1);
			}
			Thread.sleep(6000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(S_DialogueMessage.SAGE_START_MENT); // 게렝 멘트
			}
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.CROCODILE_MENT_1);	// 이제 곧 괴물들이 몰려 올 것일세
				pc.sendPackets(L1GreenMessage.CROCODILE_MENT_2);	// 각오를 단단히 하는 것이 좋을 것이야
			}
			Thread.sleep(10000L);
			_util.deleteDoor(_map);
			_util.sendPacket(_pclist, new S_ArenaPlayStatusNoti(_info, null), true);
			GeneralThreadPool.getInstance().execute(new TimerCheck());
			stage = FIRST_STEP;
			break;
		case FIRST_STEP:
			if (boss_1 != null && boss_1.isDead()) { // 로서스 감시자
				boss_1 = null;
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(L1GreenMessage.CROCODILE_MENT_3);	// 동쪽 끝 등대를 찾아가게. 그 곳을 통하여 지하수로에 갈 수 있다네
					pc.sendPackets(L1GreenMessage.CROCODILE_MENT_4);	// 수로의 끝은 악어섬과 연결되어 있지
					pc.sendPackets(L1GreenMessage.CROCODILE_MENT_5);	// 그 발걸음을 멈추지 않겠다면 굳이 막아서지는 않겠네만... 조심하게
				}
				Thread.sleep(4000L);
				moveTrap_1 = _util.spawn(_map, 56, 0); // 1단계 이동트랩
			}
			if (boss_2 != null && boss_2.isDead()) { // 로서스 감시자
				boss_2 = null;
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(L1GreenMessage.CROCODILE_MENT_6);	// 수 십년간 우리조차 접근하기 꺼려했던 곳을 기어코 가려하는가
					pc.sendPackets(L1GreenMessage.CROCODILE_MENT_7);	// 이 곳은 괴물이 되어버린 악어왕 로서스가 봉인되어 있는 곳일세
					pc.sendPackets(L1GreenMessage.CROCODILE_MENT_8);	// 부디 그대에게 신의 가호가 함께 하길...
				}
				Thread.sleep(4000L);
				moveTrap_2 = _util.spawn(_map, 57, 0); // 2단계 이동트랩
			}
			boolean usercheck = false;
			for (L1PcInstance pc : _pclist) {
				if (!((pc.getX() >= 33551 && pc.getX() <= 33597) && (pc.getY() >= 33184 && pc.getY() <= 33233))) {
					usercheck = true;
					break;
				}
			}
			if(!usercheck)stage = SECOND_STEP;
			break;
		case SECOND_STEP:
			_util.spawn(33576, 33208, (short) _map, 0, 7800114, 10);
			Thread.sleep(10000L);
			if (!running) {
				break;
			}
			// 마지막방
			for (int i = 0; i < 7; i++) {
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_PacketBox.LIGHTING); // 번쩍임
				}
				if (i == 0 || i == 2 || i == 4) {
					_util.spawn(33576, 33208, (short) _map, 0, 7800081, 10);
				}
				for (int j = 0; j < 6; j++) {
					_util.spawn(33576, 33208, (short) _map, 0, _third_MonsterList[CommonUtil.random(_third_MonsterList.length)], 12);
				}
				Thread.sleep(8000L);
				if (!running) {
					break;
				}
			}
			boss_3 = _util.spawn(_map, 53, 0); // 로서스
			stage = LAST_STEP;
			break;
		case LAST_STEP:
			if (boss_3 != null && boss_3.isDead()) {
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_DialogueMessage.SAGE_LOSUS_DIE_MENT); // 게렝 멘트
				}
				Thread.sleep(6000L);
				if (CommonUtil.random(10) < _info.infoUserList.size()) {	//	히든스테이지 인원수에 따라 증가
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(S_PacketBox.LIGHTING); // 번쩍임
					}
					Thread.sleep(2000L);
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(S_PacketBox.LIGHTING); // 번쩍임
					}
					Thread.sleep(4000L);
					event_boss = L1SpawnUtil.spawnCount(boss_3.getX(), boss_3.getY(), _map, 7800104, 0, 0, 1); // 베레스
					stage = EVENT_STEP;
				} else {
					stage = END;
				}
				if (boss_3 != null) {
					boss_3 = null;
				}
			}
			break;
		case EVENT_STEP:
			if (event_boss != null && event_boss.isDead()) {
				event_boss = null;
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_DialogueMessage.SAGE_BELETH_DIE_MENT); // 게렝 멘트
				}
				Thread.sleep(4000L);
				stage = END;
			}
			break;
		case END:
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(S_DialogueMessage.SAGE_END_MENT); // 게렝 멘트
				pc.sendPackets(L1GreenMessage.CROCODILE_MENT_9);	// 수고했네. 그대 덕분에 당분간은 평화롭겠군.
				pc.sendPackets(L1GreenMessage.CROCODILE_MENT_10);	// 이제 그대의 세계로 돌아가도록 하게.
			}
			moveTrap_3 = _util.spawn(_map, 58, 60000); // 법진
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.INDUN_CLOSE_MENT_3); // 이제 시공 여행이 종료됩니다.
			}
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1ServerMessage.sm1259);  // 잠시 후 마을로 이동됩니다.
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
				pc.sendPackets(L1GreenMessage.CROCODILE_MENT_11);	// 이런, 이 곳에 머무를 수 있는 시간이 다 되었네
				pc.sendPackets(L1GreenMessage.CROCODILE_MENT_12);	// 아쉽지만 그대가 있던 곳으로 돌아가도록 하게
			}
			closeTeleport();
			return;
		}
		limitTime--;
		checkPc();
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
				pc.sendPackets(L1GreenMessage.CROCODILE_MENT_13);	// 아아... 나의 욕심 때문에 그대마저 잃게 되었군.
				pc.sendPackets(L1GreenMessage.CROCODILE_MENT_14);	// 그대의 세계로 돌아가 편히 쉬시게.
			}
			Thread.sleep(10000L);
			endIndun();
			return;
		}
		for (int i=0; i<_pclist.size(); i++) {
			if (_pclist.get(i) == null || _pclist.get(i).getMapId() != _map) {
				removePc(_pclist.get(i));
			}
		}
		if (moveTrap_1 != null) {
			for (L1PcInstance pc : _pclist) {
				if (!pc.isDead() && (pc.getX() >= moveTrap_1.getX() - 1 && pc.getX() <= moveTrap_1.getX() + 1) && (pc.getY() >= moveTrap_1.getY() - 1 && pc.getY() <= moveTrap_1.getY() + 1) && pc.getMapId() == moveTrap_1.getMapId()) {
					pc.getTeleport().start(33459, 33373, (short) _map, pc.getMoveState().getHeading(), true);
				}
			}
		}
		
		if (moveTrap_2 != null) {
			for (L1PcInstance pc : _pclist) {
				if (!pc.isDead() && (pc.getX() >= moveTrap_2.getX() - 1 && pc.getX() <= moveTrap_2.getX() + 1) && (pc.getY() >= moveTrap_2.getY() - 1 && pc.getY() <= moveTrap_2.getY() + 1) && pc.getMapId() == moveTrap_2.getMapId()) {
					pc.getTeleport().start(33577, 33207, (short) _map, pc.getMoveState().getHeading(), true);
				}
			}
		}
		
		if (moveTrap_3 != null) {
			for (int i=0; i<_pclist.size(); i++) {
				L1PcInstance pc = _pclist.get(i);
				if (pc == null) {
					continue;
				}
				if (!pc.isDead() && (pc.getX() >= moveTrap_3.getX() - 1 && pc.getX() <= moveTrap_3.getX() + 1) && (pc.getY() >= moveTrap_3.getY() - 1 && pc.getY() <= moveTrap_3.getY() + 1) && pc.getMapId() == moveTrap_3.getMapId()) {
					if (pc.isInParty()) {
						pc.getParty().leaveMember(pc);// 파티탈퇴
					}
					removePc(pc);
					pc.getTeleport().start(33464, 32757, (short) 4, pc.getMoveState().getHeading(), true, true);
				}
			}
		}
	}
	
	private void spawn_monster(){
		try {
			for (int i = 0; i < 35; i++) {// 1번방
				if (i == 0) {
					_util.spawn(33420, 33210, (short) _map, 0, 7800114, 20);
				}
				if (i == 1 || i == 18 || i == 25 || i == 25 || i == 25) {
					_util.spawn(33420, 33210, (short) _map, 0, 7800081, 22);
				}
				_util.spawn(33420, 33210, (short) _map, 0, _First_MonsterList[CommonUtil.random(_First_MonsterList.length)], 22);
			}
			for (int i = 0; i < 35; i++) {// 2번방
				if (i == 0) {
					_util.spawn(33476, 33393, (short) _map, 0, 7800114, 20);
				}
				if (i == 1 || i == 18 || i == 25 || i == 25 || i == 25) {
					_util.spawn(33476, 33393, (short) _map, 0, 7800082, 22);
				}
				_util.spawn(33476, 33393, (short) _map, 0, _Second_MonsterList[CommonUtil.random(_Second_MonsterList.length)], 22);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void spawn_door(){
		_util.doorSpawn(19084, 33390, 33191, (short) _map, false);
		_util.doorSpawn(19084, 33390, 33192, (short) _map, false);
		_util.doorSpawn(19084, 33390, 33193, (short) _map, false);
		_util.doorSpawn(19084, 33390, 33194, (short) _map, false);
		_util.doorSpawn(19084, 33390, 33195, (short) _map, false);
		_util.doorSpawn(19084, 33390, 33196, (short) _map, false);
		_util.doorSpawn(19084, 33390, 33197, (short) _map, false);
		_util.doorSpawn(19084, 33390, 33198, (short) _map, false);
	}
	
	@Override
	protected void startIndun() {
		System.out.println("■■■■■■ Crocodile Island's secret (in-dungeon) start map number " + _map + " room number " + _info.room_id + " ■■■■■■");
		GeneralThreadPool.getInstance().schedule(this, 4000);
	}

	@Override
	protected void endIndun() {
		if (running) {
			running = false;
			System.out.println("■■■■■■ Crocodile Island's secret (in-dungeon) end map number " + _map + " room number " + _info.room_id + " ■■■■■■");
			dispose();
		}
	}
	
	@Override
	protected void dispose() {
		super.dispose();
	}
}


package l1j.server.IndunSystem.indun.action;

import java.util.ArrayList;

import l1j.server.IndunSystem.indun.IndunHandler;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1NotificationMessege;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.indun.S_ArenaPlayStatusNoti;
import l1j.server.server.serverpackets.message.S_DialogueMessage;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;

/**
 * 아우라키아
 * @author LinOffice
 */
public class Aurakia extends IndunHandler {
	private int stage						= 1;
	private static final int WAIT_STEP		= 1;
	private static final int FIRST_STEP		= 2;
	private static final int SECOND_STEP	= 3;
	private static final int THIRD_STEP		= 4;
	private static final int FOURTH_STEP	= 5;
	private static final int END			= 6;
	
	private static final int FIRST_CNT		= 2;// 최초 스폰 쿨(바퀴당 1)
	private static final int GATE_CNT		= 4;// 마지막 스폰 쿨(바퀴당 1)
	
	private static final int PURIFICATION_VALUE_1	= 30;
	private static final int PURIFICATION_VALUE_2	= PURIFICATION_VALUE_1 + 30;
	private static final int PURIFICATION_VALUE_3	= PURIFICATION_VALUE_2 + 15;
	private static final int PURIFICATION_VALUE_4	= PURIFICATION_VALUE_3 + 10;
	
	private ArrayList<L1NpcInstance> firstMonsterList;
	private L1NpcInstance darkWatch, darkTransWatch, arrestedAurakia, monsterGate_1, monsterGate_2, monsterGate_3, monsterGate_4;
	private L1DoorInstance enterDoor, eyeGate, stone_1, stone_2, stone_3, stone_4;
	private int spawnCnt, gateNumber, arrestedAttackCnt, arrestedLevel, closeDoorTime;
	
	public void increaseArrestedAttackCount(){
		arrestedAttackCnt++;
	}
	
	public Aurakia(IndunInfo info, short mapId){
		super(info, mapId);
		firstMonsterList = new ArrayList<L1NpcInstance>();
	}

	@Override
	protected void startSpawn() throws Exception {
		spawnDoor();
		spawnEgg();
	}

	@Override
	protected void process() throws Exception {
		switch(stage){
		case WAIT_STEP:
			Thread.sleep(10000L);
			_util.sendPacket(_pclist, new S_SceneNoti(true, System.currentTimeMillis() / 1000, 73600001, 32851, 32826), true);// 흔들림 + 안개
			Thread.sleep(5000L);
			_util.sendPacket(_pclist, S_DialogueMessage.AURAKIA_START_MENT, false);// 멘트
			Thread.sleep(5000L);
			GeneralThreadPool.getInstance().execute(new TimerCheck());// 타이머 시작
			_util.sendPacket(_pclist, new S_ArenaPlayStatusNoti(_info, null), true);// 인던 시작
			eyeGate.open();// 문 오픈
			enterDoor.open();// 문 오픈
			closeDoorTime = limitTime - 60;
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1NotificationMessege.AURAKIA_DARK_MENT_1);// 누가 우리의 의식을 방해하느냐
				pc.sendPackets(L1NotificationMessege.AURAKIA_DARK_MENT_2);// 우리를 방해할 자격을 갖췄는지 확인해보지
			}
			stage				= FIRST_STEP;
			break;
		case FIRST_STEP:
			firstMonsterCheck();
			if (spawnCnt >= FIRST_CNT && firstMonsterList != null && firstMonsterList.isEmpty()) {
				stage				= SECOND_STEP;
				spawnCnt			= 0;
				firstMonsterList	= null;
				Thread.sleep(5000L);
				_util.sendPacket(_pclist, L1NotificationMessege.AURAKIA_DARK_MENT_3, false);// 어둠감시관이여 저녀석들을 처리하거라
				darkWatch			= _util.spawn(_map, 104, limitTime * 1000);// 어둠 감시관 출현
				_util.sendPacket(_pclist, new S_SceneNoti(true, System.currentTimeMillis() / 1000, 73600000, darkWatch.getX(), darkWatch.getY()), true);
				Thread.sleep(3000L);
				if (darkWatch != null) {
					_util.sendPacket(_pclist, S_SceneNoti.DISABLE_73600000, false);
				}
			}
			break;
		case SECOND_STEP:
			if (darkWatch != null && darkWatch.isDead()) {// 어둠 감시관 처치
				stage				= THIRD_STEP;
				darkWatch			= null;
				Thread.sleep(5000L);
				_util.sendPacket(_pclist, L1NotificationMessege.AURAKIA_DARK_MENT_5, false);// 어둠감시관의 본모습을 보여주마
				darkTransWatch		= _util.spawn(_map, 105, limitTime * 1000);// 변신한 어둠 감시관 출현
				S_SceneNoti scene	= new S_SceneNoti(true, System.currentTimeMillis() / 1000, 73600000, darkTransWatch.getX(), darkTransWatch.getY());// 스폰이팩트
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(scene);
					pc.sendPackets(L1NotificationMessege.AURAKIA_DARK_MENT_6);// 내 본모습을 보이게 될줄은 몰랐군
				}
				scene.clear();
				scene = null;
				if (darkTransWatch != null) {
					_util.sendPacket(_pclist, S_SceneNoti.DISABLE_73600000, false);
				}
			}
			break;
		case THIRD_STEP:
			if (darkTransWatch != null && darkTransWatch.isDead()) {// 변신한 어둠 감시관 처치
				darkTransWatch = null;
				stoneEnable(true);// 석상 활성화
				arrestedAurakia	= _util.spawn(_map, 106, limitTime * 1000);// 구속된 아우라키아 출현
				_util.sendPacket(_pclist, new S_SceneNoti(true, System.currentTimeMillis() / 1000, 73600003, 0, 0), true);// 구속구 이미지 출력
				_util.sendPacket(_pclist, new S_SceneNoti(true, System.currentTimeMillis() / 1000, 73600004, arrestedAurakia.getX() + 5, arrestedAurakia.getY() - 5), true);// 구속구 이미지 출력
				Thread.sleep(2000L);
				_util.sendPacket(_pclist, S_SceneNoti.DISABLE_73600005, false);
				_util.sendPacket(_pclist, S_SceneNoti.DISABLE_73600006, false);
				_util.sendPacket(_pclist, S_SceneNoti.DISABLE_73600007, false);
				spawnCnt		= 0;
				stage			= FOURTH_STEP;
			}
			break;
		case FOURTH_STEP:
			purificationValueCheck();
			if (arrestedAurakia != null && arrestedAurakia.isDead()) {// 아우라키아 구속구 파괴
				stage			= END;
				Thread.sleep(2000L);
				stoneEnable(false);// 석상 활성화
				monsterGateOff();// 게이트 삭제
				purificationComplete();
			}
			break;
		case END:
			Thread.sleep(10000L);
			_util.sendPacket(_pclist, L1NotificationMessege.AURAKIA_CLEAR_MENT, false);// 안돼... 아우라키아의 구속이 풀리다니...
			Thread.sleep(10000L);
			_util.sendPacket(_pclist, L1ServerMessage.sm1259, false);// 잠시 후 마을로 이동됩니다.
			Thread.sleep(10000L);
			closeTeleport();
			break;
		default:break;
		}
	}

	@Override
	protected void timerCheck() throws Exception {
		if (limitTime <= 0) {
			timeOutFail();
			return;
		}
		if (closeDoorTime > 0 && closeDoorTime == limitTime) {
			enterDoor.close();
		}
		limitTime--;
		if (((stage == FIRST_STEP && spawnCnt < FIRST_CNT) || (stage == FOURTH_STEP && spawnCnt < GATE_CNT)) && limitTime % 10 == 0) {
			monsterGateAction();
		}
		checkPc();
	}
	
	private void checkPc() throws Exception {
		if (_pclist.isEmpty()) {
			endIndun();
			return;
		}
		int diechck = 0;
		L1PcInstance pc = null;
		for (int i=0; i<_pclist.size(); i++) {
			pc = _pclist.get(i);
			if (pc == null || pc.getMapId() != _map) {
				removePc(pc);
			} else if (pc.isDead()) {
				diechck++;
			}
		}
		if (_pclist.size() <= diechck) {
			_util.sendPacket(_pclist, L1NotificationMessege.AURAKIA_FAIL_MENT, false);// 역시 너희들은 아직 나의 상대가 되지 못하는구나
			endIndun();
			return;
		}
	}
	
	private void firstMonsterCheck(){
		if (firstMonsterList == null || firstMonsterList.isEmpty()) {
			return;
		}
		for (int i=0; i<firstMonsterList.size(); i++) {
			L1NpcInstance npc = firstMonsterList.get(i);
			if (npc != null && npc.isDead()) {
				firstMonsterList.remove(npc);
			}
		}
	}
	
	/**
	 * 정화 횟수 검사
	 */
	private void purificationValueCheck(){
		if (arrestedLevel < 1 && arrestedAttackCnt >= PURIFICATION_VALUE_1) {
			purificationAction(73600005);
		} else if (arrestedLevel < 2 && arrestedAttackCnt >= PURIFICATION_VALUE_2) {
			purificationAction(73600006);
		} else if (arrestedLevel < 3 && arrestedAttackCnt >= PURIFICATION_VALUE_3) {
			purificationAction(73600007);
		} else if (arrestedLevel < 4 && arrestedAttackCnt >= PURIFICATION_VALUE_4) {
			purificationAction(0);
		}
	}
	
	/**
	 * 정화 처리
	 * @param script_number
	 */
	private void purificationAction(int script_number){
		if (script_number > 0) {
			arrestedLevel++;
			_util.sendPacket(_pclist, new S_SceneNoti(true, System.currentTimeMillis() / 1000, script_number, arrestedAurakia.getX() + 5, arrestedAurakia.getY() - 5), true);// 구속구 이미지 출력
			arrestedAurakia.broadcastPacket(new S_DoActionGFX(arrestedAurakia.getId(), ActionCodes.ACTION_SkillBuff), true);
		} else {
			arrestedLevel++;
			arrestedAurakia.broadcastPacket(new S_DoActionGFX(arrestedAurakia.getId(), ActionCodes.ACTION_SkillBuff), true);
			arrestedAurakia.setCurrentHp(0);
			arrestedAurakia.setDead(true);
		}
	}
	
	private void monsterGateAction(){
		L1NpcInstance currentGate			= null;
		L1NpcInstance nextGate				= null;
		ArrayList<L1NpcInstance> monList	= null;
		switch(gateNumber){
		case 0:// 1시
			currentGate = monsterGate_1;
			nextGate = monsterGate_2;
			monList = _util.spawnList(_map, 100, 0);
			break;
		case 1:// 4시
			currentGate = monsterGate_2;
			nextGate = monsterGate_3;
			monList = _util.spawnList(_map, 101, 0);
			break;
		case 2:// 7시
			currentGate = monsterGate_3;
			nextGate = monsterGate_4;
			monList = _util.spawnList(_map, 102, 0);
			break;
		case 3:// 10시
			currentGate = monsterGate_4;
			nextGate = monsterGate_1;
			monList = _util.spawnList(_map, 103, 0);
			break;
		}
		// 최초 스폰되는 몬스터들을 모두 처치햇는지 체크해야할 리스트 
		if (stage == FIRST_STEP && firstMonsterList != null) {
			firstMonsterList.addAll(monList);
		}
		currentGate.setActionStatus(ActionCodes.ACTION_Die);
		currentGate.broadcastPacket(new S_DoActionGFX(currentGate.getId(), ActionCodes.ACTION_Die), true);
		
		// 바퀴당 쿨 1 추가
		if (gateNumber >= 3) {
			gateNumber = 0;
			spawnCnt++;
		} else {
			gateNumber++;
		}
		if ((stage == FIRST_STEP && spawnCnt < FIRST_CNT) || (stage == FOURTH_STEP && spawnCnt < GATE_CNT)) {
			nextGate.setActionStatus(0);
			nextGate.broadcastPacket(new S_NPCObject(nextGate), true);
		}
	}
	
	/**
	 * 석상 활성화
	 * @param enable
	 */
	private void stoneEnable(boolean enable){
		if (enable) {
			stone_1.open();
			stone_2.open();
			stone_3.open();
			stone_4.open();
		} else {
			stone_1.close();
			stone_2.close();
			stone_3.close();
			stone_4.close();
		}
	}
	
	private void monsterGateOff(){
		monsterGate_1.setActionStatus(ActionCodes.ACTION_Die);
		monsterGate_2.setActionStatus(ActionCodes.ACTION_Die);
		monsterGate_3.setActionStatus(ActionCodes.ACTION_Die);
		monsterGate_4.setActionStatus(ActionCodes.ACTION_Die);
		monsterGate_1.broadcastPacket(new S_DoActionGFX(monsterGate_1.getId(), ActionCodes.ACTION_Die), true);
		monsterGate_2.broadcastPacket(new S_DoActionGFX(monsterGate_2.getId(), ActionCodes.ACTION_Die), true);
		monsterGate_3.broadcastPacket(new S_DoActionGFX(monsterGate_3.getId(), ActionCodes.ACTION_Die), true);
		monsterGate_4.broadcastPacket(new S_DoActionGFX(monsterGate_4.getId(), ActionCodes.ACTION_Die), true);
	}
	
	private void spawnDoor(){
		enterDoor		= _util.doorSpawnRange(20400, 32816, 32829, (short) _map, 5, false);// 첫번째 문
		eyeGate			= _util.doorSpawn(20389, 32812, 32830, (short) _map, false);// 눈알 게이트
		monsterGate_1	= _util.spawn(_map, 107, limitTime * 1000);// 몬스터 게이트 1시
		monsterGate_2	= _util.spawn(_map, 108, limitTime * 1000);// 몬스터 게이트 4시
		monsterGate_3	= _util.spawn(_map, 109, limitTime * 1000);// 몬스터 게이트 7시
		monsterGate_4	= _util.spawn(_map, 110, limitTime * 1000);// 몬스터 게이트 10시
		stone_1			= _util.doorSpawn(20348, 32862, 32817, (short) _map, false);// 석상 12시
		stone_2			= _util.doorSpawn(20430, 32859, 32835, (short) _map, false);// 석상 3시
		stone_3			= _util.doorSpawn(20344, 32840, 32837, (short) _map, false);// 석상 6시
		stone_4			= _util.doorSpawn(20426, 32842, 32817, (short) _map, false);// 석상 9시
	}
	
	private void spawnEgg(){
		_util.spawn(_map, 111, limitTime * 1000);
	}
	
	/**
	 * 시간 초과로 인한 실패
	 * @throws Exception
	 */
	private void timeOutFail() throws Exception {
		for (L1PcInstance pc : _pclist) {
			pc.sendPackets(L1NotificationMessege.AURAKIA_FAIL_MENT);// 역시 너희들은 아직 나의 상대가 되지 못하는구나
			Thread.sleep(5000L);
			pc.sendPackets(L1ServerMessage.sm1259);// 잠시 후 마을로 이동됩니다.
			Thread.sleep(5000L);
		}
		closeTeleport();
	}
	
	/**
	 * 정화 완료
	 */
	private void purificationComplete(){
		S_SceneNoti scene	= new S_SceneNoti(true, System.currentTimeMillis() / 1000, 73600002, 0, 0);// 맵변화
		for (L1PcInstance pc : _pclist) {
			if (pc == null) {
				continue;
			}
			pc.sendPackets(S_SceneNoti.DISABLE_73600003);
			pc.sendPackets(S_SceneNoti.DISABLE_73600004);
			pc.sendPackets(S_SceneNoti.DISABLE_73600005);
			pc.sendPackets(S_SceneNoti.DISABLE_73600006);
			pc.sendPackets(S_SceneNoti.DISABLE_73600007);
			pc.sendPackets(scene);
			pc.sendPackets(S_SceneNoti.DISABLE_73600001);
			pc.sendPackets(S_DialogueMessage.AURAKIA_END_MENT);// 멘트
			pc.sendPackets(S_AvailableSpellNoti.ARROW_OF_AURAKIA_OFF);// 스킬 제거
			if (pc.isDead() || pc.getX() < 32817) {// 보상 제외
				continue;
			}
			pc.einGetExcute(300);// 아인하사드 보상
			if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(420120), 1) != L1Inventory.OK) continue;
			pc.sendPackets(new S_ServerMessage(403, pc.getInventory().storeItem(420120, 1).getItem().getDesc()), true);// 아우라키아의 선물
			if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(420121), 1) != L1Inventory.OK) continue;
			pc.sendPackets(new S_ServerMessage(403, pc.getInventory().storeItem(420121, 1).getItem().getDesc()), true);// 정화의 구슬 파편
		}
		arrestedAurakia.broadcastPacket(new S_Effect(arrestedAurakia.getId(), 20364), true);
		arrestedAurakia.deleteMe();
		
		// 메모리 정리
		arrestedAurakia = null;
		scene.clear();
		scene	= null;
	}
	
	@Override
	protected void startIndun() {
		System.out.println("■■■■■■ Auracia Purification (Indonement) start map number " + _map + " room number " + _info.room_id + " ■■■■■■");
		GeneralThreadPool.getInstance().schedule(this, 4000);
	}

	@Override
	protected void endIndun() {
		if (running) {
			running = false;
			System.out.println("■■■■■■ Auraquia purification (in-dungeon) end map number " + _map + " room number " + _info.room_id + " ■■■■■■");
			dispose();
		}
	}
	
	@Override
	protected void dispose() {
		if (firstMonsterList != null) {
			firstMonsterList.clear();
		}
		firstMonsterList	= null;
		enterDoor = eyeGate = stone_1 = stone_2 = stone_3 = stone_4 = null;
		darkWatch = darkTransWatch = arrestedAurakia = monsterGate_1 = monsterGate_2 = monsterGate_3 = monsterGate_4 = null;
		super.dispose();
	}
}


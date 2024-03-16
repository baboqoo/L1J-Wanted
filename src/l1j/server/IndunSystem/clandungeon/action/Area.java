package l1j.server.IndunSystem.clandungeon.action;

import java.util.ArrayList;

import javolution.util.FastTable;
import l1j.server.IndunSystem.clandungeon.ClanDungeonHandler;
import l1j.server.IndunSystem.clandungeon.ClanDungeonType;
import l1j.server.IndunSystem.clandungeon.ClanDungeonUtill;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 혈맹 집결지
 * @author LinOffice
 */
public class Area extends ClanDungeonHandler {
	private int limitTime = 3600;// 60분
	
	private int stage						= 0;
	private static final int STEP_1_READY	= 0;
	private static final int STEP_1_START	= 1;
	private static final int STEP_1_BOSS	= 2;
	private static final int STEP_1_TRAP	= 3;
	private static final int STEP_2_READY	= 4;
	private static final int STEP_2_START	= 5;
	private static final int STEP_2_BOSS	= 6;
	private static final int STEP_2_TRAP	= 7;
	private static final int STEP_3_READY	= 8;
	private static final int STEP_3_START	= 9;
	private static final int STEP_3_BOSS	= 10;
	private static final int STEP_3_TRAP	= 11;
	private static final int STEP_4_READY	= 12;
	private static final int STEP_4_START	= 13;
	private static final int STEP_4_BOSS	= 14;
	private static final int STEP_4_TRAP	= 15;
	private static final int STEP_5_BOSS	= 16;
	private static final int END			= 17;
	
	private L1NpcInstance _trap_1, _trap_2, _trap_3, _trap_4;
	private final FastTable<L1NpcInstance> _step_monster;
	private final FastTable<L1NpcInstance> _step_tonado;
	
	public Area(short mapId, String clanName, ClanDungeonType type){
		super(mapId, clanName, type);
		_step_monster	= new FastTable<L1NpcInstance>();
		_step_tonado	= new FastTable<L1NpcInstance>();
	}
	
	@Override
	protected void startSetting(L1PcInstance pc) {
		System.out.println("■■■■■■■■■■ Clan Gathering Point Start ■■■■■■■■■■ MAP - " + _map + " Clan: " + _clanName);
		L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 5, 20902, 0, 180000, _map);	//	혈맹 집결지 포탈
	}

	@Override
	protected void startSpawn() {
		ClanDungeonUtill.spawn(32840, 32958, _map, _clantable.getStageList(3, 30));	// 솜리
		ClanDungeonUtill.doorSpawn(32810, 32829, (short) _map, 9001, false, 19765);	// 1지역
		ClanDungeonUtill.doorSpawn(32841, 32827, (short) _map, 9001, false, 19765);	// 1지역
		ClanDungeonUtill.doorSpawn(32893, 32876, (short) _map, 9002, true, 19787);		// 2지역
		ClanDungeonUtill.doorSpawn(32892, 32905, (short) _map, 9002, true, 19787);		// 2지역
		ClanDungeonUtill.doorSpawn(32862, 32966, (short) _map, 9003, false, 19791);	// 3지역
		ClanDungeonUtill.doorSpawn(32825, 32967, (short) _map, 9003, false, 19791);	// 3지역
	}
	
	@Override
	protected void timerCheck() throws Exception {
		if (limitTime > 0) {
			limitTime--;
		}
		//"The clan gathering point usage time has 10 minutes remaining."
		//"The clan gathering point usage time has 10 seconds remaining."
		if (limitTime == 3400) {
			ClanDungeonUtill.sendItem(_map, 6015);// 맹세의 징표
		} else if (limitTime == 600) {
			//ClanDungeonUtill.ment("혈맹 집결지 이용 시간이 10분 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(730) + "10 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(731), _map);
		} else if (limitTime == 300) {
			//ClanDungeonUtill.ment("혈맹 집결지 이용 시간이 5분 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(730) + "5 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(731), _map);
		} else if (limitTime == 60) {
			//ClanDungeonUtill.ment("혈맹 집결지 이용 시간이 1분 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(730) + "1 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(731), _map);
		} else if (limitTime == 10) {
			//ClanDungeonUtill.ment("혈맹 집결지 이용 시간이 10초 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(730) + "10 " + S_SystemMessage.getRefText(719) + S_SystemMessage.getRefText(731), _map);
		}
		
		if (limitTime < 3400) {// 인원 체크
			ArrayList<L1PcInstance> list = ClanDungeonUtill.PcStageCK(_map);
			if (list.isEmpty()) {
				running = false;
			}
			list.clear();
		}
		
		if (limitTime <= 0) {
			RETURN_TEL();
			running = false;
		}
	}

	@Override
	protected void process() throws Exception {
		switch(stage){
		case STEP_1_READY:
			stageCount++;
			if (stageCount == 20) {
				//ClanDungeonUtill.ment("아덴 용사 집결중...", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(732), _map);
			} else if (stageCount == 28) {
				//ClanDungeonUtill.ment("암흑룡의 사신이 군대를 이끌고 침공하려고 하는 움직임이 포착 되었습니다...", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(733), _map);
			} else if (stageCount == 36) {
				//ClanDungeonUtill.ment("아덴의 평화를 위해 침공 당하기 전 암흑룡의 사신을 찾아내 제거 하려고 합니다...", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(734), _map);
			} else if (stageCount == 44) {
				//ClanDungeonUtill.ment("총 4개 지역을 돌파 해야 암흑룡의 사신에게 도달할 수 있습니다...", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(701), _map);
			} else if (stageCount == 52) {
				//ClanDungeonUtill.ment("실력... 운... 모든 것들이... 갖춰줘야...", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(650), _map);
			} else if (stageCount == 60) {
				//ClanDungeonUtill.ment("용사들의 여정에 행운이 함께 하길...", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(550), _map);
			} else if (stageCount == 68) {
				//ClanDungeonUtill.ment("1지역 활성화 10초 남았습니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(551), _map);
			} else if (stageCount == 76) {
				//ClanDungeonUtill.ment("곧 몬스터가 습격합니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(552), _map);
			} else if (stageCount == 80) {
				ClanDungeonUtill.sendEffect(10, _map);//	어두워짐
			} else if (stageCount >= 82) {// 1시작
				setStepMonster(ClanDungeonUtill.spawn(32782, 32820, _map, _clantable.getStageList(3, 1), false, 27));	// 1지역 몬스터 스폰
				stageCount = 0;
				stage = STEP_1_START;
			}
			break;
		case STEP_1_START:
			if (stageMonsterCheck()) {
				ClanDungeonUtill.sendEffect(21, _map);//	화면 이팩트
				setStepMonster(ClanDungeonUtill.spawn(32791, 32811, _map, _clantable.getStageList(3, 20), true, 0));	// 아투바킹 스폰
				stage = STEP_1_BOSS;
			}
			break;
		case STEP_1_BOSS:
			if (stageMonsterCheck()) {
				ClanDungeonUtill.sendExp(1, _map);
				_trap_1 = ClanDungeonUtill.spawn(32804, 32826, _map, _clantable.getStageList(3, 10));	// 1지역 트랩스폰
				stage = STEP_1_TRAP;
			}
			break;
		case STEP_1_TRAP:
			if (trapCheck(_trap_1)) {
				if (CommonUtil.random(100) < 80) {
					openDoor(9001, _map, true);
					stage = STEP_2_READY;
				} else {
					openDoor(9001, _map, false);
					stage = END;
				}
			}
			break;
		case STEP_2_READY:
			stageCount++;
			if (stageCount == 20) {
				//ClanDungeonUtill.ment("2지역 활성화 10초 남았습니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(505), _map);
			} else if (stageCount == 28) {
				//ClanDungeonUtill.ment("곧 몬스터가 습격합니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(552), _map);
			} else if (stageCount == 32) {
				ClanDungeonUtill.sendEffect(10, _map);//	어두워짐
			} else if (stageCount >= 34) {// 2시작
				setStepMonster(ClanDungeonUtill.spawn(32892, 32837, _map, _clantable.getStageList(3, 2), false, 28));	// 2지역 몬스터 스폰
				ClanDungeonUtill.spawn(32905, 32836, _map, _clantable.getStageList(3, 21), false);	// 아이스 골렘 킹 얼음 스폰
				stageCount = 0;
				stage = STEP_2_START;
			}
			break;
		case STEP_2_START:
			if (stageMonsterCheck()) {
				ClanDungeonUtill.sendEffect(19, _map);//	화면 이팩트
				ClanDungeonUtill.deleteNpc(_map, 20914);
				setStepMonster(ClanDungeonUtill.spawn(32905, 32836, _map, _clantable.getStageList(3, 22), true, 0));	// 아이스 골렘 킹 스폰
				stage = STEP_2_BOSS;
			}
			break;
		case STEP_2_BOSS:
			if (stageMonsterCheck()) {
				ClanDungeonUtill.sendExp(2, _map);
				_trap_2 = ClanDungeonUtill.spawn(32899, 32868, _map, _clantable.getStageList(3, 11));	// 2지역 트랩스폰
				stage = STEP_2_TRAP;
			}
			break;
		case STEP_2_TRAP:
			if (trapCheck(_trap_2)) {
				if (CommonUtil.random(100) < 60) {
					openDoor(9002, _map, true);
					stage = STEP_3_READY;
				} else {
					openDoor(9002, _map, false);
					stage = END;
				}
			}
			break;
		case STEP_3_READY:
			stageCount++;
			if (stageCount == 20) {
				//ClanDungeonUtill.ment("3지역 활성화 10초 남았습니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(478), _map);
			} else if (stageCount == 28) {
				//ClanDungeonUtill.ment("곧 몬스터가 습격합니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(552), _map);
			} else if (stageCount == 32) {
				ClanDungeonUtill.sendEffect(10, _map);//	어두워짐
			} else if (stageCount >= 34) {// 3시작
				setStepMonster(ClanDungeonUtill.spawn(32897, 32955, _map, _clantable.getStageList(3, 3), false, 28));	// 3지역 몬스터 스폰
				stageCount = 0;
				stage = STEP_3_START;
			}
			break;
		case STEP_3_START:
			if (stageMonsterCheck()) {
				ClanDungeonUtill.sendEffect(21, _map);//	화면 이팩트
				setStepMonster(ClanDungeonUtill.spawn(32882, 32957, _map, _clantable.getStageList(3, 23), true, 0));	// 지옥의 감시자 스폰
				stage = STEP_3_BOSS;
			}
			break;
		case STEP_3_BOSS:
			if (stageMonsterCheck()) {
				ClanDungeonUtill.sendExp(3, _map);
				_trap_3 = ClanDungeonUtill.spawn(32868, 32961, _map, _clantable.getStageList(3, 12));	// 3지역 트랩스폰
				stage = STEP_3_TRAP;
			}
			break;
		case STEP_3_TRAP:
			if (trapCheck(_trap_3)) {
				if (CommonUtil.random(100) < 40) {
					openDoor(9003, _map, true);
					stage = STEP_4_READY;
				} else {
					openDoor(9003, _map, false);
					stage = END;
				}
			}
			break;
		case STEP_4_READY:
			stageCount++;
			if (stageCount == 20) {
				//ClanDungeonUtill.ment("4지역 활성화 10초 남았습니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(475), _map);
			} else if (stageCount == 28) {
				//ClanDungeonUtill.ment("곧 몬스터가 습격합니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(552), _map);
			} else if (stageCount == 32) {
				ClanDungeonUtill.sendEffect(10, _map);//	어두워짐
			} else if (stageCount >= 34) {// 4시작
				setStepMonster(ClanDungeonUtill.spawn(32786, 32964, _map, _clantable.getStageList(3, 4), false, 28));	// 4지역 몬스터 스폰
				stageCount = 0;
				stage = STEP_4_START;
			}
			break;
		case STEP_4_START:
			if (stageMonsterCheck()) {
				ClanDungeonUtill.sendEffect(20, _map);//	화면 이팩트
				setStepMonster(ClanDungeonUtill.spawn(32786, 32964, _map, _clantable.getStageList(3, 24), true, 0));	// 스텔라리아 스폰
				stage = STEP_4_BOSS;
			}
			break;
		case STEP_4_BOSS:
			if (stageMonsterCheck()) {
				ClanDungeonUtill.sendExp(4, _map);
				_trap_4 = ClanDungeonUtill.spawn(32786, 32964, _map, _clantable.getStageList(3, 13));	// 4지역 트랩스폰
				stage = STEP_4_TRAP;
			}
			break;
		case STEP_4_TRAP:
			if (trapCheck(_trap_4)) {
				_step_tonado.addAll(ClanDungeonUtill.spawn(32786, 32964, _map, _clantable.getStageList(3, 14), false, 8));// 모래 폭풍 스폰
				setStepMonster(ClanDungeonUtill.spawn(32759, 32891, _map, _clantable.getStageList(3, 25), true, 0));	// 암흑룡의 사신 스폰
				stage = STEP_5_BOSS;
			}
			break;
		case STEP_5_BOSS:
			trap_tonadoCheck();
			if (stageMonsterCheck()) {
				ClanDungeonUtill.sendExp(5, _map);
				stage = END;
			}
			break;
		case END:
			stageCount++;
			if (stageCount == 10) {
				//ClanDungeonUtill.ment("모든 악당을 물리쳤습니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(476), _map);
			} else if (stageCount == 20) {
				//ClanDungeonUtill.ment("귀환하세요. 60초뒤에 강제 귀환되요.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(469) + "60 " + S_SystemMessage.getRefText(470), _map);
			} else if (stageCount == 50) {
				//ClanDungeonUtill.ment("귀환하세요. 30초뒤에 강제 귀환되요.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(469) + "30 " + S_SystemMessage.getRefText(470), _map);
			} else if (stageCount == 70) {
				//ClanDungeonUtill.ment("귀환하세요. 10초뒤에 강제 귀환되요.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(469) + "10 " + S_SystemMessage.getRefText(470), _map);
			} else if (stageCount == 80) {
				RETURN_TEL();
			}
			break;
		default:break;
		}
	}
	
	private void openDoor(int doorId, int mapid, boolean result) {
		L1DoorInstance door = null;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(mapid).values()) {
			if (obj instanceof L1DoorInstance) {
				door = (L1DoorInstance) obj;
				if (door.getDoorId() == doorId) {
					if (result) {
						door.setCurrentHp(0);
						door.setDead(true);
						door.isPassibleDoor(true);//하딘 시스템
						door.setActionStatus(ActionCodes.ACTION_TowerCrack1);
						door.setOpenStatus(ActionCodes.ACTION_TowerCrack1);
						door.getMap().setPassable(door.getLocation(), true);
						door.broadcastPacket(new S_DoActionGFX(door.getId(), ActionCodes.ACTION_TowerCrack1), true);
						door.setPassable(L1DoorInstance.PASS);
						door.sendDoorPacket(null);
					} else {
						door.broadcastPacket(new S_DoActionGFX(door.getId(), ActionCodes.ACTION_TowerCrack3), true);
					}
				}
			}
		}
	}
	
	int trapPcCheck = 0;
	private boolean trapCheck(L1NpcInstance npc){
		boolean result = false;
		if (npc == null) {
			return false;
		}
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 1)) {
			if (npc.getX() == pc.getX() && npc.getY() == pc.getY() && pc.getMapId() == _map) {
				trapPcCheck++;
				break;
			}
		}
		if (trapPcCheck >= 5) {
			trapPcCheck = 0;
			result=true;
		}
		return result;
	}
	
	private void trap_tonadoCheck(){
		if (_step_tonado == null || _step_tonado.isEmpty()) {
			return;
		}
		for (L1NpcInstance trap : _step_tonado) {
			if (trap == null || trap.isDead() || trap._destroyed) {
				continue;
			}
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(trap, 2)) {
				if (pc == null) {
					continue;
				}
				if (pc.getLocation().getTileLineDistance(trap.getLocation()) < 2 && !pc.isDead()) {
					pc.getTeleport().start(32737, 32896, (short)_map, pc.getMoveState().getHeading(), true);
					trap.setCurrentHp(0);
					trap.setDead(true);
					trap.setActionStatus(ActionCodes.ACTION_Die);
					Broadcaster.broadcastPacket(trap, new S_DoActionGFX(trap.getId(), ActionCodes.ACTION_Die), true);
					break;
				}
			}
		}
	}
	
	private void setStepMonster(FastTable<L1NpcInstance> list){
		if (list == null || list.isEmpty()) {
			return;
		}
		_step_monster.clear();
		_step_monster.addAll(list);
	}
	
	private boolean stageMonsterCheck(){
		if (_step_monster == null) {
			return false;
		}
		int dieCount = 0;
		for (L1NpcInstance mon : _step_monster) {
			if (mon.isDead()) {
				dieCount++;
			}
		}
		return _step_monster.size() == dieCount;
	}
	
	@Override
	public void startRaid() {
		GeneralThreadPool.getInstance().schedule(this, 5000);
	}
	
	@Override
	protected void endRaid() {
		System.out.println("■■■■■■■■■■ Clan gathering point ends ■■■■■■■■■■ MAP - " + _map + " Clan: " + _clanName);
		dispose();
	}
	
	@Override
	protected void dispose() {
		super.dispose();
		_step_monster.clear();
		_step_tonado.clear();
	}

}


package l1j.server.IndunSystem.ruun;

import java.util.ArrayList;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.CommonUtil;

/**
 * 루운성 던전
 * @author LinOffice
 */
public class Ruun implements Runnable {
	private static class newInstance {
		public static final Ruun INSTANCE = new Ruun();
	}
	public static Ruun getInstance(){
		return newInstance.INSTANCE;
	}
	
	public static final short START_MAP			= 4000;// 루운 광장
	public static final short CASTLE_MAP		= 4001;// 루운 성
	public static final short CASTLE_INNER_MAP	= 4002;// 루운 내성 맵번호
	public static final short CROWN_MAP			= 4003;// 군주의 방 맵번호
	private static final int FINAL_ROUND_TIME	= 600;// 10분
	private static final int[][] BACK_LOC		= { {32801, 32767}, {32802, 32765}, {32800, 32765}, {32799, 32767}, {32801, 32769} };
	private static final String RESET_STRING	= "$35493";// 루운성 내부에 강력한 힘이 느껴집니다. 2분 뒤에 루운 광장으로 이동 됩니다.
	
	public static boolean isRuunMap(short mapId){
		return mapId >= START_MAP && mapId <= CROWN_MAP;
	}
	
	private FastTable<L1NpcInstance> _npcList_1;// 인형 술사
	private FastTable<L1NpcInstance> _npcList_2;// 정찰 단장
	private FastTable<L1NpcInstance> _npcList_3;// 유마 박사
	private FastTable<L1NpcInstance> _npcList_4;// 다중 가면
	private FastTable<L1NpcInstance> _npcList_5;// 검귀
	private int _limitTime;
	
	private final RuunUtil ruunUtil;
	
	private Ruun() {
		ruunUtil = RuunUtil.getInstance();
	}
	
	private boolean _isRun;
	public boolean isRun(){
		return _isRun;
	}
	public void setRun(boolean run){
		_isRun = run;
	}
	
	@Override
	public void run() {
		try {
			ruunUtil.timerSend(CASTLE_INNER_MAP, FINAL_ROUND_TIME);// 10분 제한시간
			spawnMonster(RuunRound.PHANTOM_SWORD);
			while(_isRun){// 체크 시작
				try {
					timeCheck();
					if (monsterCheck(RuunRound.PHANTOM_SWORD)) {// 공략 성공
						ruunUtil.mapSystemMsgSend(CASTLE_INNER_MAP, RESET_STRING);// 루운성 내부에 강력한 힘이 느껴집니다. 2분 뒤에 루운 광장으로 이동 됩니다.
						ruunUtil.timerSend(CASTLE_INNER_MAP, 0);// 제한시간 삭제
						end();
						Thread.sleep(120000L);
						returnTeleport();
						break;
					}
					pcCheck();
				} catch(Exception e) {
					_isRun = false;
					e.printStackTrace();
				} finally {
					Thread.sleep(1000L);// 1초마다 체크
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void timeCheck(){// 제한시간 체크
		if (--_limitTime <= 0) {// 제한시간 종료
			end();
			returnTeleport();
		}
	}
	
	private void pcCheck(){
		ArrayList<L1PcInstance> pcList = ruunUtil.pcMapList(CASTLE_INNER_MAP);
		if (pcList.isEmpty()) {
			end();
			ruunUtil.objectDeleteList(_npcList_5);
			_npcList_5.clear();
			_npcList_5 = null;
		}
		pcList.clear();
		pcList = null;
	}
	
	public boolean monsterCheck(RuunRound stage){// 몬스터 체크
		FastTable<L1NpcInstance> list = null;
		switch(stage){
		case DOLL_MASTER:	list = _npcList_1;break;// 인형 술사
		case SHADOW_SCOUTS:	list = _npcList_2;break;// 정찰 단장
		case DR_UMA:		list = _npcList_3;break;// 유마 박사
		case MULTIPLE_FACE:	list = _npcList_4;break;// 다중 가면
		case PHANTOM_SWORD:	list = _npcList_5;break;// 검귀
		default:break;
		}
		return stageCheck(stage, list);
	}
	
	private boolean stageCheck(RuunRound stage, FastTable<L1NpcInstance> list){
		for (L1NpcInstance npc : list) {
			if (npc == null) {
				continue;
			}
			switch(stage){
			case SHADOW_SCOUTS:
				if (npc.getNpcId() == 70207 && !npc.isDead()) {
					return false;// 정찰 단장
				}
				break;
			case MULTIPLE_FACE:
				if (npc.getNpcId() == 70209 && !npc.isDead()) {
					return false;// 다중 가면
				}
				break;
			default:
				if (!npc.isDead()) {
					return false;
				}
				break;
			}
		}
		return true;
	}
	
	public void ruunNpcDeathCheck(L1MonsterInstance mon){
		int openDoorId = 0;
		switch(mon.getMapId()){
		case START_MAP:// 루운 광장
			if (mon.getNpcId() == 70206) {
				openDoorId = 6008;// 인형 술사
			}
			break;
		case CASTLE_MAP:// 루운 성
			if ((mon.getX() >= 32896 && mon.getX() <= 32943) && (mon.getY() >= 32738 && mon.getY() <= 32794) && monsterCheck(RuunRound.DR_UMA)) {
				openDoorId = 6010;// 유마 박사
			} else if (mon.getNpcId() == 70207) {
				openDoorId = 6009;// 정찰 단장
			}
			break;
		case CASTLE_INNER_MAP:// 루운 내성
			if (mon.getNpcId() == 70209) {
				openDoorId = 6011;// 다중 가면
			}
			break;
		case CROWN_MAP:// 군주의 방
			break;
		}
		if (openDoorId > 0) {
			ruunDoorOpenAction(openDoorId);
		}
	}
	
	private void ruunDoorOpenAction(int doorId){
		for (L1DoorInstance door : L1World.getInstance().getAllDoor()) {
			if (door.getDoorId() == doorId && door.getOpenStatus() == ActionCodes.ACTION_Close) {
				door.open();
				if (Config.DUNGEON.RUUN_CASTLE_TIMER_ACTIVE && doorId == 6011 && !isRun()) {
					start();// 4지역 타이머 시작
				}
				break;
			}
		}
	}
	
	public void ruunDoorCloseAction(L1DoorInstance door){
		switch(door.getDoorId()){
		case 6009:spawnMonster(RuunRound.SHADOW_SCOUTS);break;// 정찰 단장
		case 6010:spawnMonster(RuunRound.DR_UMA);break;// 유마 박사
		case 6011:spawnMonster(RuunRound.MULTIPLE_FACE);break;// 다중 가면
		}
	}
	
	public void start(){// 쓰레드 시작
		_isRun		= true;
		_limitTime	= FINAL_ROUND_TIME;
		GeneralThreadPool.getInstance().execute(this);
	}
	
	public void end(){// 쓰레드 종료
		_isRun		= false;
		_limitTime	= 0;
	}
	
	public void returnTeleport(){// 리턴 텔레포트
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null) {
				continue;
			}
			if (pc.getMapId() >= CASTLE_MAP && pc.getMapId() <= CROWN_MAP) {
				int[] loc = (int[])CommonUtil.randomChoice(BACK_LOC);
				pc.getTeleport().start(loc[0], loc[1], START_MAP, pc.getMoveState().getHeading(), true);
			}
		}
		ruunUtil.objectDelete(CASTLE_MAP);
		ruunUtil.objectDelete(CASTLE_INNER_MAP);
		ruunUtil.objectDelete(CROWN_MAP);
		spawnMonster(RuunRound.SHADOW_SCOUTS);// 정찰 단장
		spawnMonster(RuunRound.DR_UMA);// 유마 박사
		spawnMonster(RuunRound.MULTIPLE_FACE);// 다중 가면
	}
	
	private void spawnMonster(RuunRound stage){// 몬스터 배치
		switch(stage){
		case DOLL_MASTER:	_npcList_1 = ruunUtil.spawn(stage);break;
		case SHADOW_SCOUTS:	_npcList_2 = ruunUtil.spawn(stage);break;
		case DR_UMA:		_npcList_3 = ruunUtil.spawn(stage);break;
		case MULTIPLE_FACE:	_npcList_4 = ruunUtil.spawn(stage);break;
		case PHANTOM_SWORD:	_npcList_5 = ruunUtil.spawn(stage);break;
		default:break;
		}
	}

}


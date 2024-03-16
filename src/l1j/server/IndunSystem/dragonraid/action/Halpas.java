package l1j.server.IndunSystem.dragonraid.action;

import l1j.server.IndunSystem.dragonraid.DragonRaidHandler;
import l1j.server.IndunSystem.dragonraid.DragonRaidStage;
import l1j.server.IndunSystem.dragonraid.DragonRaildType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 할파스 레이드
 * @author LinOffice
 */
public class Halpas extends DragonRaidHandler {
	private L1NpcInstance _halpasShape, _halpasDragon;
	
	public Halpas(int mapId, DragonRaildType raidType){
		super(mapId, raidType);
	}

	@Override
	protected void ready() throws Exception {
		if (_pcList != null && !_pcList.isEmpty()) {
			for (L1PcInstance pc : _pcList) {
			    if (isWake() == false && pc != null && ((pc.getX() >= 32767 && pc.getX() <= 32876) && (pc.getY() >= 32807 && pc.getY() <= 32911))) {
			    	_stage	= DragonRaidStage.START;
				    break;
			    }
			}
		}
	}

	@Override
	protected void start() throws Exception {
		_stage			= DragonRaidStage.CHECK;
		Thread.sleep(SLEEP * 1000);// 슬립
		setWake(true);
		_raidUtil.mapGreenMsgSend(_pcList, "$31995");// 오랜만에 느껴지는 사람 향기구나!!
		_halpasShape	= _raidUtil.spawn(32789, 32895, (short) _map, 5, 900516, 0);// 할파스의 형상 스폰
	}

	@Override
	protected void result() throws Exception {
		if (_halpasDragon != null && _halpasDragon.isDead()) {
			_stage = DragonRaidStage.END;
			raidCompleteBuff();
			_raidUtil.mapGreenMsgSend(_pcList, "$31989");// 크으윽.. 분하다.. 나의 몸이 온전하기만 했다면...
			Thread.sleep(8000L);
			_raidUtil.mapGreenMsgSend(_pcList, "$31998");// 오랜만에 재미있는 놈들이 왔구나
			Thread.sleep(8000L);
			_raidUtil.mapServerMsgSend(_pcList, 1584);// 난쟁이의 외침: 어서 이 곳을 떠나세요. 곧 문이 닫힐 것입니다.
			Thread.sleep(8000L);
			_raidUtil.mapServerMsgSend(_pcList, 1476);// 시스템 메세지: 30초 후에 텔레포트 합니다.
			Thread.sleep(30000L);
		}
	}

	@Override
	protected void end() throws Exception {
		closeTeleport();
		setWake(false);
	}

	@Override
	protected void fail() throws Exception {
		_stage = DragonRaidStage.READY;
		_raidUtil.failDelete(_map);
	}
	
	@Override
	protected void check() throws Exception {
		if (_halpasShape != null && _halpasShape.isDead()) {
			_stage = DragonRaidStage.RESULT;
			_raidUtil.mapGreenMsgSend(_pcList, "$31996");// 감히 나를 화나게 하다니...
			Thread.sleep(8000L); //슬립
			_raidUtil.mapWindowSizeSend(_pcList);
			_halpasDragon = _raidUtil.spawn(32789, 32895, (short) _map, 5, 900519, 0); 	// 할파스 스폰
		}
	}
	
	@Override
	protected void createSpwan(L1PcInstance pc) {
		System.out.println("■■■■■■■■■■ Halpas Raid Start ■■■■■■■■■■ MAP - " + _map);
		L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 5, 900510, 0, 3600000, _map);	//	드래곤 포탈
		L1SpawnUtil.spawn2(32748, 32900, (short) _map, 5, 900511, 0, 3600000, _map);			//	할파스의 은신처 레어입장
		L1SpawnUtil.spawn2(32738, 32899, (short) _map, 4, 900512, 0, 3600000, _map);			//	자켄
		L1SpawnUtil.spawn2(32726, 32910, (short) _map, 3, 900513, 0, 3600000, _map);			//	잡화상 쿠박
		L1SpawnUtil.spawn2(32731, 32917, (short) _map, 5, 900514, 0, 3600000, _map);			//	창고지기 도람
		L1SpawnUtil.spawn2(32739, 32916, (short) _map, 6, 900515, 0, 3600000, _map);			//	강화버프 오림
	}

	@Override
	public void raidStart() {
		GeneralThreadPool.getInstance().schedule(this, 5000);
	}

	@Override
	protected void raidClose() {
		dispose();
	}
	
	@Override
	protected void dispose() {
		super.dispose();
		System.out.println("■■■■■■■■■■ Halpas Raid Ends ■■■■■■■■■■ MAP - " + _map);
	}
}


package l1j.server.IndunSystem.dragonraid.action;

import l1j.server.IndunSystem.dragonraid.DragonRaidHandler;
import l1j.server.IndunSystem.dragonraid.DragonRaidStage;
import l1j.server.IndunSystem.dragonraid.DragonRaildType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 안타라스 레이드
 * @author LinOffice
 */
public class Antaras extends DragonRaidHandler {
	
	public Antaras(int mapId, DragonRaildType raidType){
		super(mapId, raidType);
	}

	@Override
	protected void ready() throws Exception {
	/*	for (L1PcInstance pc : _pcList) {
	    	if (isAnta() == false && pc != null && ((pc.getX() >= 32748 && pc.getX() <= 32828) && (pc.getY() >= 32652 && pc.getY() <= 32731)))
		    	stage = DragonRaidStage.START;
		}*/
		_stage = DragonRaidStage.START;
	}

	@Override
	protected void start() throws Exception {
		_stage = DragonRaidStage.RESULT;
		Thread.sleep(20 * 1000); // 2분 슬립
		setWake(true);
		_raidUtil.mapServerMsgSend(_pcList, 1570);// 안타라스: 나의 잠을 깨우는 자... 누구인가...
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1571);// 크레이: 안타라스..! 이번에야말로 그동안의 원한을 갚고야 말겟다
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1572);// 안타라스: 하찮은 마물 주제에 포기를 모르는구나
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1573);// 안타라스: 어리석은 자여! 나의 분노를 자극하는구나
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1574);// 크레이: 용사들이여 그대들의 칼에 아덴의 운명이 걸려있다
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1578);// 안타라스: 감히 나를 상대하려 하다니.. 그러고도 너희가 살길 바라느냐?
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1579);// 안타라스: 나의 분노가 하늘에 닿았다. 이제 곧 나의 아버지가 나설 것이다.
		Thread.sleep(3000);
		_raidUtil.spawn(32780, 32693, (short) _map, CommonUtil.random(8), 900013, 0); // 안타라스 스폰
	}

	@Override
	protected void result() throws Exception {
		if (_raidUtil.dieCheck(_map, 900013)) {
			_stage = DragonRaidStage.END;
			raidCompleteBuff();
			_raidUtil.mapServerMsgSend(_pcList, 1580);// 안타라스: 크륵.. 땅 밑의 버러지 같은 놈들에게 당하다니..
			Thread.sleep(3000);
			_raidUtil.mapServerMsgSend(_pcList, 1581);// 크레이: 오오.. 피눈물만 흐르던 내 눈으로 드디어
			Thread.sleep(3000);
			_raidUtil.mapServerMsgSend(_pcList, 1584);// 난쟁이의 외침: 어서 이 곳을 떠나세요. 곧 문이 닫힐 것입니다.
			Thread.sleep(3000);
			L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(1593), true);//난쟁이의 외침: 안타라스의 검은 숨결을 멈추게 한 용사들이 탄생 하였습니다.
			Thread.sleep(3000);
			_raidUtil.mapServerMsgSend(_pcList, 1476);// 30초 후에 텔레포트//시스템 메세지: 30초 후에 텔레포트 합니다.
			Thread.sleep(30000);
		} else if (isWake() == false) {
			_stage = DragonRaidStage.FAIL;
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
	}
	
	@Override
	protected void createSpwan(L1PcInstance pc) {
		System.out.println("■■■■■■■■■■ Antharas Raid Start ■■■■■■■■■■ MAP - " + _map);
		L1SpawnUtil.spawn2(32740, 32794, (short) 37, 5, 900300, 0, 3600000, _map); // 텔레포터
		L1SpawnUtil.spawn2(33449, 32805, (short) 4, 5, 900304, 0, 3600000, _map); // 전조현상
		L1SpawnUtil.spawn2(33447, 32793, (short) 4, 5, 900304, 0, 3600000, _map); // 전조현상
		L1SpawnUtil.spawn2(33429, 32799, (short) 4, 5, 900304, 0, 3600000, _map); // 전조현상
		L1SpawnUtil.spawn2(33415, 32811, (short) 4, 5, 900304, 0, 3600000, _map); // 전조현상
		L1SpawnUtil.spawn2(33429, 32819, (short) 4, 5, 900304, 0, 3600000, _map); // 전조현상
		L1SpawnUtil.spawn2(33434, 32829, (short) 4, 5, 900304, 0, 3600000, _map); // 전조현상
		
		//L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 5, 900007, 0, 2700000, _map); // 포탈
		//L1SpawnUtil.spawn2(32705, 32668, (short) _map, 5, 900008, 0, 2700000, _map); // 직계형 입구
		//L1SpawnUtil.spawn2(32681, 32680, (short) _map, 5, 900010, 0, 2700000, _map); // 창고지기 파미리드
		//L1SpawnUtil.spawn2(32685, 32662, (short) _map, 5, 900009, 0, 2700000, _map); // 잡화상인 이르바길
		//L1SpawnUtil.spawn2(32689, 32667, (short) _map, 5, 7200026, 0, 2700000, _map); // 크레이
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
		for (L1NpcInstance npc : L1World.getInstance().getAllNpc()) {// 포탈삭제
			if (npc == null) {
				continue;
			}
			if ((npc.getNpcId() == 900007 && npc.getMoveMapId() == _map) || npc.getNpcId() == 900300 || npc.getNpcId() == 900304) {
				npc.deleteMe();
			}
		}
		System.out.println("■■■■■■■■■■ Antharas Raid End ■■■■■■■■■■ MAP - " + _map);
	}
}


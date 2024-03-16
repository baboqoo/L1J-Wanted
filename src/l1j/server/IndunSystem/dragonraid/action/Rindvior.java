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
 * 린드비오르 레이드
 * @author LinOffice
 */
public class Rindvior extends DragonRaidHandler {
	
	public Rindvior(int mapId, DragonRaildType raidType){
		super(mapId, raidType);
	}

	@Override
	protected void ready() throws Exception {
	/*	for (L1PcInstance pc : _pcList) {
	    	if (isRind() == false && pc != null && ((pc.getX() >= 32817 && pc.getX() <= 32882) && (pc.getY() >= 32841 && pc.getY() <= 32912)))
		    	_stage = DragonRaidStage.START;
		}*/
		_stage = DragonRaidStage.START;
	}

	@Override
	protected void start() throws Exception {
		_stage = DragonRaidStage.RESULT;
		Thread.sleep(20 * 1000); // 2분 슬립
		setWake(true);
		_raidUtil.mapServerMsgSend(_pcList, 1755);//린드비오르 : 누가 나의 단잠을 방해 하는가? 케레니스 또 너인가?
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1756);//린드비오르 : 다른 놈들이군.. 아직도 정신 못차린 인간들이 남아있었는가..
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1757);//린드비오르 : 흐흐흐.. 좋다. 잠시 놀아주지... 대신..
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1758);//린드비오르 : 오늘 날 막지 못한다면. 지상에 존재하는 모든 인간들의 왕국을 영원히 소멸시켜버릴 것이다!
		Thread.sleep(3000);
		_raidUtil.spawn(32840, 32888, (short) _map, CommonUtil.random(8), 5100, 0); // 린드비오르 스폰
	}

	@Override
	protected void result() throws Exception {
		if (_raidUtil.dieCheck(_map, 5100)) {
			_stage = DragonRaidStage.END;
			raidCompleteBuff();
			_raidUtil.mapServerMsgSend(_pcList, 1772);//린드비오르 : 크아악..! 이런 굴욕이.. 내 힘만 온전했더라도..!!
			Thread.sleep(3000);
			_raidUtil.mapServerMsgSend(_pcList, 1773);//린드비오르: 땅 위를 기어다니는 마물들 주제에 감히 나를..
			Thread.sleep(3000);
			L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(1754), true);// 난쟁이의 외침: 린드비오르의 날개를 꺾은 용사들이 탄생 하였습니다.!!
			Thread.sleep(3000);
			_raidUtil.mapServerMsgSend(_pcList, 1476);// 30초 후에 텔레포트
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
		System.out.println("■■■■■■■■■■ Lindvior Raid Start ■■■■■■■■■■ MAP - " + _map);
		L1SpawnUtil.spawn2(34266, 32837, (short) 15410, 5, 900302, 0, 3600000, _map); // 텔레포터
		L1SpawnUtil.spawn2(34266, 32837, (short) 15410, 5, 900312, 0, 3600000, _map); // 전조현상
		L1SpawnUtil.spawn2(33930, 33352, (short) 4, 5, 900311, 0, 3600000, _map); // 전조현상
		L1SpawnUtil.spawn2(33918, 33348, (short) 4, 5, 900311, 0, 3600000, _map); // 전조현상
		L1SpawnUtil.spawn2(33933, 33333, (short) 4, 5, 900311, 0, 3600000, _map); // 전조현상
		L1SpawnUtil.spawn2(33944, 33334, (short) 4, 5, 900311, 0, 3600000, _map); // 전조현상
		
		//L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 5, 900219, 0, 2700000, _map); // 포탈
		//L1SpawnUtil.spawn2(32717, 32911, (short) _map, 5, 5101, 0, 2700000, _map); // 직계형 입구
		//L1SpawnUtil.spawn2(32748, 32869, (short) _map, 5, 5102, 0, 2700000, _map); // 레어 입구
		//L1SpawnUtil.spawn2(32718, 32913, (short) _map, 5, 5103, 0, 2700000, _map); // 폭포 구조물
		//L1SpawnUtil.spawn2(32736, 32864, (short) _map, 5, 5104, 0, 2700000, _map); // 시빌레
		//L1SpawnUtil.spawn2(32734, 32878, (short) _map, 5, 5105, 0, 2700000, _map); // 프레디
		//L1SpawnUtil.spawn2(32734, 32860, (short) _map, 5, 60169, 0, 2700000, _map); // 군터
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
		for (L1NpcInstance npc : L1World.getInstance().getAllNpc()) {//포탈삭제
			if (npc == null) {
				continue;
			}
			if ((npc.getNpcId() == 900219 && npc.getMoveMapId() == _map) || npc.getNpcId() == 900302 || (npc.getNpcId() == 900311 || npc.getNpcId() == 900312)) {
				npc.deleteMe();
			}
		}
		System.out.println("■■■■■■■■■■ Lindvior Raid Ends ■■■■■■■■■■ MAP - " + _map);
	}
}


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
 * 파푸리온 레이드
 * @author LinOffice
 */
public class Fafurion extends DragonRaidHandler {
	
	public Fafurion(int mapId, DragonRaildType raidType){
		super(mapId, raidType);
	}

	@Override
	protected void ready() throws Exception {
	/*	for (L1PcInstance pc : _pcList) {
	    	if (isFafu() == false && pc != null && ((pc.getX() >= 32929 && pc.getX() <= 33009) && (pc.getY() >= 32804 && pc.getY() <= 32883)))
	    		_stage = DragonRaidStage.START;
		}*/
		_stage = DragonRaidStage.START;
	}

	@Override
	protected void start() throws Exception {
		_stage = DragonRaidStage.RESULT;
		Thread.sleep(20 * 1000); // 2분 슬립
		setWake(true);
		_raidUtil.mapServerMsgSend(_pcList, 1657);// 파푸리온: 크크크.. 아직도 포기하지 못했는가.. 어리석은 무녀여..
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1658);// 무녀 사엘: 파푸리온..! 네놈의 웃음도 오늘이 마지막이 될 것이다
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1659);// 파푸리온: 크크크크.. 네 어리석음 덕분에 오늘도 피의 잔치를 벌이겟구나
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1660);// 무녀 사엘: 이 사악한 드래곤..! 오늘은 기필고 너를 죽이고 저주를 풀것이다.
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1661);// 파푸리온: 가소롭구나! 저들이 너와 함께 이승을 떠돌게 될 나의 제물들인 것이냐!
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1662);// 무녀 사엘: 용사들이여! 저 사악한 파푸리온을 물리치고..
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1663);// 파푸리온: 놀잇감으로는 충분하구나! 흐흐흐..
		Thread.sleep(3000);
		_raidUtil.mapServerMsgSend(_pcList, 1664);// 파푸리온: 뼈 속까지 파고드는 두려움이 무엇인지 이 몸이 알게 해주마
		Thread.sleep(3000);
		_raidUtil.spawn(32959, 32834, (short) _map, CommonUtil.random(8), 900040, 0); // 파푸리온 스폰
	}

	@Override
	protected void result() throws Exception {
		if (_raidUtil.dieCheck(_map, 900040)) {
			_stage = DragonRaidStage.END;
			raidCompleteBuff();
			_raidUtil.mapServerMsgSend(_pcList, 1668);//파푸리온: 크르륵.. 이 내가.. 하찮은 존재에게.. 방심하다니..!
			Thread.sleep(3000);
			_raidUtil.mapServerMsgSend(_pcList, 1669);//무녀 사엘: 드디어 마지막인가..! 이 원한이..!
			Thread.sleep(3000);
			_raidUtil.mapServerMsgSend(_pcList, 1670);//파푸리온: 이제 물놀이는 끝이다.. 너희들은 이제 내 저주를 피할수 없다!
			Thread.sleep(3000);
			_raidUtil.mapServerMsgSend(_pcList, 1671);//무녀 사엘: 이제 제가 할 수 있는 마지막 힘으로 용사님들을 소환하겟습니다.
			Thread.sleep(3000);
			L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(1682), true);//카임무사의 외침: 파푸리온의 검은 숨결을 멈추게 한 용사들이 탄생 하였습니다.!!
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
		System.out.println("■■■■■■■■■■ Fapurion Raid Start ■■■■■■■■■■ MAP - " + _map);
		L1SpawnUtil.spawn2(32737, 32682, (short) 63, 5, 900301, 0, 3600000, _map); // 텔레포터
		L1SpawnUtil.spawn2(32737, 32682, (short) 63, 5, 900310, 0, 3600000, _map); // 전조현상 
		L1SpawnUtil.spawn2(33599, 33247, (short) 4, 5, 900305, 0, 3600000, _map); // 전조현상 중앙
		L1SpawnUtil.spawn2(33608, 33252, (short) 4, 5, 900308, 0, 3600000, _map); // 전조현상 3시
		L1SpawnUtil.spawn2(33595, 33251, (short) 4, 5, 900306, 0, 3600000, _map); // 전조현상 6시
		L1SpawnUtil.spawn2(33594, 33238, (short) 4, 5, 900307, 0, 3600000, _map); // 전조현상 9시
		L1SpawnUtil.spawn2(33610, 33236, (short) 4, 5, 900309, 0, 3600000, _map); // 전조현상 12시
		
		//L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 5, 900036, 0, 2700000, _map); // 포탈
		//L1SpawnUtil.spawn2(32943, 32670, (short) _map, 5, 900037, 0, 2700000, _map); // 직계형 입구
		//L1SpawnUtil.spawn2(32934, 32680, (short) _map, 5, 900042, 0, 2700000, _map); // 창고지기 베아사무
		//L1SpawnUtil.spawn2(32920, 32664, (short) _map, 5, 900041, 0, 2700000, _map); // 잡화상인 카임사무
		//L1SpawnUtil.spawn2(32928, 32661, (short) _map, 4, 4039009, 0, 2700000, _map); // 사엘
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
			if ((npc.getNpcId() == 900036 && npc.getMoveMapId() == _map) || npc.getNpcId() == 900301 || (npc.getNpcId() >= 900305 && npc.getNpcId() <= 900310)) {
				npc.deleteMe();
			}
		}
		System.out.println("■■■■■■■■■■ Fapurion Raid Ends ■■■■■■■■■■ MAP - " + _map);
	}
}


package l1j.server.IndunSystem.dragonraid.action;

import l1j.server.IndunSystem.dragonraid.DragonRaidHandler;
import l1j.server.IndunSystem.dragonraid.DragonRaidStage;
import l1j.server.IndunSystem.dragonraid.DragonRaildType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 발라카스 레이드
 * @author LinOffice
 */
public class Valakas extends DragonRaidHandler {
	
	public Valakas(int mapId, DragonRaildType raidType){
		super(mapId, raidType);
	}

	@Override
	protected void ready() throws Exception {
	/*	for (L1PcInstance pc : _pcList) {
	    	if (isValla() == false && pc != null && ((pc.getX() >= 32744 && pc.getX() <= 32802) && (pc.getY() >= 32868 && pc.getY() <= 32921)))
		    	stage = DragonRaidStage.START;
		}*/
		_stage = DragonRaidStage.START;
	}

	@Override
	protected void start() throws Exception {
		_stage = DragonRaidStage.RESULT;
//		spawn_fire(); // 횃불 스폰

		Thread.sleep(20000L);
		setWake(true);
		_raidUtil.mapGreenMsgSend(_pcList, "$25548");// 오림:다들 조심하게 놈은 이미 잠에서 깨어낫어
		Thread.sleep(10000L);
		_raidUtil.mapGreenMsgSend(_pcList, "$25549");// 오림:주변의 화염 자네들을 가두기위해
		Thread.sleep(10000L);
		_raidUtil.mapGreenMsgSend(_pcList, "\\aG$25533");// 발라카스:크르르르 귀찮은 벌레들이
		Thread.sleep(2000L);
		L1EffectSpawn.getInstance().spawnEffect(7311168, 5, 32761, 32895, (short) _map); // 번개
		Thread.sleep(500L);
		L1EffectSpawn.getInstance().spawnEffect(7311169, 5, 32761, 32895, (short) _map); // 흑떨어짐
		Thread.sleep(7000L);
		_raidUtil.mapGreenMsgSend(_pcList, "\\aG$25534");// 발라카스:네놈들도 할파스의 권속들이냐
		Thread.sleep(10000L);
		_raidUtil.mapGreenMsgSend(_pcList, "\\aG$25535");// 발라카스:상관없겟지 벌레라면 다 쓸어버리면
		Thread.sleep(10000L);
		L1EffectSpawn.getInstance().spawnEffect(7311166, 5, 32761, 32895, (short) _map); // 발라카스눈
		Thread.sleep(5000L);
		_raidUtil.mapGreenMsgSend(_pcList, "\\aG$25536");// 발라카스:감히 신성한 곳에 더러운 발을 들이민
		
//		_raidUtil.Delete_Door(9000, _map);
		
		for (L1PcInstance pc : _pcList) {
			pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 15930), true); // 등장 미티어 이팩
		}
		Thread.sleep(1000L); 
		_raidUtil.spawn(32761, 32895, (short) _map, CommonUtil.random(8), 7311162, 0); // 발라카스 등장
	}

	@Override
	protected void result() throws Exception {
		if (_raidUtil.dieCheck(_map, 7311162)) {
			_stage = DragonRaidStage.END;
			raidCompleteBuff();			
			Thread.sleep(10000L);
			_raidUtil.mapGreenMsgSend(_pcList, "\\aG$25541");// 발라카스:크르르르 한줌도 안되는 미천한 존재들이 기어코
			L1EffectSpawn.getInstance().spawnEffect(7311169, 5, 32761, 32895, (short) _map);// 흙 떨어짐
			Thread.sleep(10000L);
			_raidUtil.mapGreenMsgSend(_pcList, "\\aG$25542");// 발라카스:오늘은 한발 물러나겟다
			L1EffectSpawn.getInstance().spawnEffect(7311169, 5, 32761, 32895, (short) _map);// 흙 떨어짐
			Thread.sleep(10000L);
			_raidUtil.mapGreenMsgSend(_pcList, "\\aG$25543");// 발라카스:그날이 너희들의 마지막이 될 것이다
			L1EffectSpawn.getInstance().spawnEffect(7311169, 5, 32761, 32895, (short) _map);// 흙 떨어짐
			Thread.sleep(10000L);
			_raidUtil.mapGreenMsgSend(_pcList, "$25544");// 오림:아아 절반의 성공이야
			Thread.sleep(10000L);
			_raidUtil.mapGreenMsgSend(_pcList, "$25545");// 오림:하지만 모두 잘 해주었네
			Thread.sleep(10000L);
			_raidUtil.mapGreenMsgSend(_pcList, "$25546");// 오림:이런 큰일일세 이 곳을 지배하던
			
			for (L1PcInstance pc : _pcList) {
				pc.sendPackets(new S_Effect(pc.getId(), 1249), true);// 땅 흔들림
			}
			L1EffectSpawn.getInstance().spawnEffect(7311169, 5, 32761, 32895, (short) _map);// 흙 떨어짐
			Thread.sleep(10000L);
			_raidUtil.mapGreenMsgSend(_pcList, "$25547");// 오림:모두 서두르게 안전한 곳으로
			L1EffectSpawn.getInstance().spawnEffect(7311169, 5, 32761, 32895, (short) _map);// 흙 떨어짐
			Thread.sleep(4000L);
			_raidUtil.mapServerMsgSend(_pcList, 1476);// 30초 후에 텔레포트
			Thread.sleep(30000L);
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
		System.out.println("■■■■■■■■■■ Valakas Raid Start ■■■■■■■■■■ MAP - " + _map);
		L1SpawnUtil.spawn2(33726, 32244, (short) 15440, 0, 900303, 0, 3600000, _map);// 텔레포터
		L1SpawnUtil.spawn2(33701, 32498, (short) 4, 0, 900313, 0, 3600000, _map);// 전조현상
		L1SpawnUtil.spawn2(33708, 32505, (short) 4, 0, 900313, 0, 3600000, _map);// 전조현상
		L1SpawnUtil.spawn2(33728, 32487, (short) 4, 0, 900313, 0, 3600000, _map);// 전조현상
		L1SpawnUtil.spawn2(33727, 32511, (short) 4, 0, 900313, 0, 3600000, _map);// 전조현상
		
		//L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 0, 7220062, 0, 2700000, _map);// 포탈
		//L1SpawnUtil.spawn2(32753, 32927, (short) _map, 0, 7220063, 0, 2700000, _map);// 입장
		//L1SpawnUtil.spawn2(32739, 32925, (short) _map, 5, 7220064, 0, 2700000, _map);// 버프오림		
		//L1SpawnUtil.spawn2(32733, 32921, (short) _map, 5, 7311159, 0, 2700000, _map);// 데스나이트
		//L1SpawnUtil.spawn2(32727, 32935, (short) _map, 5, 7311161, 0, 2700000, _map);// 잡화상인 쿠컴벨
		//L1SpawnUtil.spawn2(32734, 32937, (short) _map, 5, 7311160, 0, 2700000, _map);// 창고 라즈
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
		for (L1NpcInstance npc : L1World.getInstance().getAllNpc()) {// 포탈, 전조현상 삭제
			if (npc == null) {
				continue;
			}
			if ((npc.getNpcId() == 7220062 && npc.getMoveMapId() == _map) || npc.getNpcId() == 900303 || npc.getNpcId() == 900313) {
				npc.deleteMe();
			}
		}
		System.out.println("■■■■■■■■■■ Valakas Raid Ends ■■■■■■■■■■ MAP - " + _map);
	}

/*	private void spawn_fire(){
		_raidUtil.doorSpawn(32764, 32898, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32763, 32899, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32765, 32899, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32767, 32900, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32766, 32901, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32768, 32901, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32770, 32900, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32773, 32899, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32774, 32900, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32775, 32899, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32778, 32899, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32777, 32898, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32779, 32898, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32779, 32895, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32779, 32894, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32780, 32893, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32779, 32890, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32780, 32890, (short) _map, 9000, 15920); // 횟불					
		_raidUtil.doorSpawn(32780, 32889, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32780, 32887, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32779, 32886, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32780, 32885, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32780, 32884, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32780, 32883, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32779, 32882, (short) _map, 9000, 15920); // 횟불					
		_raidUtil.doorSpawn(32778, 32883, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32777, 32882, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32775, 32883, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32774, 32881, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32772, 32883, (short) _map, 9000, 15920); // 횟불					
		_raidUtil.doorSpawn(32771, 32882, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32770, 32882, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32769, 32883, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32768, 32883, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32769, 32879, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32766, 32883, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32764, 32883, (short) _map, 9000, 15920); // 횟불					
		_raidUtil.doorSpawn(32763, 32884, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32764, 32885, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32761, 32887, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32761, 32888, (short) _map, 9000, 15920); // 횟불					
		_raidUtil.doorSpawn(32761, 32890, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32762, 32890, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32761, 32891, (short) _map, 9000, 15920); // 횟불					
		_raidUtil.doorSpawn(32761, 32893, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32761, 32895, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32762, 32894, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32762, 32896, (short) _map, 9000, 15920); // 횟불					
		_raidUtil.doorSpawn(32762, 32898, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32762, 32899, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32761, 32894, (short) _map, 9000, 15920); // 횟불					
		_raidUtil.doorSpawn(32763, 32892, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32756, 32888, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32761, 32885, (short) _map, 9000, 15920); // 횟불					
		_raidUtil.doorSpawn(32762, 32884, (short) _map, 9000, 15920); // 횟불
		_raidUtil.doorSpawn(32769, 32882, (short) _map, 9000, 15920); // 횟불
	}*/
}


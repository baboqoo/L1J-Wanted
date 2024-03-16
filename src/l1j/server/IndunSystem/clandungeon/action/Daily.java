package l1j.server.IndunSystem.clandungeon.action;

import java.util.ArrayList;

import l1j.server.IndunSystem.clandungeon.ClanDungeonHandler;
import l1j.server.IndunSystem.clandungeon.ClanDungeonType;
import l1j.server.IndunSystem.clandungeon.ClanDungeonUtill;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 데일리 혈맹 던전
 * @author LinOffice
 */
public class Daily extends ClanDungeonHandler {
	private int limitTime = 1200;// 20분
	
	private int stage				= 0;
	private static final int READY	= 0;
	private static final int FIRST	= 1;
	private static final int SECOND	= 2;
	private static final int THIRD	= 3;
	private static final int END	= 4;
	
	private L1NpcInstance[] _boss_1, _boss_2, _boss_3;
	private int bossResultCount = 0;
	private boolean bossCheck_1, bossCheck_2, bossCheck_3;
	
	public Daily(short mapId, String clanName, ClanDungeonType type){
		super(mapId, clanName, type);
	}

	@Override
	protected void startSetting(L1PcInstance pc) {
		System.out.println("■■■■■■■■■■ Daily Pledge Dungeon start ■■■■■■■■■■ MAP - " + _map + " pledge: " + _clanName);
		L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 5, 20900, 0, 60000, _map);	//	혈맹 데일리 포탈
	}

	@Override
	protected void startSpawn() {

	}

	@Override
	protected void timerCheck() throws Exception {
		if (limitTime > 0) {
			limitTime--;
		}
		if (limitTime == 1140) {
			ClanDungeonUtill.sendItem(_map, 6014);// 맹세의 징표(1일)
		} else if (limitTime == 600) {
			//ClanDungeonUtill.ment("혈맹 던전 이용 시간이 10분 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(448) + "10 " + S_SystemMessage.getRefText(453), _map);
		} else if (limitTime == 300) {
			//ClanDungeonUtill.ment("혈맹 던전 이용 시간이 5분 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(448) + "5 " + S_SystemMessage.getRefText(453), _map);
		} else if (limitTime == 60) {
			//ClanDungeonUtill.ment("혈맹 던전 이용 시간이 1분 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(448) + "1 " + S_SystemMessage.getRefText(453), _map);
		} else if (limitTime == 10) {
			//ClanDungeonUtill.ment("혈맹 던전 이용 시간이 10초 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(448) + "10 " + S_SystemMessage.getRefText(454), _map);
		}
		if (limitTime < 1140) {// 인원 체크
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
		case READY:
			stageCount++;
			if (stageCount == 10) {
				//ClanDungeonUtill.ment("1군 몬스터가 출현하기 까지 30초 남았습니다.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(449) + "30 " + S_SystemMessage.getRefText(434), _map);
			} else if (stageCount == 20) {
				//ClanDungeonUtill.ment("1군 몬스터가 출현하기 까지 20초 남았습니다.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(449) + "20 " + S_SystemMessage.getRefText(434), _map);
			} else if (stageCount == 30) {
				//ClanDungeonUtill.ment("1군 몬스터가 출현하기 까지 10초 남았습니다.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(449) + "10 " + S_SystemMessage.getRefText(434), _map);
			} else if (stageCount == 40) {
				//ClanDungeonUtill.ment("곧 몬스터가 습격합니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(552), _map);
			} else if (stageCount == 45) {
				ClanDungeonUtill.sendEffect(10, _map);//	어두워짐
			} else if (stageCount >= 47) {// 1시작
				ClanDungeonUtill.sendEffect(7, _map);//	번쩍임
				_boss_1 = ClanDungeonUtill.spawn(_map, _clantable.getStageList(1, 1), true);	// 1군 보스 스폰
				ClanDungeonUtill.spawn(_map, _clantable.getStageList(1, 1), false);			// 1군 몬스터 스폰
				stageCount=0;
				bossResultCount=0;
				stage = FIRST;
			}
			break;
		case FIRST:
			if (bossCheck(_boss_1)) {
				if (!bossCheck_1) {
					ClanDungeonUtill.sendExp(1, _map);
					ClanDungeonUtill.sendItem(_map, 6000);
					bossCheck_1=true;
				}
				_boss_1 = null;
				stageCount++;
				if (stageCount == 10) {
					//ClanDungeonUtill.ment("2군 몬스터가 출현하기 까지 30초 남았습니다.", _map); // CHECKED OK
					ClanDungeonUtill.ment(S_SystemMessage.getRefText(450) + "30 " + S_SystemMessage.getRefText(434), _map);
				} else if (stageCount == 20) {
					//ClanDungeonUtill.ment("2군 몬스터가 출현하기 까지 20초 남았습니다.", _map); // CHECKED OK
					ClanDungeonUtill.ment(S_SystemMessage.getRefText(450) + "20 " + S_SystemMessage.getRefText(434), _map);
				} else if (stageCount == 30) {
					//ClanDungeonUtill.ment("2군 몬스터가 출현하기 까지 10초 남았습니다.", _map); // CHECKED OK
					ClanDungeonUtill.ment(S_SystemMessage.getRefText(450) + "10 " + S_SystemMessage.getRefText(434), _map);
				} else if (stageCount == 40) {
					//ClanDungeonUtill.ment("곧 몬스터가 습격합니다.", _map); // CHECKED OK
					ClanDungeonUtill.ment(S_SystemMessage.getRefText(552), _map);
				} else if (stageCount == 45) {
					ClanDungeonUtill.sendEffect(10, _map);//	어두워짐
				} else if (stageCount >= 47) {// 2시작
					ClanDungeonUtill.sendEffect(7, _map);//	번쩍임
					_boss_2 = ClanDungeonUtill.spawn(_map, _clantable.getStageList(1, 2), true);	// 2군 보스 스폰
					ClanDungeonUtill.spawn(_map, _clantable.getStageList(1, 2), false);			// 2군 몬스터 스폰
					stageCount=0;
					bossResultCount=0;
					stage = SECOND;
				}
			}
			break;
		case SECOND:
			if (bossCheck(_boss_2)) {
				if (!bossCheck_2) {
					ClanDungeonUtill.sendExp(2, _map);
					ClanDungeonUtill.sendItem(_map, 6001);
					bossCheck_2=true;
				}
				_boss_2 = null;
				stageCount++;
				if (stageCount == 10) {
					//ClanDungeonUtill.ment("3군 몬스터가 출현하기 까지 30초 남았습니다.", _map); // CHECKED OK
					ClanDungeonUtill.ment(S_SystemMessage.getRefText(451) + "30 " + S_SystemMessage.getRefText(434), _map);
				} else if (stageCount == 20) {
					//ClanDungeonUtill.ment("3군 몬스터가 출현하기 까지 20초 남았습니다.", _map); // CHECKED OK
					ClanDungeonUtill.ment(S_SystemMessage.getRefText(451) + "20 " + S_SystemMessage.getRefText(434), _map);
				} else if (stageCount == 30) {
					//ClanDungeonUtill.ment("3군 몬스터가 출현하기 까지 10초 남았습니다.", _map); // CHECKED OK
					ClanDungeonUtill.ment(S_SystemMessage.getRefText(451) + "10 " + S_SystemMessage.getRefText(434), _map);
				} else if (stageCount == 40) {
					//ClanDungeonUtill.ment("곧 몬스터가 습격합니다.", _map);
					ClanDungeonUtill.ment(S_SystemMessage.getRefText(552), _map);
				} else if (stageCount == 45) {
					ClanDungeonUtill.sendEffect(10, _map);//	어두워짐
				} else if (stageCount >= 47) {// 3시작
					ClanDungeonUtill.sendEffect(7, _map);//	번쩍임
					_boss_3 = ClanDungeonUtill.spawn(_map, _clantable.getStageList(1, 3), true);	// 3군 보스 스폰
					ClanDungeonUtill.spawn(_map, _clantable.getStageList(1, 3), false);			// 3군 몬스터 스폰
					stageCount=0;
					bossResultCount=0;
					stage = THIRD;
				}
			}
			break;
		case THIRD:
			if (bossCheck(_boss_3)) {
				if (!bossCheck_3) {
					ClanDungeonUtill.sendExp(3, _map);
					ClanDungeonUtill.sendItem(_map, 6002);
					bossCheck_3=true;
				}
				_boss_3 = null;
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
	
	private boolean bossCheck(L1NpcInstance[] npcArray){
		if (npcArray == null) {
			return true;
		}
		bossResultCount++;
		boolean result = true;
		for (L1NpcInstance npc : npcArray) {
			if (!npc.isDead()) {
				result = false;
				if (bossResultCount <= 100 && bossResultCount % 10 == 0) {
					ClanDungeonUtill.spawnStage(_map, _clantable.getStageList(1, stage), _clantable.getRandomMonsterId(1, stage));
				}
				break;
			}
		}
		return result;
	}

	@Override
	public void startRaid() {
		GeneralThreadPool.getInstance().schedule(this, 5000);
	}

	@Override
	protected void endRaid() {
		System.out.println("■■■■■■■■■■ Daily Pledge Dungeon ends ■■■■■■■■■■ MAP - " + _map + " pledge: " + _clanName);
		dispose();
	}
	
	@Override
	protected void dispose() {
		super.dispose();
		_boss_1 = _boss_2 = _boss_3 = null;
	}

}


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
 * 위클리 혈맹 던전
 * @author LinOffice
 */
public class Weekly extends ClanDungeonHandler {
	private int limitTime = 1200;// 20분
	
	private static enum WeeklyStep {
		READY, START, END
	}
	private WeeklyStep stage	= WeeklyStep.READY;
	
	private L1NpcInstance[] _boss;
	private int bossResultCount = 0;
	
	public Weekly(short mapId, String clanName, ClanDungeonType type){
		super(mapId, clanName, type);
	}

	@Override
	protected void startSetting(L1PcInstance pc) {
		System.out.println("■■■■■■■■■■ Weekly Pledge Dungeon start ■■■■■■■■■■ MAP - " + _map + " pledge: " + _clanName);
		L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 5, 20901, 0, 60000, _map);	//	혈맹 위클리 포탈
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
			ClanDungeonUtill.sendItem(_map, 6015);// 맹세의 징표(주간)
		} else if (limitTime == 600) {
			//ClanDungeonUtill.ment("혈맹 던전 이용 시간이 10분 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(1400) + "10 " + S_SystemMessage.getRefText(453), _map);
		} else if (limitTime == 300) {
			//ClanDungeonUtill.ment("혈맹 던전 이용 시간이 5분 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(1400) + "5 " + S_SystemMessage.getRefText(453), _map);
		} else if (limitTime == 60) {
			//ClanDungeonUtill.ment("혈맹 던전 이용 시간이 1분 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(1400) + "1 " + S_SystemMessage.getRefText(453), _map);
		} else if (limitTime == 10) {
			//ClanDungeonUtill.ment("혈맹 던전 이용 시간이 10초 남았습니다.", _map); // CHECKED OK
			ClanDungeonUtill.ment(S_SystemMessage.getRefText(1400) + "10 " + S_SystemMessage.getRefText(454), _map);
		}
		if (limitTime < 1140) {// 인원 체크
			ArrayList<L1PcInstance> list = ClanDungeonUtill.PcStageCK(_map);
			if(list.isEmpty())running = false;
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
				//ClanDungeonUtill.ment("몬스터가 출현하기 까지 1분 남았습니다.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(435) + "1 " + S_SystemMessage.getRefText(436), _map);
			} else if (stageCount == 40) {
				//ClanDungeonUtill.ment("몬스터가 출현하기 까지 30초 남았습니다.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(435) + "30 " + S_SystemMessage.getRefText(377), _map);
			} else if (stageCount == 50) {
				//ClanDungeonUtill.ment("몬스터가 출현하기 까지 20초 남았습니다.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(435) + "20 " + S_SystemMessage.getRefText(377), _map);
			} else if (stageCount == 60) {
				//ClanDungeonUtill.ment("몬스터가 출현하기 까지 10초 남았습니다.", _map); // CHECKED OK
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(435) + "10 " + S_SystemMessage.getRefText(377), _map);
			} else if (stageCount == 70) {
				//ClanDungeonUtill.ment("곧 몬스터가 습격합니다.", _map);
				ClanDungeonUtill.ment(S_SystemMessage.getRefText(552), _map);
			} else if (stageCount == 75) {
				ClanDungeonUtill.sendEffect(10, _map);//	어두워짐
			} else if (stageCount >= 77) {// 1시작
				ClanDungeonUtill.sendEffect(7, _map);//	번쩍임
				_boss = ClanDungeonUtill.spawn(_map, _clantable.getStageList(2, 1), true);// 보스 스폰
				stageCount=0;
				stage = WeeklyStep.START;
			}
			break;
		case START:
			if (bossCheck(_boss)) {
				_boss = null;
				stage = WeeklyStep.END;
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
		default:
			break;
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
				if (bossResultCount % 5 == 0) {
					int type=1;
					switch(npc.getNpcId()){
					case 20828:type=1;break;
					case 20829:type=2;break;
					case 20830:type=3;break;
					case 20831:type=4;break;
					case 20832:type=5;break;
					case 20833:type=6;break;
					case 20834:type=7;break;
					case 20835:type=8;break;
					case 20836:type=9;break;
					case 20837:type=10;break;
					case 20838:type=11;break;
					}
					ClanDungeonUtill.spawnStage(_map, _clantable.getStageList(2, type), _clantable.getRandomMonsterId(2, type));// 스테이지별 일반 몬스터 스폰
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
		System.out.println("■■■■■■■■■■ Weekly Pledge Dungeon ends ■■■■■■■■■■ MAP - " + _map + " pledge: " + _clanName);
		dispose();
	}

	@Override
	protected void dispose() {
		super.dispose();
		_boss = null;
	}
}


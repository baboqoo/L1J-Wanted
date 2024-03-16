package l1j.server.server.controller.action;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.Config;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.templates.L1NpcNight;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 밤 낮 변화 컨트롤러
 * @author LinOffice
 */
public class GameTimeNight implements ControllerInterface {
	private static class newInstance {
		public static final GameTimeNight INSTANCE = new GameTimeNight();
	}
	public static GameTimeNight getInstance() {
		return newInstance.INSTANCE;
	}
	
	GameTimeClock clock = GameTimeClock.getInstance();
	
	private GameTimeNight(){
		long time	= clock.getGameTime().getSeconds() % 86400;
		NIGHT		= time <= 21600 || time >= 72000;// 6시 ~ 20시 낮
	}
	
	private static boolean NIGHT;
	public static boolean isNight(){
		return NIGHT;
	}

	@Override
	public void execute() {
		long time		= clock.getGameTime().getSeconds() % 86400;
		boolean flag	= time <= 21600 || time >= 72000;// 6시 ~ 20시 낮
		if (NIGHT != flag) {
			NIGHT = flag;
			if (NIGHT) {
				changeNight();
			} else {
				changeDay();
			}
		}
		if (NIGHT && GameServerSetting.FORGOTTEN_ISLAND_LOCAL) {
			forgottenIslandNightSpawn();
		}
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	/**
	 * 밤이 되면 실행할 시스템
	 */
	void changeNight(){
		transNpc(SpawnTable.getNightNpcs());
	}
	
	/**
	 * 낮이 되면 실행할 시스템
	 */
	void changeDay(){
		transNpc(SpawnTable.getDayNpcs());
	}
	
	void transNpc(Map<Integer, L1NpcNight> map){
		if (map.isEmpty()) {
			return;
		}
		int key				= 0;
		L1NpcNight value	= null;
		for (Map.Entry<Integer, L1NpcNight> data : map.entrySet()) {
			key		= data.getKey();
			value	= data.getValue();
			for (L1Object obj : L1World.getInstance().getVisibleObjects(value.getTargetMapId()).values()) {
				if (obj == null || obj instanceof L1MonsterInstance == false || ((L1MonsterInstance) obj).getNpcId() != key) {
					continue;
				}
				((L1MonsterInstance) obj).transForm(value.getNpcId());
			}
		}
	}
	
	int _nightSpawnCnt;
	void forgottenIslandNightSpawn(){
		if (++_nightSpawnCnt >= Config.DUNGEON.ISLAND_NIGHT_SPAWN_DELAY) {
			_nightSpawnCnt = 0;
			ArrayList<L1PcInstance> list = L1World.getInstance().getMapPlayer(70);
			if (list == null || list.isEmpty()) {
				return;
			}
			L1PcInstance near = null;
			for (L1PcInstance pc : list) {
				if (pc == null) {
					continue;
				}
				if (near == null && pc.getRegion() == L1RegionStatus.NORMAL) {
					near = pc;
				}
				pc.sendPackets(L1GreenMessage.ISLAND_NIGHT_SPAWN_MENT);// 어딘가에서 음산한 기운이 느껴집니다.
			}
			if (near == null) {
				return;
			}
			L1Location loc = near.getLocation().randomLocation(20, true);// 유저 근처 좌표
			
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						for (int i=0; i<18; i++) {
							L1SpawnUtil.spawn2(loc.getX(), loc.getY(), (short)loc.getMapId(), 5, 72041, 20, 0, 0);// 늑대인간
							if (i % 3 == 0) {
								L1SpawnUtil.spawn2(loc.getX(), loc.getY(), (short)loc.getMapId(), 5, 72042, 20, 0, 0);// 라이칸스로프
							}
							if (i % 4 == 0) {
								L1SpawnUtil.spawn2(loc.getX(), loc.getY(), (short)loc.getMapId(), 5, 72050, 20, 0, 0);// 늑대인간(강화)
							}
							if (i % 8 == 0) {
								L1SpawnUtil.spawn2(loc.getX(), loc.getY(), (short)loc.getMapId(), 5, 72051, 20, 0, 0);// 라이칸스로프(강화)
							}
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}, 3000L);// 3초 딜레이
		}
	}
}


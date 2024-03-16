package l1j.server.server.controller.action;

import java.util.Random;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1MobGroupSpawn;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 페어리 퀸 스폰 컨트롤러
 * @author LinOffice
 */
public class FairlyQueen implements ControllerInterface {
	private static final Random random = new Random(System.nanoTime());
	private static class newInstance {
		public static final FairlyQueen INSTANCE = new FairlyQueen();
	}
	public static FairlyQueen getInstance() {
		return newInstance.INSTANCE;
	}
	private FairlyQueen(){}
	
	private boolean queenSpawn;
	
	GameTimeClock clock = GameTimeClock.getInstance();
	
	@Override
	public void execute() {
		try {
			long time = clock.getGameTime().getSeconds() % 86400;
			if ((time > 32400 && time < 36000) 
					|| (time > 68400 && time < 72000) 
					|| (time < -32400 && time > -36000) 
					|| (time < -68400 && time > -72000)) {
				if (!queenSpawn) {
					queenSpawn = true;
					fairlyQueenSpawn();
				}
			} else {
				queenSpawn = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}
	
	private void fairlyQueenSpawn() {
		int delay = (random.nextInt(4) * 600000) + 1;
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				int deletetime = (random.nextInt(11) + 10) * 60000;
				L1NpcInstance queen = L1SpawnUtil.spawn(33164, 32284, (short) 4, 5, 70852, 0, deletetime);
				L1MobGroupSpawn.getInstance().doSpawn(queen, 60, true, false);
				for (L1NpcInstance group : queen.getMobGroupInfo().getMember()) {
					new L1NpcDeleteTimer(group, deletetime).begin();
				}
			}
		}, delay);
	}

}


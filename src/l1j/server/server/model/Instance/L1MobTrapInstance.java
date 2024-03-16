package l1j.server.server.model.Instance;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.object.S_TrapObject;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1SpawnUtil;

public class L1MobTrapInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1MobTrapInstance.class.getName());

	public L1MobTrapInstance(L1Npc template) {
		super(template);
	}

	private static final int SLEEP_TIME = 500;
	private boolean AIStart = false;

	/** 유저가 트랩을 발견시 **/
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_TrapObject(this, null), true);
		startTrapAI();
	}

	private synchronized void startTrapAI() {
		if (AIStart == false) {
			AIStart = true;
			new TrapAIThreadImpl().start();
		}
	}

	interface TrapAI {
		public void start();
	}

	class TrapAIThreadImpl implements Runnable, TrapAI {
		public void start() {
			GeneralThreadPool.getInstance().execute(TrapAIThreadImpl.this);
		}

		@Override
		public void run() {
			try {
				while(this != null){
					Thread.sleep(SLEEP_TIME);
					if (onTrapAi()) {
						AIStart = false;
						return;
					}
				}
			} catch (Exception e) {
				//_log.log(Level.WARNING, "TrapAI에 예외가 발생했습니다.", e);
				_log.log(Level.WARNING, "TrapAI caught an exception.", e);
			}
		}
	}
	
	private static final int[] TRAP_SPAWN_MONSTERS = { 7000056, 7000057, 7000058, 7000059, 7000060 };// 나올몹종류
	private boolean onTrapAi() {
		if (L1World.getInstance().getRecognizePlayer(this).size() == 0) {
			return true;
		}
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 0)) {
			if (pc != null) {
				int count = IntRange.random(1) + 3;// 불러낼수치
				for (int i = 0; i < count; i++) {
					L1SpawnUtil.spawn5(getX(), getY(), getMapId(), 5, TRAP_SPAWN_MONSTERS[IntRange.random(TRAP_SPAWN_MONSTERS.length)], 4);
				}
				deleteMe();
				return true;
			}
		}
		return false;
	}
}


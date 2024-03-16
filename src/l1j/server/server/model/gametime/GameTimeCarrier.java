package l1j.server.server.model.gametime;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_GameTime;

public class GameTimeCarrier implements Runnable {
	private L1PcInstance _pc;
	private boolean on = true;

	public GameTimeCarrier(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (!on || _pc == null || _pc.getNetConnection() == null) {
				return;
			}
			long serverTime = GameTimeClock.getInstance().getGameTime().getSeconds();
			if (serverTime % 300 == 0) {
				_pc.sendPackets(new S_GameTime(serverTime), true);
			}
			GeneralThreadPool.getInstance().schedule(this, 1000L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		GeneralThreadPool.getInstance().execute(this);
	}

	public void stop() {
		on = false;
	}
}


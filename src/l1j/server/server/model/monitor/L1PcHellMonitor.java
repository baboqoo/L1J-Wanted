package l1j.server.server.model.monitor;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1PcHellMonitor extends L1PcMonitor {

	public L1PcHellMonitor(L1PcInstance owner) {
		super(owner);
	}

	@Override
	public void execTask() {
		if (_owner.isDead()) {
			return;
		}
		_owner.setHellTime(_owner.getHellTime() - 1);
		if (_owner.getHellTime() <= 0) {
			Runnable r = new L1PcMonitor(_owner) {
				@Override
				public void execTask() {
					_owner.endHell();
				}
			};
			GeneralThreadPool.getInstance().execute(r);
		}
	}
}


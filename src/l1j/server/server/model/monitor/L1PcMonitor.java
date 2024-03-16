package l1j.server.server.model.monitor;

import l1j.server.server.model.Instance.L1PcInstance;

public abstract class L1PcMonitor implements Runnable {
	protected L1PcInstance _owner;

	public L1PcMonitor(L1PcInstance owner) {
		_owner = owner;
	}

	@Override
	public final void run() {
		if (_owner == null || _owner.getNetConnection() == null) {
			return;
		}
		execTask();
	}

	public abstract void execTask();
}


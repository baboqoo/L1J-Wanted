package l1j.server.server.model.monitor;

import l1j.server.server.model.Instance.L1PcInstance;

public class L1PcGhostMonitor extends L1PcMonitor {

	public L1PcGhostMonitor(L1PcInstance owner) {
		super(owner);
	}

	@Override
	public void execTask() {
		if (_owner.isGhost()) {
			_owner.makeEndGhost();
		}
		_owner.endGhost();
	}
}


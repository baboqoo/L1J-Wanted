package l1j.server.server.model.monitor;

import l1j.server.server.model.Instance.L1PcInstance;

public class L1PcInvisDelay extends L1PcMonitor {

	public L1PcInvisDelay(L1PcInstance owner) {
		super(owner);
	}

	@Override
	public void execTask() {
		_owner.addInvisDelayCounter(-1);
	}
}


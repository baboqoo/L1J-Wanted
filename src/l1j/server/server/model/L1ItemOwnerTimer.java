package l1j.server.server.model;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;

public class L1ItemOwnerTimer implements Runnable {
	public L1ItemOwnerTimer(L1ItemInstance item, int timeMillis) {
		_item		= item;
		_timeMillis	= timeMillis;
	}

	@Override
	public void run() {
		_item.setItemOwner(null);
	}

	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, _timeMillis);
	}

	private final L1ItemInstance _item;
	private final int _timeMillis;
}


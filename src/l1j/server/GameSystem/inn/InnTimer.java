package l1j.server.GameSystem.inn;

import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.model.Getback;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class InnTimer extends TimerTask {
	private int _keyMapId;
	private int _keyCount;
	private int _timeMillis;
    private boolean exit;
	public InnTimer(int keyMapId, int keyCount, int timeMillis) {
		_keyMapId	= keyMapId;
		_keyCount	= keyCount;
		_timeMillis	= timeMillis;
	}

	public synchronized void decreaseKey(int count) {
		try {
			int check = _keyCount - count;
			if (check <= 0) {
				run();
			} else {
				_keyCount = check;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void run() {
		try {
			if (exit) {
				return;
			}
			exit = true;
			for (L1PcInstance pc : L1World.getInstance().getMapPlayer(_keyMapId)) {
				int[] loc = Getback.GetBack_Location(pc);
				pc.getTeleport().initPortal(loc[0], loc[1], (short)loc[2], pc.getMoveState().getHeading());
			}
			InnHandler inn = InnHandler.getInstance();
			inn.setINN(_keyMapId, false);
			inn.setInnTimer(_keyMapId, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void begin() {
		Timer timer = new Timer();
		timer.schedule(this, _timeMillis);
	}
}

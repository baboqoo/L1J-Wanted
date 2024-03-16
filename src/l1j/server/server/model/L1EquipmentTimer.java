package l1j.server.server.model;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1EquipmentTimer implements Runnable {
	private long _scheduleTime;
	private boolean _active;

	public L1EquipmentTimer(L1PcInstance pc, L1ItemInstance item, long scheduleTime) {
		_pc				= pc;
		_item			= item;
		_scheduleTime	= scheduleTime;
		_active			= true;
	}

	@Override
	public void run() {
		if (!_active) {
			return;
		}
		if ((_item.getRemainingTime() - 1) > 0) {
			if (_pc.getOnlineStatus() == 0) {
				_item.stopEquipmentTimer(_pc);
			}
			_item.setRemainingTime(_item.getRemainingTime() - 1);
			_pc.getInventory().updateItem(_item, L1PcInventory.COL_REMAINING_TIME);// 시간아이템 실시간보기
			GeneralThreadPool.getInstance().schedule(this, _scheduleTime);
		} else {
			_pc.getInventory().removeItem(_item, 1);
		}
	}

	public void cancel() {
		_active = false;
	}

	private final L1PcInstance _pc;
	private final L1ItemInstance _item;
}


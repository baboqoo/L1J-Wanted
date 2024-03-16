package l1j.server.GameSystem.freebuffshield;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.serverpackets.system.DISABLE_FREE_BUFF_SHIELD;

/**
 * 가호(버프) 패널티 타이머(1초간격 패널티 시간 감소 처리)
 * @author LinOffice
 */
public class FavorLockedTimer implements Runnable {
	protected FreeBuffShieldHandler _parent;
	protected DISABLE_FREE_BUFF_SHIELD _disable_state;
	protected boolean _active;

	protected FavorLockedTimer(FreeBuffShieldHandler parent, DISABLE_FREE_BUFF_SHIELD disable_state) {
		_parent			= parent;
		_disable_state	= disable_state;
		_active			= true;
	}
	
	@Override
	public void run() {
		try {
			if (!_active || _parent == null || _disable_state == null) {
				return;
			}
			// 패널티 시간 감소
			if (_disable_state.add_and_get_favor_locked_time(-1) > 0) {
				GeneralThreadPool.getInstance().schedule(this, 1000L);
				return;
			}
			_active = false;
			_parent.set_disable_state(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}


package l1j.server.GameSystem.freebuffshield;

import l1j.server.server.GeneralThreadPool;

/**
 * PC 플레이 마스터 금빛 버프 타이머(1초간격 시간 감소 처리)
 * @author LinOffice
 */
public class PCMasterGoldenBuffTimer implements Runnable {
	protected GoldenBuffInfo _info;
	protected boolean _active;

	protected PCMasterGoldenBuffTimer(GoldenBuffInfo info) {
		_info = info;
	}
	
	@Override
	public void run() {
		try {
			if (!_active || _info == null || _info.owner == null) {
				return;
			}
			// 버프 시간 감소
			if (--_info.remain_time > 0) {
				GeneralThreadPool.getInstance().schedule(this, 1000L);
				return;
			}
			_info.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}


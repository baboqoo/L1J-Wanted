package l1j.server.server;

/**
 * 반족 주기 처리 추상 Task
 * @author LinOffice
 */
public abstract class RepeatTask implements Runnable {
	
	/**
	 * 부모 생성자
	 * @param interval(주기)
	 */
	public RepeatTask(long interval) {
		_interval	= interval;
		_active		= true;
	}
	
	/**
	 * 반복 주기
	 * @return
	 */
	public long getInterval() {
		return _interval;
	}
	
	/**
	 * 업무 처리
	 */
    public abstract void execute();
	
	@Override
	public final void run() {
		if (!_active) {
			return;
		}
		execute();
		if (_active) {
			GeneralThreadPool.getInstance().schedule(this, _interval);
		}
	}
	
	/**
	 * 반복 종료
	 */
	public void cancel() {
		_active = false;
	}
	
	private boolean _active;
	private long _interval;
}


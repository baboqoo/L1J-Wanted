package l1j.server.server;

/**
 * 한번 실해하는 추상 Task
 * @author LinOffice
 */
public abstract class SingleTask implements Runnable {
	/**
	 * 업무 처리
	 */
	public abstract void execute();

	@Override
	public final void run() {
		if (!_active) {
			return;
		}
		_executed = true;
		execute();
	}

	public void cancel() {
		_active = false;
	}
	
	/**
	 * Task 작동 여부
	 * @return boolean
	 */
	public boolean isActive() {
		return _active;
	}

	public boolean isExecuted() {
		return _executed;
	}

	private boolean _active = true;
	private boolean _executed = false;
}


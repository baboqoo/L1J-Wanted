package l1j.server.server.utils;

public class PerformanceTimer {
	private long _begin = System.currentTimeMillis();

	public void reset() {
		_begin = System.currentTimeMillis();
	}

	public long get() {
		return System.currentTimeMillis() - _begin;
	}
}


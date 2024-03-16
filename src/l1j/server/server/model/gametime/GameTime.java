package l1j.server.server.model.gametime;

public class GameTime extends BaseTime {
	// 2003년 7월 3일 12:00(UTC)이 1월 1일00:00
	protected static final long BASE_TIME_IN_MILLIS_REAL = 1057233600000L;

	@Override
	protected long getBaseTimeInMil() {
		return BASE_TIME_IN_MILLIS_REAL;
	}

	@Override
	protected long makeTime(long timeMillis) {
		long t1 = timeMillis - getBaseTimeInMil();
		if (t1 < 0)
			throw new IllegalArgumentException();
		long t2 = (long) ((t1 * 6) / 1000L);
		long t3 = t2 % 3; // 시간이 3의 배수가 되도록(듯이) 조정
		return (long) (t2 - t3);
	}
}


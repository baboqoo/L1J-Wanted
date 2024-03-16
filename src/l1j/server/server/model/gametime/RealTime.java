package l1j.server.server.model.gametime;

import java.util.Calendar;
import java.util.TimeZone;

public class RealTime extends BaseTime {
	@Override
	protected Calendar makeCalendar() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));// 한국 시간
		cal.setTimeInMillis(0);
		cal.add(Calendar.SECOND, (int) _time);
		return cal;
	}

	@Override
	protected long getBaseTimeInMil() {
		return 0;
	}

	@Override
	protected long makeTime(long timeMillis) {
		long t1 = timeMillis - getBaseTimeInMil();
		if (t1 < 0)
			throw new IllegalArgumentException();
		int t2 = (int) (t1 / 1000L);
		return t2;
	}
}


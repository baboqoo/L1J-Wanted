package l1j.server.server.model.gametime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.server.utils.IntRange;

public abstract class BaseTime {
	protected final long _time;
	protected final Calendar _calendar;

	public BaseTime() {
		this(System.currentTimeMillis());
	}

	public BaseTime(long timeMillis) {
		_time = makeTime(timeMillis);
		_calendar = makeCalendar();
	}

	protected Calendar makeCalendar() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeInMillis(0);
		cal.add(Calendar.SECOND, (int) _time);
		return cal;
	}

	protected abstract long makeTime(long timeMillis);

	protected abstract long getBaseTimeInMil();

	public int get(int field) {
		return _calendar.get(field);
	}

	public long getSeconds() {
		return _time;
	}

	public Calendar getCalendar() {
		return (Calendar) _calendar.clone();
	}

	public boolean isNight() {
		int hour = _calendar.get(Calendar.HOUR_OF_DAY);
		return !IntRange.includes(hour, 6, 19);// 6:00-19:59, 낮이 아니면 true
	}

	public String toString() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
		f.setTimeZone(_calendar.getTimeZone());
		return f.format(_calendar.getTime()) + "(" + getSeconds() + ")";
	}
}

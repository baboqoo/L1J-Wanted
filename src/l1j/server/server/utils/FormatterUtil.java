package l1j.server.server.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import l1j.server.server.model.gametime.RealTimeClock;

public class FormatterUtil {
	private static final SimpleDateFormat T_DOUBLE_FORMATTER	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private static final SimpleDateFormat DEFAULT_FORMATTER		= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static String get_formatter_time(){
		return get_formatter_time(new GregorianCalendar());
	}
	public static String get_tdouble_formatter_time(){
		return get_tdouble_formatter_time(new GregorianCalendar());
	}
	public static synchronized String get_formatter_time(Calendar cal){
		return DEFAULT_FORMATTER.format(cal.getTime());
	}
	public static synchronized String get_tdouble_formatter_time(Calendar cal){
		return T_DOUBLE_FORMATTER.format(cal.getTime());
	}
	public static String get_tdouble_formatter_time(Timestamp ts){
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		if (ts != null)
			cal.setTimeInMillis(ts.getTime());
		return get_tdouble_formatter_time(cal);
	}
}


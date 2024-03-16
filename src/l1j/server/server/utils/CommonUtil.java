package l1j.server.server.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1ItemInstance;

public class CommonUtil {
	private static final Random _rnd	= new Random(System.nanoTime());
	
	/**
	 * 2011.08.05 금액표시
	 * @param number
	 * @return String
	 */
	public static String numberFormat(int number) {
		try {
			NumberFormat nf = NumberFormat.getInstance();
			return nf.format(number);
		} catch (Exception e) {
			return Integer.toString(number);
		}
	}
	
	/**
	 * 2011.08.05 랜덤함수
	 * @param number
	 * @return int
	 */
	public static int random(int number) {
		return _rnd.nextInt(number);
	}
	
	/**
	 * true or false
	 * @return boolean
	 */
	public static boolean nextBoolean() {
		return _rnd.nextBoolean();
	}
	
	/**
	 * 2022.02.19 랜덤함수
	 * @return int
	 */
	public static int nextInt() {
		return _rnd.nextInt();
	}
	
	/**
	 * 2022.02.19 랜덤함수
	 * @return float
	 */
	public static float nextFloat() {
		return _rnd.nextFloat();
	}
	
	/**
	 * 2011.08.05 랜덤함수
	 * @param lbound
	 * @param ubound
	 * @return int
	 */
	public static int random(int lbound, int ubound) {
		return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
	}
	
	/**
	 * 2022.02.19 랜덤함수
	 * @param objArray
	 * @return Object
	 */
	public static Object randomChoice(Object[] objArray){
		return objArray[_rnd.nextInt(objArray.length)];
	}
	
	/**
	 * 2022.02.19 랜덤함수
	 * @param intArray
	 * @return int
	 */
	public static int randomIntChoice(int[] intArray){
		return intArray[_rnd.nextInt(intArray.length)];
	}
	
	/**
	 * 2022.02.19 텍스트 to int array
	 * @param string
	 * @return int[]
	 */
	public static int[] parseIntArray(String str){
		if(StringUtil.isNullOrEmpty(str))return null;
		String[] array 	= str.split(StringUtil.CommaString);
		int[] result	= new int[array.length];
		for(int i=0; i<array.length; i++){
			try{
				result[i] = Integer.parseInt(array[i]);
			}catch(Exception e){}
		}
		return result;
	}
	
	/**
	 * 2011.08.30 데이터포맷
	 * @param type
	 * @return String
	 */
	public static String dateFormat(String type) {
		return new SimpleDateFormat(type, Locale.KOREA).format(Calendar.getInstance().getTime());
	}
	
	/**
	 * 2011.08.30 데이터포맷
	 * @param type
	 * @return String
	 */
	public static String dateFormat(String type, Timestamp date) {
		return new SimpleDateFormat(type, Locale.KOREA).format(date.getTime());
	}
	
	/**
	 * 2011.08.31 아이템 종료 시간
	 * @param item
	 * @param minute
	 */
	public static void SetTodayDeleteTime(L1ItemInstance item, int minute) {
		item.setEndTime(new Timestamp(System.currentTimeMillis() + (60000 * minute)));
	}	
	
	/**
	 * 2011.08.31 아이템 종료 시간 지정(오늘 남은 시간 계산) - 로또 시스템
	 * @param item
	 */
	public static void SetTodayDeleteTime(L1ItemInstance item) {
		int hour = Integer.parseInt(dateFormat("HH"));
		int minute = Integer.parseInt(dateFormat("mm"));
		int time = hour <= 22 && minute < 30 ? (23 - hour) * 60 + (60 - minute) + ~0x0000003B : (23 - hour) * 60 + (60 - minute) + ~0x0000003B + 1440;
		item.setEndTime(new Timestamp(System.currentTimeMillis() + (60000 * time)));
	}	
	
	/**
	 * 2011.08.31 지정시간까지 남은 시간
	 * @param item
	 */
	public static int getRestTime(int hh) {		
		int hour = Integer.parseInt(dateFormat("HH"));
		int minute = Integer.parseInt(dateFormat("mm"));
		return (hh - hour) * 60 - minute;
	}
	
	/**
	 * 하루기준 현재 시간 초 단위 계산
	 * @return int
	 */
	public static int getCurrentDayTimeSeconds(){
		//Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Config.SERVER.TIME_ZONE));		
		return (cal.get(Calendar.HOUR_OF_DAY) * 3600) + (cal.get(Calendar.MINUTE) * 60) + cal.get(Calendar.SECOND);
	}

	//public static final String[] WEEK_DAY_ARRAY		= { "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" };
	//public static final String[] WEEK_DAY_FLAG_ARRAY	= { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
	public static final String[] WEEK_DAY_ARRAY	= { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
	
	/**
	 * 시간에 해당하는 요일을 리턴.
	 * @param time
	 * @return String
	 */
	public static String getYoil(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		return WEEK_DAY_ARRAY[ c.get(Calendar.DAY_OF_WEEK) + ~0x00000000 ];
	}
	
	public static enum DungeonCheckType {
		FAILE, SUCCESS, RESET
	}
	
	/** 던전 입장 시간 체크 **/
	@SuppressWarnings("deprecation")
	public static DungeonCheckType getDungeonTimeCheck(Timestamp accountday, int outtime, int usetime) {
		Timestamp nowday = new Timestamp(System.currentTimeMillis());
		if (accountday != null) {
			long clac = nowday.getTime() - accountday.getTime();
			int hours = nowday.getHours();
			int lasthours = accountday.getHours();
			if (accountday.getDate() != nowday.getDate()) {
				if (clac > 86400000 || hours >= Config.ALT.DAILY_RESET_HOUR || lasthours < Config.ALT.DAILY_RESET_HOUR) {
					return DungeonCheckType.RESET;
				}
			} else {
				if (lasthours < Config.ALT.DAILY_RESET_HOUR && hours >= Config.ALT.DAILY_RESET_HOUR) {
					return DungeonCheckType.RESET;
				}
			}
			if (outtime <= usetime) {
				return DungeonCheckType.FAILE;// 모두사용
			}
			return DungeonCheckType.SUCCESS;
		} else {
			return DungeonCheckType.RESET;
		}
	}
	
	/** 일일 초기화 체크 **/
	@SuppressWarnings("deprecation")
	public static boolean isDayResetTimeCheck(Timestamp resettime){
		if (resettime != null) {
			Timestamp nowday = new Timestamp(System.currentTimeMillis());
			long calc = nowday.getTime() - resettime.getTime();
			int hours = nowday.getHours();
			int lasthours = resettime.getHours();
			if (resettime.getDate() != nowday.getDate()) {
				if (calc > 86400000 || hours >= Config.ALT.DAILY_RESET_HOUR || lasthours < Config.ALT.DAILY_RESET_HOUR) {
					return true;
				}
			} else {
				if (lasthours < Config.ALT.DAILY_RESET_HOUR && hours >= Config.ALT.DAILY_RESET_HOUR) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}
	
	/**
	 * 지정된 시간이 지났는지 체크
	 * @param resettime
	 * @param hour
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	public static boolean isDayResetTimeCheck(Timestamp resettime, int hour){
		if (resettime != null) {
			Timestamp nowday = new Timestamp(System.currentTimeMillis());
			long calc = nowday.getTime() - resettime.getTime();
			int hours = nowday.getHours();
			int lasthours = resettime.getHours();
			if (resettime.getDate() != nowday.getDate()) {
				if (calc > 86400000 || hours >= hour || lasthours < hour) {
					return true;
				}
			} else {
				if (lasthours < hour && hours >= hour) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}
	
	/** 수요일 정기정검 체크 **/
	@SuppressWarnings("deprecation")
	public static boolean isWeekResetCheck(Timestamp accountday) {
		Timestamp nowday = new Timestamp(System.currentTimeMillis());
		if (accountday != null) {
			long clac = nowday.getTime() - accountday.getTime();
			int hours = nowday.getHours();
			int lasthours = accountday.getHours();
			if (accountday.getDate() != nowday.getDate()) {// 날짜가 다를때
				if (clac > 604800000) {
					return true;
				}
				if (nowday.getDay() >= 3 && hours >= 10) {// 수요일 이후
					if (nowday.getDay() == 3 && accountday.getDay() == 3) {
						return true;// 수요일
					}
					if (nowday.getDay() == 4 && clac > 86400000 + (hours > lasthours ? (hours - lasthours + 1) * 3600000 : 3600000)) {
						return true;// 목요일
					}
					if (nowday.getDay() == 5 && clac > 172800000 + (hours > lasthours ? (hours - lasthours + 1) * 3600000 : 3600000)) {
						return true;// 금요일
					}
					if (nowday.getDay() == 6 && clac > 259200000 + (hours > lasthours ? (hours - lasthours + 1) * 3600000 : 3600000)) {
						return true;// 토요일
					}
				}
			} else {// 같은날
				if (nowday.getDay() == 3 && lasthours < 10 && hours >= 10) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 다음 요일 0:일 ~ 6:토 and 시간 취득
	 * @param nextDay
	 * @param nextHours
	 * @return long
	 */
	public static long getNextDayTime(int nextDay, int nextHours) {
		Calendar cal			= Calendar.getInstance();
		int currday				= cal.get(Calendar.DAY_OF_WEEK) + ~0x00000000;// 현재 요일(일:0~토:6)
		int currhour			= cal.get(Calendar.HOUR_OF_DAY);	// 현재 시간
		cal.set(Calendar.DAY_OF_WEEK, nextDay + 1);// 이번주 요일 셋팅
		if ((currday == nextDay && currhour >= nextHours) || currday > nextDay) {// 현재 시간이 ?요일 ?시 이후일경우
			cal.add(Calendar.DATE, 7);// 다음주를 위해 7일을 더한다
		}
		SimpleDateFormat sdf	= new SimpleDateFormat(StringUtil.DateFormatString);
		String formaytime		= sdf.format(cal.getTime());
		Date nextdate			= Date.valueOf(formaytime);// 나머지를 뺀 다음 시간
		sdf = null;
		cal = null;
		return nextdate.getTime() + (nextHours * 3600000);	// 다음 시간
	}
	
	/**
	 * 다음 요일 0:일 ~ 6:토 and 시간 취득
	 * @param nextDay
	 * @param nextHours
	 * @param nextMinut
	 * @return long
	 */
	public static long getNextDayHourMinutTime(int nextDay, int nextHours, int nextMinut) {
		Calendar cal			= Calendar.getInstance();
		int currday				= cal.get(Calendar.DAY_OF_WEEK) + ~0x00000000;// 현재 요일
		int currhour			= cal.get(Calendar.HOUR_OF_DAY);	// 현재 시간
		int currMinut			= cal.get(Calendar.MINUTE);			// 현재 분
		cal.set(Calendar.DAY_OF_WEEK, nextDay + 1);// 이번주 요일 셋팅
		if (currday > nextDay // 날이 지낫을 경우
				|| (currday == nextDay && currhour > nextHours)// 날이 같은대 시간이 지난 경우 
				|| (currday == nextDay && currhour == nextHours && currMinut >= nextMinut)) {// 날과 시간이 같은대 분이 지난 경우
			cal.add(Calendar.DATE, 7);// 다음주를 위해 7일을 더한다
		}
		SimpleDateFormat sdf	= new SimpleDateFormat(StringUtil.DateFormatString);
		String formaytime		= sdf.format(cal.getTime());
		java.sql.Date nextdate	= java.sql.Date.valueOf(formaytime);// 나머지를 뺀 다음 시간
		sdf = null;
		cal = null;
		return nextdate.getTime() + (nextHours * 3600000) + (nextMinut * 60000);	// 다음 시간
	}
	
	/**
	 * 지정된 다음 시간 취득
	 * @param nextHours
	 * @return long
	 */
	@SuppressWarnings("deprecation")
	public static long getNextTime(int nextHours){
		java.util.Date date		= new java.util.Date(System.currentTimeMillis());
		int currHour			= date.getHours();// 현재 시간
		int currMinut			= date.getMinutes();// 현재 분
		long nextTime			= date.getTime();// 현재 시간
		if (currHour >= nextHours) {// 시간이 지난 경우
			nextTime += 86400000;// 시간이 지낫다면 하루를 더한다
			nextTime -= (currHour - nextHours) * 3600000;
		} else {// 시간이 지나지 않앗을 경우
			nextTime += (nextHours - currHour) * 3600000;
		}
		if (currMinut > 0) {
			nextTime -= currMinut * 60000;// 분 제거
		}
		return nextTime;
	}
	
	/**
	 * 지정된 다음 시간 취득
	 * @param nextHours
	 * @param nextMinut
	 * @return long
	 */
	@SuppressWarnings("deprecation")
	public static long getNextTime(int nextHours, int nextMinut){
		java.util.Date date		= new java.util.Date(System.currentTimeMillis());
		int currHour			= date.getHours();// 현재 시간
		int currMinut			= date.getMinutes();// 현재 분
		long nextTime			= date.getTime();// 현재 시간
		if (currHour > nextHours) {// 시간이 지난 경우
			nextTime += 86400000;// 시간이 지낫다면 하루를 더한다
			nextTime -= (currHour - nextHours) * 3600000;
			if (currMinut > nextMinut) {// 분이 지낫을 경우
				nextTime -= 3600000;// 한시간을 뺀다
				nextTime += (60 - currMinut + nextMinut) * 60000;// 다음 분까지의 시간을 더한다
			} else if (currMinut < nextMinut) {// 분이 안지낫을 경우
				nextTime += (nextMinut - currMinut) * 60000;// 다음 분까지의 시간을 더한다
			}
		} else if (currHour == nextHours && currMinut >= nextMinut) {// 시간이 지난 경우
			nextTime += 86400000;// 시간이 지낫다면 하루를 더한다
			if (currMinut > nextMinut) {// 분이 지낫을 경우
				nextTime -= 3600000;// 한시간을 뺀다
				nextTime += (60 - currMinut + nextMinut) * 60000;// 다음 분까지의 시간을 더한다
			}
		} else {// 시간이 안지낫을 경우
			nextTime += ((nextHours - currHour) * 3600000);// 시간을 더한다
			if (currMinut > nextMinut) {// 분이 지낫을 경우
				nextTime -= 3600000;// 한시간을 뺀다
				nextTime += (60 - currMinut + nextMinut) * 60000;// 다음 분까지의 시간을 더한다
			} else if (currMinut < nextMinut) {// 분이 안지낫을 경우
				nextTime += (nextMinut - currMinut) * 60000;// 다음 분까지의 시간을 더한다
			}
		}
		return nextTime;
	}
	
	public static void encode_xor(byte[] source_array, byte[] key_array, int start_index, int size){
		int key_size = key_array.length;
		for (int i=start_index; i<size; ++i)
			source_array[i] ^= key_array[i % key_size];
	}
	
	public static void zigzag(byte[] array, int start_index){
		int size = array.length - start_index;
		if ((size & 1) == 1)
			--size;
		
		for (int i=start_index; i<size; i+=2) {
			int flow_index = i + 1; 
			byte b = array[i];
			array[i] = array[flow_index];
			array[flow_index] = b;
		}
	}
	
	public static void reverse(byte[] array, int start_index){
		int lo = start_index;
		int hi = array.length - 1;
		while (lo < hi) {
			byte b = array[lo];
			array[lo++] = array[hi];
			array[hi--] = b;
		}
	}
	
}


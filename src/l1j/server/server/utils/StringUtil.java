package l1j.server.server.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import l1j.server.server.model.gametime.RealTimeClock;

public class StringUtil {
	private static final DecimalFormat DECIMAL_PATTERN	= new DecimalFormat("###,###,###,###");
	private static final SimpleDateFormat DATE_FORMAT	= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	public static final char DirectorySeparatorChar	= '/';
	public static final char DirectoryExtensionChar	= '.';
	public static final char Eof					= (char)-1;
	
	public static final String EmptyString			= "";
	public static final String EmptyOneString		= " ";
	public static final String PlusString			= "+";
	public static final String MinusString			= "-";
	public static final String ZeroString			= "0";
	public static final String OneString			= "1";
	public static final String SlushString			= "/";
	public static final String CommaString			= ",";
	public static final String ColonString			= ":";
	public static final String PeriodString			= ".";
	public static final String DollarString			= "$";
	public static final String AndString			= "&";
	public static final String EqualsString			= "=";
	public static final String UnderbarString		= "_";
	public static final String TrueString			= "true";
	public static final String FalseString			= "false";
	public static final String DivisionString		= ", ";
	public static final String LineString			= "\r\n";
	
	private static final int SyllablesKorMin		= 0xAC00;
	private static final int SyllablesKorMax		= 0xD7A3;
	private static final int LowerAlphaMin			= 0x61;
	private static final int LowerAlphaMax			= 0x7A;
	private static final int UpperAlphaMin			= 0x41;
	private static final int UpperAlphaMax			= 0x5A;
	private static final int DigitMin				= 0x30;
	private static final int DigitMax				= 0x39;

	private static final HashSet<Integer> SpecialCharacters;
	static {
		byte[] special_characters = new byte[]{
				// !"#$%&'()*+,-./@[\]^_`{|}~:;<=>?
				0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x40, 
				0x5B, 0x5C, 0x5D, 0x5E, 0x5F, 0x60, 0x7B, 0x7C, 0x7D, 0x7E, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F
		};
		SpecialCharacters = new HashSet<Integer>(special_characters.length + 1);
		for (byte b : special_characters) {
			SpecialCharacters.add(new Integer(b));
		}
	}
	
	public static final List<Character> INVALID_KO_CHARACTER = Arrays.asList(new Character[] {
			'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ',
			'ㅛ', 'ㅕ', 'ㅑ', 'ㅐ', 'ㅔ', 'ㅗ', 'ㅓ', 'ㅏ', 'ㅣ', 'ㅠ', 'ㅜ', 'ㅡ', 'ㅒ', 'ㅖ', 'ㅢ', 'ㅟ', 'ㅝ', 'ㅞ', 'ㅙ', 
			'ㅚ', '씹', '좆', '좃', ' ', '$', ':', '-', '_', '!', '"', '·', '$', '%', '&', '/', '(', ')', '=', '?', '¿', '¡',
			'º', 'ª', '^', '`', '+', '*', '´', '¨', '.', ',', ';', '<', '>', '|', '@', '#', '~', '€', '¬', '}', '{', '[', ']'
	});

	public static boolean isDirectorySeparatorChar(char c){
		return c == DirectorySeparatorChar;
	}
	
	public static boolean is_special_character(char c){
		return is_special_character((int)c);
	}
	
	public static boolean is_special_character(int c){
		return SpecialCharacters.contains(c);
	}
	
	public static boolean is_digit(char c){
		return is_digit((int)c);
	}
	
	public static boolean is_digit(int c){
		return IntRange.includes(c, DigitMin, DigitMax);
	}
	
	public static boolean is_upper_alpha(char c){
		return is_upper_alpha((int)c);
	}
	
	public static boolean is_upper_alpha(int c){
		return IntRange.includes(c, UpperAlphaMin, UpperAlphaMax);
	}
	
	public static boolean is_lower_alpha(char c){
		return is_lower_alpha((int)c);
	}
	
	public static boolean is_lower_alpha(int c){
		return IntRange.includes(c, LowerAlphaMin, LowerAlphaMax);
	}
	
	public static boolean is_syllables_kor(char c){
		return is_syllables_kor((int)c);
	}
	
	public static boolean is_syllables_kor(int c){
		return IntRange.includes(c, SyllablesKorMin, SyllablesKorMax);
	}
	
	public static final String DateFormatString			= "yyyy-MM-dd";
	public static final String DateFormatStringMinut	= "yyyy-MM-dd HH:mm";
	public static final String DateFormatStringSeconds	= "yyyy-MM-dd HH:mm:ss";
	
	/*public static String parse_money_string(int price){
		if (price >= 10000) {
			int ten_thousand = price / 10000;
			int thousand = price % 10000;
			return thousand == 0 ? String.format("%,d만", ten_thousand) : String.format("%,d만%,d", ten_thousand, thousand);
		}
		return String.format("%,d", price);
	}*/
	
	public static boolean isNullOrEmpty(String s){
		return s == null || s.equals(EmptyString) || s.length() <= 0;
	}
	
	public static String replace(final String source_string, String old_string, String new_string){
		if (isNullOrEmpty(source_string)) {
			return EmptyString;
		}
		int old_string_length = old_string.length();
		if (old_string_length <= 0) {
			return source_string;
		}
		int i = source_string.lastIndexOf(old_string);
		if (i < 0) {
			return source_string;
		}
		StringBuilder sb = new StringBuilder(source_string);
		while (i >= 0) {
			sb.replace(i, (i + old_string_length), new_string);
			i = source_string.lastIndexOf(old_string, i - 1);
		}
		return sb.toString();
	}
	
	public static String replace(final String source_string, ArrayList<KeyValuePair<String, String>> params){
		if (isNullOrEmpty(source_string)) {
			return EmptyString;
		}
		StringBuilder sb = new StringBuilder(source_string);
		for (KeyValuePair<String, String> pair : params) {
			int old_string_length = pair.key.length();
			int i = sb.lastIndexOf(pair.key);
			if (i < 0) {
				continue;
			}
			while (i >= 0) {
				sb.replace(i, (i + old_string_length), pair.value);
				i = sb.lastIndexOf(pair.key, i - 1);
			}
		}
		return sb.toString();
	}

	public static long convert_datetime_millis(String dateString, String formatString){
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		try {
			Date date = sdf.parse(dateString);
			return date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}

	/*public static String remainTimeString(int remainSeconds) {
		int remain = remainSeconds;
		int day = 0;
		int hour = 0;
		int minute = 0;
		int seconds = 0;
		if (remain >= 86400) {
			day = remain / 86400;
			remain &= 86400 - 1;
		}
		if (remain >= 3600) {
			hour = remain / 3600;
			remain &= 3600 - 1;
		}
		if (remain >= 60) {
			minute = remain / 60;
			remain &= 60 - 1;
		}
		if (remain > 0) {
			seconds = remain;
		}
		StringBuilder sb = new StringBuilder();
		if (day > 0) {
			sb.append(day).append("일 ").append(hour).append("시간 ").append(minute).append("분 ").append(seconds).append("초 남았습니다.");
		} else if (hour > 0) {
			sb.append(hour).append("시간 ").append(minute).append("분 ").append(seconds).append("초 남았습니다.");			
		} else if (minute > 0) {
			sb.append(minute).append("분 ").append(seconds).append("초 남았습니다.");						
		} else {			
			sb.append(seconds).append("초 남았습니다.");						
		}
		return sb.toString();
	}*/
	
	public static long convert_datetime_millis(String dateString){
		return convert_datetime_millis(dateString, DateFormatStringSeconds);
	}
	
	public static String convert_datetime_string(Calendar cal){
		return String.format("%04d-%02d-%02d %02d:%02d:%02d", 
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
	}
	
	public static String convert_date_string(Calendar cal){
		return String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String get_current_datetime(){
		return convert_datetime_string(RealTimeClock.getInstance().getRealTimeCalendar());		
	}
	
	public static String get_current_date(){
		return convert_date_string(RealTimeClock.getInstance().getRealTimeCalendar());		
	}
	
	/**
	 * 문자 형태의 시간을 long타입으로 변환(24:00:00 -> 86400000)
	 * @param str
	 * @return long
	 */
	public static long get_time_string_to_long(String str) {
		String[] array = str.split(ColonString);
		switch (array.length) {
		case 1:
			return Integer.parseInt(array[0]) * 1000L;
		case 2:
			return (Integer.parseInt(array[0]) * 60000L) 
					+ (Integer.parseInt(array[1]) * 1000L);
		case 3:
			return (Integer.parseInt(array[0]) * 3600000L) 
					+ (Integer.parseInt(array[1]) * 60000L) 
					+ (Integer.parseInt(array[2]) * 1000L);
		case 4:
			return (Integer.parseInt(array[0]) * 86400000L) 
					+ (Integer.parseInt(array[1]) * 3600000L) 
					+ (Integer.parseInt(array[2]) * 60000L) 
					+ (Integer.parseInt(array[3]) * 1000L);
		default:
			return 0L;
		}
	}
	
	/**
	 * 문자열에 토근을 삽입하여 이어 붙인다.
	 * @param tok 삽입될 토큰
	 * @param args 이어 붙일 문자열 컬렉션
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String concatToken(String tok, Collection<String> args){
		if (args == null || args.size() <= 0) {
			return EmptyString;
		}
		
		String t = EmptyString;
		StringBuilder sb = new StringBuilder(args.size() * (8 + tok.length()));
		for (String s : args) {
			sb.append(t).append(s);
			t = tok;
		}
		return sb.toString();
	}
	
	/**
	 * T형 컬렉션에 토근을 삽입하여 이어 붙인다.
	 * @param tok 삽입될 토큰
	 * @param args 이어 붙일 T 컬렉션
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static <T> String concatTokenGeneric(String tok, Collection<T> args){
		if (args == null || args.size() <= 0) {
			return EmptyString;
		}
		
		String t = EmptyString;
		StringBuilder sb = new StringBuilder(args.size() * (8 + tok.length()));
		for (T s : args) {
			sb.append(t).append(String.valueOf(s));
			t = tok;
		}
		return sb.toString();
	}
	
	public static String joinAsOnce(CharSequence delimiter, String s1, String s2){
		return new StringBuilder(s1.length() + s2.length() + delimiter.length() + 4)
				.append(s1)
				.append(delimiter)
				.append(s2)
				.toString();
	}
	
	/**
	 * 문자열에 토근을 삽입하여 이어 붙인다.
	 * @param delimiter 구분 기호
	 * @param args 이어 붙일 문자열들
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String join(CharSequence delimiter, String...args){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (8 + delimiter.length()));
		for (String s : args) {
			sb.append(t).append(s);
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 문자열에 토근을 삽입하여 이어 붙인다.
	 * @param delimiter 구분 기호
	 * @param args 이어 붙일 컬렉션
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static <T> String join(CharSequence delimiter, Collection<T> args){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.size() * (8 + delimiter.length()));
		for (T s : args) {
			sb.append(t).append(s);
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code T[]}
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static <T> String join(CharSequence delimiter, T[] args){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (8 + delimiter.length()));
		for (T arg : args) {
			sb.append(t).append(arg);
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * byte 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code byte[]}
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String join(CharSequence delimiter, byte[] args){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
		for (byte arg : args) {
			sb.append(t).append(arg);
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code boolean[]}
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String join(CharSequence delimiter, boolean[] args){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (5 + delimiter.length()));
		for (boolean arg : args) {
			sb.append(t).append(arg);
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code short[]}
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String join(CharSequence delimiter, char[] args){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
		for (char arg : args) {
			sb.append(t).append(arg);
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code short[]}
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static <T> String join(CharSequence delimiter, short[] args){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
		for (short arg : args) {
			sb.append(t).append(arg);
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code int[]}
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static <T> String join(CharSequence delimiter, int[] args){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (4 + delimiter.length()));
		for (int arg : args) {
			sb.append(t).append(arg);
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code long[]}
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static <T> String join(CharSequence delimiter, long[] args){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (8 + delimiter.length()));
		for (long arg : args) {
			sb.append(t).append(arg);
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code byte[]}
	 * @param radix 16일 경우 16진수로 변환
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String join(CharSequence delimiter, byte[] args, int radix){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (1 + delimiter.length()));
		for (byte arg : args) {
			sb.append(t);
			if (radix == 16) {
				sb.append(Integer.toHexString(Byte.toUnsignedInt(arg)));
			} else {
				sb.append(arg);
			}
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * char로 쓰지 않고 int로 변환한다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code short[]}
	 * @param radix 16일 경우 16진수로 변환
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String join(CharSequence delimiter, char[] args, int radix){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
		for (char arg : args) {
			sb.append(t);
			if (radix == 16) {
				sb.append(Integer.toHexString(arg));
			} else {
				sb.append((int)arg);
			}
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code short[]}
	 * @param radix 16일 경우 16진수로 변환
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String join(CharSequence delimiter, short[] args, int radix){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
		for (short arg : args) {
			sb.append(t);
			if (radix == 16) {
				sb.append(Integer.toHexString(arg));
			} else {
				sb.append(arg);
			}
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code int[]}
	 * @param radix 16일 경우 16진수로 변환
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String join(CharSequence delimiter, int[] args, int radix){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (4 + delimiter.length()));
		for (int arg : args) {
			sb.append(t);
			if (radix == 16) {
				sb.append(Integer.toHexString(arg));
			} else {
				sb.append(arg);
			}
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * 배열에 토큰을 삽입하여 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param args 배열 {@code long[]}
	 * @param radix 16일 경우 16진수로 변환
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String join(CharSequence delimiter, long[] args, int radix){
		CharSequence t = EmptyString;
		StringBuilder sb = new StringBuilder(args.length * (8 + delimiter.length()));
		for (long arg : args) {
			sb.append(t);
			if (radix ==  16) {
				sb.append(Long.toHexString(arg));
			} else {
				sb.append(arg);
			}
			t = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * iterable을 {title}{itemValue}{delimiter} 형태로 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param title 아이템 마다 붙을 제목 문자열
	 * @param iterable 변환할 {@code Iterable<T>}
	 * @param size 크기
	 * @return 변환된 문자열
	 **/
	public static <T> String join(CharSequence delimiter, CharSequence title, Iterable<T> iterable, int size){
		StringBuilder sb = new StringBuilder(size * (32 + title.length()));
		CharSequence tok = EmptyString;
		for (T t : iterable) {
			sb.append(tok)
			.append(title)
			.append(t);
			tok = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * collection을 {title}{itemValue}{delimiter} 형태로 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param title 아이템 마다 붙을 제목 문자열
	 * @param collection 변환할 {@code Collection<T>}
	 * @return 변환된 문자열
	 **/
	public static <T> String join(CharSequence delimiter, CharSequence title, Collection<T> collection){
		return join(delimiter, title, collection, collection.size());
	}
	
	/**
	 * {title}{key}={value}{delimiter} 형태로 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param title 아이템 마다 붙을 제목 문자열
	 * @param iterable {@code Iterable<Map.Entry<K, V>>}
	 * @param size 크기
	 * @return 변환된 문자열
	 **/
	public static <K, V> String join(CharSequence delimiter, String title, Iterable<Map.Entry<K, V>> iterable, int size){
		StringBuilder sb = new StringBuilder(size * (32 + title.length()));
		CharSequence tok = EmptyString;
		
		for (Map.Entry<K, V> entry : iterable) {
			sb.append(tok)
			.append(title)
			.append(String.valueOf(entry.getKey()))
			.append(EqualsString)
			.append(String.valueOf(entry.getValue()));
			tok = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * {title}{key}={value}{delimiter} 형태로 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param title 아이템 마다 붙을 제목 문자열
	 * @param entrySet {@code Set<Map.Entry<K, V>>}
	 * @return 변환된 문자열
	 **/
	public static <K, V> String join(CharSequence delimiter, String title, Set<Map.Entry<K, V>> entrySet){
		return join(delimiter, title, entrySet, entrySet.size());
	}
	
	/**
	 * map을 {title}{key}={value}{delimiter} 형태로 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param title 아이템 마다 붙을 제목 문자열
	 * @param map 변환할 {@code Map<K, V>}
	 * @return 변환된 문자열
	 **/
	public static <K, V> String join(CharSequence delimiter, String title, Map<K, V> map){
		return join(delimiter, title, map.entrySet(), map.size());
	}
	
	/**
	 * {title}{key}=[{value1, value2...}]{delimiter} 형태로 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param title 아이템 마다 붙을 제목 문자열
	 * @param iterable 변환할 Iterable{Map.Entry{K, List{V}}}
	 * @param size Iterable의 사이즈
	 * @param valuesDelimiter value list에서 사용될 delimiter
	 * @return 변환된 문자열
	 **/
	public static <K, V> String join(CharSequence delimiter, String title, Iterable<Map.Entry<K, List<V>>> iterable, int size, CharSequence valuesDelimiter){
		StringBuilder sb = new StringBuilder(size * (64 + title.length()));
		CharSequence tok = EmptyString;
		for (Map.Entry<K, List<V>> entry : iterable) {
			List<V> list = entry.getValue();
			sb.append(tok)
			.append(title)
			.append(String.valueOf(entry.getKey()))
			.append("=[")
			.append(join(valuesDelimiter, list))
			.append("]");
			tok = delimiter;
		}
		return sb.toString();
	}
	
	/**
	 * {title}{key}=[{value1, value2...}]{delimiter} 형태로 이어붙인다.
	 * @param delimiter 구분 기호
	 * @param title 아이템 마다 붙을 제목 문자열
	 * @param entrySet 변환할 {@code Set<Map.Entry<K, List<V>>>}
	 * @param valuesDelimiter value list에서 사용될 delimiter
	 * @return 변환된 문자열
	 **/
	public static <K, V> String join(CharSequence delimiter, String title, Set<Map.Entry<K, List<V>>> entrySet, CharSequence valuesDelimiter){
		return join(delimiter, title, entrySet, entrySet.size(), valuesDelimiter);
	}
	
	public static int tryParseInt(String s, int defaultValue){
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {}
		return defaultValue;
	}
	
	public static long tryParseLong(String s, long defaultValue){
		try {
			return Long.parseLong(s);
		} catch (Exception e) {}
		return defaultValue;
	}
	
	public static boolean tryParseBool(String s, boolean defaultValue){
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {}
		return defaultValue;
	}
	
	public static boolean isNumber(String str) {
		if (isNullOrEmpty(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			int ascii = (int)str.charAt(i);
			if (ascii < 48 || ascii > 57) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 문자열을 이어 붙인다.
	 * @param args 이어 붙일 문자열들
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String concat(String... args){
		StringBuilder sb = new StringBuilder(args.length << 3);
		for (String s : args) {
			sb.append(s);
		}
		return sb.toString();
	}
	
	/**
	 * 문자열을 이어 붙인다.
	 * @param args 이어 붙일 문자열 컬렉션
	 * @return 이어진 문자열을 반환한다.
	 **/
	public static String concat(Collection<String> args){
		StringBuilder sb = new StringBuilder(args.size() << 3);
		for (String s : args) {
			sb.append(s);
		}
		return sb.toString();
	}
	
	/**
	 * 숫자에 콤마추가
	 * @param num
	 * @return String
	 */
	public static String comma(double num) {
		return DECIMAL_PATTERN.format(num);
	}
	
	/**
	 * UUID 생성
	 * @return String
	 */
	public static String createUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 날짜 포맷
	 * @param time
	 * @return formatString
	 */
	public static String getFormatDate(Timestamp time) {
		return DATE_FORMAT.format(time);
	}
	
	/**
	 * 한글, 특수문자 url encoding
	 * @param url
	 * @return encode url
	 */
	public static String encodeUrl(String url) {
		try {
			return URLEncoder.encode(url, CharsetUtil.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return EmptyString;
	}
	
	/**
	 * 한글, 특수문자 url decoding
	 * @param url
	 * @return decode url
	 */
	public static String decodeUrl(String url) {
		try {
			return URLDecoder.decode(url, CharsetUtil.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return EmptyString;
	}
}


package l1j.server.server.utils;

import java.nio.charset.Charset;

public class CharsetUtil {
	public static final String UTF_8_STR		= "UTF-8";
	public static final String UTF_16_STR		= "UTF-16";
	public static final String UTF_16BE_STR		= "UTF-16BE";
	public static final String UTF_16LE_STR		= "UTF-16LE";
	public static final String ISO_8859_1_STR	= "ISO-8859-1";
	public static final String US_ASCII_STR		= "US-ASCII";
	public static final String MS_949_STR		= "MS949";
	public static final String EUC_KR_STR		= "EUC-KR";
	
	public static final Charset UTF_8			= Charset.forName(UTF_8_STR);
	public static final Charset UTF_16			= Charset.forName(UTF_16_STR);
	public static final Charset UTF_16BE		= Charset.forName(UTF_16BE_STR);
	public static final Charset UTF_16LE		= Charset.forName(UTF_16LE_STR);
	public static final Charset ISO_8859_1		= Charset.forName(ISO_8859_1_STR);
	public static final Charset US_ASCII		= Charset.forName(US_ASCII_STR);
	public static final Charset MS_949			= Charset.forName(MS_949_STR);
	public static final Charset EUC_KR			= Charset.forName(EUC_KR_STR);
}


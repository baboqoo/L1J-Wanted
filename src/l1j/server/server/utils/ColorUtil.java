package l1j.server.server.utils;

import java.util.concurrent.ConcurrentHashMap;

public class ColorUtil {
	private static final byte[] BLACK_RGB		= getColorRgbBytes(0, 0, 0);
	private static final byte[] WHITE_RGB		= getColorRgbBytes(255, 255, 255);
	private static final byte[] RED_RGB			= getColorRgbBytes(255, 0, 0);
	private static final byte[] GREEN_RGB		= getColorRgbBytes(0, 255, 0);
	private static final byte[] BLUE_RGB		= getColorRgbBytes(0, 0, 255);
	private static final ConcurrentHashMap<String, byte[]> COLOR_BYTE_DATA = new ConcurrentHashMap<>();
	
	public static byte[] getColorRgbBytes(String color){
		byte[] result = COLOR_BYTE_DATA.get(color);
		if (result == null) {
			String[] array = color.split(StringUtil.EmptyOneString);
			result = new byte[array.length];
			for (int i=0; i<array.length; i++) {
				result[i] = (byte)Integer.parseInt(array[i], 16);// 글자색깔 (html글자색상표참조)
			}
			COLOR_BYTE_DATA.put(color, result);
			array = null;
		}
		return result;
	}
	
	public static byte[] getColorRgbBytes(int r, int g, int b) {
		return new byte[]{ (byte) r, (byte) g, (byte) b };
	}
	
	public static byte[] getBlackRgbBytes() {
		return BLACK_RGB;
	}
	
	public static byte[] getWhiteRgbBytes() {
		return WHITE_RGB;
	}
	
	public static byte[] getRedRgbBytes() {
		return RED_RGB;
	}
	
	public static byte[] getGreenRgbBytes() {
		return GREEN_RGB;
	}
	
	public static byte[] getBlueRgbBytes() {
		return BLUE_RGB;
	}
	
}


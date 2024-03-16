package l1j.server.server.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteBufferUtil {
	/**
	 * Charset으로 encode하여 반환한다
	 * @param str
	 * @param charSetName
	 * @return
	 */
	public static ByteBuffer encode(String str, String charSetName){
		return Charset.forName(charSetName).encode(str);
	}
	
	/**
	 * Charset으로 decode하여 반환한다
	 * @param str
	 * @param charSetName
	 * @return
	 */
	public static byte[] decode(String str, String charSetName){
		ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
		return Charset.forName(charSetName).decode(buffer).toString().getBytes();
	}
}

